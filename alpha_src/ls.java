/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInput;
import java.io.DataOutput;

public class ls
extends el {
    public short a;

    public ls() {
    }

    public ls(short s2) {
        this.a = s2;
    }

    void a(DataOutput dataOutput) {
        dataOutput.writeShort(this.a);
    }

    void a(DataInput dataInput) {
        this.a = dataInput.readShort();
    }

    public byte a() {
        return 2;
    }

    public String toString() {
        return "" + this.a;
    }
}

