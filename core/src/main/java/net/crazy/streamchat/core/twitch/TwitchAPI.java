package net.crazy.streamchat.core.twitch;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.chat.events.channel.FollowEvent;
import com.github.twitch4j.chat.events.channel.GiftSubscriptionsEvent;
import feign.Logger.Level;
import lombok.Getter;
import net.crazy.streamchat.core.StreamChat;

public class TwitchAPI {

    private final StreamChat addon;
    public final TwitchAuth auth;

    @Getter
    private String loginName;

    public TwitchAPI(StreamChat addon) {
        this.addon = addon;
        this.auth = new TwitchAuth(addon);

        this.createClient();
    }

    public void createClient() {
        addon.info("Attempting to create TwitchClient...");
        if (addon.getTwitchClient() != null) {
            return;
        }

        if (!addon.configuration().enabled().get()) {
            return;
        }

        String token = addon.configuration().apiConfig.token;
        if (token.isEmpty()) {
            return;
        }
        addon.info("Token for TwitchClient exists, proceeding...");

        try {
            CredentialManager credManager = CredentialManagerBuilder.builder().build();
            OAuth2Credential credential = new OAuth2Credential("twitch", token);

            // Build Twitch Client
            TwitchClient client = TwitchClientBuilder.builder()
                .withCredentialManager(credManager)
                .withDefaultAuthToken(credential)
                .withEnableHelix(true)
                .withFeignLogLevel(Level.NONE)
                .build();

            // Getting LoginName for Requests
            this.loginName = new TwitchIdentityProvider(null, null, null)
                .getAdditionalCredentialInformation(credential).map(OAuth2Credential::getUserName)
                .orElse("");
            addon.info("Loginname: %s", this.loginName);

            client.getClientHelper().enableFollowEventListener(this.loginName);
            addon.setTwitchClient(client);

            EventManager eventManager = addon.getTwitchClient().getEventManager();
            HelixNotificationListener helixNotifications = new HelixNotificationListener(addon);

            eventManager.onEvent(FollowEvent.class, helixNotifications::handleFollow);
            eventManager.onEvent(GiftSubscriptionsEvent.class,
                helixNotifications::handleGiftSubscriptions);

            addon.info("TwitchClient started successfully");
        } catch (Exception exception) {
            addon.logger().error("Couldn't start TwitchClient", exception);
        }
    }
}
