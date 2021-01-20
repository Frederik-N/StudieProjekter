package hotciv.broker;

import frds.broker.*;
import frds.broker.marshall.json.StandardJSONRequestor;
import hotciv.broker.common.NamingService;
import hotciv.broker.common.NamingServiceImpl;
import hotciv.broker.marshall.json.RootInvoker;
import hotciv.framework.*;
import hotciv.broker.client.GameProxy;
import hotciv.broker.marshall.json.HotCivGameInvoker;
import hotciv.broker.client.LocalMethodClientRequestHandler;
import hotciv.standard.NullObserver;
import hotciv.stub.StubGame2;
import hotciv.stub.StubGame3;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestGameProxy {
    private Game game;
    private Invoker invoker;
    private Requestor requestor;
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
    public void shouldHaveWinner() {
        Player winner = game.getWinner();
        assertThat(winner, is(Player.YELLOW));
    }

    @Test
    public void shouldHaveAge() {
        int age = game.getAge();
        assertThat(age, is(42));
    }

    @Test
    public void playerInTurnIsPlayerRed() {
        Player player = game.getPlayerInTurn();
        assertThat(player, is(Player.RED));
    }

    @Test
    public void playerInTurnAfterTurnEndIsPlayerBlue() {
        game.endOfTurn();
        Player player = game.getPlayerInTurn();
        assertThat(player, is(Player.BLUE));
    }

    @Test
    public void moveUnit2_2to2_3IsTrue() {
        Position p1 = new Position(2,2);
        Position p2 = new Position(2,3);
        Boolean move = game.moveUnit(p1, p2);
        assertThat(move, is(true));
    }

    @Test
    public void moveUnit2_1to2_3IsFalse() {
        Position p1 = new Position(2,1);
        Position p2 = new Position(2,3);
        Boolean move = game.moveUnit(p1, p2);
        assertThat(move, is(false));
    }

    @Test
    public void shouldHaveCityWithGreenOwner() {
        Position p1 = new Position(1,1);
        City city = game.getCityAt(p1);
        assertThat(city.getOwner(), is(Player.GREEN));
    }

    @Test
    public void shouldHaveSettlerUnit() {
        Position p1 = new Position(3,3);
        Unit unit = game.getUnitAt(p1);
        assertThat(unit.getTypeString(), is(GameConstants.SETTLER));
    }

    @Test
    public void shouldHavePlainsTile() {
        Position p1 = new Position(3,3);
        Tile tile = game.getTileAt(p1);
        assertThat(tile.getTypeString(), is(GameConstants.PLAINS));
    }

    @Test
    public void changeProductionInCity1_1() {
        Position p1 = new Position(1, 1);
        game.changeProductionInCityAt(p1, GameConstants.ARCHER);
        City city = game.getCityAt(p1);
        assertThat(city.getProduction(), is(GameConstants.ARCHER));
    }

    @Test
    public void performUnitActionAt3_3() {
        Position p1 = new Position(3,3);
        game.performUnitActionAt(p1);
        City city = game.getCityAt(p1);
        assertThat(city.getOwner(), is(Player.RED));
        assertThat(game.getUnitAt(p1), is(nullValue()));
    }
}