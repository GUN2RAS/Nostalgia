package net.nostalgia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.EndTick;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.nostalgia.alphalogic.ritual.HologramChunkLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StressTestCommand {
  private static final Logger LOGGER = LoggerFactory.getLogger("NostalgiaStress");
  private static final int[] STAGE_COUNTS = new int[]{1, 5, 20};
  private static final long STAGE_TIMEOUT_MS = 60000L;
  private static volatile boolean running = false;
  private static boolean tickListenerRegistered = false;
  private static CommandSourceStack pendingSource;
  private static BlockPos playerPos;
  private static long testStartMs;
  private static int currentStageIndex = -1;
  private static long stageStartMs;
  private static int stagePendingCount;
  private static AtomicInteger stageCompletedCount;
  private static final List<StressTestCommand.StageResult> stageResults = new ArrayList<>();
  private static final List<StressTestCommand.TaskResult> currentStageTaskResults = Collections.synchronizedList(new ArrayList<>());
  private static long stageBaselineMsptNs;
  private static int progressTickCounter;

  public StressTestCommand() {
  }

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(
      (LiteralArgumentBuilder)Commands.literal("nostalgia")
        .then(
          ((LiteralArgumentBuilder)Commands.literal("stresstest")
              .requires(source -> Commands.hasPermission(Commands.LEVEL_GAMEMASTERS).test(source) && source.getEntity() != null))
            .executes(StressTestCommand::runStressTest)
        )
    );
  }

  private static int runStressTest(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
    if (running) {
      ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("\u00a7c[STRESS] Test already running!"));
      return 0;
    } else {
      ServerPlayer player = ((CommandSourceStack)context.getSource()).getPlayerOrException();
      ServerLevel overworld = ((CommandSourceStack)context.getSource()).getServer().getLevel(Level.OVERWORLD);
      if (overworld == null) {
        ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("\u00a7c[STRESS] Overworld not found!"));
        return 0;
      } else {
        running = true;
        pendingSource = (CommandSourceStack)context.getSource();
        playerPos = player.blockPosition();
        testStartMs = System.currentTimeMillis();
        stageResults.clear();
        currentStageIndex = -1;
        progressTickCounter = 0;
        LOGGER.info("=== STRESS TEST START (staged: 1 -> 5 -> 20) ===");
        pendingSource.sendSuccess(() -> Component.literal("\u00a76[STRESS] \u00a7eStarting staged test: 1 \u2192 5 \u2192 20 tasks, 60s timeout each"), true);
        if (!tickListenerRegistered) {
          tickListenerRegistered = true;
          ServerTickEvents.END_SERVER_TICK.register((EndTick)server -> onTick(server));
        }

        startNextStage(((CommandSourceStack)context.getSource()).getServer());
        return 1;
      }
    }
  }

  private static void startNextStage(MinecraftServer server) {
    currentStageIndex++;
    if (currentStageIndex >= STAGE_COUNTS.length) {
      onAllStagesComplete(server);
    } else {
      HologramChunkLoader.cancelStressTasks();
      int taskCount = STAGE_COUNTS[currentStageIndex];
      stageStartMs = System.currentTimeMillis();
      stageBaselineMsptNs = server.getAverageTickTimeNanos();
      stagePendingCount = taskCount;
      stageCompletedCount = new AtomicInteger(0);
      currentStageTaskResults.clear();
      progressTickCounter = 0;
      ServerLevel overworld = server.getLevel(Level.OVERWORLD);
      Random rand = new Random(42 + currentStageIndex);
      int spread = 2500;
      int radius = 300;
      int chunksPerTask = HologramChunkLoader.getAllChunksInRadius(playerPos, radius).size();
      LOGGER.info("--- Stage {}: {} task(s), ~{} chunks each ---", new Object[]{currentStageIndex + 1, taskCount, chunksPerTask});
      if (pendingSource != null) {
        int stageNum = currentStageIndex + 1;
        pendingSource.sendSuccess(() -> Component.literal("\u00a76[STRESS] \u00a7bStage " + stageNum + "\u00a7f: " + taskCount + " task(s)..."), false);
      }

      for (int i = 0; i < taskCount; i++) {
        int offsetX = rand.nextInt(spread * 2) - spread;
        int offsetZ = rand.nextInt(spread * 2) - spread;
        BlockPos center = playerPos.offset(offsetX, 0, offsetZ);
        int taskIndex = i + 1;
        long taskStartMs = System.currentTimeMillis();
        HologramChunkLoader.startStressLoading(overworld, center, radius, () -> {
          long taskDuration = System.currentTimeMillis() - taskStartMs;
          currentStageTaskResults.add(new StressTestCommand.TaskResult("T" + taskIndex, center, chunksPerTask, taskDuration));
          int done = stageCompletedCount.incrementAndGet();
          LOGGER.info("  Stage {} task {}/{} done in {} ms", new Object[]{currentStageIndex + 1, done, stagePendingCount, taskDuration});
          if (done >= stagePendingCount) {
            server.execute(() -> onStageComplete(server, false));
          }
        });
      }
    }
  }

  private static void onTick(MinecraftServer server) {
    if (running && currentStageIndex >= 0 && currentStageIndex < STAGE_COUNTS.length) {
      progressTickCounter++;
      long elapsed = System.currentTimeMillis() - stageStartMs;
      if (elapsed > 60000L && stageCompletedCount.get() < stagePendingCount) {
        LOGGER.warn("!!! Stage {} TIMEOUT after {} ms !!!", currentStageIndex + 1, elapsed);
        onStageComplete(server, true);
      } else {
        if (progressTickCounter % 100 == 0) {
          int done = stageCompletedCount.get();
          double mspt = server.getAverageTickTimeNanos() / 1000000.0;
          int inflight = HologramChunkLoader.getGlobalInflight();
          int activeTasks = HologramChunkLoader.getActiveTaskCount();
          Runtime rt = Runtime.getRuntime();
          long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024L / 1024L;
          long totalMB = rt.totalMemory() / 1024L / 1024L;
          String eta = "...";
          if (done > 0) {
            long remaining = elapsed * stagePendingCount / done - elapsed;
            eta = remaining / 1000L + "s";
          }

          LOGGER.info(
            "[S{}] {}/{} done | inflight:{} active:{} | MSPT:{} ms | Heap:{}/{} MB | {}s | ETA:{}",
            new Object[]{
              currentStageIndex + 1,
              done,
              stagePendingCount,
              inflight,
              activeTasks,
              String.format(Locale.US, "%.1f", mspt),
              usedMB,
              totalMB,
              elapsed / 1000L,
              eta
            }
          );
        }
      }
    }
  }

  private static void onStageComplete(MinecraftServer server, boolean timedOut) {
    long stageDuration = System.currentTimeMillis() - stageStartMs;
    double msptAfter = server.getAverageTickTimeNanos() / 1000000.0;
    double msptBefore = stageBaselineMsptNs / 1000000.0;
    Runtime rt = Runtime.getRuntime();
    long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024L / 1024L;
    long totalMB = rt.totalMemory() / 1024L / 1024L;
    int completed = stageCompletedCount.get();
    String diag = null;
    if (timedOut) {
      int inflight = HologramChunkLoader.getGlobalInflight();
      int activeTasks = HologramChunkLoader.getActiveTaskCount();
      int pending = stagePendingCount - completed;
      diag = String.format(
        Locale.US,
        "Timeout after %d ms: %d/%d tasks done, %d pending, inflight=%d, activeTasks=%d, MSPT=%.1f ms, Heap=%d/%d MB",
        stageDuration,
        completed,
        stagePendingCount,
        pending,
        inflight,
        activeTasks,
        msptAfter,
        usedMB,
        totalMB
      );
      LOGGER.warn("  DIAG: {}", diag);
    }

    StressTestCommand.StageResult result = new StressTestCommand.StageResult(
      STAGE_COUNTS[currentStageIndex],
      stageDuration,
      timedOut,
      msptBefore,
      msptAfter,
      usedMB,
      totalMB,
      completed,
      new ArrayList<>(currentStageTaskResults),
      diag
    );
    stageResults.add(result);
    String status = timedOut ? "\u00a7cTIMEOUT" : "\u00a7aDONE";
    if (pendingSource != null) {
      int stageNum = currentStageIndex + 1;
      pendingSource.sendSuccess(
        () -> Component.literal(
          "\u00a76[STRESS] \u00a7bStage "
            + stageNum
            + " "
            + status
            + " \u00a7f"
            + stageDuration
            + " ms"
            + (timedOut ? " \u00a7c(" + completed + "/" + STAGE_COUNTS[stageNum - 1] + " completed)" : "")
        ),
        false
      );
    }

    LOGGER.info(
      "--- Stage {} {} in {} ms (MSPT: {} -> {}) ---",
      new Object[]{
        currentStageIndex + 1,
        timedOut ? "TIMEOUT" : "COMPLETE",
        stageDuration,
        String.format(Locale.US, "%.2f", msptBefore),
        String.format(Locale.US, "%.2f", msptAfter)
      }
    );
    HologramChunkLoader.cancelStressTasks();
    startNextStage(server);
  }

  private static void onAllStagesComplete(MinecraftServer server) {
    long totalDuration = System.currentTimeMillis() - testStartMs;
    LOGGER.info("=== ALL STAGES COMPLETE in {} ms ===", totalDuration);
    String reportPath = writeReport(totalDuration);
    if (pendingSource != null) {
      pendingSource.sendSuccess(() -> Component.literal("\u00a76[STRESS] \u00a7a=== TEST COMPLETE \u00a7f" + totalDuration + " ms ==="), true);

      for (int i = 0; i < stageResults.size(); i++) {
        StressTestCommand.StageResult sr = stageResults.get(i);
        int stageNum = i + 1;
        String icon = sr.timedOut ? "\u00a7c\u2717" : "\u00a7a\u2713";
        if (sr.timedOut) {
          pendingSource.sendSuccess(
            () -> Component.literal(
              "\u00a76  S"
                + stageNum
                + " "
                + icon
                + " \u00a7f"
                + sr.taskCount
                + " tasks: \u00a7cTIMEOUT \u00a77("
                + sr.completedTasks
                + "/"
                + sr.taskCount
                + " done)"
            ),
            false
          );
        } else {
          long fastest = sr.taskResults.stream().mapToLong(r -> r.durationMs).min().orElse(0L);
          long slowest = sr.taskResults.stream().mapToLong(r -> r.durationMs).max().orElse(0L);
          pendingSource.sendSuccess(
            () -> Component.literal(
              "\u00a76  S"
                + stageNum
                + " "
                + icon
                + " \u00a7f"
                + sr.taskCount
                + " tasks: \u00a7e"
                + sr.durationMs
                + "ms \u00a77(min="
                + fastest
                + " max="
                + slowest
                + " MSPT="
                + String.format(Locale.US, "%.1f", sr.msptAfter)
                + ")"
            ),
            false
          );
        }
      }

      if (reportPath != null) {
        pendingSource.sendSuccess(() -> Component.literal("\u00a76[STRESS] \u00a7fReport: \u00a7b" + reportPath), false);
      }
    }

    running = false;
  }

  private static String writeReport(long totalDuration) {
    try {
      File dir = new File("nostalgia_cache");
      if (!dir.exists()) {
        dir.mkdirs();
      }

      SimpleDateFormat fileDF = new SimpleDateFormat("yyyyMMdd_HHmmss");
      String filename = "stresstest_" + fileDF.format(new Date()) + ".md";
      File file = new File(dir, filename);
      SimpleDateFormat reportDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      try (FileWriter w = new FileWriter(file)) {
        w.write("# Hologram Cache Stress Test Report\n\n");
        w.write("Generated: " + reportDF.format(new Date()) + "\n\n");
        w.write("## Configuration\n");
        w.write("- **Stages**: 1 \u2192 5 \u2192 20 tasks\n");
        w.write("- **Timeout**: 60s per stage\n");
        w.write("- **Spread**: \u00b12500 blocks\n");
        w.write("- **Radius**: 300 blocks\n");
        w.write("- **Global inflight limit**: 40\n");
        w.write("- **Total test time**: " + totalDuration + " ms (" + String.format(Locale.US, "%.1f", totalDuration / 1000.0) + " sec)\n\n");
        w.write("## Stage Summary\n\n");
        w.write("| Stage | Tasks | Duration | Status | MSPT Before | MSPT After | Heap | Completed |\n");
        w.write("|-------|-------|----------|--------|-------------|------------|------|-----------|\n");

        for (int i = 0; i < stageResults.size(); i++) {
          StressTestCommand.StageResult sr = stageResults.get(i);
          w.write(
            String.format(
              Locale.US,
              "| %d | %d | %d ms | %s | %.2f ms | %.2f ms | %d/%d MB | %d/%d |\n",
              i + 1,
              sr.taskCount,
              sr.durationMs,
              sr.timedOut ? "**TIMEOUT**" : "OK",
              sr.msptBefore,
              sr.msptAfter,
              sr.heapUsedMB,
              sr.heapTotalMB,
              sr.completedTasks,
              sr.taskCount
            )
          );
        }

        for (int i = 0; i < stageResults.size(); i++) {
          StressTestCommand.StageResult sr = stageResults.get(i);
          w.write("\n## Stage " + (i + 1) + " (" + sr.taskCount + " tasks)\n\n");
          if (sr.timedOut) {
            w.write("> **TIMEOUT**: " + sr.timeoutDiag + "\n\n");
          }

          if (!sr.taskResults.isEmpty()) {
            long fastest = sr.taskResults.stream().mapToLong(r -> r.durationMs).min().orElse(0L);
            long slowest = sr.taskResults.stream().mapToLong(r -> r.durationMs).max().orElse(0L);
            double avg = sr.taskResults.stream().mapToLong(r -> r.durationMs).average().orElse(0.0);
            w.write("| Metric | Value |\n");
            w.write("|--------|-------|\n");
            w.write("| Fastest | " + fastest + " ms |\n");
            w.write("| Slowest | " + slowest + " ms |\n");
            w.write("| Average | " + String.format(Locale.US, "%.0f", avg) + " ms |\n\n");
            w.write("| # | Label | Center | Duration |\n");
            w.write("|---|-------|--------|----------|\n");
            List<StressTestCommand.TaskResult> sorted = new ArrayList<>(sr.taskResults);
            sorted.sort((a, b) -> Long.compare(a.durationMs, b.durationMs));

            for (int j = 0; j < sorted.size(); j++) {
              StressTestCommand.TaskResult tr = sorted.get(j);
              w.write("| " + (j + 1) + " | " + tr.label + " | " + tr.center.getX() + ", " + tr.center.getZ() + " | " + tr.durationMs + " ms |\n");
            }
          } else {
            w.write("*No tasks completed before timeout*\n");
          }
        }

        w.write("\n## System Info\n\n");
        w.write("- Java: " + System.getProperty("java.version") + "\n");
        w.write("- CPU cores: " + Runtime.getRuntime().availableProcessors() + "\n");
        w.write("- Max heap: " + Runtime.getRuntime().maxMemory() / 1024L / 1024L + " MB\n");
      }

      return file.getAbsolutePath();
    } catch (Exception var21) {
      LOGGER.error("Failed to write stress test report", var21);
      return null;
    }
  }

  private static class StageResult {
    final int taskCount;
    final long durationMs;
    final boolean timedOut;
    final double msptBefore;
    final double msptAfter;
    final long heapUsedMB;
    final long heapTotalMB;
    final int completedTasks;
    final List<StressTestCommand.TaskResult> taskResults;
    final String timeoutDiag;

    StageResult(
      int taskCount,
      long durationMs,
      boolean timedOut,
      double msptBefore,
      double msptAfter,
      long heapUsedMB,
      long heapTotalMB,
      int completedTasks,
      List<StressTestCommand.TaskResult> taskResults,
      String timeoutDiag
    ) {
      this.taskCount = taskCount;
      this.durationMs = durationMs;
      this.timedOut = timedOut;
      this.msptBefore = msptBefore;
      this.msptAfter = msptAfter;
      this.heapUsedMB = heapUsedMB;
      this.heapTotalMB = heapTotalMB;
      this.completedTasks = completedTasks;
      this.taskResults = taskResults;
      this.timeoutDiag = timeoutDiag;
    }
  }

  private static class TaskResult {
    final String label;
    final BlockPos center;
    final int totalChunks;
    final long durationMs;

    TaskResult(String label, BlockPos center, int totalChunks, long durationMs) {
      this.label = label;
      this.center = center;
      this.totalChunks = totalChunks;
      this.durationMs = durationMs;
    }
  }
}
