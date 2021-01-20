package hotciv.standard.factories;

import hotciv.framework.*;
import hotciv.standard.*;
import hotciv.standard.strategies.*;

public class DeltaCivFactory implements GameFactory {
    @Override
    public WinConditionStrat createWinConditionStrat() {
        return new AlphaWinConditionStrat();
    }

    @Override
    public WorldMapStrat createWorldMapStrat() {
        return new DeltaWorldMapStrat();
    }

    @Override
    public BattleStrat createBattleStrat() {
        return new AlphaBattleStrat();
    }

    @Override
    public AgeWorldStrat createAgeWorldStrat() {
        return new AlphaAgeWorldStrat();
    }

    @Override
    public UnitActionStrat createUnitActionStrat() {
        return new AlphaActionStrat();
    }
}
