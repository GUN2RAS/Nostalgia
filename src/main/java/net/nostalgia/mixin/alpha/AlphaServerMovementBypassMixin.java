package net.nostalgia.mixin.alpha;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.world.dimension.ModDimensions;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class AlphaServerMovementBypassMixin {

    @Unique
    private boolean nostalgia$isFloatyZone(Entity entity) {
        if (entity.level().dimension() == ModDimensions.RD_132211_LEVEL_KEY) return true;
        if (entity.level().tickRateManager() instanceof TickRateManagerAccess access) {
            return access.nostalgia$isChunkFrozen(entity.level().dimension(), entity.chunkPosition());
        }
        return false;
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void globallyBypassModernPhysicsInAlpha(MoverType type, Vec3 movement, CallbackInfo ci) {
        if ((Object) this instanceof Player player) {
            if (!player.isSpectator()) {
                boolean inAlpha = player.level().dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY;
                boolean inFloaty = nostalgia$isFloatyZone(player);
                if (inAlpha || inFloaty) {
                    player.setPos(player.getX() + movement.x, player.getY() + movement.y, player.getZ() + movement.z);
                
                    if (inAlpha && player.onGround() && player.fallDistance > 0.0F) {
                        player.causeFallDamage(player.fallDistance, 1.0F, player.damageSources().fall());
                        player.fallDistance = 0.0F;
                    }

                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void globallyInjectAlphaBlockIntersections(CallbackInfo ci) {
        if ((Object) this instanceof Player player) {
            if (player.level().dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY && !player.isSpectator()) {
                net.minecraft.world.phys.AABB box = player.getBoundingBox().inflate(0.1);
                net.minecraft.core.BlockPos minPos = net.minecraft.core.BlockPos.containing(box.minX, box.minY, box.minZ);
                net.minecraft.core.BlockPos maxPos = net.minecraft.core.BlockPos.containing(box.maxX, box.maxY, box.maxZ);
                if (player.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                    net.minecraft.core.BlockPos.MutableBlockPos mutablePos = new net.minecraft.core.BlockPos.MutableBlockPos();
                    for (int x = minPos.getX(); x <= maxPos.getX(); ++x) {
                        for (int y = minPos.getY(); y <= maxPos.getY(); ++y) {
                            for (int z = minPos.getZ(); z <= maxPos.getZ(); ++z) {
                                mutablePos.set(x, y, z);
                                net.minecraft.world.level.block.state.BlockState bs = serverLevel.getBlockState(mutablePos);
                                if (bs.is(net.nostalgia.block.AlphaBlocks.ALPHA_CACTUS) || bs.is(net.minecraft.world.level.block.Blocks.CACTUS)) {
                                    player.hurtServer(serverLevel, player.damageSources().cactus(), 1.0F);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
