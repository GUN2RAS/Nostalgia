package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import net.nostalgia.client.ritual.ClientFreezeRegions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelAnimateTickFreezeMixin {

    @Inject(
            method = "doAnimateTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
            ),
            cancellable = true
    )
    private void nostalgia$skipAnimateInZone(
            int xt, int yt, int zt, int r,
            RandomSource animateRandom,
            @Nullable Block markerParticleTarget,
            BlockPos.MutableBlockPos pos,
            CallbackInfo ci
    ) {
        TickRateManagerAccess access = ClientFreezeRegions.access();
        if (access == null || !access.nostalgia$hasRegions()) return;
        ClientLevel self = (ClientLevel) (Object) this;
        long key = ChunkPos.pack(pos.getX() >> 4, pos.getZ() >> 4);
        if (access.nostalgia$isChunkFrozen(self.dimension(), key)) {
            ci.cancel();
        }
    }
}
