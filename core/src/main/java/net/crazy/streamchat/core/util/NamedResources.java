package net.crazy.streamchat.core.util;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class NamedResources {
  public static Icon
      logo = of("streamchat"),
      arrowLeft = of("arrow-left"),
      floppyDisk = of("floppy-disk"),
      pen = of("pen"),
      plus = of("plus"),
      question = of("question"),
      toggleOn = of("toggle-on"),
      toggleOff = of("toggle-off");


  private static final String PATH = "themes/vanilla/textures/%s.png";

  private static Icon of(String iconName) {
    ResourceLocation location = ResourceLocation.create("streamchat",
        String.format(PATH, iconName));
    return Icon.texture(location).resolution(32, 32);
  }
}
