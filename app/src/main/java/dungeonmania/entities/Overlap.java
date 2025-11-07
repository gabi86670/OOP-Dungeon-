package dungeonmania.entities;

import dungeonmania.map.GameMap;

public interface Overlap {
    /**
     * Called whenever another entity overlaps with this entity's position. Useful for triggering battles and item
     * collection.
     */
    void onOverlap(GameMap map, Entity entity);
}
