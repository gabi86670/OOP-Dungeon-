package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.collectables.Treasure;

public class TreasureCreator extends EntityCreator {
    @Override
    public String getType() {
        return "treasure";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        return new Treasure(getPosition(json));

    }
}
