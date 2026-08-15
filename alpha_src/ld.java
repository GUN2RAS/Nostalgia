/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ld
extends fn {
    public int a;
    public int b;
    public int c;

    public void a(DataInputStream dataInputStream) {
        this.a = dataInputStream.readShort();
        this.b = dataInputStream.readByte();
        this.c = dataInputStream.readShort();
    }

    public void a(DataOutputStream dataOutputStream) {
        dataOutputStream.writeShort(this.a);
        dataOutputStream.writeByte(this.b);
        dataOutputStream.writeShort(this.c);
    }

    public void a(lb lb2) {
        lb2.a(this);
    }

    public int a() {
        return 5;
    }
}

