package hotciv.standard;

import hotciv.framework.*;
import hotciv.utility.Utility;

import java.util.*;

/**
 * Skeleton implementation of HotCiv.
 * <p>
 * This source code is from the book
 * "Flexible, Reliable Software:
 * Using Patterns and Agile Development"
 * published 2010 by CRC Press.
 * Author:
 * Henrik B Christensen
 * Department of Computer Science
 * Aarhus University
 * <p>
 * Please visit http://www.baerbak.com/ for further information.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class GameImpl implements Game {
    private AgeWorldStrat ageWorldStrat;
    private WinConditionStrat winConditionStrat;
    private UnitActionStrat unitActionStrat;
    private WorldMapStrat worldMapStrat;
    private BattleStrat battleStrat;
    private GameFactory gameFactory;
    private Player currentPlayer = Player.RED;
    private Player winnerPlayer;
    private int gameAge = -4000;
    private TileImpl[][] tiles;

    private HashMap<Position, CityImpl> cities;
    private HashMap<Position, UnitImpl> units;

    private HashMap<String, int[]> allStats = new HashMap<>();
    private int[] archerStats = {GameConstants.ARCHERDEF, GameConstants.ARCHERATK, GameConstants.ARCHERMOV, GameConstants.ARCHERCOST, 0};
    private int[] legionStats = {GameConstants.LEGIONDEF, GameConstants.LEGIONATK, GameConstants.LEGIONMOV, GameConstants.LEGIONCOST, 0};
    private int[] settlerStats = {GameConstants.SETTLERDEF, GameConstants.SETTLERATK, GameConstants.SETTLERMOV, GameConstants.SETTLERCOST, 0};
    private int[] bombStats = {GameConstants.BOMBDEF, GameConstants.BOMBATK, GameConstants.BOMBMOV, GameConstants.BOMBCOST, 1};
    private List<GameObserver> observers = new ArrayList<>();

    private String objectID;

    public GameImpl(GameFactory gameFactory) {
        this.gameFactory = gameFactory;
        this.worldMapStrat = gameFactory.createWorldMapStrat();
        this.ageWorldStrat = gameFactory.createAgeWorldStrat();
        this.winConditionStrat = gameFactory.createWinConditionStrat();
        this.unitActionStrat = gameFactory.createUnitActionStrat();
        this.battleStrat = gameFactory.createBattleStrat();

        ConstructorCalls();
    }

    private void ConstructorCalls() {
        allStats.put(GameConstants.ARCHER, archerStats);
        allStats.put(GameConstants.LEGION, legionStats);
        allStats.put(GameConstants.SETTLER, settlerStats);
        allStats.put(GameConstants.BOMB, bombStats);
        UnitImpl.initializeStats(allStats);

        cities = worldMapStrat.generateCityMap();
        units = worldMapStrat.generateUnitMap();
        tiles = worldMapStrat.generateTileMap();

        objectID = "game";
    }

    public Tile getTileAt(Position p) {
        return tiles[p.getRow()][p.getColumn()];
    }

    public Unit getUnitAt(Position p) {
        return units.get(p);
    }

    public City getCityAt(Position p) {
        return cities.get(p);
    }

    public Player getPlayerInTurn() {
        return currentPlayer;
    }

    public Player getWinner() {
        return winnerPlayer;
    }

    public int getAge() {
        return gameAge;
    }

    public boolean moveUnit(Position from, Position to) {
        boolean unitOnPosition = getUnitAt(from) != null;
        if (!unitOnPosition) return false;

        if(!canUnitMove(from, to)) return false;

        boolean isToACity = cities.containsKey(to);
        if (isToACity) moveUnitToCity(to);

        BattleResult result = unitBattle(from, to);

        if (result == BattleResult.DEFEAT) {
            observers.forEach(k -> k.worldChangedAt(from));
            return false;
        }
        moveActualUnit(from, to);
        observers.forEach(k -> k.worldChangedAt(from));
        observers.forEach(k -> k.worldChangedAt(to));
        return true;
    }

    enum BattleResult {VICTORY, DEFEAT, NOTHING}

    private BattleResult unitBattle(Position from, Position to) {
        boolean isUnitOnToTile = getUnitAt(to) == null;
        if (isUnitOnToTile) return BattleResult.NOTHING;

        boolean isBattleWon = battleStrat.battle(this, from, to);
        if (isBattleWon) {
            winConditionStrat.incrementBattleWon(currentPlayer);
            return BattleResult.VICTORY;
        }

        if (currentPlayer == Player.RED) {
            winConditionStrat.incrementBattleWon(Player.BLUE);
        } else winConditionStrat.incrementBattleWon(Player.RED);
        removeUnit(from);
        return BattleResult.DEFEAT;
    }

    private boolean canUnitMove(Position from, Position to) {
        UnitImpl tUnit = (UnitImpl) getUnitAt(from);
        // If not flying check if the tile is mountain or ocean
        boolean isFlying = tUnit.getFlying();
        if (!isFlying) {
            boolean isMountain = getTileAt(to).getTypeString().equals(GameConstants.MOUNTAINS);
            if (isMountain) return false;

            boolean isOcean = getTileAt(to).getTypeString().equals(GameConstants.OCEANS);
            if (isOcean) return false;
        }

        boolean isToEmpty = getUnitAt(to) == null;
        if (!isToEmpty) {
            boolean isToOwnUnit = getUnitAt(to).getOwner() == currentPlayer;
            if (isToOwnUnit) return false;
        }

        boolean isFromOwnUnit = currentPlayer != tUnit.getOwner();
        if(isFromOwnUnit) return false;
        // Unit needs to have movecount and cannot move more than 1 tile.
        boolean hasMoveCount = tUnit.getMoveCount() < 1;
        if(hasMoveCount) return false;

        boolean isOnlyOneMove = Math.abs(from.getRow() - to.getRow()) > 1 || Math.abs(from.getColumn() - to.getColumn()) > 1;
        if(isOnlyOneMove) return false;
        return true;
    }

    private void moveActualUnit(Position from, Position to) {
        UnitImpl nUnit = (UnitImpl) getUnitAt(from);
        removeUnit(from);
        nUnit.setMoveCount(nUnit.getMoveCount()-1);
        createUnitAt(to, nUnit);
    }

    private void moveUnitToCity(Position to) {
        CityImpl tCity = (CityImpl) getCityAt(to);
        tCity.changeOwner(currentPlayer);
    }

    public void endOfTurn() {
        // Checking if anyone has won
        winnerPlayer = winConditionStrat.winCondition(this);
        // Nobody has won yet continue
        if (winnerPlayer == null) {
            // Normal turn code continues
            if (currentPlayer == Player.RED) {
                currentPlayer = Player.BLUE;
            } else {
                // Blue ends his turn and round over
                currentPlayer = Player.RED;
                endOfRound();
            }
        }
        observers.forEach(k -> k.turnEnds(currentPlayer, gameAge));
    }

    public void endOfRound() {
        // Advances the age
        gameAge = ageWorldStrat.ageWorld(gameAge);
        winConditionStrat.incrementRounds();
        // Movecount to 1 and every city gets 6 treasury
        updateCitiesAndUnits();
        // If possible produce a unit at the city
        produceUnitAtCities();
    }

    private void produceUnitAtCities() {
        // Check to see if any cities has enough to produce unit
        for (Position p : cities.keySet()) {
            produceUnitAtCityWithEnoughTreasury(p);
            observers.forEach(k -> k.worldChangedAt(p));
        }
    }

    private int getCostOfUnit(String type) {
        UnitImpl tUnit = new UnitImpl(Player.RED, type);
        return tUnit.getCost();
    }

    private void produceUnitAtCityWithEnoughTreasury(Position p) {
        int cost = getCostOfUnit(getCityAt(p).getProduction());
        // Check if the city can afford it
        boolean cityHasTreasuryEnough = getCityAt(p).getTreasury() >= cost;
        if (cityHasTreasuryEnough) {
            ((CityImpl) getCityAt(p)).addToTreasury(-cost);
            produceUnitAtEmptyTile(p);
        }
    }

    private void produceUnitAtEmptyTile(Position p) {
        for (Position p2 : Utility.get8neighborhoodOf(p)) {
            boolean isUnitOnTile = getUnitAt(p2) != null;
            if (!isUnitOnTile) {
                createUnitAt(p2, new UnitImpl(cities.get(p).getOwner(), cities.get(p).getProduction()));
                return;
            }
        }
    }

    private void updateCitiesAndUnits() {
        // Add 6 to each city treasure
        cities.forEach((k, v) -> v.addToTreasury(6));
        // Reset move count for units if they're moveable
        units.forEach((k, v) -> {
            boolean moveable = v.getMoveable();
            if (moveable) v.setMoveCount(v.getMaxMoveCount());
        });
    }

    public void changeWorkForceFocusInCityAt(Position p, String balance) {
        ((CityImpl) getCityAt(p)).setWorkforceFocus(balance);
        observers.forEach(k -> k.worldChangedAt(p));
    }

    public void changeProductionInCityAt(Position p, String unitType) {
        ((CityImpl) getCityAt(p)).setProduction(unitType);
        observers.forEach(k -> k.worldChangedAt(p));
    }

    public void performUnitActionAt(Position p) {
        unitActionStrat.performAction(this, p);
        observers.forEach(k -> k.worldChangedAt(p));
    }

    public void addObserver(GameObserver observer) {
        if(!observers.contains(observer)) observers.add(observer);
    }

    public void setTileFocus(Position position) {
        observers.forEach(k -> k.tileFocusChangedAt(position));
    }

    @Override
    public String getID() {
        return objectID;
    }

    //Methods for testing "subject" in observer pattern
    public List<GameObserver> getObservers() {
        return observers;
    }

    public HashMap<Position, CityImpl> getCities() {
        return cities;
    }

    public void createCityAt(Position p, CityImpl city) {
        cities.put(p, city);
    }

    public void createUnitAt(Position p, UnitImpl unit) {
        units.put(p, unit);
    }

    public void removeUnit(Position p) {
        units.remove(p);
    }

    public void removeCity(Position p) {
        cities.remove(p);
    }

    public static class Builder {
        private AgeWorldStrat builderAgeWorldStrat;
        private WinConditionStrat builderWinConditionStrat;
        private UnitActionStrat builderUnitActionStrat;
        private WorldMapStrat builderWorldMapStrat;
        private BattleStrat builderBattleStrat;

        public Builder () {
            builderAgeWorldStrat = new AlphaAgeWorldStrat();
            builderWinConditionStrat = new AlphaWinConditionStrat();
            builderUnitActionStrat = new AlphaActionStrat();
            builderWorldMapStrat = new AlphaWorldMapStrat();
            builderBattleStrat = new AlphaBattleStrat();
        }

        public Builder setBuilderAgeWorldStrat(AgeWorldStrat builderAgeWorldStrat) {
            this.builderAgeWorldStrat = builderAgeWorldStrat;
            return this;
        }

        public Builder setBuilderWinConditionStrat(WinConditionStrat builderWinConditionStrat) {
            this.builderWinConditionStrat = builderWinConditionStrat;
            return this;
        }

        public Builder setBuilderUnitActionStrat(UnitActionStrat builderUnitActionStrat) {
            this.builderUnitActionStrat = builderUnitActionStrat;
            return this;
        }

        public Builder setBuilderWorldMapStrat(WorldMapStrat builderWorldMapStrat) {
            this.builderWorldMapStrat = builderWorldMapStrat;
            return this;
        }

        public Builder setBuilderBattleStrat(BattleStrat builderBattleStrat) {
            this.builderBattleStrat = builderBattleStrat;
            return this;
        }

        public GameImpl build() {
            return new GameImpl(this);
        }
    }

    private GameImpl(Builder builder) {
        ageWorldStrat = builder.builderAgeWorldStrat;
        winConditionStrat = builder.builderWinConditionStrat;
        unitActionStrat = builder.builderUnitActionStrat;
        worldMapStrat = builder.builderWorldMapStrat;
        battleStrat = builder.builderBattleStrat;

        ConstructorCalls();
    }
}
