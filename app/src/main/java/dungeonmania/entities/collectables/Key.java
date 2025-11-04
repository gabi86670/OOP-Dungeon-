package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Key extends InventoryItem {
    private int number;

    public Key(Position position, int number) {
        super(position);
        this.number = number;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player player) {
            if (player.getInventory().count(Key.class) >= 1) {
                return;
            } else if (!player.pickUp(this)) {
                return;
            }
            map.destroyEntity(this);
        }
    }

    public int getnumber() {
        return number;
    }
}
