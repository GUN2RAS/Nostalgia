package net.nostalgia.alphalogic.ritual;

public enum RitualPhase {
  IDLE(0),
  CACHE_GEN(1),
  HOLOGRAM_DISPLAY(2),
  TELEPORT(3);

  private final int id;

  private RitualPhase(int id) {
    this.id = id;
  }

  public int id() {
    return this.id;
  }

  public static RitualPhase fromId(int id) {
    return switch (id) {
      case 1 -> CACHE_GEN;
      case 2 -> HOLOGRAM_DISPLAY;
      case 3 -> TELEPORT;
      default -> IDLE;
    };
  }
}
