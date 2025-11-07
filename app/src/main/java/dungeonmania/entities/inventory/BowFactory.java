package dungeonmania.entities.inventory;

import org.json.JSONObject;

import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.Buildable;

public class BowFactory implements CraftingFactory {
    private final int bowDurability;

    public BowFactory(JSONObject config) {
        this.bowDurability = config.optInt("bow_durability");
    }

    @Override
    public Buildable craft() {
        return new Bow(bowDurability);
    }

}
