package dungeonmania.entities.inventory;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Overlap;
import dungeonmania.entities.collectables.Buff;
import dungeonmania.entities.collectables.Durability;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

/**
 * An item in the inventory
 */
public abstract class InventoryItem extends Entity implements Overlap {
    private Durability durability;
    private Buff buff;

    public InventoryItem(Position position) {
        super(position);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    // @Override
    // public abstract void onOverlap(GameMap map, Entity entity);

    /**
     * Use this inventory item to apply a buff to the player's battle statistics (eg having a sword increases the
     * player's attacking power).
     */
    public BattleStatistics applyBuff(BattleStatistics origin) {
        if (buff != null) {
            return buff.applyBuff(origin);
        } else {
            return origin;
        }
    }

    /** Returns the durability of the item. */
    public int getDurability() {
        if (durability != null) {
            return durability.getDurability();
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public void setDurability(Durability durability) {
        this.durability = durability;
    }

    protected Durability getDurabilityObject() {
        return durability;
    }

    public void setBuff(Buff buff) {
        this.buff = buff;
    }
}
