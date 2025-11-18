package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Buildable;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.map.GameMap;

public class Recipe {
    private final GameMap map;
    private final BowFactory bowFactory;
    private final ShieldFactory shieldFactory;
    private final SceptreFactory sceptreFactory;
    private final MidnightArmourFactory armourFactory;

    public Recipe(GameMap map, BowFactory bowFactory, ShieldFactory shieldFactory, SceptreFactory sceptreFactory,
            MidnightArmourFactory armourFactory) {
        this.map = map;
        this.bowFactory = bowFactory;
        this.shieldFactory = shieldFactory;
        this.sceptreFactory = sceptreFactory;
        this.armourFactory = armourFactory;
    }

    private boolean hasZombies() {
        return map.getEntities().stream().anyMatch(e -> e instanceof ZombieToast || e instanceof ZombieToastSpawner);
    }

    public List<String> canCraftItems(Player player) {
        List<String> craftables = new ArrayList<>();

        if (bowFactory.canCraft(player)) {
            craftables.add("bow");
        }
        if (shieldFactory.canCraft(player)) {
            craftables.add("shield");
        }
        if (sceptreFactory.canCraft(player)) {
            craftables.add("sceptre");
        }
        if (!hasZombies() && armourFactory.canCraft(player)) {
            craftables.add("midnight_armour");
        }
        return craftables;
    }

    public Buildable craft(String item, Player player) {
        switch (item.toLowerCase()) {
        case "bow":
            if (bowFactory.canCraft(player)) {
                return bowFactory.craft(player);
            }
            break;
        case "shield":
            if (shieldFactory.canCraft(player)) {
                return shieldFactory.craft(player);
            }
            break;
        case "sceptre":
            if (sceptreFactory.canCraft(player)) {
                return sceptreFactory.craft(player);
            }
            break;
        case "midnight_armour":
            if (!hasZombies() && armourFactory.canCraft(player)) {
                return armourFactory.craft(player);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown item: " + item);
        }
        return null;
    }
}
