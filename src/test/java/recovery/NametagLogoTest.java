package recovery;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.ConfigManager;
import com.cheatbreaker.client.event.EventBus;
import com.cheatbreaker.client.module.type.NametagModule;
import com.cheatbreaker.client.util.UuidParser;
import com.cheatbreaker.client.util.player.PlayerNametagStyle;
import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import junit.framework.TestCase;
import sun.misc.Unsafe;

public class NametagLogoTest extends TestCase {
    private CheatBreaker previous;
    private NametagModule module;
    private UuidParser styles;
    private final UUID playerId = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private static <T> T allocate(Class<T> type) throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return type.cast(((Unsafe) field.get(null)).allocateInstance(type));
    }

    @Override public void setUp() {
        previous = CheatBreaker.instance;
        try {
            CheatBreaker.instance = allocate(CheatBreaker.class);
            CheatBreaker.instance.configManager = allocate(ConfigManager.class);
            EventBus bus = allocate(EventBus.class);
            bus.recoveredField2205 = new ConcurrentHashMap<>();
            CheatBreaker.instance.recoveredField1568 = bus;
            module = new NametagModule();
            styles = new UuidParser();
        } catch (Exception error) {
            CheatBreaker.instance = previous;
            throw new IllegalStateException(error);
        }
    }

    @Override public void tearDown() { CheatBreaker.instance = previous; }

    public void testOwnLogoWorksWithoutServiceAndDoesNotRegisterRemoteIdentity() {
        PlayerNametagStyle style = module.getLogoStyle(playerId, "Steve", "Steve", true, null, styles);
        assertNotNull(style);
        assertEquals(2, style.method_26419().length);
        assertEquals(0xFFFFFF, style.method_26419()[0]);
        assertEquals(0xFFFFFF, style.method_26419()[1]);
        assertEquals("", style.method_26422());
        assertTrue(styles.method_21030().isEmpty());
        assertNotNull(module.getLogoStyle(playerId, "Steve", "Steve", true, null, null));
        assertNull(module.getLogoStyle(playerId, "Steve", "Steve", false, null, styles));
    }

    public void testLogoAndModuleTogglesBothHideLogo() {
        module.recoveredField2922.setValue(false, false);
        assertNull(module.getLogoStyle(playerId, "Steve", "Steve", true, null, styles));
        module.recoveredField2922.setValue(true, false);
        module.setDefaultState(false);
        assertNull(module.getLogoStyle(playerId, "Steve", "Steve", true, null, styles));
    }

    public void testRemoteStyleRequiresMatchingUuidAndPreservesColors() {
        PlayerNametagStyle remote = new PlayerNametagStyle("OldName", "youtube", new int[]{0xFF0000, 0x0000FF});
        styles.method_21030().put(playerId, remote);
        assertSame(remote, module.getLogoStyle(playerId, "Steve", "[VIP] Steve", false, null, styles));
        assertSame(remote, module.getLogoStyle(playerId, "Steve", "Steve", true, null, styles));
        assertNull(module.getLogoStyle(UUID.randomUUID(), "Steve", "Steve", false, null, styles));
    }

    public void testFormattedNamesAndLocalNickHiderAlias() {
        assertNotNull(module.getLogoStyle(playerId, "Steve", "\u00a76[VIP] \u00a7aSteve", true, null, styles));
        assertNotNull(module.getLogoStyle(playerId, "Steve", "[VIP] You", true, "You", styles));
        assertNotNull(module.getLogoStyle(playerId, "Steve", "Alias.*", true, "Alias.*", styles));
        styles.method_21030().put(playerId, new PlayerNametagStyle("Steve", "", new int[]{1, 2}));
        assertNull(module.getLogoStyle(playerId, "Steve", "You", false, "You", styles));
    }

    public void testScoreLabelsAndPartialNamesNeverReceiveLogo() {
        for (String label : new String[]{"20 Health", "Steve2", "NotSteve", "Steve_", "", "[VIP] Alex"}) {
            assertNull(label, module.getLogoStyle(playerId, "Steve", label, true, null, styles));
        }
        assertNull(module.getLogoStyle(playerId, "Steve", null, true, null, styles));
        assertNull(module.getLogoStyle(playerId, "Steve", "20 Health", true, "", styles));
    }
}
