package hotciv.standard;

import hotciv.framework.DiceStrat;

public class FixedDiceStrat implements DiceStrat {
    @Override
    public int rollDice() {
        return 1;
    }

    public int rollDice2() { return 3; }
}
