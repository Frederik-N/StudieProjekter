package hotciv.broker.client;

import frds.broker.ClientProxy;
import frds.broker.Requestor;
import hotciv.broker.common.OperationNames;
import hotciv.framework.Tile;

public class TileProxy implements Tile, ClientProxy {
    private final Requestor requestor;
    private final String objectID;

    public TileProxy(String objectID, Requestor requestor) {
        this.requestor = requestor;
        this.objectID = objectID;
    }

    @Override
    public String getTypeString() {
        return requestor.sendRequestAndAwaitReply(objectID, OperationNames.TILE_GET_TYPE, String.class);
    }

    @Override
    public String getID() {
        return objectID;
    }
}
