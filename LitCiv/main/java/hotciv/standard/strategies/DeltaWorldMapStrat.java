package hotciv.standard.strategies;

import hotciv.standard.*;
import hotciv.framework.*;

import java.util.HashMap;

public class DeltaWorldMapStrat implements WorldMapStrat {

    @Override
    public HashMap<Position, CityImpl> generateCityMap() {
        HashMap<Position, CityImpl> cities = new HashMap<>();
        // Adds cities to world
        cities.put(new Position(8, 12),new CityImpl(Player.RED, 0, GameConstants.LEGION, GameConstants.foodFocus));
        cities.put(new Position(4, 5),new CityImpl(Player.BLUE, 0, GameConstants.LEGION, GameConstants.foodFocus));
        return cities;
    }

    @Override
    public HashMap<Position, UnitImpl> generateUnitMap() {
        HashMap<Position, UnitImpl> units = new HashMap<>();
        // Adds units to world
        units.put(new Position(3, 8), new UnitImpl(Player.RED, GameConstants.ARCHER));
        units.put(new Position(4,4), new UnitImpl(Player.BLUE, GameConstants.LEGION));
        units.put(new Position(5,5), new UnitImpl(Player.RED, GameConstants.SETTLER));
        return units;
    }

    @Override
    public TileImpl[][] generateTileMap() {
        TileImpl[][] tiles = new TileImpl[GameConstants.WORLDSIZE][GameConstants.WORLDSIZE];
        // Code from StubGame1.java from Henrik B Christensen. Which fills world with tiles.
        String[] layout =
                new String[] {
                        "...ooMooooo.....",
                        "..ohhoooofffoo..",
                        ".oooooMooo...oo.",
                        ".ooMMMoooo..oooo",
                        "...ofooohhoooo..",
                        ".ofoofooooohhoo.",
                        "...ooo..........",
                        ".ooooo.ooohooM..",
                        ".ooooo.oohooof..",
                        "offfoooo.offoooo",
                        "oooooooo...ooooo",
                        ".ooMMMoooo......",
                        "..ooooooffoooo..",
                        "....ooooooooo...",
                        "..ooohhoo.......",
                        ".....ooooooooo..",
                };
        String line;
        for (int r = 0; r < GameConstants.WORLDSIZE; r++ ) {
            line = layout[r];
            for ( int c = 0; c < GameConstants.WORLDSIZE; c++ ) {
                String type = letterToType(line.charAt(c));
                tiles[r][c] = new TileImpl(type);
            }
        }
        return tiles;
    }

    public String letterToType(char tileChar) {
        String type = "error";
        if ( tileChar == '.' ) { type = GameConstants.OCEANS; }
        if ( tileChar == 'o' ) { type = GameConstants.PLAINS; }
        if ( tileChar == 'M' ) { type = GameConstants.MOUNTAINS; }
        if ( tileChar == 'f' ) { type = GameConstants.FOREST; }
        if ( tileChar == 'h' ) { type = GameConstants.HILLS; }
        return type;
    }
}
