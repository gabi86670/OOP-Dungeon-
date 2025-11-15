package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.PotionListener;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.MovementStrategies.AlliedMovement;
import dungeonmania.entities.enemies.MovementStrategies.HostileMovement;
import dungeonmania.entities.enemies.MovementStrategies.MovementStrategy;
import dungeonmania.entities.enemies.MovementStrategies.RandomMovement;
import dungeonmania.entities.enemies.MovementStrategies.RunAwayMovement;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable, PotionListener {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;

    private double allyAttack;
    private double allyDefence;
    private boolean allied = false;

    /** Type of movement to use */
    private MovementStrategy movementStrategy = new HostileMovement();

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
    }

    public boolean isAllied() {
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied)
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current mercenary can be bribed
     */
    private boolean canBeBribed(Player player) {
        Position playerPos = player.getPosition();
        int distance = Math.abs(playerPos.getX() - getPosition().getX())
                + Math.abs(playerPos.getY() - getPosition().getY());
        int numTreasure = player.countEntityOfType(Treasure.class) - player.countEntityOfType(SunStone.class);
        return distance <= bribeRadius && numTreasure >= bribeAmount;
    }

    /**
     * bribe the mercenary
     */
    private void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.useBribeTreasure();
        }
    }

    @Override
    public void interact(Player player, Game game) {
        allied = true;
        this.movementStrategy = new AlliedMovement();
        bribe(player);
    }

    @Override
    public void move(Game game) {
        movementStrategy.move(game, this);
    }

    @Override
    public boolean isInteractable(Player player) {
        return !allied && canBeBribed(player);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        if (!allied)
            return super.getBattleStatistics();
        return new BattleStatistics(0, allyAttack, allyDefence, 1, 1);
    }

    @Override
    public void notifyPotion(Potion potion) {
        if (allied)
            return;

        if (potion instanceof InvisibilityPotion)
            this.movementStrategy = new RandomMovement();
        if (potion instanceof InvincibilityPotion)
            this.movementStrategy = new RunAwayMovement();

    }

    @Override
    public void notifyNoPotion() {
        if (allied)
            return;

        this.movementStrategy = new HostileMovement();
    }
}
