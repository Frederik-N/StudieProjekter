package hotciv.broker.client;

import frds.broker.ClientProxy;
import frds.broker.Requestor;
import hotciv.broker.marshall.json.HotCivTileInvoker;
import hotciv.framework.*;
import hotciv.broker.common.OperationNames;
import hotciv.stub.StubCity;
import hotciv.stub.StubTile;
import hotciv.stub.StubUnit;

public class GameProxy implements Game, ClientProxy {
    private final String objectId;
    private final Requestor requestor;
    private GameObserver observer;

    public GameProxy(String objectId, Requestor requestor) {
        this.objectId = objectId;
        this.requestor = requestor;
    }

    @Override
    public Tile getTileAt(Position p) {
        String id = requestor.sendRequestAndAwaitReply(objectId, OperationNames.GET_TILE_AT_OPERATION, String.class, p);
        if (id != null) {
            Tile proxy = new TileProxy(id, requestor);
            return proxy;
        }
        return null;
    }

    @Override
    public Unit getUnitAt(Position p) {
        String id = requestor.sendRequestAndAwaitReply(objectId, OperationNames.GET_UNIT_AT_OPERATION, String.class, p);
        if (id != null) {
            Unit proxy = new UnitProxy(id, requestor);
            return proxy;
        }
        return null;
    }

    @Override
    public City getCityAt(Position p) {
        String id = requestor.sendRequestAndAwaitReply(objectId, OperationNames.GET_CITY_AT_OPERATION, String.class, p);
        if (id != null) {
            City proxy = new CityProxy(id, requestor);
            return proxy;
        }
        return null;
    }

    @Override
    public Player getPlayerInTurn() {
        return requestor.sendRequestAndAwaitReply(objectId, OperationNames.GET_PLAYER_IN_TURN_OPERATION, Player.class);
    }

    @Override
    public Player getWinner() {
        return requestor.sendRequestAndAwaitReply(objectId, OperationNames.GET_WINNER_OPERATION, Player.class);
    }

    @Override
    public int getAge() {
        return requestor.sendRequestAndAwaitReply(objectId, OperationNames.GET_AGE_OPERATION, int.class);
    }

    @Override
    public boolean moveUnit(Position from, Position to) {
        boolean valid = requestor.sendRequestAndAwaitReply(objectId, OperationNames.MOVE_UNIT_OPERATION, boolean.class, from, to);
        if(valid) {
            observer.worldChangedAt(from);
            observer.worldChangedAt(to);
        }
        return valid;
    }

    @Override
    public void endOfTurn() {
        if(getPlayerInTurn()==Player.RED) {
            observer.turnEnds(Player.BLUE, getAge());
        } else {
            observer.turnEnds(Player.RED, getAge()+100);
        }
        requestor.sendRequestAndAwaitReply(objectId, OperationNames.END_OF_TURN_OPERATION, null);
    }

    @Override
    public void changeWorkForceFocusInCityAt(Position p, String balance) {
        requestor.sendRequestAndAwaitReply(objectId, OperationNames.CHANGE_WORK_IN_CITY_AT, null, p, balance);
    }

    @Override
    public void changeProductionInCityAt(Position p, String unitType) {
        requestor.sendRequestAndAwaitReply(objectId, OperationNames.CHANGE_PROD_IN_CITY_AT, null, p, unitType);
    }

    @Override
    public void performUnitActionAt(Position p) {
        requestor.sendRequestAndAwaitReply(objectId, OperationNames.PERFORM_UNIT_ACTION_AT, null, p);
    }

    @Override
    public void addObserver(GameObserver observer) {
        //Maybe local method?
        this.observer = observer;
    }

    @Override
    public void setTileFocus(Position position) {
        // Local method
        observer.tileFocusChangedAt(position);
    }

    @Override
    public String getID() {
        return objectId;
    }
}
