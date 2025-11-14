package dungeonmania.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.Portal;
import dungeonmania.entities.PotionListener;
import dungeonmania.entities.Switch;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.util.Position;

public class MapInitialiser {
    private final Game game;
    private final GameMap map;
    private final Player player;
    /** The tiles of the map */
    private Map<Position, MapTile> nodes = new HashMap<>();

    public MapInitialiser(Game game, GameMap map, Player player, Map<Position, MapTile> nodes) {
        this.game = game;
        this.map = map;
        this.player = player;
        this.nodes = nodes;
    }

    /**
     * Initialise the game map
     * 1. pair up portals
     * 2. register all movables
     * 3. register all spawners
     * 4. register bombs and switches
     * 5. more...
     */
    public void init() {
        initPairPortals();
        initRegisterMovables();
        initRegisterSpawners();
        initRegisterBombsAndSwitches();
        initPotionListeners();
    }

    // gets all entities on the map
    public List<Entity> getEntities() {
        return nodes.values().stream().flatMap(tile -> tile.getEntities().stream()).toList();
    }

    // gets all entities of a specified type
    public <T extends Entity> List<T> getEntities(Class<T> type) {
        return getEntities().stream().filter(type::isInstance).map(type::cast).toList();
    }

    /** Subscribe bombs and switches to each other */
    private void initRegisterBombsAndSwitches() {
        List<Bomb> bombs = getEntities(Bomb.class);
        List<Switch> switchs = getEntities(Switch.class);
        for (Bomb b : bombs) {
            for (Switch s : switchs) {
                if (Position.isAdjacent(b.getPosition(), s.getPosition())) {
                    s.subscribe(b, map);
                }
            }
        }
    }

    /** Pair up portals if there's any */
    private void initPairPortals() {
        Map<String, Portal> portalsMap = new HashMap<>();
        List<Portal> portals = map.getEntities(Portal.class);
        nodes.forEach((k, v) -> {
            v.getEntities().stream().filter(Portal.class::isInstance).map(Portal.class::cast).forEach(portal -> {
                String color = portal.getColor();
                if (portalsMap.containsKey(color)) {
                    portal.bind(portalsMap.get(color));
                } else {
                    portalsMap.put(color, portal);
                }
            });
        });
    }

    /** Register each enemy to move on each tick. */
    private void initRegisterMovables() {
        List<Enemy> enemies = getEntities(Enemy.class);
        enemies.forEach(e -> {
            game.register(() -> e.move(game), Game.AI_MOVEMENT, e.getId());
        });
    }

    /**
     * Register each zombie toast spawner to attempt to spawn an enemy each tick as well as initialise the spider
     * spawning mechanic.
     */
    private void initRegisterSpawners() {
        List<ZombieToastSpawner> zts = getEntities(ZombieToastSpawner.class);
        zts.forEach(e -> {
            game.register(() -> e.spawn(game), Game.AI_MOVEMENT, e.getId());
        });
        game.register(() -> game.getEntityFactory().spawnSpider(game), Game.AI_MOVEMENT, "spawnSpiders");
    }

    /** Initialise and register "potion listeners" to be responsive to player potion updates */
    private void initPotionListeners() {
        getEntities().stream().filter(PotionListener.class::isInstance).map(PotionListener.class::cast)
                .forEach(this::registerPotionListener);
    }

    /** Register a potion listener on the player */
    public void registerPotionListener(PotionListener e) {
        player.registerPotionListener(e);
    }

}
