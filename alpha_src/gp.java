/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class gp
extends fn {
    public int a;
    public String b;
    public int c;
    public int d;
    public int e;
    public byte f;
    public byte g;
    public int h;

    public gp() {
    }

    public gp(dm dm2) {
        this.a = dm2.ab;
        this.b = dm2.i;
        this.c = eo.b(dm2.ak * 32.0);
        this.d = eo.b(dm2.al * 32.0);
        this.e = eo.b(dm2.am * 32.0);
        this.f = (byte)(dm2.aq * 256.0f / 360.0f);
        this.g = (byte)(dm2.ar * 256.0f / 360.0f);
        ev ev2 = dm2.b.a();
        this.h = ev2 == null ? 0 : ev2.c;
    }

    public void a(DataInputStream dataInputStream) {
        this.a = dataInputStream.readInt();
        this.b = dataInputStream.readUTF();
        this.c = dataInputStream.readInt();
        this.d = dataInputStream.readInt();
        this.e = dataInputStream.readInt();
        this.f = dataInputStream.readByte();
        this.g = dataInputStream.readByte();
        this.h = dataInputStream.readShort();
    }

    public void a(DataOutputStream dataOutputStream) {
        dataOutputStream.writeInt(this.a);
        dataOutputStream.writeUTF(this.b);
        dataOutputStream.writeInt(this.c);
        dataOutputStream.writeInt(this.d);
        dataOutputStream.writeInt(this.e);
        dataOutputStream.writeByte(this.f);
        dataOutputStream.writeByte(this.g);
        dataOutputStream.writeShort(this.h);
    }

    public void a(lb lb2) {
        lb2.a(this);
    }

    public int a() {
        return 28;
    }
}

