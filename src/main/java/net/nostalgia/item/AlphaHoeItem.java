package net.nostalgia.item;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.nostalgia.block.AlphaBlocks;

public class AlphaHoeItem extends HoeItem {

    protected static final Map<Block, Pair<Predicate<UseOnContext>, Consumer<UseOnContext>>> ALPHA_TILLABLES = Maps.newHashMap(
            ImmutableMap.of(
                    AlphaBlocks.ALPHA_GRASS_BLOCK, Pair.of(HoeItem::onlyIfAirAbove, changeIntoState(AlphaBlocks.ALPHA_FARMLAND.defaultBlockState())),
                    AlphaBlocks.ALPHA_DIRT, Pair.of(HoeItem::onlyIfAirAbove, changeIntoState(AlphaBlocks.ALPHA_FARMLAND.defaultBlockState()))
            )
    );

    public AlphaHoeItem(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline, Item.Properties properties) {
        super(material, attackDamageBaseline, attackSpeedBaseline, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Block block = level.getBlockState(pos).getBlock();

        Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> logicPair = ALPHA_TILLABLES.get(block);
        
        if (logicPair == null) {
            
            return super.useOn(context);
        } else {
            Predicate<UseOnContext> predicate = logicPair.getFirst();
            Consumer<UseOnContext> action = logicPair.getSecond();
            if (predicate.test(context)) {
                Player player = context.getPlayer();
                level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!level.isClientSide()) {
                    action.accept(context);
                    if (player != null) {
                        context.getItemInHand().hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
                    }
                }
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }
    }
}
