package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.collectables.Arrow;

public class ArrowCreator extends EntityCreator {
    @Override
    public String getType() {
        return "arrow";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        return new Arrow(getPosition(json));

    }
}
