package net.nostalgia.client.gui.hologram3d;

import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.client.gui.hologram3d.render.IsometricTileRenderer;
import net.nostalgia.client.gui.hologram3d.sprite.BlockSpriteProvider;
import net.nostalgia.client.gui.hologram3d.sprite.IsometricSprite;

public class HologramMiniRenderer {
  private static final int TEX_W = 160;
  private static final int TEX_H = 62;
  private static final int DEFAULT_BG_COLOR = -15727344;
  private int bgColor = -15727344;
  private int bgFillColor = -16448240;
  private static final int WIREFRAME_COLOR = -30720;
  private static final int WIREFRAME_FILL = 822044160;
  private HologramTerrainData data;
  private final HologramCamera camera;
  private BlockPos selectedLanding;
  private long markerSpawnTime;
  private boolean interactiveMode;
  private boolean dragging;
  private double lastDragX;
  private double lastDragY;
  private double totalDragDist;
  private HologramMiniRenderer.RenderMode renderMode = HologramMiniRenderer.RenderMode.TEXTURED;
  private int viewX;
  private int viewY;
  private int viewW;
  private int viewH;
  private NativeImage image;
  private DynamicTexture dynamicTexture;
  private Identifier textureId;
  private boolean textureDirty = true;
  private int[][] hitZBuffer;
  private final int[] dotHitWorldX;
  private final int[] dotHitWorldZ;
  private final int[] dotHitWorldY;
  private final int[] dotHitScreenPxX;
  private final int[] dotHitScreenPxY;
  private int dotHitCount;
  private static final int MAX_HIT_POINTS = 10000;

  public HologramMiniRenderer(int centerX, int centerZ, int maxRadius, boolean interactive) {
    this.camera = new HologramCamera(centerX, centerZ, maxRadius);
    this.interactiveMode = interactive;
    this.dotHitWorldX = new int[10000];
    this.dotHitWorldZ = new int[10000];
    this.dotHitWorldY = new int[10000];
    this.dotHitScreenPxX = new int[10000];
    this.dotHitScreenPxY = new int[10000];
  }

  public void setData(HologramTerrainData data) {
    this.data = data;
    this.textureDirty = true;
    if (this.selectedLanding == null && data != null) {
      int cx = data.getCenterX();
      int cz = data.getCenterZ();
      int cy = data.getHeight(data.getRadius(), data.getRadius());
      this.selectedLanding = new BlockPos(cx, cy, cz);
      this.markerSpawnTime = System.currentTimeMillis();
    }
  }

  public void markTextureDirty() {
    this.textureDirty = true;
  }

  public void render(GuiGraphicsExtractor graphics, int x, int y, int w, int h, float partialTick) {
    this.viewX = x;
    this.viewY = y;
    this.viewW = w;
    this.viewH = h;
    graphics.fill(x, y, x + w, y + h, this.bgFillColor);
    if (this.data != null && this.data.isReady()) {
      if (this.textureDirty) {
        this.rebakeTexture();
        this.textureDirty = false;
      }

      if (this.textureId != null) {
        graphics.enableScissor(x, y, x + w, y + h);
        graphics.blit(RenderPipelines.GUI_TEXTURED, this.textureId, x, y, 0.0F, 0.0F, w, h, 160, 62);
        this.renderMarker(graphics, x, y, w, h);
        graphics.disableScissor();
      }

      this.renderModeToggle(graphics, x, y, w);
      if (this.selectedLanding != null) {
        String coords = "X:" + this.selectedLanding.getX() + " Z:" + this.selectedLanding.getZ();
        int textW = Minecraft.getInstance().font.width(coords);
        graphics.fill(x + w - textW - 4, y + h - 10, x + w, y + h, -2147483648);
        graphics.text(Minecraft.getInstance().font, coords, x + w - textW - 2, y + h - 9, -16720419, false);
      }
    }
  }

  private void renderModeToggle(GuiGraphicsExtractor graphics, int x, int y, int w) {
    int btnX = x + w - 11;
    int btnY = y + 2;
    int btnW = 9;
    int btnH = 9;
    graphics.fill(btnX, btnY, btnX + btnW, btnY + btnH, -1879048192);
    graphics.outline(btnX, btnY, btnW, btnH, -1442788148);
    switch (this.renderMode) {
      case DOT:
        for (int i = 0; i < 3; i++) {
          graphics.fill(btnX + 2 + i * 2, btnY + 2 + i, btnX + 3 + i * 2, btnY + 3 + i, -16724788);
          graphics.fill(btnX + 3 + i * 2, btnY + 5 - i, btnX + 4 + i * 2, btnY + 6 - i, -16724788);
        }
        break;
      case FLAT:
        graphics.fill(btnX + 3, btnY + 2, btnX + 6, btnY + 3, -16724788);
        graphics.fill(btnX + 2, btnY + 3, btnX + 7, btnY + 4, -16724788);
        graphics.fill(btnX + 2, btnY + 4, btnX + 7, btnY + 6, -16742264);
        graphics.fill(btnX + 3, btnY + 6, btnX + 6, btnY + 7, -16751002);
        break;
      case TEXTURED:
        graphics.fill(btnX + 3, btnY + 2, btnX + 6, btnY + 3, -16711732);
        graphics.fill(btnX + 2, btnY + 3, btnX + 7, btnY + 4, -16711732);
        graphics.fill(btnX + 2, btnY + 4, btnX + 4, btnY + 6, -16733560);
        graphics.fill(btnX + 5, btnY + 4, btnX + 7, btnY + 6, -16742298);
        graphics.fill(btnX + 3, btnY + 6, btnX + 6, btnY + 7, -16751036);
    }
  }

  private void rebakeTexture() {
    if (this.image == null) {
      this.image = new NativeImage(160, 62, false);
    }

    for (int py = 0; py < 62; py++) {
      for (int px = 0; px < 160; px++) {
        this.image.setPixel(px, py, this.bgColor);
      }
    }

    this.hitZBuffer = new int[160][62];
    this.dotHitCount = 0;
    switch (this.renderMode) {
      case DOT:
        this.rebakeDotCloud();
        break;
      case FLAT:
        this.rebakeFlat();
        break;
      case TEXTURED:
        this.rebakeTextured();
    }

    if (this.dynamicTexture == null) {
      this.dynamicTexture = new DynamicTexture(() -> "nostalgia_hologram", this.image);
      this.textureId = Identifier.parse("nostalgia:hologram_preview_" + System.identityHashCode(this));
      Minecraft.getInstance().getTextureManager().register(this.textureId, this.dynamicTexture);
    } else {
      this.dynamicTexture.upload();
    }
  }

  private void rebakeFlat() {
    float centerPx = 80.0F;
    float centerPy = 31.0F;
    float[] projected = new float[2];
    int diameter = this.data.getDiameter();
    int baseY = 64;
    float zoom = this.camera.getZoom();
    int step = Math.max(1, (int)(1.0F / zoom * 0.3F));

    for (int i = 0; i < 160; i++) {
      Arrays.fill(this.hitZBuffer[i], 2147483647);
    }

    IsometricSprite.LOD lod = IsometricSprite.lodForZoom(zoom);
    Int2ObjectOpenHashMap<IsometricSprite> spriteCache = new Int2ObjectOpenHashMap();

    for (int dx = diameter - 1; dx >= 0; dx -= step) {
      for (int dz = 0; dz < diameter; dz += step) {
        int color = this.data.getColor(dx, dz);
        if ((color & 0xFF000000) != 0) {
          int rx = dx - this.data.getRadius();
          int rz = dz - this.data.getRadius();
          if (rx * rx + rz * rz <= this.data.getRadius() * this.data.getRadius()) {
            int worldX = this.data.getCenterX() - this.data.getRadius() + dx;
            int worldZ = this.data.getCenterZ() - this.data.getRadius() + dz;
            int worldY = this.data.getHeight(dx, dz);
            float height = (worldY - baseY) * 0.5F;
            this.camera.project(worldX, height, worldZ, projected);
            int sx = (int)(centerPx + projected[0]);
            int sy = (int)(centerPy + projected[1]);
            int maxExtent = lod.tileW + lod.sideH;
            if (sx >= -maxExtent && sx < 160 + maxExtent && sy >= -maxExtent && sy < 62 + maxExtent) {
              int sortKey = dx * 1000 + dz;
              int stateId = this.data.getStateId(dx, dz);
              IsometricSprite sprite = (IsometricSprite)spriteCache.get(stateId);
              if (sprite == null) {
                sprite = IsometricSprite.fromColor(color, lod);
                spriteCache.put(stateId, sprite);
              }

              IsometricTileRenderer.drawBlock(this.image, sx, sy, sprite, this.hitZBuffer, sortKey, 160, 62);
            }
          }
        }
      }
    }
  }

  private void rebakeTextured() {
    float centerPx = 80.0F;
    float centerPy = 31.0F;
    float[] projected = new float[2];
    int diameter = this.data.getDiameter();
    int baseY = 64;
    float zoom = this.camera.getZoom();
    int step = Math.max(1, (int)(1.0F / zoom * 0.3F));

    for (int i = 0; i < 160; i++) {
      Arrays.fill(this.hitZBuffer[i], 2147483647);
    }

    IsometricSprite.LOD lod = IsometricSprite.lodForZoom(zoom);
    BlockSpriteProvider spriteProvider = BlockSpriteProvider.instance();
    Int2ObjectOpenHashMap<IsometricSprite> spriteCache = new Int2ObjectOpenHashMap();

    for (int dx = diameter - 1; dx >= 0; dx -= step) {
      for (int dz = 0; dz < diameter; dz += step) {
        int color = this.data.getColor(dx, dz);
        if ((color & 0xFF000000) != 0) {
          int rx = dx - this.data.getRadius();
          int rz = dz - this.data.getRadius();
          if (rx * rx + rz * rz <= this.data.getRadius() * this.data.getRadius()) {
            int worldX = this.data.getCenterX() - this.data.getRadius() + dx;
            int worldZ = this.data.getCenterZ() - this.data.getRadius() + dz;
            int worldY = this.data.getHeight(dx, dz);
            float height = (worldY - baseY) * 0.5F;
            this.camera.project(worldX, height, worldZ, projected);
            int sx = (int)(centerPx + projected[0]);
            int sy = (int)(centerPy + projected[1]);
            int maxExtent = lod.tileW + lod.sideH;
            if (sx >= -maxExtent && sx < 160 + maxExtent && sy >= -maxExtent && sy < 62 + maxExtent) {
              int sortKey = dx * 1000 + dz;
              int stateId = this.data.getStateId(dx, dz);
              IsometricSprite sprite = (IsometricSprite)spriteCache.get(stateId);
              if (sprite == null) {
                if (stateId != 0) {
                  BlockState state = Block.stateById(stateId);
                  int[] topPx = spriteProvider.getTopPixels(state);
                  int[] sidePx = spriteProvider.getSidePixels(state);
                  sprite = IsometricSprite.create(topPx, sidePx, lod);
                } else {
                  sprite = IsometricSprite.fromColor(color, lod);
                }

                spriteCache.put(stateId, sprite);
              }

              IsometricTileRenderer.drawBlock(this.image, sx, sy, sprite, this.hitZBuffer, sortKey, 160, 62);
            }
          }
        }
      }
    }
  }

  private void rebakeDotCloud() {
    float centerPx = 80.0F;
    float centerPy = 31.0F;
    float[] projected = new float[2];
    int diameter = this.data.getDiameter();
    int baseY = 64;
    int step = 2;

    for (int dx = 0; dx < diameter; dx += step) {
      for (int dz = 0; dz < diameter; dz += step) {
        int color = this.data.getColor(dx, dz);
        if ((color & 0xFF000000) != 0) {
          int rx = dx - this.data.getRadius();
          int rz = dz - this.data.getRadius();
          if (rx * rx + rz * rz <= this.data.getRadius() * this.data.getRadius()) {
            int worldX = this.data.getCenterX() - this.data.getRadius() + dx;
            int worldZ = this.data.getCenterZ() - this.data.getRadius() + dz;
            int worldY = this.data.getHeight(dx, dz);
            this.camera.project(worldX, (worldY - baseY) * 0.5F, worldZ, projected);
            int sx = (int)(centerPx + projected[0]);
            int sy = (int)(centerPy + projected[1]);
            if (sx >= 0 && sx < 160 && sy >= 0 && sy < 62) {
              this.image.setPixel(sx, sy, color);
              if (this.dotHitCount < 10000) {
                this.dotHitWorldX[this.dotHitCount] = worldX;
                this.dotHitWorldZ[this.dotHitCount] = worldZ;
                this.dotHitWorldY[this.dotHitCount] = worldY;
                this.dotHitScreenPxX[this.dotHitCount] = sx;
                this.dotHitScreenPxY[this.dotHitCount] = sy;
                this.dotHitCount++;
              }
            }
          }
        }
      }
    }
  }

  private void drawIsoDiamond(GuiGraphicsExtractor graphics, int mx, int my, int dw, int dh, int color, boolean outlineOnly) {
    int halfW = dw / 2;
    int halfH = dh / 2;
    if (halfW >= 1 && halfH >= 1) {
      for (int dy = -halfH; dy <= halfH; dy++) {
        float progress = 1.0F - (float)Math.abs(dy) / halfH;
        int dx = Math.round(halfW * progress);
        if (outlineOnly) {
          if (dx == 0) {
            graphics.fill(mx, my + dy, mx + 1, my + dy + 1, color);
          } else {
            graphics.fill(mx - dx, my + dy, mx - dx + 1, my + dy + 1, color);
            graphics.fill(mx + dx, my + dy, mx + dx + 1, my + dy + 1, color);
          }
        } else {
          graphics.fill(mx - dx, my + dy, mx + dx + 1, my + dy + 1, color);
        }
      }
    } else {
      graphics.fill(mx, my, mx + 1, my + 1, color);
    }
  }

  private void renderMarker(GuiGraphicsExtractor graphics, int x, int y, int w, int h) {
    if (this.selectedLanding != null) {
      float[] projected = new float[2];
      int baseY = 64;
      this.camera.project(this.selectedLanding.getX(), (this.selectedLanding.getY() - baseY) * 0.5F, this.selectedLanding.getZ(), projected);
      float scaleX = w / 160.0F;
      float scaleY = h / 62.0F;
      IsometricSprite.LOD lod = IsometricSprite.lodForZoom(this.camera.getZoom());
      float offsetY = this.renderMode == HologramMiniRenderer.RenderMode.DOT ? 0.0F : lod.tileH * 0.5F;
      int mx = (int)(x + (80.0F + projected[0]) * scaleX);
      int my = (int)(y + (31.0F + projected[1] + offsetY) * scaleY);
      long time = System.currentTimeMillis();
      long elapsed = time - this.markerSpawnTime;
      float spawnAlpha = Math.min(1.0F, (float)elapsed / 250.0F);
      float pulse = (float)(0.5 + 0.5 * Math.sin(time * 0.005));
      float ring = (float)(time % 1500L) / 1500.0F;
      if (this.renderMode == HologramMiniRenderer.RenderMode.DOT) {
        int beamAlpha = (int)((60.0F + 40.0F * pulse) * spawnAlpha);
        int beamColor = beamAlpha << 24 | 65518;
        graphics.fill(mx, y, mx + 1, my - 2, beamColor);
        int ringRadius = (int)(2.0F + ring * 5.0F);
        int ringAlpha = (int)(200.0F * (1.0F - ring) * spawnAlpha);
        if (ringAlpha > 10) {
          int ringColor = ringAlpha << 24 | 65484;
          graphics.outline(mx - ringRadius, my - ringRadius, ringRadius * 2 + 1, ringRadius * 2 + 1, ringColor);
        }

        int dotAlpha = (int)((200.0F + 55.0F * pulse) * spawnAlpha);
        int dotColor = dotAlpha << 24 | 65501;
        graphics.fill(mx - 1, my - 1, mx + 2, my + 2, dotColor);
        int crossAlpha = (int)((140.0F + 60.0F * pulse) * spawnAlpha);
        int crossColor = crossAlpha << 24 | 52411;
        graphics.fill(mx - 3, my, mx - 1, my + 1, crossColor);
        graphics.fill(mx + 2, my, mx + 4, my + 1, crossColor);
        graphics.fill(mx, my - 3, mx + 1, my - 1, crossColor);
        graphics.fill(mx, my + 2, mx + 1, my + 4, crossColor);
      } else {
        int beamAlpha = (int)((70.0F + 50.0F * pulse) * spawnAlpha);
        int beamColor = beamAlpha << 24 | 65518;
        graphics.fill(mx, y, mx + 1, my - 4, beamColor);
        int dw = (int)(lod.tileW * scaleX);
        int dh = (int)(lod.tileH * scaleY);
        int fillAlpha = (int)((50.0F + 30.0F * pulse) * spawnAlpha);
        int fillColor = fillAlpha << 24 | 65501;
        this.drawIsoDiamond(graphics, mx, my, dw, dh, fillColor, false);
        int borderAlpha = (int)((200.0F + 55.0F * pulse) * spawnAlpha);
        int borderColor = borderAlpha << 24 | 65501;
        this.drawIsoDiamond(graphics, mx, my, dw, dh, borderColor, true);
        int ringAlpha = (int)(200.0F * (1.0F - ring) * spawnAlpha);
        if (ringAlpha > 10) {
          int ringColor = ringAlpha << 24 | 65484;
          int rdw = (int)(lod.tileW * (1.0F + ring * 1.5F) * scaleX);
          int rdh = (int)(lod.tileH * (1.0F + ring * 1.5F) * scaleY);
          this.drawIsoDiamond(graphics, mx, my, rdw, rdh, ringColor, true);
        }
      }
    }
  }

  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (!this.isInBounds(mouseX, mouseY)) {
      return false;
    } else {
      int btnX = this.viewX + this.viewW - 11;
      int btnY = this.viewY + 2;
      if (mouseX >= btnX && mouseX <= btnX + 9 && mouseY >= btnY && mouseY <= btnY + 9) {
        this.renderMode = switch (this.renderMode) {
          case DOT -> HologramMiniRenderer.RenderMode.FLAT;
          case FLAT -> HologramMiniRenderer.RenderMode.TEXTURED;
          case TEXTURED -> HologramMiniRenderer.RenderMode.DOT;
        };
        this.textureDirty = true;
        return true;
      } else if (button == 0) {
        this.dragging = true;
        this.lastDragX = mouseX;
        this.lastDragY = mouseY;
        this.totalDragDist = 0.0;
        return true;
      } else {
        return false;
      }
    }
  }

  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    if (button == 0 && this.dragging) {
      this.dragging = false;
      if (this.interactiveMode && this.totalDragDist < 8.0) {
        this.selectPointAt(mouseX, mouseY);
      }

      return true;
    } else {
      return false;
    }
  }

  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    if (!this.dragging) {
      return false;
    } else {
      double dx = mouseX - this.lastDragX;
      double dy = mouseY - this.lastDragY;
      this.totalDragDist = this.totalDragDist + (Math.abs(dx) + Math.abs(dy));
      this.lastDragX = mouseX;
      this.lastDragY = mouseY;
      this.camera.pan((float)dx, (float)dy);
      this.textureDirty = true;
      return true;
    }
  }

  public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    if (!this.isInBounds(mouseX, mouseY)) {
      return false;
    } else {
      float texScaleX = 160.0F / this.viewW;
      float texScaleY = 62.0F / this.viewH;
      float cursorOffX = (float)(mouseX - this.viewX) * texScaleX - 80.0F;
      float cursorOffY = (float)(mouseY - this.viewY) * texScaleY - 31.0F;
      this.camera.adjustZoom(verticalAmount, cursorOffX, cursorOffY);
      this.textureDirty = true;
      return true;
    }
  }

  private void selectPointAt(double mouseX, double mouseY) {
    if (this.data != null) {
      float scaleX = 160.0F / this.viewW;
      float scaleY = 62.0F / this.viewH;
      int targetPxX = (int)((mouseX - this.viewX) * scaleX);
      int targetPxY = (int)((mouseY - this.viewY) * scaleY);
      BlockPos newPos = null;
      if (this.renderMode == HologramMiniRenderer.RenderMode.DOT) {
        int bestIdx = -1;
        double bestDist = 1.7976931348623157E308;

        for (int i = 0; i < this.dotHitCount; i++) {
          double ddx = this.dotHitScreenPxX[i] - targetPxX;
          double ddy = this.dotHitScreenPxY[i] - targetPxY;
          double dist = ddx * ddx + ddy * ddy;
          if (dist < bestDist) {
            bestDist = dist;
            bestIdx = i;
          }
        }

        if (bestIdx >= 0 && bestDist < 992.0) {
          newPos = new BlockPos(this.dotHitWorldX[bestIdx], this.dotHitWorldY[bestIdx], this.dotHitWorldZ[bestIdx]);
        }
      } else if (this.hitZBuffer != null) {
        int searchRadius = 3;
        int bestSortKey = 2147483647;
        double bestDist = 1.7976931348623157E308;

        for (int ox = -searchRadius; ox <= searchRadius; ox++) {
          for (int oy = -searchRadius; oy <= searchRadius; oy++) {
            int px = targetPxX + ox;
            int py = targetPxY + oy;
            if (px >= 0 && px < 160 && py >= 0 && py < 62) {
              int sortKey = this.hitZBuffer[px][py];
              if (sortKey != 2147483647) {
                double dist = ox * ox + oy * oy;
                if (dist < bestDist) {
                  bestDist = dist;
                  bestSortKey = sortKey;
                }
              }
            }
          }
        }

        if (bestSortKey != 2147483647) {
          int dx = bestSortKey / 1000;
          int dz = bestSortKey % 1000;
          int worldX = this.data.getCenterX() - this.data.getRadius() + dx;
          int worldZ = this.data.getCenterZ() - this.data.getRadius() + dz;
          int worldY = this.data.getHeight(dx, dz);
          newPos = new BlockPos(worldX, worldY, worldZ);
        }
      }

      if (newPos != null && (this.selectedLanding == null || !this.selectedLanding.equals(newPos))) {
        this.selectedLanding = newPos;
        this.markerSpawnTime = System.currentTimeMillis();
        if (Minecraft.getInstance().player != null) {
          Minecraft.getInstance().player.playSound(SoundEvents.TRIAL_SPAWNER_SPAWN_ITEM, 0.6F, 1.0F);
        }
      }
    }
  }

  private boolean isInBounds(double mouseX, double mouseY) {
    return mouseX >= this.viewX && mouseX < this.viewX + this.viewW && mouseY >= this.viewY && mouseY < this.viewY + this.viewH;
  }

  public void close() {
    if (this.dynamicTexture != null) {
      this.dynamicTexture.close();
      this.dynamicTexture = null;
    }

    if (this.image != null) {
      this.image.close();
      this.image = null;
    }

    this.textureId = null;
  }

  public BlockPos getSelectedLanding() {
    return this.selectedLanding;
  }

  public void setInteractiveMode(boolean interactive) {
    this.interactiveMode = interactive;
  }

  public boolean isInteractive() {
    return this.interactiveMode;
  }

  public HologramCamera getCamera() {
    return this.camera;
  }

  public void setBgColor(int textureBg, int fillBg) {
    this.bgColor = textureBg;
    this.bgFillColor = fillBg;
    this.textureDirty = true;
  }

  private static enum RenderMode {
    DOT,
    FLAT,
    TEXTURED;

    private RenderMode() {
    }
  }
}
