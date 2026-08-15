package net.nostalgia.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.entity.decoration.painting.PaintingVariants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class AlphaPaintingItem extends HangingEntityItem {
  private static final Set<ResourceKey<PaintingVariant>> ALPHA_VARIANTS = Set.of(
    PaintingVariants.KEBAB,
    PaintingVariants.AZTEC,
    PaintingVariants.ALBAN,
    PaintingVariants.AZTEC2,
    PaintingVariants.BOMB,
    PaintingVariants.PLANT,
    PaintingVariants.WASTELAND,
    PaintingVariants.POOL,
    PaintingVariants.COURBET,
    PaintingVariants.SEA,
    PaintingVariants.SUNSET,
    PaintingVariants.CREEBET,
    PaintingVariants.WANDERER,
    PaintingVariants.GRAHAM,
    PaintingVariants.MATCH,
    PaintingVariants.BUST,
    PaintingVariants.STAGE,
    PaintingVariants.VOID,
    PaintingVariants.SKULL_AND_ROSES,
    PaintingVariants.FIGHTERS,
    PaintingVariants.POINTER,
    PaintingVariants.PIGSCENE,
    PaintingVariants.SKELETON,
    PaintingVariants.DONKEY_KONG
  );

  public AlphaPaintingItem(Properties properties) {
    super(EntityType.PAINTING, properties);
  }

  public InteractionResult useOn(UseOnContext context) {
    BlockPos pos = context.getClickedPos();
    Direction clickedFace = context.getClickedFace();
    BlockPos blockPos = pos.relative(clickedFace);
    Player player = context.getPlayer();
    ItemStack itemInHand = context.getItemInHand();
    if (player != null && !this.mayPlace(player, clickedFace, itemInHand, blockPos)) {
      return InteractionResult.FAIL;
    } else {
      Level level = context.getLevel();
      Painting candidate = new Painting(level, blockPos);
      List<Holder<PaintingVariant>> potentialVariants = new ArrayList<>();
      level.registryAccess().lookupOrThrow(Registries.PAINTING_VARIANT).getTagOrEmpty(PaintingVariantTags.PLACEABLE).forEach(variant -> {
        if (variant.unwrapKey().map(ALPHA_VARIANTS::contains).orElse(false)) {
          potentialVariants.add((Holder<PaintingVariant>)variant);
        }
      });
      if (potentialVariants.isEmpty()) {
        return InteractionResult.CONSUME;
      } else {
        candidate.setDirection(clickedFace);
        potentialVariants.removeIf(variant -> {
          candidate.setVariant(variant);
          return !candidate.survives();
        });
        if (potentialVariants.isEmpty()) {
          return InteractionResult.CONSUME;
        } else {
          int largestArea = potentialVariants.stream().mapToInt(v -> ((PaintingVariant)v.value()).area()).max().orElse(0);
          potentialVariants.removeIf(v -> ((PaintingVariant)v.value()).area() < largestArea);
          Optional<Holder<PaintingVariant>> selected = Util.getRandomSafe(potentialVariants, level.getRandom());
          if (selected.isEmpty()) {
            return InteractionResult.CONSUME;
          } else {
            candidate.setVariant(selected.get());
            candidate.setDirection(clickedFace);
            EntityType.createDefaultStackConfig(level, itemInHand, player).accept(candidate);
            if (candidate.survives()) {
              if (!level.isClientSide()) {
                candidate.playPlacementSound();
                level.gameEvent(player, GameEvent.ENTITY_PLACE, candidate.position());
                level.addFreshEntity(candidate);
              }

              itemInHand.shrink(1);
              return InteractionResult.SUCCESS;
            } else {
              return InteractionResult.CONSUME;
            }
          }
        }
      }
    }
  }
}
