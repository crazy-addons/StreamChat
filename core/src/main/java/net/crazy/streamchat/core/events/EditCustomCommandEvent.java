package net.crazy.streamchat.core.events;

import lombok.Getter;
import net.crazy.streamchat.core.commands.CustomCommand;
import net.labymod.api.event.Event;

public class EditCustomCommandEvent implements Event {
  @Getter
  private final CustomCommand customCommand;

  public EditCustomCommandEvent(CustomCommand customCommand) {
    this.customCommand = customCommand;
  }
}
