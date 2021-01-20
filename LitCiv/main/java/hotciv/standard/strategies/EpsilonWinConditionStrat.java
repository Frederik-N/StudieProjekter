package hotciv.standard.strategies;

import hotciv.standard.*;
import hotciv.framework.*;

public class EpsilonWinConditionStrat implements WinConditionStrat {
    int redBattlesWon;
    int blueBattlesWon;

    @Override
    public Player winCondition(GameImpl gameImpl) {
        if (redBattlesWon >= 3) return Player.RED;
        else if (blueBattlesWon >=3) return Player.BLUE;
        else return null;
    }

    @Override
    public void incrementBattleWon(Player p) {
        if (p == Player.RED) redBattlesWon++;
        else blueBattlesWon++;
    }

    @Override
    public void incrementRounds() {
    }
}
