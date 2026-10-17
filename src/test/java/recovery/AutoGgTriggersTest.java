package recovery;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.type.HypixelModule;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import junit.framework.TestCase;
import sun.misc.Unsafe;

public class AutoGgTriggersTest extends TestCase {
    private static <T> T allocate(Class<T> type) throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return type.cast(((Unsafe)field.get(null)).allocateInstance(type));
    }

    public void testNativeSettingRegistrationAndDefaults() throws Exception {
        com.cheatbreaker.client.CheatBreaker previous = com.cheatbreaker.client.CheatBreaker.instance;
        net.minecraft.client.Minecraft previousMinecraft = net.minecraft.client.Minecraft.theMinecraft;
        try {
            net.minecraft.client.Minecraft.theMinecraft = allocate(net.minecraft.client.Minecraft.class);
            com.cheatbreaker.client.CheatBreaker client = allocate(com.cheatbreaker.client.CheatBreaker.class);
            client.configManager = allocate(com.cheatbreaker.client.config.ConfigManager.class);
            client.recoveredField1568 = allocate(com.cheatbreaker.client.event.EventBus.class);
            client.recoveredField1568.recoveredField2205 = new java.util.concurrent.ConcurrentHashMap<>();
            com.cheatbreaker.client.CheatBreaker.instance = client;
            HypixelModule module = new HypixelModule() {
                @Override public void method_25693() { /* No network in tests. */ }
            };
            assertTrue(module.getSettingsList().contains(module.autoGgCustomTriggers));
            assertTrue(module.getSettingsList().contains(module.autoGgUseDefaultTriggers));
            assertEquals(Setting.Type.STRING, module.autoGgCustomTriggers.getType());
            assertTrue(module.autoGgCustomTriggers.method_08911().endsWith("String"));
            assertEquals("", module.autoGgCustomTriggers.getValue());
            assertEquals(512, module.autoGgCustomTriggers.getMaxStringLength());
            assertTrue(module.autoGgUseDefaultTriggers.method_08908());
            assertFalse(module.autoGgCustomTriggers.method_08921().getAsBoolean());
            module.recoveredField450.setValue(true, false);
            assertTrue(module.autoGgCustomTriggers.method_08921().getAsBoolean());
        } finally {
            com.cheatbreaker.client.CheatBreaker.instance = previous;
            net.minecraft.client.Minecraft.theMinecraft = previousMinecraft;
        }
    }

    public void testCustomPhrasesAndSeparators() {
        String triggers = " Victory! ; Game Over；游戏结束 ";
        assertTrue(HypixelModule.matchesCustomGgTrigger("*** Victory! ***", triggers));
        assertTrue(HypixelModule.matchesCustomGgTrigger("Game Over - scores", triggers));
        assertTrue(HypixelModule.matchesCustomGgTrigger("提示：游戏结束", triggers));
        assertFalse(HypixelModule.matchesCustomGgTrigger("victory!", triggers));
        assertFalse(HypixelModule.matchesCustomGgTrigger("Game starts soon", triggers));
    }

    public void testEmptyEntriesCannotMatchAllMessages() {
        for (String triggers : new String[]{"", " ", "; ;；\r\n", "; Victory! ;"}) {
            assertFalse(HypixelModule.matchesCustomGgTrigger("Game starts soon", triggers));
        }
    }

    public void testDefaultToggleAndOfflineCustomMatching() throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        HypixelModule module = (HypixelModule)((Unsafe)field.get(null)).allocateInstance(HypixelModule.class);
        module.autoGgCustomTriggers = new Setting("custom");
        module.autoGgCustomTriggers.recoveredField3086 = "Custom end";
        module.autoGgUseDefaultTriggers = new Setting("defaults");
        module.autoGgUseDefaultTriggers.recoveredField3086 = true;
        module.recoveredField443 = Arrays.asList("Default end");
        assertTrue(module.matchesAutoGgTrigger("Default end"));
        assertTrue(module.matchesAutoGgTrigger("Custom end"));
        module.autoGgUseDefaultTriggers.recoveredField3086 = false;
        assertFalse(module.matchesAutoGgTrigger("Default end"));
        assertTrue(module.matchesAutoGgTrigger("Custom end"));
        module.recoveredField443 = new ArrayList<>();
        module.autoGgUseDefaultTriggers.recoveredField3086 = true;
        assertTrue(module.matchesAutoGgTrigger("Custom end"));
        module.autoGgCustomTriggers.recoveredField3086 = "";
        assertFalse(module.matchesAutoGgTrigger("Anything"));
    }

    public void testTextEditorKeepsLongTriggerListOnReopen() {
        Setting setting = new Setting("triggers").setMaxStringLength(512);
        net.minecraft.client.gui.GuiTextField input = new net.minecraft.client.gui.GuiTextField(0, null, 0, 0, 140, 10);
        input.setMaxStringLength(setting.getMaxStringLength());
        StringBuilder triggers = new StringBuilder();
        for (int i = 0; i < 20; i++) triggers.append("Game end ").append(i).append(';');
        input.setText(triggers.toString());
        assertEquals(triggers.toString(), input.getText());
        assertEquals(64, new Setting("other").getMaxStringLength());
    }
}
