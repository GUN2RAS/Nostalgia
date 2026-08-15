package net.nostalgia.alphalogic.core;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class AlphaRenderState {
  public static int renderDistanceMode = 0;

  public AlphaRenderState() {
  }

  public static void cycleRenderDistance() {
    renderDistanceMode = (renderDistanceMode + 1) % 4;
    String modeName = "";
    switch (renderDistanceMode) {
      case 0:
        modeName = "Far";
        break;
      case 1:
        modeName = "Normal";
        break;
      case 2:
        modeName = "Short";
        break;
      case 3:
        modeName = "Tiny";
    }

    if (Minecraft.getInstance().player != null) {
      Minecraft.getInstance().player.sendOverlayMessage(Component.literal("Render distance: " + modeName));
    }
  }

  public static float getFogEnd() {
    switch (renderDistanceMode) {
      case 1:
        return 128.0F;
      case 2:
        return 64.0F;
      case 3:
        return 32.0F;
      default:
        return 256.0F;
    }
  }

  public static float getFogStart() {
    switch (renderDistanceMode) {
      case 1:
        return 32.0F;
      case 2:
        return 16.0F;
      case 3:
        return 8.0F;
      default:
        return 64.0F;
    }
  }

  public static boolean isFogDense() {
    return renderDistanceMode >= 1;
  }

  public static float getCelestialAlpha() {
    float fogEnd = getFogEnd();
    return Mth.clamp((fogEnd - 32.0F) / 224.0F, 0.0F, 1.0F);
  }
}
