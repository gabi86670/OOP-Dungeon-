package dungeonmania.battles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Useable;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.ResponseBuilder;
import dungeonmania.util.NameConverter;

/** Handles the battle process between the player and an enemy */
public class BattleFacade {
    private List<BattleResponse> battleResponses = new ArrayList<>();

    /** Battle the player against the given enemy */
    public void battle(Game game, Player player, Enemy enemy) {
        // 0. init
        double initialPlayerHealth = player.getHealth();
        double initialEnemyHealth = enemy.getHealth();
        String enemyString = NameConverter.toSnakeCase(enemy);

        // 1. get and apply buff to player, using any relevant potions, inventory items and allies
        BattleStatistics playerBuff = new BattleStatistics(0, 0, 0, 1, 1);
        List<InventoryItem> battleItems = player.getBattleItems();

        Potion effectivePotion = player.getEffectivePotion();
        if (effectivePotion != null) {
            playerBuff = player.applyBuff(playerBuff);
        } else {
            for (InventoryItem item : player.getBattleItems()) {
                playerBuff = item.applyBuff(playerBuff);
                ((Useable) item).use(game);
            }
        }

        List<Mercenary> mercs = game.getAlliedMercenaries();
        for (Mercenary merc : mercs) {
            // if (!merc.isAllied())
            //     continue;
            playerBuff = BattleStatistics.applyBuff(playerBuff, merc.getBattleStatistics());
        }

        // 2. Battle the two stats
        BattleStatistics playerBaseStatistics = player.getBattleStatistics();
        BattleStatistics enemyBaseStatistics = enemy.getBattleStatistics();
        BattleStatistics playerBattleStatistics = BattleStatistics.applyBuff(playerBaseStatistics, playerBuff);
        BattleStatistics enemyBattleStatistics = enemyBaseStatistics;
        if (!playerBattleStatistics.isBattleEnabled() || !enemyBaseStatistics.isBattleEnabled())
            return;

        List<BattleRound> rounds = BattleStatistics.battle(playerBattleStatistics, enemyBattleStatistics);

        // 3. update health to the actual statistics
        player.setHealth(playerBattleStatistics.getHealth());
        enemy.setHealth(enemyBattleStatistics.getHealth());

        // 4. Log the battle - solidate it to be a battle response
        battleResponses.add(new BattleResponse(enemyString,
                rounds.stream().map(ResponseBuilder::getRoundResponse).collect(Collectors.toList()),
                battleItems.stream().map(Entity.class::cast).map(ResponseBuilder::getItemResponse)
                        .collect(Collectors.toList()),
                initialPlayerHealth, initialEnemyHealth));
    }

    /** Return the battle log, displayed to the player */
    public List<BattleResponse> getBattleResponses() {
        return battleResponses;
    }
}
