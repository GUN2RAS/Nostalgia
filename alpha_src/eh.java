/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class eh
extends fn {
    public double a;
    public double b;
    public double c;
    public double d;
    public float e;
    public float f;
    public boolean g;
    public boolean h;
    public boolean i;

    public eh() {
    }

    public eh(boolean bl2) {
        this.g = bl2;
    }

    public void a(lb lb2) {
        lb2.a(this);
    }

    public void a(DataInputStream dataInputStream) {
        this.g = dataInputStream.read() != 0;
    }

    public void a(DataOutputStream dataOutputStream) {
        dataOutputStream.write(this.g ? 1 : 0);
    }

    public int a() {
        return 1;
    }
}

