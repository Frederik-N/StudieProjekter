package hotciv.standard;

import hotciv.framework.BattleStrat;
import hotciv.framework.Position;

public class AlphaBattleStrat implements BattleStrat {

    @Override
    public boolean battle(GameImpl GameImpl, Position from, Position to) {
        return true;
    }
}
