/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ij
extends fn {
    public String a;

    public ij() {
    }

    public ij(String string) {
        this.a = string;
    }

    public void a(DataInputStream dataInputStream) {
        this.a = dataInputStream.readUTF();
    }

    public void a(DataOutputStream dataOutputStream) {
        dataOutputStream.writeUTF(this.a);
    }

    public void a(lb lb2) {
        lb2.a(this);
    }

    public int a() {
        return this.a.length();
    }
}

