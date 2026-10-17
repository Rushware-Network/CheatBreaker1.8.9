package recovery;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.ModuleManager;
import com.cheatbreaker.client.module.type.ChatModule;
import com.cheatbreaker.client.util.chat.ClientChatLine;
import java.lang.reflect.Field;
import junit.framework.TestCase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import sun.misc.Unsafe;

public class UnlimitedChatTest extends TestCase {
    private CheatBreaker previousClient;
    private Minecraft previousMinecraft;
    private ChatModule module;
    private GuiNewChat chat;

    public static class TestFont extends FontRenderer {
        public TestFont() { super(null, null, null, false); }
        @Override public int getStringWidth(String text) {
            return EnumChatFormatting.getTextWithoutFormattingCodes(text).length();
        }
        @Override public String trimStringToWidth(String text, int width, boolean reverse) {
            String plain = EnumChatFormatting.getTextWithoutFormattingCodes(text);
            return plain.substring(0, Math.min(width, plain.length()));
        }
    }

    private static <T> T allocate(Class<T> type) throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return type.cast(((Unsafe)field.get(null)).allocateInstance(type));
    }

    @Override public void setUp() {
        previousClient = CheatBreaker.instance;
        previousMinecraft = Minecraft.theMinecraft;
        try {
            Minecraft mc = allocate(Minecraft.class);
            Minecraft.theMinecraft = mc;
            mc.gameSettings = allocate(GameSettings.class);
            mc.gameSettings.chatColours = true;
            mc.gameSettings.chatScale = 1.0F;
            mc.gameSettings.chatWidth = 1.0F;
            mc.fontRendererObj = allocate(TestFont.class);
            CheatBreaker.instance = allocate(CheatBreaker.class);
            CheatBreaker.instance.moduleManager = allocate(ModuleManager.class);
            module = allocate(ChatModule.class);
            module.enabled = true;
            module.recoveredField828 = new Setting("Unlimited Chat");
            module.recoveredField828.recoveredField3086 = true;
            CheatBreaker.instance.moduleManager.chatModule = module;
            chat = new GuiNewChat(mc);
        } catch (Exception error) {
            tearDown();
            throw new IllegalStateException(error);
        }
    }

    @Override public void tearDown() {
        CheatBreaker.instance = previousClient;
        Minecraft.theMinecraft = previousMinecraft;
    }

    private void addMessages(int count) {
        for (int i = 0; i < count; i++) {
            chat.setChatLine(new ChatComponentText("message " + i), 0, i, false);
        }
    }

    public void testUnlimitedHistorySurvivesRefreshAndScroll() {
        addMessages(250);
        assertEquals(250, chat.chatLines.size());
        assertEquals(250, chat.drawnChatLines.size());
        chat.scroll(200);
        assertEquals(200, chat.scrollPos);
        chat.refreshChat();
        assertEquals(0, chat.scrollPos);
        assertEquals(250, chat.chatLines.size());
        assertEquals(250, chat.drawnChatLines.size());
        assertEquals("message 0", chat.drawnChatLines.get(249).getChatComponent().getUnformattedText());
    }

    public void testDisabledSettingKeepsNewestHundred() {
        module.recoveredField828.recoveredField3086 = false;
        addMessages(250);
        assertEquals(100, chat.chatLines.size());
        assertEquals(100, chat.drawnChatLines.size());
        assertEquals("message 150", chat.chatLines.get(99).getChatComponent().getUnformattedText());
    }

    public void testDisabledModuleKeepsVanillaLimit() {
        module.enabled = false;
        addMessages(250);
        assertEquals(100, chat.chatLines.size());
        assertEquals(100, chat.drawnChatLines.size());
    }

    public void testSwitchOffTrimsOnNextMessage() {
        addMessages(250);
        module.recoveredField828.recoveredField3086 = false;
        chat.setChatLine(new ChatComponentText("latest"), 0, 251, false);
        assertEquals(100, chat.chatLines.size());
        assertEquals(100, chat.drawnChatLines.size());
        assertEquals("latest", chat.chatLines.get(0).getChatComponent().getUnformattedText());
    }

    public void testTranslationAndClientLinesSurviveRefreshAndReplacement() {
        ChatComponentText client = new ChatComponentText("first\nsecond");
        client.recoveredField3553 = true;
        chat.setChatLine(client, 7, 1, false);
        chat.setChatLine(new ChatComponentTranslation("chat.type.text", "Player", "hello"), 0, 2, false);
        assertEquals(2, chat.chatLines.size());
        assertEquals(3, chat.drawnChatLines.size());
        assertEquals("second", chat.drawnChatLines.get(1).getChatComponent().getUnformattedText());
        assertTrue(((ClientChatLine)chat.drawnChatLines.get(1)).method_28277());
        chat.refreshChat();
        assertEquals(2, chat.chatLines.size());
        assertEquals(3, chat.drawnChatLines.size());
        chat.setChatLine(new ChatComponentText("replacement"), 7, 3, false);
        assertEquals(2, chat.chatLines.size());
        assertEquals(2, chat.drawnChatLines.size());
        chat.deleteChatLine(7);
        assertEquals(1, chat.chatLines.size());
        assertEquals(1, chat.drawnChatLines.size());
        chat.clearChatMessages();
        assertTrue(chat.chatLines.isEmpty());
        assertTrue(chat.drawnChatLines.isEmpty());
    }
}
