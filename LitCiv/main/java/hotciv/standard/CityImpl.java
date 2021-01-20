package hotciv.standard;

import hotciv.framework.City;
import hotciv.framework.Player;

import java.util.UUID;

public class CityImpl implements City {
    private String objectId;

    public CityImpl(Player p, int treasury, String production, String workforce) {
        this.p = p;
        this.production = production;
        this.workforce = workforce;
        this.treasury = treasury;
        objectId = UUID.randomUUID().toString();
    }

    private Player p;
    private int treasury;
    private String production;
    private String workforce;
    private int size = 1;

    @Override
    public Player getOwner() {
        return p;
    }

    @Override
    public int getSize() {
        return size;
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
        return objectId;
    }

    public void addToTreasury(int temp) {
        treasury += temp;
    }

    public void setProduction(String temp) {
        production = temp;
    }

    public void setWorkforceFocus(String temp) {
        workforce = temp;
    }

    public void changeOwner(Player currentPlayer) { p = currentPlayer; }

    public void decrementSize() { size--; }
}
