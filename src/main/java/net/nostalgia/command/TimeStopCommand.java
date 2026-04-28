package net.nostalgia.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.nostalgia.alphalogic.ritual.RitualManager;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import net.nostalgia.alphalogic.ritual.event.TransitionEvent;

public class TimeStopCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(TimeStopCommand::registerCommand);
    }

    private static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("timestop")
            .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
            .executes(context -> {
                CommandSourceStack source = context.getSource();
                ServerLevel level = source.getLevel();

                TransitionEvent event = RitualEventRegistry.activeRitual();
                RitualManager.State state = event != null ? event.state() : RitualManager.State.INACTIVE;
                if (state == RitualManager.State.FROZEN || state == RitualManager.State.TIME_STOPPING) {
                    RitualManager.triggerTimeResume();
                    source.sendSuccess(() -> Component.literal("Time Stop: RESTORING. Accelerating local timeframe over 2000ms..."), true);
                } else if (state == RitualManager.State.INACTIVE || state == RitualManager.State.TIME_RESUMING) {
                    RitualManager.triggerTimeStop(level);
                    source.sendSuccess(() -> Component.literal("Time Stop: INITIATING. Decelerating local timeframe over 2000ms..."), true);
                }
                return 1;
            })
        );
    }
}
