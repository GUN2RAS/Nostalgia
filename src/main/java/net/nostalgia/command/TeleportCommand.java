package net.nostalgia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.nostalgia.world.dimension.ModDimensions;

public class TeleportCommand {

        public static void register() {
                CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                        registerCommand(dispatcher);
                });
        }

        private static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
                
                LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("nostalgia")
                                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));

                builder.then(Commands.literal("rd")
                                .executes(context -> {
                                        ServerPlayer player = context.getSource().getPlayerOrException();
                                        ServerLevel targetWorld = context.getSource().getServer()
                                                        .getLevel(ModDimensions.RD_132211_LEVEL_KEY);

                                        if (targetWorld != null) {
                                                BlockPos spawnPos = findSafeSpawn(targetWorld, player.getBlockX(), player.getBlockZ());
                                                player.teleportTo(targetWorld, spawnPos.getX() + 0.5, spawnPos.getY(),
                                                                spawnPos.getZ() + 0.5, java.util.Collections.emptySet(),
                                                                player.getYRot(),
                                                                player.getXRot(), true);
                                                context.getSource().sendSuccess(
                                                                () -> Component.literal("Телепортация в rd-132211!"),
                                                                false);
                                        } else {
                                                context.getSource()
                                                                .sendFailure(Component
                                                                                .literal("Измерение RD не найдено!"));
                                        }

                                        return 1;
                                }));

                builder.then(Commands.literal("alpha")
                                .executes(context -> {
                                        ServerPlayer player = context.getSource().getPlayerOrException();
                                        ServerLevel targetWorld = context.getSource().getServer()
                                                        .getLevel(ModDimensions.ALPHA_112_01_LEVEL_KEY);

                                        if (targetWorld != null) {
                                                teleportToAlpha(player, targetWorld);
                                                context.getSource().sendSuccess(
                                                                () -> Component.literal(
                                                                                "Телепортация в Alpha 1.1.2_01!"),
                                                                false);
                                        } else {
                                                context.getSource()
                                                                .sendFailure(Component.literal(
                                                                                "Измерение Alpha не найдено!"));
                                        }

                                        return 1;
                                }));

                dispatcher.register(builder);
        }

        public static void teleportToAlpha(ServerPlayer player, ServerLevel targetWorld) {
            performRelativeTeleport(player, targetWorld);
        }

        public static void teleportToRD(ServerPlayer player, ServerLevel targetWorld) {
            performRelativeTeleport(player, targetWorld);
        }

        private static void performRelativeTeleport(ServerPlayer player, ServerLevel targetWorld) {
            net.minecraft.world.level.Level currentLevel = player.level();
            net.minecraft.core.BlockPos.MutableBlockPos pos = new net.minecraft.core.BlockPos.MutableBlockPos(
                    player.getBlockX(), (int)player.getY(), player.getBlockZ());
            
            while (pos.getY() > currentLevel.getMinY() && currentLevel.getBlockState(pos).isAir()) {
                pos.move(net.minecraft.core.Direction.DOWN);
            }

            net.minecraft.world.phys.Vec3 motion = player.getDeltaMovement();
            player.teleportTo(targetWorld, player.getX(), player.getY(), player.getZ(), 
                    java.util.Collections.emptySet(),
                    player.getYRot(),
                    player.getXRot(), true);
            player.setDeltaMovement(motion);
            player.hurtMarked = true; 
        }

        public static net.minecraft.core.BlockPos findSafeSpawn(ServerLevel level, int startX, int startZ) {
            if (level.dimension() == net.nostalgia.world.dimension.ModDimensions.RD_132211_LEVEL_KEY) {
                int rdX = Math.floorMod(startX, 256);
                int rdZ = Math.floorMod(startZ, 256);
                return new net.minecraft.core.BlockPos(rdX, 43, rdZ);
            }
            int radius = 0;
            while (radius <= 1000) {
                for (int x = startX - radius; x <= startX + radius; x += 32) {
                    for (int z = startZ - radius; z <= startZ + radius; z += 32) {
                        level.getChunk(x >> 4, z >> 4); 
                        int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, x, z);
                        if (y > 0) {
                            net.minecraft.core.BlockPos pos = new net.minecraft.core.BlockPos(x, y, z);
                            net.minecraft.world.level.block.state.BlockState floor = level.getBlockState(pos.below());
                            if (!floor.isAir() && floor.getFluidState().isEmpty() && !floor.is(net.minecraft.world.level.block.Blocks.WATER) && !floor.is(net.nostalgia.block.AlphaBlocks.ALPHA_LEAVES) && !floor.is(net.nostalgia.block.AlphaBlocks.ALPHA_OAK_LOG)) {
                                return pos;
                            }
                        }
                    }
                }
                if (radius == 0) radius = 32;
                else radius += 32;
            }
            return new net.minecraft.core.BlockPos(startX, 100, startZ); 
        }
}
