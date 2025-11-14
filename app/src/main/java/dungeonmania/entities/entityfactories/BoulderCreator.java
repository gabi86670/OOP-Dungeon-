package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;

public class BoulderCreator extends EntityCreator {
    @Override
    public String getType() {
        return "boulder";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        return new Boulder(getPosition(json));

    }
}
