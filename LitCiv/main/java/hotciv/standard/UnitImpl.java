package hotciv.standard;

import hotciv.framework.Player;
import hotciv.framework.Unit;

import java.util.Map;
import java.util.UUID;

public class UnitImpl implements Unit {
    public UnitImpl(Player p, String type) {
        this.p = p;
        this.type = type;
        this.stats = allStats.get(type).clone();
        maxMoves = stats[moves];
        objectID = UUID.randomUUID().toString();
    }

    private final String type;
    private Player p;
    private boolean moveable = true;
    private static Map<String, int[]> allStats;
    // {Defense, Attack, Moves, Cost}
    private int[] stats;
    private final int defense = 0;
    private final int attack = 1;
    private final int moves = 2;
    private final int cost = 3;
    private final int flying = 4;
    private int maxMoves;
    private String objectID;

    @Override
    public String getTypeString() {
        return type;
    }

    @Override
    public Player getOwner() {
        return p;
    }

    @Override
    public int getMoveCount() {
        return stats[moves];
    }

    @Override
    public int getDefensiveStrength() {
        return stats[defense];
    }

    @Override
    public int getAttackingStrength() {
        return stats[attack];
    }

    @Override
    public String getID() {
        return objectID;
    }

    public void setDefensiveStrength(int d) { stats[defense] = d; }

    public boolean getMoveable() {return moveable;}

    public void switchMoveable() {
        if (moveable) {
            stats[moves] = 0;
        }
        moveable = !moveable;
    }

    public void setMoveCount(int temp) {
        stats[moves] = temp;
    }

    public int getMaxMoveCount() {
        return maxMoves;
    }

    public int getCost() {
        return stats[cost];
    }

    public boolean getFlying() {
        // 1 for true and 0 for false.
        if (stats[flying] == 1) return true; else return false;
    }

    public int[] getStats() { return stats;
    }

    public static void initializeStats(Map<String, int[]> allStats) {
        UnitImpl.allStats = allStats;
    }
}

