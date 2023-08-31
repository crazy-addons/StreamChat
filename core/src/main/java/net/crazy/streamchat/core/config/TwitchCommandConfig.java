package net.crazy.streamchat.core.config;

import lombok.Getter;
import net.crazy.streamchat.core.activity.commands.CommandActivity;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.ActivitySettingWidget.ActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.util.MethodOrder;

public class TwitchCommandConfig extends Config {

  @TextFieldSetting(maxLength = 1)
  @Getter
  private final ConfigProperty<String> commandPrefix = new ConfigProperty<>("!");

  @ActivitySetting
  @MethodOrder(after = "commandPrefix")
  public Activity openCommands() {
    return new CommandActivity();
  }

}
