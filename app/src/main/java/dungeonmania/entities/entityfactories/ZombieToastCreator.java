package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.enemies.ZombieToast;

public class ZombieToastCreator extends EntityCreator {
    @Override
    public String getType() {
        return "zombie_toast";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        double zombieHealth = config.optDouble("zombie_health", ZombieToast.DEFAULT_HEALTH);
        double zombieAttack = config.optDouble("zombie_attack", ZombieToast.DEFAULT_ATTACK);
        return new ZombieToast(getPosition(json), zombieHealth, zombieAttack);
    }

}
