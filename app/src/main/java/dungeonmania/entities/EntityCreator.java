package dungeonmania.entities;

import org.json.JSONObject;

import dungeonmania.util.Position;

public abstract class EntityCreator {
    public abstract String getType();

    public abstract Entity createEntity(JSONObject json, JSONObject config);

    protected Position getPosition(JSONObject json) {
        return new Position(json.getInt("x"), json.getInt("y"));
    }
}
