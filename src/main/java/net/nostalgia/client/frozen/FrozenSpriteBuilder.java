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

    private FrozenSpriteBuilder() {}

    public static SpriteContents buildFrozenFromLive(SpriteContents live) {
        if (live == null) return null;
        if (!live.isAnimated()) return null;

        int frameW = live.width();
        int frameH = live.height();
        if (frameW <= 0 || frameH <= 0) return null;

        NativeImage src;
        try {
            src = ((SpriteContentsAccessor) (Object) live).nostalgia$getOriginalImage();
        } catch (Throwable t) {
            return null;
        }
        if (src == null) return null;
        if (src.getWidth() < frameW || src.getHeight() < frameH) return null;

        NativeImage firstFrame = new NativeImage(frameW, frameH, false);
        src.copyRect(firstFrame, 0, 0, 0, 0, frameW, frameH, false, false);

        Identifier frozenId = FrozenSpriteRegistry.toFrozenId(live.name());
        FrozenSpriteRegistry.registerMapping(live.name(), frozenId);

        return new SpriteContents(frozenId, new FrameSize(frameW, frameH), firstFrame);
    }
}
