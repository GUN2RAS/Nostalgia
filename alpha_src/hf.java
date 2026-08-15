/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class hf
extends fn {
    public int a;
    public int b;

    public hf() {
    }

    public hf(kh kh2, int n2) {
        this.a = kh2.ab;
        this.b = n2;
    }

    public void a(DataInputStream dataInputStream) {
        this.a = dataInputStream.readInt();
        this.b = dataInputStream.readByte();
    }

    public void a(DataOutputStream dataOutputStream) {
        dataOutputStream.writeInt(this.a);
        dataOutputStream.writeByte(this.b);
    }

    public void a(lb lb2) {
        lb2.a(this);
    }

    public int a() {
        return 5;
    }
}

