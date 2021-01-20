package hotciv.standard.factories;

import hotciv.framework.*;
import hotciv.standard.*;
import hotciv.standard.strategies.*;

public class BetaCivFactory implements GameFactory {
    @Override
    public WinConditionStrat createWinConditionStrat() {
        return new BetaWinConditionStrat();
    }

    @Override
    public WorldMapStrat createWorldMapStrat() {
        return new AlphaWorldMapStrat();
    }

    @Override
    public BattleStrat createBattleStrat() {
        return new AlphaBattleStrat();
    }

    @Override
    public AgeWorldStrat createAgeWorldStrat() {
        return new BetaAgeWorldStrat();
    }

    @Override
    public UnitActionStrat createUnitActionStrat() {
        return new AlphaActionStrat();
    }
}
