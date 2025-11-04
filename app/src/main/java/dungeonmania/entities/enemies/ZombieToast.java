package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.PotionListener;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.MovementStrategies.MovementStrategy;
import dungeonmania.entities.enemies.MovementStrategies.RandomMovement;
import dungeonmania.entities.enemies.MovementStrategies.RunAwayMovement;

import dungeonmania.util.Position;

public class ZombieToast extends Enemy implements PotionListener {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;

    private MovementStrategy movementStrategy = new RandomMovement();

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
    }

    @Override
    public void move(Game game) {
        movementStrategy.move(game, this);
    }

    @Override
    public void notifyPotion(Potion potion) {
        if (potion instanceof InvincibilityPotion)
            this.movementStrategy = new RunAwayMovement();

    }

    @Override
    public void notifyNoPotion() {
        this.movementStrategy = new RandomMovement();

    }

}
