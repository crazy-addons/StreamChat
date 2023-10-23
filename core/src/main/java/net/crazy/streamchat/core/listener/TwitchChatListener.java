package net.crazy.streamchat.core.listener;

import net.crazy.streamchat.api.events.TwitchMessageReceived;
import net.crazy.streamchat.api.events.TwitchSendMessage;
import net.crazy.streamchat.core.StreamChat;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatMessageSendEvent;

public class TwitchChatListener {

  private final StreamChat addon;

  public TwitchChatListener(StreamChat addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onTwitchMessageReceive(TwitchMessageReceived event) {
    if (!addon.configuration().enabled().get())
      return;

    String prefix = addon.configuration().getPrefix().getOrDefault("§5✪ §d%user% §8> §7%message%");
    String message = prefix.replace("%user%", event.getSender())
        .replace("%message%", event.getMessage());
    addon.getMessageHistory().add(ComponentWidget.text(message));

    if (addon.configuration().twitchChatPreview.getEnabled().get())
      addon.displayMessage(message);
  }

  @Subscribe
  public void onMessageSend(ChatMessageSendEvent event) {
    if (!addon.configuration().enabled().get() ||
        !addon.configuration().twitchChatWrite.getEnabled().get())
      return;

    addon.labyAPI().eventBus().fire(new TwitchSendMessage(event.getMessage()));
    event.setCancelled(true);
  }
}
