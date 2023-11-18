package net.crazy.streamchat.core.commands.variables;

import net.crazy.streamchat.core.StreamChat;
import net.crazy.streamchat.core.commands.CommandVariables;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.network.server.ServerData;
import net.labymod.api.client.world.ClientWorld;

public class MinecraftVariables extends Variables {
  private final Minecraft minecraft;

  public MinecraftVariables(StreamChat addon) {
    super(addon);
    this.minecraft = addon.labyAPI().minecraft();

    this.provider.register(CommandVariables.HEALTH, this::getHealth);
    this.provider.register(CommandVariables.FOOD, this::getFood);
    this.provider.register(CommandVariables.USERNAME, this::getName);
    this.provider.register(CommandVariables.VERSION, this::getVersion);
    this.provider.register(CommandVariables.SERVER, this::getServer);
    this.provider.register(CommandVariables.BIOME, this::getBiome);
    this.provider.register(CommandVariables.GAMEMODE, this::getGameMode);
  }

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

  public String getName() {
    if (minecraft.getClientPlayer() == null)
      return unknown;
    return minecraft.getClientPlayer().getName();
  }

  public String getVersion() {
    return minecraft.getVersion();
  }

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

  public String getBiome() {
    ClientWorld world = addon.labyAPI().minecraft().clientWorld();
    if (world == null)
      return "Unknown";

    return world.getReadableBiomeName();
  }

  public String getGameMode() {
    if (minecraft.getClientPlayer() == null)
      return unknown;
    return minecraft.getClientPlayer().gameMode().getName();
  }
}
