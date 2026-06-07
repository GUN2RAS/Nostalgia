package net.nostalgia.mixin.client.frozen;

import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.nostalgia.client.frozen.FrozenSpriteRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(TextureAtlas.class)
public abstract class TextureAtlasMixin {

    @Inject(method = "upload", at = @At("TAIL"))
    private void nostalgia$captureFrozenSprites(SpriteLoader.Preparations preparations, CallbackInfo ci) {
        TextureAtlas self = (TextureAtlas) (Object) this;
        if (!TextureAtlas.LOCATION_BLOCKS.equals(self.location())) return;

        for (Map.Entry<Identifier, TextureAtlasSprite> e : preparations.regions().entrySet()) {
            Identifier id = e.getKey();
            if (id == null) continue;
            if (!FrozenSpriteRegistry.FROZEN_NAMESPACE.equals(id.getNamespace())) continue;
            if (!id.getPath().startsWith(FrozenSpriteRegistry.FROZEN_PATH_PREFIX)) continue;
            FrozenSpriteRegistry.registerSprite(id, e.getValue());
        }
        FrozenSpriteRegistry.bindFrozenByLive();
    }
}
