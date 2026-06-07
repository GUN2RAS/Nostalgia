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
        net.nostalgia.alphalogic.ritual.EchoRitualManager.init();
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
            net.minecraft.server.level.ServerLevel overworld = server.overworld();
            if (overworld != null) {
                net.nostalgia.alphalogic.bridge.AlphaEngineManager.setWorldSeed(overworld.getSeed());
            }
            net.nostalgia.alphalogic.ritual.TimestopZoneManager.loadZones(server);
            net.nostalgia.alphalogic.ritual.SkyPortalManager.loadFromDisk(server);
        });

        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            net.nostalgia.alphalogic.ritual.EchoRitualManager.clearStateOnServerStop();
        });

        net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_SERVER_TICK.register(server -> {
            net.nostalgia.alphalogic.ritual.SkyPortalEventInstance portalInst = net.nostalgia.alphalogic.ritual.SkyPortalManager.getActive();
            if (portalInst != null) {
                portalInst.tick();
                if (!portalInst.isActive()) {
                    net.nostalgia.alphalogic.ritual.SkyPortalManager.toggleGlobal(server, portalInst.center(), false, 0L, portalInst.sourceDimension(), portalInst.targetDimension());
                } else if (net.nostalgia.alphalogic.ritual.DimensionUtil.isRD(portalInst.targetDimension()) && (6000 - portalInst.timerTicks()) == 25) {
                    net.nostalgia.network.S2CSkyPortalCancelPayload cancelPayload = new net.nostalgia.network.S2CSkyPortalCancelPayload(true);
                    for (net.minecraft.server.level.ServerPlayer p : server.getPlayerList().getPlayers()) {
                        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(p, cancelPayload);
                    }
                    net.nostalgia.alphalogic.ritual.SkyPortalManager.toggleGlobal(server, portalInst.center(), false, 0L, portalInst.sourceDimension(), portalInst.targetDimension());
                } else if (server.getTickCount() % 20 == 0) {
                    net.minecraft.server.level.ServerLevel sourceLevel = server.getLevel(net.minecraft.resources.ResourceKey.create(
                        net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse(portalInst.sourceDimension())));
                    if (sourceLevel != null && sourceLevel.isLoaded(portalInst.center())) {
                        net.minecraft.world.level.block.state.BlockState bState = sourceLevel.getBlockState(portalInst.center());
                        net.minecraft.world.level.block.state.BlockState aState = sourceLevel.getBlockState(portalInst.center().below());
                        boolean valid = bState.is(net.minecraft.world.level.block.Blocks.BEACON) &&
                                        aState.is(net.minecraft.world.level.block.Blocks.RESPAWN_ANCHOR);
                        if (!valid) {
                            net.nostalgia.alphalogic.ritual.SkyPortalManager.toggleGlobal(server, portalInst.center(), false, 0L, portalInst.sourceDimension(), portalInst.targetDimension());
                        }
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

            net.nostalgia.alphalogic.ritual.SkyPortalEventInstance portalInst = net.nostalgia.alphalogic.ritual.SkyPortalManager.getActive();
            if (portalInst != null && portalInst.sourceDimension().equals(serverLevel.dimension().identifier().toString())) {
                double dist = portalInst.center().distSqr(beaconCandidate);
                if (dist <= 9.0) {
                    net.nostalgia.alphalogic.ritual.SkyPortalManager.toggleGlobal(
                            serverLevel.getServer(), portalInst.center(), portalInst.inverted(),
                            portalInst.seed(), portalInst.sourceDimension(), portalInst.targetDimension());
                }
            }

            net.nostalgia.alphalogic.ritual.event.TimestopZoneEvent zone =
                    net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.findZoneByBeacon(beaconCandidate);
            if (zone == null) return;
            net.nostalgia.alphalogic.ritual.EchoRitualEventInstance activeInst = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.findInstanceByBeacon(zone.beaconPos());
            if (activeInst != null && activeInst.state() != net.nostalgia.alphalogic.ritual.EchoRitualManager.State.INACTIVE) {
                net.nostalgia.alphalogic.ritual.EchoRitualManager.handleInterrupt(zone.beaconPos());
            } else {
                net.nostalgia.alphalogic.ritual.TimestopZoneManager.removeZone(serverLevel, zone.beaconPos());
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
            net.nostalgia.alphalogic.ritual.EchoRitualManager.handlePlayerDisconnect(handler.getPlayer());
        });

        net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            net.minecraft.server.level.ServerPlayer player = handler.getPlayer();
            if (net.nostalgia.alphalogic.ritual.TimestopZoneManager.hasActiveZone()) {
                net.nostalgia.alphalogic.ritual.TimestopZoneManager.sendZoneToPlayer(player);
            }
            net.nostalgia.alphalogic.ritual.SkyPortalManager.sendPortalToPlayer(player, server);
            net.nostalgia.alphalogic.ritual.EchoRitualEventInstance joinInst = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.findInstanceForParticipant(player.getUUID());
            if (joinInst != null) {
                long seed = net.nostalgia.alphalogic.bridge.AlphaEngineManager.getWorldSeed();
                net.minecraft.server.level.ServerLevel tlEarly = server.getLevel(net.minecraft.resources.ResourceKey.create(
                    net.minecraft.core.registries.Registries.DIMENSION, net.minecraft.resources.Identifier.tryParse(joinInst.targetDimensionId())));
                
                int targetSkyColor = -1;
                int targetFogColor = -1;
                if (tlEarly != null && joinInst.targetPos() != null) {
                    targetSkyColor = tlEarly.environmentAttributes().getValue(net.minecraft.world.attribute.EnvironmentAttributes.SKY_COLOR, joinInst.targetPos().getCenter(), null);
                    targetFogColor = tlEarly.environmentAttributes().getValue(net.minecraft.world.attribute.EnvironmentAttributes.FOG_COLOR, joinInst.targetPos().getCenter(), null);
                }

                net.minecraft.world.level.block.state.BlockState joinBeaconState = joinInst.deltaCache.get(joinInst.beaconPos());
                if (joinBeaconState == null && tlEarly != null) joinBeaconState = tlEarly.getBlockState(joinInst.beaconPos());
                net.minecraft.world.level.block.state.BlockState joinAnchorState = joinInst.deltaCache.get(joinInst.beaconPos().below());
                if (joinAnchorState == null && tlEarly != null) joinAnchorState = tlEarly.getBlockState(joinInst.beaconPos().below());

                net.nostalgia.network.S2CStartTransitionVisualsPayload startPayload =
                        new net.nostalgia.network.S2CStartTransitionVisualsPayload(
                                joinInst.id(),
                                joinInst.targetDimensionId(),
                                joinInst.beaconPos(),
                                joinInst.targetPos(),
                                joinInst.offsetX(), joinInst.yOffset(), joinInst.offsetZ(),
                                seed, targetSkyColor, targetFogColor,
                                joinBeaconState != null ? net.minecraft.world.level.block.Block.getId(joinBeaconState) : 0,
                                joinAnchorState != null ? net.minecraft.world.level.block.Block.getId(joinAnchorState) : 0);
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, startPayload);

                net.nostalgia.network.S2CSyncParticipantsPayload syncPayload =
                        new net.nostalgia.network.S2CSyncParticipantsPayload(
                                new java.util.ArrayList<>(joinInst.participants()));
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, syncPayload);

                net.nostalgia.network.S2CRitualPhasePayload phasePayload =
                        new net.nostalgia.network.S2CRitualPhasePayload(joinInst.id(), joinInst.phase());
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, phasePayload);
            }
        });

        net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
            if (entity instanceof net.minecraft.server.level.ServerPlayer player) {
                if (net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isParticipantAny(player.getUUID())) {
                    net.minecraft.server.MinecraftServer server = ((net.minecraft.server.level.ServerLevel) player.level()).getServer();
                    if (server != null) {
                        net.nostalgia.alphalogic.ritual.EchoRitualManager.removeParticipant(player.getUUID(), server);
                    }
                }
            }
            return true;
        });

        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            net.nostalgia.alphalogic.ritual.SkyPortalEventInstance inst = net.nostalgia.alphalogic.ritual.SkyPortalManager.getActive();
            if (inst != null) {
                net.nostalgia.alphalogic.ritual.SkyPortalManager.stop(server, inst.sourceDimension());
            }
            net.nostalgia.alphalogic.ritual.EchoRitualManager.clearStateOnServerStop();
        });

        ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register((player, origin, destination) -> {
            if (net.nostalgia.alphalogic.ritual.TimestopZoneManager.hasActiveZone()) {
                net.nostalgia.alphalogic.ritual.TimestopZoneManager.sendZoneToPlayer(player);
            }
            net.nostalgia.alphalogic.ritual.SkyPortalManager.sendPortalToPlayer(player, ((net.minecraft.server.level.ServerLevel) player.level()).getServer());
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            if (net.nostalgia.alphalogic.ritual.TimestopZoneManager.hasActiveZone()) {
                net.nostalgia.alphalogic.ritual.TimestopZoneManager.sendZoneToPlayer(newPlayer);
            }
            net.nostalgia.alphalogic.ritual.SkyPortalManager.sendPortalToPlayer(newPlayer, ((net.minecraft.server.level.ServerLevel) newPlayer.level()).getServer());
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
                    endRitualForPlayer(serverPlayer);
                    TeleportCommand.teleportToAlpha(serverPlayer, alphaLevel);
                }
                return net.minecraft.world.InteractionResult.SUCCESS;
            } else if (interaction.entityTags().contains("nostalgia_matrix_rd")) {
                net.minecraft.server.level.ServerLevel rdLevel = world.getServer().getLevel(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, Identifier.fromNamespaceAndPath("nostalgia", "rd_132211")));
                if (rdLevel != null) {
                    endRitualForPlayer(serverPlayer);
                    TeleportCommand.teleportToRD(serverPlayer, rdLevel);
                }
                return net.minecraft.world.InteractionResult.SUCCESS;
            }
        }
        return net.minecraft.world.InteractionResult.PASS;
    }

    private static void endRitualForPlayer(net.minecraft.server.level.ServerPlayer player) {
        net.nostalgia.alphalogic.ritual.EchoRitualEventInstance inst = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.findInstanceForParticipant(player.getUUID());
        if (inst != null) {
            net.nostalgia.alphalogic.ritual.EchoRitualManager.endRitualForInstance(inst);
        }
    }
}
