package hotciv.standard;

import hotciv.framework.*;
import hotciv.standard.*;
import hotciv.standard.factories.*;
import hotciv.standard.strategies.*;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestBetaCiv {
    private BetaAgeWorldStrat betaAge;
    private BetaWinConditionStrat betaWin;
    private GameImpl gameBeta;

    @Before
    public void setUp() {
        gameBeta = new GameImpl(new BetaCivFactory());
    }

    @Test
    public void worldAgingBeta() {
        for (int i = 0; i <= 39; i++) {
            gameBeta.endOfTurn();
            gameBeta.endOfTurn();
        }
        assertThat(gameBeta.getAge(), is(-1));
        gameBeta.endOfTurn();
        gameBeta.endOfTurn();
        assertThat(gameBeta.getAge(), is(1));
    }

    @Test
    public void winningConditionRedBeta() {
        Position p = new Position(2, 0);
        Position p2 = new Position(3,1);
        Position pCity = new Position(4, 1);
        gameBeta.moveUnit(p, p2);
        gameBeta.endOfTurn();
        gameBeta.endOfTurn();
        gameBeta.moveUnit(p2, pCity);
        gameBeta.endOfTurn();
        gameBeta.endOfTurn();
        assertThat(gameBeta.getWinner(), is(Player.RED));
    }

    @Test
    public void winningConditionBlueBeta() {
        Position p = new Position(3, 2);
        Position p2 = new Position(2,1);
        Position pCity = new Position(1, 1);
        gameBeta.endOfTurn();
        // Blues turn
        gameBeta.moveUnit(p, p2);
        gameBeta.endOfTurn();
        gameBeta.endOfTurn();
        // Blues turn
        gameBeta.moveUnit(p2, pCity);
        gameBeta.endOfTurn();
        gameBeta.endOfTurn();
        assertThat(gameBeta.getWinner(), is(Player.BLUE));
    }
}
