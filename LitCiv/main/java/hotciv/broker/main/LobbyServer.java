package hotciv.broker.main;

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
import frds.broker.Invoker;
import frds.broker.ipc.socket.SocketServerRequestHandler;
import hotciv.broker.common.NamingService;
import hotciv.broker.common.NamingServiceImpl;
import hotciv.broker.marshall.json.RootInvoker;
import hotciv.framework.Game;
import hotciv.standard.GameImpl;
import hotciv.standard.factories.SemiCivFactory;

/** Socket based GameLobby server.
 *
 * @author Henrik Baerbak Christensen, CS @ AU
 */
public class LobbyServer {
    public static void main(String[] args) throws Exception {
        new LobbyServer();
    }

    public LobbyServer() throws Exception {
        int port = 37321;
        // Define the server side delegates

        Game servant = new GameImpl(new SemiCivFactory());
        NamingService namingService = new NamingServiceImpl();
        namingService.putGame(servant.getID(), servant);
        Invoker invoker = new RootInvoker(namingService);

        // Configure a socket based server request handler
        SocketServerRequestHandler ssrh =
                new SocketServerRequestHandler();
        ssrh.setPortAndInvoker(port, invoker);

        // Welcome
        System.out.println("=== HotCiv Socket based Server Request Handler (port:"
                + port + ") ===");
        System.out.println(" Use ctrl-c to terminate!");
        ssrh.start();
    }
}

