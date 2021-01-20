package hotciv.standard;

import hotciv.framework.AgeWorldStrat;

public class AlphaAgeWorldStrat implements AgeWorldStrat {
    @Override
    public int ageWorld(int gameAge) {
        return gameAge += 100;
    }
}
