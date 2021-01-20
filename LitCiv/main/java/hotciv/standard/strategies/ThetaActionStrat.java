package hotciv.standard.strategies;

import hotciv.standard.*;
import hotciv.framework.*;
import hotciv.utility.Utility;

public class ThetaActionStrat implements UnitActionStrat {
    private UnitActionStrat gammaActionStrat = new GammaActionStrat();

    @Override
    public void performAction(GameImpl gameImpl, Position p) {
        if (gameImpl.getUnitAt(p).getTypeString() == GameConstants.BOMB) performBomb(gameImpl, p);
        else gammaActionStrat.performAction(gameImpl, p);
    }

    private void performBomb(GameImpl gameImpl, Position p) {
        for (Position p2 : Utility.get8neighborhoodOf(p)) {
            CityImpl tCity = (CityImpl) gameImpl.getCityAt(p2);
            boolean isCity = tCity != null;
            if (isCity) {
                boolean isCitySize1 = tCity.getSize() == 1;
                if (isCitySize1) gameImpl.removeCity(p2);
                else {
                    ((CityImpl) gameImpl.getCityAt(p2)).decrementSize();
                }
            }
            // Remove units arounds
            gameImpl.removeUnit(p2);
        }
        // Remove Bomb itself
        gameImpl.removeUnit(p);
    }
}