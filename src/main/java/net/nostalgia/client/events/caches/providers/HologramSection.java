package net.nostalgia.client.events.caches.providers;

import java.nio.ByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class HologramSection {
  public BlockState[] palette;
  public byte[] indices;
  public Holder<Biome>[] biomePalette;
  public byte[] biomeIndices;
  private byte[] lazyData;
  private int lazyOffset;
  private volatile boolean isLazy = false;

  public HologramSection(BlockState[] palette, byte[] indices, Holder<Biome>[] biomePalette, byte[] biomeIndices) {
    this.palette = palette;
    this.indices = indices;
    this.biomePalette = biomePalette;
    this.biomeIndices = biomeIndices;
  }

  public HologramSection(byte[] lazyData, int lazyOffset) {
    this.lazyData = lazyData;
    this.lazyOffset = lazyOffset;
    this.isLazy = true;
  }

  public void resolveLazy() {
    if (this.isLazy) {
      synchronized (this) {
        if (this.isLazy) {
          ByteBuffer buf = ByteBuffer.wrap(this.lazyData, this.lazyOffset, this.lazyData.length - this.lazyOffset);
          int palSize = buf.getShort() & '\uffff';
          this.palette = new BlockState[palSize];

          for (int p = 0; p < palSize; p++) {
            this.palette[p] = Block.stateById(buf.getInt());
          }

          if (palSize > 1) {
            this.indices = new byte[4096];
            buf.get(this.indices);
          }

          int biomePalSize = buf.getShort() & '\uffff';
          if (biomePalSize > 0) {
            this.biomePalette = new Holder[biomePalSize];
            Registry<Biome> biomeRegistry = Minecraft.getInstance().level != null
              ? Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.BIOME)
              : null;

            for (int p = 0; p < biomePalSize; p++) {
              int biomeId = buf.getInt();
              if (biomeRegistry != null) {
                Biome b = (Biome)biomeRegistry.byId(biomeId);
                if (b != null) {
                  this.biomePalette[p] = biomeRegistry.wrapAsHolder(b);
                } else {
                  this.biomePalette[p] = (Holder<Biome>)biomeRegistry.getAny().get();
                }
              }
            }
          }

          if (biomePalSize > 1) {
            this.biomeIndices = new byte[64];
            buf.get(this.biomeIndices);
          }

          this.lazyData = null;
          this.isLazy = false;
        }
      }
    }
  }

  public HologramSection(BlockState[] palette, byte[] indices) {
    this(palette, indices, null, null);
  }

  public BlockState getBlockState(int localX, int localY, int localZ) {
    this.resolveLazy();
    if (this.palette != null && this.palette.length != 0) {
      if (this.palette.length != 1 && this.indices != null) {
        int index = localY << 8 | localZ << 4 | localX;
        if (index >= 0 && index < 4096) {
          int palIndex = this.indices[index] & 255;
          return palIndex >= this.palette.length ? Blocks.AIR.defaultBlockState() : this.palette[palIndex];
        } else {
          return Blocks.AIR.defaultBlockState();
        }
      } else {
        return this.palette[0];
      }
    } else {
      return Blocks.AIR.defaultBlockState();
    }
  }

  public Holder<Biome> getBiome(int localX, int localY, int localZ) {
    this.resolveLazy();
    if (this.biomePalette != null && this.biomePalette.length != 0) {
      if (this.biomePalette.length != 1 && this.biomeIndices != null) {
        int biomeX = localX >> 2;
        int biomeY = localY >> 2;
        int biomeZ = localZ >> 2;
        int index = biomeY << 4 | biomeZ << 2 | biomeX;
        if (index >= 0 && index < 64) {
          int palIndex = this.biomeIndices[index] & 255;
          return palIndex >= this.biomePalette.length ? this.biomePalette[0] : this.biomePalette[palIndex];
        } else {
          return this.biomePalette[0];
        }
      } else {
        return this.biomePalette[0];
      }
    } else {
      return null;
    }
  }
}
