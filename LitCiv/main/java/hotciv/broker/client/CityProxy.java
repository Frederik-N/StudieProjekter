package hotciv.broker.client;

import frds.broker.ClientProxy;
import frds.broker.Requestor;
import hotciv.broker.common.OperationNames;
import hotciv.framework.City;
import hotciv.framework.Player;

public class CityProxy implements City, ClientProxy {
    private final Requestor requestor;
    private final String objectID;

    public CityProxy(String objectID, Requestor requestor) {
        this.requestor = requestor;
        this.objectID = objectID;
    }

    @Override
    public Player getOwner() {
        return requestor.sendRequestAndAwaitReply(objectID, OperationNames.CITY_GET_OWNER, Player.class);
    }

    @Override
    public int getSize() {
        return requestor.sendRequestAndAwaitReply(objectID, OperationNames.CITY_GET_SIZE, int.class);
    }

    @Override
    public int getTreasury() {
        return requestor.sendRequestAndAwaitReply(objectID, OperationNames.CITY_GET_TREASURY, int.class);
    }

    @Override
    public String getProduction() {
        return requestor.sendRequestAndAwaitReply(objectID, OperationNames.CITY_GET_PRODUCTION, String.class);
    }

    @Override
    public String getWorkforceFocus() {
        return requestor.sendRequestAndAwaitReply(objectID, OperationNames.CITY_GET_WORKFORCE, String.class);
    }

    @Override
    public String getID() {
        return objectID;
    }
}
