package recovery;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.type.HypixelModule;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import junit.framework.TestCase;
import sun.misc.Unsafe;

public class AutoGgTriggersTest extends TestCase {
    public static class RecordingModule extends HypixelModule {
        String sent;
        int sends;
        float delay;

        @Override public void method_25694(String message, float seconds) {
            sent = message;
            sends++;
            delay = seconds;
        }
    }

    public void testChatEventRoutesAutoGgByServer() throws Exception {
        RecordingModule module = allocate(RecordingModule.class);
        module.minecraft = allocate(net.minecraft.client.Minecraft.class);
        module.autoGgCustomTriggers = testSetting("custom", "Iron Sword");
        module.autoGgUseDefaultTriggers = testSetting("defaults", true);
        module.recoveredField443 = Arrays.asList("Victory!");
        module.recoveredField450 = testSetting("auto gg", true);
        module.recoveredField445 = testSetting("message", "gg");
        module.recoveredField449 = testSetting("delay", 0.5F);
        module.recoveredField439 = testSetting("hearts", false);
        module.recoveredField440 = testSetting("gl", false);
        module.recoveredField448 = testSetting("wb", false);
        module.recoveredField441 = testSetting("friend", false);
        module.recoveredField466 = new String[]{"You have been eliminated!", "Reward Summary"};
        com.cheatbreaker.client.event.type.ChatMessageEvent death =
            new com.cheatbreaker.client.event.type.ChatMessageEvent("JustTNT__ was knocked off a high place by ThanCoca's Iron Sword");
        module.minecraft.currentServerData = new net.minecraft.client.multiplayer.ServerData(false, "test", "localhost:25565", false);
        module.method_25691(death);
        assertEquals("gg", module.sent);
        assertEquals(1, module.sends);
        assertEquals(0.5F, module.delay);
        module.minecraft.currentServerData.serverIP = "MC.HYPIXEL.NET";
        module.method_25691(death);
        assertEquals("/achat gg", module.sent);
        assertEquals(2, module.sends);
        module.minecraft.currentServerData.serverIP = "localhost";
        module.recoveredField445.recoveredField3086 = "Good game!";
        module.method_25691(new com.cheatbreaker.client.event.type.ChatMessageEvent("Victory!"));
        assertEquals("Good game!", module.sent);
        assertEquals(3, module.sends);
        module.method_25691(new com.cheatbreaker.client.event.type.ChatMessageEvent("No trigger"));
        module.recoveredField450.recoveredField3086 = false;
        module.method_25691(death);
        module.recoveredField450.recoveredField3086 = true;
        module.minecraft.currentServerData = null;
        module.method_25691(death);
        assertEquals(3, module.sends);
    }

    private static Setting testSetting(String name, Object value) {
        Setting setting = new Setting(name);
        setting.recoveredField3086 = value;
        return setting;
    }

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
