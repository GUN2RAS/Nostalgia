package net.nostalgia.client.gui.hologram3d;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.nostalgia.client.events.caches.impl.AlphaByteCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramRegistry;
import net.nostalgia.client.events.caches.providers.HologramDiskCache;

public class CacheDiagnostics {
  public static volatile boolean overlayActive = false;
  private static final List<String> lines = new ArrayList<>();
  private static long lastUpdateMs = 0L;
  private static final long UPDATE_INTERVAL_MS = 500L;
  private static Path logFile = null;

  public CacheDiagnostics() {
  }

  public static void toggle() {
    overlayActive = !overlayActive;
    if (overlayActive) {
      try {
        Path dir = HologramDiskCache.getServerFolder();
        if (dir == null) {
          dir = Path.of(System.getProperty("user.dir"));
        }

        logFile = dir.resolve("cache_diag.log");
        Files.createDirectories(dir);
        Files.writeString(logFile, "=== Cache Diagnostics Started ===\n", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
      } catch (Exception var1) {
        logFile = null;
      }

      lastUpdateMs = 0L;
    }

    Minecraft mc = Minecraft.getInstance();
    if (mc.player != null) {
      mc.player.sendSystemMessage(Component.literal("\u00a76Cache Diagnostics: " + (overlayActive ? "\u00a7aON" : "\u00a7cOFF")));
    }
  }

  public static void drawHUD(GuiGraphicsExtractor graphics) {
    if (overlayActive) {
      Minecraft mc = Minecraft.getInstance();
      if (!mc.options.hideGui) {
        long now = System.currentTimeMillis();
        if (now - lastUpdateMs > 500L) {
          refreshData();
          lastUpdateMs = now;
        }

        Font font = mc.font;
        int x = 4;
        int y = 4;
        int lineH = 10;
        int maxW = 0;

        for (String line : lines) {
          int w = font.width(line.replaceAll("\u00a7.", ""));
          if (w > maxW) {
            maxW = w;
          }
        }

        int totalH = lines.size() * lineH + 4;
        graphics.fill(x - 2, y - 2, x + maxW + 4, y + totalH, -1072689120);

        for (int i = 0; i < lines.size(); i++) {
          graphics.text(font, lines.get(i), x, y + i * lineH, -1, true);
        }
      }
    }
  }

  private static void refreshData() {
    lines.clear();
    StringBuilder log = new StringBuilder();
    lines.add("\u00a76\u00a7lCACHE DIAGNOSTICS");
    log.append("[").append(System.currentTimeMillis()).append("] ");
    int alphaChunks = AlphaByteCache.CHUNK_CACHE.size();
    long alphaBytes = 0L;

    for (byte[] d : AlphaByteCache.CHUNK_CACHE.values()) {
      alphaBytes += d.length;
    }

    String dimId = AlphaByteCache.cachedDimensionId;
    lines.add("\u00a7eAlpha\u00a77: " + alphaChunks + " chunks \u00a7f" + fmt(alphaBytes) + " \u00a77dim=" + (dimId != null ? dimId : "null"));
    log.append("alpha=").append(alphaChunks).append("/").append(fmt(alphaBytes)).append(" ");
    addDimStats("nostalgia:alpha_112_01", "Alpha", log);
    addDimStats("nostalgia:rd_132211", "RD", log);
    addDimStats("minecraft:overworld", "OW", log);

    try {
      Path folder = HologramDiskCache.getServerFolder();
      if (folder != null && Files.exists(folder)) {
        long total = 0L;

        try (Stream<Path> stream = Files.list(folder)) {
          for (Path p : stream.toList()) {
            if (Files.isRegularFile(p)) {
              total += Files.size(p);
            }
          }
        }

        lines.add("\u00a7eDisk\u00a77: \u00a7f" + fmt(total));
        log.append("disk=").append(fmt(total)).append(" ");
      }
    } catch (Exception var19) {
    }

    long totalRAM = Runtime.getRuntime().totalMemory();
    long freeRAM = Runtime.getRuntime().freeMemory();
    long usedRAM = totalRAM - freeRAM;
    lines.add("\u00a7eJVM\u00a77: \u00a7f" + fmt(usedRAM) + "\u00a77/" + fmt(totalRAM));
    if (logFile != null) {
      try (BufferedWriter w = Files.newBufferedWriter(logFile, StandardOpenOption.APPEND)) {
        w.write(log.toString());
        w.newLine();
      } catch (Exception var17) {
      }
    }
  }

  private static void addDimStats(String dimId, String label, StringBuilder log) {
    DimensionHologramCache cache = DimensionHologramRegistry.getByName(dimId);
    if (cache != null) {
      int sections = cache.getSections().size();
      int deltas = cache.overrideCount();
      if (sections != 0 || deltas != 0) {
        lines.add("\u00a7e" + label + "\u00a77: sec=" + sections + " delta=" + deltas + " highY=" + cache.getHighestY());
        log.append(label.toLowerCase()).append("=").append(sections).append("s/").append(deltas).append("d ");
      }
    }
  }

  private static String fmt(long bytes) {
    if (bytes < 1024L) {
      return bytes + "B";
    } else if (bytes < 1048576L) {
      return String.format("%.1fKB", bytes / 1024.0);
    } else {
      return bytes < 1073741824L ? String.format("%.1fMB", bytes / 1048576.0) : String.format("%.2fGB", bytes / 1.073741824E9);
    }
  }

  public static void runFullDiagnostic() {
    toggle();
  }
}
