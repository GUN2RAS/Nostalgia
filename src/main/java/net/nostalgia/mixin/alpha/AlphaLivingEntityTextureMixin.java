package net.nostalgia.mixin.alpha;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.nostalgia.world.rules.DimensionRules;
import net.nostalgia.world.rules.NostalgiaDimensionRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntityRenderer.class)
public class AlphaLivingEntityTextureMixin {
    @ModifyVariable(method = "getRenderType", at = @At("STORE"), ordinal = 0)
    private Identifier nostalgia$overrideEntityTexture(Identifier original, LivingEntityRenderState state, boolean p1, boolean p2, boolean p3) {
        if (Minecraft.getInstance().level != null) {
            DimensionRules rules = NostalgiaDimensionRules.getRules(Minecraft.getInstance().level);
            if (rules != null) {
                Identifier override = rules.getOverriddenTexture(null, original);
                if (override != null) {
                    return override;
                }
            }
        }
        return original;
    }
}
