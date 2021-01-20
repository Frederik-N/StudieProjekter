package hotciv.stub;

import hotciv.framework.Player;
import hotciv.framework.Unit;

import java.util.UUID;

public class StubUnit implements Unit {
  private String type;
  private Player owner;
  private String objectID;

  public StubUnit(String type, Player owner) {
    this.type = type;
    this.owner = owner;
    this.objectID = UUID.randomUUID().toString();
  }
  public String getTypeString() { return type; }
  public Player getOwner() { return owner; }
  public int getMoveCount() { return 1; }
  public int getDefensiveStrength() { return 0; }
  public int getAttackingStrength() { return 0; }

  @Override
  public String getID() {
    return objectID;
  }
}
