package hotciv.framework;

import hotciv.framework.Game;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.standard.GameImpl;

public interface BattleStrat {
    boolean battle(GameImpl GameImpl, Position from, Position to);
}
