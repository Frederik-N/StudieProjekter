package hotciv.broker;

import frds.broker.ClientRequestHandler;
import frds.broker.Invoker;
import frds.broker.Requestor;
import frds.broker.marshall.json.StandardJSONRequestor;
import hotciv.broker.client.GameProxy;
import hotciv.broker.client.LocalMethodClientRequestHandler;
import hotciv.broker.client.UnitProxy;
import hotciv.broker.common.NamingService;
import hotciv.broker.common.NamingServiceImpl;
import hotciv.broker.marshall.json.HotCivUnitInvoker;
import hotciv.broker.marshall.json.RootInvoker;
import hotciv.framework.*;
import hotciv.standard.NullObserver;
import hotciv.stub.StubGame2;
import hotciv.stub.StubGame3;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TestUnitProxy {
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
    public void shouldHaveOwner() {
        Unit unit = game.getUnitAt(new Position(3,3));
        Player owner = unit.getOwner();
        assertThat(owner, is(Player.RED));
    }

    @Test
    public void shouldHaveType() {
        Unit unit = game.getUnitAt(new Position(3,3));
        String type = unit.getTypeString();
        assertThat(type,  is(GameConstants.SETTLER));
    }

    @Test
    public void shouldHaveMoveCount() {
        Unit unit = game.getUnitAt(new Position(3,3));
        int moveCount = unit.getMoveCount();
        assertThat(moveCount,  is(1));
    }

    @Test
    public void shouldHaveAttackStrength() {
        Unit unit = game.getUnitAt(new Position(3,3));
        int attackingStrength = unit.getAttackingStrength();
        assertThat(attackingStrength,  is(0));
    }

    @Test
    public void shouldHaveDefensiveStrength() {
        Unit unit = game.getUnitAt(new Position(3,3));
        int defensiveStrength = unit.getDefensiveStrength();
        assertThat(defensiveStrength,  is(0));
    }
}
