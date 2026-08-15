/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class jl
extends fn {
    public int a;
    public int b;
    public int c;
    public int d;
    public byte e;
    public byte f;

    public jl() {
    }

    public jl(kh kh2) {
        this.a = kh2.ab;
        this.b = eo.b(kh2.ak * 32.0);
        this.c = eo.b(kh2.al * 32.0);
        this.d = eo.b(kh2.am * 32.0);
        this.e = (byte)(kh2.aq * 256.0f / 360.0f);
        this.f = (byte)(kh2.ar * 256.0f / 360.0f);
    }

    public void a(DataInputStream dataInputStream) {
        this.a = dataInputStream.readInt();
        this.b = dataInputStream.readInt();
        this.c = dataInputStream.readInt();
        this.d = dataInputStream.readInt();
        this.e = (byte)dataInputStream.read();
        this.f = (byte)dataInputStream.read();
    }

    public void a(DataOutputStream dataOutputStream) {
        dataOutputStream.writeInt(this.a);
        dataOutputStream.writeInt(this.b);
        dataOutputStream.writeInt(this.c);
        dataOutputStream.writeInt(this.d);
        dataOutputStream.write(this.e);
        dataOutputStream.write(this.f);
    }

    public void a(lb lb2) {
        lb2.a(this);
    }

    public int a() {
        return 34;
    }
}

