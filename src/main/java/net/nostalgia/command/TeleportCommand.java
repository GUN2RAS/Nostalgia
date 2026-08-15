package net.nostalgia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Collections;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.block.AlphaBlocks;
import net.nostalgia.world.dimension.ModDimensions;

public class TeleportCommand {
  public TeleportCommand() {
  }

  public static void register() {
    CommandRegistrationCallback.EVENT.register((CommandRegistrationCallback)(dispatcher, registryAccess, environment) -> registerCommand(dispatcher));
  }

  private static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
    LiteralArgumentBuilder<CommandSourceStack> builder = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("nostalgia")
      .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));
    builder.then(
      Commands.literal("rd")
        .executes(
          context -> {
            ServerPlayer player = ((CommandSourceStack)context.getSource()).getPlayerOrException();
            ServerLevel targetWorld = ((CommandSourceStack)context.getSource()).getServer().getLevel(ModDimensions.RD_132211_LEVEL_KEY);
            if (targetWorld != null) {
              BlockPos spawnPos = findSafeSpawn(targetWorld, player.getBlockX(), player.getBlockZ());
              player.teleportTo(
                targetWorld, spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, Collections.emptySet(), player.getYRot(), player.getXRot(), true
              );
              ((CommandSourceStack)context.getSource())
                .sendSuccess(() -> Component.literal("\u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0430\u0446\u0438\u044f \u0432 rd-132211!"), false);
            } else {
              ((CommandSourceStack)context.getSource())
                .sendFailure(
                  Component.literal("\u0418\u0437\u043c\u0435\u0440\u0435\u043d\u0438\u0435 RD \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e!")
                );
            }

            return 1;
          }
        )
    );
    builder.then(
      Commands.literal("alpha")
        .executes(
          context -> {
            ServerPlayer player = ((CommandSourceStack)context.getSource()).getPlayerOrException();
            ServerLevel targetWorld = ((CommandSourceStack)context.getSource()).getServer().getLevel(ModDimensions.ALPHA_112_01_LEVEL_KEY);
            if (targetWorld != null) {
              teleportToAlpha(player, targetWorld);
              ((CommandSourceStack)context.getSource())
                .sendSuccess(() -> Component.literal("\u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0430\u0446\u0438\u044f \u0432 Alpha 1.1.2_01!"), false);
            } else {
              ((CommandSourceStack)context.getSource())
                .sendFailure(
                  Component.literal("\u0418\u0437\u043c\u0435\u0440\u0435\u043d\u0438\u0435 Alpha \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e!")
                );
            }

            return 1;
          }
        )
    );
    dispatcher.register(builder);
  }

  public static void teleportToAlpha(ServerPlayer player, ServerLevel targetWorld) {
    performRelativeTeleport(player, targetWorld);
  }

  public static void teleportToRD(ServerPlayer player, ServerLevel targetWorld) {
    performRelativeTeleport(player, targetWorld);
  }

  private static void performRelativeTeleport(ServerPlayer player, ServerLevel targetWorld) {
    Level currentLevel = player.level();
    MutableBlockPos pos = new MutableBlockPos(player.getBlockX(), (int)player.getY(), player.getBlockZ());

    while (pos.getY() > currentLevel.getMinY() && currentLevel.getBlockState(pos).isAir()) {
      pos.move(Direction.DOWN);
    }

    Vec3 motion = player.getDeltaMovement();
    player.teleportTo(targetWorld, player.getX(), player.getY(), player.getZ(), Collections.emptySet(), player.getYRot(), player.getXRot(), true);
    player.setDeltaMovement(motion);
    player.hurtMarked = true;
  }

  public static boolean isHologramBlock(ServerLevel level, BlockPos pos) {
    String currentDim = level.dimension().identifier().toString();
    for (net.nostalgia.alphalogic.ritual.SkyPortalEventInstance portal : net.nostalgia.alphalogic.ritual.SkyPortalManager.allPortals()) {
      if (portal != null && portal.isActive() && portal.containsOverworldPos(pos, currentDim)) {
        return true;
      }
    }
    if (net.nostalgia.alphalogic.ritual.HologramWorldData.get(level).getDelta(pos) != null) {
      return true;
    }
    return false;
  }

  public static int getSurfaceY(ServerLevel level, int x, int z) {
    return getSurfaceY(level, x, z, false);
  }

  public static int getSurfaceY(ServerLevel level, int x, int z, boolean includeFluids) {
    for (int y = level.getMaxY(); y > level.getMinY(); y--) {
      BlockPos pos = new BlockPos(x, y, z);
      if (isHologramBlock(level, pos)) {
        continue;
      }
      BlockState state = level.getBlockState(pos);
      if (!state.isAir()
        && (!state.getCollisionShape(level, pos).isEmpty() || includeFluids && !state.getFluidState().isEmpty())
        && !state.is(BlockTags.LEAVES)
        && !state.is(BlockTags.LOGS)
        && !state.is(AlphaBlocks.ALPHA_LEAVES)
        && !state.is(AlphaBlocks.ALPHA_OAK_LOG)) {
        return y + 1;
      }
    }

    return level.getMinY() + 100;
  }

  public static BlockPos findSafeSpawn(ServerLevel level, int startX, int startZ) {
    if (level.dimension() == ModDimensions.RD_132211_LEVEL_KEY) {
      return new BlockPos(128, 43, 128);
    } else {
      int radius = 0;

      while (radius <= 1000) {
        for (int x = startX - radius; x <= startX + radius; x += 32) {
          for (int z = startZ - radius; z <= startZ + radius; z += 32) {
            level.getChunk(x >> 4, z >> 4);
            int y = -1;

            for (int checkY = level.getMaxY(); checkY > level.getMinY(); checkY--) {
              BlockPos checkPos = new BlockPos(x, checkY, z);
              BlockState state = level.getBlockState(checkPos);
              if (!state.getCollisionShape(level, checkPos).isEmpty()
                && !state.is(BlockTags.LEAVES)
                && !state.is(BlockTags.LOGS)
                && !state.is(AlphaBlocks.ALPHA_LEAVES)
                && !state.is(AlphaBlocks.ALPHA_OAK_LOG)) {
                y = checkY + 1;
                break;
              }
            }

            if (y > level.getMinY()) {
              BlockPos pos = new BlockPos(x, y, z);
              BlockState floor = level.getBlockState(pos.below());
              BlockState body = level.getBlockState(pos);
              BlockState head = level.getBlockState(pos.above());
              boolean isSafeFloor = !floor.isAir()
                && floor.getFluidState().isEmpty()
                && !floor.is(Blocks.WATER)
                && !floor.is(Blocks.LAVA)
                && !floor.is(BlockTags.LEAVES)
                && !floor.is(BlockTags.LOGS)
                && !floor.is(AlphaBlocks.ALPHA_LEAVES)
                && !floor.is(AlphaBlocks.ALPHA_OAK_LOG);
              boolean isSafeSpace = body.getCollisionShape(level, pos).isEmpty()
                && head.getCollisionShape(level, pos.above()).isEmpty()
                && !body.is(Blocks.WATER)
                && !body.is(Blocks.LAVA);
              if (isSafeFloor && isSafeSpace) {
                return pos;
              }
            }
          }
        }

        if (radius == 0) {
          radius = 32;
        } else {
          radius += 32;
        }
      }

      return new BlockPos(startX, level.getMinY() + 100, startZ);
    }
  }
}
