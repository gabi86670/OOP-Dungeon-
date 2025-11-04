package dungeonmania.entities.collectables;

import dungeonmania.battles.BattleStatistics;

public class Buff {
    private final BattleStatistics buffStat;

    public Buff(BattleStatistics stats) {
        this.buffStat = stats;
    }

    public BattleStatistics applyBuff(BattleStatistics stats) {
        return BattleStatistics.applyBuff(stats, buffStat);
    }
}
