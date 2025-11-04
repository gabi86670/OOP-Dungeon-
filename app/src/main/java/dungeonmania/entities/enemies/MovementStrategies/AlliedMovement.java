package dungeonmania.entities.enemies.MovementStrategies;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class AlliedMovement implements MovementStrategy {
    private boolean wasAdjacentToPlayer = false;

    @Override
    public void move(Game game, Enemy enemy) {
        GameMap map = game.getMap();
        Position currPos = enemy.getPosition();
        Position nextPos;
        Player player = game.getPlayer();

        boolean isAdjacentToPlayer = Position.isAdjacent(player.getPosition(), currPos);
        if (wasAdjacentToPlayer && !isAdjacentToPlayer) {
            nextPos = player.getPreviousDistinctPosition();
        } else {
            // If currently still adjacent, wait in place. Else pursue the player.
            nextPos = isAdjacentToPlayer ? currPos : map.dijkstraPathFind(currPos, player.getPosition(), enemy);
            wasAdjacentToPlayer = Position.isAdjacent(player.getPosition(), nextPos);
        }
        map.moveTo(enemy, nextPos);
    }

}
