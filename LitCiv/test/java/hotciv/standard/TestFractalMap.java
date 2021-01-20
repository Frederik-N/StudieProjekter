package hotciv.standard;
import hotciv.framework.*;

import hotciv.standard.factories.AlphaCivFactory;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestFractalMap {
    private GameImpl gameDelta;

    @Test
    public void shouldGenerateRandomTiles() {
        List<String> tiles = new ArrayList<>();
        // Add random tiles to list
        for (int i = 0; i < 100; i++) {
            gameDelta = new GameImpl(new DeltaFractalCivFactory());
            Position p = new Position(0,0);
            tiles.add(gameDelta.getTileAt(p).getTypeString());
        }
        // Check that all the tiles are not equal
        int count = 0;
        for (String t: tiles) {
            if (t == tiles.get(0)) count++;
        }
        // If the tiles who were identical equals list size, they must all be the same
        assertNotEquals(count, tiles.size());
    }
}
