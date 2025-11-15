package dungeonmania.entities.inventory;

import org.json.JSONObject;
import dungeonmania.entities.buildables.Buildable;
import dungeonmania.entities.buildables.Sceptre;

public class SceptreFactory implements CraftingFactory {
    private final int sceptreDurability;

    public SceptreFactory(JSONObject config) {
        this.sceptreDurability = config.optInt("mind_control_duration");
    }

    @Override
    public Buildable craft() {
        return new Sceptre(sceptreDurability);
    }
}
