package net.crazy.streamchat.core.twitch;

import net.crazy.streamchat.core.StreamChat;

public class TwitchAPI {
   private final StreamChat addon;
   public final TwitchAuth auth;

   public TwitchAPI(StreamChat addon) {
      this.addon = addon;
      this.auth = new TwitchAuth(addon);
   }
}
