package net.crazy.streamchat.core.activity.commands;

import net.crazy.streamchat.core.StreamChat;
import net.crazy.streamchat.core.commands.CustomCommand;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;

@AutoActivity
public class CommandCreationOverlay extends SimpleActivity {
  private final StreamChat addon = StreamChat.addon;

  private CustomCommand customCommand = null;

  /**
   * Create a new Command
   */
  public CommandCreationOverlay() {}

  /**
   * Edit an existing command
   * @param customCommand command to edit
   */
  public CommandCreationOverlay(CustomCommand customCommand) {
    this.customCommand = customCommand;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    TextFieldWidget nameWidget = new TextFieldWidget();
    nameWidget.addId("nameWidget");
    document.addChild(nameWidget);
  }
}
