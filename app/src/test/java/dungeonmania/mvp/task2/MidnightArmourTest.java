package dungeonmania.mvp.task2;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

@Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class MidnightArmourTest {
    @Test
    @Tag("16-1")
    @DisplayName("Test craft midnight armour successfully when no zombies and add to inventory")
    public void craftArmourNoZombies() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_MidnightArmour_NoZombies", "c_MidnightArmour_NoZombies");

        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("16-2")
    @DisplayName("Test can't craft midnight armour if zombies exist")
    public void cannotCraftArmourWithZombies() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_MidnightArmour_WithZombies", "c_MidnightArmour_WithZombies");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertThrows(Exception.class, () -> dmc.build("midnight_armour"));
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());

        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }
}
