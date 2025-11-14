package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.Exit;

public class ExitCreator extends EntityCreator {
    @Override
    public String getType() {
        return "exit";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        return new Exit(getPosition(json));

    }
}
