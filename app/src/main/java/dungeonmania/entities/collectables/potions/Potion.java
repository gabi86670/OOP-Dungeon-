package dungeonmania.entities.collectables.potions;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Durability;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Potion extends InventoryItem {
    private int duration;

    public Potion(Position position, int duration) {
        super(position);
        this.duration = duration;
        setDurability(new Durability(1));
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player player) {
            if (!player.pickUp(this))
                return;
            map.destroyEntity(this);
        }
    }

    public int getDuration() {
        return duration;
    }
}
