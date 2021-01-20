package hotciv.standard.strategies;

import hotciv.standard.*;
import hotciv.framework.*;

public class GammaActionStrat implements UnitActionStrat {
    @Override
    public void performAction(GameImpl gameImpl, Position p) {
        switch (gameImpl.getUnitAt(p).getTypeString()) {
            case GameConstants.ARCHER:
                performArcher(gameImpl, p);
                break;
            case GameConstants.SETTLER:
                performSettler(gameImpl, p);
                break;
        }
    }

    private void performSettler(GameImpl gameImpl, Position p) {
        gameImpl.createCityAt(p, new CityImpl(gameImpl.getUnitAt(p).getOwner(), 0, GameConstants.LEGION, GameConstants.foodFocus));
        gameImpl.removeUnit(p);
    }

    private void performArcher(GameImpl gameImpl, Position p) {
        // If unit is fortified, remove fortify else make it fortify.
        UnitImpl unit = (UnitImpl) gameImpl.getUnitAt(p);
        // Unfortify
        if (!unit.getMoveable()) {
            unit.setDefensiveStrength(GameConstants.ARCHERDEF);
        }
        // Fortify
        else {
            unit.setDefensiveStrength(GameConstants.ARCHERDEF*2);
        }
        unit.switchMoveable();
        gameImpl.createUnitAt(p, unit);
    }
}
