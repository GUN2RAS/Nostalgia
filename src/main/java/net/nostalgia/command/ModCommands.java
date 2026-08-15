package net.nostalgia.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collections;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.nostalgia.alphalogic.ritual.HologramChunkLoader;
import net.nostalgia.network.S2CDebugOwerPayload;
import net.nostalgia.network.S2CGlassBreakPayload;

public class ModCommands {
  public static boolean glassBreakState = false;
  public static boolean debugOwerState = false;

  public ModCommands() {
  }

  public static void register() {
    CommandRegistrationCallback.EVENT
      .register(
        (CommandRegistrationCallback)(dispatcher, registryAccess, environment) -> {
          dispatcher.register(
            (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("glassbreak")
                    .requires(source -> Commands.hasPermission(Commands.LEVEL_GAMEMASTERS).test(source) && source.getEntity() != null))
                  .executes(ModCommands::toggleGlassBreak))
                .then(Commands.literal("on").executes(ctx -> fireGlassBreak(ctx, true))))
              .then(Commands.literal("off").executes(ctx -> fireGlassBreak(ctx, false)))
          );
          dispatcher.register(
            (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("ower")
                .requires(source -> Commands.hasPermission(Commands.LEVEL_GAMEMASTERS).test(source) && source.getEntity() != null))
              .executes(ModCommands::toggleDebugOwer)
          );
          StressTestCommand.register(dispatcher);
        }
      );
  }

  private static int toggleGlassBreak(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
    return fireGlassBreak(context, !glassBreakState);
  }

  private static int fireGlassBreak(CommandContext<CommandSourceStack> context, boolean active) throws CommandSyntaxException {
    glassBreakState = active;
    ServerPlayer player = ((CommandSourceStack)context.getSource()).getPlayerOrException();
    BlockPos anchor = active ? player.blockPosition() : BlockPos.ZERO;
    ServerPlayNetworking.send(player, new S2CGlassBreakPayload(active, anchor));
    return 1;
  }

  private static int toggleDebugOwer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
    debugOwerState = !debugOwerState;
    ServerPlayer player = ((CommandSourceStack)context.getSource()).getPlayerOrException();
    BlockPos center = player.blockPosition();
    ServerPlayNetworking.send(player, new S2CDebugOwerPayload(debugOwerState, center));
    if (debugOwerState) {
      ServerLevel overworldLevel = ((CommandSourceStack)context.getSource()).getServer().getLevel(Level.OVERWORLD);
      if (overworldLevel != null) {
        int radius = 300;
        HologramChunkLoader.startLoading(
          Collections.singletonList(player), overworldLevel, center, radius, HologramChunkLoader.getAllChunksInRadius(center, radius)
        );
      }
    }

    ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Ower toggled: " + debugOwerState), false);
    return 1;
  }
}
