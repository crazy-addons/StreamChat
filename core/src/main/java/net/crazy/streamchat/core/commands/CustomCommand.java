package net.crazy.streamchat.core.commands;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class CustomCommand {

  @Getter
  private String name, response;

  @Getter
  private boolean enabled;

  @Getter
  private ArrayList<String> triggers;

  private CustomCommand(String name, boolean enabled,
      ArrayList<String> triggers, String response) {
    this.name = name;
    this.enabled = enabled;
    this.triggers = triggers;
    this.response = response;
  }

  public static CustomCommand create(String name, boolean enabled,
      ArrayList<String> triggers, String response) {
    return new CustomCommand(name, enabled, triggers, response);
  }

  public void toggle() {
    this.enabled = !enabled;
  }

  public boolean hasTrigger(String trigger) {
    return this.triggers.contains(trigger.toLowerCase());
  }

  public static class CustomCommandBuilder {

    private String name, response;
    private final ArrayList<String> triggers = new ArrayList<>();

    private boolean enabled = true;

    public CustomCommandBuilder setName(String name) {
      this.name = name;
      return this;
    }

    public CustomCommandBuilder setEnabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    public CustomCommandBuilder addTriggers(String... triggers) {
      this.triggers.addAll(List.of(triggers));
      return this;
    }

    public CustomCommandBuilder setResponse(String response) {
      this.response = response;
      return this;
    }

    public CustomCommand create() {
      return CustomCommand.create(name, enabled, triggers, response);
    }
  }
}
