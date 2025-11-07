package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.UUID;

/**
 * Entity: the base type for "things" within the game. Everything is an entity, from the player to the monsters to the
 * items to the literal walls of the world.
 *
 * This class contains common functionality for all entities, such as handling movement on the map, and storing
 * information about how to draw the entity (eg the character is drawn above the floor).
 */
public abstract class Entity {
    public static final int FLOOR_LAYER = 0;
    public static final int ITEM_LAYER = 1;
    public static final int DOOR_LAYER = 2;
    public static final int CHARACTER_LAYER = 3;

    /** Current position of the entity */
    private Position position;
    /** Position of the entity on the previous tick */
    private Position previousPosition;
    /**
     * The last position this entity visited that is different to the current position.
     *
     * Set to `null` initially.
     */
    private Position previousDistinctPosition;
    /**
     * Facing direction (helpful for drawing sprites that look in the direction the entity is moving)
     *
     * Set to `null` initially. Use `setFacing` to update.
     */
    private Direction facing;
    /** Unique ID of the entity (as a UUID) */
    private String entityId;

    /** Construct an entity at the given position */
    public Entity(Position position) {
        this.position = position;
        this.previousPosition = position;
        this.previousDistinctPosition = null;
        this.entityId = UUID.randomUUID().toString();
        this.facing = null;
    }

    /**
     * Given the current game map and another entity, return whether `that` entity can move onto `this` entity.
     *
     * By default, this returns `false` meaning that no other entities can move into the same position as this entity.
     */
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return false;
    }

    /** use setPosition */
    @Deprecated(forRemoval = true)
    public void translate(Direction direction) {
        previousPosition = this.position;
        this.position = Position.translateBy(this.position, direction);
        if (!previousPosition.equals(this.position)) {
            previousDistinctPosition = previousPosition;
        }
    }

    /** use setPosition */
    @Deprecated(forRemoval = true)
    public void translate(Position offset) {
        this.position = Position.translateBy(this.position, offset);
    }

    /** Current position of the entity */
    public Position getPosition() {
        return position;
    }

    /** Position of the entity on the previous tick */
    public Position getPreviousPosition() {
        return previousPosition;
    }

    /**
     * The last position this entity visited that is different to the current position.
     *
     * Set to `null` initially.
     */
    public Position getPreviousDistinctPosition() {
        return previousDistinctPosition;
    }

    /** Unique ID for the entity */
    public String getId() {
        return entityId;
    }

    /**
     * Update the position of this entity.
     *
     * This only updates the entity's internal position data (and previous position), and so the position also needs to
     * get updated on the GameMap too.
     */
    public void setPosition(Position position) {
        previousPosition = this.position;
        this.position = position;
        if (!previousPosition.equals(this.position)) {
            previousDistinctPosition = previousPosition;
        }
    }

    /** Update the facing direction for this entity. */
    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    /**
     * Facing direction (helpful for drawing sprites that look in the direction the entity is moving)
     *
     * Set to `null` initially. Use `setFacing` to update.
     */
    public Direction getFacing() {
        return this.facing;
    }
}
