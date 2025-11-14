package dungeonmania.entities.entityfactories;

import org.json.JSONObject;

import dungeonmania.entities.ColorCodedType;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.Portal;

public class PortalCreator extends EntityCreator {
    @Override
    public String getType() {
        return "portal";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        return new Portal(getPosition(json), ColorCodedType.valueOf(json.getString("colour")));
    }
}
