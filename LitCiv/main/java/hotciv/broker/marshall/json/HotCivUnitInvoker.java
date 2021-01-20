package hotciv.broker.marshall.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import frds.broker.Invoker;
import frds.broker.ReplyObject;
import hotciv.broker.common.NamingService;
import hotciv.broker.common.OperationNames;
import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.framework.Tile;
import hotciv.framework.Unit;
import hotciv.stub.StubUnit;

import javax.servlet.http.HttpServletResponse;

public class HotCivUnitInvoker implements Invoker {
    private Gson gson;
    private NamingService namingService;

    public HotCivUnitInvoker(NamingService namingService) {
        this.namingService = namingService;
        gson = new Gson();
    }

    @Override
    public ReplyObject handleRequest(String objectId, String operationName, String payload) {

        ReplyObject reply = null;
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(payload).getAsJsonArray();

        Unit unit = lookupUnit(objectId);
        if(operationName.equals(OperationNames.UNIT_GET_OWNER)) {
            Player owner = unit.getOwner();
            reply = new ReplyObject(HttpServletResponse.SC_OK,gson.toJson(owner));
        } else if(operationName.equals(OperationNames.UNIT_GET_TYPE)) {
            String type = unit.getTypeString();
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(type));
        } else if(operationName.equals(OperationNames.UNIT_GET_MOVECOUNT)) {
            int moveCount = unit.getMoveCount();
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(moveCount));
        } else if(operationName.equals(OperationNames.UNIT_GET_ATTACKINGSTRENGTH)) {
            int attackingStrength = unit.getAttackingStrength();
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(attackingStrength));
        } else if(operationName.equals(OperationNames.UNIT_GET_DEFENSIVESTRENGTH)) {
            int defensiveStrength = unit.getDefensiveStrength();
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(defensiveStrength));
        } else {
            reply = new ReplyObject(HttpServletResponse.SC_NOT_IMPLEMENTED, "Server recieved unknown operation: "+operationName+".");
        }
        return reply;
    }

    private Unit lookupUnit(String objectId) {
        Unit unit = namingService.getUnit(objectId);
        if (unit == null) {
            throw new RuntimeException("Could not lookup Unit: "+objectId);
        }
        return unit;
    }
}
