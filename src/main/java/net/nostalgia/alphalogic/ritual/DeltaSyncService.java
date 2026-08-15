package net.nostalgia.alphalogic.ritual;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.network.S2CSyncAlphaDeltasPayload;

public final class DeltaSyncService {
  private DeltaSyncService() {
  }

  public static void broadcastSingleDelta(MinecraftServer server, BlockPos overworldPos, BlockState state, String dimensionId, Set<UUID> targets) {
    long[] posArr = new long[]{overworldPos.asLong()};
    int[] stateArr = new int[]{Block.getId(state)};
    S2CSyncAlphaDeltasPayload payload = new S2CSyncAlphaDeltasPayload(dimensionId, posArr, stateArr);
    if (targets != null && !targets.isEmpty()) {
      for (UUID uuid : targets) {
        ServerPlayer target = server.getPlayerList().getPlayer(uuid);
        if (target != null) {
          ServerPlayNetworking.send(target, payload);
        }
      }
    } else {
      for (ServerPlayer p : server.getPlayerList().getPlayers()) {
        ServerPlayNetworking.send(p, payload);
      }
    }
  }

  public static void broadcastBulkDeltas(MinecraftServer server, Map<BlockPos, BlockState> deltas, String dimensionId, Set<UUID> targets) {
    if (!deltas.isEmpty()) {
      long[] positions = new long[deltas.size()];
      int[] states = new int[deltas.size()];
      int idx = 0;

      for (Entry<BlockPos, BlockState> entry : deltas.entrySet()) {
        positions[idx] = entry.getKey().asLong();
        states[idx] = Block.getId(entry.getValue());
        idx++;
      }

      S2CSyncAlphaDeltasPayload payload = new S2CSyncAlphaDeltasPayload(dimensionId, positions, states);
      if (targets != null && !targets.isEmpty()) {
        for (UUID uuid : targets) {
          ServerPlayer target = server.getPlayerList().getPlayer(uuid);
          if (target != null) {
            ServerPlayNetworking.send(target, payload);
          }
        }
      } else {
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
          ServerPlayNetworking.send(p, payload);
        }
      }
    }
  }

  public static void sendBulkDeltasToPlayer(ServerPlayer player, Map<BlockPos, BlockState> deltas, String dimensionId) {
    if (!deltas.isEmpty()) {
      long[] positions = new long[deltas.size()];
      int[] states = new int[deltas.size()];
      int idx = 0;

      for (Entry<BlockPos, BlockState> entry : deltas.entrySet()) {
        positions[idx] = entry.getKey().asLong();
        states[idx] = Block.getId(entry.getValue());
        idx++;
      }

      ServerPlayNetworking.send(player, new S2CSyncAlphaDeltasPayload(dimensionId, positions, states));
    }
  }
}
