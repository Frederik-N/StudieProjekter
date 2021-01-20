package hotciv.standard;

import hotciv.framework.*;
import hotciv.standard.strategies.GammaActionStrat;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TestBuilder {
    private GameImpl game;

    @Before
    public void setUp() {
        game = new GameImpl.Builder()
                .setBuilderAgeWorldStrat(new AlphaAgeWorldStrat())
                .setBuilderWinConditionStrat(new AlphaWinConditionStrat())
                .setBuilderUnitActionStrat(new GammaActionStrat())
                .setBuilderWorldMapStrat(new AlphaWorldMapStrat())
                .setBuilderBattleStrat(new AlphaBattleStrat())
                .build();
    }

    @Test
    public void settlerActionGamma() {
        Position p = new Position(4, 3);
        game.performUnitActionAt(p);
        assertThat(game.getCityAt(p).getOwner(), is(Player.RED));
    }

    @Test
    public void cantMoveToOcean() {
        Position p = new Position(2, 0);
        Position p2 = new Position(1, 0);
        assertThat(game.moveUnit(p, p2), is(false));

        // Checks if Archer is same place and no Unit is at Ocean tile
        assertThat(game.getUnitAt(p).getOwner(), is(Player.RED));
        assertThat(game.getUnitAt(p).getTypeString(), is(GameConstants.ARCHER));
        assertThat(game.getUnitAt(p2), is(nullValue()));
    }
}
