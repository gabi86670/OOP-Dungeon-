package dungeonmania.entities.inventory;

import org.json.JSONObject;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Buildable;
import dungeonmania.entities.buildables.Sceptre;

public class SceptreFactory implements CraftingFactory {
    private final int sceptreDurability;

    public SceptreFactory(JSONObject config) {
        this.sceptreDurability = config.optInt("mind_control_duration");
    }

    @Override
    public Buildable craft(Player player) {
        CraftingItems items = new CraftingItems(player);
        if (!canCraft(player)) {
            throw new IllegalStateException("Inefficient materials - cannot craft shield");
        }

        if (items.countWood() >= 1) {
            items.removeWood(1);
        } else if (items.countArrows() >= 2) {
            items.removeArrows(2);
        }

        if (items.countKeys() >= 1) {
            items.removeKey(1);
        } else if (items.countTreasure() >= 1) {
            items.removeNormalTreasure(1);
        }

        items.removeSunStone(1);
        return new Sceptre(sceptreDurability);
    }

    @Override
    public boolean canCraft(Player player) {
        CraftingItems items = new CraftingItems(player);

        return ((items.countWood() >= 1 || items.countArrows() >= 2)
                && (items.countKeys() >= 1 || items.countTreasure() >= 1) && items.countSunStones() >= 1);
    }
}
