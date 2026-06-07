package net.nostalgia.client.performance;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.DeltaTracker;
import net.nostalgia.client.events.caches.impl.AlphaByteCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramRegistry;
import net.nostalgia.client.events.caches.providers.DimensionHologramCache;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class SHAMetricsCollector {
    public static boolean overlayActive = false;


    public static volatile long lastCompileTimeNs = 0L;
    public static volatile long lastRenderTimeNs = 0L;
    

    public static volatile long lastDiskWriteTimeMs = 0L;
    public static volatile long lastDiskReadTimeMs = 0L;
    public static volatile double lastDiskWriteSizeMB = 0.0;
    public static volatile double lastDiskReadSizeMB = 0.0;
    public static volatile String lastDiskWriteTarget = "None";
    public static volatile String lastDiskReadTarget = "None";
    

    private static final int MAX_HISTORY = 60;
    public static final LinkedList<Float> compileHistory = new LinkedList<>();
    public static final LinkedList<Float> renderHistory = new LinkedList<>();
    public static final LinkedList<Float> diskWriteHistory = new LinkedList<>();
    public static final LinkedList<Float> diskReadHistory = new LinkedList<>();
    

    private static final int MAX_EVENTS = 20;
    public static final LinkedList<String> eventLog = new LinkedList<>();
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    public static synchronized void logEvent(String msg) {
        String timestamp = timeFormat.format(new Date());
        String entry = "[" + timestamp + "] " + msg;
        eventLog.addLast(entry);
        if (eventLog.size() > MAX_EVENTS) {
            eventLog.removeFirst();
        }
    }

    public static synchronized void recordCompile(long durationNs) {
        lastCompileTimeNs = durationNs;
        float ms = durationNs / 1_000_000.0f;
        compileHistory.addLast(ms);
        if (compileHistory.size() > MAX_HISTORY) {
            compileHistory.removeFirst();
        }
    }

    public static synchronized void recordRender(long durationNs) {
        lastRenderTimeNs = durationNs;
        float ms = durationNs / 1_000_000.0f;
        renderHistory.addLast(ms);
        if (renderHistory.size() > MAX_HISTORY) {
            renderHistory.removeFirst();
        }
    }

    public static synchronized void recordDiskWrite(String target, long durationNs, long bytes) {
        float ms = durationNs / 1_000_000.0f;
        double mb = bytes / (1024.0 * 1024.0);
        lastDiskWriteTimeMs = (long) ms;
        lastDiskWriteSizeMB = mb;
        lastDiskWriteTarget = target;
        diskWriteHistory.addLast(ms);
        if (diskWriteHistory.size() > MAX_HISTORY) {
            diskWriteHistory.removeFirst();
        }
        logEvent(String.format(Locale.US, "Disk write: %s (%.2f MB in %.1f ms, %.1f MB/s)", 
            target, mb, ms, ms > 0 ? (mb / (ms / 1000.0)) : 0.0));
    }

    public static synchronized void recordDiskRead(String target, long durationNs, long bytes) {
        float ms = durationNs / 1_000_000.0f;
        double mb = bytes / (1024.0 * 1024.0);
        lastDiskReadTimeMs = (long) ms;
        lastDiskReadSizeMB = mb;
        lastDiskReadTarget = target;
        diskReadHistory.addLast(ms);
        if (diskReadHistory.size() > MAX_HISTORY) {
            diskReadHistory.removeFirst();
        }
        logEvent(String.format(Locale.US, "Disk read: %s (%.2f MB in %.1f ms, %.1f MB/s)", 
            target, mb, ms, ms > 0 ? (mb / (ms / 1000.0)) : 0.0));
    }

    public static void drawHUD(GuiGraphicsExtractor graphics, DeltaTracker tracker) {
        if (!overlayActive) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.screen != null) return;

        Font font = mc.font;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();


        int w = 240;
        int h = 330;
        int x = screenWidth - w - 10;
        int y = 10;


        graphics.fill(x, y, x + w, y + h, 0xCE111116);
        graphics.fill(x, y, x + w, y + 1, 0x40FFFFFF);
        graphics.fill(x, y + h - 1, x + w, y + h, 0x40000000);
        graphics.fill(x, y, x + 1, y + h, 0x40FFFFFF);
        graphics.fill(x + w - 1, y, x + w, y + h, 0x40000000);

        int ty = y + 8;
        graphics.text(font, "§b§lSHA PERFORMANCE PROFILER", x + 12, ty, 0xFF00FFFF, false);
        ty += 12;
        graphics.fill(x + 10, ty, x + w - 10, ty + 1, 0x25FFFFFF);
        ty += 8;


        int alphaChunks = AlphaByteCache.FAST_CACHE.size();
        double alphaRAM = (alphaChunks * 32768) / 1024.0 / 1024.0;

        int totalSections = 0;
        int totalOverrides = 0;
        try {
            DimensionHologramCache ow = DimensionHologramRegistry.getByName("minecraft:overworld");
            if (ow != null) {
                totalSections += ow.getSections().size();
                totalOverrides += ow.overrideCount();
            }
            DimensionHologramCache alpha = DimensionHologramRegistry.getByName("nostalgia:alpha_112_01");
            if (alpha != null) {
                totalSections += alpha.getSections().size();
                totalOverrides += alpha.overrideCount();
            }
            DimensionHologramCache rd = DimensionHologramRegistry.getByName("nostalgia:rd_132211");
            if (rd != null) {
                totalSections += rd.getSections().size();
                totalOverrides += rd.overrideCount();
            }
        } catch (Exception ignored) {}

        double totalRAM = alphaRAM + (totalSections * 4096 * 2) / 1024.0 / 1024.0;

        float avgRender = getAverage(renderHistory);
        float avgCompile = getAverage(compileHistory);


        graphics.text(font, "§fSky Render: §a" + String.format(Locale.US, "%.3f ms", avgRender) + " §7(last: " + String.format(Locale.US, "%.2f", lastRenderTimeNs / 1_000_000.0f) + ")", x + 12, ty, 0xFFFFFFFF, false);
        ty += 12;
        graphics.text(font, "§fSodium Spoof: §a" + String.format(Locale.US, "%.3f ms", avgCompile) + " §7(last: " + String.format(Locale.US, "%.2f", lastCompileTimeNs / 1_000_000.0f) + ")", x + 12, ty, 0xFFFFFFFF, false);
        ty += 12;
        graphics.text(font, "§fRAM cache: §d" + String.format(Locale.US, "%.2f MB", totalRAM), x + 12, ty, 0xFFFFFFFF, false);
        ty += 12;
        graphics.text(font, "§fCached chunks: §e" + alphaChunks + " §7(Alpha)", x + 12, ty, 0xFFFFFFFF, false);
        ty += 12;
        graphics.text(font, "§fCached sections: §e" + totalSections + " §7(Other)", x + 12, ty, 0xFFFFFFFF, false);
        ty += 12;
        graphics.text(font, "§fOverrides: §e" + totalOverrides + " §7(Deltas)", x + 12, ty, 0xFFFFFFFF, false);
        ty += 12;

        boolean renderActive = net.nostalgia.client.render.PortalSkyRenderer.active;
        boolean groundedActive = false;
        graphics.text(font, "§fHolograms: " + (renderActive ? "§aPORTAL" : (groundedActive ? "§aGROUNDED" : "§cOFF")), x + 12, ty, 0xFFFFFFFF, false);
        ty += 12;


        ty += 4;
        graphics.fill(x + 10, ty, x + w - 10, ty + 1, 0x25FFFFFF);
        ty += 6;
        graphics.text(font, "§b§lDISK CACHE SYSTEM:", x + 12, ty, 0xFF00FFFF, false);
        ty += 12;
        String readText = lastDiskReadTimeMs > 0 ? String.format(Locale.US, "§a%d ms §7(%.2f MB)", lastDiskReadTimeMs, lastDiskReadSizeMB) : "§7None";
        graphics.text(font, "§fLast Load: " + readText, x + 12, ty, 0xFFFFFFFF, false);
        ty += 12;
        String writeText = lastDiskWriteTimeMs > 0 ? String.format(Locale.US, "§a%d ms §7(%.2f MB)", lastDiskWriteTimeMs, lastDiskWriteSizeMB) : "§7None";
        graphics.text(font, "§fLast Save: " + writeText, x + 12, ty, 0xFFFFFFFF, false);
        ty += 12;


        ty += 6;
        graphics.text(font, "§7Latency Timeline (last 50 frames)", x + 12, ty, 0xFFAAAAAA, false);
        ty += 12;

        int graphH = 45;
        int graphY = ty;
        graphics.fill(x + 10, graphY, x + w - 10, graphY + graphH, 0x15FFFFFF);
        graphics.fill(x + 10, graphY, x + w - 10, graphY + 1, 0x30FFFFFF);
        graphics.fill(x + 10, graphY + graphH - 1, x + w - 10, graphY + graphH, 0x30FFFFFF);

        synchronized (SHAMetricsCollector.class) {
            int barCount = Math.min(renderHistory.size(), 50);
            int barW = 4;
            int startIdx = renderHistory.size() - barCount;
            for (int i = 0; i < barCount; i++) {
                float ms = renderHistory.get(startIdx + i);
                int barH = (int) Math.min(graphH, (ms / 16.67f) * graphH);
                if (barH < 1) barH = 1;
                
                int bx = x + 12 + i * barW;
                int by1 = graphY + graphH - barH;
                int by2 = graphY + graphH;

                int color = 0xFF55FF55;
                if (ms > 4.0f) color = 0xFFFF5555;
                else if (ms > 1.5f) color = 0xFFFFFF55;

                graphics.fill(bx, by1, bx + barW - 1, by2, color);
            }
        }
        ty += graphH + 8;


        graphics.fill(x + 10, ty, x + w - 10, ty + 1, 0x25FFFFFF);
        ty += 6;
        graphics.text(font, "§b§lLAST HOLOGRAM EVENT:", x + 12, ty, 0xFF00FFFF, false);
        ty += 12;

        String lastEvent = "No events logged";
        synchronized (SHAMetricsCollector.class) {
            if (!eventLog.isEmpty()) {
                lastEvent = eventLog.getLast();
            }
        }

        if (lastEvent.length() > 38) {
            lastEvent = lastEvent.substring(0, 35) + "...";
        }
        graphics.text(font, "§e" + lastEvent, x + 12, ty, 0xFFFFFFFF, false);
    }

    private static float getAverage(List<Float> list) {
        if (list == null || list.isEmpty()) return 0.0f;
        float sum = 0.0f;
        synchronized (SHAMetricsCollector.class) {
            for (float v : list) {
                sum += v;
            }
            return sum / list.size();
        }
    }

    public static String exportReport() {
        try {
            File dir = new File("nostalgia_cache");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, "sha_perf_report.md");
            FileWriter writer = new FileWriter(file);
            
            writer.write("# SHA Spoofed Hologram Architecture Performance Report\n\n");
            SimpleDateFormat reportDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            writer.write("Generated at: " + reportDF.format(new Date()) + "\n\n");

            writer.write("## 1. Core Metrics Summary\n");
            

            int alphaChunks = AlphaByteCache.FAST_CACHE.size();
            double alphaRAM = (alphaChunks * 32768) / 1024.0 / 1024.0;
            int totalSections = 0;
            int totalOverrides = 0;
            try {
                DimensionHologramCache ow = DimensionHologramRegistry.getByName("minecraft:overworld");
                if (ow != null) {
                    totalSections += ow.getSections().size();
                    totalOverrides += ow.overrideCount();
                }
                DimensionHologramCache alpha = DimensionHologramRegistry.getByName("nostalgia:alpha_112_01");
                if (alpha != null) {
                    totalSections += alpha.getSections().size();
                    totalOverrides += alpha.overrideCount();
                }
                DimensionHologramCache rd = DimensionHologramRegistry.getByName("nostalgia:rd_132211");
                if (rd != null) {
                    totalSections += rd.getSections().size();
                    totalOverrides += rd.overrideCount();
                }
            } catch (Exception ignored) {}
            double totalRAM = alphaRAM + (totalSections * 4096 * 2) / 1024.0 / 1024.0;

            writer.write("- **RAM Cache usage**: " + String.format(Locale.US, "%.2f MB", totalRAM) + "\n");
            writer.write("- **Cached Alpha chunks**: " + alphaChunks + " (" + String.format(Locale.US, "%.2f MB", alphaRAM) + ")\n");
            writer.write("- **Cached dimensions sections**: " + totalSections + "\n");
            writer.write("- **Active Block Deltas / Overrides**: " + totalOverrides + "\n");
            writer.write("- **Avg Sky Portal Render Time**: " + String.format(Locale.US, "%.3f ms", getAverage(renderHistory)) + "\n");
            writer.write("- **Avg Sodium Section Compile time**: " + String.format(Locale.US, "%.3f ms", getAverage(compileHistory)) + "\n\n");

            writer.write("## 2. Disk Cache Performance\n");
            writer.write("- **Last Disk Load Time**: " + (lastDiskReadTimeMs > 0 ? String.format(Locale.US, "%d ms (Size: %.2f MB, Target: %s)", lastDiskReadTimeMs, lastDiskReadSizeMB, lastDiskReadTarget) : "N/A") + "\n");
            writer.write("- **Last Disk Save Time**: " + (lastDiskWriteTimeMs > 0 ? String.format(Locale.US, "%d ms (Size: %.2f MB, Target: %s)", lastDiskWriteTimeMs, lastDiskWriteSizeMB, lastDiskWriteTarget) : "N/A") + "\n");
            writer.write("- **Avg Disk Load Time**: " + String.format(Locale.US, "%.1f ms", getAverage(diskReadHistory)) + "\n");
            writer.write("- **Avg Disk Save Time**: " + String.format(Locale.US, "%.1f ms", getAverage(diskWriteHistory)) + "\n\n");

            writer.write("## 3. Event Log Timeline\n\n");
            writer.write("```\n");
            synchronized (SHAMetricsCollector.class) {
                for (String log : eventLog) {
                    writer.write(log + "\n");
                }
            }
            writer.write("```\n\n");

            writer.write("## 4. Frame Profiling Metrics\n\n");
            writer.write("| Frame # | Sky Render (ms) | Sodium Compile (ms) |\n");
            writer.write("|---|---|---|\n");
            
            synchronized (SHAMetricsCollector.class) {
                int count = Math.min(renderHistory.size(), compileHistory.size());
                for (int i = 0; i < count; i++) {
                    writer.write(String.format(Locale.US, "| %d | %.3f ms | %.3f ms |\n", i + 1, renderHistory.get(i), compileHistory.get(i)));
                }
            }

            writer.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
