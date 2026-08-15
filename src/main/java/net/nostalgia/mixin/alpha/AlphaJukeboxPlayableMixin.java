package net.nostalgia.mixin.alpha;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.nostalgia.block.AlphaBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JukeboxPlayable.class)
public class AlphaJukeboxPlayableMixin {

    @Inject(method = "tryInsertIntoJukebox", at = @At("HEAD"), cancellable = true)
    private static void nostalgia$allowAlphaJukebox(Level level, BlockPos pos, ItemStack toInsert, Player player, CallbackInfoReturnable<InteractionResult> cir) {
        JukeboxPlayable jukeboxPlayable = toInsert.get(DataComponents.JUKEBOX_PLAYABLE);
        if (jukeboxPlayable == null) {
            return;
        }

        BlockState state = level.getBlockState(pos);
        if (state.is(AlphaBlocks.ALPHA_JUKEBOX) && !state.getValue(JukeboxBlock.HAS_RECORD)) {
            if (!level.isClientSide()) {
                ItemStack inserted = toInsert.consumeAndReturn(1, player);
                if (level.getBlockEntity(pos) instanceof JukeboxBlockEntity jukebox) {
                    jukebox.setTheItem(inserted);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
                }

                player.awardStat(Stats.PLAY_RECORD);
            }

            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
