package net.nostalgia.client.events.caches;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.DimensionUtil;
import net.nostalgia.client.events.caches.impl.AlphaByteCache;
import net.nostalgia.client.events.caches.providers.AlphaHologramProvider;
import net.nostalgia.client.events.caches.providers.DimensionHologramCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramProvider;
import net.nostalgia.client.events.caches.providers.DimensionHologramRegistry;
import net.nostalgia.client.events.caches.providers.EmptyHologramProvider;
import net.nostalgia.client.events.caches.providers.HologramSection;
import net.nostalgia.client.events.caches.providers.RDHologramProvider;
import net.nostalgia.client.events.core.DebugOwerContext;
import net.nostalgia.client.events.core.IHologramContext;
import net.nostalgia.client.events.echo.EchoTransitionContext;
import net.nostalgia.client.events.skyportal.SkyPortalContext;
import net.nostalgia.client.performance.SHAMetricsCollector;
import net.nostalgia.client.render.PortalSkyRenderer;
import net.sha.api.HologramBounds;
import net.sha.api.HologramProvider;

public class UniversalHologramCache implements HologramProvider {
  public static final UniversalHologramCache INSTANCE = new UniversalHologramCache();
  public static volatile boolean cacheGenerated = false;
  public static volatile boolean overworldCacheReady = false;
  public static volatile boolean debugOwer = false;
  public static volatile BlockPos debugOwerCenter = null;
  public static volatile boolean decoupledCollision = false;
  public static volatile float customCollisionRadius = -1.0F;
  private static final Map<String, DimensionHologramProvider> PROVIDERS = new ConcurrentHashMap<>();
  public static final List<IHologramContext> ACTIVE_CONTEXTS = new CopyOnWriteArrayList<>();
  private static long lastSpoofLogTime = 0L;

  public UniversalHologramCache() {
  }

  public static boolean hasAnyCacheData() {
    if (cacheGenerated || overworldCacheReady || (AlphaByteCache.FAST_CACHE != null && !AlphaByteCache.FAST_CACHE.isEmpty())) return true;
    return DimensionHologramRegistry.hasAnySections();
  }

  public static DimensionHologramProvider getProvider(String dimensionId) {
    return dimensionId == null ? null : PROVIDERS.get(DimensionUtil.normalize(dimensionId));
  }

  public static BlockState getBlockState(String targetDim, int worldX, int y, int worldZ, boolean isSkyInverted) {
    DimensionHologramCache overrideCache = DimensionHologramRegistry.getByName(targetDim);
    if (overrideCache != null) {
      long packedPos = BlockPos.asLong(worldX, y, worldZ);
      BlockState ritualOverride = overrideCache.getRitualOverride(packedPos);
      if (ritualOverride != null) {
        return ritualOverride;
      }

      BlockState delta = overrideCache.getOverrideRaw(packedPos);
      if (delta != null) {
        return delta;
      }
    }

    if (DimensionUtil.isClientGenerated(targetDim)) {
      int chunkX = worldX >> 4;
      int chunkZ = worldZ >> 4;
      long chunkHash = ChunkPos.pack(chunkX, chunkZ);
      byte[] chunkData = AlphaByteCache.FAST_CACHE.get(chunkHash);
      if (chunkData != null) {
        if (y >= 0 && y < 128) {
          int localX = worldX & 15;
          int localZ = worldZ & 15;
          int index = (localX * 16 + localZ) * 128 + y;
          byte blockId = chunkData[index];
          if (blockId == 0) {
            return isSkyInverted ? null : Blocks.AIR.defaultBlockState();
          } else {
            DimensionHologramProvider provider = getProvider(targetDim);
            return provider.getBlockState(blockId, isSkyInverted);
          }
        } else {
          return isSkyInverted ? null : Blocks.AIR.defaultBlockState();
        }
      } else {
        return null;
      }
    } else {
      DimensionHologramCache cache = DimensionHologramRegistry.getByName(targetDim);
      BlockState state = cache != null ? cache.getSectionBlock(worldX, y, worldZ) : null;
      if (state != null && !state.isAir()) {
        return state;
      } else {
        return isSkyInverted ? null : Blocks.AIR.defaultBlockState();
      }
    }
  }

  public static int getSafeSurfaceYUpwards(String targetDim, int worldX, int startY, int worldZ) {
    int maxY = DimensionUtil.isClientGenerated(targetDim) ? 127 : 320;

    for (int y = Math.max(-64, startY); y < maxY; y++) {
      BlockState feet = getBlockState(targetDim, worldX, y, worldZ, false);
      BlockState head = getBlockState(targetDim, worldX, y + 1, worldZ, false);
      Minecraft client = Minecraft.getInstance();
      BlockPos posFeet = new BlockPos(worldX, y, worldZ);
      BlockPos posHead = new BlockPos(worldX, y + 1, worldZ);
      boolean feetSolid = feet != null && (!feet.getCollisionShape(client.level, posFeet).isEmpty() || !feet.getFluidState().isEmpty());
      boolean headSolid = head != null && (!head.getCollisionShape(client.level, posHead).isEmpty() || !head.getFluidState().isEmpty());
      if (!feetSolid && !headSolid) {
        return y;
      }
    }

    return startY;
  }

  public boolean isActive() {
    if (!hasAnyCacheData()) {
      return false;
    } else {
      for (IHologramContext ctx : ACTIVE_CONTEXTS) {
        if (ctx.isActive()) {
          return true;
        }
      }

      return false;
    }
  }

  public boolean providesCollision() {
    return true;
  }

  public HologramBounds getBounds() {
    int minX = 2147483647;
    int minY = 2147483647;
    int minZ = 2147483647;
    int maxX = -2147483648;
    int maxY = -2147483648;
    int maxZ = -2147483648;
    boolean hasBounds = false;

    for (IHologramContext ctx : ACTIVE_CONTEXTS) {
      if (ctx.isActive()) {
        BlockPos c = ctx.getCenter();
        if (c != null) {
          int r = (int)ctx.getCollisionRadius() + 16;
          int rY = (int)ctx.getCollisionRadius() + 16;
          int minYVal = c.getY() - rY;
          int maxYVal = c.getY() + rY;
          if (ctx.isSkyInverted()) {
            Minecraft mc = Minecraft.getInstance();
            boolean isTarget = mc.level != null && mc.level.dimension().identifier().toString().equals(PortalSkyRenderer.originalTargetDimension);
            minYVal = isTarget ? PortalSkyRenderer.crackPlaneYTarget : PortalSkyRenderer.crackPlaneY;
            maxYVal = 320;
          }

          minX = Math.min(minX, c.getX() - r);
          minY = Math.min(minY, Math.max(-64, minYVal));
          minZ = Math.min(minZ, c.getZ() - r);
          maxX = Math.max(maxX, c.getX() + r);
          maxY = Math.max(maxY, Math.min(320, maxYVal));
          maxZ = Math.max(maxZ, c.getZ() + r);
          hasBounds = true;
        }
      }
    }

    return hasBounds ? new HologramBounds(minX, minY, minZ, maxX, maxY, maxZ) : new HologramBounds(0, 0, 0, 0, 0, 0);
  }

  public BlockState getSpoofedBlock(int worldX, int y, int worldZ) {
    for (IHologramContext ctx : ACTIVE_CONTEXTS) {
      if (ctx.isActive() && ctx.contains(worldX, y, worldZ)) {
        String targetDim = ctx.getTargetDimension();
        if (targetDim != null) {
          DimensionHologramCache cache = DimensionHologramRegistry.getByName(targetDim);
          if (cache != null) {
            int tx;
            int ty;
            int tz;
            if (ctx.isSkyInverted()) {
              tx = worldX;
              int portalZ = PortalSkyRenderer.portalCenter.getZ();
              tz = 2 * portalZ - worldZ;
              int crackPlaneY = PortalSkyRenderer.crackPlaneY;
              int crackPlaneYTarget = PortalSkyRenderer.crackPlaneYTarget;
              int inversionConstant = crackPlaneY + crackPlaneYTarget;
              ty = inversionConstant - y;
            } else {
              tx = worldX + ctx.getOffsetX();
              ty = y - ctx.getOffsetY();
              tz = worldZ + ctx.getOffsetZ();
            }

            long targetPos = BlockPos.asLong(tx, ty, tz);
            if (cache.hasRitualOverride(targetPos)) {
              return cache.getRitualOverride(targetPos);
            }
          }
        }
      }
    }

    for (IHologramContext ctxx : ACTIVE_CONTEXTS) {
      if (ctxx.isActive() && ctxx.isTerrainActive() && ctxx.contains(worldX, y, worldZ)) {
        String targetDim = ctxx.getTargetDimension();
        if (targetDim != null) {
          DimensionHologramCache cache = DimensionHologramRegistry.getByName(targetDim);
          if (cache != null) {
            int txx;
            int tyx;
            int tzx;
            if (ctxx.isSkyInverted()) {
              txx = worldX;
              int portalZ = PortalSkyRenderer.portalCenter.getZ();
              tzx = 2 * portalZ - worldZ;
              int crackPlaneY = PortalSkyRenderer.crackPlaneY;
              int crackPlaneYTarget = PortalSkyRenderer.crackPlaneYTarget;
              int inversionConstant = crackPlaneY + crackPlaneYTarget;
              tyx = inversionConstant - y;
            } else {
              txx = worldX + ctxx.getOffsetX();
              tyx = y - ctxx.getOffsetY();
              tzx = worldZ + ctxx.getOffsetZ();
            }

            long targetPos = BlockPos.asLong(txx, tyx, tzx);
            if (cache.hasOverrideRaw(targetPos)) {
              return cache.getOverrideRaw(targetPos);
            }
          }
        }
      }
    }

    if (!hasAnyCacheData()) {
      return null;
    } else {
      for (IHologramContext ctxxx : ACTIVE_CONTEXTS) {
        if (ctxxx.isActive() && ctxxx.contains(worldX, y, worldZ)) {
          if (!ctxxx.isTerrainActive()) {
            return null;
          }

          boolean isSkyInverted = ctxxx.isSkyInverted();
          int sourceX;
          int sourceZ;
          int sourceY;
          if (isSkyInverted) {
            Minecraft mc = Minecraft.getInstance();
            boolean isTarget = mc.level != null && mc.level.dimension().identifier().toString().equals(PortalSkyRenderer.originalTargetDimension);
            int crackPlaneY = PortalSkyRenderer.crackPlaneY;
            int crackPlaneYTarget = PortalSkyRenderer.crackPlaneYTarget;
            int inversionConstant = crackPlaneY + crackPlaneYTarget;
            sourceX = worldX;
            int portalZ = PortalSkyRenderer.portalCenter.getZ();
            sourceZ = 2 * portalZ - worldZ;
            int currentCrackPlaneY = isTarget ? crackPlaneYTarget : crackPlaneY;
            if (y <= currentCrackPlaneY) {
              return null;
            }

            sourceY = inversionConstant - y;
          } else {
            sourceX = worldX + ctxxx.getOffsetX();
            sourceZ = worldZ + ctxxx.getOffsetZ();
            sourceY = y - ctxxx.getOffsetY();
          }

          return getBlockState(ctxxx.getTargetDimension(), sourceX, sourceY, sourceZ, isSkyInverted);
        }
      }

      return null;
    }
  }

  public Holder<Biome> getSpoofedBiome(int worldX, int y, int worldZ) {
    if (!hasAnyCacheData()) {
      return null;
    } else {
      for (IHologramContext ctx : ACTIVE_CONTEXTS) {
        if (ctx.isActive() && ctx.contains(worldX, y, worldZ)) {
          if (!ctx.isTerrainActive()) {
            return null;
          }

          boolean isSkyInverted = ctx.isSkyInverted();
          int sourceX;
          int sourceZ;
          int sourceY;
          if (isSkyInverted) {
            Minecraft mc = Minecraft.getInstance();
            boolean isTarget = mc.level != null && mc.level.dimension().identifier().toString().equals(PortalSkyRenderer.originalTargetDimension);
            int crackPlaneY = PortalSkyRenderer.crackPlaneY;
            int crackPlaneYTarget = PortalSkyRenderer.crackPlaneYTarget;
            int inversionConstant = crackPlaneY + crackPlaneYTarget;
            sourceX = worldX;
            int portalZ = PortalSkyRenderer.portalCenter.getZ();
            sourceZ = 2 * portalZ - worldZ;
            int currentCrackPlaneY = isTarget ? crackPlaneYTarget : crackPlaneY;
            if (y <= currentCrackPlaneY) {
              return null;
            }

            sourceY = inversionConstant - y;
          } else {
            sourceX = worldX + ctx.getOffsetX();
            sourceZ = worldZ + ctx.getOffsetZ();
            sourceY = y - ctx.getOffsetY();
          }

          if (!DimensionUtil.isClientGenerated(ctx.getTargetDimension())) {
            DimensionHologramCache cache = DimensionHologramRegistry.getByName(ctx.getTargetDimension());
            Holder<Biome> biome = cache != null ? cache.getSectionBiome(sourceX, sourceY, sourceZ) : null;
            if (biome != null) {
              return biome;
            }
          }

          return null;
        }
      }

      return null;
    }
  }

  public void getSpoofedBlockRange(int originX, int originY, int originZ, BlockState[] blockArray) {
    if (hasAnyCacheData()) {
      List<IHologramContext> activeContexts = new ArrayList<>(2);

      for (int i = 0; i < ACTIVE_CONTEXTS.size(); i++) {
        IHologramContext ctx = ACTIVE_CONTEXTS.get(i);
        if (ctx.isActive()) {
          BlockPos c = ctx.getCenter();
          if (c != null) {
            double r = ctx.getCollisionRadius();
            double minSectionDistSq = 0.0;
            if (c.getX() < originX) {
              minSectionDistSq += (originX - c.getX()) * (originX - c.getX());
            } else if (c.getX() > originX + 15) {
              minSectionDistSq += (c.getX() - (originX + 15)) * (c.getX() - (originX + 15));
            }

            if (c.getZ() < originZ) {
              minSectionDistSq += (originZ - c.getZ()) * (originZ - c.getZ());
            } else if (c.getZ() > originZ + 15) {
              minSectionDistSq += (c.getZ() - (originZ + 15)) * (c.getZ() - (originZ + 15));
            }

            if (minSectionDistSq <= r * r) {
              activeContexts.add(ctx);
            }
          }
        }
      }

      if (!activeContexts.isEmpty()) {
        class ContextCache {
          final IHologramContext ctx;
          final boolean isClientGen;
          final String targetDim;
          final int offsetX;
          final int offsetY;
          final int offsetZ;
          DimensionHologramCache dimCache;
          HologramSection lastSection;
          long lastSectionKey;

          ContextCache(IHologramContext ctx) {
            Objects.requireNonNull(UniversalHologramCache.this);
            super();
            this.lastSection = null;
            this.lastSectionKey = -9223372036854775808L;
            this.ctx = ctx;
            this.targetDim = ctx.getTargetDimension();
            this.isClientGen = DimensionUtil.isClientGenerated(this.targetDim);
            this.offsetX = ctx.getOffsetX();
            this.offsetY = ctx.getOffsetY();
            this.offsetZ = ctx.getOffsetZ();
            if (this.targetDim != null) {
              this.dimCache = DimensionHologramRegistry.getByName(this.targetDim);
            }
          }
        }

        List<ContextCache> caches = new ArrayList<>(activeContexts.size());

        for (int ix = 0; ix < activeContexts.size(); ix++) {
          caches.add(new ContextCache(activeContexts.get(ix)));
        }

        long now = System.currentTimeMillis();
        if (now - lastSpoofLogTime > 2000L) {
          lastSpoofLogTime = now;
          String targetNames = "";

          for (int ix = 0; ix < caches.size(); ix++) {
            targetNames = targetNames + caches.get(ix).targetDim + " ";
          }

          String msg = "[SHA-API] Sodium requested spoofed blocks at section: (" + originX + ", " + originY + ", " + originZ + ") targeting: " + targetNames;
          SHAMetricsCollector.logEvent(msg);
        }

        long lastCachedChunkHash = -9223372036854775808L;
        byte[] lastCachedAlphaData = null;

        for (int lx = 0; lx < 16; lx++) {
          int worldX = originX + lx;

          for (int lz = 0; lz < 16; lz++) {
            int worldZ = originZ + lz;

            for (int ly = 0; ly < 16; ly++) {
              int worldY = originY + ly;
              int ix = ly << 8 | lz << 4 | lx;

              for (int cIdx = 0; cIdx < caches.size(); cIdx++) {
                ContextCache cc = caches.get(cIdx);
                if (cc.ctx.contains(worldX, worldY, worldZ)) {
                  boolean isSkyInverted = cc.ctx.isSkyInverted();
                  int tx;
                  int ty;
                  int tz;
                  if (isSkyInverted) {
                    int crackPlaneY = PortalSkyRenderer.crackPlaneY;
                    int crackPlaneYTarget = PortalSkyRenderer.crackPlaneYTarget;
                    int inversionConstant = crackPlaneY + crackPlaneYTarget;
                    tx = worldX;
                    int portalZ = PortalSkyRenderer.portalCenter.getZ();
                    tz = 2 * portalZ - worldZ;
                    ty = inversionConstant - worldY;
                  } else {
                    tx = worldX + cc.offsetX;
                    ty = worldY - cc.offsetY;
                    tz = worldZ + cc.offsetZ;
                  }

                  if (cc.dimCache != null) {
                    long targetPos = BlockPos.asLong(tx, ty, tz);
                    BlockState override = cc.dimCache.getRitualOverride(targetPos);
                    if (override != null) {
                      blockArray[ix] = override;
                      break;
                    }
                  }

                  if (cc.ctx.isTerrainActive()) {
                    if (cc.dimCache != null) {
                      long targetPos = BlockPos.asLong(tx, ty, tz);
                      BlockState delta = cc.dimCache.getOverrideRaw(targetPos);
                      if (delta != null) {
                        blockArray[ix] = delta;
                        break;
                      }
                    }

                    if (cc.isClientGen) {
                      int chunkX = tx >> 4;
                      int chunkZ = tz >> 4;
                      long chunkHash = ChunkPos.pack(chunkX, chunkZ);
                      byte[] chunkData;
                      if (chunkHash == lastCachedChunkHash) {
                        chunkData = lastCachedAlphaData;
                      } else {
                        chunkData = AlphaByteCache.FAST_CACHE.get(chunkHash);
                        lastCachedChunkHash = chunkHash;
                        lastCachedAlphaData = chunkData;
                      }

                      if (chunkData != null && ty >= 0 && ty < 128) {
                        int localX = tx & 15;
                        int localZ = tz & 15;
                        int index = (localX * 16 + localZ) * 128 + ty;
                        byte blockId = chunkData[index];
                        if (blockId != 0) {
                          DimensionHologramProvider provider = PROVIDERS.get(DimensionUtil.normalize(cc.targetDim));
                          if (provider != null) {
                            BlockState state = provider.getBlockState(blockId, isSkyInverted);
                            if (state != null) {
                              blockArray[ix] = state;
                              break;
                            }
                          }
                        } else if (!isSkyInverted) {
                          blockArray[ix] = Blocks.AIR.defaultBlockState();
                          break;
                        }
                      } else if (chunkData != null && !isSkyInverted) {
                        blockArray[ix] = Blocks.AIR.defaultBlockState();
                        break;
                      }
                    } else if (cc.dimCache != null) {
                      int tcx = tx >> 4;
                      int tsy = ty >> 4;
                      int tcz = tz >> 4;
                      long secKey = (tcx & 4194303L) << 42 | (tsy & 1048575L) << 22 | tcz & 4194303L;
                      HologramSection section;
                      if (secKey == cc.lastSectionKey) {
                        section = cc.lastSection;
                      } else {
                        section = (HologramSection)cc.dimCache.getSections().get(secKey);
                        cc.lastSection = section;
                        cc.lastSectionKey = secKey;
                      }

                      if (section != null) {
                        int tlx = tx & 15;
                        int tly = ty & 15;
                        int tlz = tz & 15;
                        BlockState state = section.getBlockState(tlx, tly, tlz);
                        if (state != null && !state.isAir()) {
                          blockArray[ix] = state;
                          break;
                        }

                        if (!isSkyInverted) {
                          blockArray[ix] = Blocks.AIR.defaultBlockState();
                          break;
                        }
                      } else if (!isSkyInverted) {
                        blockArray[ix] = Blocks.AIR.defaultBlockState();
                        break;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  public static void clearMemoryCaches() {
    AlphaByteCache.clear();
    AlphaByteCache.cachedDimensionId = null;
    cacheGenerated = false;
    overworldCacheReady = false;
    DimensionHologramRegistry.clearAll();
    DimensionHologramRegistry.clearAllOverrides();
  }

  static {
    ACTIVE_CONTEXTS.add(EchoTransitionContext.INSTANCE);
    ACTIVE_CONTEXTS.add(SkyPortalContext.INSTANCE);
    ACTIVE_CONTEXTS.add(DebugOwerContext.INSTANCE);
    PROVIDERS.put("nostalgia:alpha_112_01", new AlphaHologramProvider());
    PROVIDERS.put("nostalgia:rd_132211", new RDHologramProvider());
    PROVIDERS.put("minecraft:overworld", new EmptyHologramProvider());
  }
}
