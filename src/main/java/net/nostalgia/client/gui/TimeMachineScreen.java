package net.nostalgia.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.nostalgia.inventory.TimeMachineMenu;
import net.nostalgia.network.C2STravelRequestPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class TimeMachineScreen extends AbstractContainerScreen<TimeMachineMenu> {

    public static boolean nextScreenIsError = false;
    public static String lastErrorCode = null;
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
    private float flowProgress = 0.0f;
    private int currentEnergyColor = 0x00D6D6;
    private float connectionPulse = 0.0f;
    private boolean wasCircuitClosed = false;
    private boolean wasFuelPresent = false;
    private float prevFlowProgress = 0.0f;
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
    private long lastManualTickTime = 0;

    public TimeMachineScreen(TimeMachineMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        ((net.nostalgia.mixin.alpha.AbstractContainerScreenAccessor) this).setImageHeight(176);
        this.inventoryLabelY = 1000;
        this.titleLabelX = 8;
        this.titleLabelY = 3;
    }

    @Override
    protected void init() {
        super.init();
        
        this.generations.clear();
        this.generations.addAll(List.of("alpha", "special", "home"));
        this.genNames.clear();
        this.genNames.addAll(List.of("ALPHA", "RD", "HOME"));
        
        this.versions.clear();
        this.versions.add(new ArrayList<>(List.of("alpha")));
        this.versions.add(new ArrayList<>(List.of("rd")));
        this.versions.add(new ArrayList<>(List.of("overworld")));
        
        this.versionNames.clear();
        this.versionNames.add(new ArrayList<>(List.of("1.1.2_01")));
        this.versionNames.add(new ArrayList<>(List.of("132211")));
        this.versionNames.add(new ArrayList<>(List.of("VANILLA")));
        
        this.versionIcons.clear();
        this.versionIcons.add(new ArrayList<>(List.of(TimeMachineLayout.ALPHA_NODE)));
        this.versionIcons.add(new ArrayList<>(List.of(TimeMachineLayout.RD_NODE)));
        this.versionIcons.add(new ArrayList<>(List.of(TimeMachineLayout.OVERWORLD_NODE)));

        String currentDimStr = this.minecraft.level.dimension().identifier().toString();
        String currentDimId = "unknown";
        if (currentDimStr.contains("alpha")) currentDimId = "alpha";
        else if (currentDimStr.contains("rd")) currentDimId = "rd";
        else if (currentDimStr.contains("overworld")) currentDimId = "overworld";

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
            this.genLampBrightnesses[i] = 0.0f;
            this.genPrevLampBrightnesses[i] = 0.0f;
        }
        for (int i = 0; i < 8; i++) {
            this.verCurrentChars[i] = ' ';
            this.verTargetChars[i] = ' ';
            this.verLampBrightnesses[i] = 0.0f;
            this.verPrevLampBrightnesses[i] = 0.0f;
        }

        if (nextScreenIsError) {
            this.isUnstableError = true;
            nextScreenIsError = false;
        }

        ItemStack fuel = this.menu.slots.get(0).getItem();
        boolean hasEcho = fuel.is(net.minecraft.world.item.Items.ECHO_SHARD);
        boolean hasAmethyst = fuel.is(net.minecraft.world.item.Items.AMETHYST_SHARD) || fuel.is(net.nostalgia.item.ModItems.CHARGED_AMETHYST);

        float initialEnergy = this.menu.getEnergy() / 100.0f;
        this.flowProgress = initialEnergy;
        this.prevFlowProgress = initialEnergy;

        List<PlasmaWireRenderer.WireSegment> w1 = List.of(
                new PlasmaWireRenderer.WireSegment(28, 100, 28, 121),
                new PlasmaWireRenderer.WireSegment(28, 121, 77, 121)
        );
        List<PlasmaWireRenderer.WireSegment> w2 = List.of(
                new PlasmaWireRenderer.WireSegment(56, 100, 56, 115),
                new PlasmaWireRenderer.WireSegment(56, 115, 77, 115)
        );
        List<PlasmaWireRenderer.WireSegment> w3 = List.of(
                new PlasmaWireRenderer.WireSegment(88, 100, 88, 115)
        );
        List<PlasmaWireRenderer.WireSegment> w4 = List.of(
                new PlasmaWireRenderer.WireSegment(121, 100, 121, 115),
                new PlasmaWireRenderer.WireSegment(121, 115, 99, 115)
        );
        List<PlasmaWireRenderer.WireSegment> w5 = List.of(
                new PlasmaWireRenderer.WireSegment(154, 100, 154, 121),
                new PlasmaWireRenderer.WireSegment(154, 121, 99, 121)
        );

        this.wires = new PlasmaWireRenderer.WirePath[]{
                new PlasmaWireRenderer.WirePath(2, w1),
                new PlasmaWireRenderer.WirePath(1, w2),
                new PlasmaWireRenderer.WirePath(2, w3),
                new PlasmaWireRenderer.WirePath(1, w4),
                new PlasmaWireRenderer.WirePath(2, w5)
        };
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.lastManualTickTime = System.currentTimeMillis();
        this.doTickLogic();
    }

    private void doTickLogic() {
        this.screenTickCount++;
        this.prevFlowProgress = this.flowProgress;
        this.sparks.removeIf(s -> {
            s.x += s.vx;
            s.y += s.vy;
            s.age++;
            return s.age >= s.maxAge;
        });

        System.arraycopy(this.genLampBrightnesses, 0, this.genPrevLampBrightnesses, 0, 5);
        System.arraycopy(this.verLampBrightnesses, 0, this.verPrevLampBrightnesses, 0, 8);

        String genStr = this.isUnstableError ? net.nostalgia.client.gui.TimeMachineErrorHandler.getLampGenString(lastErrorCode) : this.genNames.get(this.selectedGenIndex);
        for (int i = 0; i < 5; i++) {
            this.genTargetChars[i] = i < genStr.length() ? genStr.charAt(i) : ' ';
        }

        String verStr = this.isUnstableError ? net.nostalgia.client.gui.TimeMachineErrorHandler.getLampVerString(lastErrorCode) : this.versionNames.get(this.selectedGenIndex).get(this.selectedVersionIndex);
        for (int i = 0; i < 8; i++) {
            this.verTargetChars[i] = i < verStr.length() ? verStr.charAt(i) : ' ';
        }

        for (int i = 0; i < 5; i++) {
            if (this.genCurrentChars[i] == this.genTargetChars[i]) {
                this.genLampBrightnesses[i] = Math.min(1.0f, this.genLampBrightnesses[i] + 0.15f);
            } else {
                this.genLampBrightnesses[i] = Math.max(0.0f, this.genLampBrightnesses[i] - 0.20f);
                if (this.genLampBrightnesses[i] <= 0.0f) {
                    this.genCurrentChars[i] = this.genTargetChars[i];
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            if (this.verCurrentChars[i] == this.verTargetChars[i]) {
                this.verLampBrightnesses[i] = Math.min(1.0f, this.verLampBrightnesses[i] + 0.15f);
            } else {
                this.verLampBrightnesses[i] = Math.max(0.0f, this.verLampBrightnesses[i] - 0.20f);
                if (this.verLampBrightnesses[i] <= 0.0f) {
                    this.verCurrentChars[i] = this.verTargetChars[i];
                }
            }
        }

        ItemStack fuel = this.menu.slots.get(0).getItem();
        boolean hasEcho = fuel.is(net.minecraft.world.item.Items.ECHO_SHARD);
        boolean hasAmethyst = fuel.is(net.minecraft.world.item.Items.AMETHYST_SHARD) || fuel.is(net.nostalgia.item.ModItems.CHARGED_AMETHYST);
        boolean isFuelPresent = hasEcho || hasAmethyst;

        float targetProgress = isFuelPresent ? 1.0f : 0.0f;

        if (this.isUnstableError && !"UNSTABLE".equals(lastErrorCode) && !hasAmethyst) {
            this.isUnstableError = false;
            nextScreenIsError = false;
            lastErrorCode = null;
        }

        if (this.isFirstSync && this.menu.isDataSynced()) {
            this.flowProgress = targetProgress;
            this.prevFlowProgress = targetProgress;
            this.wasCircuitClosed = targetProgress >= 1.0f;
            this.wasFuelPresent = isFuelPresent;
            if (isFuelPresent) {
                this.currentEnergyColor = hasEcho ? 0x00D6D6 : 0xCC66FF;
            }
            this.isFirstSync = false;
        }

        if (isFuelPresent && !this.wasFuelPresent) {
            this.wasFuelPresent = true;
            if (this.minecraft != null && this.minecraft.player != null) {
                if (hasEcho) {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.SCULK_CATALYST_BLOOM, 0.8f, 1.3f);
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.BEACON_ACTIVATE, 0.5f, 1.5f);
                } else {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_PLACE, 1.0f, 1.2f);
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME, 0.8f, 1.1f);
                }
            }
        } else if (!isFuelPresent) {
            this.wasFuelPresent = false;
        }

        if (this.flowProgress < targetProgress) {
            this.flowProgress = Math.min(targetProgress, this.flowProgress + 0.015f);
        } else if (this.flowProgress > targetProgress) {
            this.flowProgress = Math.max(targetProgress, this.flowProgress - 0.025f);
        }

        if (this.flowProgress >= 1.0f && !this.wasCircuitClosed) {
            this.wasCircuitClosed = true;
            this.connectionPulse = 1.0f;
            if (this.minecraft != null && this.minecraft.player != null) {
                if (hasEcho) {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.BEACON_POWER_SELECT, 1.0f, 1.3f);
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.SCULK_BLOCK_CHARGE, 1.0f, 1.2f);
                } else {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.BEACON_POWER_SELECT, 1.0f, 1.3f);
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME, 1.0f, 1.1f);
                }
            }
        } else if (this.flowProgress < 1.0f) {
            this.wasCircuitClosed = false;
        }

        if (hasEcho) {
            this.currentEnergyColor = 0x00D6D6;
        } else if (hasAmethyst) {
            this.currentEnergyColor = 0xCC66FF;
        }

        if (this.flowProgress > 0.0f && this.minecraft != null && this.minecraft.player != null) {
            if (this.screenTickCount % 40 == 0) {
                this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.CONDUIT_AMBIENT, 0.15f * this.flowProgress, 1.2f);
            }
            if (Math.random() < 0.08 * this.flowProgress) {
                this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.CAMPFIRE_CRACKLE, 0.12f * this.flowProgress, 1.6f + (float) Math.random() * 0.4f);
            }
        }

        if (isFuelPresent && this.flowProgress > 0.0f) {
            if (Math.random() < 0.25f * this.flowProgress) {
                PlasmaWireRenderer.Spark spark = new PlasmaWireRenderer.Spark();
                spark.x = TimeMachineLayout.SLOT_X + 9.0f + (float) (Math.random() - 0.5f) * 12.0f;
                spark.y = TimeMachineLayout.SLOT_Y + 9.0f + (float) (Math.random() - 0.5f) * 12.0f;
                spark.vx = (float) (Math.random() - 0.5f) * 0.6f;
                spark.vy = -0.4f - (float) Math.random() * 0.8f;
                spark.maxAge = 15 + (int) (Math.random() * 15);
                spark.age = 0;
                spark.scale = 0.6f + (float) Math.random() * 0.8f;
                this.sparks.add(spark);
            }
        }

        if (this.isOverloading) {
            this.overloadFrames++;
            if (this.minecraft != null && this.minecraft.player != null) {
                if (this.overloadFrames == 1) {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.BEACON_ACTIVATE, 1.0f, 0.5f);
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.CONDUIT_ACTIVATE, 1.0f, 0.1f);
                }
                if (this.overloadFrames == 15) {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_RESONATE, 1.0f, 0.5f);
                }
                if (this.overloadFrames == 25) {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.RESPAWN_ANCHOR_CHARGE, 1.0f, 0.6f);
                }
                if (this.overloadFrames > 5 && Math.random() < 0.2) {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.GLASS_BREAK, 0.2f, (float) (1.2 + Math.random()));
                }
                if (this.overloadFrames > 40) {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.WARDEN_SONIC_BOOM, 2.0f, 0.5f);
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.PORTAL_TRAVEL, 0.4f, 1.2f);
                    String target = this.versions.get(this.selectedGenIndex).get(this.selectedVersionIndex);
                    ClientPlayNetworking.send(new C2STravelRequestPayload(target));
                    this.minecraft.player.closeContainer();
                }
            }
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        long now = System.currentTimeMillis();
        if (this.lastManualTickTime == 0) {
            this.lastManualTickTime = now;
        }
        while (now - this.lastManualTickTime >= 50L) {
            this.lastManualTickTime += 50L;
            this.doTickLogic();
        }



        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        int cx = x + TimeMachineLayout.SLOT_X;
        int cy = y + TimeMachineLayout.SLOT_Y;

        graphics.fill(cx - 8, cy - 8, cx + 24, cy + 24, TimeMachineLayout.GOLD_SHADOW);
        graphics.fill(cx - 7, cy - 7, cx + 23, cy + 23, TimeMachineLayout.GOLD_PRIMARY);
        graphics.fill(cx - 6, cy - 6, cx + 22, cy + 22, TimeMachineLayout.GOLD_BORDER_L);

        graphics.fill(cx - 5, cy - 5, cx + 21, cy + 21, TimeMachineLayout.BACKGROUND_COLOR);

        graphics.fill(cx - 3, cy - 3, cx + 19, cy + 19, 0xFF0A0510);
        graphics.fill(cx - 2, cy - 2, cx + 18, cy + 18, TimeMachineLayout.SLOT_BG);
        graphics.outline(cx - 3, cy - 3, 22, 22, this.isOverloading ? (0xFF000000 | this.currentEnergyColor) : TimeMachineLayout.SLOT_BORDER);

        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        this.extractTooltip(graphics, mouseX, mouseY);
    }

    private void renderChassis(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, TimeMachineLayout.BACKGROUND_COLOR);
        Random woodRand = new Random(1337);
        for (int i = 0; i < 15; i++) {
            int wy = y + woodRand.nextInt(this.imageHeight);
            int wh = woodRand.nextInt(6) + 2;
            int wcol = woodRand.nextBoolean() ? 0x125C3A21 : 0x0C2C170B;
            graphics.fill(x, wy, x + this.imageWidth, wy + wh, wcol);
        }
        graphics.fill(x, y, x + this.imageWidth, y + 4, 0x55000000);
        graphics.fill(x, y + this.imageHeight - 4, x + this.imageWidth, y + this.imageHeight, 0x55000000);
        graphics.fill(x, y, x + 4, y + this.imageHeight, 0x55000000);
        graphics.fill(x + this.imageWidth - 4, y, x + this.imageWidth, y + this.imageHeight, 0x55000000);

        int frameL = x - 2;
        int frameT = y - 2;
        int frameR = x + this.imageWidth + 2;
        int frameB = y + this.imageHeight + 2;

        graphics.fill(frameL, frameT, frameR - 1, frameT + 1, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(frameR - 1, frameT, frameR, frameT + 1, TimeMachineLayout.GOLD_SHADOW);

        graphics.fill(frameL, frameT + 1, frameL + 1, frameT + 2, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(frameL + 1, frameT + 1, frameR - 1, frameT + 2, TimeMachineLayout.GOLD_PRIMARY);
        graphics.fill(frameR - 1, frameT + 1, frameR, frameT + 2, TimeMachineLayout.GOLD_SHADOW);

        graphics.fill(frameL, frameT + 2, frameL + 1, frameT + 3, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(frameL + 1, frameT + 2, frameR - 2, frameT + 3, TimeMachineLayout.GOLD_PRIMARY);
        graphics.fill(frameR - 2, frameT + 2, frameR - 1, frameT + 3, TimeMachineLayout.GOLD_BORDER_R);
        graphics.fill(frameR - 1, frameT + 2, frameR, frameT + 3, TimeMachineLayout.GOLD_SHADOW);

        graphics.fill(frameL, frameT + 3, frameL + 1, frameT + 4, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(frameL + 1, frameT + 3, frameL + 2, frameT + 4, TimeMachineLayout.GOLD_BORDER_L);
        graphics.fill(frameL + 2, frameT + 3, frameR - 2, frameT + 4, TimeMachineLayout.GOLD_PRIMARY);
        graphics.fill(frameR - 2, frameT + 3, frameR - 1, frameT + 4, TimeMachineLayout.GOLD_BORDER_R);
        graphics.fill(frameR - 1, frameT + 3, frameR, frameT + 4, TimeMachineLayout.GOLD_SHADOW);

        graphics.fill(frameL, frameT + 4, frameL + 1, frameB - 4, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(frameL + 1, frameT + 4, frameL + 4, frameB - 4, TimeMachineLayout.GOLD_BORDER_L);
        graphics.fill(frameR - 4, frameT + 4, frameR - 1, frameB - 4, TimeMachineLayout.GOLD_BORDER_R);
        graphics.fill(frameR - 1, frameT + 4, frameR, frameB - 4, TimeMachineLayout.GOLD_SHADOW);

        graphics.fill(frameL, frameB - 4, frameL + 1, frameB - 3, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(frameL + 1, frameB - 4, frameL + 2, frameB - 3, TimeMachineLayout.GOLD_BORDER_L);
        graphics.fill(frameL + 2, frameB - 4, frameR - 2, frameB - 3, TimeMachineLayout.GOLD_SECONDARY);
        graphics.fill(frameR - 2, frameB - 4, frameR - 1, frameB - 3, TimeMachineLayout.GOLD_BORDER_R);
        graphics.fill(frameR - 1, frameB - 4, frameR, frameB - 3, TimeMachineLayout.GOLD_SHADOW);

        graphics.fill(frameL, frameB - 3, frameL + 1, frameB - 2, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(frameL + 1, frameB - 3, frameR - 2, frameB - 2, TimeMachineLayout.GOLD_SECONDARY);
        graphics.fill(frameR - 2, frameB - 3, frameR - 1, frameB - 2, TimeMachineLayout.GOLD_BORDER_R);
        graphics.fill(frameR - 1, frameB - 3, frameR, frameB - 2, TimeMachineLayout.GOLD_SHADOW);

        graphics.fill(frameL, frameB - 2, frameL + 1, frameB - 1, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(frameL + 1, frameB - 2, frameR - 1, frameB - 1, TimeMachineLayout.GOLD_SECONDARY);
        graphics.fill(frameR - 1, frameB - 2, frameR, frameB - 1, TimeMachineLayout.GOLD_SHADOW);

        graphics.fill(frameL, frameB - 1, frameR, frameB, TimeMachineLayout.GOLD_SHADOW);

        int[][] rivets = {
                {frameL + 6, frameT + 6},
                {frameR - 7, frameT + 6},
                {frameL + 6, frameB - 7},
                {frameR - 7, frameB - 7}
        };
        for (int[] r : rivets) {
            graphics.fill(r[0] - 1, r[1] - 1, r[0] + 2, r[1] + 2, TimeMachineLayout.RIVET_DARK);
            graphics.fill(r[0], r[1], r[0] + 1, r[1] + 1, TimeMachineLayout.RIVET_LIGHT);
            graphics.fill(r[0], r[1], r[0], r[1], TimeMachineLayout.RIVET_BRIGHT);
        }
    }

    private void renderWiresAndPlasma(GuiGraphicsExtractor graphics, int x, int y, float gameTime, float renderedProgress, float overloadProgress) {
        if (this.wires != null) {
            for (PlasmaWireRenderer.WirePath w : this.wires) {
                PlasmaWireRenderer.drawWireBase(graphics, w, x, y);
            }
        }

        if (renderedProgress > 0.0f) {
            float plasmaSpeed = this.isOverloading ? (0.003f + overloadProgress * 0.012f) : 0.0022f;
            if (this.wires != null) {
                for (int i = 0; i < this.wires.length; i++) {
                    PlasmaWireRenderer.WirePath w = this.wires[i];
                    float wireProgress;
                    if (i == 0 || i == 4) {
                        wireProgress = smoothStep(0.0f, 0.5f, renderedProgress);
                    } else if (i == 1 || i == 3) {
                        wireProgress = smoothStep(0.3f, 0.8f, renderedProgress);
                    } else {
                        wireProgress = smoothStep(0.6f, 1.0f, renderedProgress);
                    }
                    if (wireProgress > 0.0f) {
                        PlasmaWireRenderer.drawPlasmaWire(graphics, w, x, y, gameTime, plasmaSpeed, this.currentEnergyColor, wireProgress, overloadProgress, this.isOverloading);
                    }
                }
            }
        }

        if (this.isOverloading && this.wires != null) {
            if (overloadProgress > 0.3f) {
                for (PlasmaWireRenderer.WirePath w : this.wires) {
                    PlasmaWireRenderer.drawCableLightning(graphics, w, x, y, gameTime, this.currentEnergyColor);
                }
            }
            int flashAlpha = (int) (Math.sin(this.overloadFrames * 0.4f) * 20 + 20);
            graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, (flashAlpha << 24) | this.currentEnergyColor);
        }
    }

    private void renderFuelSlot(GuiGraphicsExtractor graphics, int x, int y, float gameTime, float renderedProgress, float overloadProgress) {
        int sx = x + TimeMachineLayout.SLOT_X;
        int sy = y + TimeMachineLayout.SLOT_Y;


        int[][] rivets = {{sx - 7, sy - 7}, {sx + 22, sy - 7}, {sx - 7, sy + 22}, {sx + 22, sy + 22}};
        for (int[] r : rivets) {
            graphics.fill(r[0], r[1], r[0] + 2, r[1] + 2, TimeMachineLayout.RIVET_DARK);
            graphics.fill(r[0], r[1], r[0] + 1, r[1] + 1, TimeMachineLayout.RIVET_BRIGHT);
        }


        graphics.fill(sx + 5, sy - 8, sx + 11, sy - 6, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(sx + 5, sy + 22, sx + 11, sy + 24, TimeMachineLayout.GOLD_SECONDARY);
        graphics.fill(sx - 8, sy + 5, sx - 6, sy + 11, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(sx + 22, sy + 5, sx + 24, sy + 11, TimeMachineLayout.GOLD_SECONDARY);

        if (renderedProgress > 0.0f) {
            float pulse = 0.5f + 0.5f * (float) Math.sin(gameTime * 0.005f);
            float radius = 8.0f + pulse * 4.0f;
            int alphaGlow = (int) (renderedProgress * (40.0f + pulse * 20.0f));
            for (int r = 1; r <= (int) radius; r++) {
                int a = (int) (alphaGlow * (1.0f - (float) r / radius));
                int col = (a << 24) | (this.currentEnergyColor & 0xFFFFFF);
                graphics.outline(sx + 8 - r, sy + 8 - r, r * 2, r * 2, col);
            }
            PlasmaWireRenderer.drawSlotLightning(graphics, x, y, gameTime, this.currentEnergyColor, overloadProgress);
        }

        if (!this.sparks.isEmpty()) {
            for (PlasmaWireRenderer.Spark s : this.sparks) {
                float lifeRatio = 1.0f - ((float) s.age / s.maxAge);
                int alpha = (int) (lifeRatio * 255 * renderedProgress);
                int color = (alpha << 24) | (this.currentEnergyColor & 0xFFFFFF);
                int px = x + (int) s.x;
                int py = y + (int) s.y;
                int size = Math.max(1, (int) (s.scale * lifeRatio * 2.0f));
                graphics.fill(px - size / 2, py - size / 2, px - size / 2 + size, py - size / 2 + size, color);
            }
        }

        if (this.connectionPulse > 0.0f) {
            int rCol = (this.currentEnergyColor >> 16) & 0xFF;
            int gCol = (this.currentEnergyColor >> 8) & 0xFF;
            int bCol = this.currentEnergyColor & 0xFF;
            float waveSize = (1.0f - this.connectionPulse) * 35.0f;
            int alphaWave = (int) (this.connectionPulse * 200.0f);
            int colWave = (alphaWave << 24) | (rCol << 16) | (gCol << 8) | bCol;
            int waveX = (x + 88) - 11 - (int) waveSize;
            int waveY = (y + 126) - 11 - (int) waveSize;
            int waveW = 22 + (int) waveSize * 2;
            int waveH = 22 + (int) waveSize * 2;
            graphics.outline(waveX, waveY, waveW, waveH, colWave);
            graphics.outline(waveX - 1, waveY - 1, waveW + 2, waveH + 2, colWave);
            int flashAlpha = (int) (this.connectionPulse * 45.0f);
            int colFlash = (flashAlpha << 24) | (rCol << 16) | (gCol << 8) | bCol;
            graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, colFlash);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        float renderedProgress = this.prevFlowProgress + (this.flowProgress - this.prevFlowProgress) * partialTick;
        float gameTime = (this.screenTickCount + partialTick) * 50.0f;
        float overloadProgress = this.isOverloading ? (this.overloadFrames / 40.0f) : 0.0f;

        renderChassis(graphics, x, y);


        int panelTop = y + 142;

        graphics.fill(x, panelTop, x + this.imageWidth, y + this.imageHeight, 0xFF25130A);


        Random woodRand = new Random(54321);
        for (int i = 0; i < 4; i++) {
            int wy = panelTop + 2 + woodRand.nextInt(this.imageHeight - 146);
            int wh = woodRand.nextInt(2) + 1;
            int wcol = woodRand.nextBoolean() ? 0x1A402213 : 0x140F0804;
            graphics.fill(x + 2, wy, x + this.imageWidth - 2, wy + wh, wcol);
        }


        graphics.outline(x, panelTop, this.imageWidth, this.imageHeight - 142, TimeMachineLayout.GOLD_SHADOW);
        int pL = x + 2;
        int pR = x + this.imageWidth - 2;
        int pT = panelTop + 2;
        int pB = y + this.imageHeight - 2;
        graphics.fill(pL, pT, pR, pT + 1, TimeMachineLayout.GOLD_LIGHT);
        graphics.fill(pL, pT + 1, pL + 1, pB, TimeMachineLayout.GOLD_BORDER_L);
        graphics.fill(pR - 1, pT + 1, pR, pB, TimeMachineLayout.GOLD_SHADOW);
        graphics.fill(pL + 1, pB - 1, pR - 1, pB, TimeMachineLayout.GOLD_SHADOW);


        graphics.outline(x + 7, y + 146, 162, 18, 0xFF1E1008);


        for (int col = 0; col < 9; col++) {
            int slotX = x + 8 + col * 18;
            int slotY = y + 147;

            graphics.fill(slotX, slotY, slotX + 16, slotY + 16, 0xFF0B0604);
            graphics.outline(slotX - 1, slotY - 1, 18, 18, TimeMachineLayout.GOLD_BORDER_R);
            graphics.outline(slotX, slotY, 16, 16, 0xFF1E1008);
        }


        int[][] panelRivets = {
            {x + 5, y + 168},
            {x + 171, y + 168}
        };
        for (int[] r : panelRivets) {
            graphics.fill(r[0], r[1], r[0] + 2, r[1] + 2, TimeMachineLayout.RIVET_DARK);
            graphics.fill(r[0], r[1], r[0] + 1, r[1] + 1, TimeMachineLayout.RIVET_LIGHT);
            graphics.fill(r[0], r[1], r[0], r[1], TimeMachineLayout.RIVET_BRIGHT);
        }

        Identifier currentIcon = this.versionIcons.get(this.selectedGenIndex).get(this.selectedVersionIndex);
        CrtMonitorRenderer.render(graphics, x, y, gameTime, this.isOverloading, overloadProgress, this.currentEnergyColor, currentIcon);

        this.connectionPulse = Math.max(0.0f, this.connectionPulse - 0.03f);

        renderWiresAndPlasma(graphics, x, y, gameTime, renderedProgress, overloadProgress);
        renderFuelSlot(graphics, x, y, gameTime, renderedProgress, overloadProgress);

        int lampColor = this.isUnstableError ? 0xFF0000 : this.currentEnergyColor;

        for (int i = 0; i < 5; i++) {
            int lx = x + 8 + i * 14;
            int ly = y + 78;
            boolean isHovered = isHoveringArea(x + 8, y + 78, 70, 22, mouseX, mouseY) && !this.isOverloading;
            float lBright = this.genPrevLampBrightnesses[i] + (this.genLampBrightnesses[i] - this.genPrevLampBrightnesses[i]) * partialTick;
            NixieLampRenderer.render(graphics, this.font, lx, ly, 12, 22, this.genCurrentChars[i], lBright, lampColor, isHovered);
        }

        for (int i = 0; i < 8; i++) {
            int lx = x + 83 + i * 11;
            int ly = y + 78;
            boolean isHovered = isHoveringArea(x + 83, y + 78, 87, 22, mouseX, mouseY) && !this.isOverloading;
            float lBright = this.verPrevLampBrightnesses[i] + (this.verLampBrightnesses[i] - this.verPrevLampBrightnesses[i]) * partialTick;
            NixieLampRenderer.render(graphics, this.font, lx, ly, 10, 22, this.verCurrentChars[i], lBright, lampColor, isHovered);
        }

        LeverRenderer.render(graphics, this.font, x, y, gameTime, renderedProgress, partialTick, this.isOverloading, this.overloadFrames, this.currentEnergyColor);

        int btnGoX = x + TimeMachineLayout.TOGGLE_X;
        int btnGoY = y + TimeMachineLayout.TOGGLE_Y;
        boolean hoverGo = isHoveringArea(btnGoX, btnGoY, TimeMachineLayout.TOGGLE_W, TimeMachineLayout.TOGGLE_H, mouseX, mouseY) && !this.isOverloading;

        if (hoverGo) {
            graphics.setTooltipForNextFrame(this.font, Component.translatable("gui.nostalgia.time_machine.launch_tooltip"), mouseX, mouseY);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
    }

    private boolean isHoveringArea(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        if (this.isUnstableError) {
            return super.mouseClicked(event, isDoubleClick);
        }
        if (this.isOverloading) return false;

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if (isHoveringArea(x + 8, y + 78, 70, 22, event.x(), event.y()) && event.button() == 0) {
            this.selectedGenIndex = (this.selectedGenIndex + 1) % this.generations.size();
            this.selectedVersionIndex = 0;
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1.0f);
            }
            return true;
        }

        if (isHoveringArea(x + 83, y + 78, 87, 22, event.x(), event.y()) && event.button() == 0) {
            List<String> curVersions = this.versions.get(this.selectedGenIndex);
            this.selectedVersionIndex = (this.selectedVersionIndex + 1) % curVersions.size();

            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1.0f);
            }
            return true;
        }

        int btnGoX = x + TimeMachineLayout.TOGGLE_X;
        int btnGoY = y + TimeMachineLayout.TOGGLE_Y;
        if (isHoveringArea(btnGoX, btnGoY, TimeMachineLayout.TOGGLE_W, TimeMachineLayout.TOGGLE_H, event.x(), event.y()) && event.button() == 0) {
            if (this.flowProgress >= 1.0f && !this.isOverloading) {
                
                ItemStack fuel = this.menu.slots.get(0).getItem();
                boolean hasAmethyst = fuel.is(net.minecraft.world.item.Items.AMETHYST_SHARD) || fuel.is(net.nostalgia.item.ModItems.CHARGED_AMETHYST);

                if (hasAmethyst && this.minecraft != null && this.minecraft.player != null) {
                    net.minecraft.client.multiplayer.ClientLevel cLevel = this.minecraft.level;
                    net.minecraft.core.BlockPos pPos = this.minecraft.player.blockPosition();
                    
                    if (cLevel.dimensionType().hasCeiling() || !cLevel.canSeeSky(pPos.above(2))) {
                        this.isUnstableError = true;
                        lastErrorCode = "NO SKY";
                        this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 0.5f);
                        return true;
                    }
                    if (pPos.getY() < Math.max(64, cLevel.getSeaLevel()) - 5) {
                        this.isUnstableError = true;
                        lastErrorCode = "< 64 Y";
                        this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 0.5f);
                        return true;
                    }
                    if (pPos.getY() + 90 >= cLevel.getMaxY()) {
                        this.isUnstableError = true;
                        lastErrorCode = "TOO HIGH";
                        this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 0.5f);
                        return true;
                    }
                }

                this.isOverloading = true;
                this.overloadFrames = 0;
                net.nostalgia.client.events.echo.RitualVisualManager.suppressZoneAudioUntil = System.currentTimeMillis() + 5000L;
                if (this.minecraft != null && this.minecraft.player != null) {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.LEVER_CLICK, 1.0f, 1.0f);
                }
                return true;
            } else if (this.flowProgress < 1.0f) {
                if (this.minecraft != null && this.minecraft.player != null) {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.LEVER_CLICK, 0.8f, 0.5f);
                }
                return true;
            }
        }

        return super.mouseClicked(event, isDoubleClick);
    }

    private float smoothStep(float edge0, float edge1, float x) {
        float t = Math.max(0.0f, Math.min(1.0f, (x - edge0) / (edge1 - edge0)));
        return t * t * (3.0f - 2.0f * t);
    }
}
