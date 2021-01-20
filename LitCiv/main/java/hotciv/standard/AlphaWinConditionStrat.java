package hotciv.standard;

import hotciv.framework.Player;
import hotciv.framework.WinConditionStrat;

public class AlphaWinConditionStrat implements WinConditionStrat {
    @Override
    public Player winCondition(GameImpl gameImpl) {
        if (gameImpl.getAge() == -3000) {
            return Player.RED;
        } else return null;
    }

    @Override
    public void incrementBattleWon(Player p) {

    }

    @Override
    public void incrementRounds() {

    }
}
