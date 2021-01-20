package hotciv.broker.marshall.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import frds.broker.Invoker;
import frds.broker.ReplyObject;
import hotciv.broker.common.NamingService;
import hotciv.broker.common.OperationNames;
import hotciv.framework.City;
import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.framework.Tile;
import hotciv.stub.StubCity;

import javax.servlet.http.HttpServletResponse;

public class HotCivCityInvoker implements Invoker {
    private Gson gson;
    private NamingService namingService;

    public HotCivCityInvoker(NamingService namingService) {
        this.namingService = namingService;
        gson = new Gson();
    }

    @Override
    public ReplyObject handleRequest(String objectId, String operationName, String payload) {

        ReplyObject reply = null;
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(payload).getAsJsonArray();

        City city = lookupCity(objectId);
        if(operationName.equals(OperationNames.CITY_GET_OWNER)) {
            Player owner = city.getOwner();
            reply = new ReplyObject(HttpServletResponse.SC_OK,gson.toJson(owner));
        } else if(operationName.equals(OperationNames.CITY_GET_SIZE)) {
            int size = city.getSize();
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(size));
        } else if(operationName.equals(OperationNames.CITY_GET_PRODUCTION)) {
            String production = city.getProduction();
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(production));
        } else if(operationName.equals(OperationNames.CITY_GET_TREASURY)) {
            int treasury = city.getTreasury();
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(treasury));
        } else if(operationName.equals(OperationNames.CITY_GET_WORKFORCE)) {
            String workforce = city.getWorkforceFocus();
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(workforce));
        } else {
            reply = new ReplyObject(HttpServletResponse.SC_NOT_IMPLEMENTED, "Server recieved unknown operation: "+operationName+".");
        }
        return reply;
    }

    private City lookupCity(String objectId) {
        City city = namingService.getCity(objectId);
        if (city == null) {
            throw new RuntimeException("Could not lookup City: "+objectId);
        }
        return city;
    }
}
