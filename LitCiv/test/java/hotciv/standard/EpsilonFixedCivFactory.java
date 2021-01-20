package hotciv.standard;

import hotciv.framework.*;
import hotciv.standard.*;
import hotciv.standard.factories.*;
import hotciv.standard.strategies.*;

public class EpsilonFixedCivFactory extends EpsilonCivFactory {
    @Override
    public WorldMapStrat createWorldMapStrat() {
        return new WorldMapForTestingEpsilonStrat();
    }

    @Override
    public BattleStrat createBattleStrat() {
        return new EpsilonBattleStrat(new FixedDiceStrat());
    }

}
