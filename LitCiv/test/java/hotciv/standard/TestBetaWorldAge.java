package hotciv.standard;

import hotciv.framework.*;
import hotciv.standard.*;
import hotciv.standard.factories.*;
import hotciv.standard.strategies.*;
import org.junit.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestBetaWorldAge {
    private BetaAgeWorldStrat betaAge;

    @Before
    public void setUp() {
        betaAge = new BetaAgeWorldStrat();
    }

    @Test
    public void worldAges100in4000BCto100BC() {
        assertThat(betaAge.ageWorld(-4000), is(-3900));
        assertThat(betaAge.ageWorld(-200), is(-100));
    }

    @Test
    public void worldAges100BCto1BCto1ADto50AD() {
        assertThat(betaAge.ageWorld(-100), is(-1));
        assertThat(betaAge.ageWorld(-1), is(1));
        assertThat(betaAge.ageWorld(1), is(50));
    }

    @Test
    public void worldAges50in50ADto1750AD() {
        assertThat(betaAge.ageWorld(50), is(100));
        assertThat(betaAge.ageWorld(1700), is(1750));
    }

    @Test
    public void worldAges25in1750ADto1900AD() {
        assertThat(betaAge.ageWorld(1750), is(1775));
        assertThat(betaAge.ageWorld(1900-25), is(1900));
    }

    @Test
    public void worldAges5in1900ADto1970AD() {
        assertThat(betaAge.ageWorld(1900), is(1905));
        assertThat(betaAge.ageWorld(1965), is(1970));
    }

    @Test
    public void worldAges1after1970AD() {
        assertThat(betaAge.ageWorld(1970), is(1971));
    }
}
