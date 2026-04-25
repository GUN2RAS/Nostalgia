package net.nostalgia.client.render;

import com.mojang.blaze3d.vertex.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.nostalgia.alphalogic.bridge.AlphaEngineManager;
import net.nostalgia.alphalogic.gen.AlphaLevelSource;
import org.joml.Matrix4f;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CompletableFuture;

@Environment(EnvType.CLIENT)
public class FakeTerrainRenderer {

    private record AlphaBlockInfo(BlockPos pos, byte blockId) {}
    private static final List<AlphaBlockInfo> hologramBlocks = new CopyOnWriteArrayList<>();

    private static volatile long cachedVBOPointer = 0;
    private static volatile int cachedVBOSize = 0;
    private static volatile int[] indexCountByRadius = new int[500];
    private static volatile com.mojang.blaze3d.vertex.MeshData.DrawState cachedDrawState = null;

    private static net.minecraft.world.level.block.state.BlockState getStateForAlphaId(byte id) {
        net.minecraft.world.level.block.state.BlockState state = net.nostalgia.block.AlphaBlocks.ALPHA_STONE.defaultBlockState();
        switch (id) {
            case 1: state = net.nostalgia.block.AlphaBlocks.ALPHA_STONE.defaultBlockState(); break;
            case 2: state = net.nostalgia.block.AlphaBlocks.ALPHA_GRASS_BLOCK.defaultBlockState(); break;
            case 3: state = net.nostalgia.block.AlphaBlocks.ALPHA_DIRT.defaultBlockState(); break;
            case 4: state = net.nostalgia.block.AlphaBlocks.ALPHA_COBBLESTONE.defaultBlockState(); break;
            case 12: state = net.nostalgia.block.AlphaBlocks.ALPHA_SAND.defaultBlockState(); break;
            case 13: state = net.nostalgia.block.AlphaBlocks.ALPHA_GRAVEL.defaultBlockState(); break;
            case 17: state = net.nostalgia.block.AlphaBlocks.ALPHA_OAK_LOG.defaultBlockState(); break;
            case 18: state = net.nostalgia.block.AlphaBlocks.ALPHA_LEAVES.defaultBlockState(); break;
        }
        return state;
    }

    public static void generateHologram(String dimensionId) {
        hologramBlocks.clear();
        if (!"alpha".equals(dimensionId)) return;

        if (!"alpha".equals(dimensionId)) return;
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;

        BlockPos playerPos = client.player.blockPosition();

        CompletableFuture.runAsync(() -> {
            try {
                long seed = AlphaEngineManager.getWorldSeed();
                AlphaLevelSource source = new AlphaLevelSource(seed);

                int cx = playerPos.getX() >> 4;
                int cz = playerPos.getZ() >> 4;

                for (int x = cx - 1; x <= cx + 1; x++) {
                    for (int z = cz - 1; z <= cz + 1; z++) {
                        byte[] blocks = new byte[32768];
                        source.generateTerrain(x, z, blocks);
                        source.replaceBlocksForBiome(x, z, blocks);

                        for (int i = 0; i < 16; i++) {
                            for (int j = 0; j < 16; j++) {
                                for (int k = 127; k >= 0; k--) {
                                    int index = (i * 16 + j) * 128 + k;
                                    byte block = blocks[index];
                                    if (block != 0) {
                                        boolean isTransparent = false;

                                        if (k == 127 || blocks[index + 1] == 0 || blocks[index + 1] == 8 || blocks[index + 1] == 9 || blocks[index + 1] == 18) {
                                            isTransparent = true;
                                        } else if (k == 0 || blocks[index - 1] == 0 || blocks[index - 1] == 8 || blocks[index - 1] == 9 || blocks[index - 1] == 18) {
                                            isTransparent = true;
                                        } else if (i == 15 || blocks[((i + 1) * 16 + j) * 128 + k] == 0 || blocks[((i + 1) * 16 + j) * 128 + k] == 8 || blocks[((i + 1) * 16 + j) * 128 + k] == 9 || blocks[((i + 1) * 16 + j) * 128 + k] == 18) {
                                            isTransparent = true;
                                        } else if (i == 0 || blocks[((i - 1) * 16 + j) * 128 + k] == 0 || blocks[((i - 1) * 16 + j) * 128 + k] == 8 || blocks[((i - 1) * 16 + j) * 128 + k] == 9 || blocks[((i - 1) * 16 + j) * 128 + k] == 18) {
                                            isTransparent = true;
                                        } else if (j == 15 || blocks[(i * 16 + (j + 1)) * 128 + k] == 0 || blocks[(i * 16 + (j + 1)) * 128 + k] == 8 || blocks[(i * 16 + (j + 1)) * 128 + k] == 9 || blocks[(i * 16 + (j + 1)) * 128 + k] == 18) {
                                            isTransparent = true;
                                        } else if (j == 0 || blocks[(i * 16 + (j - 1)) * 128 + k] == 0 || blocks[(i * 16 + (j - 1)) * 128 + k] == 8 || blocks[(i * 16 + (j - 1)) * 128 + k] == 9 || blocks[(i * 16 + (j - 1)) * 128 + k] == 18) {
                                            isTransparent = true;
                                        }

                                        if (isTransparent) {
                                            hologramBlocks.add(new AlphaBlockInfo(new BlockPos(x * 16 + i, k, z * 16 + j), block));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                int highestY = 0;
                for (AlphaBlockInfo info : hologramBlocks) {
                    if (info.pos.getX() == playerPos.getX() && info.pos.getZ() == playerPos.getZ()) {
                        if (info.pos.getY() > highestY) highestY = info.pos.getY();
                    }
                }
                if (highestY < 5) highestY = 64;
                net.nostalgia.client.ritual.RitualVisualManager.yOffset = playerPos.getY() - highestY;

                rebuildMesh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void clear() {
        hologramBlocks.clear();
        if (cachedVBOPointer != 0) {
            org.lwjgl.system.MemoryUtil.nmemFree(cachedVBOPointer);
            cachedVBOPointer = 0;
        }
        cachedVBOSize = 0;
        cachedDrawState = null;
        java.util.Arrays.fill(indexCountByRadius, 0);
    }

    public static java.util.List<net.minecraft.world.phys.AABB> getHologramCollisionBoxes() {
        if (hologramBlocks.isEmpty()) return java.util.List.of();
        java.util.List<net.minecraft.world.phys.AABB> boxes = new java.util.ArrayList<>();
        int yOffset = net.nostalgia.client.ritual.RitualVisualManager.yOffset;
        for (AlphaBlockInfo info : hologramBlocks) {
             boxes.add(new net.minecraft.world.phys.AABB(info.pos.getX(), info.pos.getY() + yOffset, info.pos.getZ(), info.pos.getX() + 1, info.pos.getY() + yOffset + 1, info.pos.getZ() + 1));
        }
        return boxes;
    }

    private static void rebuildMesh() {
        java.util.Map<Integer, java.util.List<net.minecraft.client.resources.model.geometry.BakedQuad>> quadsByRadius = new java.util.HashMap<>();
        Minecraft client = Minecraft.getInstance();
        net.minecraft.client.renderer.block.BlockStateModelSet bsmSet = client.getModelManager().getBlockStateModelSet();
        net.minecraft.util.RandomSource random = net.minecraft.util.RandomSource.create();
        
        BlockPos center = net.nostalgia.client.ritual.RitualVisualManager.ritualCenter;
        int yOffset = net.nostalgia.client.ritual.RitualVisualManager.yOffset;
        
        for (AlphaBlockInfo info : hologramBlocks) {
            int dx = Math.abs(info.pos.getX() - center.getX());
            int dy = Math.abs(info.pos.getY() - center.getY());
            int dz = Math.abs(info.pos.getZ() - center.getZ());
            int chebyshev = Math.max(dx, Math.max(dy, dz));
            
            java.util.List<net.minecraft.client.resources.model.geometry.BakedQuad> tier = quadsByRadius.computeIfAbsent(chebyshev, k -> new java.util.ArrayList<>());
            
            net.minecraft.world.level.block.state.BlockState state = getStateForAlphaId(info.blockId);
            net.minecraft.client.renderer.block.dispatch.BlockStateModel model = bsmSet.get(state);
            
            float qx = info.pos.getX();
            float qy = info.pos.getY() + yOffset;
            float qz = info.pos.getZ();
            
            java.util.List<net.minecraft.client.resources.model.geometry.BakedQuad> quads = new java.util.ArrayList<>();
            random.setSeed(42L);
            java.util.List<net.minecraft.client.renderer.block.dispatch.BlockStateModelPart> parts = new java.util.ArrayList<>(); model.collectParts(random, parts);
            for (net.minecraft.client.renderer.block.dispatch.BlockStateModelPart part : parts) {
                quads.addAll(part.getQuads(null));
                for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.values()) {
                    quads.addAll(part.getQuads(dir));
                }
            }
            
            for (net.minecraft.client.resources.model.geometry.BakedQuad q : quads) {
                net.minecraft.client.resources.model.geometry.BakedQuad translated = new net.minecraft.client.resources.model.geometry.BakedQuad(
                    new org.joml.Vector3f(q.position0()).add(qx, qy, qz),
                    new org.joml.Vector3f(q.position1()).add(qx, qy, qz),
                    new org.joml.Vector3f(q.position2()).add(qx, qy, qz),
                    new org.joml.Vector3f(q.position3()).add(qx, qy, qz),
                    q.packedUV0(), q.packedUV1(), q.packedUV2(), q.packedUV3(),
                    q.direction(), q.materialInfo()
                );
                tier.add(translated);
            }
        }

        com.mojang.blaze3d.vertex.ByteBufferBuilder byteBufferBuilder = new com.mojang.blaze3d.vertex.ByteBufferBuilder(4194304);
        com.mojang.blaze3d.vertex.BufferBuilder bufferBuilder = new com.mojang.blaze3d.vertex.BufferBuilder(byteBufferBuilder, com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS, com.mojang.blaze3d.vertex.DefaultVertexFormat.BLOCK);
        
        int totalIndices = 0;
        int[] newIndexCountByRadius = new int[500];

        com.mojang.blaze3d.vertex.QuadInstance quadInstance = new com.mojang.blaze3d.vertex.QuadInstance();
        for (int i = 0; i < 500; i++) {
            java.util.List<net.minecraft.client.resources.model.geometry.BakedQuad> tier = quadsByRadius.get(i);
            if (tier != null) {
                for (net.minecraft.client.resources.model.geometry.BakedQuad quad : tier) {
                    bufferBuilder.putBlockBakedQuad(0, 0, 0, quad, quadInstance);
                    totalIndices += 6;
                }
            }
            newIndexCountByRadius[i] = totalIndices;
        }

        if (totalIndices > 0) {
            com.mojang.blaze3d.vertex.MeshData mesh = bufferBuilder.buildOrThrow();
            java.nio.ByteBuffer vb = mesh.vertexBuffer();
            int size = vb.remaining();
            long pointer = org.lwjgl.system.MemoryUtil.nmemAlloc(size);
            org.lwjgl.system.MemoryUtil.memCopy(org.lwjgl.system.MemoryUtil.memAddress(vb), pointer, size);
            
            long oldPointer = cachedVBOPointer;
            cachedVBOPointer = pointer;
            cachedVBOSize = size;
            cachedDrawState = mesh.drawState();
            indexCountByRadius = newIndexCountByRadius;
            
            if (oldPointer != 0) {
                org.lwjgl.system.MemoryUtil.nmemFree(oldPointer);
            }
            
            mesh.close();
        }
    }

    public static void render(Camera camera, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        if (hologramBlocks.isEmpty() || cachedVBOPointer == 0 || cachedDrawState == null || net.nostalgia.client.ritual.RitualVisualManager.isBystander) return;

        float globalAlpha = Math.min(net.nostalgia.client.ritual.RitualVisualManager.getTransitionTimeSeconds() / 3.0f, 1.0f);
        if (globalAlpha <= 0.05f) return;

        float transitionTime = net.nostalgia.client.ritual.RitualVisualManager.getTransitionTimeSeconds();
        float currentRadius = 0.0f;
        if (transitionTime > 5.0f) {
            float progress = Math.min((transitionTime - 5.0f) / 4.0f, 1.0f);
            currentRadius = progress * 250.0f;
        }
        
        int indicesToDraw = indexCountByRadius[Math.min((int)currentRadius, 499)];
        if (indicesToDraw <= 0) return;

        org.lwjgl.opengl.GL11.glClear(org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT);

        com.mojang.blaze3d.vertex.ByteBufferBuilder vBuilder = new com.mojang.blaze3d.vertex.ByteBufferBuilder(cachedVBOSize + 1024);
        long vPtr = vBuilder.reserve(cachedVBOSize);
        org.lwjgl.system.MemoryUtil.memCopy(cachedVBOPointer, vPtr, cachedVBOSize);
        com.mojang.blaze3d.vertex.ByteBufferBuilder.Result vResult = vBuilder.build();

        com.mojang.blaze3d.vertex.MeshData.DrawState slicedState = new com.mojang.blaze3d.vertex.MeshData.DrawState(
            cachedDrawState.format(), 
            cachedDrawState.vertexCount(), 
            indicesToDraw, 
            cachedDrawState.mode(), 
            cachedDrawState.indexType()
        );

        com.mojang.blaze3d.vertex.MeshData frameMesh = new com.mojang.blaze3d.vertex.MeshData(vResult, slicedState);

        net.minecraft.client.renderer.rendertype.RenderType type = net.minecraft.client.renderer.rendertype.RenderTypes.solidMovingBlock();

        com.mojang.blaze3d.systems.RenderSystem.getModelViewStack().pushMatrix();
        com.mojang.blaze3d.systems.RenderSystem.getModelViewStack().identity();
        com.mojang.blaze3d.systems.RenderSystem.getModelViewStack().mul(viewMatrix);
        net.minecraft.world.phys.Vec3 camPos = camera.position();
        com.mojang.blaze3d.systems.RenderSystem.getModelViewStack().translate((float)-camPos.x, (float)-camPos.y, (float)-camPos.z);

        type.draw(frameMesh);
        
        com.mojang.blaze3d.systems.RenderSystem.getModelViewStack().popMatrix();
    }
}
