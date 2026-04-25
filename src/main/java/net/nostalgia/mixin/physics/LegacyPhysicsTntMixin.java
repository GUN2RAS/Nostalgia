package net.nostalgia.mixin.physics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.level.block.state.BlockBehaviour.class)
public abstract class LegacyPhysicsTntMixin {

    @Inject(method = "attack", at = @At("HEAD"))
    private void nostalgia$igniteTntOnPunch(BlockState state, Level level, BlockPos pos, Player player, CallbackInfo ci) {
        if (!level.isClientSide() && ((Object) this instanceof TntBlock)) {
            if (state.is(net.nostalgia.block.AlphaBlocks.ALPHA_TNT) || net.nostalgia.world.rules.NostalgiaRules.getForLevel(level).tntIgnitesOnPunch) {
                TntBlock.prime(level, pos);
                level.removeBlock(pos, false);
            }
        }
    }
}
