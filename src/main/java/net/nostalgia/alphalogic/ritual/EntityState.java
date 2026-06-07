package net.nostalgia.alphalogic.ritual;

import net.minecraft.world.phys.Vec3;

public record EntityState(Vec3 pos, float yRot, float xRot, Vec3 motion) {}
