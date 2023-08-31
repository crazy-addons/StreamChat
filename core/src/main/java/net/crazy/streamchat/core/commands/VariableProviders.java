package net.crazy.streamchat.core.commands;

import net.crazy.streamchat.core.StreamChat;
import net.labymod.api.client.Minecraft;

public class VariableProviders {
  private final StreamChat addon;
  private final Minecraft minecraft;

  public VariableProviders() {
    this.addon = StreamChat.addon;
    this.minecraft = addon.labyAPI().minecraft();
  }

  @VariableProvider(identifier = "health")
  public String getHealth() {
    if (minecraft.getClientPlayer() == null)
      return "20";
    return String.valueOf(minecraft.getClientPlayer().getHealth());
  }

  @VariableProvider(identifier = "username")
  public String getName() {
    if (minecraft.getClientPlayer() == null)
      return "Unknown";
    return minecraft.getClientPlayer().getName();
  }

  @VariableProvider(identifier = "version")
  public String getVersion() {
    return minecraft.getVersion();
  }
}
