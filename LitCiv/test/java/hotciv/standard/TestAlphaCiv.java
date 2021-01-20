package hotciv.standard;

import hotciv.framework.*;

import hotciv.standard.factories.AlphaCivFactory;
import hotciv.view.CivDrawing;
import minidraw.framework.DrawingEditor;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestAlphaCiv {
    private GameImpl gameAlpha;

    @Before
    public void setUp() {
        gameAlpha = new GameImpl(new AlphaCivFactory());
    }

    @Test
    public void shouldBeRedAsStartingPlayer() {
        assertThat(gameAlpha, is(notNullValue()));
        assertThat(gameAlpha.getPlayerInTurn(), is(Player.RED));
    }

    @Test
    public void afterRedTurnIsBlueTurn() {
        gameAlpha.endOfTurn();
        assertThat(gameAlpha.getPlayerInTurn(), is(Player.BLUE));
    }

    @Test
    public void afterBlueTurnIsRedTurn() {
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();
        assertThat(gameAlpha.getPlayerInTurn(), is(Player.RED));
    }

    @Test
    public void gameAgeIs4000BCFirstTurn() {
        assertThat(gameAlpha.getAge(), is(-4000));
    }

    @Test
    public void gameAgeIs3900BCSecondTurn() {
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();
        assertThat(gameAlpha.getAge(), is(-3900));
    }

    @Test
    public void redWinsAt3000BC() {
        for (int i = 0; i <= 10; i++) {
            gameAlpha.endOfTurn();
            gameAlpha.endOfTurn();
        }
        assertThat(gameAlpha.getWinner(), is(Player.RED));
    }

    @Test
    public void noOneWinsAt3100BC() {
        for (int i = 0; i <= 8; i++) {
            gameAlpha.endOfTurn();
            gameAlpha.endOfTurn();
        }
        assertThat(gameAlpha.getAge(), is(-3100));
        assertThat(gameAlpha.getWinner(), is(nullValue()));
    }

    @Test
    public void everythingIsPlainsBut1_1a1_0a0_1a2_2() {
        // Check top-left corner
        Position p = new Position(0, 0);
        assertThat(GameConstants.PLAINS, is(gameAlpha.getTileAt(p).getTypeString()));

        //Check the middle
        p = new Position(8, 8);
        assertThat(GameConstants.PLAINS, is(gameAlpha.getTileAt(p).getTypeString()));

        //Check bottom-right corner
        p = new Position(GameConstants.WORLDSIZE - 1, GameConstants.WORLDSIZE - 1);
        assertThat(GameConstants.PLAINS, is(gameAlpha.getTileAt(p).getTypeString()));
    }

    @Test
    public void hillsAt0_1() {
        Position p = new Position(0, 1);
        assertThat(gameAlpha.getTileAt(p).getTypeString(), is(GameConstants.HILLS));
    }

    @Test
    public void oceanAt1_0() {
        Position p = new Position(1, 0);
        assertThat(gameAlpha.getTileAt(p).getTypeString(), is(GameConstants.OCEANS));
    }

    @Test
    public void mountainAt2_2() {
        Position p = new Position(2, 2);
        assertThat(gameAlpha.getTileAt(p).getTypeString(), is(GameConstants.MOUNTAINS));
    }

    @Test
    public void redCityAt1_1() {
        Position p = new Position(1, 1);
        assertThat(gameAlpha.getCityAt(p).getOwner(), is(Player.RED));
    }

    @Test
    public void blueCityAt4_1() {
        Position p = new Position(4, 1);
        assertThat(gameAlpha.getCityAt(p).getOwner(), is(Player.BLUE));
    }

    @Test
    public void citySizeIsAlways1() {
        Position pRed = new Position(1, 1);
        assertThat(gameAlpha.getCityAt(pRed).getSize(), is(1));
        Position pBlue = new Position(4, 1);
        assertThat(gameAlpha.getCityAt(pBlue).getSize(), is(1));
    }


    @Test
    public void cityProduces6EachTurn() {
        //Red city position
        Position p = new Position(1, 1);
        //Blue city position
        Position p2 = new Position(4, 1);
        assertThat(gameAlpha.getCityAt(p).getTreasury(), is(0));
        assertThat(gameAlpha.getCityAt(p2).getTreasury(), is(0));
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();
        assertThat(gameAlpha.getCityAt(p).getTreasury(), is(6));
        assertThat(gameAlpha.getCityAt(p2).getTreasury(), is(6));
    }

    @Test
    public void redArcherAt2_0() {
        Position p = new Position(2, 0);
        assertThat(gameAlpha.getUnitAt(p).getOwner(), is(Player.RED));
        assertThat(gameAlpha.getUnitAt(p).getTypeString(), is(GameConstants.ARCHER));
    }

    @Test
    public void blueLegionAt3_2() {
        Position p = new Position(3, 2);
        assertThat(gameAlpha.getUnitAt(p).getOwner(), is(Player.BLUE));
        assertThat(gameAlpha.getUnitAt(p).getTypeString(), is(GameConstants.LEGION));
    }

    @Test
    public void redSettlerAt4_3() {
        Position p = new Position(4, 3);
        assertThat(gameAlpha.getUnitAt(p).getOwner(), is(Player.RED));
        assertThat(gameAlpha.getUnitAt(p).getTypeString(), is(GameConstants.SETTLER));
    }

    @Test
    public void moveUnitOnce() {
        //Position of red settler and new position
        Position p = new Position(4, 3);
        Position p2 = new Position(4, 4);
        gameAlpha.moveUnit(p, p2);
        //What is on the new position
        assertThat(gameAlpha.getUnitAt(p2).getOwner(), is(Player.RED));
        assertThat(gameAlpha.getUnitAt(p2).getTypeString(), is(GameConstants.SETTLER));

        // Checking that the former position is empty
        assertThat(gameAlpha.getUnitAt(p), is(nullValue()));
    }

    @Test
    public void redUnitAttackBlueUnit() {
        //p is Reds Settler and p2 is blues legion
        Position p = new Position(4, 3);
        Position p2 = new Position(3, 2);
        assertThat(gameAlpha.moveUnit(p, p2), is(true));

        // Check that Blue Unit has been replaced by Red and no Unit is at earlier position
        assertThat(gameAlpha.getUnitAt(p), is(nullValue()));
        assertThat(gameAlpha.getUnitAt(p2).getOwner(), is(Player.RED));
        assertThat(gameAlpha.getUnitAt(p2).getTypeString(), is(GameConstants.SETTLER));
    }

    @Test
    public void cantMoveToMountain() {
        // Switches turn so blue can move Unit at (3,2)
        gameAlpha.endOfTurn();
        Position p = new Position(3, 2);
        Position p2 = new Position(2, 2);
        assertThat(gameAlpha.moveUnit(p, p2), is(false));

        // Checks if Legion is same place and no Unit is at Mountain tile
        assertThat(gameAlpha.getUnitAt(p).getOwner(), is(Player.BLUE));
        assertThat(gameAlpha.getUnitAt(p).getTypeString(), is(GameConstants.LEGION));
        assertThat(gameAlpha.getUnitAt(p2), is(nullValue()));
    }

    @Test
    public void cantMoveToOcean() {
        Position p = new Position(2, 0);
        Position p2 = new Position(1, 0);
        assertThat(gameAlpha.moveUnit(p, p2), is(false));

        // Checks if Archer is same place and no Unit is at Ocean tile
        assertThat(gameAlpha.getUnitAt(p).getOwner(), is(Player.RED));
        assertThat(gameAlpha.getUnitAt(p).getTypeString(), is(GameConstants.ARCHER));
        assertThat(gameAlpha.getUnitAt(p2), is(nullValue()));
    }

    @Test
    public void redCannotMoveBlueUnits() {
        // Red tries to move Blues Legion to (4,2)
        Position p2 = new Position(3, 2);
        Position p = new Position(4, 2);
        assertThat(gameAlpha.moveUnit(p2, p), is(false));

        //Check that no units moved
        assertThat(gameAlpha.getUnitAt(p2).getOwner(), is(Player.BLUE));
        assertThat(gameAlpha.getUnitAt(p2).getTypeString(), is(GameConstants.LEGION));
        assertThat(gameAlpha.getUnitAt(p), is(nullValue()));
    }

    @Test
    public void blueCannotMoveRedUnits() {
        // Check if Blue can move Reds Settler at (4,3) to (5,2)
        gameAlpha.endOfTurn();
        Position p = new Position(4, 3);
        Position p2 = new Position(5, 2);
        assertThat(gameAlpha.moveUnit(p, p2), is(false));

        //Check that no units moved
        assertThat(gameAlpha.getUnitAt(p).getOwner(), is(Player.RED));
        assertThat(gameAlpha.getUnitAt(p).getTypeString(), is(GameConstants.SETTLER));
        assertThat(gameAlpha.getUnitAt(p2), is(nullValue()));
    }

    @Test
    public void unitCantMoveTwice() {
        //Position of red settler and new position (4,4) is moved to
        Position p = new Position(4, 3);
        Position p2 = new Position(4, 4);
        gameAlpha.moveUnit(p, p2);
        //Checking that it cannot move back again
        assertThat(gameAlpha.moveUnit(p2, p), is(false));
        //Check it didn't move
        assertThat(gameAlpha.getUnitAt(p2).getOwner(), is(Player.RED));
        assertThat(gameAlpha.getUnitAt(p2).getTypeString(), is(GameConstants.SETTLER));
        assertThat(gameAlpha.getUnitAt(p), is(nullValue()));
    }

    @Test
    public void citiesCanSelectProduce() {
        Position p = new Position(1, 1);
        Position p2 = new Position(4, 1);

        // Red city can produce
        gameAlpha.changeProductionInCityAt(p, GameConstants.LEGION);
        assertThat(GameConstants.LEGION, is(gameAlpha.getCityAt(p).getProduction()));
        gameAlpha.changeProductionInCityAt(p, GameConstants.ARCHER);
        assertThat(GameConstants.ARCHER, is(gameAlpha.getCityAt(p).getProduction()));

        // Blue city can produce
        gameAlpha.changeProductionInCityAt(p2, GameConstants.LEGION);
        assertThat(GameConstants.LEGION, is(gameAlpha.getCityAt(p2).getProduction()));
        gameAlpha.changeProductionInCityAt(p2, GameConstants.ARCHER);
        assertThat(GameConstants.ARCHER, is(gameAlpha.getCityAt(p2).getProduction()));
    }

    @Test
    public void movingUnitChangesMoveCount() {
        // Checking that Red Settler has movecount 1
        Position p = new Position(4, 3);
        assertThat(gameAlpha.getUnitAt(p).getMoveCount(), is(1));

        // Moving Red Settler to (4,4) and checking movecount is 0
        Position p2 = new Position(4, 4);
        gameAlpha.moveUnit(p, p2);
        assertThat(gameAlpha.getUnitAt(p2).getMoveCount(), is(0));
    }

    @Test
    public void unitMoveCountIsResetEachTurn() {
        // Red Settler is moved to (4,4) and movecount resets after a round
        Position p = new Position(4, 3);
        assertThat(gameAlpha.getUnitAt(p).getMoveCount(), is(1));
        Position p2 = new Position(4, 4);
        gameAlpha.moveUnit(p, p2);
        assertThat(gameAlpha.getUnitAt(p2).getMoveCount(), is(0));
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();
        assertThat(gameAlpha.getUnitAt(p2).getMoveCount(), is(1));
    }

    @Test
    public void cityProducesUnitWhenEnoughProduction() {
        Position p = new Position(1, 1);
        gameAlpha.changeProductionInCityAt(p, GameConstants.ARCHER);
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();

        // Archers cost should be deducted for the treasury so 12-10=2
        assertThat(gameAlpha.getCityAt(p).getTreasury(), is(2));
        // New Red Archer at city tile is produced
        assertThat(gameAlpha.getUnitAt(p).getOwner(), is(Player.RED));
        assertThat(gameAlpha.getUnitAt(p).getTypeString(), is(GameConstants.ARCHER));
    }

    @Test
    public void cityProducesSettlerWhenEnoughProduction() {
        Position p = new Position(1, 1);
        gameAlpha.changeProductionInCityAt(p, GameConstants.SETTLER);
        for (int i = 0; i <= 5; i++) {
            gameAlpha.endOfTurn();
            gameAlpha.endOfTurn();
        }

        // Settler cost should be deducted for the treasury so 36-30=6
        assertThat(gameAlpha.getCityAt(p).getTreasury(), is(6));
        // New Red Settler at city tile is produced
        assertThat(gameAlpha.getUnitAt(p).getOwner(), is(Player.RED));
        assertThat(gameAlpha.getUnitAt(p).getTypeString(), is(GameConstants.SETTLER));
    }

    @Test
    public void cityProducesOnlyTheUnitsItCanAfford() {
        Position p = new Position(1, 1);
        gameAlpha.changeProductionInCityAt(p, GameConstants.SETTLER);
        for (int i = 0; i <= 5; i++) {
            gameAlpha.endOfTurn();
            gameAlpha.endOfTurn();
        }
        Position p2 = new Position(0, 1);
        Position p3 = new Position(1, 2);
        Position p4 = new Position(1, 0);
        Position p5 = new Position(2, 1);
        assertThat(gameAlpha.getUnitAt(p).getTypeString(), is(GameConstants.SETTLER));
        assertThat(gameAlpha.getUnitAt(p2), is(nullValue()));
        assertThat(gameAlpha.getUnitAt(p3), is(nullValue()));
        assertThat(gameAlpha.getUnitAt(p4), is(nullValue()));
        assertThat(gameAlpha.getUnitAt(p5), is(nullValue()));
    }

    @Test
    public void unitIsSpawnedWestOfCity() {
        Position p = new Position(1, 1);
        gameAlpha.changeProductionInCityAt(p, GameConstants.ARCHER);
        for (int i = 0; i <= 4; i++) {
            gameAlpha.endOfTurn();
            gameAlpha.endOfTurn();
        }
        Position pWest = new Position(0, 1);
        assertThat(gameAlpha.getUnitAt(p).getTypeString(), is(GameConstants.ARCHER));
        assertThat(gameAlpha.getUnitAt(pWest).getTypeString(), is(GameConstants.ARCHER));
    }

    @Test
    public void unitsHaveStats() {
        Position pArcher = new Position(2, 0);
        Position pLegion = new Position(3, 2);
        Position pSettler = new Position(4, 3);
        assertThat(gameAlpha.getUnitAt(pArcher).getAttackingStrength(), is(2));
        assertThat(gameAlpha.getUnitAt(pArcher).getDefensiveStrength(), is(3));
        assertThat(gameAlpha.getUnitAt(pLegion).getAttackingStrength(), is(4));
        assertThat(gameAlpha.getUnitAt(pLegion).getDefensiveStrength(), is(2));
        assertThat(gameAlpha.getUnitAt(pSettler).getAttackingStrength(), is(0));
        assertThat(gameAlpha.getUnitAt(pSettler).getDefensiveStrength(), is(3));
    }

    @Test
    public void cityChangesWorkforce() {
        Position p = new Position(1, 1);
        assertThat(gameAlpha.getCityAt(p).getWorkforceFocus(), is(GameConstants.foodFocus));
        gameAlpha.changeWorkForceFocusInCityAt(p, GameConstants.productionFocus);
        assertThat(gameAlpha.getCityAt(p).getWorkforceFocus(), is(GameConstants.productionFocus));
    }

    @Test
    public void playerCannotMoveOwnUnitIntoOwnUnit() {
        Position p = new Position(2,  0);
        Position p2 = new Position(3,  1);
        Position p3 = new Position(4,  2);
        Position p4 = new Position(4,  3);
        gameAlpha.moveUnit(p, p2);
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();
        gameAlpha.moveUnit(p2, p3);
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();
        assertThat(gameAlpha.moveUnit(p3, p4),is(false));
    }

    @Test
    public void unitCannotMoveMoreThanOneDistance() {
        assertThat(gameAlpha.moveUnit(new Position(2,0), new Position(4,0)), is(false));
        assertThat(gameAlpha.moveUnit(new Position(4,3), new Position(4,5)), is(false));
    }

    @Test
    public void unitsCanConquerCities() {
        Position p = new Position(2,  0);
        Position p2 = new Position(3,  1);
        Position p3 = new Position(4,  1);
        gameAlpha.moveUnit(p, p2);
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();
        gameAlpha.moveUnit(p2, p3);
        assertThat(gameAlpha.getCityAt(p3).getOwner(), is(Player.RED));
        assertThat(gameAlpha.getUnitAt(p3).getOwner(), is(Player.RED));

    }

    @Test
    public void settlerActionDoesNothing() {
        Position p = new Position(4,3);
        gameAlpha.performUnitActionAt(p);
        assertThat(gameAlpha.getCityAt(p), is(nullValue()));
    }

    @Test
    public void archerActionDoesNothing() {
        // Setup the unit
        Position p = new Position(2,0);
        gameAlpha.performUnitActionAt(p);
        assertThat(gameAlpha.getUnitAt(p).getDefensiveStrength(), is(GameConstants.ARCHERDEF));
    }

    @Test
    public void twoArchersCanMoveInSameTurn() {
        gameAlpha.changeProductionInCityAt(new Position(1,1), GameConstants.ARCHER);
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();
        gameAlpha.endOfTurn();
        assertThat(gameAlpha.moveUnit(new Position(2,0), new Position(3,1)), is(true));
        assertThat(gameAlpha.getUnitAt(new Position(1,1)).getTypeString(), is(GameConstants.ARCHER));
        assertThat(gameAlpha.moveUnit(new Position(1,1), new Position(2,0)), is(true));

    }
}
