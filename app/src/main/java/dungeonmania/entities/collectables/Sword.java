package dungeonmania.entities.collectables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Sword extends InventoryItem implements Useable {
    public static final double DEFAULT_ATTACK = 1;
    public static final double DEFAULT_ATTACK_SCALE_FACTOR = 1;
    public static final int DEFAULT_DURABILITY = 5;
    public static final double DEFAULT_DEFENCE = 0;
    public static final double DEFAULT_DEFENCE_SCALE_FACTOR = 1;

    // private Durability durability;

    public Sword(Position position, double attack, int durability) {
        super(position);
        setDurability(new Durability(durability));
        setBuff(new Buff(new BattleStatistics(0, attack, 0, 1, 1)));
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player player) {
            if (!player.pickUp(this))
                return;
            map.destroyEntity(this);
        }
    }

    @Override
    public void use(Game game) {
        Durability durability = getDurabilityObject();
        durability.useCollectable();
        if (durability.isNegativeDurability()) {
            game.getPlayer().remove(this);
        }
    }

}
