package net.nostalgia.alphalogic.core;

public class AlphaAABB {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public AlphaAABB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public static AlphaAABB create(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new AlphaAABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public AlphaAABB setBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }

    public AlphaAABB expand(double x, double y, double z) {
        double d0 = this.minX;
        double d1 = this.minY;
        double d2 = this.minZ;
        double d3 = this.maxX;
        double d4 = this.maxY;
        double d5 = this.maxZ;
        if (x < 0.0) d0 += x;
        if (x > 0.0) d3 += x;
        if (y < 0.0) d1 += y;
        if (y > 0.0) d4 += y;
        if (z < 0.0) d2 += z;
        if (z > 0.0) d5 += z;
        return new AlphaAABB(d0, d1, d2, d3, d4, d5);
    }

    public AlphaAABB grow(double x, double y, double z) {
        double d0 = this.minX - x;
        double d1 = this.minY - y;
        double d2 = this.minZ - z;
        double d3 = this.maxX + x;
        double d4 = this.maxY + y;
        double d5 = this.maxZ + z;
        return new AlphaAABB(d0, d1, d2, d3, d4, d5);
    }

    public AlphaAABB offset(double x, double y, double z) {
        return new AlphaAABB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public double calculateXOffset(AlphaAABB other, double offsetX) {
        if (other.maxY <= this.minY || other.minY >= this.maxY) {
            return offsetX;
        }
        if (other.maxZ <= this.minZ || other.minZ >= this.maxZ) {
            return offsetX;
        }
        if (offsetX > 0.0 && other.maxX <= this.minX) {
            double maxOffset = this.minX - other.maxX;
            if (maxOffset < offsetX) return maxOffset;
        }
        if (offsetX < 0.0 && other.minX >= this.maxX) {
            double maxOffset = this.maxX - other.minX;
            if (maxOffset > offsetX) return maxOffset;
        }
        return offsetX;
    }

    public double calculateYOffset(AlphaAABB other, double offsetY) {
        if (other.maxX <= this.minX || other.minX >= this.maxX) {
            return offsetY;
        }
        if (other.maxZ <= this.minZ || other.minZ >= this.maxZ) {
            return offsetY;
        }
        if (offsetY > 0.0 && other.maxY <= this.minY) {
            double maxOffset = this.minY - other.maxY;
            if (maxOffset < offsetY) return maxOffset;
        }
        if (offsetY < 0.0 && other.minY >= this.maxY) {
            double maxOffset = this.maxY - other.minY;
            if (maxOffset > offsetY) return maxOffset;
        }
        return offsetY;
    }

    public double calculateZOffset(AlphaAABB other, double offsetZ) {
        if (other.maxX <= this.minX || other.minX >= this.maxX) {
            return offsetZ;
        }
        if (other.maxY <= this.minY || other.minY >= this.maxY) {
            return offsetZ;
        }
        if (offsetZ > 0.0 && other.maxZ <= this.minZ) {
            double maxOffset = this.minZ - other.maxZ;
            if (maxOffset < offsetZ) return maxOffset;
        }
        if (offsetZ < 0.0 && other.minZ >= this.maxZ) {
            double maxOffset = this.maxZ - other.minZ;
            if (maxOffset > offsetZ) return maxOffset;
        }
        return offsetZ;
    }

    public boolean intersects(AlphaAABB other) {
        if (other.maxX <= this.minX || other.minX >= this.maxX) {
            return false;
        }
        if (other.maxY <= this.minY || other.minY >= this.maxY) {
            return false;
        }
        return other.maxZ > this.minZ && other.minZ < this.maxZ;
    }

    public AlphaAABB move(double x, double y, double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }

    public double getAverageEdgeLength() {
        double d0 = this.maxX - this.minX;
        double d1 = this.maxY - this.minY;
        double d2 = this.maxZ - this.minZ;
        return (d0 + d1 + d2) / 3.0;
    }

    public AlphaAABB copy() {
        return new AlphaAABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public void set(AlphaAABB other) {
        this.minX = other.minX;
        this.minY = other.minY;
        this.minZ = other.minZ;
        this.maxX = other.maxX;
        this.maxY = other.maxY;
        this.maxZ = other.maxZ;
    }
}
