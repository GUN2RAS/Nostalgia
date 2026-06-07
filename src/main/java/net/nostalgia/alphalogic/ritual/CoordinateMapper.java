package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;

public final class CoordinateMapper {

    private CoordinateMapper() {}

    public static BlockPos sourceToTarget(BlockPos sourcePos, int offsetX, int yOffset, int offsetZ, boolean inverted, int maxY) {
        int ax = sourcePos.getX() + offsetX;
        int ay;
        int az;
        if (inverted) {
            SkyPortalEventInstance portal = SkyPortalManager.getActive();
            if (portal != null) {
                ay = portal.crackPlaneY() + portal.crackPlaneYTarget() - sourcePos.getY();
                az = 2 * portal.center().getZ() - sourcePos.getZ();
            } else {
                ay = maxY - sourcePos.getY();
                az = sourcePos.getZ() + offsetZ;
            }
        } else {
            ay = sourcePos.getY() - yOffset;
            az = sourcePos.getZ() + offsetZ;
        }
        return new BlockPos(ax, ay, az);
    }

    public static BlockPos sourceToTarget(int wx, int wy, int wz, int offsetX, int yOffset, int offsetZ, boolean inverted, int maxY) {
        int ax = wx + offsetX;
        int ay;
        int az;
        if (inverted) {
            SkyPortalEventInstance portal = SkyPortalManager.getActive();
            if (portal != null) {
                ay = portal.crackPlaneY() + portal.crackPlaneYTarget() - wy;
                az = 2 * portal.center().getZ() - wz;
            } else {
                ay = maxY - wy;
                az = wz + offsetZ;
            }
        } else {
            ay = wy - yOffset;
            az = wz + offsetZ;
        }
        return new BlockPos(ax, ay, az);
    }

    public static BlockPos targetToSource(BlockPos targetPos, int offsetX, int yOffset, int offsetZ, boolean inverted, int maxY) {
        int wx = targetPos.getX() - offsetX;
        int wy;
        int wz;
        if (inverted) {
            SkyPortalEventInstance portal = SkyPortalManager.getActive();
            if (portal != null) {
                wy = portal.crackPlaneY() + portal.crackPlaneYTarget() - targetPos.getY();
                wz = 2 * portal.center().getZ() - targetPos.getZ();
            } else {
                wy = maxY - targetPos.getY();
                wz = targetPos.getZ() - offsetZ;
            }
        } else {
            wy = targetPos.getY() + yOffset;
            wz = targetPos.getZ() - offsetZ;
        }
        return new BlockPos(wx, wy, wz);
    }

    public static long targetToSourcePacked(BlockPos targetPos, int offsetX, int yOffset, int offsetZ, boolean inverted, int maxY) {
        int wx = targetPos.getX() - offsetX;
        int wy;
        int wz;
        if (inverted) {
            SkyPortalEventInstance portal = SkyPortalManager.getActive();
            if (portal != null) {
                wy = portal.crackPlaneY() + portal.crackPlaneYTarget() - targetPos.getY();
                wz = 2 * portal.center().getZ() - targetPos.getZ();
            } else {
                wy = maxY - targetPos.getY();
                wz = targetPos.getZ() - offsetZ;
            }
        } else {
            wy = targetPos.getY() + yOffset;
            wz = targetPos.getZ() - offsetZ;
        }
        return BlockPos.asLong(wx, wy, wz);
    }

    public static long sourceToTargetPacked(BlockPos sourcePos, int offsetX, int yOffset, int offsetZ, boolean inverted, int maxY) {
        int ax = sourcePos.getX() + offsetX;
        int ay;
        int az;
        if (inverted) {
            SkyPortalEventInstance portal = SkyPortalManager.getActive();
            if (portal != null) {
                ay = portal.crackPlaneY() + portal.crackPlaneYTarget() - sourcePos.getY();
                az = 2 * portal.center().getZ() - sourcePos.getZ();
            } else {
                ay = maxY - sourcePos.getY();
                az = sourcePos.getZ() + offsetZ;
            }
        } else {
            ay = sourcePos.getY() - yOffset;
            az = sourcePos.getZ() + offsetZ;
        }
        return BlockPos.asLong(ax, ay, az);
    }
}
