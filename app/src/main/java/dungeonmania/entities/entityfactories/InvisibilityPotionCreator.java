package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;

public class InvisibilityPotionCreator extends EntityCreator {
    @Override
    public String getType() {
        return "invisibility_potion";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        int invisibilityPotionDuration = config.optInt("invisibility_potion_duration",
                InvisibilityPotion.DEFAULT_DURATION);
        return new InvisibilityPotion(getPosition(json), invisibilityPotionDuration);
    }
}
