package net.nostalgia.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("glassbreak")
                .requires(source -> Commands.hasPermission(Commands.LEVEL_GAMEMASTERS).test(source) && source.getEntity() != null)
                .executes(ModCommands::toggleGlassBreak)
                .then(Commands.literal("on").executes(ctx -> fireGlassBreak(ctx, true)))
                .then(Commands.literal("off").executes(ctx -> fireGlassBreak(ctx, false)))
            );
            dispatcher.register(Commands.literal("ower")
                .requires(source -> Commands.hasPermission(Commands.LEVEL_GAMEMASTERS).test(source) && source.getEntity() != null)
                .executes(ModCommands::toggleDebugOwer)
            );
        });
    }

    public static boolean glassBreakState = false;

    private static int toggleGlassBreak(CommandContext<CommandSourceStack> context) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        return fireGlassBreak(context, !glassBreakState);
    }

    private static int fireGlassBreak(CommandContext<CommandSourceStack> context, boolean active) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        glassBreakState = active;
        ServerPlayer player = context.getSource().getPlayerOrException();
        net.minecraft.core.BlockPos anchor = active ? player.blockPosition() : net.minecraft.core.BlockPos.ZERO;
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, new net.nostalgia.network.S2CGlassBreakPayload(active, anchor));
        return 1;
    }

    public static boolean debugOwerState = false;

    private static int toggleDebugOwer(CommandContext<CommandSourceStack> context) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        debugOwerState = !debugOwerState;
        ServerPlayer player = context.getSource().getPlayerOrException();
        net.minecraft.core.BlockPos center = player.blockPosition();
        
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, new net.nostalgia.network.S2CDebugOwerPayload(debugOwerState, center));
        
        if (debugOwerState) {
            net.minecraft.server.level.ServerLevel overworldLevel = context.getSource().getServer().getLevel(net.minecraft.world.level.Level.OVERWORLD);
            if (overworldLevel != null) {
                int radius = 300;
                net.nostalgia.alphalogic.ritual.HologramChunkLoader.startLoading(java.util.Collections.singletonList(player), overworldLevel, center, radius, net.nostalgia.alphalogic.ritual.HologramChunkLoader.getAllChunksInRadius(center, radius));
            }
        }
        
        context.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal("Ower toggled: " + debugOwerState), false);
        return 1;
    }
}
