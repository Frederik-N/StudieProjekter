package hotciv.broker.main;

import frds.broker.ClientRequestHandler;
import frds.broker.Requestor;
import frds.broker.ipc.socket.SocketClientRequestHandler;
import frds.broker.marshall.json.StandardJSONRequestor;
import hotciv.framework.*;
import hotciv.broker.client.GameProxy;
import hotciv.standard.NullObserver;

import java.io.IOException;

public class HotCivClient {
    public HotCivClient(String hostname) {
        System.out.println("--- HotCiv TEST Client (socket) (host: "+hostname+") ---");

        ClientRequestHandler crh
                = new SocketClientRequestHandler();
        crh.setServer(hostname, 37321);
        Requestor requestor = new StandardJSONRequestor(crh);

        Game game = new GameProxy("test", requestor);
        testSimpleMethods(game);
    }

    public static void main(String[] args) throws IOException {
        new HotCivClient(args[0]);
    }

    private void testSimpleMethods(Game game) {
        System.out.println("--- Testing methods ---");
        System.out.println("Game age: " + game.getAge());
        System.out.println("Game winner: " + game.getWinner());
        System.out.println("Game PlayerInTurn: " + game.getPlayerInTurn());
        System.out.println("Game MoveUnit (2,2) to (2,3): " + game.moveUnit(new Position(2,2), new Position(2,3)));
        game.endOfTurn();
        System.out.println("Game PlayerInTurn after endOfTurn: "+game.getPlayerInTurn());
    }
}
