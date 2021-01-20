package hotciv.stub;

import hotciv.framework.City;
import hotciv.framework.Player;

import java.util.UUID;

public class StubCity implements City {
  private String objectID;
  private int treasury;
  private String workforce;
  private String production;
  private Player p;

  public StubCity(Player p, int treasury, String production, String workforce) {
    this.p = p;
    this.production = production;
    this.workforce = workforce;
    this.treasury = treasury;
    objectID = UUID.randomUUID().toString();

  }
  @Override
  public Player getOwner() {
    return p;
  }

  @Override
  public int getSize() {
    return 1;
  }

  @Override
  public int getTreasury() {
    return treasury;
  }

  @Override
  public String getProduction() {
    return production;
  }

  @Override
  public String getWorkforceFocus() {
    return workforce;
  }

  @Override
  public String getID() {
    return objectID;
  }
}
