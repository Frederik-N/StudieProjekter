package hotciv.standard;

import hotciv.framework.WorldMapStrat;
import hotciv.standard.factories.DeltaCivFactory;
import hotciv.standard.strategies.FractalAdapter;

public class DeltaFractalCivFactory extends DeltaCivFactory {
    @Override
    public WorldMapStrat createWorldMapStrat() {
        return new FractalAdapter();
    }
}
