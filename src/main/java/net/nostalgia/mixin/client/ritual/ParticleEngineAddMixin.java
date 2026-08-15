package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.world.level.ChunkPos;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineAddMixin {

    @Inject(method = "add(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void nostalgia$suppressInZone(Particle p, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (!(mc.level.tickRateManager() instanceof TickRateManagerAccess access)) return;
        if (!access.nostalgia$hasRegions()) return;
        ParticleAccessor acc = (ParticleAccessor) (Object) p;
        double px = acc.nostalgia$getX();
        double pz = acc.nostalgia$getZ();
        long key = ChunkPos.pack((int) Math.floor(px) >> 4, (int) Math.floor(pz) >> 4);
        if (access.nostalgia$isChunkFrozen(mc.level.dimension(), key)) {
            ci.cancel();
        }
    }
}
