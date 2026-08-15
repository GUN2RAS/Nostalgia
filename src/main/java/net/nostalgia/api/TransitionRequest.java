package net.nostalgia.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.nostalgia.alphalogic.ritual.geometry.TransitionGeometry;

public record TransitionRequest(ServerPlayer player, String sourceDim, String targetDim, TransitionGeometry geometry, BlockPos beaconPos) {
}
