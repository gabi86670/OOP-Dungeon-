package dungeonmania.entities.enemies.MovementStrategies;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class RunAwayMovement implements MovementStrategy {
    @Override
    public void move(Game game, Enemy enemy) {
        GameMap map = game.getMap();
        Position currPos = enemy.getPosition();

        Position plrDiff = Position.calculatePositionBetween(map.getPlayer().getPosition(), currPos);
        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(currPos, Direction.RIGHT)
                : Position.translateBy(currPos, Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(currPos, Direction.DOWN)
                : Position.translateBy(currPos, Direction.UP);
        Position offset = currPos;

        // If on the same Y axis and can flee left or right, do so.
        if (plrDiff.getY() == 0 && map.canMoveTo(enemy, moveX))
            offset = moveX;
        // Or if on the same X axis and can flee up or down, do so.
        else if (plrDiff.getX() == 0 && map.canMoveTo(enemy, moveY))
            offset = moveY;
        // Prioritise Y movement if further away on the X axis
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            if (map.canMoveTo(enemy, moveY))
                offset = moveY;
            else if (map.canMoveTo(enemy, moveX))
                offset = moveX;
            else
                offset = currPos;
            // Prioritise X movement if further away on the Y axis
        } else {
            if (map.canMoveTo(enemy, moveX))
                offset = moveX;
            else if (map.canMoveTo(enemy, moveY))
                offset = moveY;
            else
                offset = currPos;
        }
        map.moveTo(enemy, offset);
    }

}
