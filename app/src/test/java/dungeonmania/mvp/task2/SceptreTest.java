package dungeonmania.mvp.task2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.TimeUnit;
import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

@Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class SceptreTest {
    @Test
    @Tag("15-1")
    @DisplayName("Test craft sceptre and adding it inventory")
    public void craftSceptre() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SceptreCraftTest", "c_SceptreCraftTest");

        res = dmc.tick(Direction.RIGHT); // wood
        res = dmc.tick(Direction.RIGHT); // treasure
        res = dmc.tick(Direction.RIGHT); // sun stone

        // check all items are in inventory
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // craft sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("15-2")
    @DisplayName("Test sceptre mind control works correctly")
    public void sceptreMindControl() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SceptreMindControl", "c_SceptreMindControl");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // check all items are in inventory
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // craft sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        assertFalse(TestUtils.getEntityById(res, mercId).get().isInteractable());

        res = dmc.tick(Direction.RIGHT);
        assertFalse(TestUtils.getEntityById(res, mercId).get().isInteractable());
        res = dmc.tick(Direction.RIGHT);
        assertFalse(TestUtils.getEntityById(res, mercId).get().isInteractable());

        // now its hostile
        res = dmc.tick(Direction.UP);
    }
}
