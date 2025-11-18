package dungeonmania.entities.inventory;

import org.json.JSONObject;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.Buildable;

public class BowFactory implements CraftingFactory {
    private final int bowDurability;

    public BowFactory(JSONObject config) {
        this.bowDurability = config.optInt("bow_durability");
    }

    @Override
    public Buildable craft(Player player) {
        CraftingItems items = new CraftingItems(player);
        if (!canCraft(player)) {
            throw new IllegalStateException("Inefficient materials - cannot craft bow");
        }
        items.removeWood(1);
        items.removeArrows(3);

        return new Bow(bowDurability);
    }

    @Override
    public boolean canCraft(Player player) {
        CraftingItems items = new CraftingItems(player);
        return (items.countWood() >= 1 && items.countArrows() >= 3);
    }
}
