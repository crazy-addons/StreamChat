package net.crazy.streamchat.core;

import lombok.Getter;
import net.crazy.streamchat.core.activity.ControlNavigationElement;
import net.crazy.streamchat.core.generated.DefaultReferenceStorage;
import net.crazy.streamchat.core.listener.HotKeyListener;
import net.crazy.streamchat.core.listener.TwitchChatListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.models.addon.annotation.AddonMain;
import java.util.ArrayList;

@AddonMain
public class StreamChat extends LabyAddon<Configuration> {

  public static StreamChat addon;

  @Getter
  private TwitchBot bot;

  @Getter
  private final ArrayList<ComponentWidget> messageHistory = new ArrayList<>();

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

    this.logger().info(String.format("StreamChat+ | Addon successfully enabled. (v%s)",
        this.addonInfo().getVersion()));
  }

  @Override
  protected Class<Configuration> configurationClass() {
    return Configuration.class;
  }
}
