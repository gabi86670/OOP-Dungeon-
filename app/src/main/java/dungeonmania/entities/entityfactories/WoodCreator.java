package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.collectables.Wood;

public class WoodCreator extends EntityCreator {
    @Override
    public String getType() {
        return "wood";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        return new Wood(getPosition(json));

    }
}
