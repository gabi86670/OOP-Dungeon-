package dungeonmania.goals.leaves;

import dungeonmania.Game;
import dungeonmania.goals.Goal;

public class TreasureGoal implements Goal {
    private int target;

    public TreasureGoal(int target) {
        this.target = target;
    }

    @Override
    public boolean achieved(Game game) {
        return game.getCollectedTreasureCount() >= target;
    }

    @Override
    public String toString(Game game) {
        return achieved(game) ? "" : ":treasure";
    }

}
