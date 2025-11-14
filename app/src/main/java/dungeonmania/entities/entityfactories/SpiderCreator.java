package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.enemies.Spider;

public class SpiderCreator extends EntityCreator {
    @Override
    public String getType() {
        return "spider";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        double spiderHealth = config.optDouble("spider_health", Spider.DEFAULT_HEALTH);
        double spiderAttack = config.optDouble("spider_attack", Spider.DEFAULT_ATTACK);
        return new Spider(getPosition(json), spiderHealth, spiderAttack);
    }

}
