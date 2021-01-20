package hotciv.broker.client;

import frds.broker.ClientProxy;
import frds.broker.Requestor;
import hotciv.broker.common.OperationNames;
import hotciv.framework.Player;
import hotciv.framework.Unit;

public class UnitProxy implements Unit, ClientProxy {
    private final Requestor requestor;
    private String objectId;

    public UnitProxy(String objectId, Requestor requestor) {
        this.requestor = requestor;
        this.objectId = objectId;
    }

    @Override
    public String getTypeString() {
        return requestor.sendRequestAndAwaitReply(objectId, OperationNames.UNIT_GET_TYPE, String.class);
    }

    @Override
    public Player getOwner() {
        return requestor.sendRequestAndAwaitReply(objectId, OperationNames.UNIT_GET_OWNER, Player.class);
    }

    @Override
    public int getMoveCount() {
        return requestor.sendRequestAndAwaitReply(objectId, OperationNames.UNIT_GET_MOVECOUNT, int.class);
    }

    @Override
    public int getDefensiveStrength() {
        return requestor.sendRequestAndAwaitReply(objectId, OperationNames.UNIT_GET_DEFENSIVESTRENGTH, int.class);
    }

    @Override
    public int getAttackingStrength() {
        return requestor.sendRequestAndAwaitReply(objectId, OperationNames.UNIT_GET_ATTACKINGSTRENGTH, int.class);
    }

    @Override
    public String getID() {
        return objectId;
    }
}
