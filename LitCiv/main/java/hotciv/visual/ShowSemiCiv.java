package hotciv.visual;

import hotciv.framework.Game;
import hotciv.standard.GameImpl;
import hotciv.standard.factories.SemiCivFactory;
import minidraw.framework.DrawingEditor;
import minidraw.standard.MiniDrawApplication;

public class ShowSemiCiv {
    public static void main(String[] args) {
        Game game = new GameImpl(new SemiCivFactory());

        DrawingEditor editor =
                new MiniDrawApplication("SemiCiv Game",
                        new HotCivFactory4(game));
        editor.open();
        editor.showStatus("Play the game.");

        editor.setTool(new CompositionTool(editor, game));
    }
}
