package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.world.level.ChunkPos;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import net.nostalgia.client.ritual.ClientFreezeRegions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ParticleGroup.class)
public abstract class ParticleGroupTickFreezeMixin {

    @Redirect(
            method = "tickParticle",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;tick()V")
    )
    private void nostalgia$freezeIfInZone(Particle particle) {
        TickRateManagerAccess access = ClientFreezeRegions.access();
        if (access == null || !access.nostalgia$hasRegions()) {
            particle.tick();
            return;
        }
        ParticleAccessor acc = (ParticleAccessor) (Object) particle;
        ClientLevel level = acc.nostalgia$getLevel();
        if (level == null) {
            particle.tick();
            return;
        }
        double x = acc.nostalgia$getX();
        double z = acc.nostalgia$getZ();
        long chunkKey = ChunkPos.pack((int) Math.floor(x) >> 4, (int) Math.floor(z) >> 4);
        if (access.nostalgia$isChunkFrozen(level.dimension(), chunkKey)) {
            acc.nostalgia$setXo(x);
            acc.nostalgia$setYo(acc.nostalgia$getY());
            acc.nostalgia$setZo(z);
            acc.nostalgia$setXd(0.0);
            acc.nostalgia$setYd(0.0);
            acc.nostalgia$setZd(0.0);
            return;
        }
        particle.tick();
    }
}
