/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class do
extends fn {
    public int a;
    public int b;
    public int c;
    public int d;
    public int e;

    public do() {
    }

    public do(int n2, int n3, int n4, int n5, int n6) {
        this.a = n2;
        this.b = n3;
        this.c = n4;
        this.d = n5;
        this.e = n6;
    }

    public void a(DataInputStream dataInputStream) {
        this.a = dataInputStream.readShort();
        this.b = dataInputStream.readInt();
        this.c = dataInputStream.read();
        this.d = dataInputStream.readInt();
        this.e = dataInputStream.read();
    }

    public void a(DataOutputStream dataOutputStream) {
        dataOutputStream.writeShort(this.a);
        dataOutputStream.writeInt(this.b);
        dataOutputStream.write(this.c);
        dataOutputStream.writeInt(this.d);
        dataOutputStream.write(this.e);
    }

    public void a(lb lb2) {
        lb2.a(this);
    }

    public int a() {
        return 12;
    }
}

