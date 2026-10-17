package recovery;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.ConfigManager;
import com.cheatbreaker.client.config.GlobalSettings;
import com.cheatbreaker.client.config.Setting;
import java.lang.reflect.Field;
import junit.framework.TestCase;
import com.cheatbreaker.client.util.render.TextureAnimationTicker;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import org.apache.logging.log4j.LogManager;
import sun.misc.Unsafe;

public class TextureAnimationTickerTest extends TestCase {
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
            CheatBreaker.instance.recoveredField1579 = LogManager.getLogger("TextureAnimationTickerTest");
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

    private static class Counter implements ITickable {
        int count;
        @Override public void tick() { count++; }
    }

    public void testSliderDefaultsRangeAndMasterVisibility() {
        Setting slider = global.lowerAnimationTicks;
        assertTrue(global.recoveredField571.contains(slider));
        assertEquals("Lower Animation Ticks", slider.method_08911());
        assertEquals(Setting.Type.INTEGER, slider.getType());
        assertEquals(20, slider.method_08912());
        assertEquals(0, ((Number)slider.method_08904()).intValue());
        assertEquals(20, ((Number)slider.method_08878()).intValue());
        assertTrue(slider.method_08921().getAsBoolean());
        global.recoveredField563.setValue(false, false);
        assertFalse(slider.method_08921().getAsBoolean());
    }

    public void testEveryRateIsEvenlyDistributedAcrossTextureManagerTickables() {
        for (int rate = 0; rate <= 20; rate++) {
            global.lowerAnimationTicks.setValue(rate, false);
            TextureAnimationTicker ticker = new TextureAnimationTicker();
            TextureManager textures = new TextureManager(null);
            Counter atlas = new Counter();
            Counter custom = new Counter();
            textures.listTickables.add(atlas);
            textures.listTickables.add(custom);
            for (int tick = 1; tick <= 200; tick++) {
                ticker.tick(textures);
                assertEquals("Rate " + rate + ", game tick " + tick, tick * rate / 20, atlas.count);
                assertEquals(atlas.count, custom.count);
            }
        }
    }

    public void testFreezeResumeAndRateChangesHaveNoCatchUpBurst() {
        TextureAnimationTicker ticker = new TextureAnimationTicker();
        Counter counter = new Counter();
        global.lowerAnimationTicks.setValue(1, false);
        for (int tick = 0; tick < 19; tick++) ticker.tick(counter);
        assertEquals(0, counter.count);
        global.lowerAnimationTicks.setValue(0, false);
        for (int tick = 0; tick < 200; tick++) ticker.tick(counter);
        assertEquals(0, counter.count);
        global.lowerAnimationTicks.setValue(1, false);
        for (int tick = 0; tick < 19; tick++) ticker.tick(counter);
        assertEquals(0, counter.count);
        ticker.tick(counter);
        assertEquals(1, counter.count);
        global.lowerAnimationTicks.setValue(20, false);
        ticker.tick(counter);
        assertEquals(2, counter.count);
    }

    public void testMasterOffAndStartupKeepVanillaRate() {
        TextureAnimationTicker ticker = new TextureAnimationTicker();
        Counter counter = new Counter();
        global.lowerAnimationTicks.setValue(0, false);
        global.recoveredField563.setValue(false, false);
        for (int tick = 0; tick < 20; tick++) ticker.tick(counter);
        assertEquals(20, counter.count);
        global.recoveredField563.setValue(true, false);
        ticker.tick(counter);
        assertEquals(20, counter.count);
        CheatBreaker.instance = null;
        ticker.tick(counter);
        assertEquals(21, counter.count);
        CheatBreaker.instance = previousClient;
    }
}
