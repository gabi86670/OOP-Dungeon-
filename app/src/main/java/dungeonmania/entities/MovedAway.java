package dungeonmania.entities;

import dungeonmania.map.GameMap;

public interface MovedAway {
    /** Called whenever another entity moves away from this entity's position */
    void onMovedAway(GameMap map, Entity entity);
}
