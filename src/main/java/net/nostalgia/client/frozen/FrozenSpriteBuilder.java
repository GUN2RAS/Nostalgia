package net.nostalgia.client.frozen;

import com.mojang.blaze3d.platform.NativeImage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.Identifier;
import net.nostalgia.mixin.client.frozen.SpriteContentsAccessor;

@Environment(EnvType.CLIENT)
public final class FrozenSpriteBuilder {
  private FrozenSpriteBuilder() {
  }

  public static SpriteContents buildFrozenFromLive(SpriteContents live) {
    if (live == null) {
      return null;
    } else if (!live.isAnimated()) {
      return null;
    } else {
      int frameW = live.width();
      int frameH = live.height();
      if (frameW > 0 && frameH > 0) {
        NativeImage src;
        try {
          src = ((SpriteContentsAccessor)live).nostalgia$getOriginalImage();
        } catch (Throwable var6) {
          return null;
        }

        if (src == null) {
          return null;
        } else if (src.getWidth() >= frameW && src.getHeight() >= frameH) {
          NativeImage firstFrame = new NativeImage(frameW, frameH, false);
          src.copyRect(firstFrame, 0, 0, 0, 0, frameW, frameH, false, false);
          Identifier frozenId = FrozenSpriteRegistry.toFrozenId(live.name());
          FrozenSpriteRegistry.registerMapping(live.name(), frozenId);
          return new SpriteContents(frozenId, new FrameSize(frameW, frameH), firstFrame);
        } else {
          return null;
        }
      } else {
        return null;
      }
    }
  }
}
