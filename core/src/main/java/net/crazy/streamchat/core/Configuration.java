package net.crazy.streamchat.core;

import lombok.Getter;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

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

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }
}
