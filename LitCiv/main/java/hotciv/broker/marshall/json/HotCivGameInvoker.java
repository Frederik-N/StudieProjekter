package hotciv.broker.marshall.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import frds.broker.Invoker;
import frds.broker.ReplyObject;
import hotciv.broker.common.NamingService;
import hotciv.framework.*;
import hotciv.broker.common.OperationNames;

import javax.servlet.http.HttpServletResponse;

public class HotCivGameInvoker implements Invoker {
    private NamingService namingService;
    private final Gson gson;

    public HotCivGameInvoker(NamingService namingService) {
        this.namingService = namingService;
        gson = new Gson();
    }

    @Override
    public ReplyObject handleRequest(String objectId, String operationName, String payload) {
        ReplyObject reply = null;

        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(payload).getAsJsonArray();

        Game game = lookupGame(objectId);
        if (operationName.equals(OperationNames.GET_WINNER_OPERATION)) {
            Player winner = game.getWinner();
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(winner));

        } else if (operationName.equals(OperationNames.GET_AGE_OPERATION)) {
            int age = game.getAge();
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(age));

        } else if (operationName.equals(OperationNames.GET_CITY_AT_OPERATION)) {
            Position p = gson.fromJson(array.get(0), Position.class);

            City city = game.getCityAt(p);
            if (city != null) {
                String id = city.getID();
                namingService.putCity(id, city);

                reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(id));
            } else {
                reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(null));
            }

        } else if (operationName.equals(OperationNames.GET_UNIT_AT_OPERATION)) {
            Position p = gson.fromJson(array.get(0), Position.class);

            Unit unit = game.getUnitAt(p);
            if (unit != null) {
                String id = unit.getID();
                namingService.putUnit(id, unit);

                reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(id));
            } else {
                reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(null));
            }

        } else if (operationName.equals(OperationNames.GET_TILE_AT_OPERATION)) {
            Position p = gson.fromJson(array.get(0), Position.class);

            Tile tile = game.getTileAt(p);
            if (tile != null) {
                String id = tile.getID();
                namingService.putTile(id, tile);

                reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(id));
            } else {
                reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(null));
            }

        } else if (operationName.equals(OperationNames.GET_PLAYER_IN_TURN_OPERATION)) {
            Player player = game.getPlayerInTurn();
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(player));

        } else if (operationName.equals(OperationNames.END_OF_TURN_OPERATION)) {
            game.endOfTurn();
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(null));

        } else if (operationName.equals(OperationNames.MOVE_UNIT_OPERATION)) {
            Position p1 = gson.fromJson(array.get(0), Position.class);
            Position p2 = gson.fromJson(array.get(1), Position.class);

            boolean valid = game.moveUnit(p1, p2);
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(valid));

        } else if (operationName.equals(OperationNames.CHANGE_WORK_IN_CITY_AT)) {
            Position p1 = gson.fromJson(array.get(0), Position.class);
            String balance = gson.fromJson(array.get(1), String.class);

            game.changeWorkForceFocusInCityAt(p1, balance);
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(null));

        } else if (operationName.equals(OperationNames.CHANGE_PROD_IN_CITY_AT)) {
            Position p1 = gson.fromJson(array.get(0), Position.class);
            String unit = gson.fromJson(array.get(1), String.class);

            game.changeProductionInCityAt(p1, unit);
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(null));

        } else if (operationName.equals(OperationNames.PERFORM_UNIT_ACTION_AT)) {
            Position p1 = gson.fromJson(array.get(0), Position.class);

            game.performUnitActionAt(p1);
            reply = new ReplyObject(HttpServletResponse.SC_OK, gson.toJson(null));

        } else {
            reply = new ReplyObject(HttpServletResponse.SC_NOT_IMPLEMENTED, "Server recieved unknown operation: "+operationName+".");
        }
        return reply;
    }
    private Game lookupGame(String objectId) {
        Game game = namingService.getGame(objectId);
        if (game == null) {
            throw new RuntimeException("Could not lookup Game: "+objectId);
        }
        return game;
    }
}
