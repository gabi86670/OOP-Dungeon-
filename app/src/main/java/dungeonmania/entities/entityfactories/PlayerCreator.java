package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.Player;

public class PlayerCreator extends EntityCreator {
    @Override
    public String getType() {
        return "player";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        double playerHealth = config.optDouble("player_health", Player.DEFAULT_HEALTH);
        double playerAttack = config.optDouble("player_attack", Player.DEFAULT_ATTACK);
        return new Player(getPosition(json), playerHealth, playerAttack);
    }
}
