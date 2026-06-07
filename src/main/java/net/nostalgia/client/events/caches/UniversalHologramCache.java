package net.nostalgia.client.events.caches;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.client.events.caches.impl.AlphaByteCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramProvider;
import net.nostalgia.client.events.caches.providers.EmptyHologramProvider;
import net.nostalgia.client.events.caches.providers.DimensionHologramCache;
import net.nostalgia.client.events.caches.providers.DimensionHologramRegistry;

import net.nostalgia.client.events.core.IHologramContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UniversalHologramCache implements net.sha.api.HologramProvider {
    public static final UniversalHologramCache INSTANCE = new UniversalHologramCache();

    public static volatile boolean cacheGenerated = false;
    public static volatile boolean overworldCacheReady = false;
    public static volatile boolean debugOwer = false;

    public static volatile BlockPos debugOwerCenter = null;
    public static volatile boolean decoupledCollision = false;
    public static volatile float customCollisionRadius = -1f;

    private static final Map<String, DimensionHologramProvider> PROVIDERS = new ConcurrentHashMap<>();

    public static final List<IHologramContext> ACTIVE_CONTEXTS = new CopyOnWriteArrayList<>();

    static {
        ACTIVE_CONTEXTS.add(net.nostalgia.client.events.echo.EchoTransitionContext.INSTANCE);
        ACTIVE_CONTEXTS.add(net.nostalgia.client.events.skyportal.SkyPortalContext.INSTANCE);
        ACTIVE_CONTEXTS.add(net.nostalgia.client.events.core.DebugOwerContext.INSTANCE);

        PROVIDERS.put(net.nostalgia.alphalogic.ritual.DimensionUtil.ALPHA_FULL, new net.nostalgia.client.events.caches.providers.AlphaHologramProvider());
        PROVIDERS.put(net.nostalgia.alphalogic.ritual.DimensionUtil.RD_FULL, new net.nostalgia.client.events.caches.providers.RDHologramProvider());
        PROVIDERS.put(net.nostalgia.alphalogic.ritual.DimensionUtil.OW_FULL, new EmptyHologramProvider());
    }

    public static DimensionHologramProvider getProvider(String dimensionId) {
        if (dimensionId == null) return null;
        return PROVIDERS.get(net.nostalgia.alphalogic.ritual.DimensionUtil.normalize(dimensionId));
    }

    public static BlockState getBlockState(String targetDim, int worldX, int y, int worldZ, boolean isSkyInverted) {

        if (net.nostalgia.alphalogic.ritual.DimensionUtil.isClientGenerated(targetDim)) {
        } else {
            DimensionHologramCache cache = DimensionHologramRegistry.getByName(targetDim);
            BlockState state = cache != null ? cache.getSectionBlock(worldX, y, worldZ) : null;
            if (state != null && !state.isAir()) {
                return state;
            }
            return isSkyInverted ? null : Blocks.AIR.defaultBlockState();
        }

        int chunkX = worldX >> 4;
        int chunkZ = worldZ >> 4;

        long chunkHash = net.minecraft.world.level.ChunkPos.pack(chunkX, chunkZ);
        byte[] chunkData = AlphaByteCache.FAST_CACHE.get(chunkHash);

        if (chunkData != null) {
            if (y >= 0 && y < 128) {
                int localX = worldX & 15;
                int localZ = worldZ & 15;
                int index = (localX * 16 + localZ) * 128 + y;
                byte blockId = chunkData[index];
                if (blockId == 0) {
                    return isSkyInverted ? null : Blocks.AIR.defaultBlockState();
                }
                DimensionHologramProvider provider = getProvider(targetDim);
                return provider.getBlockState(blockId, isSkyInverted);
            } else {
                return isSkyInverted ? null : Blocks.AIR.defaultBlockState();
            }
        }
        return null;
    }

    public static int getSafeSurfaceYUpwards(String targetDim, int worldX, int startY, int worldZ) {
        int maxY = net.nostalgia.alphalogic.ritual.DimensionUtil.isClientGenerated(targetDim) ? 127 : 320;
        for (int y = Math.max(-64, startY); y < maxY; y++) {
            BlockState feet = getBlockState(targetDim, worldX, y, worldZ, false);
            BlockState head = getBlockState(targetDim, worldX, y + 1, worldZ, false);
            
            net.minecraft.client.Minecraft client = net.minecraft.client.Minecraft.getInstance();
            net.minecraft.core.BlockPos posFeet = new net.minecraft.core.BlockPos(worldX, y, worldZ);
            net.minecraft.core.BlockPos posHead = new net.minecraft.core.BlockPos(worldX, y + 1, worldZ);
            boolean feetSolid = feet != null && (!feet.getCollisionShape(client.level, posFeet).isEmpty() || !feet.getFluidState().isEmpty());
            boolean headSolid = head != null && (!head.getCollisionShape(client.level, posHead).isEmpty() || !head.getFluidState().isEmpty());
            
            if (!feetSolid && !headSolid) {
                return y;
            }
        }
        return startY;
    }

    @Override
    public boolean isActive() {
        if (!cacheGenerated && !overworldCacheReady) return false;
        
        for (IHologramContext ctx : ACTIVE_CONTEXTS) {
            if (ctx.isActive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean providesCollision() {
        return true;
    }

    @Override
    public net.sha.api.HologramBounds getBounds() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        boolean hasBounds = false;

        for (IHologramContext ctx : ACTIVE_CONTEXTS) {
            if (!ctx.isActive()) continue;
            
            BlockPos c = ctx.getCenter();
            if (c != null) {
                int r = (int) ctx.getCollisionRadius() + 16;
                int rY = (int) ctx.getCollisionRadius() + 16;
                
                int minYVal = c.getY() - rY;
                int maxYVal = c.getY() + rY;
                if (ctx.isSkyInverted()) {
                    net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                    boolean isTarget = mc.level != null && mc.level.dimension().identifier().toString().equals(net.nostalgia.client.render.PortalSkyRenderer.originalTargetDimension);
                    minYVal = isTarget ? net.nostalgia.client.render.PortalSkyRenderer.crackPlaneYTarget : net.nostalgia.client.render.PortalSkyRenderer.crackPlaneY;
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

        if (hasBounds) {
            return new net.sha.api.HologramBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }
        return new net.sha.api.HologramBounds(0, 0, 0, 0, 0, 0);
    }

    @Override
    public BlockState getSpoofedBlock(int worldX, int y, int worldZ) {

        for (IHologramContext ctx : ACTIVE_CONTEXTS) {
            if (ctx.isActive() && ctx.contains(worldX, y, worldZ)) {
                String targetDim = ctx.getTargetDimension();
                if (targetDim != null) {
                    DimensionHologramCache cache = DimensionHologramRegistry.getByName(targetDim);
                    if (cache != null) {
                        int tx, ty, tz;
                        if (ctx.isSkyInverted()) {
                            tx = worldX;
                            int portalZ = net.nostalgia.client.render.PortalSkyRenderer.portalCenter.getZ();
                            tz = 2 * portalZ - worldZ;
                            int crackPlaneY = net.nostalgia.client.render.PortalSkyRenderer.crackPlaneY;
                            int crackPlaneYTarget = net.nostalgia.client.render.PortalSkyRenderer.crackPlaneYTarget;
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


        for (IHologramContext ctx : ACTIVE_CONTEXTS) {
            if (ctx.isActive() && ctx.isTerrainActive() && ctx.contains(worldX, y, worldZ)) {
                String targetDim = ctx.getTargetDimension();
                if (targetDim != null) {
                    DimensionHologramCache cache = DimensionHologramRegistry.getByName(targetDim);
                    if (cache != null) {
                        int tx, ty, tz;
                        if (ctx.isSkyInverted()) {
                            tx = worldX;
                            int portalZ = net.nostalgia.client.render.PortalSkyRenderer.portalCenter.getZ();
                            tz = 2 * portalZ - worldZ;
                            int crackPlaneY = net.nostalgia.client.render.PortalSkyRenderer.crackPlaneY;
                            int crackPlaneYTarget = net.nostalgia.client.render.PortalSkyRenderer.crackPlaneYTarget;
                            int inversionConstant = crackPlaneY + crackPlaneYTarget;
                            ty = inversionConstant - y;
                        } else {
                            tx = worldX + ctx.getOffsetX();
                            ty = y - ctx.getOffsetY();
                            tz = worldZ + ctx.getOffsetZ();
                        }
                        long targetPos = BlockPos.asLong(tx, ty, tz);
                        if (cache.hasOverrideRaw(targetPos)) {
                            return cache.getOverrideRaw(targetPos);
                        }
                    }
                }
            }
        }

        if (!cacheGenerated && !overworldCacheReady) return null;

        for (IHologramContext ctx : ACTIVE_CONTEXTS) {
            if (ctx.isActive() && ctx.contains(worldX, y, worldZ)) {

                if (!ctx.isTerrainActive()) {
                    return null;
                }

                int sourceX;
                int sourceZ;
                int sourceY;
                boolean isSkyInverted = ctx.isSkyInverted();
                if (isSkyInverted) {
                    net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                    boolean isTarget = mc.level != null && mc.level.dimension().identifier().toString().equals(net.nostalgia.client.render.PortalSkyRenderer.originalTargetDimension);
                    int crackPlaneY = net.nostalgia.client.render.PortalSkyRenderer.crackPlaneY;
                    int crackPlaneYTarget = net.nostalgia.client.render.PortalSkyRenderer.crackPlaneYTarget;
                    int inversionConstant = crackPlaneY + crackPlaneYTarget;

                    sourceX = worldX;
                    int portalZ = net.nostalgia.client.render.PortalSkyRenderer.portalCenter.getZ();
                    sourceZ = 2 * portalZ - worldZ;
                    int currentCrackPlaneY = isTarget ? crackPlaneYTarget : crackPlaneY;
                    if (y <= currentCrackPlaneY) return null;
                    sourceY = inversionConstant - y;
                } else {
                    sourceX = worldX + ctx.getOffsetX();
                    sourceZ = worldZ + ctx.getOffsetZ();
                    sourceY = y - ctx.getOffsetY();
                }

                return getBlockState(ctx.getTargetDimension(), sourceX, sourceY, sourceZ, isSkyInverted);
            }
        }

        return null;
    }

    @Override
    public net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome> getSpoofedBiome(int worldX, int y, int worldZ) {
        if (!cacheGenerated && !overworldCacheReady) return null;

        for (IHologramContext ctx : ACTIVE_CONTEXTS) {
            if (ctx.isActive() && ctx.contains(worldX, y, worldZ)) {
                
                if (!ctx.isTerrainActive()) {
                    return null;
                }

                int sourceX;
                int sourceZ;
                int sourceY;
                boolean isSkyInverted = ctx.isSkyInverted();
                if (isSkyInverted) {
                    net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                    boolean isTarget = mc.level != null && mc.level.dimension().identifier().toString().equals(net.nostalgia.client.render.PortalSkyRenderer.originalTargetDimension);
                    int crackPlaneY = net.nostalgia.client.render.PortalSkyRenderer.crackPlaneY;
                    int crackPlaneYTarget = net.nostalgia.client.render.PortalSkyRenderer.crackPlaneYTarget;
                    int inversionConstant = crackPlaneY + crackPlaneYTarget;

                    sourceX = worldX;
                    int portalZ = net.nostalgia.client.render.PortalSkyRenderer.portalCenter.getZ();
                    sourceZ = 2 * portalZ - worldZ;
                    int currentCrackPlaneY = isTarget ? crackPlaneYTarget : crackPlaneY;
                    if (y <= currentCrackPlaneY) return null;
                    sourceY = inversionConstant - y;
                } else {
                    sourceX = worldX + ctx.getOffsetX();
                    sourceZ = worldZ + ctx.getOffsetZ();
                    sourceY = y - ctx.getOffsetY();
                }

                if (net.nostalgia.alphalogic.ritual.DimensionUtil.isClientGenerated(ctx.getTargetDimension())) {
                } else {
                    DimensionHologramCache cache = DimensionHologramRegistry.getByName(ctx.getTargetDimension());
                    net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome> biome = cache != null ? cache.getSectionBiome(sourceX, sourceY, sourceZ) : null;
                    if (biome != null) {
                        return biome;
                    }
                }
                return null;
            }
        }

        return null;
    }

    private static long lastSpoofLogTime = 0;

    @Override
    public void getSpoofedBlockRange(int originX, int originY, int originZ, BlockState[] blockArray) {
        if (!cacheGenerated && !overworldCacheReady) return;


        java.util.List<IHologramContext> activeContexts = new java.util.ArrayList<>(2);
        for (int i = 0; i < ACTIVE_CONTEXTS.size(); i++) {
            IHologramContext ctx = ACTIVE_CONTEXTS.get(i);
            if (ctx.isActive()) {
                BlockPos c = ctx.getCenter();
                if (c != null) {
                    double r = ctx.getCollisionRadius();
                    double minSectionDistSq = 0;
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

        if (activeContexts.isEmpty()) return;


        class ContextCache {
            final IHologramContext ctx;
            final boolean isClientGen;
            final String targetDim;
            final int offsetX, offsetY, offsetZ;
            DimensionHologramCache dimCache;
            

            net.nostalgia.client.events.caches.providers.HologramSection lastSection = null;
            long lastSectionKey = -1;
            
            ContextCache(IHologramContext ctx) {
                this.ctx = ctx;
                this.targetDim = ctx.getTargetDimension();
                this.isClientGen = net.nostalgia.alphalogic.ritual.DimensionUtil.isClientGenerated(this.targetDim);
                this.offsetX = ctx.getOffsetX();
                this.offsetY = ctx.getOffsetY();
                this.offsetZ = ctx.getOffsetZ();
                
                if (this.targetDim != null) {
                    this.dimCache = DimensionHologramRegistry.getByName(this.targetDim);
                }
            }
        }

        java.util.List<ContextCache> caches = new java.util.ArrayList<>(activeContexts.size());
        for (int i = 0; i < activeContexts.size(); i++) {
            caches.add(new ContextCache(activeContexts.get(i)));
        }

        long now = System.currentTimeMillis();
        if (now - lastSpoofLogTime > 2000) {
            lastSpoofLogTime = now;
            String targetNames = "";
            for (int i = 0; i < caches.size(); i++) {
                targetNames += caches.get(i).targetDim + " ";
            }
            String msg = "[SHA-API] Sodium requested spoofed blocks at section: (" + originX + ", " + originY + ", " + originZ + ") targeting: " + targetNames;
            net.nostalgia.client.performance.SHAMetricsCollector.logEvent(msg);
            // org.slf4j.LoggerFactory.getLogger("UniversalHologramCache").info(msg);
        }

        long lastCachedChunkHash = -1;
        byte[] lastCachedAlphaData = null;


        for (int lx = 0; lx < 16; lx++) {
            int worldX = originX + lx;
            for (int lz = 0; lz < 16; lz++) {
                int worldZ = originZ + lz;
                for (int ly = 0; ly < 16; ly++) {
                    int worldY = originY + ly;
                    int i = (ly << 8) | (lz << 4) | lx;

                    for (int cIdx = 0; cIdx < caches.size(); cIdx++) {
                        ContextCache cc = caches.get(cIdx);
                        if (cc.ctx.contains(worldX, worldY, worldZ)) {
                            int tx, ty, tz;
                            boolean isSkyInverted = cc.ctx.isSkyInverted();
                            if (isSkyInverted) {
                                int crackPlaneY = net.nostalgia.client.render.PortalSkyRenderer.crackPlaneY;
                                int crackPlaneYTarget = net.nostalgia.client.render.PortalSkyRenderer.crackPlaneYTarget;
                                int inversionConstant = crackPlaneY + crackPlaneYTarget;
                                tx = worldX;
                                int portalZ = net.nostalgia.client.render.PortalSkyRenderer.portalCenter.getZ();
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
                                    blockArray[i] = override;
                                    break;
                                }
                            }
                            
                            if (!cc.ctx.isTerrainActive()) {
                                continue;
                            }
                            

                            if (cc.dimCache != null) {
                                long targetPos = BlockPos.asLong(tx, ty, tz);
                                BlockState delta = cc.dimCache.getOverrideRaw(targetPos);
                                if (delta != null) {
                                    blockArray[i] = delta;
                                    break;
                                }
                            }
                            

                            if (cc.isClientGen) {
                                int chunkX = tx >> 4;
                                int chunkZ = tz >> 4;
                                long chunkHash = net.minecraft.world.level.ChunkPos.pack(chunkX, chunkZ);
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
                                        DimensionHologramProvider provider = PROVIDERS.get(net.nostalgia.alphalogic.ritual.DimensionUtil.normalize(cc.targetDim));
                                        if (provider != null) {
                                            BlockState state = provider.getBlockState(blockId, isSkyInverted);
                                            if (state != null) {
                                                blockArray[i] = state;
                                                break;
                                            }
                                        }
                                    } else if (!isSkyInverted) {
                                        blockArray[i] = net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
                                        break;
                                    }
                                } else if (chunkData != null && !isSkyInverted) {
                                    blockArray[i] = net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
                                    break;
                                }

                            } else if (cc.dimCache != null) {

                                int tcx = tx >> 4, tsy = ty >> 4, tcz = tz >> 4;
                                long secKey = (((long) tcx & 0x3FFFFF) << 42) | (((long) tsy & 0xFFFFF) << 22) | ((long) tcz & 0x3FFFFF);
                                net.nostalgia.client.events.caches.providers.HologramSection section;
                                if (secKey == cc.lastSectionKey) {
                                    section = cc.lastSection;
                                } else {
                                    section = cc.dimCache.getSections().get(secKey);
                                    cc.lastSection = section;
                                    cc.lastSectionKey = secKey;
                                }
                                
                                if (section != null) {
                                    int tlx = tx & 15, tly = ty & 15, tlz = tz & 15;
                                    BlockState state = section.getBlockState(tlx, tly, tlz);
                                    if (state != null && !state.isAir()) {
                                        blockArray[i] = state;
                                        break;
                                    }

                                    if (!isSkyInverted) {
                                        blockArray[i] = net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
                                        break;
                                    }
                                } else if (!isSkyInverted) {
                                    blockArray[i] = net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
                                    break;
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
}
