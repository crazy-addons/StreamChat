package net.crazy.streamchat.core.config;

import net.crazy.streamchat.core.StreamChat;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.util.MethodOrder;

public class TwitchAPIConfig extends Config {
   @Exclude
   public String token = "";

   @SwitchSetting
   private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(false);

   @ButtonSetting
   @MethodOrder(after = "enabled")
   public void startAuth() {
      StreamChat.addon.getTwitchAPI().auth.startOAuth();
   }
}
