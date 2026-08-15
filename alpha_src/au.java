/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class au
extends bh {
    public void a() {
        this.e.clear();
        this.e.add(new fk(1, this.c / 2 - 100, this.d / 4 + 72, "Respawn"));
        this.e.add(new fk(2, this.c / 2 - 100, this.d / 4 + 96, "Title menu"));
        if (this.b.i == null) {
            ((fk)this.e.get((int)1)).g = false;
        }
    }

    protected void a(char c2, int n2) {
    }

    protected void a(fk fk2) {
        if (fk2.f == 0) {
            // empty if block
        }
        if (fk2.f == 1) {
            this.b.o();
            this.b.a((bh)null);
        }
        if (fk2.f == 2) {
            this.b.a((cn)null);
            this.b.a(new cx());
        }
    }

    public void a(int n2, int n3, float f2) {
        this.a(0, 0, this.c, this.d, 0x60500000, -1602211792);
        GL11.glPushMatrix();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        this.a(this.g, "Game over!", this.c / 2 / 2, 30, 0xFFFFFF);
        GL11.glPopMatrix();
        this.a(this.g, "Score: &e" + this.b.g.r(), this.c / 2, 100, 0xFFFFFF);
        super.a(n2, n3, f2);
    }

    public boolean b() {
        return false;
    }
}

