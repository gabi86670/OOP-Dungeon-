package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.Wall;

public class WallCreator extends EntityCreator {
    @Override
    public String getType() {
        return "wall";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        return new Wall(getPosition(json));

    }
}
