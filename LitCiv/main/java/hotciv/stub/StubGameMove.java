package hotciv.stub;

import hotciv.framework.*;

import java.util.HashMap;
import java.util.Map;

/** Test stub for game for visual testing of
 * minidraw based graphics.
 *
 * SWEA support code.
 *
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

public class StubGameMove implements Game {

  // === Unit handling ===
  private Position pos_archer_red;
  private Position pos_legion_blue;
  private Position pos_settler_red;
  private Position pos_bomb_red;
  private Position pos_city_red;

  private Unit red_archer, blue_legion, settler_red, bomb_red;
  private City red_city;

  public Unit getUnitAt(Position p) {
    if (p.equals(pos_archer_red)) {
      return red_archer;
    }
    if (p.equals(pos_settler_red)) {
      return settler_red;
    }
    if (p.equals(pos_legion_blue)) {
      return blue_legion;
    }
    if (p.equals(pos_bomb_red)) {
      return bomb_red;
    }
    return null;
  }

  // Stub only allows moving red archer
  public boolean moveUnit(Position from, Position to) {
    System.out.println("-- StubGame2 / moveUnit called: " + from + "->" + to);
    if (from.equals(pos_archer_red)) {
      pos_archer_red = to;
    }
    if (from.equals(pos_legion_blue)) {
      pos_legion_blue = to;
    }
    if (from.equals(pos_settler_red)) {
      pos_settler_red = to;
    }
    if (from.equals(pos_bomb_red)) {
      pos_bomb_red = to;
    }
    // notify our observer(s) about the changes on the tiles
    gameObserver.worldChangedAt(from);
    gameObserver.worldChangedAt(to);
    return true;
  }

  // === Turn handling ===
  private Player inTurn;

  public void endOfTurn() {
    System.out.println("-- StubGame2 / endOfTurn called.");
    inTurn = (getPlayerInTurn() == Player.RED ?
            Player.BLUE :
            Player.RED);
    // no age increments
    gameObserver.turnEnds(inTurn, -4000);
  }

  public Player getPlayerInTurn() {
    return inTurn;
  }


  // === Observer handling ===
  protected GameObserver gameObserver;

  // observer list is only a single one...
  public void addObserver(GameObserver observer) {
    gameObserver = observer;
  }

  public StubGameMove() {
    defineWorld(1);
    // AlphaCiv configuration
    pos_archer_red = new Position(2, 0);
    pos_legion_blue = new Position(3, 2);
    pos_settler_red = new Position(4, 3);
    pos_bomb_red = new Position(6, 4);
    pos_city_red = new Position(5, 5);

    // the only one I need to store for this stub
    red_archer = new StubUnit(GameConstants.ARCHER, Player.RED);
    blue_legion = new StubUnit(GameConstants.LEGION, Player.BLUE);
    settler_red = new StubUnit(GameConstants.SETTLER, Player.RED);
    bomb_red = new StubUnit(GameConstants.BOMB, Player.RED);

    red_city = new StubCity(Player.RED, 0, GameConstants.ARCHER, GameConstants.productionFocus);

    inTurn = Player.RED;
  }

  // A simple implementation to draw the map of DeltaCiv
  protected Map<Position, Tile> world;

  public Tile getTileAt(Position p) {
    return world.get(p);
  }


  /**
   * define the world.
   *
   * @param worldType 1 gives one layout while all other
   *                  values provide a second world layout.
   */
  protected void defineWorld(int worldType) {
    world = new HashMap<Position, Tile>();
    for (int r = 0; r < GameConstants.WORLDSIZE; r++) {
      for (int c = 0; c < GameConstants.WORLDSIZE; c++) {
        Position p = new Position(r, c);
        world.put(p, new StubTile(GameConstants.PLAINS));
      }
    }
  }

  public City getCityAt(Position p) {
    if (p.equals(pos_city_red)) {
      return red_city;
    }
    return null;
  }

  public void changeWorkForceFocusInCityAt(Position p, String balance) {
    red_city = new StubCity(getCityAt(p).getOwner(), getCityAt(p).getTreasury(), getCityAt(p).getProduction(), balance);
  }

  public void changeProductionInCityAt(Position p, String unitType) {
    red_city = new StubCity(getCityAt(p).getOwner(), getCityAt(p).getTreasury(), unitType, getCityAt(p).getWorkforceFocus());
  }

  // TODO: Add more stub behaviour to test MiniDraw updating
  public Player getWinner() {
    return null;
  }

  public int getAge() {
    return -4000;
  }

  public void performUnitActionAt(Position p) {
  }

  public void setTileFocus(Position position) {
    // TODO: setTileFocus implementation pending.
    System.out.println("-- StubGame2 / setTileFocus called.");
    gameObserver.tileFocusChangedAt(position);
  }

  @Override
  public String getID() {
    return null;
  }
}
