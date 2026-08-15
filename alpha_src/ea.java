/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class ea
extends ee {
    private gh j;
    private gh l;
    private int m = 0;

    public ea(gh gh2, gh gh3) {
        int n2;
        int n3;
        this.j = gh2;
        this.l = gh3;
        this.f = false;
        int n4 = 222;
        int n5 = n4 - 108;
        this.m = gh3.c() / 9;
        this.h = n5 + this.m * 18;
        int n6 = (this.m - 4) * 18;
        for (n3 = 0; n3 < this.m; ++n3) {
            for (n2 = 0; n2 < 9; ++n2) {
                this.i.add(new mm(this, gh3, n2 + n3 * 9, 8 + n2 * 18, 18 + n3 * 18));
            }
        }
        for (n3 = 0; n3 < 3; ++n3) {
            for (n2 = 0; n2 < 9; ++n2) {
                this.i.add(new mm(this, gh2, n2 + (n3 + 1) * 9, 8 + n2 * 18, 103 + n3 * 18 + n6));
            }
        }
        for (n3 = 0; n3 < 9; ++n3) {
            this.i.add(new mm(this, gh2, n3, 8 + n3 * 18, 161 + n6));
        }
    }

    protected void j() {
        this.g.b(this.l.d(), 8, 6, 0x404040);
        this.g.b(this.j.d(), 8, this.h - 96 + 2, 0x404040);
    }

    protected void a(float f2) {
        int n2 = this.b.n.a("/gui/container.png");
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.b.n.b(n2);
        int n3 = (this.c - this.a) / 2;
        int n4 = (this.d - this.h) / 2;
        this.b(n3, n4, 0, 0, this.a, this.m * 18 + 17);
        this.b(n3, n4 + this.m * 18 + 17, 0, 126, this.a, 96);
    }
}

