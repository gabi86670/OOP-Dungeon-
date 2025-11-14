package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.Switch;

public class SwitchCreator extends EntityCreator {
    @Override
    public String getType() {
        return "switch";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        return new Switch(getPosition(json));

    }
}
