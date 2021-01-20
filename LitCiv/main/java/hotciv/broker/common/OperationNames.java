package hotciv.broker.common;

public class OperationNames {
    public static final String CITY_PREFIX = "city";
    public static final String UNIT_PREFIX = "unit";
    public static final String TILE_PREFIX = "tile";
    public static final String GAME_PREFIX = "game";

    /*** Game ***/
    public static final String GET_WINNER_OPERATION = GAME_PREFIX + "-get-winner";
    public static final String GET_AGE_OPERATION = GAME_PREFIX + "-get-age";
    public static final String GET_UNIT_AT_OPERATION = GAME_PREFIX + "-get-unit-at";
    public static final String GET_CITY_AT_OPERATION = GAME_PREFIX + "-get-city-at";
    public static final String GET_TILE_AT_OPERATION = GAME_PREFIX + "-get-tile-at";
    public static final String GET_PLAYER_IN_TURN_OPERATION = GAME_PREFIX + "-get-player-in-turn";
    public static final String MOVE_UNIT_OPERATION = GAME_PREFIX + "-move-unit";
    public static final String END_OF_TURN_OPERATION = GAME_PREFIX + "-end-of-turn";
    public static final String PERFORM_UNIT_ACTION_AT = GAME_PREFIX + "-perform-unit-action-at";
    public static final String CHANGE_PROD_IN_CITY_AT = GAME_PREFIX + "-change-prod-in-city-at";
    public static final String CHANGE_WORK_IN_CITY_AT = GAME_PREFIX + "-change-work-in-city-at";

    /*** City ***/
    public static final String CITY_GET_OWNER = CITY_PREFIX + "-get-owner";
    public static final String CITY_GET_SIZE = CITY_PREFIX +"-get-size";
    public static final String CITY_GET_TREASURY = CITY_PREFIX +"-get-treasury";
    public static final String CITY_GET_PRODUCTION = CITY_PREFIX +"-get-production";
    public static final String CITY_GET_WORKFORCE = CITY_PREFIX +"-get-workforce";

    /*** Tile ***/
    public static final String TILE_GET_TYPE = TILE_PREFIX + "-get-type";

    /*** Unit ***/
    public static final String UNIT_GET_OWNER = UNIT_PREFIX + "-get-owner";
    public static final String UNIT_GET_TYPE = UNIT_PREFIX +"-get-type";
    public static final String UNIT_GET_MOVECOUNT = UNIT_PREFIX +"-get-movecount";
    public static final String UNIT_GET_ATTACKINGSTRENGTH = UNIT_PREFIX +"-get-attackingstrength";
    public static final String UNIT_GET_DEFENSIVESTRENGTH = UNIT_PREFIX +"-get-defensivestrength";
}
