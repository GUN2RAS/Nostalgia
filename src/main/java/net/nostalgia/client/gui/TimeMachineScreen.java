package net.nostalgia.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.nostalgia.alphalogic.bridge.AlphaEngineManager;
import net.nostalgia.alphalogic.ritual.DimensionUtil;
import net.nostalgia.client.events.caches.impl.AlphaByteCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramRegistry;
import net.nostalgia.client.events.caches.providers.HologramDiskCache;
import net.nostalgia.client.events.caches.providers.HologramSection;
import net.nostalgia.client.events.echo.RitualVisualManager;
import net.nostalgia.client.gui.hologram3d.HologramMiniRenderer;
import net.nostalgia.client.gui.hologram3d.HologramTerrainData;
import net.nostalgia.inventory.TimeMachineMenu;
import net.nostalgia.item.ModItems;
import net.nostalgia.mixin.alpha.AbstractContainerScreenAccessor;
import net.nostalgia.network.C2STerminalCacheRequestPayload;
import net.nostalgia.network.C2STravelRequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeMachineScreen extends AbstractContainerScreen<TimeMachineMenu> {
  private static final Logger LOGGER = LoggerFactory.getLogger("NostalgiaCache");
  public static boolean nextScreenIsError = false;
  public static String lastErrorCode = null;
  public static volatile boolean terminalCacheArrived = false;
  private boolean isUnstableError = false;
  private int overloadFrames = 0;
  private boolean isOverloading = false;
  private final List<String> generations = new ArrayList<>();
  private final List<String> genNames = new ArrayList<>();
  private int selectedGenIndex = 0;
  private final List<List<String>> versions = new ArrayList<>();
  private final List<List<String>> versionNames = new ArrayList<>();
  private final List<List<Identifier>> versionIcons = new ArrayList<>();
  private int selectedVersionIndex = 0;
  private PlasmaWireRenderer.WirePath[] wires;
  private float flowProgress = 0.0F;
  private int currentEnergyColor = 54998;
  private float connectionPulse = 0.0F;
  private boolean wasCircuitClosed = false;
  private boolean wasFuelPresent = false;
  private float prevFlowProgress = 0.0F;
  private final List<PlasmaWireRenderer.Spark> sparks = new ArrayList<>();
  private final char[] genCurrentChars = new char[5];
  private final char[] genTargetChars = new char[5];
  private final float[] genLampBrightnesses = new float[5];
  private final float[] genPrevLampBrightnesses = new float[5];
  private final char[] verCurrentChars = new char[8];
  private final char[] verTargetChars = new char[8];
  private final float[] verLampBrightnesses = new float[8];
  private final float[] verPrevLampBrightnesses = new float[8];
  private boolean isFirstSync = true;
  private int screenTickCount = 0;
  private long lastManualTickTime = 0L;
  private long openTime = 0L;
  private TimeMachineScreen.MonitorState monitorState = TimeMachineScreen.MonitorState.BOOTING;
  private static HologramMiniRenderer hologramRenderer;
  private boolean hologramDataLoaded = false;
  private int lastLoadedSectionsCount = -1;
  private static HologramTerrainData currentTerrainData;
  private static String currentDimId;
  private float terminalAlpha = 1.0F;
  private float glitchAlpha = 0.0F;
  private float contentFadeAlpha = 1.0F;
  private int signalAcquiredTimer = 0;
  private int lastSelectedGenIndex = -1;
  private int lastSelectedVerIndex = -1;
  private boolean lastFuelWasEcho = false;
  private boolean lastFuelWasAmethyst = false;
  private float noSignalAlpha = 0.0F;
  private float signalAcquiredAlpha = 0.0F;
  private float bootTextAlpha = 1.0F;

  public TimeMachineScreen(TimeMachineMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    ((AbstractContainerScreenAccessor)this).setImageHeight(176);
    this.inventoryLabelY = 1000;
    this.titleLabelX = 8;
    this.titleLabelY = 3;
  }

  protected void init() {
    super.init();
    terminalCacheArrived = false;
    this.lastLoadedSectionsCount = -1;
    this.generations.clear();
    this.generations.addAll(List.of("alpha", "home", "special"));
    this.genNames.clear();
    this.genNames.addAll(List.of("ALPHA", "HOME", "RD"));
    this.versions.clear();
    this.versions.add(new ArrayList<>(List.of("alpha")));
    this.versions.add(new ArrayList<>(List.of("overworld")));
    this.versions.add(new ArrayList<>(List.of("rd")));
    this.versionNames.clear();
    this.versionNames.add(new ArrayList<>(List.of("1.1.2_01")));
    this.versionNames.add(new ArrayList<>(List.of("VANILLA")));
    this.versionNames.add(new ArrayList<>(List.of("132211")));
    this.versionIcons.clear();
    this.versionIcons.add(new ArrayList<>(List.of(TimeMachineLayout.ALPHA_NODE)));
    this.versionIcons.add(new ArrayList<>(List.of(TimeMachineLayout.OVERWORLD_NODE)));
    this.versionIcons.add(new ArrayList<>(List.of(TimeMachineLayout.RD_NODE)));
    this.preloadServerDimensionCaches();
    String currentDimStr = this.minecraft.level.dimension().identifier().toString();
    String currentDimId = "unknown";
    if (currentDimStr.contains("alpha")) {
      currentDimId = "alpha";
    } else if (currentDimStr.contains("rd")) {
      currentDimId = "rd";
    } else if (currentDimStr.contains("overworld")) {
      currentDimId = "overworld";
    }

    for (int i = this.versions.size() - 1; i >= 0; i--) {
      List<String> vers = this.versions.get(i);
      int idx = vers.indexOf(currentDimId);
      if (idx != -1) {
        vers.remove(idx);
        this.versionNames.get(i).remove(idx);
        this.versionIcons.get(i).remove(idx);
      }

      if (vers.isEmpty()) {
        this.generations.remove(i);
        this.genNames.remove(i);
        this.versions.remove(i);
        this.versionNames.remove(i);
        this.versionIcons.remove(i);
      }
    }

    if (this.generations.isEmpty()) {
      this.generations.add("alpha");
      this.genNames.add("ALPHA");
      this.versions.add(new ArrayList<>(List.of("alpha")));
      this.versionNames.add(new ArrayList<>(List.of("1.1.2_01")));
      this.versionIcons.add(new ArrayList<>(List.of(TimeMachineLayout.ALPHA_NODE)));
    }

    this.selectedGenIndex = 0;
    this.selectedVersionIndex = 0;

    for (int i = 0; i < 5; i++) {
      this.genCurrentChars[i] = ' ';
      this.genTargetChars[i] = ' ';
      this.genLampBrightnesses[i] = 0.0F;
      this.genPrevLampBrightnesses[i] = 0.0F;
    }

    for (int i = 0; i < 8; i++) {
      this.verCurrentChars[i] = ' ';
      this.verTargetChars[i] = ' ';
      this.verLampBrightnesses[i] = 0.0F;
      this.verPrevLampBrightnesses[i] = 0.0F;
    }

    if (nextScreenIsError) {
      this.isUnstableError = true;
      nextScreenIsError = false;
    }

    ItemStack fuel = ((Slot)((TimeMachineMenu)this.menu).slots.get(0)).getItem();
    boolean hasEcho = fuel.is(Items.ECHO_SHARD);
    boolean hasAmethyst = fuel.is(Items.AMETHYST_SHARD) || fuel.is(ModItems.CHARGED_AMETHYST);
    float initialEnergy = ((TimeMachineMenu)this.menu).getEnergy() / 100.0F;
    this.flowProgress = initialEnergy;
    this.prevFlowProgress = initialEnergy;
    List<PlasmaWireRenderer.WireSegment> w1 = List.of(
      new PlasmaWireRenderer.WireSegment(28, 100, 28, 121), new PlasmaWireRenderer.WireSegment(28, 121, 77, 121)
    );
    List<PlasmaWireRenderer.WireSegment> w2 = List.of(
      new PlasmaWireRenderer.WireSegment(56, 100, 56, 115), new PlasmaWireRenderer.WireSegment(56, 115, 77, 115)
    );
    List<PlasmaWireRenderer.WireSegment> w3 = List.of(new PlasmaWireRenderer.WireSegment(88, 100, 88, 115));
    List<PlasmaWireRenderer.WireSegment> w4 = List.of(
      new PlasmaWireRenderer.WireSegment(121, 100, 121, 115), new PlasmaWireRenderer.WireSegment(121, 115, 99, 115)
    );
    List<PlasmaWireRenderer.WireSegment> w5 = List.of(
      new PlasmaWireRenderer.WireSegment(154, 100, 154, 121), new PlasmaWireRenderer.WireSegment(154, 121, 99, 121)
    );
    this.wires = new PlasmaWireRenderer.WirePath[]{
      new PlasmaWireRenderer.WirePath(2, w1),
      new PlasmaWireRenderer.WirePath(1, w2),
      new PlasmaWireRenderer.WirePath(2, w3),
      new PlasmaWireRenderer.WirePath(1, w4),
      new PlasmaWireRenderer.WirePath(2, w5)
    };
    if (((TimeMachineMenu)this.menu).getWasBooted() == 1) {
      this.openTime = System.currentTimeMillis() - 950L;
    } else {
      this.openTime = System.currentTimeMillis();
    }

    this.glitchAlpha = 0.0F;
    this.signalAcquiredTimer = 0;
    boolean isFuelPresent = hasEcho || hasAmethyst;
    if (isFuelPresent) {
      this.monitorState = TimeMachineScreen.MonitorState.MAP_ACTIVE;
      this.terminalAlpha = 0.0F;
      this.bootTextAlpha = 0.0F;
      this.noSignalAlpha = 0.0F;
      this.signalAcquiredAlpha = 0.0F;
      this.wasFuelPresent = true;
      this.flowProgress = 1.0F;
      this.prevFlowProgress = 1.0F;
      this.wasCircuitClosed = true;
      this.currentEnergyColor = hasEcho ? '\ud6d6' : 13395711;
      this.lastSelectedGenIndex = this.selectedGenIndex;
      this.lastSelectedVerIndex = this.selectedVersionIndex;
      this.lastFuelWasEcho = hasEcho;
      this.lastFuelWasAmethyst = hasAmethyst;
      this.hologramDataLoaded = false;
      this.initHologramRenderer(hasEcho, false);
    } else {
      this.monitorState = TimeMachineScreen.MonitorState.BOOTING;
      this.terminalAlpha = 1.0F;
      this.bootTextAlpha = 1.0F;
      this.noSignalAlpha = 0.0F;
      this.signalAcquiredAlpha = 0.0F;
    }
  }

  protected void containerTick() {
    super.containerTick();
    this.lastManualTickTime = System.currentTimeMillis();
    this.doTickLogic();
  }

  private void doTickLogic() {
    if (this.glitchAlpha > 0.0F) {
      if (this.contentFadeAlpha >= 0.4F) {
        this.glitchAlpha = Math.max(0.0F, this.glitchAlpha - 0.17F);
      } else {
        this.glitchAlpha = Math.max(0.0F, this.glitchAlpha - 0.03F);
      }
    }
    if (this.contentFadeAlpha < 1.0F && this.hologramDataLoaded) {
      this.contentFadeAlpha = Math.min(1.0F, this.contentFadeAlpha + 0.1F);
    }

    if (this.signalAcquiredTimer > 0) {
      this.signalAcquiredTimer--;
    }

    switch (this.monitorState) {
      case BOOTING:
        this.bootTextAlpha = Math.min(1.0F, this.bootTextAlpha + 0.15F);
        this.noSignalAlpha = Math.max(0.0F, this.noSignalAlpha - 0.15F);
        this.signalAcquiredAlpha = Math.max(0.0F, this.signalAcquiredAlpha - 0.15F);
        this.terminalAlpha = Math.min(1.0F, this.terminalAlpha + 0.08F);
        if (((TimeMachineMenu)this.menu).getWasBooted() == 1 || System.currentTimeMillis() - this.openTime >= 950L) {
          this.monitorState = TimeMachineScreen.MonitorState.ALPHA_PROTOCOL;
        }
        break;
      case ALPHA_PROTOCOL:
        this.bootTextAlpha = Math.min(1.0F, this.bootTextAlpha + 0.15F);
        this.noSignalAlpha = Math.max(0.0F, this.noSignalAlpha - 0.15F);
        this.signalAcquiredAlpha = Math.max(0.0F, this.signalAcquiredAlpha - 0.15F);
        this.terminalAlpha = Math.min(1.0F, this.terminalAlpha + 0.08F);
        break;
      case NO_SIGNAL:
        this.noSignalAlpha = Math.min(1.0F, this.noSignalAlpha + 0.15F);
        this.bootTextAlpha = Math.max(0.0F, this.bootTextAlpha - 0.15F);
        this.signalAcquiredAlpha = Math.max(0.0F, this.signalAcquiredAlpha - 0.15F);
        this.terminalAlpha = Math.min(1.0F, this.terminalAlpha + 0.08F);
        if (this.glitchAlpha <= 0.0F) {
          this.monitorState = TimeMachineScreen.MonitorState.ALPHA_PROTOCOL;
        }
        break;
      case SIGNAL_ACQUIRED:
        this.signalAcquiredAlpha = Math.min(1.0F, this.signalAcquiredAlpha + 0.15F);
        this.noSignalAlpha = Math.max(0.0F, this.noSignalAlpha - 0.15F);
        this.bootTextAlpha = Math.max(0.0F, this.bootTextAlpha - 0.15F);
        this.terminalAlpha = Math.min(1.0F, this.terminalAlpha + 0.08F);
        if (this.signalAcquiredTimer <= 0 && this.hologramDataLoaded) {
          this.monitorState = TimeMachineScreen.MonitorState.MAP_ACTIVE;
        }
        break;
      case MAP_ACTIVE:
        this.bootTextAlpha = Math.max(0.0F, this.bootTextAlpha - 0.15F);
        this.noSignalAlpha = Math.max(0.0F, this.noSignalAlpha - 0.15F);
        this.signalAcquiredAlpha = Math.max(0.0F, this.signalAcquiredAlpha - 0.15F);
        this.terminalAlpha = Math.max(0.0F, this.terminalAlpha - 0.08F);
    }

    if (((TimeMachineMenu)this.menu).getWasBooted() == 0 && System.currentTimeMillis() - this.openTime >= 950L) {
      ((TimeMachineMenu)this.menu).setWasBooted(1);
    }

    this.screenTickCount++;
    this.prevFlowProgress = this.flowProgress;
    this.sparks.removeIf(s -> {
      s.x = s.x + s.vx;
      s.y = s.y + s.vy;
      s.age++;
      return s.age >= s.maxAge;
    });
    System.arraycopy(this.genLampBrightnesses, 0, this.genPrevLampBrightnesses, 0, 5);
    System.arraycopy(this.verLampBrightnesses, 0, this.verPrevLampBrightnesses, 0, 8);
    long now = System.currentTimeMillis();
    String genStr;
    String verStr;
    if (this.isUnstableError) {
      genStr = TimeMachineErrorHandler.getLampGenString(lastErrorCode);
      verStr = TimeMachineErrorHandler.getLampVerString(lastErrorCode);
    } else {
      genStr = this.genNames.get(this.selectedGenIndex);
      verStr = this.versionNames.get(this.selectedGenIndex).get(this.selectedVersionIndex);
    }

    for (int i = 0; i < 5; i++) {
      this.genTargetChars[i] = i < genStr.length() ? genStr.charAt(i) : 32;
    }

    for (int i = 0; i < 8; i++) {
      this.verTargetChars[i] = i < verStr.length() ? verStr.charAt(i) : 32;
    }

    for (int i = 0; i < 5; i++) {
      if (this.genCurrentChars[i] == this.genTargetChars[i]) {
        this.genLampBrightnesses[i] = Math.min(1.0F, this.genLampBrightnesses[i] + 0.15F);
      } else {
        this.genLampBrightnesses[i] = Math.max(0.0F, this.genLampBrightnesses[i] - 0.2F);
        if (this.genLampBrightnesses[i] <= 0.0F) {
          this.genCurrentChars[i] = this.genTargetChars[i];
        }
      }
    }

    for (int ix = 0; ix < 8; ix++) {
      if (this.verCurrentChars[ix] == this.verTargetChars[ix]) {
        this.verLampBrightnesses[ix] = Math.min(1.0F, this.verLampBrightnesses[ix] + 0.15F);
      } else {
        this.verLampBrightnesses[ix] = Math.max(0.0F, this.verLampBrightnesses[ix] - 0.2F);
        if (this.verLampBrightnesses[ix] <= 0.0F) {
          this.verCurrentChars[ix] = this.verTargetChars[ix];
        }
      }
    }

    ItemStack fuel = ((Slot)((TimeMachineMenu)this.menu).slots.get(0)).getItem();
    boolean hasEcho = fuel.is(Items.ECHO_SHARD);
    boolean hasAmethyst = fuel.is(Items.AMETHYST_SHARD) || fuel.is(ModItems.CHARGED_AMETHYST);
    boolean isFuelPresent = hasEcho || hasAmethyst;
    float targetProgress = isFuelPresent ? 1.0F : 0.0F;
    if (this.isUnstableError && !"UNSTABLE".equals(lastErrorCode) && !hasAmethyst) {
      this.isUnstableError = false;
      nextScreenIsError = false;
      lastErrorCode = null;
    }

    if (this.isFirstSync && ((TimeMachineMenu)this.menu).isDataSynced()) {
      this.flowProgress = targetProgress;
      this.prevFlowProgress = targetProgress;
      this.wasCircuitClosed = targetProgress >= 1.0F;
      this.wasFuelPresent = isFuelPresent;
      if (isFuelPresent) {
        this.currentEnergyColor = hasEcho ? '\ud6d6' : 13395711;
        this.terminalAlpha = 0.0F;
        this.bootTextAlpha = 0.0F;
        this.noSignalAlpha = 0.0F;
        this.signalAcquiredAlpha = 0.0F;
        this.monitorState = TimeMachineScreen.MonitorState.MAP_ACTIVE;
        if (this.hologramRenderer == null) {
          this.hologramDataLoaded = false;
          this.initHologramRenderer(hasEcho, true);
        }
      } else if (((TimeMachineMenu)this.menu).getWasBooted() == 1) {
        this.monitorState = TimeMachineScreen.MonitorState.ALPHA_PROTOCOL;
        this.terminalAlpha = 1.0F;
        this.bootTextAlpha = 1.0F;
        this.noSignalAlpha = 0.0F;
        this.signalAcquiredAlpha = 0.0F;
      } else {
        this.monitorState = TimeMachineScreen.MonitorState.BOOTING;
      }

      this.isFirstSync = false;
    }

    if (isFuelPresent && !this.wasFuelPresent) {
      this.wasFuelPresent = true;
      this.glitchAlpha = 0.0F;
      if (this.monitorState == TimeMachineScreen.MonitorState.NO_SIGNAL) {
        this.signalAcquiredTimer = 15;
        this.monitorState = TimeMachineScreen.MonitorState.SIGNAL_ACQUIRED;
        this.bootTextAlpha = 0.0F;
        this.noSignalAlpha = 0.0F;
      } else {
        this.signalAcquiredTimer = 0;
        this.monitorState = TimeMachineScreen.MonitorState.MAP_ACTIVE;
        this.bootTextAlpha = 0.0F;
        this.noSignalAlpha = 0.0F;
        this.signalAcquiredAlpha = 0.0F;
      }

      if (this.minecraft != null && this.minecraft.player != null) {
        if (hasEcho) {
          this.minecraft.player.playSound(SoundEvents.SCULK_CATALYST_BLOOM, 0.8F, 1.3F);
          this.minecraft.player.playSound(SoundEvents.BEACON_ACTIVATE, 0.5F, 1.5F);
        } else {
          this.minecraft.player.playSound(SoundEvents.AMETHYST_BLOCK_PLACE, 1.0F, 1.2F);
          this.minecraft.player.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 0.8F, 1.1F);
        }
      }
    } else if (!isFuelPresent && this.wasFuelPresent) {
      this.wasFuelPresent = false;
      this.glitchAlpha = 1.0F;
      this.monitorState = TimeMachineScreen.MonitorState.NO_SIGNAL;
      this.bootTextAlpha = 0.0F;
      this.signalAcquiredAlpha = 0.0F;
      this.hologramRenderer = null;
      this.hologramDataLoaded = false;
      if (this.minecraft != null && this.minecraft.player != null) {
        this.minecraft.player.playSound(SoundEvents.REDSTONE_TORCH_BURNOUT, 0.4F, 1.1F);
        this.minecraft.player.playSound(SoundEvents.BEACON_DEACTIVATE, 0.4F, 1.5F);
      }
    } else if (!isFuelPresent) {
      this.wasFuelPresent = false;
    }

    if (isFuelPresent
      && (
        (this.hologramRenderer == null && !this.hologramDataLoaded)
          || this.selectedGenIndex != this.lastSelectedGenIndex
          || this.selectedVersionIndex != this.lastSelectedVerIndex
          || hasEcho != this.lastFuelWasEcho
          || hasAmethyst != this.lastFuelWasAmethyst
      )) {
      this.lastSelectedGenIndex = this.selectedGenIndex;
      this.lastSelectedVerIndex = this.selectedVersionIndex;
      this.lastFuelWasEcho = hasEcho;
      this.lastFuelWasAmethyst = hasAmethyst;
      this.hologramDataLoaded = false;
      this.lastLoadedSectionsCount = -1;
      this.currentTerrainData = null;
      this.currentDimId = null;
      this.glitchAlpha = 1.0F;
      this.contentFadeAlpha = 0.0F;

      this.initHologramRenderer(hasEcho, false);
    }

    if (terminalCacheArrived) {
      terminalCacheArrived = false;
    }

    if (isFuelPresent && this.hologramRenderer != null && this.screenTickCount % 5 == 0) {
      String targetDim = this.versions.get(this.selectedGenIndex).get(this.selectedVersionIndex);
      String dimId = DimensionUtil.normalize(targetDim);
      DimensionHologramCache cache = DimensionHologramRegistry.getByName(dimId);
      if (cache != null) {
        int currentCount = cache.getSections().size();
        if (currentCount != this.lastLoadedSectionsCount && currentCount > 0) {
          this.lastLoadedSectionsCount = currentCount;
          this.refreshTerrainData(dimId);
        }
      }
    }

    if (this.flowProgress < targetProgress) {
      this.flowProgress = Math.min(targetProgress, this.flowProgress + 0.015F);
    } else if (this.flowProgress > targetProgress) {
      this.flowProgress = Math.max(targetProgress, this.flowProgress - 0.025F);
    }

    if (this.flowProgress >= 1.0F && !this.wasCircuitClosed) {
      this.wasCircuitClosed = true;
      this.connectionPulse = 1.0F;
      if (this.minecraft != null && this.minecraft.player != null) {
        if (hasEcho) {
          this.minecraft.player.playSound(SoundEvents.BEACON_POWER_SELECT, 1.0F, 1.3F);
          this.minecraft.player.playSound(SoundEvents.SCULK_BLOCK_CHARGE, 1.0F, 1.2F);
        } else {
          this.minecraft.player.playSound(SoundEvents.BEACON_POWER_SELECT, 1.0F, 1.3F);
          this.minecraft.player.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 1.0F, 1.1F);
        }
      }
    } else if (this.flowProgress < 1.0F) {
      this.wasCircuitClosed = false;
    }

    if (hasEcho) {
      this.currentEnergyColor = 54998;
    } else if (hasAmethyst) {
      this.currentEnergyColor = 13395711;
    }

    if (this.flowProgress > 0.0F && this.minecraft != null && this.minecraft.player != null) {
      if (this.screenTickCount % 40 == 0) {
        this.minecraft.player.playSound(SoundEvents.CONDUIT_AMBIENT, 0.15F * this.flowProgress, 1.2F);
      }

      if (Math.random() < 0.08 * this.flowProgress) {
        this.minecraft.player.playSound(SoundEvents.CAMPFIRE_CRACKLE, 0.12F * this.flowProgress, 1.6F + (float)Math.random() * 0.4F);
      }
    }

    if (isFuelPresent && this.flowProgress > 0.0F && Math.random() < 0.25F * this.flowProgress) {
      PlasmaWireRenderer.Spark spark = new PlasmaWireRenderer.Spark();
      spark.x = 89.0F + (float)(Math.random() - 0.5) * 12.0F;
      spark.y = 125.0F + (float)(Math.random() - 0.5) * 12.0F;
      spark.vx = (float)(Math.random() - 0.5) * 0.6F;
      spark.vy = -0.4F - (float)Math.random() * 0.8F;
      spark.maxAge = 15 + (int)(Math.random() * 15.0);
      spark.age = 0;
      spark.scale = 0.6F + (float)Math.random() * 0.8F;
      this.sparks.add(spark);
    }

    if (this.isOverloading) {
      this.overloadFrames++;
      if (this.minecraft != null && this.minecraft.player != null) {
        if (this.overloadFrames == 1) {
          this.minecraft.player.playSound(SoundEvents.BEACON_ACTIVATE, 1.0F, 0.5F);
          this.minecraft.player.playSound(SoundEvents.CONDUIT_ACTIVATE, 1.0F, 0.1F);
        }

        if (this.overloadFrames == 15) {
          this.minecraft.player.playSound(SoundEvents.AMETHYST_BLOCK_RESONATE, 1.0F, 0.5F);
        }

        if (this.overloadFrames == 25) {
          this.minecraft.player.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, 1.0F, 0.6F);
        }

        if (this.overloadFrames > 5 && Math.random() < 0.2) {
          this.minecraft.player.playSound(SoundEvents.GLASS_BREAK, 0.2F, (float)(1.2 + Math.random()));
        }

        if (this.overloadFrames > 40) {
          this.minecraft.player.playSound(SoundEvents.WARDEN_SONIC_BOOM, 2.0F, 0.5F);
          this.minecraft.player.playSound(SoundEvents.PORTAL_TRAVEL, 0.4F, 1.2F);
          String target = this.versions.get(this.selectedGenIndex).get(this.selectedVersionIndex);
          BlockPos landing = this.hologramRenderer != null ? this.hologramRenderer.getSelectedLanding() : null;
          ClientPlayNetworking.send(new C2STravelRequestPayload(target, landing));
          this.minecraft.player.closeContainer();
        }
      }
    }
  }

  public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
    long now = System.currentTimeMillis();
    if (this.lastManualTickTime == 0L) {
      this.lastManualTickTime = now;
    }

    while (now - this.lastManualTickTime >= 50L) {
      this.lastManualTickTime += 50L;
      this.doTickLogic();
    }

    int x = (this.width - this.imageWidth) / 2;
    int y = (this.height - this.imageHeight) / 2;
    int cx = x + 80;
    int cy = y + 116;
    graphics.fill(cx - 8, cy - 8, cx + 24, cy + 24, -10793984);
    graphics.fill(cx - 7, cy - 7, cx + 23, cy + 23, -2838729);
    graphics.fill(cx - 6, cy - 6, cx + 22, cy + 22, -4680154);
    graphics.fill(cx - 5, cy - 5, cx + 21, cy + 21, -12770540);
    graphics.fill(cx - 3, cy - 3, cx + 19, cy + 19, -16120560);
    graphics.fill(cx - 2, cy - 2, cx + 18, cy + 18, -15463137);
    graphics.outline(cx - 3, cy - 3, 22, 22, this.isOverloading ? 0xFF000000 | this.currentEnergyColor : -11913108);
    super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    this.extractTooltip(graphics, mouseX, mouseY);
  }

  private void renderChassis(GuiGraphicsExtractor graphics, int x, int y) {
    graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, -12770540);
    Random woodRand = new Random(1337L);

    for (int i = 0; i < 15; i++) {
      int wy = y + woodRand.nextInt(this.imageHeight);
      int wh = woodRand.nextInt(6) + 2;
      int wcol = woodRand.nextBoolean() ? 308034081 : 204216075;
      graphics.fill(x, wy, x + this.imageWidth, wy + wh, wcol);
    }

    graphics.fill(x, y, x + this.imageWidth, y + 4, 1426063360);
    graphics.fill(x, y + this.imageHeight - 4, x + this.imageWidth, y + this.imageHeight, 1426063360);
    graphics.fill(x, y, x + 4, y + this.imageHeight, 1426063360);
    graphics.fill(x + this.imageWidth - 4, y, x + this.imageWidth, y + this.imageHeight, 1426063360);
    int frameL = x - 2;
    int frameT = y - 2;
    int frameR = x + this.imageWidth + 2;
    int frameB = y + this.imageHeight + 2;
    graphics.fill(frameL, frameT, frameR - 1, frameT + 1, -1840);
    graphics.fill(frameR - 1, frameT, frameR, frameT + 1, -10793984);
    graphics.fill(frameL, frameT + 1, frameL + 1, frameT + 2, -1840);
    graphics.fill(frameL + 1, frameT + 1, frameR - 1, frameT + 2, -2838729);
    graphics.fill(frameR - 1, frameT + 1, frameR, frameT + 2, -10793984);
    graphics.fill(frameL, frameT + 2, frameL + 1, frameT + 3, -1840);
    graphics.fill(frameL + 1, frameT + 2, frameR - 2, frameT + 3, -2838729);
    graphics.fill(frameR - 2, frameT + 2, frameR - 1, frameT + 3, -6389475);
    graphics.fill(frameR - 1, frameT + 2, frameR, frameT + 3, -10793984);
    graphics.fill(frameL, frameT + 3, frameL + 1, frameT + 4, -1840);
    graphics.fill(frameL + 1, frameT + 3, frameL + 2, frameT + 4, -4680154);
    graphics.fill(frameL + 2, frameT + 3, frameR - 2, frameT + 4, -2838729);
    graphics.fill(frameR - 2, frameT + 3, frameR - 1, frameT + 4, -6389475);
    graphics.fill(frameR - 1, frameT + 3, frameR, frameT + 4, -10793984);
    graphics.fill(frameL, frameT + 4, frameL + 1, frameB - 4, -1840);
    graphics.fill(frameL + 1, frameT + 4, frameL + 4, frameB - 4, -4680154);
    graphics.fill(frameR - 4, frameT + 4, frameR - 1, frameB - 4, -6389475);
    graphics.fill(frameR - 1, frameT + 4, frameR, frameB - 4, -10793984);
    graphics.fill(frameL, frameB - 4, frameL + 1, frameB - 3, -1840);
    graphics.fill(frameL + 1, frameB - 4, frameL + 2, frameB - 3, -4680154);
    graphics.fill(frameL + 2, frameB - 4, frameR - 2, frameB - 3, -7637760);
    graphics.fill(frameR - 2, frameB - 4, frameR - 1, frameB - 3, -6389475);
    graphics.fill(frameR - 1, frameB - 4, frameR, frameB - 3, -10793984);
    graphics.fill(frameL, frameB - 3, frameL + 1, frameB - 2, -1840);
    graphics.fill(frameL + 1, frameB - 3, frameR - 2, frameB - 2, -7637760);
    graphics.fill(frameR - 2, frameB - 3, frameR - 1, frameB - 2, -6389475);
    graphics.fill(frameR - 1, frameB - 3, frameR, frameB - 2, -10793984);
    graphics.fill(frameL, frameB - 2, frameL + 1, frameB - 1, -1840);
    graphics.fill(frameL + 1, frameB - 2, frameR - 1, frameB - 1, -7637760);
    graphics.fill(frameR - 1, frameB - 2, frameR, frameB - 1, -10793984);
    graphics.fill(frameL, frameB - 1, frameR, frameB, -10793984);
    int[][] rivets = new int[][]{{frameL + 6, frameT + 6}, {frameR - 7, frameT + 6}, {frameL + 6, frameB - 7}, {frameR - 7, frameB - 7}};

    for (int[] r : rivets) {
      graphics.fill(r[0] - 1, r[1] - 1, r[0] + 2, r[1] + 2, -11912192);
      graphics.fill(r[0], r[1], r[0] + 1, r[1] + 1, -3825624);
      graphics.fill(r[0], r[1], r[0], r[1], -3936);
    }
  }

  private void renderWiresAndPlasma(GuiGraphicsExtractor graphics, int x, int y, float gameTime, float renderedProgress, float overloadProgress) {
    if (this.wires != null) {
      for (PlasmaWireRenderer.WirePath w : this.wires) {
        PlasmaWireRenderer.drawWireBase(graphics, w, x, y);
      }
    }

    if (renderedProgress > 0.0F) {
      float plasmaSpeed = this.isOverloading ? 0.003F + overloadProgress * 0.012F : 0.0022F;
      if (this.wires != null) {
        for (int i = 0; i < this.wires.length; i++) {
          PlasmaWireRenderer.WirePath w = this.wires[i];
          float wireProgress;
          if (i == 0 || i == 4) {
            wireProgress = this.smoothStep(0.0F, 0.5F, renderedProgress);
          } else if (i != 1 && i != 3) {
            wireProgress = this.smoothStep(0.6F, 1.0F, renderedProgress);
          } else {
            wireProgress = this.smoothStep(0.3F, 0.8F, renderedProgress);
          }

          if (wireProgress > 0.0F) {
            PlasmaWireRenderer.drawPlasmaWire(
              graphics, w, x, y, gameTime, plasmaSpeed, this.currentEnergyColor, wireProgress, overloadProgress, this.isOverloading
            );
          }
        }
      }
    }

    if (this.isOverloading && this.wires != null) {
      if (overloadProgress > 0.3F) {
        for (PlasmaWireRenderer.WirePath wx : this.wires) {
          PlasmaWireRenderer.drawCableLightning(graphics, wx, x, y, gameTime, this.currentEnergyColor);
        }
      }

      int flashAlpha = (int)(Math.sin(this.overloadFrames * 0.4F) * 20.0 + 20.0);
      graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, flashAlpha << 24 | this.currentEnergyColor);
    }
  }

  private void renderFuelSlot(GuiGraphicsExtractor graphics, int x, int y, float gameTime, float renderedProgress, float overloadProgress) {
    int sx = x + 80;
    int sy = y + 116;
    int[][] rivets = new int[][]{{sx - 7, sy - 7}, {sx + 22, sy - 7}, {sx - 7, sy + 22}, {sx + 22, sy + 22}};

    for (int[] r : rivets) {
      graphics.fill(r[0], r[1], r[0] + 2, r[1] + 2, -11912192);
      graphics.fill(r[0], r[1], r[0] + 1, r[1] + 1, -3936);
    }

    graphics.fill(sx + 5, sy - 8, sx + 11, sy - 6, -1840);
    graphics.fill(sx + 5, sy + 22, sx + 11, sy + 24, -7637760);
    graphics.fill(sx - 8, sy + 5, sx - 6, sy + 11, -1840);
    graphics.fill(sx + 22, sy + 5, sx + 24, sy + 11, -7637760);
    if (renderedProgress > 0.0F) {
      float pulse = 0.5F + 0.5F * (float)Math.sin(gameTime * 0.005F);
      float radius = 8.0F + pulse * 4.0F;
      int alphaGlow = (int)(renderedProgress * (40.0F + pulse * 20.0F));

      for (int r = 1; r <= (int)radius; r++) {
        int a = (int)(alphaGlow * (1.0F - r / radius));
        int col = a << 24 | this.currentEnergyColor & 16777215;
        graphics.outline(sx + 8 - r, sy + 8 - r, r * 2, r * 2, col);
      }

      PlasmaWireRenderer.drawSlotLightning(graphics, x, y, gameTime, this.currentEnergyColor, overloadProgress);
    }

    if (!this.sparks.isEmpty()) {
      for (PlasmaWireRenderer.Spark s : this.sparks) {
        float lifeRatio = 1.0F - (float)s.age / s.maxAge;
        int alpha = (int)(lifeRatio * 255.0F * renderedProgress);
        int color = alpha << 24 | this.currentEnergyColor & 16777215;
        int px = x + (int)s.x;
        int py = y + (int)s.y;
        int size = Math.max(1, (int)(s.scale * lifeRatio * 2.0F));
        graphics.fill(px - size / 2, py - size / 2, px - size / 2 + size, py - size / 2 + size, color);
      }
    }

    if (this.connectionPulse > 0.0F) {
      int rCol = this.currentEnergyColor >> 16 & 0xFF;
      int gCol = this.currentEnergyColor >> 8 & 0xFF;
      int bCol = this.currentEnergyColor & 0xFF;
      float waveSize = (1.0F - this.connectionPulse) * 35.0F;
      int alphaWave = (int)(this.connectionPulse * 200.0F);
      int colWave = alphaWave << 24 | rCol << 16 | gCol << 8 | bCol;
      int waveX = x + 88 - 11 - (int)waveSize;
      int waveY = y + 126 - 11 - (int)waveSize;
      int waveW = 22 + (int)waveSize * 2;
      int waveH = 22 + (int)waveSize * 2;
      graphics.outline(waveX, waveY, waveW, waveH, colWave);
      graphics.outline(waveX - 1, waveY - 1, waveW + 2, waveH + 2, colWave);
      int flashAlpha = (int)(this.connectionPulse * 45.0F);
      int colFlash = flashAlpha << 24 | rCol << 16 | gCol << 8 | bCol;
      graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, colFlash);
    }
  }

  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
    int x = (this.width - this.imageWidth) / 2;
    int y = (this.height - this.imageHeight) / 2;
    float renderedProgress = this.prevFlowProgress + (this.flowProgress - this.prevFlowProgress) * partialTick;
    float gameTime = (this.screenTickCount + partialTick) * 50.0F;
    float overloadProgress = this.isOverloading ? this.overloadFrames / 40.0F : 0.0F;
    this.renderChassis(graphics, x, y);
    int panelTop = y + 142;
    graphics.fill(x, panelTop, x + this.imageWidth, y + this.imageHeight, -14347510);
    Random woodRand = new Random(54321L);

    for (int i = 0; i < 4; i++) {
      int wy = panelTop + 2 + woodRand.nextInt(this.imageHeight - 146);
      int wh = woodRand.nextInt(2) + 1;
      int wcol = woodRand.nextBoolean() ? 440410643 : 336529412;
      graphics.fill(x + 2, wy, x + this.imageWidth - 2, wy + wh, wcol);
    }

    graphics.outline(x, panelTop, this.imageWidth, this.imageHeight - 142, -10793984);
    int pL = x + 2;
    int pR = x + this.imageWidth - 2;
    int pT = panelTop + 2;
    int pB = y + this.imageHeight - 2;
    graphics.fill(pL, pT, pR, pT + 1, -1840);
    graphics.fill(pL, pT + 1, pL + 1, pB, -4680154);
    graphics.fill(pR - 1, pT + 1, pR, pB, -10793984);
    graphics.fill(pL + 1, pB - 1, pR - 1, pB, -10793984);
    graphics.outline(x + 7, y + 146, 162, 18, -14807032);

    for (int col = 0; col < 9; col++) {
      int slotX = x + 8 + col * 18;
      int slotY = y + 147;
      graphics.fill(slotX, slotY, slotX + 16, slotY + 16, -16054780);
      graphics.outline(slotX - 1, slotY - 1, 18, 18, -6389475);
      graphics.outline(slotX, slotY, 16, 16, -14807032);
    }

    int[][] panelRivets = new int[][]{{x + 5, y + 168}, {x + 171, y + 168}};

    for (int[] r : panelRivets) {
      graphics.fill(r[0], r[1], r[0] + 2, r[1] + 2, -11912192);
      graphics.fill(r[0], r[1], r[0] + 1, r[1] + 1, -3825624);
      graphics.fill(r[0], r[1], r[0], r[1], -3936);
    }

    Identifier currentIcon = this.versionIcons.get(this.selectedGenIndex).get(this.selectedVersionIndex);
    int monX = x + 8;
    int monY = y + 14;
    String selectedDim = this.versions.get(this.selectedGenIndex).get(this.selectedVersionIndex);
    Identifier previewTex = DimensionUtil.getPreviewTexture(selectedDim);
    boolean hasMap = this.hologramRenderer != null && (this.monitorState == TimeMachineScreen.MonitorState.MAP_ACTIVE || this.terminalAlpha < 1.0F);
    boolean hasPreview = previewTex != null && this.hologramDataLoaded && this.monitorState == TimeMachineScreen.MonitorState.MAP_ACTIVE;
    if (hasPreview && !hasMap) {
      CrtMonitorRenderer.renderBorderOnly(graphics, x, y, this.isOverloading, overloadProgress);
      graphics.blit(RenderPipelines.GUI_TEXTURED, previewTex, monX, monY, 0.0F, 0.0F, 160, 62, 256, 128);
      if (this.contentFadeAlpha < 1.0F) {
        int fadeBlack = ((int)(255.0F * (1.0F - this.contentFadeAlpha))) << 24;
        graphics.fill(monX, monY, monX + 160, monY + 62, fadeBlack);
      }
      if (this.glitchAlpha > 0.0F) {
        CrtMonitorRenderer.renderGlitchOverlay(graphics, monX, monY, gameTime, this.glitchAlpha);
      }
      if (this.isOverloading) {
        CrtMonitorRenderer.renderOverloadOverlay(graphics, x, y, gameTime, overloadProgress, this.currentEnergyColor);
      }
    } else if (hasMap) {
      if (this.terminalAlpha <= 0.0F) {
        CrtMonitorRenderer.renderBorderOnly(graphics, x, y, this.isOverloading, overloadProgress);
        this.hologramRenderer.render(graphics, monX, monY, 160, 62, partialTick);
        if (this.contentFadeAlpha < 1.0F) {
          int fadeBlack = ((int)(255.0F * (1.0F - this.contentFadeAlpha))) << 24;
          graphics.fill(monX, monY, monX + 160, monY + 62, fadeBlack);
        }
        if (this.glitchAlpha > 0.0F) {
          CrtMonitorRenderer.renderGlitchOverlay(graphics, monX, monY, gameTime, this.glitchAlpha);
        }
        if (this.isOverloading) {
          CrtMonitorRenderer.renderOverloadOverlay(graphics, x, y, gameTime, overloadProgress, this.currentEnergyColor);
        }
      } else {
        CrtMonitorRenderer.renderBorderOnly(graphics, x, y, this.isOverloading, overloadProgress);
        this.hologramRenderer.render(graphics, monX, monY, 160, 62, partialTick);
        CrtMonitorRenderer.render(
          graphics,
          this.font,
          x,
          y,
          gameTime,
          this.isOverloading,
          overloadProgress,
          this.currentEnergyColor,
          currentIcon,
          this.terminalAlpha,
          System.currentTimeMillis() - this.openTime,
          this.noSignalAlpha,
          this.signalAcquiredAlpha,
          this.bootTextAlpha,
          this.glitchAlpha
        );
      }
    } else {
      CrtMonitorRenderer.render(
        graphics,
        this.font,
        x,
        y,
        gameTime,
        this.isOverloading,
        overloadProgress,
        this.currentEnergyColor,
        currentIcon,
        this.terminalAlpha,
        System.currentTimeMillis() - this.openTime,
        this.noSignalAlpha,
        this.signalAcquiredAlpha,
        this.bootTextAlpha,
        this.glitchAlpha
      );
    }

    this.connectionPulse = Math.max(0.0F, this.connectionPulse - 0.03F);
    this.renderWiresAndPlasma(graphics, x, y, gameTime, renderedProgress, overloadProgress);
    this.renderFuelSlot(graphics, x, y, gameTime, renderedProgress, overloadProgress);
    int lampColor = this.isUnstableError ? 16711680 : this.currentEnergyColor;

    for (int i = 0; i < 5; i++) {
      int lx = x + 8 + i * 14;
      int ly = y + 78;
      boolean isHovered = this.isHoveringArea(x + 8, y + 78, 70, 22, mouseX, mouseY) && !this.isOverloading;
      float lBright = this.genPrevLampBrightnesses[i] + (this.genLampBrightnesses[i] - this.genPrevLampBrightnesses[i]) * partialTick;
      NixieLampRenderer.render(graphics, this.font, lx, ly, 12, 22, this.genCurrentChars[i], lBright, lampColor, isHovered);
    }

    for (int i = 0; i < 8; i++) {
      int lx = x + 83 + i * 11;
      int ly = y + 78;
      boolean isHovered = this.isHoveringArea(x + 83, y + 78, 87, 22, mouseX, mouseY) && !this.isOverloading;
      float lBright = this.verPrevLampBrightnesses[i] + (this.verLampBrightnesses[i] - this.verPrevLampBrightnesses[i]) * partialTick;
      NixieLampRenderer.render(graphics, this.font, lx, ly, 10, 22, this.verCurrentChars[i], lBright, lampColor, isHovered);
    }

    LeverRenderer.render(graphics, this.font, x, y, gameTime, renderedProgress, partialTick, this.isOverloading, this.overloadFrames, this.currentEnergyColor);
    int btnGoX = x + 125;
    int btnGoY = y + 118;
    boolean hoverGo = this.isHoveringArea(btnGoX, btnGoY, 18, 24, mouseX, mouseY) && !this.isOverloading;
    if (hoverGo) {
      graphics.setTooltipForNextFrame(this.font, Component.translatable("gui.nostalgia.time_machine.launch_tooltip"), mouseX, mouseY);
    }
  }

  protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
  }

  private boolean isHoveringArea(int x, int y, int width, int height, double mouseX, double mouseY) {
    return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
  }

  public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
    if (this.isUnstableError) {
      return super.mouseClicked(event, isDoubleClick);
    } else if (this.isOverloading) {
      return false;
    } else if (this.monitorState == TimeMachineScreen.MonitorState.MAP_ACTIVE
      && this.hologramRenderer != null
      && this.hologramRenderer.mouseClicked(event.x(), event.y(), event.button())) {
      return true;
    } else {
      int x = (this.width - this.imageWidth) / 2;
      int y = (this.height - this.imageHeight) / 2;
      if (this.isHoveringArea(x + 8, y + 78, 70, 22, event.x(), event.y()) && event.button() == 0) {
        this.selectedGenIndex = (this.selectedGenIndex + 1) % this.generations.size();
        this.selectedVersionIndex = 0;
        if (this.minecraft != null && this.minecraft.player != null) {
          this.minecraft.player.playSound((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), 0.5F, 1.0F);
        }

        return true;
      } else if (this.isHoveringArea(x + 83, y + 78, 87, 22, event.x(), event.y()) && event.button() == 0) {
        List<String> curVersions = this.versions.get(this.selectedGenIndex);
        this.selectedVersionIndex = (this.selectedVersionIndex + 1) % curVersions.size();
        if (this.minecraft != null && this.minecraft.player != null) {
          this.minecraft.player.playSound((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), 0.5F, 1.0F);
        }

        return true;
      } else {
        int btnGoX = x + 125;
        int btnGoY = y + 118;
        if (this.isHoveringArea(btnGoX, btnGoY, 18, 24, event.x(), event.y()) && event.button() == 0) {
          if (!this.hologramDataLoaded) {
            this.isUnstableError = true;
            lastErrorCode = "NO SCAN";
            if (this.minecraft != null && this.minecraft.player != null) {
              this.minecraft.player.playSound((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 0.5F);
            }

            return true;
          }

          if (this.flowProgress >= 1.0F && !this.isOverloading) {
            ItemStack fuel = ((Slot)((TimeMachineMenu)this.menu).slots.get(0)).getItem();
            boolean hasAmethyst = fuel.is(Items.AMETHYST_SHARD) || fuel.is(ModItems.CHARGED_AMETHYST);
            if (hasAmethyst && this.minecraft != null && this.minecraft.player != null) {
              ClientLevel cLevel = this.minecraft.level;
              BlockPos pPos = this.minecraft.player.blockPosition();
              if (cLevel.dimensionType().hasCeiling() || !cLevel.canSeeSky(pPos.above(2))) {
                this.isUnstableError = true;
                lastErrorCode = "NO SKY";
                this.minecraft.player.playSound((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 0.5F);
                return true;
              }

              if (pPos.getY() < Math.max(64, cLevel.getSeaLevel()) - 5) {
                this.isUnstableError = true;
                lastErrorCode = "< 64 Y";
                this.minecraft.player.playSound((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 0.5F);
                return true;
              }

              if (pPos.getY() + 90 >= cLevel.getMaxY()) {
                this.isUnstableError = true;
                lastErrorCode = "TOO HIGH";
                this.minecraft.player.playSound((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 0.5F);
                return true;
              }
            }

            this.isOverloading = true;
            this.overloadFrames = 0;
            RitualVisualManager.suppressZoneAudioUntil = System.currentTimeMillis() + 5000L;
            if (this.minecraft != null && this.minecraft.player != null) {
              this.minecraft.player.playSound(SoundEvents.LEVER_CLICK, 1.0F, 1.0F);
            }

            return true;
          }

          if (this.flowProgress < 1.0F) {
            if (this.minecraft != null && this.minecraft.player != null) {
              this.minecraft.player.playSound(SoundEvents.LEVER_CLICK, 0.8F, 0.5F);
            }

            return true;
          }
        }

        return super.mouseClicked(event, isDoubleClick);
      }
    }
  }

  public boolean mouseReleased(MouseButtonEvent event) {
    return this.monitorState == TimeMachineScreen.MonitorState.MAP_ACTIVE
        && this.hologramRenderer != null
        && this.hologramRenderer.mouseReleased(event.x(), event.y(), event.button())
      ? true
      : super.mouseReleased(event);
  }

  public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
    return this.monitorState == TimeMachineScreen.MonitorState.MAP_ACTIVE
        && this.hologramRenderer != null
        && this.hologramRenderer.mouseDragged(event.x(), event.y(), event.button(), dx, dy)
      ? true
      : super.mouseDragged(event, dx, dy);
  }

  public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    return this.monitorState == TimeMachineScreen.MonitorState.MAP_ACTIVE
        && this.hologramRenderer != null
        && this.hologramRenderer.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
      ? true
      : super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
  }

  private void initHologramRenderer(boolean isEcho, boolean sync) {
    if (this.minecraft != null && this.minecraft.player != null) {
      BlockPos playerPos = this.minecraft.player.blockPosition();
      int cx = playerPos.getX();
      int cz = playerPos.getZ();
      int radius = 300;
      String targetDim = this.versions.get(this.selectedGenIndex).get(this.selectedVersionIndex);
      String dimIdForScan = DimensionUtil.normalize(targetDim);
      if (!DimensionUtil.hasHologramMap(dimIdForScan)) {
        if (this.hologramRenderer != null) {
          this.hologramRenderer.close();
          this.hologramRenderer = null;
        }
        this.hologramDataLoaded = true;
        return;
      }
      HologramTerrainData terrainData;
      boolean canReuse = this.currentTerrainData != null
        && dimIdForScan.equals(this.currentDimId)
        && Math.abs(this.currentTerrainData.getCenterX() - cx) < 50
        && Math.abs(this.currentTerrainData.getCenterZ() - cz) < 50;
      if (canReuse) {
        terrainData = this.currentTerrainData;
        if (this.hologramRenderer == null) {
          this.hologramRenderer = new HologramMiniRenderer(cx, cz, radius, isEcho);
          this.hologramRenderer.setData(terrainData);
        }
        this.hologramDataLoaded = true;
      } else {
        if (this.hologramRenderer != null) {
          this.hologramRenderer.close();
        }
        this.hologramRenderer = new HologramMiniRenderer(cx, cz, radius, isEcho);
        terrainData = new HologramTerrainData(cx, cz, radius);
        this.currentTerrainData = terrainData;
        this.currentDimId = dimIdForScan;
        if (sync) {
          String dimId = DimensionUtil.normalize(targetDim);
          this.loadTerrainFromSources(terrainData, dimId);
          this.hologramRenderer.setData(terrainData);
          this.hologramDataLoaded = true;
          if (!this.hasTerrainRealData(terrainData) && DimensionUtil.isClientGenerated(dimId)) {
            this.autoGenerateCache(terrainData, dimIdForScan);
          }
        } else {
          CompletableFuture.runAsync(() -> {
            String dimIdx = DimensionUtil.normalize(targetDim);
            this.loadTerrainFromSources(terrainData, dimIdx);
            Minecraft.getInstance().execute(() -> {
              this.hologramRenderer.setData(terrainData);
              this.hologramDataLoaded = true;
              if (!this.hasTerrainRealData(terrainData) && DimensionUtil.isClientGenerated(dimIdx)) {
                this.autoGenerateCache(terrainData, dimIdForScan);
              }
            });
          });
        }
      }
    }
  }

  private void refreshTerrainData(String dimId) {
    if (this.hologramRenderer == null || this.currentTerrainData == null) return;
    CompletableFuture.runAsync(() -> {
      if (DimensionUtil.isClientGenerated(dimId)) {
        this.currentTerrainData.extractFromAlphaCache(dimId);
      } else {
        this.currentTerrainData.extractFromDimensionCache(dimId);
      }
      this.currentTerrainData.applyDeltas(dimId);
      if (!this.currentTerrainData.isReady()) {
        this.currentTerrainData.markReady();
      }
      Minecraft.getInstance().execute(() -> {
        if (this.hologramRenderer != null) {
          this.hologramRenderer.setData(this.currentTerrainData);
          this.hologramRenderer.markTextureDirty();
          if (this.hasTerrainRealData(this.currentTerrainData) && this.contentFadeAlpha == 0.0F) {
            this.contentFadeAlpha = 0.01F;
          }
        }
      });
    });
  }

  private void loadTerrainFromSources(HologramTerrainData terrainData, String dimId) {
    terrainData.loadFromHeightmap(dimId);
    if (DimensionUtil.isClientGenerated(dimId)) {
      terrainData.extractFromAlphaCache(dimId);
    } else {
      terrainData.extractFromDimensionCache(dimId);
    }
    terrainData.applyDeltas(dimId);
    if (!terrainData.isReady()) {
      terrainData.markReady();
    }
  }

  private boolean hasTerrainRealData(HologramTerrainData terrainData) {
    int diameter = terrainData.getDiameter();
    int step = Math.max(1, diameter / 32);
    for (int row = diameter / 4; row < diameter; row += diameter / 4) {
      for (int d = 0; d < diameter; d += step) {
        if (terrainData.getColor(d, row) != 0) {
          return true;
        }
      }
    }
    return false;
  }

  public void onClose() {
    AlphaByteCache.cancelGeneration();
    super.onClose();
  }

  public static void clearStaticCache() {
    if (hologramRenderer != null) {
      hologramRenderer.close();
      hologramRenderer = null;
    }
    currentTerrainData = null;
    currentDimId = null;
  }

  private void autoGenerateCache(HologramTerrainData terrainData, String dimId) {
    if (this.minecraft != null && this.minecraft.player != null) {
      BlockPos center = this.minecraft.player.blockPosition();
      long seed = AlphaEngineManager.getWorldSeed();
      AlphaByteCache.generateCacheProgressive(center, seed, dimId, (cp, chunkData) -> {
        terrainData.updateChunkRegion(cp, chunkData);
        Minecraft.getInstance().execute(() -> {
          if (this.hologramRenderer != null) {
            this.hologramRenderer.markTextureDirty();
            if (!this.hologramDataLoaded) {
              this.hologramDataLoaded = true;
            }
          }
        });
      }, () -> {});
    }
  }

  private float smoothStep(float edge0, float edge1, float x) {
    float t = Math.max(0.0F, Math.min(1.0F, (x - edge0) / (edge1 - edge0)));
    return t * t * (3.0F - 2.0F * t);
  }

  private static final Map<String, Long> lastPreloadTimestamps = new java.util.HashMap<>();
  private static final long PRELOAD_DEBOUNCE_MS = 3_000L;

  private void preloadServerDimensionCaches() {
    for (List<String> verList : this.versions) {
      for (String ver : verList) {
        String dimId = DimensionUtil.normalize(ver);
        if (!DimensionUtil.isClientGenerated(dimId)) {
          this.preloadDimensionCache(dimId);
        }
      }
    }
  }

  private void preloadDimensionCache(String dimId) {
    long now = System.currentTimeMillis();
    Long lastTime = lastPreloadTimestamps.get(dimId);
    if (lastTime != null && (now - lastTime) < PRELOAD_DEBOUNCE_MS) {
      return;
    }
    lastPreloadTimestamps.put(dimId, now);

    CompletableFuture.runAsync(
      () -> {
        HologramDiskCache.DimensionCacheResult diskResult = HologramDiskCache.loadDimensionCache(dimId);
        Minecraft.getInstance()
          .execute(
            () -> {
              if (Minecraft.getInstance().getConnection() != null) {
                if (diskResult != null && diskResult.sections() != null && !diskResult.sections().isEmpty()) {
                  DimensionHologramCache cache = DimensionHologramRegistry.getByName(dimId);
                  if (cache != null) {
                    Long2ObjectOpenHashMap<HologramSection> merged = new Long2ObjectOpenHashMap<>(diskResult.sections());
                    Long2ObjectOpenHashMap<HologramSection> existing = cache.getSections();
                    if (existing != null && !existing.isEmpty()) {
                      merged.putAll(existing);
                    }
                    cache.setSections(merged);
                    cache.setChunkVersions(diskResult.chunkVersions());
                  }

                  long[] chunkArr = cache != null ? cache.getChunkVersions().keySet().toLongArray() : new long[0];
                  ClientPlayNetworking.send(
                    new C2STerminalCacheRequestPayload(dimId, true, chunkArr, cache != null ? cache.getChunkVersions().values().toLongArray() : new long[0])
                  );
                } else {
                  ClientPlayNetworking.send(new C2STerminalCacheRequestPayload(dimId, false));
                }
              }
            }
          );
      }
    );
  }

  private static enum MonitorState {
    BOOTING,
    ALPHA_PROTOCOL,
    NO_SIGNAL,
    SIGNAL_ACQUIRED,
    MAP_ACTIVE;

    private MonitorState() {
    }
  }
}
