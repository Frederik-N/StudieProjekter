package hotciv.stub;

import frds.broker.Servant;
import hotciv.framework.*;

public class StubGame3 implements Game, Servant {
    Position position_of_green_city = new Position(1,1);
    Position position_of_settler_unit = new Position(3,3);
    Position position_from = new Position(2,2);
    Position position_to = new Position(2,3);
    Player playerInTurn = Player.RED;
    City city = new StubCity(Player.GREEN, 4, null, null);
    City city2 = new StubCity(null, 0, null, null);
    Unit unit = new StubUnit(GameConstants.SETTLER, Player.RED);
    Tile tile = new StubTile(GameConstants.PLAINS);

    @Override
    public Tile getTileAt(Position p) {
        if (p.equals(position_of_settler_unit)) {
            return tile;
        }
        return null;
    }

    @Override
    public Unit getUnitAt(Position p) {
        if (p.equals(position_of_settler_unit)) {
            return unit;
        }
        return null;
    }

    @Override
    public City getCityAt(Position p) {
        if (p.equals(position_of_green_city)) {
            return city;
        } else if (p.equals(position_of_settler_unit)) {
            return city2;
        }
        return null;
    }

    @Override
    public Player getPlayerInTurn() {
        return playerInTurn;
    }

    @Override
    public Player getWinner() {
        return Player.YELLOW;
    }

    @Override
    public int getAge() {
        return 42;
    }

    @Override
    public boolean moveUnit(Position from, Position to) {
        if (from.equals(position_from) && to.equals(position_to)) {
            return true;
        } else return false;
    }

    @Override
    public void endOfTurn() {
        playerInTurn = Player.BLUE;
    }

    @Override
    public void changeWorkForceFocusInCityAt(Position p, String balance) {

    }

    @Override
    public void changeProductionInCityAt(Position p, String unitType) {
        if (p.equals(position_of_green_city) && unitType.equals(GameConstants.ARCHER)) {
            city = new StubCity(Player.GREEN, 4, GameConstants.ARCHER, null);
        }
    }

    @Override
    public void performUnitActionAt(Position p) {
        if (p.equals(position_of_settler_unit)) {
            unit = null;
            city2 = new StubCity(Player.RED, 0, null,null);
        }
    }

    @Override
    public void addObserver(GameObserver observer) {

    }

    @Override
    public void setTileFocus(Position position) {

    }

    @Override
    public String getID() {
        return "test";
    }
}
