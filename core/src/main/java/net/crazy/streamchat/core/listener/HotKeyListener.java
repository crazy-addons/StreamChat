package net.crazy.streamchat.core.listener;

import net.crazy.streamchat.core.StreamChat;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.input.KeyEvent.State;

public class HotKeyListener {
  private final StreamChat addon;

  public HotKeyListener(StreamChat addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onKeyPressed(KeyEvent event) {
    if (event.state() != State.PRESS
        || Laby.labyAPI().minecraft().minecraftWindow().isScreenOpened()) {
      return;
    }

    if (addon.configuration().twitchChatPreview.getHotkey().get().equals(event.key())) {
      boolean newState = !addon.configuration().twitchChatPreview.getEnabled().get();
      addon.configuration().twitchChatPreview.getEnabled().set(newState);
      addon.saveConfiguration();
      addon.displayMessage(Component.translatable(
          String.format("streamchat.messages.states.preview.%s", newState ? "enabled" : "disabled")));
      return;
    }

    if (addon.configuration().twitchChatWrite.getHotkey().get().equals(event.key())) {
      boolean newState = !addon.configuration().twitchChatWrite.getEnabled().get();
      addon.configuration().twitchChatWrite.getEnabled().set(newState);
      addon.saveConfiguration();
      addon.displayMessage(Component.translatable(
          String.format("streamchat.messages.states.write.%s", newState ? "enabled" : "disabled")));
    }
  }
}
