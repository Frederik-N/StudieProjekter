package hotciv.standard.factories;

import hotciv.framework.*;
import hotciv.standard.*;
import hotciv.standard.strategies.*;

public class SemiCivFactory implements GameFactory {
    @Override
    public WinConditionStrat createWinConditionStrat() {
        return new EpsilonWinConditionStrat();
    }

    @Override
    public WorldMapStrat createWorldMapStrat() {
        return new DeltaWorldMapStrat();
    }

    @Override
    public BattleStrat createBattleStrat() {
        return new EpsilonBattleStrat(new RandomDiceStrat());
    }

    @Override
    public AgeWorldStrat createAgeWorldStrat() {
        return new BetaAgeWorldStrat();
    }

    @Override
    public UnitActionStrat createUnitActionStrat() {
        return new GammaActionStrat();
    }
}
