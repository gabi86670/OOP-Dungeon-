package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.entities.collectables.Useable;

public class Sceptre extends Buildable implements Useable {
    private int durability;

    public Sceptre(int durability) {
        super(null);
        this.durability = durability;
    }

    public int getDurability() {
        return durability;
    }

    @Override
    public void use(Game game) {
        game.getPlayer().remove(this);
    }
}
