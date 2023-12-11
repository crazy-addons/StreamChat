package net.crazy.streamchat.core.twitch;

import com.github.twitch4j.chat.events.channel.FollowEvent;
import net.crazy.streamchat.core.StreamChat;
import net.labymod.api.client.component.Component;
import net.labymod.api.notification.Notification;

public class HelixListener {

   private final StreamChat addon;

   public HelixListener(StreamChat addon) {
      this.addon = addon;
   }

   public void handleFollow(FollowEvent event) {
      if (!addon.configuration().apiConfig.getFollowNotify().get()) {
         return;
      }

      String user = event.getUser().getName();
      addon.pushNotification(Notification.builder()
          .title(Component.translatable("streamchat.messages.notification.events.follow.title"))
          .text(Component.translatable("streamchat.messages.notification.events.follow.text",
              Component.text(user))));
   }
}
