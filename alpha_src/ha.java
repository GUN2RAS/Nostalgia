/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ha
extends fn {
    public int a;
    public int b;
    public int c;
    public int d;
    public byte e;
    public byte f;
    public byte g;
    public int h;
    public int i;

    public ha() {
    }

    public ha(dx dx2) {
        this.a = dx2.ab;
        this.h = dx2.a.c;
        this.i = dx2.a.a;
        this.b = eo.b(dx2.ak * 32.0);
        this.c = eo.b(dx2.al * 32.0);
        this.d = eo.b(dx2.am * 32.0);
        this.e = (byte)(dx2.an * 128.0);
        this.f = (byte)(dx2.ao * 128.0);
        this.g = (byte)(dx2.ap * 128.0);
    }

    public void a(DataInputStream dataInputStream) {
        this.a = dataInputStream.readInt();
        this.h = dataInputStream.readShort();
        this.i = dataInputStream.readByte();
        this.b = dataInputStream.readInt();
        this.c = dataInputStream.readInt();
        this.d = dataInputStream.readInt();
        this.e = dataInputStream.readByte();
        this.f = dataInputStream.readByte();
        this.g = dataInputStream.readByte();
    }

    public void a(DataOutputStream dataOutputStream) {
        dataOutputStream.writeInt(this.a);
        dataOutputStream.writeShort(this.h);
        dataOutputStream.writeByte(this.i);
        dataOutputStream.writeInt(this.b);
        dataOutputStream.writeInt(this.c);
        dataOutputStream.writeInt(this.d);
        dataOutputStream.writeByte(this.e);
        dataOutputStream.writeByte(this.f);
        dataOutputStream.writeByte(this.g);
    }

    public void a(lb lb2) {
        lb2.a(this);
    }

    public int a() {
        return 22;
    }
}

