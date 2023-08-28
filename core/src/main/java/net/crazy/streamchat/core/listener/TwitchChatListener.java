package net.crazy.streamchat.core.listener;

import net.crazy.streamchat.core.StreamChat;
import net.crazy.streamchat.api.events.TwitchMessageReceived;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.event.Subscribe;

public class TwitchChatListener {

  private final StreamChat addon;

  public TwitchChatListener(StreamChat addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onTwitchMessageReceive(TwitchMessageReceived event) {
    String prefix = addon.configuration().getPrefix().getOrDefault("§5✪ §d%user% §8> §7%message%");
    String message = prefix.replace("%user%", event.getSender())
        .replace("%message%", event.getMessage());
    addon.displayMessage(message);
    addon.getMessageHistory().add(ComponentWidget.text(message));
  }
}
