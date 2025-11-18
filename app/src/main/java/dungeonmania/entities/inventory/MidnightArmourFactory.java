package dungeonmania.entities.inventory;

import org.json.JSONObject;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Buildable;
import dungeonmania.entities.buildables.MidnightArmour;

public class MidnightArmourFactory implements CraftingFactory {
    private final double armourDefence;
    private final double armourAttack;

    public MidnightArmourFactory(JSONObject config) {
        this.armourDefence = config.optInt("midnight_armour_defence");
        this.armourAttack = config.optInt("midnight_armour_attack");
    }

    @Override
    public Buildable craft(Player player) {
        CraftingItems items = new CraftingItems(player);
        if (!canCraft(player)) {
            throw new IllegalStateException("Inefficient materials - cannot craft bow");
        }
        items.removeSunStone(1);
        items.removeSword();

        return new MidnightArmour(armourDefence, armourAttack);
    }

    @Override
    public boolean canCraft(Player player) {
        CraftingItems items = new CraftingItems(player);

        return (items.countSunStones() >= 1 && items.hasSword());
    }
}
