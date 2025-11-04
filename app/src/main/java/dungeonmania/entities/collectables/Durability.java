package dungeonmania.entities.collectables;

public class Durability {
    private int durability;

    public Durability(int durability) {
        this.durability = durability;
    }

    public int getDurability() {
        return this.durability;
    }

    public void useCollectable() {
        if (durability > 0) {
            this.durability--;
        }
    }

    public boolean isNegativeDurability() {
        return this.durability <= 0;
    }
}
