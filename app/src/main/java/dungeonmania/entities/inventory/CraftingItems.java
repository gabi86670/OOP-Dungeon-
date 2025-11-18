package dungeonmania.entities.inventory;

import java.util.List;

import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class CraftingItems {
    private final Player player;
    private final Inventory inventory;
    private final List<Wood> wood;
    private final List<Arrow> arrows;
    private final List<Treasure> normalTreasure;
    private final List<Key> keys;
    private final List<SunStone> sunStones;
    private final Sword sword;

    public CraftingItems(Player player) {
        this.player = player;
        this.inventory = player.getInventory();
        this.wood = inventory.getEntities(Wood.class);
        this.arrows = inventory.getEntities(Arrow.class);
        this.normalTreasure = inventory.getEntities(Treasure.class).stream().filter(t -> !(t instanceof SunStone))
                .toList();
        this.keys = inventory.getEntities(Key.class);
        this.sunStones = inventory.getEntities(Treasure.class).stream().filter(t -> t instanceof SunStone)
                .map(t -> (SunStone) t).toList();
        this.sword = inventory.getFirst(Sword.class);
    }

    /** Removes the first n Wood items */
    public void removeWood(int n) {
        wood.stream().limit(n).forEach(inventory::remove);
    }

    /** Removes the first n Arrow items */
    public void removeArrows(int n) {
        arrows.stream().limit(n).forEach(inventory::remove);
    }

    /** Removes the first normal treasure */
    public void removeNormalTreasure(int n) {
        if (!normalTreasure.isEmpty()) {
            normalTreasure.stream().limit(n).forEach(inventory::remove);
        }
    }

    /** Removes the first key */
    public void removeKey(int n) {
        keys.stream().limit(n).forEach(inventory::remove);
    }

    /** Removes the first SunStone */
    public void removeSunStone(int n) {
        sunStones.stream().limit(n).forEach(inventory::remove);
    }

    /** Removes the sword */
    public void removeSword() {
        if (sword != null) {
            inventory.remove(sword);
        }
    }

    public int countWood() {
        return wood.size();
    }

    public int countArrows() {
        return arrows.size();
    }

    public int countTreasure() {
        return normalTreasure.size();
    }

    public int countKeys() {
        return keys.size();
    }

    public int countSunStones() {
        return sunStones.size();
    }

    public boolean hasSword() {
        return sword != null;
    }
}
