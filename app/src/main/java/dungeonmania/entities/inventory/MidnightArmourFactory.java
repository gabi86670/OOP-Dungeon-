package dungeonmania.entities.inventory;

import org.json.JSONObject;
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
    public Buildable craft() {
        return new MidnightArmour(armourDefence, armourAttack);
    }
}
