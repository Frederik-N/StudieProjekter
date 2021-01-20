package hotciv.stub;

import frds.broker.Servant;
import hotciv.framework.*;
import hotciv.standard.UnitImpl;

import java.util.HashMap;

public class StubGame4 implements Game, Servant {
    Position settler = new Position(3,3);
    Position pCity = new Position(5,5);
    Position pTile = new Position(6,6);
    Position settler2 = new Position(2,3);
    StubUnit unit = new StubUnit(GameConstants.SETTLER, Player.RED);
    StubUnit unit2 = new StubUnit(GameConstants.SETTLER, Player.BLUE);
    StubTile tile = new StubTile(GameConstants.PLAINS);
    StubCity city = new StubCity(Player.GREEN, 1, null, null);
    HashMap<Position, StubUnit> units;
    HashMap<Position, StubTile> tiles;
    HashMap<Position, StubCity> cities;

    public StubGame4() {
        units = new HashMap<>();
        tiles = new HashMap<>();
        cities = new HashMap<>();
        units.put(settler, unit);
        units.put(settler2, unit2);
        tiles.put(pTile ,tile);
        cities.put(pCity ,city);
    }

    @Override
    public Tile getTileAt(Position p) {
        return tiles.get(p);
    }

    @Override
    public Unit getUnitAt(Position p) {
        return units.get(p);
    }

    @Override
    public City getCityAt(Position p) {
        return cities.get(p);
    }

    @Override
    public Player getPlayerInTurn() {
        return null;
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
        StubUnit tempUnit = units.get(from);
        units.remove(from);
        units.put(to, tempUnit);
        return true;
    }

    @Override
    public void endOfTurn() {
    }

    @Override
    public void changeWorkForceFocusInCityAt(Position p, String balance) {

    }

    @Override
    public void changeProductionInCityAt(Position p, String unitType) {
    }

    @Override
    public void performUnitActionAt(Position p) {
    }

    @Override
    public void addObserver(GameObserver observer) {

    }

    @Override
    public void setTileFocus(Position position) {

    }

    @Override
    public String getID() {
        return "test2";
    }
}

