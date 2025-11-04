package dungeonmania.entities.enemies.MovementStrategies;

import java.util.List;
import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class RandomMovement implements MovementStrategy {
    private Random randGen = new Random();

    @Override
    public void move(Game game, Enemy enemy) {
        GameMap map = game.getMap();
        Position currPos = enemy.getPosition();

        List<Position> pos = currPos.getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> map.canMoveTo(enemy, p)).toList();
        Position nextPos;
        if (pos.size() == 0) {
            nextPos = currPos;
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
        }
        map.moveTo(enemy, nextPos);
    }

}
