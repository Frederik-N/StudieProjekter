package hotciv.broker.main;

import frds.broker.Invoker;
import frds.broker.ipc.socket.SocketServerRequestHandler;
import hotciv.broker.common.NamingService;
import hotciv.broker.common.NamingServiceImpl;
import hotciv.framework.Game;
import hotciv.broker.marshall.json.HotCivGameInvoker;
import hotciv.stub.StubGame3;

public class HotCivServer {
    private static Thread daemon;

    public static void main(String[] args) throws Exception {
        new HotCivServer(); // No error handling!
    }


    public HotCivServer() throws Exception {
        int port = 37321;
        // Define the server side delegates

        Game servant = new StubGame3();
        NamingService namingService = new NamingServiceImpl();
        Invoker invoker = new HotCivGameInvoker(namingService);

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
