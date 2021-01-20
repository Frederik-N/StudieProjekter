package hotciv.standard;

import hotciv.framework.*;

import hotciv.standard.factories.GammaCivFactory;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestGammaCiv {
    private GameImpl gameGamma;

    @Before
    public void setUp() {
        gameGamma = new GameImpl(new GammaCivFactory());
    }

    @Test
    public void settlerActionGamma() {
        Position p = new Position(4, 3);
        gameGamma.performUnitActionAt(p);
        assertThat(gameGamma.getCityAt(p).getOwner(), is(Player.RED));
    }

    @Test
    public void archerActionGamma() {
        Position p = new Position(2, 0);
        gameGamma.performUnitActionAt(p);
        assertThat(gameGamma.getUnitAt(p).getDefensiveStrength(), is(GameConstants.ARCHERDEF*2));
        gameGamma.performUnitActionAt(p);
        assertThat(gameGamma.getUnitAt(p).getDefensiveStrength(), is(GameConstants.ARCHERDEF));
    }

    @Test
    public void archerCantMoveFortified() {
        Position p = new Position(2, 0);
        gameGamma.performUnitActionAt(p);
        assertThat(gameGamma.moveUnit(p, new Position(2,1)), is(false));
    }
}
