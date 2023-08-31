package net.crazy.streamchat.core;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.crazy.streamchat.api.events.TwitchCommandReceived;
import net.crazy.streamchat.api.events.TwitchMessageReceived;
import net.crazy.streamchat.api.events.TwitchSendMessage;
import net.labymod.api.event.Subscribe;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

public class TwitchBot extends PircBot {

  private final StreamChat addon;
  private final Configuration config;

  public TwitchBot(StreamChat addon) {
    this.addon = addon;
    this.config = this.addon.configuration();
    this.setName(this.config.getBotName().getOrDefault("StreamChatPlus"));
  }

  public void start() {
    if (!config.enabled().get()) {
      return;
    }

    // Check if all necessary information has been provided in the configuration
    if (!isConfiguredCorrectly()) {
      return;
    }

    boolean validToken = false;

    // Try to connect
    try {
      this.connect("irc.twitch.tv", 6667, config.getTwitchToken().get());
      validToken = true;
      addon.logger().info("StreamChat+ | TwitchBot started successfully.");
    } catch (IOException | IrcException e) {
      e.printStackTrace();
    }

    if (!validToken) {
      return;
    }

    this.joinChannel("#" + config.getTwitchChannel().get().toLowerCase());
  }

  @Override
  protected void onMessage(String channel, String sender, String login, String hostname,
      String message) {
    if (!config.enabled().get()) {
      return;
    }


    if (message.startsWith(
        addon.configuration().twitchCommandConfig.getCommandPrefix().getOrDefault("!"))) {
      addon.labyAPI().eventBus().fire(new TwitchCommandReceived(channel, sender, message));
    }

    addon.labyAPI().eventBus().fire(new TwitchMessageReceived(channel, sender, message));
  }

  @Subscribe
  public void sendMessage(TwitchSendMessage event) {
    this.sendMessage("#" + (event.getChannel() == null ?
            config.getTwitchChannel().get().toLowerCase() : event.getChannel()),
        event.getMessage());

    addon.labyAPI().eventBus().fire(
        new TwitchMessageReceived("", addon.configuration().getTwitchChannel().get(),
            event.getMessage()));
  }

  public void stop() {
    this.disconnect();
    addon.getMessageHistory().clear();
  }

  public void restart() {
    stop();
    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    executorService.schedule(this::start, 2, TimeUnit.SECONDS);
  }

  private boolean isConfiguredCorrectly() {
    if (config.getTwitchChannel() == null || config.getTwitchToken() == null
        || config.getBotName() == null) {
      return false;
    }

    return config.getTwitchToken().get().startsWith("oauth:");
  }
}
