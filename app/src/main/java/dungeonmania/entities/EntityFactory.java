package dungeonmania.entities;

import dungeonmania.Game;
import dungeonmania.entities.enemies.*;
import dungeonmania.entities.entityfactories.ArrowCreator;
import dungeonmania.entities.entityfactories.BombCreator;
import dungeonmania.entities.entityfactories.BoulderCreator;
import dungeonmania.entities.entityfactories.DoorCreator;
import dungeonmania.entities.entityfactories.ExitCreator;
import dungeonmania.entities.entityfactories.InvincibilityPotionCreator;
import dungeonmania.entities.entityfactories.InvisibilityPotionCreator;
import dungeonmania.entities.entityfactories.KeyCreator;
import dungeonmania.entities.entityfactories.MercenaryCreator;
import dungeonmania.entities.entityfactories.PlayerCreator;
import dungeonmania.entities.entityfactories.PortalCreator;
import dungeonmania.entities.entityfactories.SpiderCreator;
import dungeonmania.entities.entityfactories.SwitchCreator;
import dungeonmania.entities.entityfactories.SwordCreator;
import dungeonmania.entities.entityfactories.TreasureCreator;
import dungeonmania.entities.entityfactories.WallCreator;
import dungeonmania.entities.entityfactories.WoodCreator;
import dungeonmania.entities.entityfactories.ZombieToastCreator;
import dungeonmania.entities.entityfactories.ZombieToastSpawnerCreator;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

public class EntityFactory {
    private JSONObject config;
    private Random ranGen = new Random();
    private Map<String, EntityCreator> creators = new HashMap<>();

    public EntityFactory(JSONObject config) {
        this.config = config;

        // registers all the creators
        registerCreator(new PlayerCreator());
        registerCreator(new SpiderCreator());
        registerCreator(new MercenaryCreator());
        registerCreator(new SwitchCreator());
        registerCreator(new DoorCreator());
        registerCreator(new PortalCreator());
        registerCreator(new ZombieToastCreator());
        registerCreator(new ZombieToastSpawnerCreator());
        registerCreator(new WoodCreator());
        registerCreator(new InvisibilityPotionCreator());
        registerCreator(new InvincibilityPotionCreator());
        registerCreator(new BombCreator());
        registerCreator(new TreasureCreator());
        registerCreator(new KeyCreator());
        registerCreator(new SwordCreator());
        registerCreator(new BoulderCreator());
        registerCreator(new ExitCreator());
        registerCreator(new ArrowCreator());
        registerCreator(new WallCreator());
    }

    private void registerCreator(EntityCreator creator) {
        creators.put(creator.getType(), creator);
    }

    public Entity createEntity(JSONObject jsonEntity) {
        String type = jsonEntity.getString("type");
        if (!creators.containsKey(type)) {
            throw new IllegalArgumentException(
                    String.format("Failed to recognise '%s' entity in EntityFactory", jsonEntity.getString("type")));
        }
        return creators.get(type).createEntity(jsonEntity, config);

    }

    public void spawnSpider(Game game) {
        GameMap map = game.getMap();
        int tick = game.getTick();
        int rate = config.optInt("spider_spawn_interval", 0);
        if (rate == 0 || (tick + 1) % rate != 0)
            return;
        int radius = 20;
        Position player = map.getPlayer().getPosition();

        SpiderCreator creator = new SpiderCreator();
        JSONObject dummyJson = new JSONObject().put("x", 0).put("y", 0);
        Spider dummySpider = (Spider) creator.createEntity(dummyJson, config); // for checking possible positions

        List<Position> availablePos = new ArrayList<>();
        for (int i = player.getX() - radius; i < player.getX() + radius; i++) {
            for (int j = player.getY() - radius; j < player.getY() + radius; j++) {
                if (Position.calculatePositionBetween(player, new Position(i, j)).magnitude() > radius)
                    continue;
                Position np = new Position(i, j);
                if (!map.canMoveTo(dummySpider, np) || np.equals(player))
                    continue;
                if (map.getEntities(np).stream().anyMatch(Enemy.class::isInstance))
                    continue;
                availablePos.add(np);
            }
        }
        Position initPosition = availablePos.get(ranGen.nextInt(availablePos.size()));
        JSONObject realJson = new JSONObject().put("x", initPosition.getX()).put("y", initPosition.getY());

        Spider spider = (Spider) creator.createEntity(realJson, config);
        map.addEntity(spider);
        game.register(() -> spider.move(game), Game.AI_MOVEMENT, spider.getId());
    }

    public void spawnZombie(Game game, ZombieToastSpawner spawner) {
        GameMap map = game.getMap();
        int tick = game.getTick();
        Random randGen = new Random();
        int spawnInterval = config.optInt("zombie_spawn_interval", ZombieToastSpawner.DEFAULT_SPAWN_INTERVAL);
        if (spawnInterval == 0 || (tick + 1) % spawnInterval != 0)
            return;
        List<Position> pos = spawner.getPosition().getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> map.getEntities(p).stream().noneMatch(Wall.class::isInstance)).toList();
        if (pos.isEmpty())
            return;
        ZombieToastCreator creator = new ZombieToastCreator();
        Position spawnPos = pos.get(randGen.nextInt(pos.size()));
        JSONObject json = new JSONObject().put("x", spawnPos.getX()).put("y", spawnPos.getY());
        ZombieToast zt = (ZombieToast) creator.createEntity(json, config);
        map.addEntity(zt);
        map.registerPotionListener(zt);
        game.register(() -> zt.move(game), Game.AI_MOVEMENT, zt.getId());
    }
}
