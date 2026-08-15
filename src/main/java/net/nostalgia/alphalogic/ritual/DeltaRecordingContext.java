package net.nostalgia.alphalogic.ritual;

public final class DeltaRecordingContext {
  public static final ThreadLocal<Boolean> IS_CHUNK_TICK = ThreadLocal.withInitial(() -> false);

  private DeltaRecordingContext() {
  }
}
