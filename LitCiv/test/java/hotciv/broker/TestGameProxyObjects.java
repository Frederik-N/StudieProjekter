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
import hotciv.broker.marshall.json.HotCivGameInvoker;
import hotciv.framework.*;
import hotciv.standard.NullObserver;
import hotciv.stub.StubGame4;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class TestGameProxyObjects {
    private Game game;
    private Invoker invoker;
    private Requestor requestor;
    private NamingService namingService;

    @Before
    public void setup() {
        Game servant = new StubGame4();
        GameObserver nullObserver = new NullObserver();
        servant.addObserver(nullObserver);


        namingService = new NamingServiceImpl();
        namingService.putGame("test", servant);
        invoker = new HotCivGameInvoker(namingService);

        ClientRequestHandler crh = new LocalMethodClientRequestHandler(invoker);

        requestor = new StandardJSONRequestor(crh);

        game = new GameProxy("test", requestor);
        game.addObserver(nullObserver);
    }

    @Test
    public void CityIsTheSameInNamingService() {
        Position pCity = new Position(5,5);
        City city = game.getCityAt(pCity);

        assertThat(namingService.getCity(city.getID()), is(City.class));
    }

    @Test
    public void TileIsTheSameInNamingService() {
        Position pTile = new Position(6,6);
        Tile tile = game.getTileAt(pTile);

        assertThat(namingService.getTile(tile.getID()), is(Tile.class));
    }

    @Test
    public void UnitIsTheSameInNamingService() {
        Position settler = new Position(3,3);
        Unit unit = game.getUnitAt(settler);

        assertThat(namingService.getUnit(unit.getID()), is(Unit.class));
    }

    @Test
    public void CheckUnitIsBeingMoved() {
        Position settler = new Position(3,3);
        Unit unit1 = game.getUnitAt(settler);

        Position p1 = new Position(3,4);
        game.moveUnit(settler, p1);

        Unit unit2 = game.getUnitAt(p1);
        Unit unit3 = game.getUnitAt(new Position(2,3));

        assertThat(unit1.getID(), is(unit2.getID()));
        assertNotEquals(unit2.getID(), unit3.getID());
    }
}