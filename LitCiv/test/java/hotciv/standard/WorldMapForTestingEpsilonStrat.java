package hotciv.standard;

import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.framework.WorldMapStrat;

import java.util.HashMap;

public class WorldMapForTestingEpsilonStrat implements WorldMapStrat {

    @Override
    public HashMap<Position, CityImpl> generateCityMap() {
        HashMap<Position, CityImpl> cities = new HashMap<>();
        cities.put(new Position(15, 5), new CityImpl(Player.RED, 0, GameConstants.LEGION, GameConstants.foodFocus));
        return cities;
    }

    @Override
    public HashMap<Position, UnitImpl> generateUnitMap() {
        HashMap<Position, UnitImpl> units = new HashMap<>();
        // Archer loses to Legion on Hill
        units.put(new Position(15,15), new UnitImpl(Player.BLUE, GameConstants.LEGION));
        units.put(new Position(15,14), new UnitImpl(Player.RED, GameConstants.ARCHER));
        // Archer loses to Legion Forest
        units.put(new Position(15,10), new UnitImpl(Player.BLUE, GameConstants.LEGION));
        units.put(new Position(15,9), new UnitImpl(Player.RED, GameConstants.ARCHER));
        // Archer loses to Legion in City
        units.put(new Position(15,5), new UnitImpl(Player.BLUE, GameConstants.LEGION));
        units.put(new Position(15,4), new UnitImpl(Player.RED, GameConstants.ARCHER));
        // Settler loses to Legion
        units.put(new Position(13,13), new UnitImpl(Player.RED, GameConstants.SETTLER));
        units.put(new Position(13,12), new UnitImpl(Player.BLUE, GameConstants.LEGION));
        // Legion wins against Settler
        units.put(new Position(13,9), new UnitImpl(Player.RED, GameConstants.SETTLER));
        units.put(new Position(13,8), new UnitImpl(Player.BLUE, GameConstants.LEGION));
        // Archer loses to Legion with Supp
        units.put(new Position(13,1), new UnitImpl(Player.BLUE, GameConstants.LEGION));
        units.put(new Position(13,2), new UnitImpl(Player.BLUE, GameConstants.LEGION));
        units.put(new Position(13,0), new UnitImpl(Player.RED, GameConstants.ARCHER));
        // Red player wins at three attacks
        units.put(new Position(11,0), new UnitImpl(Player.BLUE, GameConstants.SETTLER));
        units.put(new Position(11,2), new UnitImpl(Player.BLUE, GameConstants.SETTLER));
        units.put(new Position(10,1), new UnitImpl(Player.BLUE, GameConstants.SETTLER));
        units.put(new Position(11,1), new UnitImpl(Player.RED, GameConstants.LEGION));
        // Blue player wins at three attacks
        units.put(new Position(8,0), new UnitImpl(Player.RED, GameConstants.SETTLER));
        units.put(new Position(8,2), new UnitImpl(Player.RED, GameConstants.SETTLER));
        units.put(new Position(7,1), new UnitImpl(Player.RED, GameConstants.SETTLER));
        units.put(new Position(8,1), new UnitImpl(Player.BLUE, GameConstants.LEGION));
        return units;
    }

    @Override
    public TileImpl[][] generateTileMap() {
        TileImpl[][] tiles = new TileImpl[GameConstants.WORLDSIZE][GameConstants.WORLDSIZE];
        for (int i = 0; i < GameConstants.WORLDSIZE; i++) {
            for (int j = 0; j < GameConstants.WORLDSIZE; j++) {
                tiles[i][j] = new TileImpl(GameConstants.PLAINS);
            }
        }
        tiles[15][15] = new TileImpl(GameConstants.HILLS);
        tiles[15][10] = new TileImpl(GameConstants.FOREST);

        return tiles;
    }
}
