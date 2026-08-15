/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class du
extends fn {
    public long a;

    public void a(DataInputStream dataInputStream) {
        this.a = dataInputStream.readLong();
    }

    public void a(DataOutputStream dataOutputStream) {
        dataOutputStream.writeLong(this.a);
    }

    public void a(lb lb2) {
        lb2.a(this);
    }

    public int a() {
        return 8;
    }
}

