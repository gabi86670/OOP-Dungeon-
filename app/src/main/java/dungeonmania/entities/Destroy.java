package dungeonmania.entities;

import dungeonmania.map.GameMap;

public interface Destroy {
    /** Called when this entity is destroyed (ie removed from the game map). */
    void onDestroy(GameMap gameMap);
}
