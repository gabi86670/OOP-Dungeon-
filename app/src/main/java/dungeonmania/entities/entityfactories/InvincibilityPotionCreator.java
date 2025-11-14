package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;

public class InvincibilityPotionCreator extends EntityCreator {
    @Override
    public String getType() {
        return "invincibility_potion";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        int invincibilityPotionDuration = config.optInt("invincibility_potion_duration",
                InvincibilityPotion.DEFAULT_DURATION);
        return new InvincibilityPotion(getPosition(json), invincibilityPotionDuration);
    }
}
