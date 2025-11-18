package dungeonmania.entities.inventory;

import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Buildable;

public interface CraftingFactory {
    Buildable craft(Player player);

    boolean canCraft(Player player);
}
