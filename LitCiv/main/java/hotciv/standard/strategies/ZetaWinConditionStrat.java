package hotciv.standard.strategies;

import hotciv.standard.*;
import hotciv.framework.*;

public class ZetaWinConditionStrat implements WinConditionStrat {
    private WinConditionStrat betaCivStrategy, epsilonCivStrategy, currentState;
    private int roundsPlayed;

    public ZetaWinConditionStrat() {
        this.betaCivStrategy = new BetaWinConditionStrat();
        this.epsilonCivStrategy = new EpsilonWinConditionStrat();
        this.currentState = null;
        this.roundsPlayed = 0;
    }

    @Override
    public Player winCondition(GameImpl gameImpl) {
        if(roundsPlayed>20) {
            currentState = epsilonCivStrategy;
        } else {
            currentState = betaCivStrategy;
        }
        return currentState.winCondition(gameImpl);
    }

    @Override
    public void incrementBattleWon(Player p) {
        currentState.incrementBattleWon(p);
    }

    @Override
    public void incrementRounds() {
        roundsPlayed++;
    }
}
