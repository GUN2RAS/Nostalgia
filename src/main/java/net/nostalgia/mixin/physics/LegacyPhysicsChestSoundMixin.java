package net.nostalgia.mixin.physics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.sound.AlphaSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public abstract class LegacyPhysicsChestSoundMixin {

    @Inject(method = "playSound", at = @At("HEAD"), cancellable = true)
    private static void nostalgia$replaceChestSound(Level level, BlockPos worldPosition, BlockState blockState, SoundEvent event, CallbackInfo ci) {
        if (level != null && (blockState.is(net.nostalgia.block.AlphaBlocks.ALPHA_CHEST) || net.nostalgia.world.rules.NostalgiaRules.getForLevel(level).legacySounds)) {
            SoundEvent newEvent = event;
            if (event == net.minecraft.sounds.SoundEvents.CHEST_OPEN) {
                newEvent = AlphaSounds.RANDOM_CHEST_OPEN;
            } else if (event == net.minecraft.sounds.SoundEvents.CHEST_CLOSE) {
                newEvent = AlphaSounds.RANDOM_CHEST_CLOSE;
            }
            if (newEvent != event) {
                net.minecraft.world.level.block.state.properties.ChestType type = blockState.getValue(ChestBlock.TYPE);
                if (type != net.minecraft.world.level.block.state.properties.ChestType.LEFT) {
                    double x = worldPosition.getX() + 0.5;
                    double y = worldPosition.getY() + 0.5;
                    double z = worldPosition.getZ() + 0.5;
                    if (type == net.minecraft.world.level.block.state.properties.ChestType.RIGHT) {
                        Direction direction = ChestBlock.getConnectedDirection(blockState);
                        x += direction.getStepX() * 0.5;
                        z += direction.getStepZ() * 0.5;
                    }
                    level.playSound(null, x, y, z, newEvent, SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                }
                ci.cancel();
            }
        }
    }
}
