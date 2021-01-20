package hotciv.standard;

import hotciv.framework.GameConstants;
import hotciv.framework.Tile;

import java.util.UUID;

public class TileImpl implements Tile {
    public TileImpl(String type) {
        this.type = type;
        objectId = UUID.randomUUID().toString();
    }

    private String type;
    private String objectId;

    @Override
    public String getTypeString() {
        return type;
    }

    @Override
    public String getID() {
        return objectId;
    }
}
