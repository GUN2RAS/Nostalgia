package net.nostalgia.mixin.physics;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.nostalgia.sound.AlphaSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DoorBlock.class)
public abstract class LegacyPhysicsDoorSoundMixin {

    @Inject(method = "playSound", at = @At("HEAD"), cancellable = true)
    private void nostalgia$replaceDoorSound(Entity entity, Level level, BlockPos pos, boolean isOpening, CallbackInfo ci) {
        if (net.nostalgia.world.rules.NostalgiaRules.getForLevel(level).legacySounds) {
            level.playSound(
                    entity,
                    pos,
                    isOpening ? AlphaSounds.RANDOM_DOOR_OPEN : AlphaSounds.RANDOM_DOOR_CLOSE,
                    SoundSource.BLOCKS,
                    1.0F,
                    level.getRandom().nextFloat() * 0.1F + 0.9F
            );
            ci.cancel();
        }
    }
}
