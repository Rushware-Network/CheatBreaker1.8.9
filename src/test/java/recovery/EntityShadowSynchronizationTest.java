package recovery;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.ConfigManager;
import com.cheatbreaker.client.config.GlobalSettings;
import com.cheatbreaker.client.config.Setting;
import java.lang.reflect.Field;
import junit.framework.TestCase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import org.apache.logging.log4j.LogManager;
import sun.misc.Unsafe;

public class EntityShadowSynchronizationTest extends TestCase {
    private CheatBreaker previousClient;
    private Minecraft previousMinecraft;
    private Setting previousDarkMode;
    private RecordingGameSettings game;
    private GlobalSettings global;

    public static class RecordingGameSettings extends GameSettings {
        int saves;
        @Override public void saveOptions() { saves++; }
    }

    private static <T> T allocate(Class<T> type) throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return type.cast(((Unsafe)field.get(null)).allocateInstance(type));
    }

    @Override public void setUp() {
        previousClient = CheatBreaker.instance;
        previousMinecraft = Minecraft.theMinecraft;
        previousDarkMode = GlobalSettings.recoveredField529;
        try {
            Minecraft.theMinecraft = allocate(Minecraft.class);
            game = allocate(RecordingGameSettings.class);
            Minecraft.theMinecraft.gameSettings = game;
            CheatBreaker.instance = allocate(CheatBreaker.class);
            CheatBreaker.instance.configManager = allocate(ConfigManager.class);
            CheatBreaker.instance.recoveredField1579 = LogManager.getLogger("EntityShadowSynchronizationTest");
            global = new GlobalSettings();
            CheatBreaker.instance.globalSettings = global;
        } catch (Exception error) {
            tearDown();
            throw new IllegalStateException(error);
        }
    }

    @Override public void tearDown() {
        CheatBreaker.instance = previousClient;
        Minecraft.theMinecraft = previousMinecraft;
        GlobalSettings.recoveredField529 = previousDarkMode;
    }

    public void testInitialStateUsesSavedGameOption() {
        assertFalse(global.recoveredField580.method_08908());
        game.entityShadows = true;
        assertTrue(new GlobalSettings().recoveredField580.method_08908());
        assertEquals(0, game.saves);
    }

    public void testGlobalToggleWorksInMainMenuAndWithFpsBoostDisabled() {
        // No world or RenderManager has been initialized.
        global.recoveredField563.setValue(false, false);
        for (boolean enabled : new boolean[]{true, false, true, false}) {
            global.recoveredField580.setValue(enabled, false);
            assertEquals(enabled, game.entityShadows);
            assertEquals(enabled, global.recoveredField580.method_08908());
        }
        assertEquals(4, game.saves);
        global.recoveredField580.setValue(false, false);
        assertEquals(4, game.saves);
    }

}
