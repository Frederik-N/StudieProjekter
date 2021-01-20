package hotciv.framework;

public interface GameFactory {
    WinConditionStrat createWinConditionStrat();
    WorldMapStrat createWorldMapStrat();
    BattleStrat createBattleStrat();
    AgeWorldStrat createAgeWorldStrat();
    UnitActionStrat createUnitActionStrat();
}
