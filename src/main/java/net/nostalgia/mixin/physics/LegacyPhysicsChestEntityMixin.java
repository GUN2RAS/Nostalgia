package net.nostalgia.mixin.physics;

import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlockEntity.class)
public abstract class LegacyPhysicsChestEntityMixin {

    @Inject(method = "getOpenNess", at = @At("HEAD"), cancellable = true)
    private void nostalgia$disableLidAnimation(float partialTicks, CallbackInfoReturnable<Float> cir) {
        
        try {
            ChestBlockEntity entity = (ChestBlockEntity) (Object) this;
            if (entity.getLevel() != null && (entity.getBlockState().is(net.nostalgia.block.AlphaBlocks.ALPHA_CHEST) || net.nostalgia.world.rules.NostalgiaRules.getForLevel(entity.getLevel()).legacyChest)) {
                cir.setReturnValue(0.0F);
            }
        } catch (Exception e) {
            
        }
    }
}
