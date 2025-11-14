package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.collectables.Bomb;

public class BombCreator extends EntityCreator {
    @Override
    public String getType() {
        return "bomb";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        int bombRadius = config.optInt("bomb_radius", Bomb.DEFAULT_RADIUS);
        return new Bomb(getPosition(json), bombRadius);

    }
}
