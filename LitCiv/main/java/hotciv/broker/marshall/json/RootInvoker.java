package hotciv.broker.marshall.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import frds.broker.Invoker;
import frds.broker.ReplyObject;
import hotciv.broker.common.NamingService;
import hotciv.broker.common.NamingServiceImpl;
import hotciv.broker.common.OperationNames;
import hotciv.framework.City;
import hotciv.framework.Game;
import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.stub.StubCity;

import javax.servlet.http.HttpServletResponse;
import java.rmi.Naming;
import java.rmi.activation.UnknownObjectException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class RootInvoker implements Invoker {
    private Gson gson;
    private Map<String, Invoker> invokerMap;
    private NamingService namingService;

    public RootInvoker(NamingService namingService) {
        gson = new Gson();
        invokerMap = new HashMap<>();
        this.namingService = namingService;

        Invoker gameInvoker = new HotCivGameInvoker(namingService);
        invokerMap.put(OperationNames.GAME_PREFIX, gameInvoker);

        Invoker unitInvoker = new HotCivUnitInvoker(namingService);
        invokerMap.put(OperationNames.UNIT_PREFIX, unitInvoker);

        Invoker cityInvoker = new HotCivCityInvoker(namingService);
        invokerMap.put(OperationNames.CITY_PREFIX, cityInvoker);

        Invoker tileInvoker = new HotCivTileInvoker(namingService);
        invokerMap.put(OperationNames.TILE_PREFIX, tileInvoker);
    }

    @Override
    public ReplyObject handleRequest(String objectId, String operationName, String payload) {

        ReplyObject reply = null;

        String type = operationName.substring(0, operationName.indexOf('-'));
        Invoker subInvoker = invokerMap.get(type);

        try {
            reply = subInvoker.handleRequest(objectId, operationName, payload);
        } catch(Exception e) {
            reply = new ReplyObject(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }

        return reply;
    }
}
