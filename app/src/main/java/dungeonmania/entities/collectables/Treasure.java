package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Treasure extends InventoryItem {
    public Treasure(Position position) {
        super(position);
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player player) {
            if (!player.pickUp(this))
                return;
            map.destroyEntity(this);
        }
    }
}
