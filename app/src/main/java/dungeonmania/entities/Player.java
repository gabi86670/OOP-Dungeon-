package dungeonmania.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.buildables.Buildable;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Useable;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.inventory.Recipe;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

/**
 * The game's one and only player!
 *
 * The player object stores critical information about the player such as their inventory, potion queue and battle
 * statistics.
 */
public class Player extends Entity implements Battleable, Overlap {
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 5.0;
    /** Stats used when the player goes into battle */
    private BattleStatistics battleStatistics;
    private Inventory inventory;
    /**
     * The player's queue of potions. Only one potion is ever in effect at once. Any potions activated while another
     * potion is active get queued here.
     */
    private Queue<Potion> queue = new LinkedList<>();
    /** Currently active potion, or `null` if the player is not experiencing any potion effects. */
    private Potion inEffect = null;
    private int nextTrigger = 0;
    /** Subscribers to player potion events */
    private Set<PotionListener> potionListeners = new HashSet<>();

    /** Number of treasures the player has collected */
    private int collectedTreasureCount = 0;

    private Recipe recipe;

    public Player(Position position, double health, double attack) {
        super(position);
        battleStatistics = new BattleStatistics(health, attack, 0, BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_PLAYER_DAMAGE_REDUCER);
        inventory = new Inventory();
    }

    public int getCollectedTreasureCount() {
        return collectedTreasureCount;
    }

    public boolean hasWeapon() {
        return inventory.hasWeapon();
    }

    public Useable getWeapon() {
        return inventory.getWeapon();
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    /** Returns a list of things that can be built by the player currently */
    public List<String> getBuildables() {
        if (recipe == null)
            return new ArrayList<>();
        return recipe.canCraftItems(this);
    }

    /** Called when the player chooses to craft something. */
    public boolean build(String entity) {
        if (recipe == null)
            throw new IllegalStateException("Recipe not set!");

        Buildable craftNew = recipe.craft(entity, this);
        if (craftNew == null)
            return false;

        return inventory.add((InventoryItem) craftNew);
    }

    public void move(GameMap map, Direction direction) {
        this.setFacing(direction);
        map.moveTo(this, Position.translateBy(this.getPosition(), direction));
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Enemy enemy) {
            if (enemy instanceof Mercenary mercenary && mercenary.isAllied())
                return;
            map.getGame().battle(this, enemy);
        }
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public Entity getEntity(String itemUsedId) {
        return inventory.getEntity(itemUsedId);
    }

    public boolean pickUp(Entity item) {
        if (item instanceof Treasure || item instanceof SunStone)
            collectedTreasureCount++;
        return inventory.add((InventoryItem) item);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Potion getEffectivePotion() {
        return inEffect;
    }

    /**
     * Use an item of the given type.
     *
     * This removes that item from the player's inventory. If the player has no such item, nothing happens.
     */
    public <T extends InventoryItem> void use(Class<T> itemType) {
        T item = inventory.getFirst(itemType);
        if (item != null)
            inventory.remove(item);
    }

    /*
     * Use a treasure item, exclucding SunStones.
     * 
     * This removes a treasure (excluding SunStones) from the player's inventory.
     * If the player has no treasures, then nothing happens.
     */
    public void useBribeTreasure() {
        InventoryItem i = inventory.getEntities(Treasure.class).stream().filter(item -> !(item instanceof SunStone))
                .findFirst().orElse(null);

        if (i != null) {
            inventory.remove(i);
        }
    }

    /** Called when the player places a bomb */
    public void use(Bomb bomb, GameMap map) {
        inventory.remove(bomb);
        bomb.onPutDown(map, getPosition());
    }

    /** Trigger next potion effect */
    public void triggerNext(int currentTick) {
        if (queue.isEmpty()) {
            inEffect = null;
            potionListeners.forEach(PotionListener::notifyNoPotion);
            return;
        }
        inEffect = queue.remove();
        potionListeners.forEach(e -> e.notifyPotion(inEffect));
        nextTrigger = currentTick + inEffect.getDuration();
    }

    /** Called when the player consumes a potion */
    public void use(Potion potion, int tick) {
        inventory.remove(potion);
        queue.add(potion);
        if (inEffect == null) {
            triggerNext(tick);
        }
    }

    public void onTick(int tick) {
        if (inEffect == null || tick == nextTrigger) {
            triggerNext(tick);
        }
    }

    /** Remove given item from the player's inventory */
    public void remove(InventoryItem item) {
        inventory.remove(item);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    public BattleStatistics getBattleStatisticsBuffs(Game game) {
        BattleStatistics stats = this.getBattleStatistics();

        // Apply potion buff if active
        stats = applyBuff(stats);

        // Apply inventory buffs and use items
        for (InventoryItem item : inventory.getEntities(InventoryItem.class)) {
            if (item instanceof Useable useable) {
                stats = item.applyBuff(stats);
                useable.use(game);
            }
        }

        return stats;
    }

    public <T extends InventoryItem> int countEntityOfType(Class<T> itemType) {
        return inventory.count(itemType);
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        if (inEffect instanceof InvincibilityPotion) {
            return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, 0, 1, 1, true, true));
        } else if (inEffect instanceof InvisibilityPotion) {
            return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, 0, 1, 1, false, false));
        }
        return origin;
    }

    public void registerPotionListener(PotionListener e) {
        potionListeners.add(e);

        if (getEffectivePotion() != null)
            e.notifyPotion(getEffectivePotion());
    }

    public void removePotionListener(PotionListener e) {
        potionListeners.remove(e);
    }

    public List<InventoryItem> getBattleItems() {
        return inventory.getBattleItems();
    }

    public double getHealth() {
        return battleStatistics.getHealth();
    }

    public void setHealth(double health) {
        battleStatistics.setHealth(health);
    }
}
