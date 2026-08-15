/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ez
extends fn {
    public int a;
    public byte b;
    public int c;
    public int d;
    public int e;
    public byte f;
    public byte g;

    public ez() {
    }

    public ez(ge ge2) {
        this.a = ge2.ab;
        this.b = (byte)ew.a(ge2);
        this.c = eo.b(ge2.ak * 32.0);
        this.d = eo.b(ge2.al * 32.0);
        this.e = eo.b(ge2.am * 32.0);
        this.f = (byte)(ge2.aq * 256.0f / 360.0f);
        this.g = (byte)(ge2.ar * 256.0f / 360.0f);
    }

    public void a(DataInputStream dataInputStream) {
        this.a = dataInputStream.readInt();
        this.b = dataInputStream.readByte();
        this.c = dataInputStream.readInt();
        this.d = dataInputStream.readInt();
        this.e = dataInputStream.readInt();
        this.f = dataInputStream.readByte();
        this.g = dataInputStream.readByte();
    }

    public void a(DataOutputStream dataOutputStream) {
        dataOutputStream.writeInt(this.a);
        dataOutputStream.writeByte(this.b);
        dataOutputStream.writeInt(this.c);
        dataOutputStream.writeInt(this.d);
        dataOutputStream.writeInt(this.e);
        dataOutputStream.writeByte(this.f);
        dataOutputStream.writeByte(this.g);
    }

    public void a(lb lb2) {
        lb2.a(this);
    }

    public int a() {
        return 19;
    }
}

