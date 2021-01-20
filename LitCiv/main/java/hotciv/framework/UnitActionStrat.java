package hotciv.framework;

import hotciv.framework.*;
import hotciv.standard.GameImpl;

import java.util.*;

public interface UnitActionStrat {
    void performAction(GameImpl gameImpl, Position p);
}
