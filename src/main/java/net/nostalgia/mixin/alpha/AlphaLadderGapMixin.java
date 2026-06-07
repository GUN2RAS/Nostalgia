package net.nostalgia.mixin.alpha;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class AlphaLadderGapMixin extends net.minecraft.world.entity.Entity {

    public AlphaLadderGapMixin(net.minecraft.world.entity.EntityType<?> entityType, net.minecraft.world.level.Level level) {
        super(entityType, level);
    }

    @Shadow
    protected Optional<BlockPos> lastClimbablePos;

    @Inject(method = "onClimbable", at = @At("RETURN"), cancellable = true)
    private void nostalgia$allowLadderGaps(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && !this.isSpectator()) {
            AABB box = this.getBoundingBox();
            
            int minX = Mth.floor(box.minX);
            int minY = Mth.floor(box.minY);
            int minZ = Mth.floor(box.minZ);
            
            int maxX = Mth.floor(box.maxX);
            int maxY = Mth.floor(box.maxY);
            int maxZ = Mth.floor(box.maxZ);

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        BlockState state = this.level().getBlockState(pos);
                        
                        if (state.is(net.nostalgia.block.AlphaBlocks.ALPHA_LADDER)) {
                            this.lastClimbablePos = Optional.of(pos);
                            cir.setReturnValue(true);
                            return;
                        }
                    }
                }
            }
        }
    }
}
