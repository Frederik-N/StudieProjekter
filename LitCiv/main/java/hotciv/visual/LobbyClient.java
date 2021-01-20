package hotciv.visual;

/*
        * Copyright (C) 2018 Henrik Bærbak Christensen, baerbak.com
        *
        * Licensed under the Apache License, Version 2.0 (the "License");
        * you may not use this file except in compliance with the License.
        *
        * You may obtain a copy of the License at
        *
        *     http://www.apache.org/licenses/LICENSE-2.0
        *
        *  Unless required by applicable law or agreed to in writing, software
        *  distributed under the License is distributed on an "AS IS" BASIS,
        *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        *  See the License for the specific language governing permissions and
        *  limitations under the License.
        *
        */


import frds.broker.ClientRequestHandler;
import frds.broker.Requestor;
import frds.broker.ipc.socket.SocketClientRequestHandler;
import frds.broker.marshall.json.StandardJSONRequestor;
import hotciv.broker.client.GameProxy;
import hotciv.framework.Game;
import hotciv.standard.GameImpl;
import hotciv.standard.NullObserver;
import hotciv.standard.factories.SemiCivFactory;
import minidraw.framework.DrawingEditor;
import minidraw.standard.MiniDrawApplication;

import java.io.IOException;

/** A crude manual test case to be run from the command line,
 * allows a client to create and join games.
 *
 * @author Henrik Baerbak Christensen, CS @ AU
 */
public class LobbyClient {
    private String operation;
    private String name;
    private String hostname;
    private String objectId;

    public LobbyClient(String[] args) {
        parseCommandlineParameters(args);

        System.out.println("LobbyClient: Asked to do operation "+operation+" for player "+name);
        ClientRequestHandler clientRequestHandler
                = new SocketClientRequestHandler(hostname, 37321);
        Requestor requestor = new StandardJSONRequestor(clientRequestHandler);

        if (operation.equals("create")) {
            Game game = new GameImpl(new SemiCivFactory());
            DrawingEditor editor =
                    new MiniDrawApplication("SemiCiv Game",
                            new HotCivFactory4(game));
            editor.open();
            editor.showStatus("Play the game.");

            editor.setTool(new CompositionTool(editor, game));



        } else if (operation.equals("join")) {
            Game game = new GameProxy(hostname, requestor);
            DrawingEditor editor =
                    new MiniDrawApplication("SemiCiv Game",
                            new HotCivFactory4(game));
            editor.open();
            editor.showStatus("Play the game.");

            editor.setTool(new CompositionTool(editor, game));
        }
    }

    public static void main(String[] args) throws IOException {
        new LobbyClient(args);
    }

    private void parseCommandlineParameters(String[] args) {
        if (args.length < 1) {
            explainAndFail();
        }
        operation = args[0];
    }

    private static void explainAndFail() {
        System.out.println("Usage: LobbyClient <operation> <name> <objectId> <host>");
        System.out.println("  operation is either 'create' or 'join'");
        System.out.println("  objectId is only used in join");
        System.out.println("    for join, it is the joinToken");

    }
}