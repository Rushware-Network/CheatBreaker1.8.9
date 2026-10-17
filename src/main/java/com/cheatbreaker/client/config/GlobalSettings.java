package com.cheatbreaker.client.config;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.element.module.ModuleListElement;
import com.cheatbreaker.client.ui.element.type.ColorPickerColorElement;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import com.cheatbreaker.client.util.dash.DashUtil;
import com.cheatbreaker.client.util.server.ServerRestrictionAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;
import com.cheatbreaker.client.ui.mainmenu.MainMenuMode;

public class GlobalSettings {
   public Setting friendListKeybind;
   public Setting clientUiScale;
   public KeyBinding recoveredField479;
   public Setting recoveredField480;
   public Setting recoveredField481;
   public KeyBinding recoveredField482;
   public Setting recoveredField483;
   public List<String[]> recoveredField484;
   public boolean recoveredField485;
   public Setting recoveredField486;
   public Setting rawMouseInput;
   public Setting preventImeSticking;
   public Setting recoveredField487;
   public Setting recoveredField488;
   public Setting recoveredField489;
   public Setting recoveredField490;
   public Setting recoveredField491;
   public Setting recoveredField492;
   public KeyBinding recoveredField493;
   public KeyBinding recoveredField494;
   public Setting recoveredField495;
   public Setting recoveredField496;
   public Setting recoveredField497;
   public KeyBinding recoveredField498;
   public Setting recoveredField499;
   public Setting recoveredField500;
   public Setting recoveredField501;
   public Setting recoveredField502;
   public Setting recoveredField503;
   public Setting recoveredField504;
   public Setting recoveredField505;
   public Setting recoveredField506;
   public List<ColorPickerColorElement> recoveredField507;
   public Setting recoveredField508;
   public Setting recoveredField509;
   public Setting recoveredField510;
   public Setting recoveredField511;
   public Setting recoveredField512;
   public Setting recoveredField513;
   public boolean recoveredField514;
   public Setting recoveredField515;
   public Setting recoveredField516;
   public Map<String[], String[]> recoveredField517;
   public Setting recoveredField518;
   public Setting recoveredField519;
   public Setting recoveredField520;
   public Setting recoveredField521;
   public Setting recoveredField522;
   public Setting recoveredField523;
   public Setting recoveredField524;
   public Setting recoveredField525;
   public Setting recoveredField526;
   public Setting recoveredField527;
   public Setting recoveredField528;
   public static Setting recoveredField529;
   public Setting recoveredField530;
   public Setting recoveredField531;
   public Setting recoveredField532;
   public Setting recoveredField533;
   public Setting recoveredField534;
   public Setting recoveredField535;
   public Setting recoveredField536;
   public String recoveredField537;
   public Setting recoveredField538;
   public Setting recoveredField539;
   public Setting recoveredField540;
   public Setting recoveredField541;
   public Setting recoveredField542;
   public Setting recoveredField543;
   public Setting recoveredField544;
   public Setting recoveredField545;
   public Setting recoveredField546;
   public Setting recoveredField547;
   public Setting recoveredField548;
   public Setting recoveredField549;
   public Setting recoveredField550;
   public int reconnectTime;
   public Setting recoveredField551;
   public Setting recoveredField552;
   public Setting recoveredField553;
   public Setting recoveredField554;
   public Setting recoveredField555;
   public Setting recoveredField556;
   public Setting recoveredField557;
   public Setting recoveredField558;
   public Setting recoveredField559;
   public Setting recoveredField560;
   public Setting recoveredField561;
   public Setting recoveredField562;
   public Setting recoveredField563;
   public Setting disableParticlePhysics;
   public Setting tileEntityDistance;
   public Setting recoveredField564;
   public Setting recoveredField565;
   public Setting recoveredField566;
   public Setting recoveredField567;
   public Setting recoveredField568;
   public Setting crosshairSettingsLabel;
   public Setting recoveredField569;
   public Setting recoveredField570;
   public List<Setting> recoveredField571 = new ArrayList<>();
   public Setting recoveredField572;
   public Setting recoveredField573;
   public Setting recoveredField574;
   public List<ColorPickerColorElement> recoveredField575;
   public Setting recoveredField576;
   public Setting recoveredField577;
   public KeyBinding recoveredField578;
   public Setting recoveredField579;
   public Setting recoveredField580;
   public Setting recoveredField581;
   public Setting recoveredField582;
   public Setting recoveredField583;
   public Setting recoveredField584;
   public Setting recoveredField585;
   public Setting recoveredField586;
   public Setting recoveredField587;
   public Setting recoveredField588;
   public Setting recoveredField589;
   public Setting recoveredField590;
   public Setting recoveredField591;
   public Setting recoveredField592;
   public Setting recoveredField593;
   public Setting recoveredField594;
   public Setting recoveredField595;
   public Setting recoveredField596;
   public Setting recoveredField598;
   public Setting recoveredField599;

   public Setting getCrosshairSettingsLabel() {
      return this.crosshairSettingsLabel;
   }

   public GlobalSettings() {
      this.recoveredField484 = new ArrayList<>();
      this.recoveredField517 = new HashMap<>();
      this.recoveredField514 = true;
      this.recoveredField485 = true;
      this.reconnectTime = 60;
      this.recoveredField537 = "http://server.noxiuam.gq/crashReport";
      this.recoveredField575 = new ArrayList<>();
      this.recoveredField507 = new ArrayList<>();
      CheatBreaker.getInstance().recoveredField1579.info(CheatBreaker.getInstance().recoveredField1553 + "Created settings");
      this.recoveredField501 = new Setting(this.recoveredField571, "label").setValue("Audio Settings");
      this.recoveredField549 = new Setting(this.recoveredField571, "Mute CheatBreaker sounds", "Mute notification sounds generated from CheatBreaker.")
         .setValue(false);
      this.recoveredField510 = new Setting(this.recoveredField571, "Pin Radio Player", "Pin the radio to the HUD.").setValue(false);
      this.recoveredField546 = new Setting(this.recoveredField571, "Radio Volume", "Adjusts the volume of the Dash Radio.").onChange(var0 -> {
         if (DashUtil.isPlayerNotNull()) {
            DashUtil.getDashPlayer().setFloatControlValue(Float.parseFloat(var0.toString()));
         }
      }).setValue(85).setMinMax(0, 100).method_08892("%").method_08915("MEDIUM");
      this.recoveredField523 = new Setting(this.recoveredField571, "label").setValue("FPS Boost");
      this.recoveredField563 = new Setting(this.recoveredField571, "Enable FPS Boost", "Enables all FPS boost settings in this category.").setValue(true);
      this.tileEntityDistance = new Setting(this.recoveredField571, "Tile Entity Distance",
            "Render tile entities within a percentage of their original distance. 100% preserves vanilla distances.")
         .setValue(100)
         .setMinMax(5, 100)
         .method_08892(".0%")
         .method_08915("MEDIUM")
         .method_08893("fps-inverse-52", "fps-52")
         .method_08894(this.recoveredField563::method_08908);
      this.disableParticlePhysics = new Setting(this.recoveredField571, "Disable Particle Physics",
            "Skip particle collisions with blocks to reduce CPU usage. Particles can pass through blocks.")
         .setValue(false)
         .method_08894(this.recoveredField563::method_08908);
      this.recoveredField580 = new Setting(this.recoveredField571, "Entity Shadows", "Draws a shadow below entities.")
         .setValue(Minecraft.getMinecraft().gameSettings.entityShadows)
         .method_08894(this.recoveredField563::method_08908)
         .onChange(value -> {
            GameSettings settings = Minecraft.getMinecraft().gameSettings;
            boolean enabled = (Boolean)value;
            if (settings.entityShadows != enabled) {
               settings.entityShadows = enabled;
               settings.saveOptions();
            }
         });
      this.recoveredField557 = new Setting(this.recoveredField571, "Hide Grounded Arrows").setValue(false).method_08894(this.recoveredField563::method_08908);
      this.recoveredField583 = new Setting(this.recoveredField571, "Hide Stuck Arrows").setValue(false).method_08894(this.recoveredField563::method_08908);
      this.recoveredField518 = new Setting(this.recoveredField571, "Hide Moving Arrows").setValue(false).method_08894(this.recoveredField563::method_08908);
      this.recoveredField532 = new Setting(this.recoveredField571, "Hide Foliage", "Hides things like tall grass, flowers, and shrubs")
         .setValue(false)
         .onChange(var0 -> CheatBreaker.getInstance().method_19774().renderGlobal.loadRenderers())
         .method_08894(this.recoveredField563::method_08908);
      this.recoveredField565 = new Setting(this.recoveredField571, "Hide Placed Skulls").setValue(false).method_08894(this.recoveredField563::method_08908);
      this.recoveredField513 = new Setting(this.recoveredField571, "label").setValue("FPS Limiting Settings");
      this.recoveredField562 = new Setting(
            this.recoveredField571,
            "Use Custom FPS Limiter",
            "Overrides the default FPS limiter with a custom one.\n§e§oWill not override when VSync is enabled."
         )
         .setValue(false);
      this.recoveredField596 = new Setting(this.recoveredField571, "Limit When Game is Unfocused", "Limits the FPS when you are not interacting with the game.")
         .setValue(false);
      this.recoveredField521 = new Setting(this.recoveredField571, "Limit In Pause Menu", "Limits the FPS when you are in the Pause Menu.").setValue(true);
      this.recoveredField528 = new Setting(this.recoveredField571, "Maximum FPS")
         .setMinMax(5, 1000)
         .setValue(390)
         .method_08892(" FPS")
         .method_08893("fps-inverse-52", "fps-52")
         .method_08894(() -> (Boolean)this.recoveredField562.getValue());
      this.recoveredField591 = new Setting(this.recoveredField571, "Unfocused FPS")
         .setMinMax(1, 390)
         .setValue(60)
         .method_08892(" FPS")
         .method_08893("fps-inverse-52", "fps-52")
         .method_08894(() -> (Boolean)this.recoveredField596.getValue());
      this.recoveredField539 = new Setting(this.recoveredField571, "Pause Menu FPS")
         .setMinMax(1, 390)
         .setValue(120)
         .method_08892(" FPS")
         .method_08893("fps-inverse-52", "fps-52")
         .method_08894(() -> (Boolean)this.recoveredField596.getValue());
      this.recoveredField535 = new Setting(this.recoveredField571, "Main Menu FPS")
         .setMinMax(30, 500)
         .setValue(90)
         .method_08892(" FPS")
         .method_08893("fps-inverse-52", "fps-52");
      this.recoveredField533 = new Setting(this.recoveredField571, "label").setValue("Team View Settings");
      this.recoveredField564 = new Setting(this.recoveredField571, "Enable Team View", "Enables the Team View.").setValue(true);
      this.recoveredField519 = new Setting(this.recoveredField571, "Show off-screen marker", "Show a marker above players.")
         .setValue(true)
         .method_08894(() -> (Boolean)this.recoveredField564.getValue());
      this.recoveredField598 = new Setting(this.recoveredField571, "Show distance", "Show the distance amount above a your teammates' heads.")
         .setValue(true)
         .method_08894(() -> (Boolean)this.recoveredField564.getValue());
      new Setting(this.recoveredField571, "label").setValue("General Settings");
      this.clientUiScale = new Setting(this.recoveredField571, "Client UI Scale", "Change the size of CheatBreaker menus.")
         .setValue("Normal").acceptedValues("Small", "Normal", "Large", "4x", "5x", "Auto");
      this.rawMouseInput = new Setting(this.recoveredField571, "Raw Mouse Input", "Use unaccelerated mouse movement on Windows.").setValue(true);
      this.preventImeSticking = new Setting(this.recoveredField571, "Prevent IME Input Sticking",
         "Enable Windows IME only while editing text to prevent blocked game keys.").setValue(true);
      // Keep the existing name so saved Fullbright preferences still load.
      this.recoveredField486 = new Setting(this.recoveredField571, "Fullbright", "Render the world at full brightness.").setValue(true);
      // Recover brightness from options saved by the previous gamma-changing implementation.
      GameSettings brightnessSettings = Minecraft.getMinecraft().gameSettings;
      if (brightnessSettings.gammaSetting >= 1000.0F) {
         brightnessSettings.gammaSetting = Math.max(0.0F, Math.min(1.0F, brightnessSettings.recoveredField2674));
      }
      this.crosshairSettingsLabel = new Setting(this.recoveredField571, "Current Menu", "Sets which main menu should show.\n\n0: 2017\n1: 2018\n2: 2016")
         .setMinMax(0, 2)
         .setValue(1);
      this.recoveredField560 = new Setting(this.recoveredField571, "label").setValue("Display Settings");
      this.recoveredField576 = new Setting(this.recoveredField571, "Borderless Fullscreen").setValue(false).method_08917(var0 -> {
         Minecraft.getMinecraft().method_20403();
         Minecraft.getMinecraft().method_20403();
         CBModulesGui var1x = new CBModulesGui();
         Minecraft.getMinecraft().displayGuiScreen(var1x);
         var1x.currentScrollableElement = var1x.recoveredField793;
         ((ModuleListElement)CBModulesGui.instance.recoveredField793).recoveredField1358 = true;
         CBModulesGui.instance.recoveredField793.recoveredField3012 = CheatBreaker.getInstance().getModuleManager().recoveredField1724;
      });
      this.recoveredField592 = new Setting(this.recoveredField571, "Unfullscreen when Unfocused").setValue(false);
      this.recoveredField558 = new Setting(this.recoveredField571, "label").setValue("Hud Editor Settings");
      this.recoveredField504 = new Setting(this.recoveredField571, "Mod List")
         .setValue("Normal")
         .acceptedValues("Normal", "Compact")
         .method_08914(SettingsDetailLevel.ADVANCED)
         .onChange(var0 -> {
            if (Minecraft.getMinecraft().currentScreen instanceof CBModulesGui) {
               CBModulesGui var1x = new CBModulesGui();
               Minecraft.getMinecraft().displayGuiScreen(var1x);
               var1x.currentScrollableElement = var1x.recoveredField801;
            }
         });
      recoveredField529 = new Setting(this.recoveredField571, "Dark Mode", "Switch between light and dark mode.").setValue(false);
      this.recoveredField584 = new Setting(this.recoveredField571, "Customization Level").setValue("Simple").acceptedValues("Simple", "Medium", "Advanced");
      this.recoveredField559 = new Setting(this.recoveredField571, "Show Mod Name (GUI)").setValue(true).method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField540 = new Setting(this.recoveredField571, "Snap mods to other mods (GUI)")
         .setValue(true)
         .method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField569 = new Setting(this.recoveredField571, "Snapping Strength")
         .setValue(2.0F)
         .setMinMax(1.0F, 10.0F)
         .method_08892("px")
         .method_08914(SettingsDetailLevel.MEDIUM)
         .method_08894(() -> (Boolean)this.recoveredField540.getValue());
      this.recoveredField506 = new Setting(this.recoveredField571, "label")
         .setValue("Mod Command Settings")
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField491 = new Setting(this.recoveredField571, "Enable Mod Commands").setValue(true).method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField490 = new Setting(this.recoveredField571, "Hide Callback Messages")
         .setValue(false)
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField516 = new Setting(this.recoveredField571, "label").setValue("Render Settings");
      this.recoveredField541 = new Setting(this.recoveredField571, "Show Potion info in inventory")
         .setValue(true);
      this.recoveredField515 = new Setting(
            this.recoveredField571, "Potion info shifts inventory", "Choose to make the potion info shift the inventory position."
         )
         .setValue(false);
      this.recoveredField567 = new Setting(this.recoveredField571, "Show Achievements").setValue(true);
      this.recoveredField582 = new Setting(this.recoveredField571, "Show HUD while in debug view", "Show the CheatBreaker HUD when the debug view is open.")
         .setValue(false);
      this.recoveredField594 = new Setting(this.recoveredField571, "GUI Blur", "Blurs the menus.")
         .method_08917(var0 -> Minecraft.getMinecraft().entityRenderer.method_29141())
         .setValue(false);
      this.recoveredField502 = new Setting(this.recoveredField571, "Container Background")
         .setValue("CheatBreaker")
         .acceptedValues("Vanilla", "CheatBreaker", "None");
      this.recoveredField554 = new Setting(
            this.recoveredField571,
            "Title Combat Indicators",
            "On some servers, a hit cooldown is shown because of the newer versions of the game,\nthis gives you the ability to toggle it."
         )
         .setValue(true);
      this.recoveredField587 = new Setting(this.recoveredField571, "label").setValue("Disconnect Settings");
      this.recoveredField531 = new Setting(
            this.recoveredField571, "Hold Disconnect Button", "Requires the mouse to be held for a certain period of time when clicking on Disconnect."
         )
         .setValue(false);
      this.recoveredField555 = new Setting(this.recoveredField571, "Hold Disconnect In", "Show the prompt in Singleplayer, Multiplayer, or both")
         .setValue("Multiplayer")
         .acceptedValues("Singleplayer", "Multiplayer", "Both")
         .method_08894(this.recoveredField531::method_08908);
      this.recoveredField581 = new Setting(
            this.recoveredField571, "Hold Disconnect Duration", "The amount of time the Mods button must be held down before entering the Mod Menu."
         )
         .setValue(5.0F)
         .setMinMax(1.0F, 10.0F)
         .method_08892("s")
         .method_08894(this.recoveredField531::method_08908);
      this.recoveredField481 = new Setting(this.recoveredField571, "Disable Disconnect Button", "Disable Disconnect button temporarily while in the Game Menu.")
         .setValue(false);
      this.recoveredField520 = new Setting(this.recoveredField571, "Disable Disconnect In", "Show the prompt in Singleplayer, Multiplayer, or both")
         .setValue("Multiplayer")
         .acceptedValues("Singleplayer", "Multiplayer", "Both")
         .method_08894(this.recoveredField481::method_08908);
      this.recoveredField530 = new Setting(
            this.recoveredField571, "Disable Disconnect Duration", "The amount of time the disconnect button will remain disabled."
         )
         .setValue(1.0F)
         .setMinMax(0.25F, 10.0F)
         .method_08892("s")
         .method_08894(this.recoveredField481::method_08908);
      this.recoveredField511 = new Setting(
            this.recoveredField571, "Disconnect Confirmation Prompt", "Show a disconnect confirmation prompt before actually disconnecting."
         )
         .setValue(false);
      this.recoveredField512 = new Setting(this.recoveredField571, "Show Reconnect Option", "Show a reconnect prompt")
         .setValue(true)
         .method_08894(this.recoveredField511::method_08908);
      this.recoveredField508 = new Setting(this.recoveredField571, "Show Prompt In", "Show the prompt in Singleplayer, Multiplayer, or both")
         .setValue("Multiplayer")
         .acceptedValues("Singleplayer", "Multiplayer", "Both")
         .method_08894(this.recoveredField511::method_08908);
      this.recoveredField573 = new Setting(this.recoveredField571, "Smart Disconnect", "Only show the disconnect prompt if clicking too soon.")
         .setValue(true)
         .method_08894(this.recoveredField511::method_08908);
      this.recoveredField496 = new Setting(
            this.recoveredField571, "Wait Duration", "The amount of time the user should wait before the disconnect prompt does not show."
         )
         .setValue(2.0F)
         .setMinMax(0.25F, 10.0F)
         .method_08892("s")
         .method_08894(() -> this.recoveredField573.method_08908() && this.recoveredField511.method_08908());
      this.recoveredField550 = new Setting(this.recoveredField571, "label").setValue("Streamer Mode Settings");
      this.recoveredField480 = new Setting(this.recoveredField571, "Streamer Mode", "Enables Streamer Mode Features").setValue(false);
      this.recoveredField527 = new Setting(
            this.recoveredField571, "Disable Mod Menu Keybind", "Disables the Mod Menu keybind from opening the Mod Menu when Streamer Mode is enabled."
         )
         .setValue(true)
         .method_08894(this.recoveredField480::method_08908);
      this.recoveredField505 = new Setting(
            this.recoveredField571, "Notify when pressed", "Notifies when the player presses the Mod Menu keybind while disabled."
         )
         .setValue(true)
         .method_08894(() -> this.recoveredField480.method_08908() && this.recoveredField527.method_08908());
      this.recoveredField497 = new Setting(
            this.recoveredField571, "Hold Mods Button", "Requires the mouse to be held for a certain period of time when clicking on Mods."
         )
         .setValue(true)
         .method_08894(this.recoveredField480::method_08908);
      this.recoveredField551 = new Setting(
            this.recoveredField571, "Hold Mods Duration", "The amount of time the Mods button must be held down before entering the Mod Menu."
         )
         .setValue(5.0F)
         .setMinMax(1.0F, 10.0F)
         .method_08892("s")
         .method_08894(() -> this.recoveredField480.method_08908() && this.recoveredField497.method_08908());
      this.recoveredField577 = new Setting(this.recoveredField571, "Disable Mods Button", "Disable Disconnect button temporarily while in the Game Menu.")
         .setValue(true)
         .method_08894(this.recoveredField480::method_08908);
      this.recoveredField552 = new Setting(this.recoveredField571, "Disable Mods Duration", "The amount of time the Mods button will remain disabled.")
         .setValue(2.0F)
         .setMinMax(1.0F, 10.0F)
         .method_08892("s")
         .method_08894(() -> this.recoveredField480.method_08908() && this.recoveredField577.method_08908());
      this.recoveredField545 = new Setting(this.recoveredField571, "label")
         .setValue("Resource Pack Settings")
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField542 = new Setting(this.recoveredField571, "Wide Pack Menu").setValue(false).method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField572 = new Setting(this.recoveredField571, "Transparent background", "Remove the dirt background in the resource pack menu.")
         .setValue(false)
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField526 = new Setting(this.recoveredField571, "Show Pack Folder Information", "Show the ")
         .setValue(true)
         .method_08914(SettingsDetailLevel.ADVANCED);
      this.recoveredField548 = new Setting(this.recoveredField571, "Show Pack Folder Icons")
         .setValue(true)
         .method_08914(SettingsDetailLevel.ADVANCED);
      this.recoveredField489 = new Setting(this.recoveredField571, "Show Pack Icons").setValue(true).method_08914(SettingsDetailLevel.ADVANCED);
      this.recoveredField525 = new Setting(this.recoveredField571, "Show Pack Descriptions")
         .setValue(true)
         .method_08914(SettingsDetailLevel.ADVANCED);
      this.recoveredField590 = new Setting(this.recoveredField571, "Show Search Bar").setValue(true).method_08914(SettingsDetailLevel.ADVANCED);
      this.recoveredField495 = new Setting(
            this.recoveredField571,
            "Sort Method",
            "Sort packs in a specific order.\n\n§bA-Z:§r Sort packs alphabetically starting from A to Z.\n§bZ-A:§r Sort packs alphabetically in reverse starting from Z to A."
         )
         .setValue("A-Z")
         .acceptedValues("A-Z", "Z-A")
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField487 = new Setting(this.recoveredField571, "Smooth Scrolling", "Makes scrolling smooth")
         .setValue(false)
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField568 = new Setting(this.recoveredField571, "List Background color", "Change the background color in the resource pack lists.")
         .setValue(Integer.MIN_VALUE)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08914(SettingsDetailLevel.MEDIUM);
      new Setting(this.recoveredField571, "label").setValue("Social Settings");
      this.friendListKeybind = new Setting(this.recoveredField571, "Friend List Keybind",
         "Opens or closes the friend list. Hold Shift, Ctrl or Alt for a combination. Backspace clears the binding.")
         .setValue(FriendListKeybind.DEFAULT).setMinMax(0, 2047);
      this.recoveredField544 = new Setting(this.recoveredField571, "label").setValue("Keybind Handling Settings");
      this.recoveredField492 = new Setting(this.recoveredField571, "Modern Keybind Handling").setValue(true);
      this.recoveredField588 = new Setting(this.recoveredField571, "Exclude Sneak Keybind")
         .setValue(true)
         .method_08894(() -> (Boolean)this.recoveredField492.getValue());
      this.recoveredField579 = new Setting(this.recoveredField571, "Exclude Block/Throw Keybind")
         .setValue(true)
         .method_08894(() -> (Boolean)this.recoveredField492.getValue());
      this.recoveredField593 = new Setting(this.recoveredField571, "label").setValue("Scaling Settings");
      this.recoveredField570 = new Setting(this.recoveredField571, "Mod Scale")
         .setValue("Default")
         .acceptedValues("Default", "Small", "Normal", "Large", "Auto");
      this.recoveredField500 = new Setting(this.recoveredField571, "Mod Scale Multiplier").setValue(1.0F).setMinMax(0.25F, 2.0F).method_08892("x");
      this.recoveredField595 = new Setting(this.recoveredField571, "Use Legacy Scaling").setValue(false);
      this.recoveredField524 = new Setting(this.recoveredField571, "label")
         .setValue("Screenshot Settings")
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField574 = new Setting(this.recoveredField571, "Make Shutter Sound").setValue(true).method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField589 = new Setting(this.recoveredField571, "Copy Screenshot Automatically")
         .setValue(false)
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField547 = new Setting(this.recoveredField571, "Send Message").setValue(true).method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField538 = new Setting(this.recoveredField571, "Compact Options")
         .setValue(false)
         .method_08914(SettingsDetailLevel.ADVANCED)
         .method_08894(() -> (Boolean)this.recoveredField547.getValue());
      this.recoveredField499 = new Setting(this.recoveredField571, "Show Open Option")
         .setValue(true)
         .method_08914(SettingsDetailLevel.MEDIUM)
         .method_08894(() -> (Boolean)this.recoveredField547.getValue());
      this.recoveredField488 = new Setting(this.recoveredField571, "Show Copy Option")
         .setValue(true)
         .method_08914(SettingsDetailLevel.MEDIUM)
         .method_08894(() -> (Boolean)this.recoveredField547.getValue());
      this.recoveredField553 = new Setting(this.recoveredField571, "Show Upload Option")
         .setValue(true)
         .method_08914(SettingsDetailLevel.MEDIUM)
         .method_08894(() -> (Boolean)this.recoveredField547.getValue());
      this.recoveredField534 = new Setting(this.recoveredField571, "label").setValue("Cosmetic Settings");
      this.recoveredField586 = new Setting(this.recoveredField571, "Show OptiFine Capes").setValue(true);
      this.recoveredField509 = new Setting(this.recoveredField571, "Show OptiFine Hats").setValue(true);
      this.recoveredField556 = new Setting(this.recoveredField571, "Show CheatBreaker Capes").setValue(true);
      this.recoveredField543 = new Setting(this.recoveredField571, "Show CheatBreaker Wings").setValue(true);
      String var1 = System.getProperty("os.name").toLowerCase();
      this.recoveredField566 = new Setting(this.recoveredField571, "label").setValue("Discord Rich Presence Settings").method_08894(() -> var1.contains("win"));
      this.recoveredField599 = new Setting(this.recoveredField571, "Show Discord Rich Presence")
         .setValue(true)
         .method_08894(() -> var1.contains("win"))
         .onChange(var1x -> {
            if (var1.contains("win") && Minecraft.getMinecraft().currentScreen instanceof CBModulesGui) {
               try {
                  if ((Boolean)var1x) {
                     CheatBreaker.getInstance().method_19739();
                  } else {
                     CheatBreaker.getInstance().method_19768().close();
                  }
               } catch (Exception var3) {
                  var3.printStackTrace();
               }
            }
         });
      this.recoveredField522 = new Setting(this.recoveredField571, "Show Active Server")
         .setValue(true)
         .method_08894(() -> var1.contains("win") && this.recoveredField599.method_08908())
         .onChange(var1x -> this.method_02688());
      this.recoveredField561 = new Setting(this.recoveredField571, "Show Active Account")
         .setValue(true)
         .method_08894(() -> var1.contains("win") && this.recoveredField599.method_08908())
         .onChange(var1x -> this.method_02688());
      this.recoveredField585 = new Setting(this.recoveredField571, "label").setValue("Color Settings");
      this.recoveredField483 = new Setting(this.recoveredField571, "Apply Default Color When Enabling Mods").setValue(true);
      this.recoveredField536 = new Setting(this.recoveredField571, "Default color", "Change the default color that will be displayed when mods are enabled.")
         .setValue(-1)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
      this.recoveredField517
         .put(
            new String[]{"lunar.gg"},
            new String[]{
               "While the Lunar Network is a safe server to play on, Lunar does contain a proprietary packet that may cause client crashes on 1.7.",
               String.valueOf(ServerRestrictionAction.WARN)
            }
         );
      GameSettings var2 = Minecraft.getMinecraft().gameSettings;
      this.recoveredField494 = new KeyBinding("Open Menu", 54, "CheatBreaker Client", true);
      this.recoveredField498 = new KeyBinding("Drag to look", 56, "CheatBreaker Client", true);
      this.recoveredField578 = new KeyBinding("Hide Nametags", 0, "CheatBreaker Client", true);
      this.recoveredField482 = new KeyBinding("Back look", 0, "CheatBreaker Client", true);
      this.recoveredField479 = new KeyBinding("Front look", 0, "CheatBreaker Client", true);
      var2.keyBindings = ArrayUtils.addAll(
         var2.keyBindings, this.recoveredField578, this.recoveredField494, this.recoveredField498, this.recoveredField482, this.recoveredField479
      );
   }

   public MainMenuMode method_02705() {
      return MainMenuMode.values()[(Integer)this.crosshairSettingsLabel.getValue()];
   }

   public void method_02684(Setting var1) {
      this.crosshairSettingsLabel = var1;
   }

   public void method_02688() {
      try {
         if (Minecraft.getMinecraft().currentScreen instanceof CBModulesGui) {
            String var1 = this.recoveredField561.method_08908() ? Minecraft.getMinecraft().getSession().getUsername() : null;
            CheatBreaker.getInstance().method_19778(var1);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   public Map<String[], String[]> method_02677() {
      return this.recoveredField517;
   }

   public boolean isFavouriteColor(int var1) {
      for (ColorPickerColorElement var2 : this.recoveredField575) {
         if (var2.color == var1) {
            return true;
         }
      }

      return false;
   }

   public List<String[]> method_02689() {
      return this.recoveredField484;
   }

   public void removeFavouriteColor(int var1) {
      this.recoveredField575.removeIf(var1x -> var1x.color == var1);
   }
}
