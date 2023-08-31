package net.crazy.streamchat.core.commands;

import net.crazy.streamchat.core.StreamChat;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.network.server.ServerData;
import net.labymod.api.client.world.ClientWorld;
import net.labymod.api.util.I18n;

public class VariableProviders {
  private final StreamChat addon;
  private final Minecraft minecraft;
  private final String unknown;

  public VariableProviders() {
    this.addon = StreamChat.addon;
    this.minecraft = addon.labyAPI().minecraft();
    this.unknown = I18n.getTranslation("streamchat.messages.commands.unknown");
  }

  @VariableProvider(identifier = "health")
  public String getHealth() {
    if (minecraft.getClientPlayer() == null)
      return unknown;
    return String.valueOf(minecraft.getClientPlayer().getHealth());
  }

  public String getFood() {
    if (minecraft.getClientPlayer() == null)
      return unknown;
    return String.valueOf(minecraft.getClientPlayer().foodData().getFoodLevel());
  }

  @VariableProvider(identifier = "username")
  public String getName() {
    if (minecraft.getClientPlayer() == null)
      return unknown;
    return minecraft.getClientPlayer().getName();
  }

  @VariableProvider(identifier = "version")
  public String getVersion() {
    return minecraft.getVersion();
  }

  @VariableProvider(identifier = "server")
  public String getServer() {
    ServerData serverData = addon.labyAPI().serverController().getCurrentServerData();
    if (serverData == null)
      return "Unknown";

    if (serverData.isLan())
      return "LAN";

    if (serverData.isRealm())
      return "REALM";

    return serverData.address().getHost();
  }

  @VariableProvider(identifier = "biome")
  public String getBiome() {
    ClientWorld world = addon.labyAPI().minecraft().clientWorld();
    if (world == null)
      return "Unknown";

    return world.getReadableBiomeName();
  }
}
