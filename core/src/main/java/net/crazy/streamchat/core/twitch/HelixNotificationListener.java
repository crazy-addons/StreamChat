package net.crazy.streamchat.core.twitch;

import com.github.twitch4j.chat.events.channel.FollowEvent;
import com.github.twitch4j.chat.events.channel.GiftSubscriptionsEvent;
import net.crazy.streamchat.core.StreamChat;
import net.labymod.api.client.component.Component;
import net.labymod.api.notification.Notification;

public class HelixNotificationListener {

    private final StreamChat addon;

    public HelixNotificationListener(StreamChat addon) {
        this.addon = addon;
    }

    public void handleFollow(FollowEvent event) {
        if (!addon.configuration().apiConfig.getFollowNotify().get()) {
            return;
        }

        String user = event.getUser().getName();
        addon.pushNotification(Notification.builder()
            .title(Component.translatable(getKey("follow.title")))
            .text(Component.translatable(getKey("follow.text"),
                Component.text(user != null && !user.isBlank() ? user : "Unknown"))));
    }

    public void handleGiftSubscriptions(GiftSubscriptionsEvent event) {
        if (!addon.configuration().apiConfig.getGiftSubscriptionNotify().get()) {
            return;
        }

        String user = event.getUser().getName() == null ? "Unknown" : event.getUser().getName();
        addon.pushNotification(Notification.builder()
            .title(Component.translatable(getKey("giftSub.title")))
            .text(Component.translatable(getKey("giftSub.text"), Component.text(user),
                Component.text(event.getCount()))));
    }

    private String getKey(String path) {
        return String.format("streamchat.messages.notifications.events.%s", path);
    }
}
