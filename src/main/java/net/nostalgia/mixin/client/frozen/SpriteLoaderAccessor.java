package net.nostalgia.mixin.client.frozen;

import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SpriteLoader.class)
public interface SpriteLoaderAccessor {
    @Accessor("location") Identifier nostalgia$getLocation();
}
