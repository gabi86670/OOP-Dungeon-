package dungeonmania.entities.inventory;

import org.json.JSONObject;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Buildable;

public class ShieldFactory implements CraftingFactory {
    private final int shieldDurability;
    private final double defence;

    public ShieldFactory(JSONObject config) {
        this.shieldDurability = config.optInt("shield_durability");
        this.defence = config.optInt("shield_defence");
    }

    @Override
    public Buildable craft(Player player) {
        CraftingItems items = new CraftingItems(player);
        if (!canCraft(player)) {
            throw new IllegalStateException("Inefficient materials - cannot craft shield");
        }
        items.removeWood(2);

        if (items.countTreasure() >= 1) {
            items.removeNormalTreasure(1);
        } else if (items.countKeys() >= 1) {
            items.removeKey(1);
        }

        return new Shield(shieldDurability, defence);
    }

    @Override
    public boolean canCraft(Player player) {
        CraftingItems items = new CraftingItems(player);
        return (items.countWood() >= 2
                && (items.countTreasure() >= 1 || items.countKeys() >= 1 || items.countSunStones() >= 1));
    }
}
