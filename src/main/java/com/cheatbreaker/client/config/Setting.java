package com.cheatbreaker.client.config;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.module.type.AnimationsModule;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;

public class Setting {
   private int maxStringLength = 64;

   public int getMaxStringLength() {
      return this.maxStringLength;
   }

   public Setting setMaxStringLength(int length) {
      if (length < 1) {
         throw new IllegalArgumentException("String length must be positive");
      }
      this.maxStringLength = length;
      return this;
   }

   public boolean recoveredField3079;
   public BooleanSupplier recoveredField3080;
   public AbstractModule container;
   public String recoveredField3081;
   public boolean recoveredField3082;
   public boolean recoveredField3083;
   public int[] colorArray;
   public String[] acceptedValues;
   public int recoveredField3084;
   public Object[] recoveredField3085;
   public Object recoveredField3086;
   public String recoveredField3087;
   public Object recoveredField3088;
   public Consumer<Object> valueConsumer;
   public String recoveredField3089;
   public String recoveredField3090 = "";
   public boolean recoveredField3091;
   public boolean recoveredField3092;
   public String recoveredField3093;
   public String recoveredField3094;
   public String recoveredField3095;
   public Object recoveredField3096;
   public Setting parent;
   public Object recoveredField3097;
   public String recoveredField3098;
   public Object recoveredField3099;

   public Setting setParent(Setting var1) {
      if (var1.getType() != Setting.Type.BOOLEAN) {
         throw new IllegalStateException("Parent can only be boolean.");
      } else {
         this.parent = var1;
         return this;
      }
   }

   public Setting getParent() {
      return this.parent;
   }

   public Setting(AbstractModule var1, String var2) {
      this.recoveredField3089 = "";
      this.recoveredField3093 = "";
      this.recoveredField3094 = "";
      this.recoveredField3081 = "";
      this.recoveredField3087 = "OFF";
      this.recoveredField3098 = "ON";
      this.recoveredField3082 = true;
      this.container = var1;
      var1.getSettingsList().add(this);
      this.recoveredField3095 = var2;
   }

   public int method_08912() {
      return (Integer)this.recoveredField3086;
   }

   public Setting method_08896(boolean var1) {
      this.recoveredField3082 = var1;
      return this;
   }

   public void method_08918(boolean var1) {
      this.recoveredField3091 = var1;
   }

   public Object[] method_08883() {
      return this.recoveredField3085;
   }

   public boolean method_08908() {
      return !(this.container instanceof AnimationsModule) ? (Boolean)this.recoveredField3086 : this.container.isEnabled() && (Boolean)this.recoveredField3086;
   }

   public Setting acceptedValues(String... var1) {
      this.acceptedValues = var1;
      return this;
   }

   public Setting setValue(Object var1) {
      return this.setValue(var1, true);
   }

   public Setting.Type getType() {
      if (this.recoveredField3086.getClass().isAssignableFrom(Boolean.class)) {
         return Setting.Type.BOOLEAN;
      } else if (this.recoveredField3086.getClass().isAssignableFrom(ArrayList.class) && this.recoveredField3085 != null && this.recoveredField3085.length != 0
         )
       {
         return Setting.Type.ARRAYLIST;
      } else if (!this.recoveredField3086.getClass().isAssignableFrom(String.class)) {
         if (this.recoveredField3086.getClass().isAssignableFrom(Float.class)) {
            return Setting.Type.FLOAT;
         } else if (this.recoveredField3086.getClass().isAssignableFrom(Double.class)) {
            return Setting.Type.DOUBLE;
         } else if (this.recoveredField3086.getClass().isAssignableFrom(String[].class)) {
            return Setting.Type.STRING_ARRAY;
         } else {
            return this.recoveredField3086.getClass().isAssignableFrom(Integer.class) ? Setting.Type.INTEGER : null;
         }
      } else {
         return this.acceptedValues != null && this.acceptedValues.length != 0 ? Setting.Type.STRING_ARRAY : Setting.Type.STRING;
      }
   }

   public String method_08875() {
      return this.recoveredField3081;
   }

   public Setting method_08887(int var1) {
      this.recoveredField3084 = var1;
      this.recoveredField3091 = true;
      return this;
   }

   public String method_08919() {
      return this.recoveredField3087;
   }

   public Object method_08904() {
      return this.recoveredField3097;
   }

   public Setting method_08914(Object var1) {
      this.recoveredField3096 = var1;
      return this;
   }

   public Setting method_08897(Object... var1) {
      this.recoveredField3085 = var1;
      return this;
   }

   public boolean method_08876() {
      return this.recoveredField3082;
   }

   public BooleanSupplier method_08921() {
      return this.recoveredField3080;
   }

   public String method_08872() {
      return this.recoveredField3093;
   }

   public Setting method_08892(String var1) {
      this.recoveredField3089 = var1;
      return this;
   }

   public String[] getAcceptedValues() {
      return this.acceptedValues;
   }

   public Setting method_08893(String var1, String var2) {
      this.recoveredField3094 = var1;
      this.recoveredField3081 = var2;
      return this;
   }

   public Consumer<Object> method_08902() {
      return this.valueConsumer;
   }

   public Setting onChange(Consumer<Object> var1) {
      this.valueConsumer = var1;
      return this;
   }

   public boolean method_08880() {
      return this.recoveredField3079;
   }

   public boolean method_08906() {
      return this.parent != null && (Boolean)this.parent.getValue();
   }

   public String method_08911() {
      return this.recoveredField3095;
   }

   public String method_08870() {
      return this.recoveredField3089;
   }

   public boolean method_08879() {
      return this.recoveredField3092;
   }

   public Setting method_08915(String var1) {
      this.recoveredField3093 = var1;
      return this;
   }

   public Setting method_08917(Consumer<Object> var1) {
      if (Minecraft.getMinecraft().isFullScreen() && Minecraft.getMinecraft().currentScreen instanceof CBModulesGui) {
         this.valueConsumer = var1;
      }

      return this;
   }

   public Setting setMinMax(Number var1, Number var2) {
      this.recoveredField3097 = var1;
      this.recoveredField3099 = var2;
      return this;
   }

   public Setting(List<Setting> var1, String var2, String var3) {
      this.recoveredField3089 = "";
      this.recoveredField3093 = "";
      this.recoveredField3094 = "";
      this.recoveredField3081 = "";
      this.recoveredField3087 = "OFF";
      this.recoveredField3098 = "ON";
      this.recoveredField3082 = true;
      var1.add(this);
      this.recoveredField3095 = var2;
      this.recoveredField3090 = var3;
   }

   public Object getValue() {
      return this.recoveredField3086;
   }

   public int[] method_08910() {
      return this.colorArray;
   }

   public Setting method_08884(Object var1) {
      this.recoveredField3088 = var1;
      return this;
   }

   public Setting(AbstractModule var1, String var2, String var3) {
      this.recoveredField3089 = "";
      this.recoveredField3093 = "";
      this.recoveredField3094 = "";
      this.recoveredField3081 = "";
      this.recoveredField3087 = "OFF";
      this.recoveredField3098 = "ON";
      this.recoveredField3082 = true;
      this.container = var1;
      var1.getSettingsList().add(this);
      this.recoveredField3095 = var2;
      this.recoveredField3090 = var3;
   }

   public ArrayList method_08869() {
      return (ArrayList)this.recoveredField3086;
   }

   public Setting(List<Setting> var1, String var2) {
      this.recoveredField3089 = "";
      this.recoveredField3093 = "";
      this.recoveredField3094 = "";
      this.recoveredField3081 = "";
      this.recoveredField3087 = "OFF";
      this.recoveredField3098 = "ON";
      this.recoveredField3082 = true;
      var1.add(this);
      this.recoveredField3095 = var2;
   }

   public String method_08868() {
      return this.recoveredField3090;
   }

   public void method_08885(boolean var1) {
      this.recoveredField3092 = var1;
   }

   public String method_08871() {
      return this.recoveredField3098;
   }

   public boolean method_08867() {
      return this.recoveredField3091;
   }

   public boolean method_08907() {
      return this.recoveredField3083;
   }

   public int method_08901() {
      if (this.recoveredField3079) {
         Integer var1 = (Integer)this.recoveredField3086;
         int var2 = var1 >> 24 & 0xFF;
         float var3 = (float)System.nanoTime() / 1.0E10F % 1.0F;
         return this.recoveredField3083
            ? var2 << 24 | Color.HSBtoRGB((float)(System.currentTimeMillis() % 1000L) / 1000.0F, 1.0F, 1.0F) & 16777215
            : var2 << 24 | Color.HSBtoRGB(var3, 1.0F, 1.0F) & 16777215;
      } else {
         return (Integer)this.recoveredField3086;
      }
   }

   public float method_08905() {
      return (Float)this.recoveredField3086;
   }

   public String method_08882() {
      return this.recoveredField3094;
   }

   public AbstractModule method_08920() {
      return this.container;
   }

   public Object method_08903() {
      return this.recoveredField3096;
   }

   public Object method_08878() {
      return this.recoveredField3099;
   }

   public Setting(String var1) {
      this.recoveredField3089 = "";
      this.recoveredField3093 = "";
      this.recoveredField3094 = "";
      this.recoveredField3081 = "";
      this.recoveredField3087 = "OFF";
      this.recoveredField3098 = "ON";
      this.recoveredField3082 = true;
      this.recoveredField3095 = var1;
   }

   public String method_08874() {
      return (String)this.recoveredField3086;
   }

   public Setting method_08916(String var1, String var2) {
      this.recoveredField3087 = var1;
      this.recoveredField3098 = var2;
      return this;
   }

   public Setting method_08909(boolean var1) {
      this.recoveredField3092 = var1;
      return this;
   }

   public Setting method_08894(BooleanSupplier var1) {
      this.recoveredField3080 = var1;
      return this;
   }

   public Setting setValue(Object var1, boolean var2) {
      if (CheatBreaker.getInstance().getConfigManager().recoveredField3784 != null
         && CheatBreaker.getInstance().getConfigManager().recoveredField3784.getName().equals("default")) {
         if (var2) {
            CheatBreaker.getInstance().getConfigManager().method_25097();
         }
      } else if (this.container != null) {
         this.container.method_28782().add(var1);
      }

      this.recoveredField3086 = var1;
      if (this.valueConsumer != null) {
         this.valueConsumer.accept(var1);
      }

      return this;
   }

   public Object method_08881() {
      return this.recoveredField3088;
   }

   public Color method_08913(int var1) {
      float var2 = (var1 >> 24 & 0xFF) / 255.0F;
      float var3 = (var1 >> 16 & 0xFF) / 255.0F;
      float var4 = (var1 >> 8 & 0xFF) / 255.0F;
      float var5 = (var1 & 0xFF) / 255.0F;
      return new Color(var3, var4, var5, var2);
   }

   public int method_08877() {
      return this.recoveredField3084;
   }

   public static enum Type {
      STRING,
      STRING_ARRAY,
      FLOAT,
      INTEGER,
      DOUBLE,
      BOOLEAN,
      ARRAYLIST,
      SHORT,
      FILE;
      // $VF: synthetic field
      public static Setting.Type[] recoveredField2874 = new Setting.Type[]{
         Setting.Type.STRING,
         Setting.Type.STRING_ARRAY,
         FLOAT,
         INTEGER,
         DOUBLE,
         Setting.Type.BOOLEAN,
         Setting.Type.ARRAYLIST,
         SHORT,
         FILE
      };
   }
}
