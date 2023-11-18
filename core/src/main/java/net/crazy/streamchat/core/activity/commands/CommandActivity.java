package net.crazy.streamchat.core.activity.commands;

import net.crazy.streamchat.core.StreamChat;
import net.crazy.streamchat.core.activity.widgets.HelpWidget;
import net.crazy.streamchat.core.activity.widgets.InputGroupWidget;
import net.crazy.streamchat.core.activity.widgets.InputGroupWidget.InputError;
import net.crazy.streamchat.core.commands.CommandVariables;
import net.crazy.streamchat.core.commands.CustomCommand;
import net.crazy.streamchat.core.commands.CustomCommand.CustomCommandBuilder;
import net.crazy.streamchat.core.events.EditCustomCommandEvent;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.action.ListSession;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.TilesGridWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.I18n;

@AutoActivity
@Link("commands.lss")
public class CommandActivity extends Activity {
  private final StreamChat addon = StreamChat.addon;
  private final ResourceLocation textures = ResourceLocation.create("streamchat",
      "themes/vanilla/textures/commands.png");
  private final String i18nKey = "streamchat.activities.commands.";

  private DivWidget mainPage, secondPage, helpPage;

  private ButtonWidget addButton, helpButton;

  private InputGroupWidget nameInput, triggerInput, responseInput;

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    /* Main Page */
    mainPage = new DivWidget()
        .addId("mainPage");
    mainPage.setVisible(true);
    /* MP-Buttons */
    DivWidget buttonContainer = new DivWidget();
    buttonContainer.addId("buttonContainer");

    addButton = ButtonWidget.i18n(i18nKey + "buttons.add", () -> {
      nameInput.setText("");
      triggerInput.setText("");
      responseInput.setText("");
      this.switchPage();
    }).addId("addButton");
    //addButton.updateIcon(NamedResources.plus);
    buttonContainer.addChild(addButton);
    mainPage.addChild(buttonContainer);

    /* MP-Overview */
    TilesGridWidget<CustomCommandWidget> commandsGrid = new TilesGridWidget<>();
    commandsGrid.addId("commandsGrid");

    for (CustomCommand customCommand : addon.configuration().customCommands.values())
      commandsGrid.addTile(new CustomCommandWidget(customCommand));

    ScrollWidget scrollWidget = new ScrollWidget(commandsGrid, new ListSession<>())
        .addId("commandsScroll");
    mainPage.addChild(scrollWidget);
    document().addChild(mainPage);

    /* Add Page */
    secondPage = new DivWidget()
        .addId("secondPage");
    secondPage.setVisible(false);

    DivWidget buttons = new DivWidget()
        .addId("buttons");

    ButtonWidget backButton = ButtonWidget.i18n(i18nKey + "buttons.back", this::switchPage)
        .addId("backButton");
    //backButton.updateIcon(Icon.sprite16(textures, 5, 0));
    buttons.addChild(backButton);

    helpButton = ButtonWidget.text("?")
    //ButtonWidget.icon(Icon.sprite16(textures, 6, 0))
        .addId("helpButton");
    helpButton.setPressable(this::switchHelp);
    buttons.addChild(helpButton);
    secondPage.addChild(buttons);

    DivWidget inputContainer = new DivWidget()
        .addId("inputContainer");

    /* Command */
    nameInput = new InputGroupWidget(i18nKey + "name").addId("nameInput");
    inputContainer.addChild(nameInput);

    triggerInput = new InputGroupWidget(i18nKey + "trigger", "w-75").addId("triggerInput");
    inputContainer.addChild(triggerInput);

    responseInput = new InputGroupWidget(i18nKey + "response", "w-75").addId("responseInput");
    inputContainer.addChild(responseInput);

    ButtonWidget saveButton = ButtonWidget.i18n(i18nKey + "buttons.save", this::saveCommand)
        .addId("saveButton");
    //saveButton.updateIcon(Icon.sprite16(textures, 4, 0));
    inputContainer.addChild(saveButton);

    secondPage.addChild(inputContainer);
    document().addChild(secondPage);

    helpPage = new DivWidget().addId("helpPage");
    helpPage.setVisible(false);

    ButtonWidget helpBackButton = ButtonWidget.i18n(i18nKey + "buttons.back", this::switchHelp)
        .addId("backButton");
    //helpBackButton.updateIcon(Icon.sprite16(textures, 5, 0));
    helpPage.addChild(helpBackButton);

    VerticalListWidget<Widget> helpList = new VerticalListWidget<>().addId("helpList");
    ScrollWidget helpScroll = new ScrollWidget(helpList);

    helpList.addChild(new HelpWidget("name"));
    helpList.addChild(new HelpWidget("triggers"));
    helpList.addChild(new HelpWidget("response"));
    helpList.addChild(new HelpWidget("variable"));

    for (CommandVariables variable : CommandVariables.values()) {
      String section = i18nKey + "help.variables." + variable.getIdentifier();
      String entry = String.format("§6%s §f-§7 %s", I18n.getTranslation(section + ".title"),
          I18n.getTranslation(section + ".description"));

      helpList.addChild(ComponentWidget.text(entry));
    }

    helpPage.addChild(helpScroll);
    document().addChild(helpPage);
  }

  private boolean editing = false;
  private CustomCommand editBackup = null;

  @Subscribe
  public void onEdit(EditCustomCommandEvent event) {
    CustomCommand customCommand = event.getCustomCommand();
    editing = true;
    editBackup = customCommand;
    addon.configuration().customCommands.remove(customCommand.getName().toLowerCase());

    nameInput.setText(customCommand.getName());
    responseInput.setText(customCommand.getResponse());

    StringBuilder triggerBuilder = new StringBuilder();
    for (int i = 0; i < customCommand.getTriggers().size(); i++) {
      String trigger = customCommand.getTriggers().get(i);
      triggerBuilder.append(trigger);

      if (i != customCommand.getTriggers().size() - 1)
        triggerBuilder.append(", ");
    }
    triggerInput.setText(triggerBuilder.toString());

    this.switchPage();
  }

  private void switchPage() {
    if (secondPage.isVisible() && editing) {
      addon.configuration().customCommands.put(editBackup.getName().toLowerCase(), editBackup);
      editing = false;
      editBackup = null;
    }

    mainPage.setVisible(!mainPage.isVisible());
    secondPage.setVisible(!mainPage.isVisible());
  }

  private void switchHelp() {
    secondPage.setVisible(!secondPage.isVisible());
    helpPage.setVisible(!helpPage.isVisible());
  }

  private void saveCommand() {
    nameInput.valid();
    triggerInput.valid();
    responseInput.valid();

    boolean triggerValid = triggerInput.validate("[^a-zA-Z,\\s]+", false);
    boolean valid = nameInput.validate("[^a-zA-Z]+",
        addon.configuration().customCommands.containsKey(nameInput.getText().toLowerCase()))
        && responseInput.validate("", false)
        && triggerValid;

    if (triggerValid) {
      String[] triggers = triggerInput.getText().replaceAll("\\s", "")
          .split(",");
      for (String trigger : triggers) {
        if (addon.configuration().triggerExists(trigger)) {
          triggerInput.invalid(InputError.EXISTS);
          valid = false;
          break;
        }
      }
    }

    if (!valid)
      return;

    String name = nameInput.getText();
    String[] trigger = triggerInput.getText().toLowerCase().split(",");
    String response = responseInput.getText();

    addon.configuration().customCommands.put(name.toLowerCase(),
        new CustomCommandBuilder().setName(name).addTriggers(trigger).setResponse(response).create());
    this.switchPage();
    this.reload();

    nameInput.setText("");
    triggerInput.setText("");
    responseInput.setText("");
  }
}
