package net.nostalgia;

import java.util.ArrayList;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents.AfterPlayerChange;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AllowDeath;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AfterRespawn;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStarted;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStopped;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStopping;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.EndTick;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.After;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.Disconnect;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.Join;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.bridge.AlphaEngineManager;
import net.nostalgia.alphalogic.ritual.DimensionUtil;
import net.nostalgia.alphalogic.ritual.EchoRitualEventInstance;
import net.nostalgia.alphalogic.ritual.EchoRitualManager;
import net.nostalgia.alphalogic.ritual.SkyPortalEventInstance;
import net.nostalgia.alphalogic.ritual.SkyPortalManager;
import net.nostalgia.alphalogic.ritual.TimestopZoneManager;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;
import net.nostalgia.alphalogic.ritual.event.TimestopZoneEvent;
import net.nostalgia.block.AlphaBlocks;
import net.nostalgia.block.ModBlocks;
import net.nostalgia.block.entity.ModBlockEntities;
import net.nostalgia.command.ModCommands;
import net.nostalgia.command.TeleportCommand;
import net.nostalgia.command.TimeStopCommand;
import net.nostalgia.entity.AlphaEntities;
import net.nostalgia.inventory.ModScreenHandlers;
import net.nostalgia.item.AlphaItems;
import net.nostalgia.item.ModItems;
import net.nostalgia.network.NostalgiaNetworking;
import net.nostalgia.network.S2CRitualPhasePayload;
import net.nostalgia.network.S2CSkyPortalCancelPayload;
import net.nostalgia.network.S2CSkyPortalPayload;
import net.nostalgia.network.S2CStartTransitionVisualsPayload;
import net.nostalgia.network.S2CSyncParticipantsPayload;
import net.nostalgia.network.S2CWorldSeedPayload;
import net.nostalgia.sound.AlphaSounds;
import net.nostalgia.world.gen.AlphaChunkGenerator;
import net.nostalgia.world.gen.RD132211ChunkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NostalgiaMod implements ModInitializer {
  public static final String MOD_ID = "nostalgia";
  public static final Logger LOGGER = LoggerFactory.getLogger("nostalgia");

  public NostalgiaMod() {
  }

  public void onInitialize() {
    ModItems.registerModItems();
    ModBlocks.registerModBlocks();
    AlphaSounds.initialize();
    AlphaBlocks.register();
    AlphaItems.register();
    AlphaEntities.register();
    ModScreenHandlers.register();
    EchoRitualManager.init();
    ModBlockEntities.registerBlockEntities();
    TeleportCommand.register();
    TimeStopCommand.register();
    ModCommands.register();
    net.nostalgia.world.gen.AlphaSounds.registerSounds();
    NostalgiaNetworking.register();
    FlammableBlockRegistry.getDefaultInstance().add(AlphaBlocks.ALPHA_OAK_LOG, 5, 5);
    FlammableBlockRegistry.getDefaultInstance().add(AlphaBlocks.ALPHA_OAK_PLANKS, 5, 20);
    FlammableBlockRegistry.getDefaultInstance().add(AlphaBlocks.ALPHA_LEAVES, 30, 60);
    Registry.register(BuiltInRegistries.CHUNK_GENERATOR, Identifier.fromNamespaceAndPath("nostalgia", "rd132211"), RD132211ChunkGenerator.CODEC);
    Registry.register(BuiltInRegistries.CHUNK_GENERATOR, Identifier.fromNamespaceAndPath("nostalgia", "alpha_112_01"), AlphaChunkGenerator.CODEC);
    UseEntityCallback.EVENT.register((UseEntityCallback)(player, world, hand, entity, hitResult) -> handleHologramClick(player, world, entity));
    ServerLifecycleEvents.SERVER_STARTED.register((ServerStarted)server -> {
      ServerLevel overworld = server.overworld();
      if (overworld != null) {
        AlphaEngineManager.setWorldSeed(overworld.getSeed());
      }

      TimestopZoneManager.loadZones(server);
      SkyPortalManager.loadFromDisk(server);
    });
    ServerLifecycleEvents.SERVER_STOPPING.register((ServerStopping)server -> EchoRitualManager.clearStateOnServerStop());
    ServerTickEvents.END_SERVER_TICK.register((EndTick)server -> {
      for (SkyPortalEventInstance portalInst : new ArrayList<>(SkyPortalManager.allPortals())) {
        portalInst.tick();
        if (!portalInst.isActive()) {
          SkyPortalManager.stopPortal(server, portalInst.id());
          S2CSkyPortalPayload payload = new S2CSkyPortalPayload(portalInst.id(), false, 256, 256, false, 0L, portalInst.center(), portalInst.sourceDimension(), portalInst.targetDimension());
          for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            ServerPlayNetworking.send(p, payload);
          }
        } else if (DimensionUtil.isRD(portalInst.targetDimension()) && 6000 - portalInst.timerTicks() == 25) {
          S2CSkyPortalCancelPayload cancelPayload = new S2CSkyPortalCancelPayload(true);

          for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            ServerPlayNetworking.send(p, cancelPayload);
          }

          SkyPortalManager.stopPortal(server, portalInst.id());
          S2CSkyPortalPayload payload = new S2CSkyPortalPayload(portalInst.id(), false, 256, 256, false, 0L, portalInst.center(), portalInst.sourceDimension(), portalInst.targetDimension());
          for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            ServerPlayNetworking.send(p, payload);
          }
        } else if (server.getTickCount() % 20 == 0) {
          if (RitualEventRegistry.activeTransition() != null) {
            continue;
          }
          ServerLevel sourceLevel = server.getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(portalInst.sourceDimension())));
          if (sourceLevel != null && sourceLevel.isLoaded(portalInst.center())) {
            BlockState bState = sourceLevel.getBlockState(portalInst.center());
            BlockState aState = sourceLevel.getBlockState(portalInst.center().below());
            boolean valid = bState.is(Blocks.BEACON) && aState.is(Blocks.RESPAWN_ANCHOR);
            if (!valid) {
              SkyPortalManager.stopPortal(server, portalInst.id());
              S2CSkyPortalPayload payload = new S2CSkyPortalPayload(portalInst.id(), false, 256, 256, false, 0L, portalInst.center(), portalInst.sourceDimension(), portalInst.targetDimension());
              for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                ServerPlayNetworking.send(p, payload);
              }
            }
          }
        }
      }
    });
    PlayerBlockBreakEvents.AFTER
      .register(
        (After)(world, player, pos, state, blockEntity) -> {
          if (!world.isClientSide() && world instanceof ServerLevel serverLevel) {
            BlockPos beaconCandidate = null;
            if (state.is(Blocks.BEACON)) {
              beaconCandidate = pos;
            } else if (state.is(Blocks.RESPAWN_ANCHOR)) {
              beaconCandidate = pos.above();
            }

            if (beaconCandidate != null) {
              for (SkyPortalEventInstance portalInst : SkyPortalManager.allPortals()) {
                if (portalInst.sourceDimension().equals(serverLevel.dimension().identifier().toString())) {
                  double dist = portalInst.center().distSqr(beaconCandidate);
                  if (dist <= 9.0) {
                    SkyPortalManager.stopPortal(serverLevel.getServer(), portalInst.id());
                    S2CSkyPortalPayload payload = new S2CSkyPortalPayload(portalInst.id(), false, 256, 256, false, 0L, portalInst.center(), portalInst.sourceDimension(), portalInst.targetDimension());
                    for (ServerPlayer p : serverLevel.getServer().getPlayerList().getPlayers()) {
                      ServerPlayNetworking.send(p, payload);
                    }
                    break;
                  }
                }
              }

              TimestopZoneEvent zone = RitualEventRegistry.findZoneByBeacon(beaconCandidate);
              if (zone != null) {
                EchoRitualEventInstance activeInst = RitualEventRegistry.findInstanceByBeacon(zone.beaconPos());
                if (activeInst != null && activeInst.state() != EchoRitualManager.State.INACTIVE) {
                  EchoRitualManager.handleInterrupt(zone.beaconPos());
                } else {
                  TimestopZoneManager.removeZone(serverLevel, zone.beaconPos());
                  ItemEntity crystal = new ItemEntity(
                    serverLevel, zone.beaconPos().getX() + 0.5, zone.beaconPos().getY() + 1.5, zone.beaconPos().getZ() + 0.5, new ItemStack(Items.ECHO_SHARD)
                  );
                  crystal.setDefaultPickUpDelay();
                  serverLevel.addFreshEntity(crystal);
                }
              }
            }
          }
        }
      );
    ServerPlayConnectionEvents.DISCONNECT.register((Disconnect)(handler, server) -> EchoRitualManager.handlePlayerDisconnect(handler.getPlayer()));
    ServerPlayConnectionEvents.JOIN
      .register(
        (Join)(handler, sender, server) -> {
          ServerPlayer player = handler.getPlayer();
          ServerPlayNetworking.send(player, new S2CWorldSeedPayload(server.overworld().getSeed()));
          if (TimestopZoneManager.hasActiveZone()) {
            TimestopZoneManager.sendZoneToPlayer(player);
          }

          SkyPortalManager.sendPortalToPlayer(player, server);
          EchoRitualEventInstance joinInst = RitualEventRegistry.findInstanceForParticipant(player.getUUID());
          if (joinInst != null && joinInst.targetDimensionId() != null) {
            long seed = AlphaEngineManager.getWorldSeed();
            ServerLevel tlEarly = server.getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(joinInst.targetDimensionId())));
            int targetSkyColor = -1;
            int targetFogColor = -1;
            if (tlEarly != null && joinInst.targetPos() != null) {
              targetSkyColor = (Integer)tlEarly.environmentAttributes().getValue(EnvironmentAttributes.SKY_COLOR, joinInst.targetPos().getCenter(), null);
              targetFogColor = (Integer)tlEarly.environmentAttributes().getValue(EnvironmentAttributes.FOG_COLOR, joinInst.targetPos().getCenter(), null);
            }

            BlockState joinBeaconState = joinInst.deltaCache.get(joinInst.beaconPos());
            if (joinBeaconState == null && tlEarly != null) {
              joinBeaconState = tlEarly.getBlockState(joinInst.beaconPos());
            }

            BlockState joinAnchorState = joinInst.deltaCache.get(joinInst.beaconPos().below());
            if (joinAnchorState == null && tlEarly != null) {
              joinAnchorState = tlEarly.getBlockState(joinInst.beaconPos().below());
            }

            S2CStartTransitionVisualsPayload startPayload = new S2CStartTransitionVisualsPayload(
              joinInst.id(),
              joinInst.targetDimensionId(),
              joinInst.beaconPos(),
              joinInst.targetPos(),
              joinInst.offsetX(),
              joinInst.yOffset(),
              joinInst.offsetZ(),
              seed,
              targetSkyColor,
              targetFogColor,
              joinBeaconState != null ? Block.getId(joinBeaconState) : 0,
              joinAnchorState != null ? Block.getId(joinAnchorState) : 0
            );
            ServerPlayNetworking.send(player, startPayload);
            S2CSyncParticipantsPayload syncPayload = new S2CSyncParticipantsPayload(new ArrayList<>(joinInst.participants()));
            ServerPlayNetworking.send(player, syncPayload);
            S2CRitualPhasePayload phasePayload = new S2CRitualPhasePayload(joinInst.id(), joinInst.phase());
            ServerPlayNetworking.send(player, phasePayload);
          }
        }
      );
    ServerLivingEntityEvents.ALLOW_DEATH.register((AllowDeath)(entity, damageSource, damageAmount) -> {
      if (entity instanceof ServerPlayer player && RitualEventRegistry.isParticipantAny(player.getUUID())) {
        MinecraftServer server = player.level().getServer();
        if (server != null) {
          EchoRitualManager.removeParticipant(player.getUUID(), server);
        }
      }

      return true;
    });
    ServerLifecycleEvents.SERVER_STOPPED.register((ServerStopped)server -> {
      for (SkyPortalEventInstance inst : new ArrayList<>(SkyPortalManager.allPortals())) {
        SkyPortalManager.stopPortal(server, inst.id());
      }

      EchoRitualManager.clearStateOnServerStop();
    });
    ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register((AfterPlayerChange)(player, origin, destination) -> {
      ServerPlayNetworking.send(player, new S2CWorldSeedPayload(destination.getServer().overworld().getSeed()));
      if (TimestopZoneManager.hasActiveZone()) {
        TimestopZoneManager.sendZoneToPlayer(player);
      }

      SkyPortalManager.sendPortalToPlayer(player, player.level().getServer());
    });
    ServerPlayerEvents.AFTER_RESPAWN.register((AfterRespawn)(oldPlayer, newPlayer, alive) -> {
      if (TimestopZoneManager.hasActiveZone()) {
        TimestopZoneManager.sendZoneToPlayer(newPlayer);
      }

      SkyPortalManager.sendPortalToPlayer(newPlayer, newPlayer.level().getServer());
    });
    UseBlockCallback.EVENT
      .register(
        (UseBlockCallback)(player, world, hand, hitResult) -> {
          if (!world.isClientSide() && player != null) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof HoeItem) {
              BlockPos pos = hitResult.getBlockPos();
              BlockState state = world.getBlockState(pos);
              if (hitResult.getDirection() != Direction.DOWN
                && world.getBlockState(pos.above()).isAir()
                && (state.is(AlphaBlocks.ALPHA_DIRT) || state.is(AlphaBlocks.ALPHA_GRASS_BLOCK))) {
                world.setBlock(pos, AlphaBlocks.ALPHA_FARMLAND.defaultBlockState(), 11);
                world.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                stack.hurtAndBreak(1, player, slot);
                return InteractionResult.SUCCESS;
              }
            }
          }

          return InteractionResult.PASS;
        }
      );
  }

  private static InteractionResult handleHologramClick(Player player, Level world, Entity entity) {
    if (!world.isClientSide() && entity instanceof Interaction interaction && player instanceof ServerPlayer serverPlayer) {
      if (interaction.entityTags().contains("nostalgia_matrix_alpha")) {
        ServerLevel alphaLevel = world.getServer()
          .getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath("nostalgia", "alpha_112_01")));
        if (alphaLevel != null) {
          endRitualForPlayer(serverPlayer);
          TeleportCommand.teleportToAlpha(serverPlayer, alphaLevel);
        }

        return InteractionResult.SUCCESS;
      }

      if (interaction.entityTags().contains("nostalgia_matrix_rd")) {
        ServerLevel rdLevel = world.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath("nostalgia", "rd_132211")));
        if (rdLevel != null) {
          endRitualForPlayer(serverPlayer);
          TeleportCommand.teleportToRD(serverPlayer, rdLevel);
        }

        return InteractionResult.SUCCESS;
      }
    }

    return InteractionResult.PASS;
  }

  private static void endRitualForPlayer(ServerPlayer player) {
    EchoRitualEventInstance inst = RitualEventRegistry.findInstanceForParticipant(player.getUUID());
    if (inst != null) {
      EchoRitualManager.endRitualForInstance(inst);
    }
  }
}
