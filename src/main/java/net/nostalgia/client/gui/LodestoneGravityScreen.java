package net.nostalgia.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.nostalgia.inventory.LodestoneGravityMenu;
import net.nostalgia.item.ChargedAmethystItem;
import net.nostalgia.item.ModItems;
import net.nostalgia.mixin.alpha.AbstractContainerScreenAccessor;
import net.nostalgia.network.C2SProgramAmethystPayload;

public class LodestoneGravityScreen extends AbstractContainerScreen<LodestoneGravityMenu> {
  private static final int BG_WIDTH = 180;
  private static final int BG_HEIGHT = 176;
  private int selectedDirection = -1;
  private boolean wasAmethystInSlot = false;
  private float energyLevel = 0.0F;
  private int overloadTicks = -1;
  private int overloadingDirection = -1;
  private float currentPullX = 0.0F;
  private float currentPullY = 0.0F;
  private float hoverSpin = 0.0F;
  private boolean isHoveringButton = false;
  private int ambientSoundTimer = 0;
  private int hoverSoundTimer = 0;
  private float floatTimer = 0.0F;
  private float spinTimer = 0.0F;
  private float glowSurge = 0.0F;
  private float programFlash = 0.0F;
  private final List<LodestoneGravityScreen.MysticRune> scatteredRunes = new ArrayList<>();
  private final List<LodestoneGravityScreen.MagicSpark> sparks = new ArrayList<>();

  public LodestoneGravityScreen(LodestoneGravityMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    ((AbstractContainerScreenAccessor)this).setImageWidth(180);
    ((AbstractContainerScreenAccessor)this).setImageHeight(176);
    this.inventoryLabelY = 1000;
    this.titleLabelX = 8;
    this.titleLabelY = 4;
    String[] glyphs = new String[]{
      "\u16a6",
      "\u16a2",
      "\u16a6",
      "\u16a8",
      "\u16b1",
      "\u16b2",
      "\u16b7",
      "\u16b9",
      "\u16ba",
      "\u16be",
      "\u16c1",
      "\u16c3",
      "\u16c7",
      "\u16c8",
      "\u16c9",
      "\u16ca",
      "\u16cf",
      "\u16d2"
    };
    Random r = new Random(888L);
    int[][] positions = new int[][]{
      {16, 36},
      {154, 36},
      {18, 92},
      {152, 92},
      {40, 20},
      {128, 20},
      {40, 108},
      {128, 108},
      {12, 64},
      {158, 64},
      {12, 18},
      {168, 18},
      {12, 118},
      {168, 118},
      {80, 16},
      {100, 16}
    };

    for (int i = 0; i < positions.length; i++) {
      LodestoneGravityScreen.MysticRune rune = new LodestoneGravityScreen.MysticRune();
      rune.x = positions[i][0];
      rune.y = positions[i][1];
      rune.symbol = glyphs[r.nextInt(glyphs.length)];
      rune.basePhase = r.nextFloat() * 10.0F;
      rune.breatheSpeed = 0.02F;
      this.scatteredRunes.add(rune);
    }
  }

  public void containerTick() {
    super.containerTick();
    this.floatTimer += 0.035F;
    if (this.overloadTicks > 0) {
      this.overloadTicks--;
      this.glowSurge = 2.0F;
      Random r = new Random();
      int x = (this.width - 180) / 2;
      int y = (this.height - 176) / 2;
      int cx = x + 90;
      int cy = y + 72;

      for (int i = 0; i < 4; i++) {
        LodestoneGravityScreen.MagicSpark spark = new LodestoneGravityScreen.MagicSpark();
        spark.x = cx;
        spark.y = cy;
        double angle = r.nextDouble() * 2.0 * 3.141592653589793;
        double speed = 1.0 + r.nextDouble() * 4.0;
        spark.vx = (float)(Math.cos(angle) * speed);
        spark.vy = (float)(Math.sin(angle) * speed);
        spark.age = 0;
        spark.maxAge = 12 + r.nextInt(15);
        spark.size = 1.0F + r.nextFloat() * 2.0F;
        this.sparks.add(spark);
      }

      if (this.overloadTicks == 0) {
        this.overloadTicks = -1;
        ClientPlayNetworking.send(new C2SProgramAmethystPayload(this.overloadingDirection));
        this.selectedDirection = this.overloadingDirection;
        this.programFlash = 1.0F;
        if (this.minecraft != null && this.minecraft.player != null) {
          this.minecraft.player.playSound(SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, 0.9F, 1.25F);
          this.minecraft.player.playSound(SoundEvents.CONDUIT_ACTIVATE, 0.7F, 1.1F);
          this.minecraft.player.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 1.0F, 1.0F);
        }
      }
    }

    if (this.spinTimer > 0.0F) {
      this.spinTimer = Math.max(0.0F, this.spinTimer - 0.03F);
    }

    if (this.glowSurge > 0.0F) {
      this.glowSurge = Math.max(0.0F, this.glowSurge - 0.025F);
    }

    if (this.programFlash > 0.0F) {
      this.programFlash = Math.max(0.0F, this.programFlash - 0.04F);
    }

    this.sparks.removeIf(sx -> {
      sx.x = sx.x + sx.vx;
      sx.y = sx.y + sx.vy;
      sx.age++;
      return sx.age >= sx.maxAge;
    });
    ItemStack slotStack = ((Slot)((LodestoneGravityMenu)this.menu).slots.get(0)).getItem();
    boolean hasAmethyst = !slotStack.isEmpty();
    boolean isChargedAmethyst = hasAmethyst && slotStack.is(ModItems.CHARGED_AMETHYST);
    if (hasAmethyst && !this.wasAmethystInSlot) {
      this.wasAmethystInSlot = true;
      this.spinTimer = 1.0F;
      this.glowSurge = 1.0F;
      if (this.minecraft != null && this.minecraft.player != null) {
        this.minecraft.player.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, 0.85F, 0.85F);
        this.minecraft.player.playSound(SoundEvents.SCULK_CATALYST_BLOOM, 0.75F, 0.85F);
      }
    } else if (!hasAmethyst) {
      this.wasAmethystInSlot = false;
      this.selectedDirection = -1;
    }

    if (isChargedAmethyst) {
      String dirStr = ChargedAmethystItem.getDirection(slotStack);

      int dirIdx = switch (dirStr) {
        case "up" -> 0;
        case "down" -> 1;
        case "left" -> 2;
        case "right" -> 3;
        default -> -1;
      };
      if (dirIdx != -1) {
        this.selectedDirection = dirIdx;
      }
    }

    if (hasAmethyst) {
      this.energyLevel = Math.min(1.0F, this.energyLevel + 0.05F);
    } else {
      this.energyLevel = Math.max(0.0F, this.energyLevel - 0.08F);
    }

    if (this.energyLevel > 0.05F) {
      this.ambientSoundTimer--;
      if (this.ambientSoundTimer <= 0) {
        if (this.minecraft != null && this.minecraft.player != null) {
          this.minecraft.player.playSound(SoundEvents.RESPAWN_ANCHOR_AMBIENT, 0.28F * this.energyLevel, 0.75F);
        }

        this.ambientSoundTimer = 80;
      }
    } else {
      this.ambientSoundTimer = 0;
    }

    if (this.isHoveringButton && this.wasAmethystInSlot) {
      this.hoverSoundTimer--;
      if (this.hoverSoundTimer <= 0) {
        if (this.minecraft != null && this.minecraft.player != null) {
          this.minecraft.player.playSound(SoundEvents.VAULT_AMBIENT, 0.24F * this.energyLevel, 0.65F + (float)Math.random() * 0.1F);
          this.minecraft.player.playSound(SoundEvents.CONDUIT_AMBIENT, 0.15F * this.energyLevel, 0.8F);
        }

        this.hoverSoundTimer = 15 + (int)(Math.random() * 10.0);
      }
    } else {
      this.hoverSoundTimer = 0;
    }

    if (this.wasAmethystInSlot && Math.random() < 0.25) {
      LodestoneGravityScreen.MagicSpark s = new LodestoneGravityScreen.MagicSpark();
      s.x = 90.0F;
      s.y = 72.0F;
      s.vx = (float)(Math.random() - 0.5) * 0.7F;
      s.vy = (float)(Math.random() - 0.5) * 0.7F;
      s.age = 0;
      s.maxAge = 12 + (int)(Math.random() * 12.0);
      s.size = 0.6F + (float)Math.random() * 0.8F;
      this.sparks.add(s);
    }
  }

  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
    int x = (this.width - 180) / 2;
    int y = (this.height - 176) / 2;
    int cx = x + 90;
    int cy = y + 72;
    float pTime = this.floatTimer + partialTick;
    float surge = this.glowSurge > 0.0F ? this.glowSurge - 0.025F * partialTick : 0.0F;
    float spin = this.spinTimer > 0.0F ? this.spinTimer - 0.03F * partialTick : 0.0F;
    if (this.programFlash > 0.0F) {
      float var10000 = this.programFlash - 0.04F * partialTick;
    } else {
      float var123 = 0.0F;
    }

    float breathe = 0.35F + 0.65F * (float)Math.sin(pTime * 0.9F);
    float overloadFactor = 0.0F;
    int activeEnergyColor = -5727048;
    int coreEnergyColor = -2107669;
    if (this.overloadTicks > 0) {
      float progress = this.overloadTicks / 40.0F;
      overloadFactor = (float)Math.sin(progress * 3.141592653589793);
      activeEnergyColor = this.blendColors(-5727048, -3638017, overloadFactor);
      coreEnergyColor = this.blendColors(-2107669, -2051841, overloadFactor);
    }

    float pulseFactor = breathe * 0.5F + surge * 1.5F + overloadFactor * 3.5F;
    pulseFactor = Math.min(1.0F + overloadFactor * 2.0F, Math.max(0.1F, pulseFactor));
    float hoverX = 0.0F;
    float hoverY = 0.0F;
    boolean hoveringAny = false;
    int hoveredDir = -1;
    int btnSize = 16;
    int[][] btnLayout = new int[][]{{0, -48, 0}, {0, 48, 1}, {-48, 0, 2}, {48, 0, 3}};

    label250:
    for (int[] b : btnLayout) {
      int bx = cx + b[0] - btnSize / 2;
      int by = cy + b[1] - btnSize / 2;
      int dirIdx = b[2];
      if (mouseX >= bx && mouseX < bx + btnSize && mouseY >= by && mouseY < by + btnSize) {
        hoveringAny = true;
        switch (dirIdx) {
          case 0:
            hoverX = 0.0F;
            hoverY = -1.0F;
            break label250;
          case 1:
            hoverX = 0.0F;
            hoverY = 1.0F;
            break label250;
          case 2:
            hoverX = -1.0F;
            hoverY = 0.0F;
            break label250;
          case 3:
            hoverX = 1.0F;
            hoverY = 0.0F;
          default:
            break label250;
        }
      }
    }

    float lerpSpeed = hoveringAny ? 0.15F : 0.007F;
    this.currentPullX = this.currentPullX + (hoverX - this.currentPullX) * lerpSpeed;
    this.currentPullY = this.currentPullY + (hoverY - this.currentPullY) * lerpSpeed;
    if (hoveringAny && this.wasAmethystInSlot) {
      this.hoverSpin += 0.06F;
    } else {
      this.hoverSpin = this.hoverSpin + (0.0F - this.hoverSpin) * 0.008F;
    }

    this.isHoveringButton = hoveringAny;
    graphics.fill(x, y, x + 180, y + 142, -10723225);
    graphics.outline(x, y, 180, 142, -14473687);
    graphics.outline(x + 2, y + 2, 176, 138, -7564646);
    Random stoneRand = new Random(777L);

    for (int i = 0; i < 35; i++) {
      int rx = stoneRand.nextInt(180);
      int ry = stoneRand.nextInt(142);
      int rw = stoneRand.nextInt(12) + 4;
      int rh = stoneRand.nextInt(6) + 2;
      int rcol = stoneRand.nextBoolean() ? 445420186 : 505620009;
      graphics.fill(x + rx, y + ry, x + rx + rw, y + ry + rh, rcol);
    }

    graphics.fill(x, y + 142, x + 180, y + 176, -12038830);
    graphics.outline(x, y + 142, 180, 34, -14473687);
    graphics.outline(x + 2, y + 144, 176, 30, -9078144);
    graphics.outline(x + 8, y + 146, 164, 20, -14473687);

    for (int col = 0; col < 9; col++) {
      int slotX = x + 9 + col * 18;
      int slotY = y + 147;
      graphics.fill(slotX, slotY, slotX + 16, slotY + 16, -14473687);
      graphics.outline(slotX - 1, slotY - 1, 18, 18, -10723225);
    }

    int ringColor = this.blendColors(-14473687, activeEnergyColor, this.energyLevel);
    this.drawCircle(graphics, cx, cy, 54.0F, 2, ringColor, pTime, this.energyLevel, this.currentPullX, this.currentPullY);
    this.drawCircle(graphics, cx, cy, 50.0F, 2, ringColor, pTime, this.energyLevel, this.currentPullX, this.currentPullY);
    this.drawCircle(graphics, cx, cy, 36.0F, 2, ringColor, pTime, this.energyLevel, this.currentPullX, this.currentPullY);
    this.drawCircle(graphics, cx, cy, 32.0F, 2, ringColor, pTime, this.energyLevel, this.currentPullX, this.currentPullY);
    int outerSquareColor = this.blendColors(-14473687, activeEnergyColor & 16777215 | -872415232, this.energyLevel);
    int innerSquareColor = this.blendColors(-9078144, coreEnergyColor & 16777215 | -587202560, this.energyLevel);
    graphics.outline(cx - 14, cy - 14, 28, 28, outerSquareColor);
    graphics.outline(cx - 15, cy - 15, 30, 30, innerSquareColor);
    double[] angles = new double[]{
      0.0, 0.7853981633974483, 1.5707963267948966, 2.356194490192345, 3.141592653589793, 3.9269908169872414, 4.71238898038469, 5.497787143782138
    };

    for (double angle : angles) {
      int x1 = cx + (int)Math.round(15.0 * Math.cos(angle));
      int y1 = cy + (int)Math.round(15.0 * Math.sin(angle));
      float dot = (float)(Math.cos(angle) * this.currentPullX + Math.sin(angle) * this.currentPullY);
      float magnetize = (float)Math.pow(Math.max(0.0F, dot), 3.0) * this.energyLevel * 7.5F;
      float warp = 0.0F;
      if (this.energyLevel > 0.0F && dot > 0.0F) {
        warp = (float)Math.pow(dot, 2.0) * this.energyLevel * 3.0F * (float)Math.sin(pTime * 7.5F - 6.0F + angle * 2.0);
      }

      float outerR = 50.0F + magnetize + warp;
      int x2 = cx + (int)Math.round(outerR * Math.cos(angle));
      int y2 = cy + (int)Math.round(outerR * Math.sin(angle));
      if (this.energyLevel > 0.0F) {
        this.drawThickSegment(graphics, x1, y1, x2, y2, 2, this.blendColors(-14473687, activeEnergyColor & 16777215 | 855638016, this.energyLevel));
        int steps = 18;

        for (int j = 0; j < steps; j++) {
          float t1 = (float)j / steps;
          float t2 = (float)(j + 1) / steps;
          int px1 = (int)(x1 + (x2 - x1) * t1);
          int py1 = (int)(y1 + (y2 - y1) * t1);
          int px2 = (int)(x1 + (x2 - x1) * t2);
          int py2 = (int)(y1 + (y2 - y1) * t2);
          float wave = 0.5F + 0.5F * (float)Math.sin(t1 * 5.0 - pTime * 8.0);
          float intensityFactor = 1.0F + dot * 0.7F * this.energyLevel;
          int alpha = (int)((50.0F + 40.0F * wave) * this.energyLevel * intensityFactor);
          int col = alpha << 24 | coreEnergyColor & 16777215;
          this.drawThickSegment(graphics, px1, py1, px2, py2, 2, col);
        }
      } else {
        this.drawThickSegment(graphics, x1, y1, x2, y2, 2, ringColor);
      }
    }

    for (LodestoneGravityScreen.MysticRune rune : this.scatteredRunes) {
      int idleRuneColor = -2010962391;
      float rBreathe = 0.35F + 0.65F * (float)Math.sin(rune.basePhase + pTime * 1.8F);
      float brightness = rBreathe * 0.35F + surge * 0.55F;
      brightness = Math.min(0.8F, Math.max(0.15F, brightness));
      int alphaVal = (int)(brightness * 180.0F);
      int activeGlow = alphaVal / 4 << 24 | activeEnergyColor & 16777215;
      int activeCore = alphaVal << 24 | coreEnergyColor & 16777215;
      int finalGlow = this.blendColors(0 | activeEnergyColor & 16777215, activeGlow, this.energyLevel);
      int finalCore = this.blendColors(idleRuneColor, activeCore, this.energyLevel);
      if (this.energyLevel > 0.01F) {
        graphics.text(this.font, rune.symbol, x + rune.x - 1, y + rune.y - 1, finalGlow, false);
        graphics.text(this.font, rune.symbol, x + rune.x + 1, y + rune.y + 1, finalGlow, false);
      }

      graphics.text(this.font, rune.symbol, x + rune.x, y + rune.y, finalCore, false);
    }

    float baseSpeed = pTime * 0.4F;
    float yaw = baseSpeed + spin * spin * 6.5F + overloadFactor * 35.0F;
    float pitch = baseSpeed * 0.6F + spin * spin * 3.5F + overloadFactor * 20.0F;
    float roll = baseSpeed * 0.3F + overloadFactor * 15.0F;
    float tiltYaw = this.currentPullX * 0.45F;
    float tiltPitch = this.currentPullY * 0.45F;
    int gyroColor = this.blendColors(-12038830, this.blendColors(-3432193, -32513, overloadFactor), this.energyLevel);
    this.draw3DGyroscopeRing(graphics, cx, cy, 22.0F, yaw + tiltYaw, pitch + tiltPitch, roll, gyroColor, surge + overloadFactor * 2.0F, this.energyLevel);
    this.draw3DGyroscopeRing(
      graphics, cx, cy, 18.0F, yaw + tiltYaw + 1.5707964F, roll, pitch + tiltPitch, gyroColor, surge + overloadFactor * 2.0F, this.energyLevel
    );
    this.draw3DGyroscopeRing(
      graphics, cx, cy, 14.0F, roll, pitch + tiltPitch + 1.5707964F, yaw + tiltYaw, gyroColor, surge + overloadFactor * 2.0F, this.energyLevel
    );

    for (int[] bx : btnLayout) {
      int bxx = cx + bx[0] - btnSize / 2;
      int by = cy + bx[1] - btnSize / 2;
      int dirIdx = bx[2];
      boolean hover = mouseX >= bxx && mouseX < bxx + btnSize && mouseY >= by && mouseY < by + btnSize;
      boolean selected = this.selectedDirection == dirIdx;
      int btnBg = selected ? -9078144 : (hover ? -11117468 : -12038830);
      int cx_btn = bxx + btnSize / 2;
      int cy_btn = by + btnSize / 2;

      for (int r = 0; r < 8; r++) {
        int w = 8 - r;
        graphics.fill(cx_btn - w, cy_btn - r, cx_btn + w + 1, cy_btn - r + 1, btnBg);
        graphics.fill(cx_btn - w, cy_btn + r, cx_btn + w + 1, cy_btn + r + 1, btnBg);
      }

      int activeBtnOutline = selected ? -2107669 : (hover ? -6511959 : -14473687);
      int finalBtnOutline = this.blendColors(-14473687, activeBtnOutline, !selected && !hover ? 1.0F : this.energyLevel);
      this.drawThickSegment(graphics, cx_btn, cy_btn - 8, cx_btn + 8, cy_btn, 1, finalBtnOutline);
      this.drawThickSegment(graphics, cx_btn + 8, cy_btn, cx_btn, cy_btn + 8, 1, finalBtnOutline);
      this.drawThickSegment(graphics, cx_btn, cy_btn + 8, cx_btn - 8, cy_btn, 1, finalBtnOutline);
      this.drawThickSegment(graphics, cx_btn - 8, cy_btn, cx_btn, cy_btn - 8, 1, finalBtnOutline);

      String runeSymbol = switch (dirIdx) {
        case 0 -> "\u16cf";
        case 1 -> "\u16e6";
        case 2 -> "\u16b2";
        case 3 -> "\u16a6";
        default -> "";
      };
      int activeRuneCol = selected ? -2107669 : (hover ? -5727048 : -14473687);
      int finalRuneCol = this.blendColors(-14473687, activeRuneCol, !selected && !hover ? 0.0F : this.energyLevel);
      if (hover && this.energyLevel > 0.01F) {
        int glowCol = (int)(40.0F * this.energyLevel) << 24 | 11050168;
        graphics.text(this.font, runeSymbol, cx_btn - 3, cy_btn - 5, glowCol, false);
        graphics.text(this.font, runeSymbol, cx_btn - 3, cy_btn - 3, glowCol, false);
      }

      graphics.text(this.font, runeSymbol, cx_btn - 3, cy_btn - 4, finalRuneCol, false);
    }

    int slotX = cx - 9;
    int slotY = cy - 9;
    boolean hoverSlot = mouseX >= slotX && mouseX < slotX + 18 && mouseY >= slotY && mouseY < slotY + 18;
    int targetSlotOutline = hoverSlot ? -857745685 : -2002215752;
    graphics.pose().pushMatrix();
    graphics.pose().translate(cx, cy);
    graphics.pose().rotate(this.hoverSpin);
    int finalSlotOutline = this.blendColors(-14473687, targetSlotOutline, this.energyLevel);
    graphics.outline(-10, -10, 20, 20, finalSlotOutline);
    graphics.pose().popMatrix();

    for (LodestoneGravityScreen.MagicSpark s : this.sparks) {
      int px = x + (int)s.x;
      int py = y + (int)s.y;
      if (px >= x + 2 && px < x + 180 - 2 && py >= y + 2 && py < y + 140) {
        float life = 1.0F - (float)s.age / s.maxAge;
        int alpha = (int)(life * 120.0F * this.energyLevel);
        int color = alpha << 24 | 14669547;
        int size = Math.max(1, (int)(s.size * life * 2.0F));
        graphics.fill(px - size / 2, py - size / 2, px - size / 2 + size, py - size / 2 + size, color);
      }
    }
  }

  public void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
  }

  private int blendColors(int col1, int col2, float ratio) {
    int a1 = col1 >> 24 & 0xFF;
    int r1 = col1 >> 16 & 0xFF;
    int g1 = col1 >> 8 & 0xFF;
    int b1 = col1 & 0xFF;
    int a2 = col2 >> 24 & 0xFF;
    int r2 = col2 >> 16 & 0xFF;
    int g2 = col2 >> 8 & 0xFF;
    int b2 = col2 & 0xFF;
    int a = (int)(a1 + (a2 - a1) * ratio);
    int r = (int)(r1 + (r2 - r1) * ratio);
    int g = (int)(g1 + (g2 - g1) * ratio);
    int b = (int)(b1 + (b2 - b1) * ratio);
    return a << 24 | r << 16 | g << 8 | b;
  }

  private void drawCircle(
    GuiGraphicsExtractor graphics, int cx, int cy, float radius, int thickness, int baseColor, float pTime, float energy, float pullX, float pullY
  ) {
    int numPoints = energy > 0.1F ? 48 : 32;
    int prevX = 0;
    int prevY = 0;
    boolean first = true;

    for (int i = 0; i <= numPoints; i++) {
      double angle = i * (6.283185307179586 / numPoints);
      float dot = (float)(Math.cos(angle) * pullX + Math.sin(angle) * pullY);
      float magnetize = (float)Math.pow(Math.max(0.0F, dot), 3.0) * energy * 7.5F;
      float warp = 0.0F;
      if (energy > 0.0F && dot > 0.0F) {
        warp = (float)Math.pow(dot, 2.0) * energy * 3.0F * (float)Math.sin(pTime * 7.5F - radius * 0.12F + angle * 2.0);
      }

      float finalRadius = radius + magnetize + warp;
      int sx = cx + (int)Math.round(finalRadius * Math.cos(angle));
      int sy = cy + (int)Math.round(finalRadius * Math.sin(angle));
      int color = baseColor;
      if (energy > 0.0F) {
        float wave = 0.5F + 0.5F * (float)Math.sin(angle * 2.0 - pTime * 4.0);
        float gravityGlow = 0.5F + 0.5F * dot;
        wave = wave * (1.0F - energy * 0.6F) + gravityGlow * energy * 1.4F;
        int targetCol = (int)(60.0F + 80.0F * wave) << 24 | 11050168;
        color = this.blendColors(-14473687, targetCol, energy);
      }

      if (!first) {
        this.drawThickSegment(graphics, prevX, prevY, sx, sy, thickness, color);
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
    float L = (float)Math.sqrt(dx * dx + dy * dy);
    if (L < 0.01F) {
      this.drawPixelBlock(graphics, x1, y1, thickness, color);
    } else {
      float angle = (float)Math.atan2(dy, dx);
      graphics.pose().pushMatrix();
      graphics.pose().translate(x1, y1);
      graphics.pose().rotate(angle);
      float halfW = thickness / 2.0F;
      graphics.fill(0, (int)Math.floor(-halfW), (int)Math.ceil(L), (int)Math.ceil(halfW), color);
      graphics.pose().popMatrix();
    }
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

  private void draw3DGyroscopeRing(
    GuiGraphicsExtractor graphics, int cx, int cy, float radius, float yaw, float pitch, float roll, int color, float surge, float energy
  ) {
    int numPoints = 24;
    int prevX = 0;
    int prevY = 0;
    boolean first = true;

    for (int i = 0; i <= numPoints; i++) {
      double angle = i * (6.283185307179586 / numPoints);
      double x = radius * Math.cos(angle);
      double y = radius * Math.sin(angle);
      double z = 0.0;
      double cosR = Math.cos(roll);
      double sinR = Math.sin(roll);
      double rx = x * cosR - y * sinR;
      double ry = x * sinR + y * cosR;
      double cosP = Math.cos(pitch);
      double sinP = Math.sin(pitch);
      double py = ry * cosP - z * sinP;
      double pz = ry * sinP + z * cosP;
      double cosY = Math.cos(yaw);
      double sinY = Math.sin(yaw);
      double yx = rx * cosY + pz * sinY;
      double yz = -rx * sinY + pz * cosY;
      float depth = (float)((yz + radius) / (2.0 * radius));
      int activeAlpha = (int)(40.0F + 80.0F * depth + surge * 80.0F);
      int idleAlpha = (int)(30.0F + 40.0F * depth);
      int alpha = (int)(idleAlpha + (activeAlpha - idleAlpha) * energy);
      alpha = Math.min(160, Math.max(15, alpha));
      int sx = cx + (int)yx;
      int sy = cy + (int)py;
      if (!first) {
        this.drawThickSegment(graphics, prevX, prevY, sx, sy, 1, alpha << 24 | color & 16777215);
      } else {
        first = false;
      }

      prevX = sx;
      prevY = sy;
    }
  }

  private void drawArrowShape(GuiGraphicsExtractor graphics, int cx, int cy, int direction, int color) {
    switch (direction) {
      case 0:
        graphics.fill(cx - 1, cy - 3, cx + 2, cy + 3, color);
        graphics.fill(cx - 2, cy - 1, cx + 3, cy, color);
        graphics.fill(cx - 3, cy + 1, cx + 4, cy + 2, color);
        break;
      case 1:
        graphics.fill(cx - 1, cy - 3, cx + 2, cy + 3, color);
        graphics.fill(cx - 2, cy + 1, cx + 3, cy + 2, color);
        graphics.fill(cx - 3, cy - 1, cx + 4, cy, color);
        break;
      case 2:
        graphics.fill(cx - 3, cy - 1, cx + 3, cy + 2, color);
        graphics.fill(cx - 1, cy - 2, cx, cy + 3, color);
        graphics.fill(cx + 1, cy - 3, cx + 2, cy + 4, color);
        break;
      case 3:
        graphics.fill(cx - 3, cy - 1, cx + 3, cy + 2, color);
        graphics.fill(cx + 1, cy - 2, cx + 2, cy + 3, color);
        graphics.fill(cx - 1, cy - 3, cx, cy + 4, color);
    }
  }

  public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
    if (event.button() == 0) {
      int x = (this.width - 180) / 2;
      int y = (this.height - 176) / 2;
      int cx = x + 90;
      int cy = y + 72;
      int btnSize = 16;
      int[][] btnLayout = new int[][]{{0, -48, 0}, {0, 48, 1}, {-48, 0, 2}, {48, 0, 3}};

      for (int[] b : btnLayout) {
        int bx = cx + b[0] - btnSize / 2;
        int by = cy + b[1] - btnSize / 2;
        int dirIdx = b[2];
        if (event.x() >= bx && event.x() < bx + btnSize && event.y() >= by && event.y() < by + btnSize) {
          if (this.overloadTicks > 0) {
            return true;
          }

          if (this.wasAmethystInSlot
            && (
              ((Slot)((LodestoneGravityMenu)this.menu).slots.get(0)).getItem().is(Items.AMETHYST_SHARD)
                || ((Slot)((LodestoneGravityMenu)this.menu).slots.get(0)).getItem().is(ModItems.CHARGED_AMETHYST)
            )) {
            this.overloadTicks = 40;
            this.overloadingDirection = dirIdx;
            this.glowSurge = 2.0F;
            this.spinTimer = 1.0F;
            if (this.minecraft != null && this.minecraft.player != null) {
              this.minecraft.player.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, 0.7F, 0.6F);
              this.minecraft.player.playSound(SoundEvents.CONDUIT_ACTIVATE, 0.9F, 0.85F);
            }
          } else if (!this.wasAmethystInSlot && this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.playSound((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), 0.6F, 0.6F);
          }

          return true;
        }
      }
    }

    return super.mouseClicked(event, isDoubleClick);
  }

  public boolean isPauseScreen() {
    return false;
  }

  private static class MagicSpark {
    float x;
    float y;
    float vx;
    float vy;
    int age;
    int maxAge;
    float size;

    private MagicSpark() {
    }
  }

  private static class MysticRune {
    int x;
    int y;
    String symbol;
    float basePhase;
    float breatheSpeed;

    private MysticRune() {
    }
  }
}
