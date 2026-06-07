package net.nostalgia.mixin.alpha;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntityRenderer.class)
public class AlphaLivingEntityTextureMixin {

    private static final java.util.Map<String, java.util.Optional<Identifier>> CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    private static java.util.Optional<Identifier> getCached(Identifier original) {
        return CACHE.computeIfAbsent(original.getPath(), path -> {
            if (path.contains("pig")) return java.util.Optional.of(Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/pig/pig_alpha.png"));
            if (path.contains("cow")) return java.util.Optional.of(Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/cow/cow_alpha.png"));
            if (path.contains("sheep") && !path.contains("fur")) return java.util.Optional.of(Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/sheep/sheep_alpha.png"));
            if (path.contains("chicken")) return java.util.Optional.of(Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/chicken_alpha.png"));
            if (path.contains("zombie")) return java.util.Optional.of(Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/zombie/zombie_alpha.png"));
            if (path.contains("skeleton")) return java.util.Optional.of(Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/skeleton/skeleton_alpha.png"));
            if (path.contains("spider")) return java.util.Optional.of(Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/spider/spider_alpha.png"));
            if (path.contains("creeper")) return java.util.Optional.of(Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/creeper/creeper_alpha.png"));
            return java.util.Optional.empty();
        });
    }

    @ModifyVariable(method = "getRenderType", at = @At("STORE"), ordinal = 0)
    private Identifier nostalgia$overrideEntityTexture(Identifier original, LivingEntityRenderState state, boolean p1, boolean p2, boolean p3) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            java.util.Optional<Identifier> cached = getCached(original);
            if (cached.isPresent()) {
                return cached.get();
            }
        }
        return original;
    }
}
