package net.crazy.streamchat.core.commands;

import lombok.Getter;
import net.crazy.streamchat.core.StreamChat;

public enum CommandVariables {
  /* Minecraft */
  HEALTH("health"),
  USERNAME("username"),
  VERSION("version"),
  SERVER("server"),
  BIOME("biome"),
  FOOD("food"),
  GAMEMODE("gamemode");

  @Getter
  private final String identifier;

  CommandVariables(String identifier) {
    this.identifier = identifier;
  }

  public String getValue() {
    return StreamChat.variableProvider.getVariable(this);
  }

  public static CommandVariables findById(String identifier) {
    for (CommandVariables variable : values()) {
      if (variable.identifier.equalsIgnoreCase(identifier))
        return variable;
    }
    return null;
  }
}
