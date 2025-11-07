package dungeonmania.goals.leaves;

import dungeonmania.Game;
import dungeonmania.entities.Switch;
import dungeonmania.goals.Goal;

public class BoulderGoal implements Goal {
    @Override
    public boolean achieved(Game game) {
        return game.getMap().getEntities(Switch.class).stream().allMatch(s -> s.isActivated());
    }

    @Override
    public String toString(Game game) {
        return achieved(game) ? "" : ":boulders";
    }

}
