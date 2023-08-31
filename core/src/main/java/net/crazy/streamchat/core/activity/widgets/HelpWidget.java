package net.crazy.streamchat.core.activity.widgets;

import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;

@AutoWidget
@Link("elements.lss")
public class HelpWidget extends SimpleWidget {

  private final ComponentWidget title, description;

  public HelpWidget(String i18nSection) {
    String i18nKey = String.format("streamchat.activities.commands.help.%s.", i18nSection);
    this.title = ComponentWidget.i18n(i18nKey + "title").addId("title");
    this.description = ComponentWidget.i18n(i18nKey + "description").addId("description");
  }


  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    addChild(title);
    addChild(description);
  }
}
