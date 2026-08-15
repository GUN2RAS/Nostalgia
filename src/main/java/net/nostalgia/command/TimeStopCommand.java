package net.nostalgia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.nostalgia.alphalogic.ritual.EchoRitualEventInstance;
import net.nostalgia.alphalogic.ritual.EchoRitualManager;
import net.nostalgia.alphalogic.ritual.TimestopZoneManager;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;

public class TimeStopCommand {
  public TimeStopCommand() {
  }

  public static void register() {
    CommandRegistrationCallback.EVENT.register(TimeStopCommand::registerCommand);
  }

  private static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, CommandSelection environment) {
    dispatcher.register(
      (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("timestop").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
        .executes(context -> {
          CommandSourceStack source = (CommandSourceStack)context.getSource();
          ServerLevel level = source.getLevel();
          BlockPos here = BlockPos.containing(source.getPosition());
          TimestopZoneManager.ActiveZone zone = TimestopZoneManager.findZoneContaining(level.dimension(), here);
          BlockPos beacon = zone != null ? zone.beaconPos() : here;
          EchoRitualEventInstance inst = RitualEventRegistry.findInstanceByBeacon(beacon);
          EchoRitualManager.State state = inst != null ? inst.state() : EchoRitualManager.State.INACTIVE;
          if (state == EchoRitualManager.State.FROZEN || state == EchoRitualManager.State.TIME_STOPPING) {
            EchoRitualManager.triggerTimeResume(beacon);
            source.sendSuccess(() -> Component.literal("Time Stop: RESTORING. Accelerating local timeframe over 2000ms..."), true);
          } else if (state == EchoRitualManager.State.INACTIVE || state == EchoRitualManager.State.TIME_RESUMING) {
            EchoRitualManager.triggerTimeStop(level, beacon);
            source.sendSuccess(() -> Component.literal("Time Stop: INITIATING. Decelerating local timeframe over 2000ms..."), true);
          }

          return 1;
        })
    );
  }
}
