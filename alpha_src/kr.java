/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInput;
import java.io.DataOutput;

public class kr
extends el {
    public double a;

    public kr() {
    }

    public kr(double d2) {
        this.a = d2;
    }

    void a(DataOutput dataOutput) {
        dataOutput.writeDouble(this.a);
    }

    void a(DataInput dataInput) {
        this.a = dataInput.readDouble();
    }

    public byte a() {
        return 6;
    }

    public String toString() {
        return "" + this.a;
    }
}

