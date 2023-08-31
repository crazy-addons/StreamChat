package net.crazy.streamchat.core.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.crazy.streamchat.api.events.TwitchCommandReceived;
import net.crazy.streamchat.api.events.TwitchSendMessage;
import net.crazy.streamchat.core.StreamChat;
import net.labymod.api.event.Subscribe;

public class CustomCommandManager {
  private final StreamChat addon;

  public CustomCommandManager(StreamChat addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onCommand(TwitchCommandReceived event) {
    String trigger = event.getCommand().replaceFirst(
        addon.configuration().twitchCommandConfig.getCommandPrefix().getOrDefault("!"), "");

    if (!addon.configuration().triggerExists(trigger))
      return;

    CustomCommand command = null;
    for (CustomCommand customCommand : addon.configuration().customCommands.values()) {
      if (customCommand.hasTrigger(trigger)) {
        command = customCommand;
        break;
      }
    }

    if (command == null)
      return;

    if (!command.isEnabled())
      return;

    String response = command.getResponse();
    Pattern pattern = Pattern.compile("(%[a-zA-Z]+%)");
    Matcher matcher = pattern.matcher(response);

    // Variables found
    if (matcher.find()) {
      for (int i = 0; i < matcher.groupCount(); i++) {
        String var = matcher.group(i);
        String variable = var.replaceAll("%", "");
        CommandVariables foundVariable = CommandVariables.findById(variable);

        if (foundVariable == null)
          continue;

        response = response.replace(var, foundVariable.getValue());
      }
    }

    addon.labyAPI().eventBus().fire(new TwitchSendMessage(response));
  }
}
