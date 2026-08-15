package net.nostalgia.client.gui.hologram3d.sprite;

import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSpriteProvider {
  private static BlockSpriteProvider instance;
  private final Int2ObjectOpenHashMap<int[]> topPixelsCache = new Int2ObjectOpenHashMap();
  private final Int2ObjectOpenHashMap<int[]> sidePixelsCache = new Int2ObjectOpenHashMap();
  private final HashMap<Integer, Integer> avgColorCache = new HashMap<>();
  private static final int SPRITE_SIZE = 16;
  private static final int[] EMPTY = new int[256];

  public BlockSpriteProvider() {
  }

  public static BlockSpriteProvider instance() {
    if (instance == null) {
      instance = new BlockSpriteProvider();
    }

    return instance;
  }

  public int[] getTopPixels(BlockState state) {
    int id = Block.getId(state);
    int[] cached = (int[])this.topPixelsCache.get(id);
    if (cached != null) {
      return cached;
    } else {
      int[] pixels = this.extractFacePixels(state, Direction.UP);
      if (pixels == null) {
        pixels = this.extractFacePixels(state, null);
      }

      if (pixels == null) {
        pixels = EMPTY;
      }

      this.topPixelsCache.put(id, pixels);
      return pixels;
    }
  }

  public int[] getSidePixels(BlockState state) {
    int id = Block.getId(state);
    int[] cached = (int[])this.sidePixelsCache.get(id);
    if (cached != null) {
      return cached;
    } else {
      int[] pixels = this.extractFacePixels(state, Direction.NORTH);
      if (pixels == null) {
        pixels = this.extractFacePixels(state, Direction.EAST);
      }

      if (pixels == null) {
        pixels = this.extractFacePixels(state, null);
      }

      if (pixels == null) {
        pixels = EMPTY;
      }

      this.sidePixelsCache.put(id, pixels);
      return pixels;
    }
  }

  public int getAverageColor(BlockState state) {
    int id = Block.getId(state);
    Integer cached = this.avgColorCache.get(id);
    if (cached != null) {
      return cached;
    } else {
      int[] top = this.getTopPixels(state);
      long r = 0L;
      long g = 0L;
      long b = 0L;
      int count = 0;

      for (int px : top) {
        int a = px >> 24 & 0xFF;
        if (a >= 128) {
          r += px >> 16 & 0xFF;
          g += px >> 8 & 0xFF;
          b += px & 0xFF;
          count++;
        }
      }

      int avg;
      if (count > 0) {
        avg = 0xFF000000 | (int)(r / count) << 16 | (int)(g / count) << 8 | (int)(b / count);
      } else {
        avg = -8355712;
      }

      this.avgColorCache.put(id, avg);
      return avg;
    }
  }

  private int[] extractFacePixels(BlockState state, Direction face) {
    try {
      Minecraft mc = Minecraft.getInstance();
      BlockStateModelSet modelSet = mc.getModelManager().getBlockStateModelSet();
      BlockStateModel model = modelSet.get(state);
      List<BlockStateModelPart> parts = new ArrayList<>();
      model.collectParts(RandomSource.create(42L), parts);

      for (BlockStateModelPart part : parts) {
        List<BakedQuad> quads = part.getQuads(face);
        if (quads != null && !quads.isEmpty()) {
          BakedQuad quad = quads.get(0);
          TextureAtlasSprite sprite = quad.materialInfo().sprite();
          return this.readSpritePixels(sprite);
        }
      }
    } catch (Exception var12) {
    }

    return null;
  }

  private int[] readSpritePixels(TextureAtlasSprite sprite) {
    SpriteContents contents = sprite.contents();
    NativeImage image = contents.originalImage;
    int srcW = contents.width();
    int srcH = contents.height();
    int[] pixels = new int[256];

    for (int y = 0; y < 16; y++) {
      for (int x = 0; x < 16; x++) {
        int srcX = x * srcW / 16;
        int srcY = y * srcH / 16;
        if (srcX < image.getWidth() && srcY < image.getHeight()) {
          pixels[y * 16 + x] = image.getPixel(srcX, srcY);
        }
      }
    }

    return pixels;
  }

  public void invalidate() {
    this.topPixelsCache.clear();
    this.sidePixelsCache.clear();
    this.avgColorCache.clear();
  }
}
