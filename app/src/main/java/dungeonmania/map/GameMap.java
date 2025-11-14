package dungeonmania.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.Destroy;
import dungeonmania.entities.Entity;
import dungeonmania.entities.MovedAway;
import dungeonmania.entities.Overlap;
import dungeonmania.entities.Player;
import dungeonmania.entities.Portal;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

/**
 * Class representing the map of the game
 *
 * This is implemented using a HashMap from positions to `MapTile`s, where each MapTile has a list of entities,
 * representing the entities in that position.
 *
 * Also contains helpers for path-finding and stuff.
 */
public class GameMap {
    /** The tiles of the map */
    private Map<Position, MapTile> nodes = new HashMap<>();
    /** Reference to the game object */
    private Game game;
    /** Reference to the game's player. */
    private Player player;

    /** Move an entity to a position */
    public void moveTo(Entity entity, Position position) {
        if (!canMoveTo(entity, position))
            return;

        triggerMovingAwayEvent(entity);
        removeNode(entity);
        entity.setPosition(position);
        addEntity(entity);
        triggerOverlapEvent(entity);
    }

    /** Move an entity in a given direction */
    public void moveTo(Entity entity, Direction direction) {
        if (!canMoveTo(entity, Position.translateBy(entity.getPosition(), direction)))
            return;
        triggerMovingAwayEvent(entity);
        removeNode(entity);
        entity.setPosition(Position.translateBy(entity.getPosition(), direction));

        addEntity(entity);
        triggerOverlapEvent(entity);
    }

    /*
     * Calls onMovedAway for each entity on a tile, given an entity that has just moved away from it.
     * Notably, it calls the onMovedAway method of all entities being moved away from,
     * not the entity that is doing the moving
     */
    private void triggerMovingAwayEvent(Entity entity) {
        List<Runnable> callbacks = new ArrayList<>();
        getEntities(entity.getPosition()).forEach(e -> {
            if (e != entity && e instanceof MovedAway movedAway)
                callbacks.add(() -> movedAway.onMovedAway(this, entity));
        });
        callbacks.forEach(callback -> {
            callback.run();
        });
    }

    /*
     * Calls onOverlap for each entity on a tile, given an entity overlapping them.
     * Notably, it calls the onOverlap method of all entities being overlapped onto
     * not the entity that is doing the overlapping
     */
    private void triggerOverlapEvent(Entity entity) {
        List<Runnable> overlapCallbacks = new ArrayList<>();
        getEntities(entity.getPosition()).forEach(e -> {
            if (e != entity && e instanceof Overlap overlap)
                overlapCallbacks.add(() -> overlap.onOverlap(this, entity));
        });
        overlapCallbacks.forEach(callback -> {
            callback.run();
        });
    }

    /** Return whether the given entity can move to the given position */
    public boolean canMoveTo(Entity entity, Position position) {
        return !nodes.containsKey(position) || nodes.get(position).canMoveOnto(this, entity);
    }

    /**
     * Use Dijkstra's algorithm to path-find from `src` to `dest` for `entity`.
     *
     * The algorithm has been adapted to gracefully handle portals.
     *
     * @param src start position
     * @param dest target position
     * @param entity entity performing the movement (used to check whether tiles can be moved to and the like)
     */
    public Position dijkstraPathFind(Position src, Position dest, Entity entity) {
        // if inputs are invalid, don't move
        if (!nodes.containsKey(src) || !nodes.containsKey(dest))
            return src;

        Map<Position, Integer> dist = new HashMap<>();
        Map<Position, Position> prev = new HashMap<>();
        Map<Position, Boolean> visited = new HashMap<>();

        prev.put(src, null);
        dist.put(src, 0);

        PriorityQueue<Position> q = new PriorityQueue<>((x, y) -> Integer
                .compare(dist.getOrDefault(x, Integer.MAX_VALUE), dist.getOrDefault(y, Integer.MAX_VALUE)));
        q.add(src);

        while (!q.isEmpty()) {
            Position curr = q.poll();
            if (curr.equals(dest) || dist.get(curr) > 200)
                break;
            // check portal
            if (nodes.containsKey(curr) && nodes.get(curr).getEntities().stream().anyMatch(Portal.class::isInstance)) {
                Portal portal = nodes.get(curr).getEntities().stream().filter(Portal.class::isInstance)
                        .map(Portal.class::cast).toList().get(0);
                List<Position> teleportDest = portal.getDestPositions(this, entity);
                teleportDest.stream().filter(p -> !visited.containsKey(p)).forEach(p -> {
                    dist.put(p, dist.get(curr));
                    prev.put(p, prev.get(curr));
                    q.add(p);
                });
                continue;
            }
            visited.put(curr, true);
            List<Position> neighbours = curr.getCardinallyAdjacentPositions().stream()
                    .filter(p -> !visited.containsKey(p))
                    .filter(p -> !nodes.containsKey(p) || nodes.get(p).canMoveOnto(this, entity)).toList();

            neighbours.forEach(n -> {
                int newDist = dist.get(curr) + (nodes.containsKey(n) ? nodes.get(n).getWeight() : 1);
                if (newDist < dist.getOrDefault(n, Integer.MAX_VALUE)) {
                    q.remove(n);
                    dist.put(n, newDist);
                    prev.put(n, curr);
                    q.add(n);
                }
            });
        }
        Position ret = dest;
        if (prev.get(ret) == null || ret.equals(src))
            return src;
        while (!prev.get(ret).equals(src)) {
            ret = prev.get(ret);
        }
        return ret;
    }

    public void removeNode(Entity entity) {
        Position p = entity.getPosition();
        if (nodes.containsKey(p)) {
            nodes.get(p).removeEntity(entity);
            if (nodes.get(p).size() == 0) {
                nodes.remove(p);
            }
        }
    }

    public void destroyEntitiesOnPosition(int x, int y) {
        List<Entity> entities = getEntities(new Position(x, y));
        entities = entities.stream().filter(Predicate.not(Player.class::isInstance)).toList();
        for (Entity e : entities)
            destroyEntity(e);
    }

    /** Destroy an entity from the game map */
    public void destroyEntity(Entity entity) {
        removeNode(entity);
        if (entity instanceof Destroy destroy) {
            destroy.onDestroy(this);
        }
    }

    /**
     * Add an entity to the game map
     *
     * The entity's position on the map is determined based on its `getPosition` method.
     */
    public void addEntity(Entity entity) {
        addNode(new MapTile(entity));
    }

    /**
     * Add a tile to the map.
     *
     * If another MapTile instance exists at the same position, their entities are merged into one tile.
     */
    public void addNode(MapTile tile) {
        Position p = tile.getPosition();

        if (!nodes.containsKey(p))
            nodes.put(p, tile);
        else {
            MapTile curr = nodes.get(p);
            curr.mergeEntities(tile);
            nodes.put(p, curr);
        }
    }

    /**
     * Search for an entity on the map given its ID.
     *
     * Note that this doesn't search other locations such as the player's inventory.
     *
     * @param id unique ID of entity to search for.
     * @return the entity, or `null` if not found.
     */
    public Entity getEntity(String id) {
        Entity res = null;
        for (Map.Entry<Position, MapTile> entry : nodes.entrySet()) {
            List<Entity> es = entry.getValue().getEntities().stream().filter(e -> e.getId().equals(id)).toList();
            if (!es.isEmpty()) {
                res = es.get(0);
                break;
            }
        }
        return res;
    }

    /** Return a list of entities at the given position */
    public List<Entity> getEntities(Position p) {
        MapTile node = nodes.get(p);
        return (node != null) ? node.getEntities() : new ArrayList<>();
    }

    /** Return a list of all entities on the map */
    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        nodes.forEach((k, v) -> entities.addAll(v.getEntities()));
        return entities;
    }

    /**
     * Get all entities which are an instance of the given type.
     *
     * For example, if you give `Interactable.class` as a parameter, this will return a list containing all interactive
     * entities on the map.
     *
     * @param type type of entities to find
     * @return list of entities matching that type
     */
    public <T extends Entity> List<T> getEntities(Class<T> type) {
        return getEntities().stream().filter(type::isInstance).map(type::cast).collect(Collectors.toList());
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Map<Position, MapTile> getNodes() {
        return nodes;
    }
}
