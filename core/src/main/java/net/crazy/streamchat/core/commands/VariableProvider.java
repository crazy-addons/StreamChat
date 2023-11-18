package net.crazy.streamchat.core.commands;

import java.util.HashMap;
import java.util.function.Supplier;
import lombok.Getter;
import net.crazy.streamchat.core.StreamChat;

public class VariableProvider {
  private final StreamChat addon;

  @Getter
  private final HashMap<CommandVariables, Supplier<String>> mappedVariables = new HashMap<>();

  public VariableProvider() {
    this.addon = StreamChat.addon;
  }

  public void register(CommandVariables variables, Supplier<String> valueSupplier) {
    this.mappedVariables.put(variables, valueSupplier);
  }

  public String getVariable(CommandVariables variable) {
    if (!this.mappedVariables.containsKey(variable)) {
      return "null";
    }

    return this.mappedVariables.get(variable).get();
  }
}
