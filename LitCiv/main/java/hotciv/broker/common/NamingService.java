package hotciv.broker.common;

import hotciv.framework.*;

public interface NamingService {
    void putTile(String objectId, Tile tile);

    Tile getTile(String objectId);

    void putCity(String objectId, City city);

    City getCity(String objectId);

    void putUnit(String objectId, Unit unit);

    Unit getUnit(String objectId);

    Game getGame(String objectId);

    void putGame(String objectId, Game game);
}
