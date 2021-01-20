package hotciv.visual;

import frds.broker.ClientRequestHandler;
import frds.broker.Requestor;
import frds.broker.ipc.socket.SocketClientRequestHandler;
import frds.broker.marshall.json.StandardJSONRequestor;
import hotciv.framework.Game;
import hotciv.framework.Position;
import hotciv.standard.GameImpl;
import hotciv.standard.NullObserver;
import hotciv.standard.factories.SemiCivFactory;
import hotciv.view.CivDrawing;
import hotciv.visual.CompositionTool;
import hotciv.visual.HotCivFactory4;
import minidraw.framework.DrawingEditor;
import minidraw.standard.MiniDrawApplication;

import java.io.IOException;

public class HotCivClient2 {
    public HotCivClient2(String host) {
        System.out.println("--- HotCiv TEST Client (socket) (host: "+host+") ---");

        ClientRequestHandler crh
                = new SocketClientRequestHandler();
        crh.setServer(host, 37321);
        Requestor requestor = new StandardJSONRequestor(crh);
        Game game = new GameImpl(new SemiCivFactory());

        DrawingEditor editor =
                new MiniDrawApplication("SemiCiv Game",
                        new HotCivFactory4(game));
        editor.open();
        editor.showStatus("Play the game.");

        editor.setTool(new CompositionTool(editor, game));
        game.addObserver(new CivDrawing(editor, game));
    }

    public static void main(String[] args) {
        new HotCivClient2(args[0]);
    }
}
