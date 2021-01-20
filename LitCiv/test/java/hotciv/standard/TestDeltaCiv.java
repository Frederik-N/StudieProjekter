package hotciv.standard;

import hotciv.framework.*;
import hotciv.standard.*;
import hotciv.standard.factories.*;
import hotciv.standard.strategies.*;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestDeltaCiv {
    private DeltaWorldMapStrat delta;
    private Game gameDelta;

    @Before
    public void setUp() {
        delta = new DeltaWorldMapStrat();
        gameDelta = new GameImpl(new DeltaCivFactory());
    }

    @Test
    public void tileMapDelta() {
        // Checks 10 different tiles that they are indeed at the correct place.
        assertThat(gameDelta.getTileAt(new Position(2, 0)).getTypeString(), is(GameConstants.OCEANS));
        assertThat(gameDelta.getTileAt(new Position(7, 0)).getTypeString(), is(GameConstants.OCEANS));
        assertThat(gameDelta.getTileAt(new Position(3, 4)).getTypeString(), is(GameConstants.MOUNTAINS));
        assertThat(gameDelta.getTileAt(new Position(4, 8)).getTypeString(), is(GameConstants.HILLS));
        assertThat(gameDelta.getTileAt(new Position(5, 2)).getTypeString(), is(GameConstants.FOREST));
        assertThat(gameDelta.getTileAt(new Position(5, 5)).getTypeString(), is(GameConstants.FOREST));
        assertThat(gameDelta.getTileAt(new Position(2, 5)).getTypeString(), is(GameConstants.PLAINS));
        assertThat(gameDelta.getTileAt(new Position(4, 10)).getTypeString(), is(GameConstants.PLAINS));
        assertThat(gameDelta.getTileAt(new Position(0, 8)).getTypeString(), is(GameConstants.PLAINS));
        assertThat(gameDelta.getTileAt(new Position(3, 2)).getTypeString(), is(GameConstants.PLAINS));
    }

    @Test
    public void cityMapDelta() {
        // Checks if cities is at correct places.
        assertThat(gameDelta.getCityAt(new Position(8, 12)).getOwner(), is(Player.RED));
        assertThat(gameDelta.getCityAt(new Position(4, 5)).getOwner(), is(Player.BLUE));
    }

    @Test
    public void unitMapDelta() {
        // Checks if units is at correct places.
        assertThat(gameDelta.getUnitAt(new Position(3, 8)).getOwner(), is(Player.RED));
        assertThat(gameDelta.getUnitAt(new Position(3, 8)).getTypeString(), is(GameConstants.ARCHER));
        assertThat(gameDelta.getUnitAt(new Position(4, 4)).getOwner(), is(Player.BLUE));
        assertThat(gameDelta.getUnitAt(new Position(4, 4)).getTypeString(), is(GameConstants.LEGION));
        assertThat(gameDelta.getUnitAt(new Position(5, 5)).getOwner(), is(Player.RED));
        assertThat(gameDelta.getUnitAt(new Position(5, 5)).getTypeString(), is(GameConstants.SETTLER));

    }
}
