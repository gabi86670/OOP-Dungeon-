package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.collectables.Key;

public class KeyCreator extends EntityCreator {
    @Override
    public String getType() {
        return "key";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        return new Key(getPosition(json), json.getInt("key"));
    }
}
