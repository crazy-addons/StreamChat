package net.crazy.streamchat.core.config;

import lombok.Getter;
import net.crazy.streamchat.core.StreamChat;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.notification.Notification;
import net.labymod.api.util.MethodOrder;

public class TwitchAPIConfig extends Config {
   @Exclude
   public String token = "";

   @Exclude
   public String previousScopes = "";

   @ButtonSetting
   @MethodOrder(before = "followNotify")
   @SettingRequires(value = "setup", invert = true)
   public void startAuth() {
      if (!token.isEmpty()) {
         StreamChat.addon.pushNotification(Notification.builder()
             .title(Component.text("StreamChat+"))
             .text(Component.translatable("streamchat.messages.notification.auth_already")));
         return;
      }

      StreamChat.addon.getTwitchAPI().auth.startOAuth();
   }

   @SettingSection("notifications")
   @SwitchSetting
   @Getter
   private final ConfigProperty<Boolean> followNotify = new ConfigProperty<>(true);
}
