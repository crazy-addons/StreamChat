package net.crazy.streamchat.core.activity;

import net.crazy.streamchat.core.StreamChat;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.navigation.elements.ScreenNavigationElement;

public class ControlNavigationElement extends ScreenNavigationElement {

  public ControlNavigationElement(StreamChat addon) {
    super(new ControlActivity(addon));
  }

  @Override
  public Component getDisplayName() {
    return Component.text("StreamChat+");
  }

  @Override
  public Icon getIcon() {
    return null;
  }

  @Override
  public String getWidgetId() {
    return "streamchat.controls";
  }
}
