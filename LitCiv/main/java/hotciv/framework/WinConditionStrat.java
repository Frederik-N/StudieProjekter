package hotciv.framework;

import hotciv.framework.Player;
import hotciv.standard.GameImpl;

public interface WinConditionStrat {
    Player winCondition(GameImpl GameImpl);
    void incrementBattleWon(Player p);
    void incrementRounds();
}
