package net.nostalgia.alphalogic.core;

import java.util.ArrayList;
import java.util.List;

public class AlphaVec3D {
    
    public double x;
    public double y;
    public double z;

    public AlphaVec3D(double x, double y, double z) {
        if (x == -0.0) x = 0.0;
        if (y == -0.0) y = 0.0;
        if (z == -0.0) z = 0.0;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static AlphaVec3D create(double x, double y, double z) {
        return new AlphaVec3D(x, y, z);
    }

    public AlphaVec3D normalize() {
        double len = AlphaMathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        if (len < 1.0E-4) {
            return AlphaVec3D.create(0.0, 0.0, 0.0);
        }
        return AlphaVec3D.create(this.x / len, this.y / len, this.z / len);
    }

    public AlphaVec3D crossProduct(AlphaVec3D vec) {
        return AlphaVec3D.create(this.y * vec.z - this.z * vec.y, this.z * vec.x - this.x * vec.z, this.x * vec.y - this.y * vec.x);
    }

    public AlphaVec3D addVector(double x, double y, double z) {
        return AlphaVec3D.create(this.x + x, this.y + y, this.z + z);
    }

    public double distanceTo(AlphaVec3D vec) {
        double d2 = vec.x - this.x;
        double d3 = vec.y - this.y;
        double d4 = vec.z - this.z;
        return AlphaMathHelper.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
    }

    public double squareDistanceTo(AlphaVec3D vec) {
        double d2 = vec.x - this.x;
        double d3 = vec.y - this.y;
        double d4 = vec.z - this.z;
        return d2 * d2 + d3 * d3 + d4 * d4;
    }

    public double squareDistanceTo(double x, double y, double z) {
        double d5 = x - this.x;
        double d6 = y - this.y;
        double d7 = z - this.z;
        return d5 * d5 + d6 * d6 + d7 * d7;
    }

    public double length() {
        return AlphaMathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public void rotateAroundX(float angle) {
        float f3 = AlphaMathHelper.cos(angle);
        float f4 = AlphaMathHelper.sin(angle);
        double d2 = this.x;
        double d3 = this.y * (double)f3 + this.z * (double)f4;
        double d4 = this.z * (double)f3 - this.y * (double)f4;
        this.x = d2;
        this.y = d3;
        this.z = d4;
    }

    public void rotateAroundY(float angle) {
        float f3 = AlphaMathHelper.cos(angle);
        float f4 = AlphaMathHelper.sin(angle);
        double d2 = this.x * (double)f3 + this.z * (double)f4;
        double d3 = this.y;
        double d4 = this.z * (double)f3 - this.x * (double)f4;
        this.x = d2;
        this.y = d3;
        this.z = d4;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
}
