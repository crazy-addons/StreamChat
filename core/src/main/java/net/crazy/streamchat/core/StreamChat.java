package net.crazy.streamchat.core;

import com.github.twitch4j.TwitchClient;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import net.crazy.streamchat.core.activity.control.ControlNavigationElement;
import net.crazy.streamchat.core.commands.CustomCommandManager;
import net.crazy.streamchat.core.commands.VariableProvider;
import net.crazy.streamchat.core.commands.variables.MinecraftVariables;
import net.crazy.streamchat.core.listener.HotKeyListener;
import net.crazy.streamchat.core.listener.TwitchChatListener;
import net.crazy.streamchat.core.twitch.TwitchAPI;
import net.crazy.streamchat.core.util.NamedResources;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.notification.Notification;

@AddonMain
public class StreamChat extends LabyAddon<Configuration> {

   public static StreamChat addon;
   public static VariableProvider variableProvider = new VariableProvider();

   @Getter
   private TwitchBot bot;

   @Getter
   @Setter
   private TwitchClient twitchClient;

   @Getter
   private TwitchAPI twitchAPI;

   @Getter
   private final ArrayList<ComponentWidget> messageHistory = new ArrayList<>();
   private CustomCommandManager customCommandManager;

   @Override
   protected void enable() {
      addon = this;
      this.registerSettingCategory();

      bot = new TwitchBot(this);
      this.registerListener(bot);

      this.registerListener(new TwitchChatListener(this));
      this.registerListener(new HotKeyListener(this));

      if (configuration().getControlPanel().get()) {
         this.labyAPI().navigationService().register(new ControlNavigationElement(this));
      }

      // Custom Commands
      new MinecraftVariables(this);

      this.customCommandManager = new CustomCommandManager(this);
      this.registerListener(customCommandManager);

      this.twitchAPI = new TwitchAPI(this);

      this.logger().info(String.format("StreamChat+ | Addon successfully enabled. (v%s)",
          this.addonInfo().getVersion()));
   }

   @Override
   protected Class<Configuration> configurationClass() {
      return Configuration.class;
   }

   public void pushNotification(Notification.Builder builder) {
      builder.icon(NamedResources.logo);
      builder.buildAndPush();
   }
}
