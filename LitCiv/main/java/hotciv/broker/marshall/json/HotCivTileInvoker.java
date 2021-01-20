package hotciv.broker.marshall.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import frds.broker.Invoker;
import frds.broker.ReplyObject;
import hotciv.broker.common.NamingService;
import hotciv.broker.common.OperationNames;
import hotciv.framework.*;
import hotciv.stub.StubTile;

import javax.servlet.http.HttpServletResponse;
import java.rmi.Naming;

public class HotCivTileInvoker implements Invoker {
    private Gson gson;
    private NamingService namingService;

    public HotCivTileInvoker(NamingService namingService) {
        this.namingService = namingService;
        gson = new Gson();
    }

    @Override
    public ReplyObject handleRequest(String objectId, String operationName, String payload) {

        ReplyObject reply = null;
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(payload).getAsJsonArray();

        Tile tile = lookupTile(objectId);
        if(operationName.equals(OperationNames.TILE_GET_TYPE)) {
            String type = tile.getTypeString();
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(type));
        } else {
            reply = new ReplyObject(HttpServletResponse.SC_NOT_IMPLEMENTED, "Server recieved unknown operation: "+operationName);
        }
        return reply;
    }

    private Tile lookupTile(String objectId) {
        Tile tile = namingService.getTile(objectId);
        if (tile == null) {
            throw new RuntimeException("Could not lookup Tile: "+objectId);
        }
        return tile;
    }
}
