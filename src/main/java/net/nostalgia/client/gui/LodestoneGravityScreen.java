package net.nostalgia.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.nostalgia.inventory.LodestoneGravityMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LodestoneGravityScreen extends AbstractContainerScreen<LodestoneGravityMenu> {

    private static final int BG_WIDTH = 180;
    private static final int BG_HEIGHT = 176;

    private int selectedDirection = -1;
    private boolean wasAmethystInSlot = false;
    private float energyLevel = 0.0f;

    private int overloadTicks = -1;
    private int overloadingDirection = -1;
    
    private float currentPullX = 0.0f;
    private float currentPullY = 0.0f;
    private float hoverSpin = 0.0f;
    private boolean isHoveringButton = false;

    private int ambientSoundTimer = 0;
    private int hoverSoundTimer = 0;

    private float floatTimer = 0.0f;
    private float spinTimer = 0.0f;
    private float glowSurge = 0.0f;
    private float programFlash = 0.0f;

    private static class MysticRune {
        int x, y;
        String symbol;
        float basePhase;
        float breatheSpeed;
    }
    private final List<MysticRune> scatteredRunes = new ArrayList<>();

    private static class MagicSpark {
        float x, y;
        float vx, vy;
        int age;
        int maxAge;
        float size;
    }
    private final List<MagicSpark> sparks = new ArrayList<>();

    public LodestoneGravityScreen(LodestoneGravityMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        ((net.nostalgia.mixin.alpha.AbstractContainerScreenAccessor) this).setImageWidth(BG_WIDTH);
        ((net.nostalgia.mixin.alpha.AbstractContainerScreenAccessor) this).setImageHeight(BG_HEIGHT);
        this.inventoryLabelY = 1000;
        this.titleLabelX = 8;
        this.titleLabelY = 4;

        String[] glyphs = {"ᚦ", "ᚢ", "ᚦ", "ᚨ", "ᚱ", "ᚲ", "ᚷ", "ᚹ", "ᚺ", "ᚾ", "ᛁ", "ᛃ", "ᛇ", "ᛈ", "ᛉ", "ᛊ", "ᛏ", "ᛒ"};
        Random r = new Random(888);

        int[][] positions = {
            {16, 36}, {154, 36}, {18, 92}, {152, 92},
            {40, 20}, {128, 20}, {40, 108}, {128, 108},
            {12, 64}, {158, 64},
            {12, 18}, {168, 18},
            {12, 118}, {168, 118},
            {80, 16}, {100, 16}
        };

        for (int i = 0; i < positions.length; i++) {
            MysticRune rune = new MysticRune();
            rune.x = positions[i][0];
            rune.y = positions[i][1];
            rune.symbol = glyphs[r.nextInt(glyphs.length)];
            rune.basePhase = r.nextFloat() * 10.0f;
            rune.breatheSpeed = 0.02f;
            scatteredRunes.add(rune);
        }
    }

    @Override
    public void containerTick() {
        super.containerTick();
        floatTimer += 0.035f;


        if (overloadTicks > 0) {
            overloadTicks--;
            glowSurge = 2.0f;
            
            Random r = new Random();
            int x = (this.width - BG_WIDTH) / 2;
            int y = (this.height - BG_HEIGHT) / 2;
            int cx = x + BG_WIDTH / 2;
            int cy = y + 72;
            for (int i = 0; i < 4; i++) {
                MagicSpark spark = new MagicSpark();
                spark.x = cx;
                spark.y = cy;
                double angle = r.nextDouble() * 2.0 * Math.PI;
                double speed = 1.0 + r.nextDouble() * 4.0;
                spark.vx = (float)(Math.cos(angle) * speed);
                spark.vy = (float)(Math.sin(angle) * speed);
                spark.age = 0;
                spark.maxAge = 12 + r.nextInt(15);
                spark.size = 1.0f + r.nextFloat() * 2.0f;
                sparks.add(spark);
            }

            if (overloadTicks == 0) {
                overloadTicks = -1;
                net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new net.nostalgia.network.C2SProgramAmethystPayload(overloadingDirection));
                selectedDirection = overloadingDirection;
                programFlash = 1.0f;
                if (this.minecraft != null && this.minecraft.player != null) {
                    this.minecraft.player.playSound(SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, 0.9f, 1.25f);
                    this.minecraft.player.playSound(SoundEvents.CONDUIT_ACTIVATE, 0.7f, 1.10f);
                    this.minecraft.player.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 1.0f, 1.0f);
                }
            }
        }


        if (spinTimer > 0.0f) {
            spinTimer = Math.max(0.0f, spinTimer - 0.03f);
        }
        if (glowSurge > 0.0f) {
            glowSurge = Math.max(0.0f, glowSurge - 0.025f);
        }
        if (programFlash > 0.0f) {
            programFlash = Math.max(0.0f, programFlash - 0.04f);
        }


        sparks.removeIf(s -> {
            s.x += s.vx;
            s.y += s.vy;
            s.age++;
            return s.age >= s.maxAge;
        });


        net.minecraft.world.item.ItemStack slotStack = this.menu.slots.get(0).getItem();
        boolean hasAmethyst = !slotStack.isEmpty();
        boolean isChargedAmethyst = hasAmethyst && slotStack.is(net.nostalgia.item.ModItems.CHARGED_AMETHYST);

        if (hasAmethyst && !wasAmethystInSlot) {
            wasAmethystInSlot = true;
            spinTimer = 1.0f;
            glowSurge = 1.0f;
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, 0.85f, 0.85f);
                this.minecraft.player.playSound(SoundEvents.SCULK_CATALYST_BLOOM, 0.75f, 0.85f);
            }
        } else if (!hasAmethyst) {
            wasAmethystInSlot = false;
            selectedDirection = -1;
        }


        if (isChargedAmethyst) {
            String dirStr = net.nostalgia.item.ChargedAmethystItem.getDirection(slotStack);
            int dirIdx = switch (dirStr) {
                case "up" -> 0;
                case "down" -> 1;
                case "left" -> 2;
                case "right" -> 3;
                default -> -1;
            };
            if (dirIdx != -1) {
                selectedDirection = dirIdx;
            }
        }


        if (hasAmethyst) {
            energyLevel = Math.min(1.0f, energyLevel + 0.05f);
        } else {
            energyLevel = Math.max(0.0f, energyLevel - 0.08f);
        }


        if (energyLevel > 0.05f) {
            ambientSoundTimer--;
            if (ambientSoundTimer <= 0) {
                if (this.minecraft != null && this.minecraft.player != null) {
                    this.minecraft.player.playSound(SoundEvents.RESPAWN_ANCHOR_AMBIENT, 0.28f * energyLevel, 0.75f);
                }
                ambientSoundTimer = 80;
            }
        } else {
            ambientSoundTimer = 0;
        }


        if (isHoveringButton && wasAmethystInSlot) {
            hoverSoundTimer--;
            if (hoverSoundTimer <= 0) {
                if (this.minecraft != null && this.minecraft.player != null) {
                    this.minecraft.player.playSound(SoundEvents.VAULT_AMBIENT, 0.24f * energyLevel, 0.65f + (float)Math.random() * 0.1f);
                    this.minecraft.player.playSound(SoundEvents.CONDUIT_AMBIENT, 0.15f * energyLevel, 0.80f);
                }
                hoverSoundTimer = 15 + (int)(Math.random() * 10);
            }
        } else {
            hoverSoundTimer = 0;
        }


        if (wasAmethystInSlot && Math.random() < 0.25) {
            MagicSpark s = new MagicSpark();
            s.x = BG_WIDTH / 2.0f;
            s.y = 72.0f;
            s.vx = (float)(Math.random() - 0.5f) * 0.7f;
            s.vy = (float)(Math.random() - 0.5f) * 0.7f;
            s.age = 0;
            s.maxAge = 12 + (int)(Math.random() * 12);
            s.size = 0.6f + (float)Math.random() * 0.8f;
            sparks.add(s);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int x = (this.width - BG_WIDTH) / 2;
        int y = (this.height - BG_HEIGHT) / 2;
        int cx = x + BG_WIDTH / 2;
        int cy = y + 72;

        float pTime = floatTimer + partialTick;
        float surge = glowSurge > 0.0f ? (glowSurge - 0.025f * partialTick) : 0.0f;
        float spin = spinTimer > 0.0f ? (spinTimer - 0.03f * partialTick) : 0.0f;
        float prog = programFlash > 0.0f ? (programFlash - 0.04f * partialTick) : 0.0f;


        float breathe = 0.35f + 0.65f * (float)Math.sin(pTime * 0.9f);
        
        float overloadFactor = 0.0f;
        int activeEnergyColor = 0xFFa89cb8;
        int coreEnergyColor = 0xFFdfd6eb;

        if (overloadTicks > 0) {
            float progress = (float) overloadTicks / 40.0f;
            overloadFactor = (float) Math.sin(progress * Math.PI);
            
            activeEnergyColor = blendColors(0xFFa89cb8, 0xFFc87cff, overloadFactor);
            coreEnergyColor = blendColors(0xFFdfd6eb, 0xFFe0b0ff, overloadFactor);
        }

        float pulseFactor = breathe * 0.5f + surge * 1.5f + overloadFactor * 3.5f;
        pulseFactor = Math.min(1.0f + overloadFactor * 2.0f, Math.max(0.1f, pulseFactor));


        float hoverX = 0.0f;
        float hoverY = 0.0f;
        boolean hoveringAny = false;
        int hoveredDir = -1;

        int btnSize = 16;
        int[][] btnLayout = {
            {0, -48, 0},
            {0, 48, 1},
            {-48, 0, 2},
            {48, 0, 3}
        };

        for (int[] b : btnLayout) {
            int bx = cx + b[0] - btnSize / 2;
            int by = cy + b[1] - btnSize / 2;
            int dirIdx = b[2];
            if (mouseX >= bx && mouseX < bx + btnSize && mouseY >= by && mouseY < by + btnSize) {
                hoveringAny = true;
                hoveredDir = dirIdx;
                switch (dirIdx) {
                    case 0 -> { hoverX = 0.0f; hoverY = -1.0f; }
                    case 1 -> { hoverX = 0.0f; hoverY = 1.0f; }
                    case 2 -> { hoverX = -1.0f; hoverY = 0.0f; }
                    case 3 -> { hoverX = 1.0f; hoverY = 0.0f; }
                }
                break;
            }
        }


        float lerpSpeed = hoveringAny ? 0.15f : 0.007f;
        currentPullX += (hoverX - currentPullX) * lerpSpeed;
        currentPullY += (hoverY - currentPullY) * lerpSpeed;

        if (hoveringAny && wasAmethystInSlot) {
            hoverSpin += 0.06f;
        } else {
            hoverSpin += (0.0f - hoverSpin) * 0.008f;
        }
        isHoveringButton = hoveringAny;

        graphics.fill(x, y, x + BG_WIDTH, y + 142, 0xFF5C6067);
        graphics.outline(x, y, BG_WIDTH, 142, 0xFF232629);
        graphics.outline(x + 2, y + 2, BG_WIDTH - 4, 138, 0xFF8C929A);

        Random stoneRand = new Random(777);
        for (int i = 0; i < 35; i++) {
            int rx = stoneRand.nextInt(BG_WIDTH);
            int ry = stoneRand.nextInt(142);
            int rw = stoneRand.nextInt(12) + 4;
            int rh = stoneRand.nextInt(6) + 2;
            int rcol = stoneRand.nextBoolean() ? 0x1A8C929A : 0x1E232629;
            graphics.fill(x + rx, y + ry, x + rx + rw, y + ry + rh, rcol);
        }

        graphics.fill(x, y + 142, x + BG_WIDTH, y + BG_HEIGHT, 0xFF484D52);
        graphics.outline(x, y + 142, BG_WIDTH, BG_HEIGHT - 142, 0xFF232629);
        graphics.outline(x + 2, y + 144, BG_WIDTH - 4, BG_HEIGHT - 146, 0xFF757A80);


        graphics.outline(x + 8, y + 146, 164, 20, 0xFF232629);
        for (int col = 0; col < 9; col++) {
            int slotX = x + 9 + col * 18;
            int slotY = y + 147;
            graphics.fill(slotX, slotY, slotX + 16, slotY + 16, 0xFF232629);
            graphics.outline(slotX - 1, slotY - 1, 18, 18, 0xFF5C6067);
        }


        int ringColor = blendColors(0xFF232629, activeEnergyColor, energyLevel);
        drawCircle(graphics, cx, cy, 54, 2, ringColor, pTime, energyLevel, currentPullX, currentPullY);
        drawCircle(graphics, cx, cy, 50, 2, ringColor, pTime, energyLevel, currentPullX, currentPullY);


        drawCircle(graphics, cx, cy, 36, 2, ringColor, pTime, energyLevel, currentPullX, currentPullY);
        drawCircle(graphics, cx, cy, 32, 2, ringColor, pTime, energyLevel, currentPullX, currentPullY);


        int outerSquareColor = blendColors(0xFF232629, (activeEnergyColor & 0xFFFFFF) | 0xCC000000, energyLevel);
        int innerSquareColor = blendColors(0xFF757A80, (coreEnergyColor & 0xFFFFFF) | 0xDD000000, energyLevel);
        graphics.outline(cx - 14, cy - 14, 28, 28, outerSquareColor);
        graphics.outline(cx - 15, cy - 15, 30, 30, innerSquareColor);


        double[] angles = {0, Math.PI / 4, Math.PI / 2, 3 * Math.PI / 4, Math.PI, 5 * Math.PI / 4, 3 * Math.PI / 2, 7 * Math.PI / 4};
        for (double angle : angles) {
            int x1 = cx + (int) Math.round(15 * Math.cos(angle));
            int y1 = cy + (int) Math.round(15 * Math.sin(angle));
            

            float dot = (float)(Math.cos(angle) * currentPullX + Math.sin(angle) * currentPullY);
            float magnetize = (float)Math.pow(Math.max(0.0f, dot), 3.0f) * energyLevel * 7.5f;
            float warp = 0.0f;
            if (energyLevel > 0.0f && dot > 0.0f) {
                warp = (float)Math.pow(dot, 2.0f) * energyLevel * 3.0f * (float)Math.sin(pTime * 7.5f - 50.0f * 0.12f + angle * 2.0f);
            }
            float outerR = 50.0f + magnetize + warp;
            
            int x2 = cx + (int) Math.round(outerR * Math.cos(angle));
            int y2 = cy + (int) Math.round(outerR * Math.sin(angle));
            
            if (energyLevel > 0.0f) {
                drawThickSegment(graphics, x1, y1, x2, y2, 2, blendColors(0xFF232629, (activeEnergyColor & 0xFFFFFF) | 0x33000000, energyLevel));
                
                int steps = 18;
                for (int j = 0; j < steps; j++) {
                    float t1 = (float)j / steps;
                    float t2 = (float)(j + 1) / steps;
                    int px1 = (int)(x1 + (x2 - x1) * t1);
                    int py1 = (int)(y1 + (y2 - y1) * t1);
                    int px2 = (int)(x1 + (x2 - x1) * t2);
                    int py2 = (int)(y1 + (y2 - y1) * t2);
                    
                    float wave = 0.5f + 0.5f * (float)Math.sin(t1 * 5.0 - pTime * 8.0);
                    

                    float intensityFactor = 1.0f + dot * 0.7f * energyLevel;
                    
                    int alpha = (int)((50 + 40 * wave) * energyLevel * intensityFactor);
                    int col = (alpha << 24) | (coreEnergyColor & 0xFFFFFF);
                    drawThickSegment(graphics, px1, py1, px2, py2, 2, col);
                }
            } else {
                drawThickSegment(graphics, x1, y1, x2, y2, 2, ringColor);
            }
        }


        for (MysticRune rune : scatteredRunes) {
            int idleRuneColor = 0x88232629;
            
            float rBreathe = 0.35f + 0.65f * (float)Math.sin(rune.basePhase + pTime * 1.8f);
            float brightness = rBreathe * 0.35f + surge * 0.55f;
            brightness = Math.min(0.8f, Math.max(0.15f, brightness));
            int alphaVal = (int)(brightness * 180);

            int activeGlow = ((alphaVal / 4) << 24) | (activeEnergyColor & 0xFFFFFF);
            int activeCore = (alphaVal << 24) | (coreEnergyColor & 0xFFFFFF);

            int finalGlow = blendColors(0x00000000 | (activeEnergyColor & 0xFFFFFF), activeGlow, energyLevel);
            int finalCore = blendColors(idleRuneColor, activeCore, energyLevel);

            if (energyLevel > 0.01f) {
                graphics.text(this.font, rune.symbol, x + rune.x - 1, y + rune.y - 1, finalGlow, false);
                graphics.text(this.font, rune.symbol, x + rune.x + 1, y + rune.y + 1, finalGlow, false);
            }
            graphics.text(this.font, rune.symbol, x + rune.x, y + rune.y, finalCore, false);
        }


        float baseSpeed = pTime * 0.4f;
        float yaw = baseSpeed + spin * spin * 6.5f + overloadFactor * 35.0f;
        float pitch = baseSpeed * 0.6f + spin * spin * 3.5f + overloadFactor * 20.0f;
        float roll = baseSpeed * 0.3f + overloadFactor * 15.0f;

        float tiltYaw = currentPullX * 0.45f;
        float tiltPitch = currentPullY * 0.45f;

        int gyroColor = blendColors(0xFF484D52, blendColors(0xFFcba0ff, 0xFFff80ff, overloadFactor), energyLevel);
        draw3DGyroscopeRing(graphics, cx, cy, 22.0f, yaw + tiltYaw, pitch + tiltPitch, roll, gyroColor, surge + overloadFactor * 2.0f, energyLevel);
        draw3DGyroscopeRing(graphics, cx, cy, 18.0f, yaw + tiltYaw + (float)Math.PI/2, roll, pitch + tiltPitch, gyroColor, surge + overloadFactor * 2.0f, energyLevel);
        draw3DGyroscopeRing(graphics, cx, cy, 14.0f, roll, pitch + tiltPitch + (float)Math.PI/2, yaw + tiltYaw, gyroColor, surge + overloadFactor * 2.0f, energyLevel);


        for (int[] b : btnLayout) {
            int bx = cx + b[0] - btnSize / 2;
            int by = cy + b[1] - btnSize / 2;
            int dirIdx = b[2];

            boolean hover = mouseX >= bx && mouseX < bx + btnSize && mouseY >= by && mouseY < by + btnSize;
            boolean selected = selectedDirection == dirIdx;

            int btnBg = selected ? 0xFF757A80 : (hover ? 0xFF565C64 : 0xFF484D52);
            
            int cx_btn = bx + btnSize / 2;
            int cy_btn = by + btnSize / 2;
            
            for (int r = 0; r < 8; r++) {
                int w = 8 - r;
                graphics.fill(cx_btn - w, cy_btn - r, cx_btn + w + 1, cy_btn - r + 1, btnBg);
                graphics.fill(cx_btn - w, cy_btn + r, cx_btn + w + 1, cy_btn + r + 1, btnBg);
            }
            

            int activeBtnOutline = selected ? 0xFFDFD6EB : (hover ? 0xFF9CA2A9 : 0xFF232629);
            int finalBtnOutline = blendColors(0xFF232629, activeBtnOutline, (selected || hover) ? energyLevel : 1.0f);
            
            drawThickSegment(graphics, cx_btn, cy_btn - 8, cx_btn + 8, cy_btn, 1, finalBtnOutline);
            drawThickSegment(graphics, cx_btn + 8, cy_btn, cx_btn, cy_btn + 8, 1, finalBtnOutline);
            drawThickSegment(graphics, cx_btn, cy_btn + 8, cx_btn - 8, cy_btn, 1, finalBtnOutline);
            drawThickSegment(graphics, cx_btn - 8, cy_btn, cx_btn, cy_btn - 8, 1, finalBtnOutline);


            String runeSymbol = switch (dirIdx) {
                case 0 -> "ᛏ";
                case 1 -> "ᛦ";
                case 2 -> "ᚲ";
                case 3 -> "ᚦ";
                default -> "";
            };
            
            int activeRuneCol = selected ? 0xFFDFD6EB : (hover ? 0xFFA89CB8 : 0xFF232629);
            int finalRuneCol = blendColors(0xFF232629, activeRuneCol, (selected || hover) ? energyLevel : 0.0f);


            if (hover && energyLevel > 0.01f) {
                int glowCol = ((int)(40 * energyLevel) << 24) | 0xa89cb8;
                graphics.text(this.font, runeSymbol, cx_btn - 3, cy_btn - 5, glowCol, false);
                graphics.text(this.font, runeSymbol, cx_btn - 3, cy_btn - 3, glowCol, false);
            }
            graphics.text(this.font, runeSymbol, cx_btn - 3, cy_btn - 4, finalRuneCol, false);


        }


        int slotX = cx - 9;
        int slotY = cy - 9;
        boolean hoverSlot = mouseX >= slotX && mouseX < slotX + 18 && mouseY >= slotY && mouseY < slotY + 18;
        int targetSlotOutline = hoverSlot ? 0xCCDFD6EB : 0x88A89CB8;

        graphics.pose().pushMatrix();
        graphics.pose().translate(cx, cy);
        graphics.pose().rotate(hoverSpin);
        int finalSlotOutline = blendColors(0xFF232629, targetSlotOutline, energyLevel);
        graphics.outline(-10, -10, 20, 20, finalSlotOutline);
        graphics.pose().popMatrix();

        for (MagicSpark s : sparks) {
            int px = x + (int)s.x;
            int py = y + (int)s.y;
            if (px < x + 2 || px >= x + BG_WIDTH - 2 || py < y + 2 || py >= y + 140) continue;
            float life = 1.0f - ((float)s.age / s.maxAge);
            int alpha = (int)(life * 120 * energyLevel);
            int color = (alpha << 24) | 0xDFD6EB;
            int size = Math.max(1, (int)(s.size * life * 2.0f));
            graphics.fill(px - size / 2, py - size / 2, px - size / 2 + size, py - size / 2 + size, color);
        }


    }

    @Override
    public void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
    }

    private int blendColors(int col1, int col2, float ratio) {
        int a1 = (col1 >> 24) & 0xFF;
        int r1 = (col1 >> 16) & 0xFF;
        int g1 = (col1 >> 8) & 0xFF;
        int b1 = col1 & 0xFF;

        int a2 = (col2 >> 24) & 0xFF;
        int r2 = (col2 >> 16) & 0xFF;
        int g2 = (col2 >> 8) & 0xFF;
        int b2 = col2 & 0xFF;

        int a = (int)(a1 + (a2 - a1) * ratio);
        int r = (int)(r1 + (r2 - r1) * ratio);
        int g = (int)(g1 + (g2 - g1) * ratio);
        int b = (int)(b1 + (b2 - b1) * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }


    private void drawCircle(GuiGraphicsExtractor graphics, int cx, int cy, float radius, int thickness, int baseColor, float pTime, float energy, float pullX, float pullY) {
        int numPoints = energy > 0.1f ? 48 : 32;
        int prevX = 0, prevY = 0;
        boolean first = true;
        for (int i = 0; i <= numPoints; i++) {
            double angle = i * (2.0 * Math.PI / numPoints);
            
            float dot = (float)(Math.cos(angle) * pullX + Math.sin(angle) * pullY);
            

            float magnetize = (float)Math.pow(Math.max(0.0f, dot), 3.0f) * energy * 7.5f;
            
            float warp = 0.0f;
            if (energy > 0.0f && dot > 0.0f) {
                warp = (float)Math.pow(dot, 2.0f) * energy * 3.0f * (float)Math.sin(pTime * 7.5f - radius * 0.12f + angle * 2.0f);
            }
            
            float finalRadius = radius + magnetize + warp;
            int sx = cx + (int) Math.round(finalRadius * Math.cos(angle));
            int sy = cy + (int) Math.round(finalRadius * Math.sin(angle));
            
            int color = baseColor;
            if (energy > 0.0f) {
                float wave = 0.5f + 0.5f * (float)Math.sin(angle * 2.0 - pTime * 4.0);
                float gravityGlow = 0.5f + 0.5f * dot;
                

                wave = wave * (1.0f - energy * 0.6f) + gravityGlow * energy * 1.4f;

                int targetCol = ((int)(60 + 80 * wave) << 24) | 0xa89cb8;
                color = blendColors(0xFF232629, targetCol, energy);
            }
            
            if (!first) {
                drawThickSegment(graphics, prevX, prevY, sx, sy, thickness, color);
            } else {
                first = false;
            }
            prevX = sx;
            prevY = sy;
        }
    }


    private void drawThickSegment(GuiGraphicsExtractor graphics, int x1, int y1, int x2, int y2, int thickness, int color) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float L = (float) Math.sqrt(dx * dx + dy * dy);
        if (L < 0.01f) {
            drawPixelBlock(graphics, x1, y1, thickness, color);
            return;
        }
        
        float angle = (float) Math.atan2(dy, dx);
        
        graphics.pose().pushMatrix();
        graphics.pose().translate(x1, y1);
        graphics.pose().rotate(angle);
        
        float halfW = thickness / 2.0f;
        graphics.fill(0, (int)Math.floor(-halfW), (int)Math.ceil(L), (int)Math.ceil(halfW), color);
        
        graphics.pose().popMatrix();
    }

    private void drawPixelBlock(GuiGraphicsExtractor graphics, int px, int py, int thickness, int color) {
        if (thickness <= 1) {
            graphics.fill(px, py, px + 1, py + 1, color);
        } else if (thickness == 2) {
            graphics.fill(px, py, px + 2, py + 2, color);
        } else {
            graphics.fill(px - 1, py - 1, px + 2, py + 2, color);
        }
    }


    private void draw3DGyroscopeRing(GuiGraphicsExtractor graphics, int cx, int cy, float radius, float yaw, float pitch, float roll, int color, float surge, float energy) {
        int numPoints = 24;
        int prevX = 0, prevY = 0;
        boolean first = true;

        for (int i = 0; i <= numPoints; i++) {
            double angle = i * (2.0 * Math.PI / numPoints);

            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            double z = 0.0;


            double cosR = Math.cos(roll);
            double sinR = Math.sin(roll);
            double rx = x * cosR - y * sinR;
            double ry = x * sinR + y * cosR;
            double rz = z;


            double cosP = Math.cos(pitch);
            double sinP = Math.sin(pitch);
            double px = rx;
            double py = ry * cosP - rz * sinP;
            double pz = ry * sinP + rz * cosP;


            double cosY = Math.cos(yaw);
            double sinY = Math.sin(yaw);
            double yx = px * cosY + pz * sinY;
            double yy = py;
            double yz = -px * sinY + pz * cosY;


            float depth = (float)((yz + radius) / (2.0 * radius));
            int activeAlpha = (int)(40 + 80 * depth + surge * 80);
            int idleAlpha = (int)(30 + 40 * depth);
            int alpha = (int)(idleAlpha + (activeAlpha - idleAlpha) * energy);
            alpha = Math.min(160, Math.max(15, alpha));

            int sx = cx + (int)yx;
            int sy = cy + (int)yy;

            if (!first) {
                drawThickSegment(graphics, prevX, prevY, sx, sy, 1, (alpha << 24) | (color & 0xFFFFFF));
            } else {
                first = false;
            }

            prevX = sx;
            prevY = sy;
        }
    }

    private void drawArrowShape(GuiGraphicsExtractor graphics, int cx, int cy, int direction, int color) {
        switch (direction) {
            case 0 -> {
                graphics.fill(cx - 1, cy - 3, cx + 2, cy + 3, color);
                graphics.fill(cx - 2, cy - 1, cx + 3, cy, color);
                graphics.fill(cx - 3, cy + 1, cx + 4, cy + 2, color);
            }
            case 1 -> {
                graphics.fill(cx - 1, cy - 3, cx + 2, cy + 3, color);
                graphics.fill(cx - 2, cy + 1, cx + 3, cy + 2, color);
                graphics.fill(cx - 3, cy - 1, cx + 4, cy, color);
            }
            case 2 -> {
                graphics.fill(cx - 3, cy - 1, cx + 3, cy + 2, color);
                graphics.fill(cx - 1, cy - 2, cx, cy + 3, color);
                graphics.fill(cx + 1, cy - 3, cx + 2, cy + 4, color);
            }
            case 3 -> {
                graphics.fill(cx - 3, cy - 1, cx + 3, cy + 2, color);
                graphics.fill(cx + 1, cy - 2, cx + 2, cy + 3, color);
                graphics.fill(cx - 1, cy - 3, cx, cy + 4, color);
            }
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        if (event.button() == 0) {
            int x = (this.width - BG_WIDTH) / 2;
            int y = (this.height - BG_HEIGHT) / 2;
            int cx = x + BG_WIDTH / 2;
            int cy = y + 72;

            int btnSize = 16;
            int[][] btnLayout = {
                {0, -48, 0},
                {0, 48, 1},
                {-48, 0, 2},
                {48, 0, 3}
            };

            for (int[] b : btnLayout) {
                int bx = cx + b[0] - btnSize / 2;
                int by = cy + b[1] - btnSize / 2;
                int dirIdx = b[2];

                if (event.x() >= bx && event.x() < bx + btnSize && event.y() >= by && event.y() < by + btnSize) {
                    if (overloadTicks > 0) return true;
                    if (wasAmethystInSlot && (this.menu.slots.get(0).getItem().is(net.minecraft.world.item.Items.AMETHYST_SHARD) || this.menu.slots.get(0).getItem().is(net.nostalgia.item.ModItems.CHARGED_AMETHYST))) {
                        overloadTicks = 40;
                        overloadingDirection = dirIdx;
                        glowSurge = 2.0f;
                        spinTimer = 1.0f;
                        if (this.minecraft != null && this.minecraft.player != null) {
                            this.minecraft.player.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, 0.7f, 0.6f);
                            this.minecraft.player.playSound(SoundEvents.CONDUIT_ACTIVATE, 0.9f, 0.85f);
                        }
                    } else if (!wasAmethystInSlot) {

                        if (this.minecraft != null && this.minecraft.player != null) {
                            this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.6f, 0.6f);
                        }
                    }
                    return true;
                }
            }
        }
        return super.mouseClicked(event, isDoubleClick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
