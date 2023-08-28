package net.crazy.streamchat.core;

import net.crazy.streamchat.core.events.TwitchCommandReceived;
import net.crazy.streamchat.core.events.TwitchMessageReceived;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TwitchBot extends PircBot {

  private final StreamChat addon;
  private final Configuration config;

  public TwitchBot(StreamChat addon) {
    this.addon = addon;
    this.config = this.addon.configuration();
    this.setName(this.config.getBotName().getOrDefault("StreamChatPlus"));
  }

  public void start() {
    if (!config.enabled().get())
      return;

    // Check if all necessary information has been provided in the configuration
    if (!isConfiguredCorrectly())
      return;

    boolean validToken = false;

    // Try to connect
    try {
      this.connect("irc.twitch.tv", 6667, config.getTwitchToken().get());
      validToken = true;
      addon.logger().info("StreamChat+ | TwitchBot started successfully.");
    } catch (IOException | IrcException e) {
      e.printStackTrace();
    }
    
    if (!validToken)
      return;

    this.joinChannel("#" + config.getTwitchChannel().get().toLowerCase());
  }

  @Override
  protected void onMessage(String channel, String sender, String login, String hostname,
      String message) {
    if (!config.enabled().get())
      return;

    if (message.startsWith("!")) {
      addon.labyAPI().eventBus().fire(new TwitchCommandReceived(channel, sender, message));
    }

    addon.labyAPI().eventBus().fire(new TwitchMessageReceived(channel, sender, message));
  }

  public void stop() {
    this.disconnect();
  }

  public void restart() {
    stop();
    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    executorService.schedule(this::start, 2, TimeUnit.SECONDS);
  }

  private boolean isConfiguredCorrectly() {
    if (config.getTwitchChannel() == null || config.getTwitchToken() == null
        || config.getBotName() == null)
      return false;

    return config.getTwitchToken().get().startsWith("oauth:");
  }
}
