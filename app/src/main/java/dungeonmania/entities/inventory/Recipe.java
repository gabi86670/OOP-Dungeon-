package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Buildable;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class Recipe {
    private final BowFactory bowFactory;
    private final ShieldFactory shieldFactory;
    private final SceptreFactory sceptreFactory;

    public Recipe(BowFactory bowFactory, ShieldFactory shieldFactory, SceptreFactory sceptreFactory) {
        this.bowFactory = bowFactory;
        this.shieldFactory = shieldFactory;
        this.sceptreFactory = sceptreFactory;
    }

    public List<String> canCraftItems(Player player) {
        Inventory inventory = player.getInventory();
        List<String> craftables = new ArrayList<>();
        int numTreasure = player.countEntityOfType(Treasure.class) - player.countEntityOfType(SunStone.class);

        if (inventory.count(Wood.class) >= 1 && inventory.count(Arrow.class) >= 3) {
            craftables.add("bow");
        }

        if (inventory.count(Wood.class) >= 2
                && (inventory.count(Treasure.class) >= 1 || inventory.count(Key.class) >= 1)) {
            craftables.add("shield");
        }

        if ((inventory.count(Wood.class) >= 1 || inventory.count(Arrow.class) >= 2)
                && (inventory.count(Key.class) >= 1 || numTreasure >= 1)
                && player.countEntityOfType(SunStone.class) >= 1) {
            craftables.add("sceptre");
        }

        return craftables;
    }

    public Buildable craft(String item, Player player) {
        Inventory inventory = player.getInventory();
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Arrow> arrows = inventory.getEntities(Arrow.class);
        List<Treasure> treasure = inventory.getEntities(Treasure.class);
        List<Key> keys = inventory.getEntities(Key.class);
        List<Treasure> normalTreasure = treasure.stream().filter(t -> !(t instanceof SunStone)).toList();
        List<SunStone> sunStones = treasure.stream().filter(t -> t instanceof SunStone).map(t -> (SunStone) t).toList();

        switch (item.toLowerCase()) {
        case "bow":
            if (wood.size() >= 1 && arrows.size() >= 3) {
                inventory.remove(wood.get(0));
                inventory.remove(arrows.get(0));
                inventory.remove(arrows.get(1));
                inventory.remove(arrows.get(2));

                return bowFactory.craft();
            }
            break;
        case "shield":
            if (wood.size() >= 2 && (normalTreasure.size() >= 1 || keys.size() >= 1 || sunStones.size() >= 1)) {
                inventory.remove(wood.get(0));
                inventory.remove(wood.get(1));

                if (normalTreasure.size() >= 1) {
                    inventory.remove(normalTreasure.get(0));
                } else if (keys.size() >= 1) {
                    inventory.remove(keys.get(0));
                }

                return shieldFactory.craft();
            }
            break;
        case "sceptre":
            if ((wood.size() >= 1 || arrows.size() >= 2) && (keys.size() >= 1 || normalTreasure.size() >= 1)
                    && sunStones.size() >= 1) {
                inventory.remove(sunStones.get(0));

                if (wood.size() >= 1) {
                    inventory.remove(wood.get(0));
                } else if (arrows.size() >= 2) {
                    inventory.remove(arrows.get(0));
                    inventory.remove(arrows.get(1));
                }

                if (keys.size() >= 1) {
                    inventory.remove(keys.get(0));
                } else if (normalTreasure.size() >= 1) {
                    inventory.remove(normalTreasure.get(0));
                }

                return sceptreFactory.craft();
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown item: " + item);
        }
        return null;
    }
}
