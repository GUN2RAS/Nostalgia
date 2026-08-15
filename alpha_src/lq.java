/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class lq
extends fn {
    public int a;
    public byte b;
    public byte c;
    public byte d;
    public byte e;
    public byte f;
    public boolean g = false;

    public void a(DataInputStream dataInputStream) {
        this.a = dataInputStream.readInt();
    }

    public void a(DataOutputStream dataOutputStream) {
        dataOutputStream.writeInt(this.a);
    }

    public void a(lb lb2) {
        lb2.a(this);
    }

    public int a() {
        return 4;
    }
}

