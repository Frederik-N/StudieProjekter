package hotciv.standard.strategies;

import hotciv.framework.DiceStrat;

public class RandomDiceStrat implements DiceStrat {

    @Override
    public int rollDice() {
        return (int) (Math.random() * 6) + 1;
    }
}
