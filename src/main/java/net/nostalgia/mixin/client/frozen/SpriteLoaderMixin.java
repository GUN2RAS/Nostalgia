package net.nostalgia.mixin.client.frozen;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.nostalgia.client.frozen.FrozenSpriteBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SpriteLoader.class)
public abstract class SpriteLoaderMixin {

    @ModifyVariable(
            method = "stitch",
            at = @At("HEAD"),
            argsOnly = true,
            index = 1
    )
    private List<SpriteContents> nostalgia$injectFrozen(List<SpriteContents> sprites) {
        Identifier loc;
        try {
            loc = ((SpriteLoaderAccessor) this).nostalgia$getLocation();
        } catch (Throwable t) {
            return sprites;
        }
        if (loc == null || !loc.equals(TextureAtlas.LOCATION_BLOCKS)) return sprites;

        List<SpriteContents> extras = new ArrayList<>();
        for (SpriteContents sc : sprites) {
            if (sc == null || !sc.isAnimated()) continue;
            SpriteContents frozen = FrozenSpriteBuilder.buildFrozenFromLive(sc);
            if (frozen != null) extras.add(frozen);
        }
        if (extras.isEmpty()) return sprites;

        List<SpriteContents> combined = new ArrayList<>(sprites.size() + extras.size());
        combined.addAll(sprites);
        combined.addAll(extras);
        return combined;
    }
}
