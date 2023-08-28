package net.crazy.streamchat.api.events;

import lombok.Getter;
import net.labymod.api.event.Event;

public class TwitchMessageReceived implements Event {

  @Getter
  private final String channel, sender, message;

  public TwitchMessageReceived(String channel, String sender, String message) {
    this.channel = channel;
    this.sender = sender;
    this.message = message;
  }
}
