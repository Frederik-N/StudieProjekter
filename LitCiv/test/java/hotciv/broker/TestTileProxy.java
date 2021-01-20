package hotciv.broker;

import frds.broker.ClientRequestHandler;
import frds.broker.Invoker;
import frds.broker.Requestor;
import frds.broker.marshall.json.StandardJSONRequestor;
import hotciv.broker.client.GameProxy;
import hotciv.broker.client.LocalMethodClientRequestHandler;
import hotciv.broker.client.TileProxy;
import hotciv.broker.common.NamingService;
import hotciv.broker.common.NamingServiceImpl;
import hotciv.broker.marshall.json.HotCivTileInvoker;
import hotciv.broker.marshall.json.RootInvoker;
import hotciv.framework.*;
import hotciv.standard.NullObserver;
import hotciv.stub.StubGame2;
import hotciv.stub.StubGame3;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TestTileProxy {
    private Invoker invoker;
    private Requestor requestor;
    private Game game;
    private NamingService namingService;

    @Before
    public void setup() {
        Game servant = new StubGame3();
        GameObserver nullObserver = new NullObserver();
        servant.addObserver(nullObserver);


        namingService = new NamingServiceImpl();
        namingService.putGame("test", servant);
        invoker = new RootInvoker(namingService);

        ClientRequestHandler crh = new LocalMethodClientRequestHandler(invoker);

        requestor = new StandardJSONRequestor(crh);

        game = new GameProxy("test", requestor);
        game.addObserver(nullObserver);
    }

    @Test
    public void shouldHaveType() {
        Tile tile = game.getTileAt(new Position(3,3));
        String type = tile.getTypeString();
        assertThat(type, is(GameConstants.PLAINS));
    }
}
