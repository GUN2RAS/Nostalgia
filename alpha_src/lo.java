/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class lo
extends ee {
    private n j;
    private float l;
    private float m;

    public lo(gh gh2, ev[] evArray) {
        int n2;
        int n3;
        this.f = true;
        this.j = new n(evArray);
        this.i.add(new an(this, this.j.a, this.j.b, 0, 144, 36));
        for (n3 = 0; n3 < 2; ++n3) {
            for (n2 = 0; n2 < 2; ++n2) {
                this.i.add(new mm(this, this.j.a, n2 + n3 * 2, 88 + n2 * 18, 26 + n3 * 18));
            }
        }
        for (n3 = 0; n3 < 4; ++n3) {
            n2 = n3;
            this.i.add(new lj(this, this, gh2, gh2.c() - 1 - n3, 8, 8 + n3 * 18, n2));
        }
        for (n3 = 0; n3 < 3; ++n3) {
            for (n2 = 0; n2 < 9; ++n2) {
                this.i.add(new mm(this, gh2, n2 + (n3 + 1) * 9, 8 + n2 * 18, 84 + n3 * 18));
            }
        }
        for (n3 = 0; n3 < 9; ++n3) {
            this.i.add(new mm(this, gh2, n3, 8 + n3 * 18, 142));
        }
    }

    protected void j() {
        this.g.b("Crafting", 86, 16, 0x404040);
    }

    public void a(int n2, int n3, float f2) {
        super.a(n2, n3, f2);
        this.l = n2;
        this.m = n3;
    }

    protected void a(float f2) {
        int n2 = this.b.n.a("/gui/inventory.png");
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.b.n.b(n2);
        int n3 = (this.c - this.a) / 2;
        int n4 = (this.d - this.h) / 2;
        this.b(n3, n4, 0, 0, this.a, this.h);
        GL11.glEnable((int)32826);
        GL11.glEnable((int)2903);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(n3 + 51), (float)(n4 + 75), (float)50.0f);
        float f3 = 30.0f;
        GL11.glScalef((float)(-f3), (float)f3, (float)f3);
        GL11.glRotatef((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        float f4 = this.b.g.n;
        float f5 = this.b.g.aq;
        float f6 = this.b.g.ar;
        float f7 = (float)(n3 + 51) - this.l;
        float f8 = (float)(n4 + 75 - 50) - this.m;
        GL11.glRotatef((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        i.b();
        GL11.glRotatef((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-((float)Math.atan(f8 / 40.0f)) * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        this.b.g.n = (float)Math.atan(f7 / 40.0f) * 20.0f;
        this.b.g.aq = (float)Math.atan(f7 / 40.0f) * 40.0f;
        this.b.g.ar = -((float)Math.atan(f8 / 40.0f)) * 20.0f;
        GL11.glTranslatef((float)0.0f, (float)this.b.g.aB, (float)0.0f);
        kx.a.a(this.b.g, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        this.b.g.n = f4;
        this.b.g.aq = f5;
        this.b.g.ar = f6;
        GL11.glPopMatrix();
        i.a();
        GL11.glDisable((int)32826);
    }
}

