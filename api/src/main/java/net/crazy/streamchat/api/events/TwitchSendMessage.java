package net.crazy.streamchat.api.events;

import lombok.Getter;
import net.labymod.api.event.Event;

public class TwitchSendMessage implements Event {
  @Getter
  private String channel = null;

  @Getter
  private final String message;

  public TwitchSendMessage(String message) {
    this.message = message;
  }

  public TwitchSendMessage(String channel, String message) {
    this.channel = channel;
    this.message = message;
  }
}
