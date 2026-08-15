package net.nostalgia.api;

import net.minecraft.server.level.ServerLevel;
import net.nostalgia.alphalogic.ritual.DimensionUtil;
import net.nostalgia.alphalogic.ritual.EchoRitualManager;

public final class NostalgiaAPI {
  private NostalgiaAPI() {
  }

  public static void startTransition(TransitionRequest request) {
    if (request != null && request.player() != null && request.beaconPos() != null) {
      ServerLevel targetLevel = DimensionUtil.resolveLevel(request.player().level().getServer(), request.targetDim());
      EchoRitualManager.startTeleportTransition(request.player(), targetLevel, request.targetDim(), request.beaconPos());
    }
  }
}
