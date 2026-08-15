/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class z {
    public byte[] a = new byte[1024];
    public int b;
    public boolean c = false;
    public int d = 0;
    public int e = 1;
    public int f = 0;

    public z(int n2) {
        this.b = n2;
    }

    public void a() {
    }

    public void a(ey ey2) {
        if (this.f == 0) {
            GL11.glBindTexture((int)3553, (int)ey2.a("/terrain.png"));
        } else if (this.f == 1) {
            GL11.glBindTexture((int)3553, (int)ey2.a("/gui/items.png"));
        }
    }
}

