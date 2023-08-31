package net.crazy.streamchat.core;

import java.util.ArrayList;
import lombok.Getter;
import net.crazy.streamchat.core.activity.control.ControlNavigationElement;
import net.crazy.streamchat.core.commands.CustomCommandManager;
import net.crazy.streamchat.core.listener.HotKeyListener;
import net.crazy.streamchat.core.listener.TwitchChatListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class StreamChat extends LabyAddon<Configuration> {

  public static StreamChat addon;

  @Getter
  private TwitchBot bot;

  @Getter
  private final ArrayList<ComponentWidget> messageHistory = new ArrayList<>();
  private CustomCommandManager customCommandManager;

  @Override
  protected void enable() {
    addon = this;
    this.registerSettingCategory();

    bot = new TwitchBot(this);
    this.registerListener(bot);

    this.registerListener(new TwitchChatListener(this));
    this.registerListener(new HotKeyListener(this));

    if (configuration().getControlPanel().get()) {
      this.labyAPI().navigationService().register(new ControlNavigationElement(this));
    }

    this.customCommandManager = new CustomCommandManager(this);
    this.registerListener(customCommandManager);

    this.logger().info(String.format("StreamChat+ | Addon successfully enabled. (v%s)",
        this.addonInfo().getVersion()));
  }

  @Override
  protected Class<Configuration> configurationClass() {
    return Configuration.class;
  }
}
