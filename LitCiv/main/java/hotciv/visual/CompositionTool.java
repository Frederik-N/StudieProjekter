package hotciv.visual;

import hotciv.framework.Game;
import hotciv.view.GfxConstants;
import hotciv.view.UnitFigure;
import minidraw.framework.DrawingEditor;
import minidraw.framework.Figure;
import minidraw.framework.Tool;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class CompositionTool implements Tool {
    private Game game;
    private DrawingEditor editor;
    private Tool currentTool, setFocusTool, endOfTurnTool, actionTool, unitMoveTool;


    public CompositionTool(DrawingEditor editor, Game game) {
        this.editor = editor;
        this.game = game;
        setFocusTool = new SetFocusTool(editor, game);
        endOfTurnTool = new EndOfTurnTool(editor, game);
        actionTool = new ActionTool(editor, game);
        unitMoveTool = new UnitMoveTool(editor, game);
    }

    @Override
    public void mouseDown(MouseEvent e, int x, int y) {

        if(editor.drawing().findFigure(x,y)!=null) {
            Figure currentFigure = editor.drawing().findFigure(x,y);
            Point currentpoint = currentFigure.displayBox().getLocation();
            setFocusTool.mouseDown(e,x,y);

            if(currentpoint.equals(new Point(GfxConstants.TURN_SHIELD_X,GfxConstants.TURN_SHIELD_Y))) {
                currentTool = new EndOfTurnTool(editor, game);
                currentTool.mouseDown(e,x,y);
            }

            if(currentpoint.equals(new Point(GfxConstants.REFRESH_BUTTON_X,GfxConstants.REFRESH_BUTTON_Y))) {
                editor.drawing().requestUpdate();
            }

            if(currentFigure.getClass()== UnitFigure.class) {
                currentTool = new UnitMoveTool(editor, game);
                currentTool.mouseDown(e,x,y);
            }

            if(e.isShiftDown() && currentFigure.getClass()== UnitFigure.class) {
                currentTool = new ActionTool(editor, game);
                currentTool.mouseDown(e,x,y);
            }
        }
    }

    @Override
    public void mouseDrag(MouseEvent e, int x, int y) {
        if(currentTool!=null) {
            currentTool.mouseDrag(e, x, y);
        }
    }

    @Override
    public void mouseUp(MouseEvent e, int x, int y) {
        if(currentTool!=null)  {
            currentTool.mouseUp(e,x,y);
        }
        setFocusTool.mouseDown(e,x,y);
    }

    @Override
    public void mouseMove(MouseEvent e, int x, int y) {
    }

    @Override
    public void keyDown(KeyEvent e, int key) {
    }

}
