package hotciv.standard.strategies;

import hotciv.standard.*;
import hotciv.framework.*;

import thirdparty.ThirdPartyFractalGenerator;

import java.util.HashMap;

public class FractalAdapter implements WorldMapStrat {
    ThirdPartyFractalGenerator fractalGenerator = new ThirdPartyFractalGenerator();
    DeltaWorldMapStrat deltaWorldMapStrat = new DeltaWorldMapStrat();

    @Override
    public HashMap<Position, CityImpl> generateCityMap() {
        return new HashMap<Position, CityImpl>();
    }

    @Override
    public HashMap<Position, UnitImpl> generateUnitMap() {
        return new HashMap<Position, UnitImpl>();
    }

    @Override
    public TileImpl[][] generateTileMap() {
        TileImpl[][] tiles = new TileImpl[GameConstants.WORLDSIZE][GameConstants.WORLDSIZE];

        for (int r = 0; r < GameConstants.WORLDSIZE; r++ ) {
            for ( int c = 0; c < GameConstants.WORLDSIZE; c++ ) {
                String type = deltaWorldMapStrat.letterToType(fractalGenerator.getLandscapeAt(r,c));
                tiles[r][c] = new TileImpl(type);
            }
        }
        return tiles;
    }
}
