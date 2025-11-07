package dungeonmania.entities;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Portal extends Entity implements Overlap {
    /** Color of this portal */
    private ColorCodedType color;
    /** Reference to the paired portal */
    private Portal pair;

    public Portal(Position position, ColorCodedType color) {
        super(position);
        this.color = color;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (pair == null)
            return false;
        if (entity instanceof Player || entity instanceof Mercenary)
            return pair.canTeleportTo(map, entity);
        return true;
    }

    /** Return whether the given entity can teleport to this portal */
    public boolean canTeleportTo(GameMap map, Entity entity) {
        List<Position> neighbours = getPosition().getCardinallyAdjacentPositions();
        return neighbours.stream().anyMatch(n -> map.canMoveTo(entity, n));
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (pair == null)
            return;
        if (entity instanceof Player || entity instanceof Mercenary)
            doTeleport(map, entity);
    }

    private void doTeleport(GameMap map, Entity entity) {
        pair.getPosition().getCardinallyAdjacentPositions().stream().filter(dest -> map.canMoveTo(entity, dest))
                .findAny().ifPresent(destination -> map.moveTo(entity, destination));
    }

    public String getColor() {
        return color.toString();
    }

    /** Return positions which the given entity can hypothetically travel to if they pass through the portal */
    public List<Position> getDestPositions(GameMap map, Entity entity) {
        return pair == null ? null
                : pair.getPosition().getAdjacentPositions().stream().filter(p -> map.canMoveTo(entity, p))
                        .collect(Collectors.toList());
    }

    /** Bind this portal to another portal */
    public void bind(Portal portal) {
        if (this.pair == portal)
            return;
        // Unpair with previous portal
        if (this.pair != null) {
            this.pair.bind(null);
        }
        this.pair = portal;
        if (portal != null) {
            portal.bind(this);
        }
    }
}
