package net.crazy.streamchat.core.commands.variables;

import net.crazy.streamchat.core.StreamChat;
import net.crazy.streamchat.core.commands.VariableProvider;
import net.labymod.api.util.I18n;

public abstract class Variables {
  protected final StreamChat addon;
  protected final VariableProvider provider;
  protected final String unknown;

  public Variables(StreamChat addon) {
    this.addon = addon;
    this.provider = StreamChat.variableProvider;
    this.unknown = I18n.getTranslation("streamchat.messages.commands.unknown");
  }
}
