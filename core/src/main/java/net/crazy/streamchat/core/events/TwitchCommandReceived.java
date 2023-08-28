package net.crazy.streamchat.core.events;

import lombok.Getter;
import net.labymod.api.event.Event;

public class TwitchCommandReceived implements Event {

  @Getter
  private final String channel, sender, command;

  public TwitchCommandReceived(String channel, String sender, String command) {
    this.channel = channel;
    this.sender = sender;
    this.command = command;
  }
}
