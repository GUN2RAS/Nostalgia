/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class li
extends fn {
    public int a;
    public int b;
    public int c;
    public int d;
    public int e;

    public li() {
        this.j = true;
    }

    public void a(DataInputStream dataInputStream) {
        this.a = dataInputStream.readInt();
        this.b = dataInputStream.read();
        this.c = dataInputStream.readInt();
        this.d = dataInputStream.read();
        this.e = dataInputStream.read();
    }

    public void a(DataOutputStream dataOutputStream) {
        dataOutputStream.writeInt(this.a);
        dataOutputStream.write(this.b);
        dataOutputStream.writeInt(this.c);
        dataOutputStream.write(this.d);
        dataOutputStream.write(this.e);
    }

    public void a(lb lb2) {
        lb2.a(this);
    }

    public int a() {
        return 11;
    }
}

