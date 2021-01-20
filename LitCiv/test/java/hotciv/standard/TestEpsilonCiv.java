package hotciv.standard;

import hotciv.framework.*;
import hotciv.standard.*;
import hotciv.standard.factories.*;
import hotciv.standard.strategies.*;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestEpsilonCiv {
    private GameImpl gameEpsilon, gameEpsilon2;
    private EpsilonBattleStrat epsilonBattleStrat;

    @Before
    public void setUp() {
        gameEpsilon = new GameImpl(new EpsilonFixedCivFactory());
        gameEpsilon2 = new GameImpl(new EpsilonCivFactory());
        epsilonBattleStrat = new EpsilonBattleStrat(new FixedDiceStrat());
    }

    @Test
    public void redPlayerWinsAtThreeAttacks() {
        Position pSet = new Position(11,0);
        Position pSet2 = new Position(11,2);
        Position pSet3 = new Position(10,1);
        Position pLeg = new Position(11,1);
        //Blues turn
        gameEpsilon.endOfTurn();
        //Moving settler to die to legion
        gameEpsilon.moveUnit(pSet, pLeg);
        //Check no one has won yet
        gameEpsilon.endOfTurn();
        gameEpsilon.endOfTurn();
        assertThat(gameEpsilon.getWinner(), is(nullValue()));

        gameEpsilon.moveUnit(pSet2, pLeg);
        gameEpsilon.moveUnit(pSet3, pLeg);

        gameEpsilon.endOfTurn();

        assertThat(gameEpsilon.getWinner(), is(Player.RED));
    }

    @Test
    public void bluePlayerWinsAtThreeAttacks() {
        Position pSet = new Position(8,0);
        Position pSet2 = new Position(8,2);
        Position pSet3 = new Position(7,1);
        Position pLeg = new Position(8,1);

        gameEpsilon.moveUnit(pSet, pLeg);

        gameEpsilon.endOfTurn();
        gameEpsilon.endOfTurn();
        assertThat(gameEpsilon.getWinner(), is(nullValue()));

        gameEpsilon.moveUnit(pSet2, pLeg);
        gameEpsilon.moveUnit(pSet3, pLeg);

        gameEpsilon.endOfTurn();

        assertThat(gameEpsilon.getWinner(), is(Player.BLUE));
    }

    @Test
    public void archerLosesToLegionWithSupport() {
        Position pLeg = new Position(13, 1);
        Position pLeg2 = new Position(13, 2);
        Position pArch = new Position(13, 0);

        // Arch has 2 atk, Leg has 2 def + 1 supp.
        gameEpsilon.moveUnit(pArch, pLeg);
        assertThat(gameEpsilon.getUnitAt(pArch), is(nullValue()));
    }

    @Test
    public void archerLosesToLegionInCity() {
        Position pLeg = new Position(15, 5);
        Position pArch = new Position(15, 4);

        // Arch has 2 atk, Leg has (2 def) * 3 = 6.
        gameEpsilon.moveUnit(pArch, pLeg);
        assertThat(gameEpsilon.getUnitAt(pArch), is(nullValue()));
    }

    @Test
    public void archerLosesToLegionOnHill() {
        Position pLeg = new Position(15, 10);
        Position pArch = new Position(15, 9);

        // Arch has 2 atk, Leg has 2 def * 2 = 4.
        gameEpsilon.moveUnit(pArch, pLeg);
        assertThat(gameEpsilon.getUnitAt(pArch), is(nullValue()));
    }

    @Test
    public void archerLosesToLegionInForest() {
        Position pLeg = new Position(15, 15);
        Position pArch = new Position(15, 14);

        // Arch has 2 atk, Leg has 2 def * 2 = 4.
        gameEpsilon.moveUnit(pArch, pLeg);
        assertThat(gameEpsilon.getUnitAt(pArch), is(nullValue()));
    }

    @Test
    public void settlerLosesBattleToLegion() {
        Position pSettler = new Position(13,13);
        Position pLegion = new Position(13,12);
        gameEpsilon.moveUnit(pSettler, pLegion);
        assertThat(gameEpsilon.getUnitAt(pSettler), is(nullValue()));
        assertThat(gameEpsilon.getUnitAt(pLegion).getTypeString(), is(GameConstants.LEGION));
    }

    @Test
    public void legionWinsBattleToSettler() {
        gameEpsilon.endOfTurn();
        Position pSettler = new Position(13,9);
        Position pLegion = new Position(13,8);
        System.out.println(epsilonBattleStrat.getSupport(gameEpsilon, pSettler));

        gameEpsilon.moveUnit(pLegion, pSettler);
        assertThat(gameEpsilon.getUnitAt(pSettler).getTypeString(), is(GameConstants.LEGION));
    }

    @Test
    public void supportReturns0WhenNoAdjacentUnit() {
        Position pSettler = new Position(13,9);
        assertThat(epsilonBattleStrat.getSupport(gameEpsilon, pSettler), is(0));
    }

    @Test
    public void supportReturns1WhenOneAdjacentUnit() {
        gameEpsilon.endOfTurn();
        Position pLegion = new Position(13,1);
        assertThat(epsilonBattleStrat.getSupport(gameEpsilon, pLegion), is(1));
    }

    @Test
    public void multiplierReturns1WhenOnPlain() {
        Position pSettler = new Position(13,9);
        assertThat(epsilonBattleStrat.getMultiplier(gameEpsilon, pSettler), is(1));
    }
    @Test
    public void multiplierReturns2WhenOnHillsOrForest() {
        Position pLegion = new Position(15,15);
        Position pLegion2 = new Position(15,10);
        assertThat(epsilonBattleStrat.getMultiplier(gameEpsilon, pLegion), is(2));
        assertThat(epsilonBattleStrat.getMultiplier(gameEpsilon, pLegion2), is(2));
    }
    @Test
    public void multiplierReturns3WhenOnCity() {
        Position pLegion = new Position(15,5);
        assertThat(epsilonBattleStrat.getMultiplier(gameEpsilon, pLegion), is(3));
    }
}
