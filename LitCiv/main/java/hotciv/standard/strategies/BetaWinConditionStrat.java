package hotciv.standard.strategies;

import hotciv.standard.*;
import hotciv.framework.*;

public class BetaWinConditionStrat implements WinConditionStrat {
    @Override
    public Player winCondition(GameImpl gameImpl) {
        int redCount = 0;
        for (Position p : gameImpl.getCities().keySet()) {
            if (gameImpl.getCityAt(p).getOwner() == Player.RED) {
                redCount++;
            }
        }
        int size = gameImpl.getCities().size();
        // If all cities are owned by RED, he wins
        if (redCount == size) {
            return Player.RED;
            // If no cities are owned by RED, BLUE must own them and he wins
        } else if (redCount == 0) {
            return Player.BLUE;
            // Nobody owns all cities
        } else {
            return null;
        }
    }

    @Override
    public void incrementBattleWon(Player p) {

    }

    @Override
    public void incrementRounds() {

    }
}
