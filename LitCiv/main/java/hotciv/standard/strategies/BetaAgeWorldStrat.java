package hotciv.standard.strategies;

import hotciv.framework.*;

public class BetaAgeWorldStrat implements AgeWorldStrat {

    @Override
    public int ageWorld(int gameAge) {
        // gameAge between -4000 and -100(-200+100)
        if (-4000 <= gameAge && gameAge <= -200) {
            return gameAge += 100;
        } else if (gameAge == -100) {
            return -1;
        } else if (gameAge == -1) {
            return 1;
        } else if (gameAge == 1) {
            return 50;
            // gameAge between 50 and 1750(1700+50)
        } else if (50 <= gameAge && gameAge <= 1700) {
            return gameAge += 50;
            // gameAge between 1750 and 1900(1875+25)
        } else if (1750 <= gameAge && gameAge <= 1875) {
            return gameAge += 25;
            // gameAge between 1900 and 1970(1965+5)
        } else if (1900 <= gameAge && gameAge <= 1965) {
            return gameAge += 5;
        } else
            return gameAge += 1;
    }
}
