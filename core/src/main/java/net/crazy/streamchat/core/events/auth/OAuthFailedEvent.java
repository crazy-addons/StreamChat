package net.crazy.streamchat.core.events.auth;

import net.labymod.api.event.Event;

public class OAuthFailedEvent implements Event {
   public final String message;

   public OAuthFailedEvent(String message) {
      this.message = message;
   }
}
