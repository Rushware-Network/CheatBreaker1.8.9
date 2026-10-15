package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.ModuleManager;
import java.lang.reflect.Field;
import junit.framework.TestCase;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import sun.misc.Unsafe;

public class TabListModuleTest extends TestCase {
   private CheatBreaker previous;
   private RecordingTabList module;

   private static <T> T allocate(Class<T> type) throws Exception {
      Field field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      return type.cast(((Unsafe)field.get(null)).allocateInstance(type));
   }

   private Setting value(Object value) throws Exception {
      Setting setting = allocate(Setting.class);
      setting.recoveredField3086 = value;
      return setting;
   }

   public void setUp() {
      try {
         initialize();
      } catch (Exception failure) {
         throw new RuntimeException(failure);
      }
   }

   private void initialize() throws Exception {
      previous = CheatBreaker.instance;
      CheatBreaker client = allocate(CheatBreaker.class);
      client.moduleManager = allocate(ModuleManager.class);
      module = allocate(RecordingTabList.class);
      client.moduleManager.tabList = module;
      CheatBreaker.instance = client;
   }

   public void tearDown() {
      CheatBreaker.instance = previous;
   }

   public void testLiveOverlayDelegatesOnceAndRespectsHudToggle() throws Exception {
      GuiPlayerTabOverlay overlay = allocate(GuiPlayerTabOverlay.class);
      module.enabled = true;
      module.recoveredField3899 = true;
      Scoreboard scoreboard = new Scoreboard();
      overlay.renderPlayerlist(640, scoreboard, null);
      assertEquals(1, module.calls);
      assertEquals(640, module.width);
      assertSame(scoreboard, module.scoreboard);
      module.recoveredField3899 = false;
      overlay.renderPlayerlist(640, scoreboard, null);
      assertEquals(1, module.calls);
   }

   public void testColumnsDoNotOverlapAndKeepVanillaPlayerLimit() {
      TabListLayout layout = new TabListLayout(80, 120, 640);
      assertEquals(4, layout.columns);
      assertEquals(20, layout.rows);
      assertEquals(0, layout.columnX(19));
      assertEquals(125, layout.columnX(20));
      assertEquals(0, layout.rowY(20));
      assertEquals(171, layout.rowY(19));
      assertEquals(495, layout.width);
      assertEquals(4, new TabListLayout(200, 120, 640).columns);
      TabListLayout uneven = new TabListLayout(21, 120, 200);
      assertEquals(2, uneven.columns);
      assertEquals(11, uneven.rows);
      assertTrue(uneven.width <= 200);
   }

   public void testEmptyAndNarrowLayoutsHaveNoNegativeSizes() {
      TabListLayout empty = new TabListLayout(0, 0, 0);
      assertEquals(0, empty.rows);
      assertEquals(0, empty.columnX(0));
      assertEquals(0, empty.rowY(0));
      assertTrue(empty.width > 0);
      assertTrue(new TabListLayout(80, 300, 100).width <= 100);
   }

   public void testPingThresholdsStaticColorAndUnknownResponse() throws Exception {
      module.recoveredField1216 = value(true);
      module.recoveredField1223 = value(0xFF00FF00);
      module.recoveredField1218 = value(0xFFFFFF00);
      module.recoveredField1217 = value(0xFFFF8800);
      module.recoveredField1208 = value(0xFFFF0000);
      module.recoveredField1220 = value(0xFF123456);
      assertEquals(0xFF00FF00, module.getPingColor(149));
      assertEquals(0xFFFFFF00, module.getPingColor(150));
      assertEquals(0xFFFF8800, module.getPingColor(300));
      assertEquals(0xFFFF0000, module.getPingColor(600));
      assertEquals(0xFFFF0000, module.getPingColor(-1));
      assertEquals("?", TabListModule.pingText(-1));
      assertEquals("150", TabListModule.pingText(150));
      module.recoveredField1216.recoveredField3086 = false;
      assertEquals(0xFF123456, module.getPingColor(20));
   }

   public void testHighlightPriorityAndPingRowOpacity() throws Exception {
      module.recoveredField1228 = value(true);
      module.recoveredField1206 = value(true);
      module.recoveredField1230 = value(0x40880000);
      module.recoveredField1212 = value(0x40008800);
      module.recoveredField1211 = value(0x20000000);
      module.recoveredField1207 = value(true);
      module.recoveredField1216 = value(false);
      module.recoveredField1220 = value(0xFF123456);
      assertEquals(0x40880000, module.getRowColor(true, true, 10));
      assertEquals(0x40008800, module.getRowColor(false, true, 10));
      assertEquals(0x20123456, module.getRowColor(false, false, 10));
      module.recoveredField1215 = value(true);
      module.recoveredField1222 = value(true);
      module.recoveredField1231 = value(0xFFFF0000);
      module.recoveredField1229 = value(0xFF00FF00);
      assertEquals(0xFFFF0000, module.getNameColor(true, true, false));
      assertEquals(0xFF00FF00, module.getNameColor(false, true, false));
      assertEquals(-1862270977, module.getNameColor(false, false, true));
   }

   public static class RecordingTabList extends TabListModule {
      public int calls;
      public int width;
      public Scoreboard scoreboard;

      @Override
      public void method_20823(int width, Scoreboard scoreboard, ScoreObjective objective, int x, int y) {
         calls++;
         this.width = width;
         this.scoreboard = scoreboard;
      }
   }
}
