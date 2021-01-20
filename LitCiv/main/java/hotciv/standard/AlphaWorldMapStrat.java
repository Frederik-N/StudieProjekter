package hotciv.standard;

import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.framework.WorldMapStrat;

import java.util.HashMap;

public class AlphaWorldMapStrat implements WorldMapStrat {

    @Override
    public HashMap<Position, CityImpl> generateCityMap() {
        HashMap<Position, CityImpl> cities = new HashMap<>();
        cities.put(new Position(1, 1), new CityImpl(Player.RED, 0, GameConstants.LEGION, GameConstants.foodFocus));
        cities.put(new Position(4, 1), new CityImpl(Player.BLUE, 0, GameConstants.LEGION, GameConstants.foodFocus));
        return cities;
    }

    @Override
    public HashMap<Position, UnitImpl> generateUnitMap() {
        HashMap<Position, UnitImpl> units = new HashMap<>();
        units.put(new Position(2,0), new UnitImpl(Player.RED, GameConstants.ARCHER));
        units.put(new Position(3,2), new UnitImpl(Player.BLUE, GameConstants.LEGION));
        units.put(new Position(4,3), new UnitImpl(Player.RED, GameConstants.SETTLER));
        return units;
    }

    @Override
    public TileImpl[][] generateTileMap() {
        TileImpl[][] tiles = new TileImpl[GameConstants.WORLDSIZE][GameConstants.WORLDSIZE];
        for (int i = 0; i < GameConstants.WORLDSIZE; i++) {
            for (int j = 0; j < GameConstants.WORLDSIZE; j++) {
                tiles[i][j] = new TileImpl(GameConstants.PLAINS);
            }
        }
        tiles[0][1] = new TileImpl(GameConstants.HILLS);
        tiles[1][0] = new TileImpl(GameConstants.OCEANS);
        tiles[2][2] = new TileImpl(GameConstants.MOUNTAINS);
        return tiles;
    }
}
