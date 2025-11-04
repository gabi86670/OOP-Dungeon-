package dungeonmania.entities.enemies.MovementStrategies;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class HostileMovement implements MovementStrategy {
    @Override
    public void move(Game game, Enemy enemy) {
        GameMap map = game.getMap();
        Position currPos = enemy.getPosition();
        Player player = map.getPlayer();

        Position nextPos = map.dijkstraPathFind(currPos, player.getPosition(), enemy);
        map.moveTo(enemy, nextPos);
    }

}
