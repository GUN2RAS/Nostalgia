/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInput;
import java.io.DataOutput;

public class ix
extends el {
    public byte a;

    public ix() {
    }

    public ix(byte by2) {
        this.a = by2;
    }

    void a(DataOutput dataOutput) {
        dataOutput.writeByte(this.a);
    }

    void a(DataInput dataInput) {
        this.a = dataInput.readByte();
    }

    public byte a() {
        return 1;
    }

    public String toString() {
        return "" + this.a;
    }
}

