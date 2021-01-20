package hotciv.standard.factories;

import hotciv.framework.*;
import hotciv.standard.*;
import hotciv.standard.strategies.*;

public class EpsilonCivFactory implements GameFactory {
    @Override
    public WinConditionStrat createWinConditionStrat() {
        return new EpsilonWinConditionStrat();
    }

    @Override
    public WorldMapStrat createWorldMapStrat() {
        return new AlphaWorldMapStrat();
    }

    @Override
    public BattleStrat createBattleStrat() {
        return new EpsilonBattleStrat(new RandomDiceStrat());
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
