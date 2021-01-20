package hotciv.view;

import hotciv.framework.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import minidraw.framework.*;
import minidraw.standard.*;

/** CivDrawing is a specialized Drawing (model component) from
 * MiniDraw that dynamically builds the list of Figures for MiniDraw
 * to render the Unit and other information objects that are visible
 * in the Game instance.
 *
 * TODO: This is a TEMPLATE for the SWEA Exercise! This means
 * that it is INCOMPLETE and that there are several options
 * for CLEANING UP THE CODE when you add features to it!

   This source code is from the book 
     "Flexible, Reliable Software:
       Using Patterns and Agile Development"
     published 2010 by CRC Press.
   Author: 
     Henrik B Christensen 
     Department of Computer Science
     Aarhus University
   
   Please visit http://www.baerbak.com/ for further information.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

public class CivDrawing 
  implements Drawing, GameObserver {
  
  protected Drawing delegate;
  /** store all moveable figures visible in this drawing = units */
  protected Map<Unit,UnitFigure> figureMap;

  /** store all cities visible in this drawing = cities */
  protected Map<City,CityFigure> cityFigureMap;

  /** the Game instance that this UnitDrawing is going to render units
   * from */
  protected Game game;

  protected int age = -4000;
  protected String playername = "red";

  public CivDrawing( DrawingEditor editor, Game game ) {
    super();
    this.delegate = new StandardDrawing();
    this.game = game;
    this.figureMap = new HashMap<>();
    this.cityFigureMap = new HashMap<>();

    // register this unit drawing as listener to any game state
    // changes...
    game.addObserver(this);
    // ... and build up the set of figures associated with
    // units in the game.
    defineUnitMap();
    // and the set of 'icons' in the status panel
    defineIcons();

    // build up the set of cities associated with cities in the game.
    defineCityMap();
  }
  
  /** The CivDrawing should not allow client side
   * units to add and manipulate figures; only figures
   * that renders game objects are relevant, and these
   * should be handled by observer events from the game
   * instance. Thus this method is 'killed'.
   */
  public Figure add(Figure arg0) {
    throw new RuntimeException("Should not be used...");
  }


  /** erase the old list of units, and build a completely new
   * one from scratch by iterating over the game world and add
   * Figure instances for each unit in the world.
   */
  protected void defineUnitMap() {
    // ensure no units of the old list are accidental in
    // the selection!
    clearSelection();
    removeAllUnitFigures();

    // iterate world, and create a unit figure for
    // each unit in the game world, as well as
    // create an association between the unit and
    // the unitFigure in 'figureMap'.
    Position p;
    for ( int r = 0; r < GameConstants.WORLDSIZE; r++ ) {
      for ( int c = 0; c < GameConstants.WORLDSIZE; c++ ) {
        p = new Position(r,c);
        Unit unit = game.getUnitAt(p);
        if ( unit != null ) {
          String type = unit.getTypeString();
          // convert the unit's Position to (x,y) coordinates
          Point point = new Point( GfxConstants.getXFromColumn(p.getColumn()),
                                   GfxConstants.getYFromRow(p.getRow()) );
          UnitFigure unitFigure =
            new UnitFigure( type, point, unit );
          unitFigure.addFigureChangeListener(this);
          figureMap.put(unit, unitFigure);

          // also insert in delegate list as it is
          // this list that is iterated by the
          // graphics rendering algorithms
          delegate.add(unitFigure);
        }
      }
    }
  }

  /** remove all unit figures in this
   * drawing, and reset the map (unit->unitfigure).
   * It is important to actually remove the figures
   * as it forces a graphical redraw of the screen
   * where the unit figure was.
   */
  protected void removeAllUnitFigures() {
    for (Unit u : figureMap.keySet()) {
      UnitFigure uf = figureMap.get(u);
      delegate.remove(uf);
    }
    figureMap.clear();
  }

  protected ImageFigure turnShieldIcon, cityOwnerIcon, productionIcon, workforceIcon, unitOwnerIcon, redCityIcon, refreshIcon;
  protected TextFigure ageIcon, movesIcon;
  protected void defineIcons() {
    delegate.remove(turnShieldIcon);
    delegate.remove(turnShieldIcon);

    delegate.remove(movesIcon);
    delegate.remove(cityOwnerIcon);
    delegate.remove(unitOwnerIcon);
    delegate.remove(ageIcon);
    delegate.remove(productionIcon);
    delegate.remove(refreshIcon);
    delegate.remove(redCityIcon);
    delegate.remove(workforceIcon);
    delegate.requestUpdate();
    // TODO: Further development to include rest of figures needed
    turnShieldIcon = 
      new ImageFigure( playername+"shield",
                       new Point( GfxConstants.TURN_SHIELD_X,
                                  GfxConstants.TURN_SHIELD_Y ) ); 
    // insert in delegate figure list to ensure graphical
    // rendering.
    delegate.add(turnShieldIcon);
    ageIcon = new TextFigure(String.valueOf(age)+" BC", new Point(GfxConstants.AGE_TEXT_X, GfxConstants.AGE_TEXT_Y));
    delegate.add(ageIcon);

    movesIcon = new TextFigure("", new Point(GfxConstants.UNIT_COUNT_X, GfxConstants.UNIT_COUNT_Y));

    cityOwnerIcon = new ImageFigure("blueshield", new Point(GfxConstants.CITY_SHIELD_X, GfxConstants.CITY_SHIELD_Y));

    productionIcon = new ImageFigure("legion", new Point(GfxConstants.CITY_PRODUCTION_X, GfxConstants.CITY_PRODUCTION_Y));

    workforceIcon = new ImageFigure("hammer", new Point(GfxConstants.WORKFORCEFOCUS_X, GfxConstants.WORKFORCEFOCUS_Y));

    unitOwnerIcon = new ImageFigure("blueshield", new Point(GfxConstants.UNIT_SHIELD_X, GfxConstants.UNIT_SHIELD_Y));

    refreshIcon = new ImageFigure("refresh", new Point(GfxConstants.REFRESH_BUTTON_X, GfxConstants.REFRESH_BUTTON_Y));
    delegate.add(refreshIcon);
  }
 
  // === Observer Methods ===

  public void worldChangedAt(Position pos) {
    clearSelection();
    // this is a really brute-force algorithm: destroy
    // all known units and build up the entire set again


    removeAllCityFigures();
    defineCityMap();
    removeAllUnitFigures();
    defineUnitMap();
  }

  private void removeAllCityFigures() {
    for (City c : cityFigureMap.keySet()) {
      CityFigure cf = cityFigureMap.get(c);
      delegate.remove(cf);
    }
    cityFigureMap.clear();
  }

  public void turnEnds(Player currentPlayer, int age) {
    this.age = age;
    if ( currentPlayer == Player.BLUE ) { playername = "blue"; } else {
      playername = "red";
    }
    turnShieldIcon.set( playername+"shield",
                        new Point( GfxConstants.TURN_SHIELD_X,
                                   GfxConstants.TURN_SHIELD_Y ) );
    ageIcon.setText(age+ " BC");
  }

  public void tileFocusChangedAt(Position position) {
    if(unitOwnerIcon != null) {
      delegate.remove(unitOwnerIcon);
      delegate.remove(movesIcon);
    }
    if(cityOwnerIcon != null) {
      delegate.remove(cityOwnerIcon);
      delegate.remove(productionIcon);
      delegate.remove(workforceIcon);
    }
    boolean isCityAtPosition = game.getCityAt(position) != null;
    if(isCityAtPosition) {
      City city = game.getCityAt(position);
      cityOwnerIcon.set(city.getOwner().toString().toLowerCase()+"shield", new Point(GfxConstants.CITY_SHIELD_X, GfxConstants.CITY_SHIELD_Y));
      delegate.add(cityOwnerIcon);
      productionIcon.set(game.getCityAt(position).getProduction(),new Point(GfxConstants.CITY_PRODUCTION_X, GfxConstants.CITY_PRODUCTION_Y));
      delegate.add(productionIcon);
      workforceIcon.set(game.getCityAt(position).getWorkforceFocus(),new Point(GfxConstants.WORKFORCEFOCUS_X, GfxConstants.WORKFORCEFOCUS_Y));
      delegate.add(workforceIcon);
    }
    boolean isUnitAtPosition = game.getUnitAt(position) != null;
    if(isUnitAtPosition) {
      Unit unit = game.getUnitAt(position);
      unitOwnerIcon.set(unit.getOwner().toString().toLowerCase()+"shield", new Point(GfxConstants.UNIT_SHIELD_X, GfxConstants.UNIT_SHIELD_Y));
      delegate.add(unitOwnerIcon);
      movesIcon.setText(String.valueOf(unit.getMoveCount()));
      delegate.add(movesIcon);
    }
  }

  @Override
  public void requestUpdate() {
    // A request has been issued to repaint
    // everything. We simply rebuild the
    // entire Drawing.
    defineIcons();
    defineCityMap();
    defineUnitMap();
  }

  private void defineCityMap() {
    clearSelection();
    Position p;
    for ( int r = 0; r < GameConstants.WORLDSIZE; r++ ) {
      for ( int c = 0; c < GameConstants.WORLDSIZE; c++ ) {
        p = new Position(r,c);
        City city = game.getCityAt(p);
        if ( city != null ) {
          Point point = new Point( GfxConstants.getXFromColumn(p.getColumn()),
                  GfxConstants.getYFromRow(p.getRow()) );
          CityFigure cityFigure =
                  new CityFigure(city, point);
          cityFigure.addFigureChangeListener(this);
          cityFigureMap.put(city, cityFigure);
          delegate.add(cityFigure);
        }
      }
    }
  }

  @Override
  public void addToSelection(Figure arg0) {
    delegate.addToSelection(arg0);
  }

  @Override
  public void clearSelection() {
    delegate.clearSelection();
  }

  @Override
  public void removeFromSelection(Figure arg0) {
    delegate.removeFromSelection(arg0);
  }

  @Override
  public List<Figure> selection() {
    return delegate.selection();
  }

  @Override
  public void toggleSelection(Figure arg0) {
    delegate.toggleSelection(arg0);
  }

  @Override
  public void figureChanged(FigureChangeEvent arg0) {
    delegate.figureChanged(arg0);
  }

  @Override
  public void figureInvalidated(FigureChangeEvent arg0) {
    delegate.figureInvalidated(arg0);
  }

  @Override
  public void figureRemoved(FigureChangeEvent arg0) {
    delegate.figureRemoved(arg0);
  }

  @Override
  public void figureRequestRemove(FigureChangeEvent arg0) {
    delegate.figureRequestRemove(arg0);
  }

  @Override
  public void figureRequestUpdate(FigureChangeEvent arg0) {
    delegate.figureRequestUpdate(arg0);
  }

  @Override
  public void addDrawingChangeListener(DrawingChangeListener arg0) {
    delegate.addDrawingChangeListener(arg0);   
  }

  @Override
  public void removeDrawingChangeListener(DrawingChangeListener arg0) {
    delegate.removeDrawingChangeListener(arg0);
  }

  @Override
  public Figure findFigure(int arg0, int arg1) {
    return delegate.findFigure(arg0, arg1);
  }

  @Override
  public Iterator<Figure> iterator() {
    return delegate.iterator();
  }

  @Override
  public void lock() {
    delegate.lock();
  }

  @Override
  public Figure remove(Figure arg0) {
    return delegate.remove(arg0);
  }

  @Override
  public void unlock() {
    delegate.unlock();
  }
}
