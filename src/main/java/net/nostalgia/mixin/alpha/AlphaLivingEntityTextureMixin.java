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

    @ModifyVariable(method = "getRenderType", at = @At("STORE"), ordinal = 0)
    private Identifier nostalgia$overrideEntityTexture(Identifier original, LivingEntityRenderState state, boolean p1, boolean p2, boolean p3) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            String path = original.getPath();
            
            if (path.contains("pig")) {
                return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/pig/pig_alpha.png");
            }
            if (path.contains("cow")) {
                return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/cow/cow_alpha.png");
            }
            if (path.contains("sheep") && !path.contains("fur")) {
                return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/sheep/sheep_alpha.png");
            }
            if (path.contains("chicken")) {
                return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/chicken_alpha.png");
            }
            if (path.contains("zombie")) {
                return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/zombie/zombie_alpha.png");
            }
            if (path.contains("skeleton")) {
                return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/skeleton/skeleton_alpha.png");
            }
            if (path.contains("spider")) {
                return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/spider/spider_alpha.png");
            }
            if (path.contains("creeper")) {
                return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/creeper/creeper_alpha.png");
            }
        }
        return original;
    }
}
