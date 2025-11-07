package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.collectables.Bomb;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Switch extends Entity implements Overlap, MovedAway {
    /** Whether this switch is activated (ie a boulder has been pushed on top of it) */
    private boolean activated;
    private List<Bomb> bombs = new ArrayList<>();

    public Switch(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER));
    }

    /** Subscribe to the state of this switch */
    public void subscribe(Bomb bomb, GameMap map) {
        bombs.add(bomb);
        if (activated) {
            activateBombs(map);
        }
    }

    public void unsubscribe(Bomb b) {
        bombs.remove(b);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = true;
            activateBombs(map);
        }
    }

    public void activateBombs(GameMap map) {
        for (Bomb b : bombs) {
            int x = b.getPosition().getX();
            int y = b.getPosition().getY();
            for (int i = x - b.getRadius(); i <= x + b.getRadius(); i++) {
                for (int j = y - b.getRadius(); j <= y + b.getRadius(); j++) {
                    map.destroyEntitiesOnPosition(i, j);
                }
            }
        }
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = false;
        }
    }

    public boolean isActivated() {
        return activated;
    }

}
