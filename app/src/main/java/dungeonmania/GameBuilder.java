package dungeonmania;

import java.io.IOException;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.BowFactory;
import dungeonmania.entities.inventory.Recipe;
import dungeonmania.entities.inventory.SceptreFactory;
import dungeonmania.entities.inventory.ShieldFactory;
import dungeonmania.goals.Goal;
import dungeonmania.goals.GoalFactory;
import dungeonmania.map.GameMap;
import dungeonmania.map.MapTile;
import dungeonmania.map.MapTileFactory;
import dungeonmania.util.FileLoader;

/**
 * GameBuilder -- A builder to build up the whole game
 * @author      Webster Zhang
 * @author      Tina Ji
 */
public class GameBuilder {
    private String configName;
    private String dungeonName;

    private JSONObject config;
    private JSONObject dungeon;

    /** Set the name of the configuration file to use for the game */
    public GameBuilder setConfigName(String configName) {
        this.configName = configName;
        return this;
    }

    /** Set the name of the dungeon file to use for the game */
    public GameBuilder setDungeonName(String dungeonName) {
        this.dungeonName = dungeonName;
        return this;
    }

    public Game buildGame() throws IOException {
        config = loadConfig();
        dungeon = loadDungeon();

        Game game = new Game(dungeonName);
        EntityFactory factory = new EntityFactory(config);
        game.setEntityFactory(factory);
        buildMap(game);
        buildGoals(game);
        game.init();
        game.getPlayer()
                .setRecipe(new Recipe(new BowFactory(config), new ShieldFactory(config), new SceptreFactory(config)));

        return game;
    }

    /** Load the configuration from the file system. */
    private JSONObject loadConfig() throws IOException {
        String configFile = String.format("/configs/%s.json", configName);
        try {
            return new JSONObject(FileLoader.loadResourceFile(configFile));
        } catch (IOException e) {
            throw new IOException("Failed to load config file: " + e.getMessage());
        }
    }

    /** Load the dungeon from the file system. */
    private JSONObject loadDungeon() throws IOException {
        String dungeonFile = String.format("/dungeons/%s.json", dungeonName);
        try {
            return new JSONObject(FileLoader.loadResourceFile(dungeonFile));
        } catch (IOException e) {
            throw new IOException("Failed to load dungeon file: " + e.getMessage());
        }
    }

    /** Build the game map, adding every entity from the dungeon's JSON info. */
    private void buildMap(Game game) {
        GameMap map = new GameMap();
        map.setGame(game);

        dungeon.getJSONArray("entities").forEach(e -> {
            JSONObject jsonEntity = (JSONObject) e;
            MapTile newNode = MapTileFactory.createEntity(jsonEntity, game.getEntityFactory());
            Entity entity = newNode.getEntities().get(0);

            if (newNode != null)
                map.addNode(newNode);

            if (entity instanceof Player player)
                map.setPlayer(player);
        });
        game.setMap(map);
    }

    public void buildGoals(Game game) {
        if (!dungeon.isNull("goal-condition")) {
            Goal goal = GoalFactory.createGoal(dungeon.getJSONObject("goal-condition"), config);
            game.setGoals(goal);
        }
    }
}
