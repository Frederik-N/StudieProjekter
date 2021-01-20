package hotciv.visual;

import com.google.gson.Gson;
import frds.broker.ClientRequestHandler;
import frds.broker.Invoker;
import frds.broker.Requestor;
import frds.broker.ipc.socket.SocketClientRequestHandler;
import frds.broker.marshall.json.StandardJSONRequestor;
import hotciv.broker.client.GameProxy;
import hotciv.broker.common.NamingService;
import hotciv.broker.common.NamingServiceImpl;
import hotciv.broker.marshall.json.HotCivGameInvoker;
import hotciv.framework.Game;
import hotciv.standard.NullObserver;
import hotciv.view.CivDrawing;
import minidraw.framework.DrawingEditor;
import minidraw.standard.MiniDrawApplication;

public class HotCivClient3 {
    public HotCivClient3(String host) {
        System.out.println("--- HotCiv TEST Client (socket) (host: "+host+") ---");

        ClientRequestHandler crh
                = new SocketClientRequestHandler();
        crh.setServer(host, 37321);
        Requestor requestor = new StandardJSONRequestor(crh);
        Game game = new GameProxy("game", requestor);
        DrawingEditor editor =
                new MiniDrawApplication("SemiCiv Game",
                        new HotCivFactory4(game));
        editor.open();
        editor.showStatus("Play the game.");

        editor.setTool(new CompositionTool(editor, game));

    }

    public static void main(String[] args) {
        new HotCivClient3(args[0]);
    }
}
