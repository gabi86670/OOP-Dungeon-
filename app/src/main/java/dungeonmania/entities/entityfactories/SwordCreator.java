package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.collectables.Sword;

public class SwordCreator extends EntityCreator {
    @Override
    public String getType() {
        return "sword";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        double swordAttack = config.optDouble("sword_attack", Sword.DEFAULT_ATTACK);
        int swordDurability = config.optInt("sword_durability", Sword.DEFAULT_DURABILITY);
        return new Sword(getPosition(json), swordAttack, swordDurability);
    }
}
