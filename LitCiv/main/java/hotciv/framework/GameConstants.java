package hotciv.framework;

/** Collection of constants used in HotCiv Game. Note that strings are
 * used instead of enumeration types to keep the set of valid
 * constants open to extensions by future HotCiv variants.  Enums can
 * only be changed by compile time modification.

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
public class GameConstants {
  // The size of the world is set permanently to a 16x16 grid 
  public static final int WORLDSIZE = 16;
  // Valid unit types
  public static final String ARCHER    = "archer";
  public static final String LEGION    = "legion";
  public static final String SETTLER   = "settler";
  // Valid terrain types
  public static final String PLAINS    = "plains";
  public static final String OCEANS    = "ocean";
  public static final String FOREST    = "forest";
  public static final String HILLS     = "hills";
  public static final String MOUNTAINS = "mountain";
  // Valid production balance types
  public static final String productionFocus = "hammer";
  public static final String foodFocus = "apple";
  // Price of units
  public static final int ARCHERCOST = 10;
  public static final int LEGIONCOST = 15;
  public static final int SETTLERCOST = 30;
  //Attack and defense units
  public static final int ARCHERDEF = 3;
  public static final int ARCHERATK = 2;
  public static final int LEGIONDEF = 2;
  public static final int LEGIONATK = 4;
  public static final int SETTLERDEF = 3;
  public static final int SETTLERATK = 0;
  // Moves units
  public static final int ARCHERMOV = 1;
  public static final int LEGIONMOV = 1;
  public static final int SETTLERMOV = 1;
  // Theta
  public static final String BOMB = "bomb";
  public static final int BOMBATK = 0;
  public static final int BOMBDEF = 1;
  public static final int BOMBMOV = 2;
  public static final int BOMBCOST = 60;
}
