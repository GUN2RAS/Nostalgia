package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleAccessor {
    @Accessor("x") double nostalgia$getX();
    @Accessor("y") double nostalgia$getY();
    @Accessor("z") double nostalgia$getZ();
    @Accessor("xo") void nostalgia$setXo(double v);
    @Accessor("yo") void nostalgia$setYo(double v);
    @Accessor("zo") void nostalgia$setZo(double v);
    @Accessor("xd") void nostalgia$setXd(double v);
    @Accessor("yd") void nostalgia$setYd(double v);
    @Accessor("zd") void nostalgia$setZd(double v);
    @Accessor("level") ClientLevel nostalgia$getLevel();
}
