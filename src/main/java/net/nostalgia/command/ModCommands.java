package net.nostalgia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("test_transition")
                .requires(source -> Commands.hasPermission(Commands.LEVEL_GAMEMASTERS).test(source) && source.getEntity() != null)
                .executes(ModCommands::testTransition)
            );
            dispatcher.register(Commands.literal("testportal")
                .requires(source -> Commands.hasPermission(Commands.LEVEL_GAMEMASTERS).test(source) && source.getEntity() != null)
                .executes(ModCommands::testPortal)
            );
            dispatcher.register(Commands.literal("testportal_invert")
                .requires(source -> Commands.hasPermission(Commands.LEVEL_GAMEMASTERS).test(source) && source.getEntity() != null)
                .executes(ModCommands::testPortalInvert)
            );
            dispatcher.register(Commands.literal("glassbreak")
                .requires(source -> Commands.hasPermission(Commands.LEVEL_GAMEMASTERS).test(source) && source.getEntity() != null)
                .executes(ModCommands::toggleGlassBreak)
                .then(Commands.literal("on").executes(ctx -> fireGlassBreak(ctx, true)))
                .then(Commands.literal("off").executes(ctx -> fireGlassBreak(ctx, false)))
            );
        });
    }

    public static boolean portalDebugState = false;
    public static boolean glassBreakState = false;
    public static int portalTimerTicks = 0;
    public static net.minecraft.core.BlockPos activePortalCenter = null;

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

    public static void toggleGlobalPortal(net.minecraft.server.MinecraftServer server, net.minecraft.core.BlockPos center, boolean inverted, long seed) {
        portalDebugState = !portalDebugState;
        if (portalDebugState) {
            activePortalCenter = center;
            portalTimerTicks = 6000; 

            net.minecraft.server.level.ServerLevel alphaLevel = server.getLevel(net.minecraft.resources.ResourceKey.create(
                net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse("nostalgia:alpha_112_01")));
            if (alphaLevel != null) {
                java.util.Map<net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState> deltas = net.nostalgia.alphalogic.ritual.AlphaWorldData.get(alphaLevel).getDeltasInRadius(center, 300.0);
                long[] positions = new long[deltas.size()];
                int[] states = new int[deltas.size()];
                int idx = 0;
                for (java.util.Map.Entry<net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState> entry : deltas.entrySet()) {
                    net.minecraft.core.BlockPos aPos = entry.getKey();
                    if (inverted) {
                        positions[idx] = new net.minecraft.core.BlockPos(aPos.getX(), 320 - aPos.getY(), aPos.getZ()).asLong();
                    } else {
                        positions[idx] = aPos.asLong();
                    }
                    states[idx] = net.minecraft.world.level.block.Block.getId(entry.getValue());
                    idx++;
                }
                net.nostalgia.network.S2CSyncAlphaDeltasPayload deltaPayload = new net.nostalgia.network.S2CSyncAlphaDeltasPayload(positions, states);
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(p, deltaPayload);
                }
            }

        } else {
            activePortalCenter = null;
            portalTimerTicks = 0;
        }

        net.nostalgia.network.S2CPortalDebugPayload payload = new net.nostalgia.network.S2CPortalDebugPayload(portalDebugState, inverted, seed, center);
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(p, payload);
        }
    }

    private static int testPortal(CommandContext<CommandSourceStack> context) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        toggleGlobalPortal(context.getSource().getServer(), player.blockPosition(), false, player.level().getSeed());
        return 1;
    }

    private static int testPortalInvert(CommandContext<CommandSourceStack> context) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        toggleGlobalPortal(context.getSource().getServer(), player.blockPosition(), true, player.level().getSeed());
        return 1;
    }

    private static int testTransition(CommandContext<CommandSourceStack> context) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        
        net.minecraft.server.level.ServerLevel sLevel = (net.minecraft.server.level.ServerLevel) player.level();
        net.minecraft.server.MinecraftServer server = sLevel.getServer();
        net.minecraft.server.level.ServerLevel targetLevel = server.getLevel(net.minecraft.resources.ResourceKey.create(
            net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse("nostalgia:alpha_112_01")));
            
        long currentSeed = targetLevel != null ? targetLevel.getSeed() : sLevel.getSeed();
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, 
            new net.nostalgia.network.S2CStartTransitionVisualsPayload("nostalgia:alpha_112_01", player.blockPosition(), player.blockPosition(), currentSeed)
        );
        
        context.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal("Started visual transition sequence!"), false);
        return 1;
    }
}
