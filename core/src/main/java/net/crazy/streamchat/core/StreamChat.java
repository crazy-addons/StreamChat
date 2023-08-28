package net.crazy.streamchat.core;

import lombok.Getter;
import net.crazy.streamchat.core.activity.ControlNavigationElement;
import net.crazy.streamchat.core.generated.DefaultReferenceStorage;
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

    DefaultReferenceStorage storage = this.referenceStorageAccessor();

    bot = new TwitchBot(this);

    this.registerListener(new TwitchChatListener(this));

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
