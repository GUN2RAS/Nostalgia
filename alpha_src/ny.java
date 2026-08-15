/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ny
extends fn {
    public int a;
    public int b;
    public int c;
    public byte[] d;
    public hm e;

    public ny() {
        this.j = true;
    }

    public ny(int n2, int n3, int n4, ic ic2) {
        this.j = true;
        this.a = n2;
        this.b = n3;
        this.c = n4;
        this.e = new hm();
        ic2.b(this.e);
        try {
            this.d = x.a(this.e);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void a(DataInputStream dataInputStream) {
        this.a = dataInputStream.readInt();
        this.b = dataInputStream.readShort();
        this.c = dataInputStream.readInt();
        int n2 = dataInputStream.readShort() & 0xFFFF;
        this.d = new byte[n2];
        dataInputStream.readFully(this.d);
        this.e = x.a(this.d);
    }

    public void a(DataOutputStream dataOutputStream) {
        dataOutputStream.writeInt(this.a);
        dataOutputStream.writeShort(this.b);
        dataOutputStream.writeInt(this.c);
        dataOutputStream.writeShort((short)this.d.length);
        dataOutputStream.write(this.d);
    }

    public void a(lb lb2) {
        lb2.a(this);
    }

    public int a() {
        return this.d.length + 2 + 10;
    }
}

