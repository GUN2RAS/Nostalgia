package net.nostalgia.mixin.client;

import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.item.ItemStack;
import net.nostalgia.world.rules.NostalgiaRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonRenderer.class)
public abstract class AlphaSkeletonRendererMixin<T extends AbstractSkeleton, S extends SkeletonRenderState> {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void nostalgia$extractAlphaSkeletonState(T entity, S state, float partialTicks, CallbackInfo ci) {
        if (NostalgiaRules.getForLevel(entity.level()).legacySkeleton) {
            state.isHoldingBow = false;
            state.isAggressive = true;
            state.rightHandItemState.clear();
            state.leftHandItemState.clear();
            state.rightHandItemStack = ItemStack.EMPTY;
            state.leftHandItemStack = ItemStack.EMPTY;
        }
    }
}
