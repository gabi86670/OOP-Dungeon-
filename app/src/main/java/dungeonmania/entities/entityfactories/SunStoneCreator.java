package dungeonmania.entities.entityfactories;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.collectables.SunStone;

public class SunStoneCreator extends EntityCreator {
    @Override
    public String getType() {
        return "sun_stone";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        return new SunStone(getPosition(json));
    }

}
