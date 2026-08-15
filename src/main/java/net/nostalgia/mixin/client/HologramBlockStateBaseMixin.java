package net.nostalgia.mixin.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.client.render.HologramMeshTransformBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class HologramBlockStateBaseMixin {

  @Shadow
  public abstract BlockState asState();

  @Shadow
  private BlockBehaviour.OffsetFunction offsetFunction;

  @Inject(method = "getSeed", at = @At("HEAD"), cancellable = true)
  private void nostalgia$getHologramSeed(BlockPos pos, CallbackInfoReturnable<Long> cir) {
    Long seed = HologramMeshTransformBridge.getTransformedSeed(pos);
    if (seed != null) {
      cir.setReturnValue(seed);
    }
  }

  @Inject(method = "getOffset", at = @At("HEAD"), cancellable = true)
  private void nostalgia$getHologramOffset(BlockPos pos, CallbackInfoReturnable<Vec3> cir) {
    if (this.offsetFunction != null) {
      Vec3 offset = HologramMeshTransformBridge.getTransformedOffset(this.asState(), pos, this.offsetFunction);
      if (offset != null) {
        cir.setReturnValue(offset);
      }
    }
  }
}
