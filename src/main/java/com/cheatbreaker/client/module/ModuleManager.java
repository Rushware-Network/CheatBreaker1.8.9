package com.cheatbreaker.client.module;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.event.EventBus;
import com.cheatbreaker.client.event.type.DisconnectEvent;
import com.cheatbreaker.client.event.type.LoadWorldEvent;
import com.cheatbreaker.client.module.staff.NametagsModule;
import com.cheatbreaker.client.module.staff.StaffModule;
import com.cheatbreaker.client.module.staff.TrimpModule;
import com.cheatbreaker.client.module.staff.XRayModule;
import com.cheatbreaker.client.module.type.AnimationsModule;
import com.cheatbreaker.client.module.type.HurtcamModule;
import com.cheatbreaker.client.module.type.AutoTextModule;
import com.cheatbreaker.client.module.type.TabListModule;
import com.cheatbreaker.client.module.type.BlockOverlayModule;
import com.cheatbreaker.client.module.type.BarrierBlockModule;
import com.cheatbreaker.client.module.type.BossBarModule;
import com.cheatbreaker.client.module.type.CPSModule;
import com.cheatbreaker.client.module.type.ChatModule;
import com.cheatbreaker.client.module.type.ClockModule;
import com.cheatbreaker.client.module.type.ComboCounterModule;
import com.cheatbreaker.client.module.type.CoordinatesModule;
import com.cheatbreaker.client.module.type.CrosshairModule;
import com.cheatbreaker.client.module.type.DamageTintModule;
import com.cheatbreaker.client.module.type.DirectionHudModule;
import com.cheatbreaker.client.module.type.EnchantmentGlintModule;
import com.cheatbreaker.client.module.type.EnvironmentModule;
import com.cheatbreaker.client.module.type.FPSModule;
import com.cheatbreaker.client.module.type.HitboxesModule;
import com.cheatbreaker.client.module.type.HypixelModule;
import com.cheatbreaker.client.module.type.MemoryUsageModule;
import com.cheatbreaker.client.module.type.MiniMapModule;
import com.cheatbreaker.client.module.type.MotionBlurModule;
import com.cheatbreaker.client.module.type.NametagModule;
import com.cheatbreaker.client.module.type.NickHiderModule;
import com.cheatbreaker.client.module.type.PackDisplayModule;
import com.cheatbreaker.client.module.type.ParticlesModule;
import com.cheatbreaker.client.module.type.PerspectiveModule;
import com.cheatbreaker.client.module.type.PingModule;
import com.cheatbreaker.client.module.type.PotionCounterModule;
import com.cheatbreaker.client.module.type.PotionStatusModule;
import com.cheatbreaker.client.module.type.ReachDisplayModule;
import com.cheatbreaker.client.module.type.SaturationModule;
import com.cheatbreaker.client.module.type.ScoreboardModule;
import com.cheatbreaker.client.module.type.ServerAddressModule;
import com.cheatbreaker.client.module.type.SprintResetCounterModule;
import com.cheatbreaker.client.module.type.TNTTimerModule;
import com.cheatbreaker.client.module.type.TeammatesModule;
import com.cheatbreaker.client.module.type.TextureOptionsModule;
import com.cheatbreaker.client.module.type.ToggleSprintModule;
import com.cheatbreaker.client.module.type.armourstatus.ArmourStatusModule;
import com.cheatbreaker.client.module.type.cooldowns.CooldownsModule;
import com.cheatbreaker.client.module.type.keystrokes.KeystrokesModule;
import com.cheatbreaker.client.module.type.notifications.CBNotificationsModule;
import com.cheatbreaker.client.ui.element.type.custom.KeybindElement;
import com.cheatbreaker.client.util.voicechat.VoiceChat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.EnumChatFormatting;
import com.cheatbreaker.client.event.type.KeyPressEvent;
import com.cheatbreaker.client.util.input.KeyBindingResumeListener;
import com.cheatbreaker.client.util.render.PerspectiveController;
import com.cheatbreaker.client.util.server.HypixelAutoTipTask;
import com.cheatbreaker.client.util.render.FreelookController;
import com.cheatbreaker.client.module.staff.NoClipModule;
import com.cheatbreaker.client.event.type.WorldChangeEvent;
import com.cheatbreaker.client.util.ClientStartupListener;
import com.cheatbreaker.client.util.server.LocalServerRestrictions;

public class ModuleManager {
   public CBNotificationsModule notifications;
   public FPSModule fpsModule;
   public String recoveredField1695;
   public XRayModule xray;
   public BlockOverlayModule recoveredField1696;
   public BarrierBlockModule barrierBlock;
   public ComboCounterModule recoveredField1697;
   public TeammatesModule teammatesModule;
   public KeyBindingResumeListener recoveredField1698;
   public PotionCounterModule recoveredField1699;
   public ReachDisplayModule recoveredField1700;
   public AutoTextModule recoveredField1701;
   public MiniMapModule minmap;
   public PotionStatusModule potionStatus;
   public DirectionHudModule directionHud;
   public CooldownsModule cooldowns;
   public PerspectiveController recoveredField1702;
   public PackDisplayModule packDisplayModule;
   public HitboxesModule recoveredField1703;
   public ServerAddressModule recoveredField1704;
   public NickHiderModule recoveredField1705;
   public List<AbstractModule> recoveredField1706 = new ArrayList<>();
   public SprintResetCounterModule recoveredField1707;
   public BossBarModule recoveredField1708;
   public KeystrokesModule keyStrokes;
   public CoordinatesModule coordinatesModule;
   public LocalServerRestrictions recoveredField1709;
   public SaturationModule saturationModule;
   public MemoryUsageModule recoveredField1710;
   public ArmourStatusModule armourStatus;
   public TrimpModule recoveredField1711;
   public NametagsModule recoveredField1712;
   public NametagModule recoveredField1713;
   public VoiceChat voiceChat;
   public EnchantmentGlintModule recoveredField1714;
   public ScoreboardModule scoreboard;
   public MotionBlurModule recoveredField1715;
   public ToggleSprintModule recoveredField1716;
   public AnimationsModule recoveredField1717;
   public PingModule recoveredField1718;
   public TabListModule tabList;
   public ChatModule chatModule;
   public HypixelModule recoveredField1719;
   public Map<Setting, KeybindElement> recoveredField1720;
   public CrosshairModule recoveredField1721;
   public TNTTimerModule recoveredField1722;
   public PerspectiveModule recoveredField1723;
   public HurtcamModule hurtcam;
   public int recoveredField1724;
   public List<StaffModule> recoveredField1725 = new ArrayList<>();
   public EnvironmentModule recoveredField1726;
   public ClockModule clockModule;
   public TextureOptionsModule recoveredField1727;
   public CPSModule cpsModule;
   public FreelookController recoveredField1728;
   public NoClipModule recoveredField1729;
   public ParticlesModule recoveredField1730;
   public DamageTintModule recoveredField1731;

   public ModuleManager(EventBus var1) {
      this.recoveredField1720 = new HashMap<>();
      CheatBreaker.getInstance().recoveredField1579.info(CheatBreaker.getInstance().recoveredField1553 + "Created Mod Manager");
      this.recoveredField1706.add(this.coordinatesModule = new CoordinatesModule());
      this.recoveredField1706.add(this.recoveredField1717 = new AnimationsModule());
      this.recoveredField1706.add(this.minmap = new MiniMapModule());
      this.recoveredField1706.add(this.recoveredField1716 = new ToggleSprintModule());
      this.recoveredField1706.add(this.potionStatus = new PotionStatusModule());
      this.recoveredField1706.add(this.armourStatus = new ArmourStatusModule());
      this.recoveredField1706.add(this.keyStrokes = new KeystrokesModule());
      this.recoveredField1706.add(this.scoreboard = new ScoreboardModule());
      this.recoveredField1706.add(this.chatModule = new ChatModule());
      this.recoveredField1706.add(this.recoveredField1708 = new BossBarModule());
      this.recoveredField1706.add(this.cooldowns = new CooldownsModule());
      this.recoveredField1706.add(this.notifications = new CBNotificationsModule());
      this.recoveredField1706.add(this.directionHud = new DirectionHudModule());
      this.recoveredField1706.add(this.recoveredField1726 = new EnvironmentModule());
      this.recoveredField1706.add(this.recoveredField1714 = new EnchantmentGlintModule());
      this.recoveredField1706.add(this.recoveredField1721 = new CrosshairModule());
      this.recoveredField1706.add(this.recoveredField1731 = new DamageTintModule());
      this.recoveredField1706.add(this.recoveredField1730 = new ParticlesModule());
      this.recoveredField1706.add(this.recoveredField1713 = new NametagModule());
      this.recoveredField1706.add(this.recoveredField1705 = new NickHiderModule());
      this.recoveredField1706.add(this.recoveredField1701 = new AutoTextModule());
      this.recoveredField1706.add(this.recoveredField1722 = new TNTTimerModule());
      this.recoveredField1706.add(this.recoveredField1696 = new BlockOverlayModule());
      this.recoveredField1706.add(this.barrierBlock = new BarrierBlockModule());
      this.recoveredField1706.add(this.recoveredField1727 = new TextureOptionsModule());
      this.recoveredField1706.add(this.recoveredField1723 = new PerspectiveModule());
      this.recoveredField1706.add(this.hurtcam = new HurtcamModule());
      this.recoveredField1706.add(this.recoveredField1703 = new HitboxesModule());
      this.recoveredField1706.add(this.recoveredField1719 = new HypixelModule());
      new Timer().scheduleAtFixedRate(new HypixelAutoTipTask(), TimeUnit.SECONDS.toMillis(15L), TimeUnit.MINUTES.toMillis(1L));
      this.recoveredField1706.add(this.cpsModule = new CPSModule());
      this.recoveredField1706.add(this.fpsModule = new FPSModule());
      this.recoveredField1706.add(this.recoveredField1710 = new MemoryUsageModule());
      this.recoveredField1706.add(this.recoveredField1699 = new PotionCounterModule());
      this.recoveredField1706.add(this.recoveredField1697 = new ComboCounterModule());
      this.recoveredField1706.add(this.recoveredField1707 = new SprintResetCounterModule());
      this.recoveredField1706.add(this.recoveredField1700 = new ReachDisplayModule());
      this.recoveredField1706.add(this.recoveredField1704 = new ServerAddressModule());
      this.recoveredField1706.add(this.recoveredField1718 = new PingModule());
      this.recoveredField1706.add(this.tabList = new TabListModule());
      this.recoveredField1706.add(this.clockModule = new ClockModule());
      this.recoveredField1706.add(this.saturationModule = new SaturationModule());
      this.recoveredField1706.add(this.packDisplayModule = new PackDisplayModule());
      this.recoveredField1706.add(this.recoveredField1715 = new MotionBlurModule());
      this.recoveredField1725.add(this.xray = new XRayModule());
      this.recoveredField1725.add(this.recoveredField1712 = new NametagsModule());
      this.recoveredField1725.add(this.recoveredField1729 = new NoClipModule());
      this.recoveredField1725.add(this.recoveredField1711 = new TrimpModule());
      this.voiceChat = new VoiceChat();
      this.recoveredField1728 = new FreelookController();
      this.recoveredField1702 = new PerspectiveController();
      this.teammatesModule = new TeammatesModule();
      this.recoveredField1698 = new KeyBindingResumeListener();
      this.teammatesModule.method_01340(true);
      new ClientStartupListener();
      this.recoveredField1709 = new LocalServerRestrictions();
      this.recoveredField1709.method_29289();
      var1.method_21938(LoadWorldEvent.class, this::method_21673);
      var1.method_21938(KeyPressEvent.class, this::method_21667);
      var1.method_21938(WorldChangeEvent.class, var1x -> {
         ServerData var2 = Minecraft.getMinecraft().currentServerData;
         if (var2 != null) {
            CheatBreaker.getInstance().method_19779(var2.serverIP, var2.recoveredField3393, var2.recoveredField3392);

            for (AbstractModule var4 : this.recoveredField1706) {
               var4.method_28862(Arrays.toString((Object[])var4.method_28774()).contains(var2.serverIP));
            }
         }
      });
      var1.method_21938(DisconnectEvent.class, var1x -> {
         CheatBreaker.getInstance().method_19779("", "", 0);

         for (AbstractModule var3 : this.recoveredField1725) {
            var3.setState(false);
            var3.setStaffModuleEnabled(false);
         }

         for (AbstractModule var5 : this.recoveredField1706) {
            var5.method_28862(false);
         }
      });
   }

   public AbstractModule method_21669(String var1) {
      return this.recoveredField1706.stream().filter(var1x -> var1x.getName().equalsIgnoreCase(var1)).findFirst().orElse(null);
   }

   public boolean method_21668(Setting var1, int var2) {
      boolean var3 = false;

      for (Setting var5 : this.recoveredField1720.keySet()) {
         if (var5.getType() == Setting.Type.INTEGER) {
            if (var5.method_08867() && var2 == var5.method_08877()) {
               var3 = true;
            }

            if (var5.method_08879() && var2 == var5.method_08912()) {
               var3 = true;
            }

            if ((var5.method_08911().toLowerCase().startsWith("hot key") || var5.method_08911().toLowerCase().endsWith("keybind"))
               && var2 == var5.method_08912()) {
               var3 = true;
            }
         }
      }

      if (var3) {
         CheatBreaker.getInstance()
            .getModuleManager()
            .notifications
            .queueNotification("error", EnumChatFormatting.RED + var1.method_08911() + " is already bound to another keybind!", 5000L);
      }

      return var3;
   }

   public void method_21673(LoadWorldEvent var1) {
      if (this.recoveredField1726.isEnabled()) {
         this.recoveredField1726.method_20137();
      }
   }

   public void method_21666(int var1) {
      this.recoveredField1724 = var1;
   }

   public void method_21667(KeyPressEvent var1) {
      if (var1.method_05523() != 0) {
         for (StaffModule var3 : this.recoveredField1725) {
            if (var3.isStaffEnabledModule() && (Integer)var3.getKeybindSetting().getValue() == var1.method_05523()) {
               var3.setState(!var3.isEnabled());
            }
         }
      }
   }

   public void method_21674(String var1) {
      this.recoveredField1695 = var1;
   }
}
