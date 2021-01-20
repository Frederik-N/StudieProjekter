package hotciv.framework;


import hotciv.framework.Position;
import hotciv.standard.CityImpl;
import hotciv.standard.TileImpl;
import hotciv.standard.UnitImpl;

import java.util.HashMap;

public interface WorldMapStrat {

    public HashMap<Position, CityImpl> generateCityMap();

    public HashMap<Position, UnitImpl> generateUnitMap();

    public TileImpl[][] generateTileMap();



}
