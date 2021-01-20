package hotciv.standard;

import hotciv.framework.*;

import hotciv.standard.factories.ZetaCivFactory;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestZetaCiv {
    private GameImpl gameZeta;

    @Before
    public void setUp() {
        gameZeta = new GameImpl(new ZetaCivFactory());
    }



    @Test
    public void redWinsConqueringAllCitiesBefore20Round() {
        Position p = new Position(2, 0);
        Position p2 = new Position(3,1);
        Position pCity = new Position(4, 1);
        gameZeta.moveUnit(p, p2);
        gameZeta.endOfTurn();
        gameZeta.endOfTurn();
        gameZeta.moveUnit(p2, pCity);
        gameZeta.endOfTurn();
        gameZeta.endOfTurn();
        assertThat(gameZeta.getWinner(), is(Player.RED));
    }

    @Test
    public void blueWinsConqueringAllCitiesBefore20Round() {
        Position p = new Position(3, 2);
        Position p2 = new Position(2,1);
        Position pCity = new Position(1, 1);
        gameZeta.endOfTurn();
        // Blues turn
        gameZeta.moveUnit(p, p2);
        gameZeta.endOfTurn();
        gameZeta.endOfTurn();
        // Blues turn
        gameZeta.moveUnit(p2, pCity);
        gameZeta.endOfTurn();
        gameZeta.endOfTurn();
        assertThat(gameZeta.getWinner(), is(Player.BLUE));
    }

    @Test
    public void noWinIfAPlayerConquersAllCitiesAfter20Round() {
        for(int i = 0; i<40; i++) {
            gameZeta.endOfTurn();
        }
        Position p = new Position(2, 0);
        Position p2 = new Position(3,1);
        Position pCity = new Position(4, 1);
        gameZeta.moveUnit(p, p2);
        gameZeta.endOfTurn();
        gameZeta.endOfTurn();
        gameZeta.moveUnit(p2, pCity);
        gameZeta.endOfTurn();
        gameZeta.endOfTurn();
        assertThat(gameZeta.getWinner(), is(nullValue()));

    }

    @Test
    public void redWinsWithThreeBattlesAfterRound20() {
        for(int i = 0; i<44; i++) {
            gameZeta.endOfTurn();
        }
        //Moving reds legion to kill 3 of blues units.
        Position p = new Position(2,1);
        Position p2 = new Position(3,1);
        Position p3 = new Position(4,1);
        Position p4 = new Position(4,2);

        // Kill Blue legion at (3,1)
        gameZeta.moveUnit(p, p2);
        gameZeta.endOfTurn();
        gameZeta.endOfTurn();
        // Kill Blue legion at (4,1)
        gameZeta.moveUnit(p2, p3);
        gameZeta.endOfTurn();
        gameZeta.endOfTurn();
        //Check no one has won yet
        assertThat(gameZeta.getWinner(), is(nullValue()));
        // Kill Blue legion at (4,2)
        gameZeta.moveUnit(p3, p4);
        gameZeta.endOfTurn();
        assertThat(gameZeta.getWinner(), is(Player.RED));
    }

    @Test
    public void blueWinsWithThreeBattlesAfterRound20() {
        for(int i = 0; i<43; i++) {
            gameZeta.endOfTurn();
        }
        //Moving blues legion to kill three of reds units.
        Position p = new Position(3,2);
        Position p2 = new Position(2,1);
        Position p3 = new Position(2,0);
        Position p4 = new Position(1,1);

        // Kill Red legion at (2,1)
        gameZeta.moveUnit(p, p2);
        gameZeta.endOfTurn();
        gameZeta.endOfTurn();
        // Kill Red Archer at (2,0)
        gameZeta.moveUnit(p2, p3);
        gameZeta.endOfTurn();
        gameZeta.endOfTurn();
        //Check no one has won yet
        assertThat(gameZeta.getWinner(), is(nullValue()));
        // Kill red legion at (1,1)
        gameZeta.moveUnit(p3, p4);
        gameZeta.endOfTurn();
        assertThat(gameZeta.getWinner(), is(Player.BLUE));
    }
}
