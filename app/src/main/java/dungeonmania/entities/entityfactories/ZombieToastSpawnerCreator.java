package dungeonmania.entities.entityfactories;

import org.json.JSONObject;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityCreator;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class ZombieToastSpawnerCreator extends EntityCreator {
    @Override
    public String getType() {
        return "zombie_toast_spawner";
    }

    @Override
    public Entity createEntity(JSONObject json, JSONObject config) {
        int zombieSpawnRate = config.optInt("zombie_spawn_interval", ZombieToastSpawner.DEFAULT_SPAWN_INTERVAL);
        return new ZombieToastSpawner(getPosition(json), zombieSpawnRate);
    }

}
