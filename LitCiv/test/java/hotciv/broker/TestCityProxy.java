package hotciv.broker;

import frds.broker.ClientRequestHandler;
import frds.broker.Invoker;
import frds.broker.Requestor;
import frds.broker.marshall.json.StandardJSONRequestor;
import hotciv.broker.client.CityProxy;
import hotciv.broker.client.GameProxy;
import hotciv.broker.client.LocalMethodClientRequestHandler;
import hotciv.broker.common.NamingService;
import hotciv.broker.common.NamingServiceImpl;
import hotciv.broker.marshall.json.HotCivCityInvoker;
import hotciv.broker.marshall.json.HotCivGameInvoker;
import hotciv.broker.marshall.json.RootInvoker;
import hotciv.framework.*;
import hotciv.standard.NullObserver;
import hotciv.stub.StubGame2;
import hotciv.stub.StubGame3;
import hotciv.stub.StubGame4;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TestCityProxy {
    private Game game;
    private Invoker invoker;
    private Requestor requestor;
    private NamingService namingService;

    @Before
    public void setup() {
        Game servant = new StubGame2();
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
    public void shouldHaveOwner() {
        City city = game.getCityAt(new Position(5,5));
        Player owner = city.getOwner();
        assertThat(owner, is(Player.RED));
    }

    @Test
    public void shouldHaveSize() {
        City city = game.getCityAt(new Position(5,5));
        int size = city.getSize();
        assertThat(size,  is(1));
    }

    @Test
    public void shouldHaveTreasury() {
        City city = game.getCityAt(new Position(5,5));
        int treasury = city.getTreasury();
        assertThat(treasury,  is(0));
    }

    @Test
    public void shouldHaveProduction() {
        City city = game.getCityAt(new Position(5,5));
        String production = city.getProduction();
        assertThat(production,  is(GameConstants.ARCHER));
    }

    @Test
    public void shouldHaveWorkforce() {
        City city = game.getCityAt(new Position(5,5));
        String worforce = city.getWorkforceFocus();
        assertThat(worforce,  is(GameConstants.productionFocus));
    }
}
