package net.crazy.streamchat.core.config;

import lombok.Getter;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget.KeyBindSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class TwitchChatWrite extends Config {
  @ParentSwitch
  @SwitchSetting
  @Getter
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(false);

  @KeyBindSetting
  @Getter
  private final ConfigProperty<Key> hotkey = new ConfigProperty<>(Key.NONE);
}
