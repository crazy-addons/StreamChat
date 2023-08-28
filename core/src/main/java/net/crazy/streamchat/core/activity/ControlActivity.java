package net.crazy.streamchat.core.activity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.crazy.streamchat.api.events.TwitchSendMessage;
import net.crazy.streamchat.core.StreamChat;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.entry.HorizontalListEntry;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;

@AutoActivity
@Link("control.lss")
public class ControlActivity extends SimpleActivity {

  private final StreamChat addon;
  private final ResourceLocation textures = ResourceLocation.create("streamchat",
      "themes/vanilla/textures/controls.png");

  public ControlActivity(StreamChat addon) {
    this.addon = addon;
  }

  private ButtonWidget statusButton, restartButton, sendButton;
  private TextFieldWidget inputField;
  private final Icon startIcon = Icon.sprite16(textures, 0, 0),
      stopIcon = Icon.sprite16(textures, 1, 0),
      restartIcon = Icon.sprite16(textures, 2, 0);

  VerticalListWidget<ComponentWidget> messageHistory;

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    boolean isConnected = addon.getBot().isConnected();


    HorizontalListWidget controlList = new HorizontalListWidget();
    controlList.addId("controls");

    /* Buttons */
    statusButton = ButtonWidget.i18n("streamchat.activities.control." +
        (!isConnected ? "start" : "stop"), this::changeStatus);
    statusButton.updateIcon(!isConnected ? startIcon : stopIcon);
    statusButton.addId("statusButton");
    controlList.addChild(new HorizontalListEntry(statusButton));

    restartButton = ButtonWidget.text("Restart", this::restart);
    restartButton.updateIcon(restartIcon);
    restartButton.addId("restartButton");
    controlList.addChild(new HorizontalListEntry(restartButton));
    if (!addon.getBot().isConnected())
      restartButton.setEnabled(false);

    document().addChild(controlList);

    /* Messages */
    this.messageHistory = new VerticalListWidget<>();
    messageHistory.addId("messageHistory");
    ScrollWidget scrollWidget = new ScrollWidget(this.messageHistory);
    scrollWidget.addId("historyScroll");

    for (ComponentWidget widget : addon.getMessageHistory())
      messageHistory.addChild(widget);

    document().addChild(scrollWidget);

    /* Input */
    HorizontalListWidget inputList = new HorizontalListWidget();
    inputList.addId("inputList");

    inputField = new TextFieldWidget();
    inputField.addId("chatInput");
    inputField.placeholder(Component.translatable("streamchat.activities.control.placeholder"));
    inputField.submitHandler((string) -> this.sendMessage());
    inputList.addChild(new HorizontalListEntry(inputField));

    sendButton = new ButtonWidget();
    sendButton.addId("sendButton");
    sendButton.setPressable(this::sendMessage);
    sendButton.updateIcon(Icon.sprite16(textures, 3, 0));
    inputList.addChild(new HorizontalListEntry(sendButton));
    document().addChild(inputList);
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);

    int shownMessages = messageHistory.getChildren().size();
    int availableMessages = addon.getMessageHistory().size();

    if (shownMessages >= availableMessages)
      return;


    for (int i = Math.max(shownMessages - 1, 0);
         messageHistory.getChildren().size() != addon.getMessageHistory().size(); i++) {
      ComponentWidget widget = addon.getMessageHistory().get(i);
      messageHistory.addChild(widget);
    }

    this.reload();
  }

  private void changeStatus() {
    if (addon.getBot().isConnected()) {
      addon.getBot().stop();
      this.statusButton.updateComponent(Component.text("Start"));
      this.statusButton.updateIcon(startIcon);
      this.restartButton.setEnabled(false);
      return;
    }

    addon.getBot().start();
    this.statusButton.updateComponent(Component.text("Stop"));
    this.statusButton.updateIcon(stopIcon);
    this.restartButton.setEnabled(true);
  }

  private void restart() {
    this.statusButton.setEnabled(false);
    this.restartButton.setEnabled(false);

    addon.getBot().restart();

    final ScheduledExecutorService se = Executors.newSingleThreadScheduledExecutor();
    se.schedule(() -> {
      this.statusButton.setEnabled(true);
      this.restartButton.setEnabled(true);
    }, 3, TimeUnit.SECONDS);
  }

  private void sendMessage() {
    if (!addon.getBot().isConnected())
      return;

    String message = inputField.getText();

    if (message == null || message.isEmpty() || message.isBlank())
      return;

    inputField.setText("");
    addon.labyAPI().eventBus().fire(new TwitchSendMessage(message));
  }
}
