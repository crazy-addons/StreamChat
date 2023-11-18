package net.crazy.streamchat.core.activity.widgets;

import java.util.regex.Pattern;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;

@AutoWidget
@Link("elements.lss")
public class InputGroupWidget extends SimpleWidget {
  private final String i18nKey;
  private final ComponentWidget label, error;
  private final TextFieldWidget input;

  public InputGroupWidget(String i18nKey) {
    this(i18nKey, "w-50");
  }

  public InputGroupWidget(String i18nKey, String width) {
    this.i18nKey = i18nKey;
    this.label = ComponentWidget.i18n(i18nKey + ".label").addId("label", "label-" + width);
    this.input = new TextFieldWidget().addId("input", "input-" + width);
    this.input.placeholder(Component.translatable(i18nKey + ".placeholder"));
    this.error = ComponentWidget.text("").addId("error");
    this.error.setVisible(false);
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    addChild(label);
    addChild(input);
    addChild(error);
  }

  public String getText() {
    return input.getText();
  }

  public void setText(String text) {
    input.setText(text);
  }

  public boolean validate(String regex, boolean exists) {
    String text = getText();
    if (text.isEmpty()) {
      invalid(InputError.EMPTY);
      return false;
    }

    if (text.isBlank()) {
      invalid(InputError.CHARACTERS);
      return false;
    }

    if (exists) {
      invalid(InputError.EXISTS);
      return false;
    }

    if (regex == null || regex.isEmpty())
      return true;

    Pattern pattern = Pattern.compile(regex);
    if (pattern.matcher(text).find()) {
      invalid(InputError.CHARACTERS);
      return false;
    }

    return true;
  }

  public void invalid(InputError error) {
    this.error.setComponent(Component.translatable(i18nKey + "." + error.key()));
    this.error.setVisible(true);
    this.input.addId("invalid");
  }

  public void valid() {
    this.error.setVisible(false);
    this.input.removeId("invalid");
  }

  public static enum InputError {
    EMPTY,
    EXISTS,
    CHARACTERS;

    public String key() {
      return "invalid_" + name().toLowerCase();
    }
  }
}
