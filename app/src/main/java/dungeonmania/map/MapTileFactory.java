package dungeonmania.map;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import dungeonmania.entities.EntityFactory;

/**
 * Factory that creates a MapTile from a JSON Object
 */
public class MapTileFactory {
    private static final List<String> VALID_ENTITIES = Arrays.asList("player", "zombie_toast", "zombie_toast_spawner",
            "mercenary", "wall", "boulder", "switch", "exit", "treasure", "wood", "arrow", "bomb",
            "invisibility_potion", "invincibility_potion", "portal", "sword", "spider", "door", "key", "sun_stone");

    public static MapTile createEntity(JSONObject jsonEntity, EntityFactory factory) {
        return constructEntity(jsonEntity, factory);
    }

    private static MapTile constructEntity(JSONObject jsonEntity, EntityFactory factory) {
        if (VALID_ENTITIES.contains(jsonEntity.getString("type"))) {
            return new MapTile(factory.createEntity(jsonEntity));
        } else {
            throw new IllegalArgumentException(
                    String.format("Failed to recognise '%s' entity in MapTileFactory", jsonEntity.getString("type")));
        }
    }
}
