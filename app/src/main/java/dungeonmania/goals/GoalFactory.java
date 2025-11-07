package dungeonmania.goals;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.goals.composites.AndGoal;
import dungeonmania.goals.composites.OrGoal;
import dungeonmania.goals.leaves.BoulderGoal;
import dungeonmania.goals.leaves.ExitGoal;
import dungeonmania.goals.leaves.TreasureGoal;

public class GoalFactory {
    public static Goal createGoal(JSONObject jsonGoal, JSONObject config) {
        JSONArray subgoals;
        return switch (jsonGoal.getString("goal")) {
        case "AND" -> {
            subgoals = jsonGoal.getJSONArray("subgoals");
            yield new AndGoal(createGoal(subgoals.getJSONObject(0), config),
                    createGoal(subgoals.getJSONObject(1), config));
        }
        case "OR" -> {
            subgoals = jsonGoal.getJSONArray("subgoals");
            yield new OrGoal(createGoal(subgoals.getJSONObject(0), config),
                    createGoal(subgoals.getJSONObject(1), config));
        }
        case "exit" -> new ExitGoal();
        case "boulders" -> new BoulderGoal();
        case "treasure" -> {
            int treasureGoal = config.optInt("treasure_goal", 1);
            yield new TreasureGoal(treasureGoal);
        }
        default -> null;
        };
    }
}
