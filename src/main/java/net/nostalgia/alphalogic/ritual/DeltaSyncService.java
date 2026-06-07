package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.network.S2CSyncAlphaDeltasPayload;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class DeltaSyncService {

    private DeltaSyncService() {}

    public static void broadcastSingleDelta(MinecraftServer server, BlockPos overworldPos, BlockState state, String dimensionId, Set<UUID> targets) {
        long[] posArr = new long[] { overworldPos.asLong() };
        int[] stateArr = new int[] { Block.getId(state) };
        S2CSyncAlphaDeltasPayload payload = new S2CSyncAlphaDeltasPayload(dimensionId, posArr, stateArr);

        if (targets == null || targets.isEmpty()) {
            for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(p, payload);
            }
        } else {
            for (UUID uuid : targets) {
                ServerPlayer target = server.getPlayerList().getPlayer(uuid);
                if (target != null) {
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(target, payload);
                }
            }
        }
    }

    public static void broadcastBulkDeltas(MinecraftServer server, Map<BlockPos, BlockState> deltas,
                                           String dimensionId, Set<UUID> targets) {
        if (deltas.isEmpty()) return;

        long[] positions = new long[deltas.size()];
        int[] states = new int[deltas.size()];
        int idx = 0;
        for (Map.Entry<BlockPos, BlockState> entry : deltas.entrySet()) {
            positions[idx] = entry.getKey().asLong();
            states[idx] = Block.getId(entry.getValue());
            idx++;
        }

        S2CSyncAlphaDeltasPayload payload = new S2CSyncAlphaDeltasPayload(dimensionId, positions, states);

        if (targets == null || targets.isEmpty()) {
            for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(p, payload);
            }
        } else {
            for (UUID uuid : targets) {
                ServerPlayer target = server.getPlayerList().getPlayer(uuid);
                if (target != null) {
                    net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(target, payload);
                }
            }
        }
    }

    public static void sendBulkDeltasToPlayer(ServerPlayer player, Map<BlockPos, BlockState> deltas,
                                              String dimensionId) {
        if (deltas.isEmpty()) return;

        long[] positions = new long[deltas.size()];
        int[] states = new int[deltas.size()];
        int idx = 0;
        for (Map.Entry<BlockPos, BlockState> entry : deltas.entrySet()) {
            positions[idx] = entry.getKey().asLong();
            states[idx] = Block.getId(entry.getValue());
            idx++;
        }

        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player,
                new S2CSyncAlphaDeltasPayload(dimensionId, positions, states));
    }
}
