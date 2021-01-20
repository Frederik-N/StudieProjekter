package hotciv.standard;

import hotciv.framework.*;

import hotciv.standard.factories.AlphaCivFactory;
import hotciv.view.CivDrawing;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestObserver {
    private GameImpl gameAlpha;
    private GameObserver gameObserver1,gameObserver2,gameObserver3;

    @Before
    public void setUp() {
        gameAlpha = new GameImpl(new AlphaCivFactory());
        gameObserver1 = new stubObserver();
        gameObserver2 = new stubObserver();
        gameObserver3 = new stubObserver();
        gameAlpha.addObserver(gameObserver1);
        gameAlpha.addObserver(gameObserver2);
        gameAlpha.addObserver(gameObserver3);
    }

    class stubObserver implements GameObserver {
        public boolean tileIsUpdated = false;
        public boolean turnEndsIsUpdated = false;
        public boolean worldIsUpdated = false;

        @Override
        public void worldChangedAt(Position pos) {
            worldIsUpdated = true;
        }

        @Override
        public void turnEnds(Player nextPlayer, int age) {
            turnEndsIsUpdated = true;
        }

        @Override
        public void tileFocusChangedAt(Position position) {
            tileIsUpdated = true;
        }
    }

    @Test
    public void observersAreAddedAndWillBeNotified() {
        assertThat(gameAlpha.getObservers().size(), is(3));
        gameAlpha.setTileFocus(new Position(1,1));
        assertThat(((stubObserver) gameObserver1).tileIsUpdated, is(true));
        assertThat(((stubObserver) gameObserver2).tileIsUpdated, is(true));
        assertThat(((stubObserver) gameObserver3).tileIsUpdated, is(true));
    }

    @Test
    public void sameObserverIsNotAddedTwice() {
        gameAlpha.addObserver(gameObserver1);
        assertThat(gameAlpha.getObservers().size(), is(3));
    }

    @Test
    public void moveUnitUpdatesObsevers() {
        gameAlpha.moveUnit(new Position(2,0), new Position(3,1));
        assertThat(((stubObserver) gameObserver1).worldIsUpdated, is(true));
        assertThat(((stubObserver) gameObserver2).worldIsUpdated, is(true));
    }

    @Test
    public void endOfTurnUpdatesObsevers() {
        gameAlpha.endOfTurn();
        assertThat(((stubObserver) gameObserver1).turnEndsIsUpdated, is(true));
        assertThat(((stubObserver) gameObserver2).turnEndsIsUpdated, is(true));
    }

    @Test
    public void producingUnitsUpdatesObsevers() {
        for(int i = 0; i<10; i++) {
            gameAlpha.endOfTurn();
        }
        assertThat(((stubObserver) gameObserver1).worldIsUpdated, is(true));
        assertThat(((stubObserver) gameObserver2).worldIsUpdated, is(true));
    }

    @Test
    public void changeWorkForceFocusUpdatesObsevers() {
        gameAlpha.changeWorkForceFocusInCityAt(new Position(1,1),GameConstants.foodFocus);
        assertThat(((stubObserver) gameObserver1).worldIsUpdated, is(true));
        assertThat(((stubObserver) gameObserver2).worldIsUpdated, is(true));
    }

    @Test
    public void changeProductionUpdatesObsevers() {
        gameAlpha.changeProductionInCityAt(new Position(1,1),GameConstants.ARCHER);
        assertThat(((stubObserver) gameObserver1).worldIsUpdated, is(true));
        assertThat(((stubObserver) gameObserver2).worldIsUpdated, is(true));
    }

    @Test
    public void performUnitActionUpdatesObsevers() {
        gameAlpha.performUnitActionAt(new Position(2,0));
        assertThat(((stubObserver) gameObserver1).worldIsUpdated, is(true));
        assertThat(((stubObserver) gameObserver2).worldIsUpdated, is(true));
    }

}
