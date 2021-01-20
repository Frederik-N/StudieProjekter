package hotciv.standard;

import hotciv.framework.*;
import hotciv.standard.*;
import hotciv.standard.factories.*;
import hotciv.standard.strategies.*;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestThetaCiv {
    private GameImpl gameTheta;

    @Before
    public void setUp() {
        gameTheta = new GameImpl(new ThetaCivFactory());
    }

    @Test
    public void cityProducesBomb() {
        Position p = new Position(1,1);
        gameTheta.changeProductionInCityAt(p, GameConstants.BOMB);
        for (int i = 0; i <= 8; i++) {
            gameTheta.endOfTurn();
            gameTheta.endOfTurn();
        }
        assertThat(gameTheta.getUnitAt(p), is(nullValue()));

        gameTheta.endOfTurn();
        gameTheta.endOfTurn();
        // 6+6*9 = 60 - 60 = 0
        assertThat(gameTheta.getCityAt(p).getTreasury(), is(0));
        assertThat(gameTheta.getUnitAt(p).getTypeString(), is(GameConstants.BOMB));
    }

    @Test
    public void bombHas1DefenseAnd0Attack() {
        Position p = new Position(1,1);
        gameTheta.changeProductionInCityAt(p, GameConstants.BOMB);
        for (int i = 0; i <= 9; i++) {
            gameTheta.endOfTurn();
            gameTheta.endOfTurn();
        }
        assertThat(gameTheta.getUnitAt(p).getDefensiveStrength(), is(1));
        assertThat(gameTheta.getUnitAt(p).getAttackingStrength(), is(0));
    }

    @Test
    public void bombCanMoveTwice() {
        Position p = new Position(1,1);
        gameTheta.changeProductionInCityAt(p, GameConstants.BOMB);
        for (int i = 0; i <= 9; i++) {
            gameTheta.endOfTurn();
            gameTheta.endOfTurn();
        }
        Position p2 = new Position(1,2);
        gameTheta.moveUnit(p, p2);
        Position p3 = new Position(2,3);
        gameTheta.moveUnit(p2, p3);
        assertThat(gameTheta.getUnitAt(p3).getTypeString(), is(GameConstants.BOMB));
    }

    @Test
    public void bombCantMoveThrice() {
        Position p = new Position(1,1);
        gameTheta.changeProductionInCityAt(p, GameConstants.BOMB);
        for (int i = 0; i <= 9; i++) {
            gameTheta.endOfTurn();
            gameTheta.endOfTurn();
        }
        Position p2 = new Position(1,2);
        gameTheta.moveUnit(p, p2);
        Position p3 = new Position(2,3);
        gameTheta.moveUnit(p2, p3);
        Position p4 = new Position(3,3);
        assertThat(gameTheta.moveUnit(p3, p4), is(false));
        assertThat(gameTheta.getUnitAt(p3).getTypeString(), is(GameConstants.BOMB));
    }

    @Test
    public void bombCanMoveOnMountain() {
        Position p = new Position(1,1);
        gameTheta.changeProductionInCityAt(p, GameConstants.BOMB);
        for (int i = 0; i <= 9; i++) {
            gameTheta.endOfTurn();
            gameTheta.endOfTurn();
        }
        Position p2 = new Position(2,2);
        assertThat(gameTheta.moveUnit(p, p2), is(true));
        assertThat(gameTheta.getUnitAt(p2).getTypeString(), is(GameConstants.BOMB));
    }

    @Test
    public void bombActionsDestroysBomb() {
        Position p = new Position(1,1);
        gameTheta.changeProductionInCityAt(p, GameConstants.BOMB);
        for (int i = 0; i <= 9; i++) {
            gameTheta.endOfTurn();
            gameTheta.endOfTurn();
        }

        gameTheta.performUnitActionAt(p);
        assertThat(gameTheta.getUnitAt(p), is(nullValue()));
    }

    @Test
    public void bombActionDestroysUnitsAround() {
        Position p = new Position(1,1);
        gameTheta.changeProductionInCityAt(p, GameConstants.BOMB);
        for (int i = 0; i <= 9; i++) {
            gameTheta.endOfTurn();
            gameTheta.endOfTurn();
        }

        Position p2 = new Position(2,0);
        assertThat(gameTheta.getUnitAt(p2).getTypeString(), is(GameConstants.ARCHER));
        gameTheta.performUnitActionAt(p);
        assertThat(gameTheta.getUnitAt(p2), is(nullValue()));
    }

    // All cities have 1 size, so this is the same as the one under
    // TODO: Add test when cities get above 1 size
    /*
    @Test
    public void bombActionDecreasesPopulationOfCities() {
        Position p = new Position(1,1);
        gameTheta.changeProductionInCityAt(p, GameConstants.BOMB);
        for (int i = 0; i <= 9; i++) {
            gameTheta.endOfTurn();
            gameTheta.endOfTurn();
        }
    }
    */

    @Test
    public void bombActionRemovesCitiesWhenTheyReach0Size() {
        Position p = new Position(1,1);
        gameTheta.changeProductionInCityAt(p, GameConstants.BOMB);
        for (int i = 0; i <= 9; i++) {
            gameTheta.endOfTurn();
            gameTheta.endOfTurn();
        }
        Position p2 = new Position(1,2);
        gameTheta.moveUnit(p, p2);
        assertThat(gameTheta.getCityAt(p).getSize(), is(1));
        gameTheta.performUnitActionAt(p2);
        assertThat(gameTheta.getCityAt(p), is(nullValue()));
    }
}