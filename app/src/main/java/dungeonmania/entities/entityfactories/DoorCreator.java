package dungeonmania.entities.entityfactories;

import org.json.JSONObject;

import dungeonmania.entities.Door;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;

public class DoorCreator extends EntityCreator {
    @Override
    public String getType() {
        return "door";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        return new Door(getPosition(json), json.getInt("key"));
    }
}
