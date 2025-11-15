package dungeonmania.mvp.task2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class SunStoneTest {
    @Test
    @Tag("14-1")
    @DisplayName("Test player picks up and adds sunstone to inventory")
    public void collectSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunStoneTest_PickUp", "c_SunStoneTest_PickUp");

        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("14-2")
    @DisplayName("Test sunstone counts for treasure goal")
    public void sunStoneCountsForTreasureGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunStoneTest_TreasureGoal", "c_SunStoneTest_TreasureGoal");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
    }

    @Test
    @Tag("14-3")
    @DisplayName("Test sunstone to open door and not remove it from inventory")
    public void sunStoneUsedToOpenDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunStoneTest_OpenDoor", "c_SunStoneTest_OpenDoor");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        Position beforeDoor = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.RIGHT);
        Position afterDoor = TestUtils.getEntities(res, "player").get(0).getPosition();

        // sunstone still in inventory
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // player shouldve moved thro door
        assertNotEquals(beforeDoor, afterDoor);
        res = dmc.tick(Direction.LEFT);
        Position backThroughDoor = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertNotEquals(afterDoor, backThroughDoor);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("14-4")
    @DisplayName("Test just sunstone cant bribe mercenaries")
    public void sunStoneCannotBribeSingle() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunStoneTest_BribeSingle", "c_SunStoneTest_BribeSingle");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // mercenary should be unallied
        EntityResponse merc = TestUtils.getEntities(res, "mercenary").get(0);
        assertFalse(merc.isInteractable());
    }

    @Test
    @Tag("14-5")
    @DisplayName("Test mixed treasures cannot bribe mercenaries")
    public void sunStoneWithTreasureCannotBribeMerc() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunStoneTest_MixedTreasure", "c_SunStoneTest_MixedTreasure");
        // treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        List<EntityResponse> mercs = TestUtils.getEntities(res, "mercenary");
        assertTrue(!mercs.isEmpty(), "Expected a mercenary to exist in the dungeon");
        EntityResponse merc = mercs.get(0);
        assertFalse(merc.isInteractable());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @Tag("14-6")
    @DisplayName("Test sunstone used to craft shield is not removed from inventory")
    public void sunStoneUsedForShieldCrafting() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunStoneTest_CraftShield", "c_SunStoneTest_CraftShield");
        res = dmc.tick(Direction.RIGHT);

        // pick up 2 wood
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // build Shield
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // sunstone still in inventory
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        // wood removed
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
    }
}
