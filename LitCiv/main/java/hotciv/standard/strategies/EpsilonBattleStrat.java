package hotciv.standard.strategies;

import hotciv.standard.*;
import hotciv.framework.*;
import hotciv.utility.Utility;

public class EpsilonBattleStrat implements BattleStrat {
    DiceStrat diceStrat;

    public EpsilonBattleStrat(DiceStrat diceStrat){
        this.diceStrat = diceStrat;
    }

    @Override
    public boolean battle(GameImpl gameImpl, Position from, Position to) {
        int attackingUnitAtk = (gameImpl.getUnitAt(from).getAttackingStrength() + getSupport(gameImpl, from)) * getMultiplier(gameImpl, from);
        int defendingUnitDef = (gameImpl.getUnitAt(to).getDefensiveStrength() + getSupport(gameImpl, to)) * getMultiplier(gameImpl, to);
        int d1 = diceStrat.rollDice();
        int d2 = diceStrat.rollDice();

        return attackingUnitAtk * d1 > defendingUnitDef * d2;
    }

    public int getSupport(GameImpl gameImpl, Position p) {
        int support = -1;
        for (Position p2 : Utility.get8neighborhoodOf(p)) {
            boolean isUnitOnTile = gameImpl.getUnitAt(p2) != null;
            if (isUnitOnTile) {
                boolean isOwnUnit = gameImpl.getUnitAt(p2).getOwner() == gameImpl.getPlayerInTurn();
                if (isOwnUnit) support++;
            }
        }
        return support;
    }

    public int getMultiplier(GameImpl gameImpl, Position p) {
        int multi = 1;

        boolean isHill = gameImpl.getTileAt(p).getTypeString() == GameConstants.HILLS;
        if (isHill) multi = 2;

        boolean isForest = gameImpl.getTileAt(p).getTypeString() == GameConstants.FOREST;
        if (isForest) multi = 2;

        boolean isCity = gameImpl.getCityAt(p) != null;
        if (isCity) multi = 3;

        return multi;
    }
}
