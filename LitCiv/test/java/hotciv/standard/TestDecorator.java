package hotciv.standard;

import hotciv.framework.*;

import hotciv.standard.factories.AlphaCivFactory;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestDecorator {
    private Game gameAlpha, decoratee;

    @Before
    public void setUp() {
        gameAlpha = new TranscriptDecorator(new GameImpl(new AlphaCivFactory()));
        decoratee = new GameImpl(new AlphaCivFactory());
    }

    public void changeTranscriptionMode() {
        if (gameAlpha == decoratee) {
            System.out.println("Transcription is turned on.");
            decoratee = gameAlpha;
            gameAlpha = new TranscriptDecorator(gameAlpha);
        } else {
            System.out.println("Transcription is turned off.");
            gameAlpha = decoratee;
        }
    }

    @Test
    public void TranscriptionIsAddedAndRemovedAtRunTime() {
        // Actions with transcription
        gameAlpha.moveUnit(new Position(2,0), new Position(3,1));
        gameAlpha.changeProductionInCityAt(new Position(1,1), GameConstants.ARCHER);
        gameAlpha.endOfTurn();

        // Turning off transcription
        changeTranscriptionMode();
        assertThat(gameAlpha, instanceOf(GameImpl.class));

        // Actions without transcription
        gameAlpha.endOfTurn();

        // Turning on transcription
        changeTranscriptionMode();
        assertThat(gameAlpha, instanceOf(TranscriptDecorator.class));

        // Actions with transcription
        gameAlpha.changeWorkForceFocusInCityAt(new Position(4,1), GameConstants.foodFocus);
    }
}
