package net.crazy.streamchat.core.activity.commands;

import net.crazy.streamchat.core.StreamChat;
import net.crazy.streamchat.core.commands.CustomCommand;
import net.crazy.streamchat.core.events.EditCustomCommandEvent;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;

@AutoWidget
@Link("elements.lss")
public class CustomCommandWidget extends SimpleWidget {

  private final CustomCommand customCommand;

  private final ResourceLocation textures = ResourceLocation.create("streamchat",
      "themes/vanilla/textures/commands.png");

  public CustomCommandWidget(CustomCommand customCommand) {
    this.customCommand = customCommand;
  }

  private ButtonWidget toggleButton;
  private boolean lastKnown = false;

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);
    boolean enabled = customCommand.isEnabled();
    this.lastKnown = enabled;

    addId(enabled ? "enabled" : "disabled");

    ComponentWidget name = ComponentWidget.text(customCommand.getName());
    name.addId("name");
    addChild(name);

    ButtonWidget editButton = ButtonWidget.icon(Icon.sprite16(textures, 0, 0), () ->
        StreamChat.addon.labyAPI().eventBus().fire(new EditCustomCommandEvent(this.customCommand)));
    editButton.addId("editButton");
    addChild(editButton);

    toggleButton = ButtonWidget.icon(Icon.sprite16(textures, enabled ? 1 : 2, 0));
    toggleButton.setPressable(customCommand::toggle);
    toggleButton.addId("toggleButton");
    addChild(toggleButton);
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
    boolean enabled = customCommand.isEnabled();
    if (enabled == lastKnown)
      return;

    toggleButton.updateIcon(Icon.sprite16(textures, enabled ? 1 : 2, 0));
    lastKnown = enabled;
  }
}
