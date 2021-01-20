package hotciv.visual;

import minidraw.standard.*;
import minidraw.framework.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import hotciv.framework.*;
import hotciv.view.*;
import hotciv.stub.*;
import minidraw.standard.handlers.StandardRubberBandSelectionStrategy;

/** Template code for exercise FRS 36.39.

   This source code is from the book 
     "Flexible, Reliable Software:
       Using Patterns and Agile Development"
     published 2010 by CRC Press.
   Author: 
     Henrik B Christensen 
     Computer Science Department
     Aarhus University
   
   This source code is provided WITHOUT ANY WARRANTY either 
   expressed or implied. You may study, use, modify, and 
   distribute it for non-commercial purposes. For any 
   commercial use, see http://www.baerbak.com/
 */
public class ShowMove {

  public static void main(String[] args) {
    Game game = new StubGameMove();

    DrawingEditor editor =
            new MiniDrawApplication("Move any unit using the mouse",
                    new HotCivFactory4(game));
    editor.open();
    editor.showStatus("Move units to see Game's moveUnit method being called.");

    editor.setTool(new UnitMoveTool(editor, game));
  }
}

//TODO implement dragging movement
class UnitMoveTool extends SelectionTool {
  private Game game;
  private DrawingEditor editor;
  private Position posFrom, posTo;
  
  public UnitMoveTool(DrawingEditor editor, Game game) {
    super(editor, new StandardRubberBandSelectionStrategy());
    this.editor = editor;
    this.game = game;
  }

  @Override
  public void mouseDown(MouseEvent e, int x, int y) {
    Drawing model = editor().drawing();
    posFrom =  GfxConstants.getPositionFromXY(x,y);
    draggedFigure = model.findFigure(e.getX(), e.getY());
      super.mouseDown(e, x, y);
  }

  @Override
  public void mouseDrag(MouseEvent e, int x, int y) {
    if(draggedFigure==null) return;
      super.mouseDrag(e, x, y);
      draggedFigure.invalidate();
  }

  @Override
  public void mouseUp(MouseEvent e, int x, int y) {
    if(draggedFigure==null) return;
    editor().drawing().unlock();
    posTo = GfxConstants.getPositionFromXY(x, y);
      if (posTo.getColumn() > 0 && posTo.getColumn() < GameConstants.WORLDSIZE && 0 < posTo.getRow() && posTo.getRow() < GameConstants.WORLDSIZE && draggedFigure != null && game.moveUnit(posFrom, posTo)) {
        Point pointTo = new Point(GfxConstants.getXFromColumn(posTo.getColumn()), GfxConstants.getYFromRow(posTo.getRow()));
        draggedFigure.displayBox().setLocation(pointTo);
    } else {
        Point pointFrom = new Point(GfxConstants.getXFromColumn(posFrom.getColumn()), GfxConstants.getYFromRow(posFrom.getRow()));
        draggedFigure.displayBox().setLocation(pointFrom);
        draggedFigure.invalidate();
      }
      draggedFigure  = null;
  }
}
