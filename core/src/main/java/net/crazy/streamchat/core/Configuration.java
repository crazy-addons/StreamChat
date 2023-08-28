package net.crazy.streamchat.core;

import lombok.Getter;
import net.crazy.streamchat.core.activity.ControlActivity;
import net.crazy.streamchat.core.config.TwitchChatPreview;
import net.crazy.streamchat.core.config.TwitchChatWrite;
import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.ActivitySettingWidget.ActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.AddonActivityWidget.AddonActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.MethodOrder;

@ConfigName("settings")
public class Configuration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @TextFieldSetting
  @Getter
  private final ConfigProperty<String> prefix = new ConfigProperty<>(
      "§5✪ §d%user% §8> §7%message%");

  @SettingSection("twitch")
  @TextFieldSetting
  @Getter
  private final ConfigProperty<String> botName = new ConfigProperty<>("StreamChatPlus");

  @TextFieldSetting
  @Getter
  private final ConfigProperty<String> twitchChannel = new ConfigProperty<>("");

  @TextFieldSetting
  @Getter
  private final ConfigProperty<String> twitchToken = new ConfigProperty<>("");

  @ButtonSetting
  @MethodOrder(after = "twitchToken")
  public void startBot() {
    TwitchBot bot = StreamChat.addon.getBot();
    if (bot.isConnected())
      return;

    bot.start();
  }

  @SettingSection("ingame")
  public TwitchChatPreview twitchChatPreview = new TwitchChatPreview();
  public TwitchChatWrite twitchChatWrite = new TwitchChatWrite();

  @SettingSection("extras")
  @SwitchSetting
  @Getter
  private final ConfigProperty<Boolean> controlPanel = new ConfigProperty<>(false);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }
}
