package net.nostalgia;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.nostalgia.block.ModBlocks;
import net.nostalgia.block.entity.ModBlockEntities;
import net.nostalgia.command.TeleportCommand;
import net.nostalgia.item.ModItems;
import net.nostalgia.world.gen.RD132211ChunkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NostalgiaMod implements ModInitializer {
    public static final String MOD_ID = "nostalgia";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        net.nostalgia.sound.AlphaSounds.initialize();
        net.nostalgia.block.AlphaBlocks.register();
        net.nostalgia.item.AlphaItems.register();
        net.nostalgia.entity.AlphaEntities.register();
        net.nostalgia.inventory.ModScreenHandlers.register();
        net.nostalgia.alphalogic.ritual.RitualManager.init();
        ModBlockEntities.registerBlockEntities();
        TeleportCommand.register();
        net.nostalgia.command.TimeStopCommand.register();
        net.nostalgia.command.ModCommands.register();
        net.nostalgia.world.gen.AlphaSounds.registerSounds();
        net.nostalgia.network.NostalgiaNetworking.register();

        net.fabricmc.fabric.api.registry.FlammableBlockRegistry.getDefaultInstance().add(net.nostalgia.block.AlphaBlocks.ALPHA_OAK_LOG, 5, 5);
        net.fabricmc.fabric.api.registry.FlammableBlockRegistry.getDefaultInstance().add(net.nostalgia.block.AlphaBlocks.ALPHA_OAK_PLANKS, 5, 20);
        net.fabricmc.fabric.api.registry.FlammableBlockRegistry.getDefaultInstance().add(net.nostalgia.block.AlphaBlocks.ALPHA_LEAVES, 30, 60);

        Registry.register(
                BuiltInRegistries.CHUNK_GENERATOR,
                Identifier.fromNamespaceAndPath(MOD_ID, "rd132211"),
                RD132211ChunkGenerator.CODEC);

        Registry.register(
                BuiltInRegistries.CHUNK_GENERATOR,
                Identifier.fromNamespaceAndPath(MOD_ID, "alpha_112_01"),
                net.nostalgia.world.gen.AlphaChunkGenerator.CODEC);

        net.fabricmc.fabric.api.event.player.UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            return handleHologramClick(player, world, entity);
        });

        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            net.nostalgia.alphalogic.ritual.RitualManager.loadZones(server);
        });

        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            net.nostalgia.alphalogic.ritual.RitualManager.clearStateOnServerStop();
        });

        net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (net.nostalgia.command.ModCommands.portalDebugState && net.nostalgia.command.ModCommands.portalTimerTicks > 0) {
                net.nostalgia.command.ModCommands.portalTimerTicks--;
                if (net.nostalgia.command.ModCommands.portalTimerTicks == 0) {
                    if (net.nostalgia.command.ModCommands.activePortalCenter != null) {
                        net.nostalgia.command.ModCommands.toggleGlobalPortal(server, net.nostalgia.command.ModCommands.activePortalCenter, false, 0L);
                    }
                }
            }
        });

        net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClientSide() || !(world instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;
            net.minecraft.core.BlockPos beaconCandidate = null;
            if (state.is(net.minecraft.world.level.block.Blocks.BEACON)) {
                beaconCandidate = pos;
            } else if (state.is(net.minecraft.world.level.block.Blocks.RESPAWN_ANCHOR)) {
                beaconCandidate = pos.above();
            }
            if (beaconCandidate == null) return;
            net.nostalgia.alphalogic.ritual.event.TimestopZoneEvent zone =
                    net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.findZoneByBeacon(beaconCandidate);
            if (zone == null) return;
            net.nostalgia.alphalogic.ritual.event.TransitionEvent activeRitual = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.activeRitual();
            net.minecraft.core.BlockPos activeTarget = activeRitual != null ? activeRitual.beaconPos() : null;
            if (activeRitual != null
                    && activeTarget != null && activeTarget.equals(zone.beaconPos())) {
                net.nostalgia.alphalogic.ritual.RitualManager.handleInterrupt();
            } else {
                net.nostalgia.alphalogic.ritual.RitualManager.removeZone(serverLevel, zone.beaconPos());
                net.minecraft.world.entity.item.ItemEntity crystal = new net.minecraft.world.entity.item.ItemEntity(
                        serverLevel,
                        zone.beaconPos().getX() + 0.5,
                        zone.beaconPos().getY() + 1.5,
                        zone.beaconPos().getZ() + 0.5,
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.ECHO_SHARD)
                );
                crystal.setDefaultPickUpDelay();
                serverLevel.addFreshEntity(crystal);
            }
        });

        net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            net.nostalgia.alphalogic.ritual.RitualManager.handlePlayerDisconnect(handler.getPlayer());
        });

        net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            net.minecraft.server.level.ServerPlayer player = handler.getPlayer();
            if (net.nostalgia.alphalogic.ritual.RitualManager.hasActiveZone()) {
                net.nostalgia.alphalogic.ritual.RitualManager.sendZoneToPlayer(player);
            }
            net.nostalgia.alphalogic.ritual.event.TransitionEvent joinRitual = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.activeRitual();
            if (joinRitual != null
                    && net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.participants().contains(player.getUUID())) {
                long seed = net.nostalgia.alphalogic.bridge.AlphaEngineManager.getWorldSeed();
                net.nostalgia.network.S2CStartTransitionVisualsPayload startPayload =
                        new net.nostalgia.network.S2CStartTransitionVisualsPayload(
                                net.nostalgia.alphalogic.ritual.RitualManager.getTransitionDimensionId(),
                                net.nostalgia.alphalogic.ritual.RitualManager.getTransitionBeaconPos(),
                                net.nostalgia.alphalogic.ritual.RitualManager.getTransitionTargetPos(),
                                seed);
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, startPayload);

                net.nostalgia.network.S2CSyncParticipantsPayload syncPayload =
                        new net.nostalgia.network.S2CSyncParticipantsPayload(
                                new java.util.ArrayList<>(net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.participants()));
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, syncPayload);

                net.nostalgia.network.S2CRitualPhasePayload phasePayload =
                        new net.nostalgia.network.S2CRitualPhasePayload(
                                net.nostalgia.alphalogic.ritual.RitualManager.getCurrentSyncPhase());
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, phasePayload);
            }
        });

        net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
            if (entity instanceof net.minecraft.server.level.ServerPlayer player) {
                if (net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.participants().contains(player.getUUID())) {
                    net.minecraft.server.MinecraftServer server = ((net.minecraft.server.level.ServerLevel) player.level()).getServer();
                    if (server != null) {
                        net.nostalgia.alphalogic.ritual.RitualManager.removeParticipant(player.getUUID(), server);
                    }
                }
            }
            return true;
        });

        ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register((player, origin, destination) -> {
            if (net.nostalgia.alphalogic.ritual.RitualManager.hasActiveZone()) {
                net.nostalgia.alphalogic.ritual.RitualManager.sendZoneToPlayer(player);
            }
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            if (net.nostalgia.alphalogic.ritual.RitualManager.hasActiveZone()) {
                net.nostalgia.alphalogic.ritual.RitualManager.sendZoneToPlayer(newPlayer);
            }
        });

        net.fabricmc.fabric.api.event.player.UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClientSide() && player != null) {
                net.minecraft.world.item.ItemStack stack = player.getItemInHand(hand);
                if (stack.getItem() instanceof net.minecraft.world.item.HoeItem) {
                    net.minecraft.core.BlockPos pos = hitResult.getBlockPos();
                    net.minecraft.world.level.block.state.BlockState state = world.getBlockState(pos);
                    if (hitResult.getDirection() != net.minecraft.core.Direction.DOWN && world.getBlockState(pos.above()).isAir()) {
                        if (state.is(net.nostalgia.block.AlphaBlocks.ALPHA_DIRT) || state.is(net.nostalgia.block.AlphaBlocks.ALPHA_GRASS_BLOCK)) {
                            world.setBlock(pos, net.nostalgia.block.AlphaBlocks.ALPHA_FARMLAND.defaultBlockState(), 11);
                            world.playSound(null, pos, net.minecraft.sounds.SoundEvents.HOE_TILL, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);

                            net.minecraft.world.entity.EquipmentSlot slot = hand == net.minecraft.world.InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND;
                            stack.hurtAndBreak(1, player, slot);

                            return net.minecraft.world.InteractionResult.SUCCESS;
                        }
                    }
                }
            }
            return net.minecraft.world.InteractionResult.PASS;
        });
    }

    private static net.minecraft.world.InteractionResult handleHologramClick(net.minecraft.world.entity.player.Player player, net.minecraft.world.level.Level world, net.minecraft.world.entity.Entity entity) {
        if (!world.isClientSide() && entity instanceof net.minecraft.world.entity.Interaction interaction && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            if (interaction.entityTags().contains("nostalgia_matrix_alpha")) {
                net.minecraft.server.level.ServerLevel alphaLevel = world.getServer().getLevel(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, Identifier.fromNamespaceAndPath("nostalgia", "alpha_112_01")));
                if (alphaLevel != null) {
                    net.nostalgia.alphalogic.ritual.RitualManager.endRitual();
                    TeleportCommand.teleportToAlpha(serverPlayer, alphaLevel);
                }
                return net.minecraft.world.InteractionResult.SUCCESS;
            } else if (interaction.entityTags().contains("nostalgia_matrix_rd")) {
                net.minecraft.server.level.ServerLevel rdLevel = world.getServer().getLevel(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, Identifier.fromNamespaceAndPath("nostalgia", "rd_132211")));
                if (rdLevel != null) {
                    net.nostalgia.alphalogic.ritual.RitualManager.endRitual();
                    TeleportCommand.teleportToRD(serverPlayer, rdLevel);
                }
                return net.minecraft.world.InteractionResult.SUCCESS;
            }
        }
        return net.minecraft.world.InteractionResult.PASS;
    }
}
