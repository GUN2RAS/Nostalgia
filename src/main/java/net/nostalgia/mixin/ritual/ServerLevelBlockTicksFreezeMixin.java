package net.nostalgia.mixin.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.BiConsumer;

@Mixin(ServerLevel.class)
public abstract class ServerLevelBlockTicksFreezeMixin {

    @ModifyArg(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ticks/LevelTicks;tick(JILjava/util/function/BiConsumer;)V", ordinal = 0),
            index = 2
    )
    private BiConsumer<BlockPos, Block> nostalgia$wrapBlockTicks(BiConsumer<BlockPos, Block> original) {
        ServerLevel self = (ServerLevel) (Object) this;
        if (!(self.tickRateManager() instanceof TickRateManagerAccess access) || !access.nostalgia$hasRegions()) return original;
        return (pos, block) -> {
            if (!access.nostalgia$isBlockFrozen(self.dimension(), pos)) {
                original.accept(pos, block);
            }
        };
    }

    @ModifyArg(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ticks/LevelTicks;tick(JILjava/util/function/BiConsumer;)V", ordinal = 1),
            index = 2
    )
    private BiConsumer<BlockPos, Fluid> nostalgia$wrapFluidTicks(BiConsumer<BlockPos, Fluid> original) {
        ServerLevel self = (ServerLevel) (Object) this;
        if (!(self.tickRateManager() instanceof TickRateManagerAccess access) || !access.nostalgia$hasRegions()) return original;
        return (pos, fluid) -> {
            if (!access.nostalgia$isBlockFrozen(self.dimension(), pos)) {
                original.accept(pos, fluid);
            }
        };
    }
}
