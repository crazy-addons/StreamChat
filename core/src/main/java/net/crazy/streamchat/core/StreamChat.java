package net.crazy.streamchat.core;

import lombok.Getter;
import net.crazy.streamchat.core.generated.DefaultReferenceStorage;
import net.crazy.streamchat.core.listener.TwitchChatListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class StreamChat extends LabyAddon<Configuration> {

  @Getter
  private TwitchBot bot;

  @Override
  protected void enable() {
    this.registerSettingCategory();
    bot = new TwitchBot(this);

    this.registerListener(new TwitchChatListener(this));

    this.logger().info(String.format("StreamChat+ | Addon successfully enabled. (v%s)",
        this.addonInfo().getVersion()));
  }

  @Override
  protected Class<Configuration> configurationClass() {
    return Configuration.class;
  }
}
