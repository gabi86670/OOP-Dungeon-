package dungeonmania.entities.inventory;

import org.json.JSONObject;

import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.buildables.Buildable;

public class ShieldFactory implements CraftingFactory {
    private final int shieldDurability;
    private final double defence;

    public ShieldFactory(JSONObject config) {
        this.shieldDurability = config.optInt("shield_durability");
        this.defence = config.optInt("shield_defence");
    }

    @Override
    public Buildable craft() {
        return new Shield(shieldDurability, defence);
    }
}
