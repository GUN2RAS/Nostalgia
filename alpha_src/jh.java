/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class jh {
    private Minecraft a;
    private ev b = null;
    private float c = 0.0f;
    private float d = 0.0f;
    private bc e = new bc();

    public jh(Minecraft minecraft) {
        this.a = minecraft;
    }

    public void a(ev ev2) {
        GL11.glPushMatrix();
        if (ev2.c < 256 && bc.a(ly.n[ev2.c].f())) {
            GL11.glBindTexture((int)3553, (int)this.a.n.a("/terrain.png"));
            this.e.a(ly.n[ev2.c]);
        } else {
            float f2;
            float f3;
            float f4;
            int n2;
            if (ev2.c < 256) {
                GL11.glBindTexture((int)3553, (int)this.a.n.a("/terrain.png"));
            } else {
                GL11.glBindTexture((int)3553, (int)this.a.n.a("/gui/items.png"));
            }
            ho ho2 = ho.a;
            float f5 = (float)(ev2.b() % 16 * 16 + 0) / 256.0f;
            float f6 = (float)(ev2.b() % 16 * 16 + 16) / 256.0f;
            float f7 = (float)(ev2.b() / 16 * 16 + 0) / 256.0f;
            float f8 = (float)(ev2.b() / 16 * 16 + 16) / 256.0f;
            float f9 = 1.0f;
            float f10 = 0.0f;
            float f11 = 0.3f;
            GL11.glEnable((int)32826);
            GL11.glTranslatef((float)(-f10), (float)(-f11), (float)0.0f);
            float f12 = 1.5f;
            GL11.glScalef((float)f12, (float)f12, (float)f12);
            GL11.glRotatef((float)50.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)335.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glTranslatef((float)-0.9375f, (float)-0.0625f, (float)0.0f);
            float f13 = 0.0625f;
            ho2.b();
            ho2.b(0.0f, 0.0f, 1.0f);
            ho2.a(0.0, 0.0, 0.0, f6, f8);
            ho2.a(f9, 0.0, 0.0, f5, f8);
            ho2.a(f9, 1.0, 0.0, f5, f7);
            ho2.a(0.0, 1.0, 0.0, f6, f7);
            ho2.a();
            ho2.b();
            ho2.b(0.0f, 0.0f, -1.0f);
            ho2.a(0.0, 1.0, 0.0f - f13, f6, f7);
            ho2.a(f9, 1.0, 0.0f - f13, f5, f7);
            ho2.a(f9, 0.0, 0.0f - f13, f5, f8);
            ho2.a(0.0, 0.0, 0.0f - f13, f6, f8);
            ho2.a();
            ho2.b();
            ho2.b(-1.0f, 0.0f, 0.0f);
            for (n2 = 0; n2 < 16; ++n2) {
                f4 = (float)n2 / 16.0f;
                f3 = f6 + (f5 - f6) * f4 - 0.001953125f;
                f2 = f9 * f4;
                ho2.a(f2, 0.0, 0.0f - f13, f3, f8);
                ho2.a(f2, 0.0, 0.0, f3, f8);
                ho2.a(f2, 1.0, 0.0, f3, f7);
                ho2.a(f2, 1.0, 0.0f - f13, f3, f7);
            }
            ho2.a();
            ho2.b();
            ho2.b(1.0f, 0.0f, 0.0f);
            for (n2 = 0; n2 < 16; ++n2) {
                f4 = (float)n2 / 16.0f;
                f3 = f6 + (f5 - f6) * f4 - 0.001953125f;
                f2 = f9 * f4 + 0.0625f;
                ho2.a(f2, 1.0, 0.0f - f13, f3, f7);
                ho2.a(f2, 1.0, 0.0, f3, f7);
                ho2.a(f2, 0.0, 0.0, f3, f8);
                ho2.a(f2, 0.0, 0.0f - f13, f3, f8);
            }
            ho2.a();
            ho2.b();
            ho2.b(0.0f, 1.0f, 0.0f);
            for (n2 = 0; n2 < 16; ++n2) {
                f4 = (float)n2 / 16.0f;
                f3 = f8 + (f7 - f8) * f4 - 0.001953125f;
                f2 = f9 * f4 + 0.0625f;
                ho2.a(0.0, f2, 0.0, f6, f3);
                ho2.a(f9, f2, 0.0, f5, f3);
                ho2.a(f9, f2, 0.0f - f13, f5, f3);
                ho2.a(0.0, f2, 0.0f - f13, f6, f3);
            }
            ho2.a();
            ho2.b();
            ho2.b(0.0f, -1.0f, 0.0f);
            for (n2 = 0; n2 < 16; ++n2) {
                f4 = (float)n2 / 16.0f;
                f3 = f8 + (f7 - f8) * f4 - 0.001953125f;
                f2 = f9 * f4;
                ho2.a(f9, f2, 0.0, f5, f3);
                ho2.a(0.0, f2, 0.0, f6, f3);
                ho2.a(0.0, f2, 0.0f - f13, f6, f3);
                ho2.a(f9, f2, 0.0f - f13, f5, f3);
            }
            ho2.a();
            GL11.glDisable((int)32826);
        }
        GL11.glPopMatrix();
    }

    public void a(float f2) {
        float f3 = this.d + (this.c - this.d) * f2;
        bi bi2 = this.a.g;
        GL11.glPushMatrix();
        GL11.glRotatef((float)(bi2.at + (bi2.ar - bi2.at) * f2), (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glRotatef((float)(bi2.as + (bi2.aq - bi2.as) * f2), (float)0.0f, (float)1.0f, (float)0.0f);
        i.b();
        GL11.glPopMatrix();
        float f4 = this.a.e.c(eo.b(bi2.ak), eo.b(bi2.al), eo.b(bi2.am));
        GL11.glColor4f((float)f4, (float)f4, (float)f4, (float)1.0f);
        if (this.b != null) {
            GL11.glPushMatrix();
            float f5 = 0.8f;
            float f6 = bi2.d(f2);
            float f7 = eo.a(f6 * (float)Math.PI);
            float f8 = eo.a(eo.c(f6) * (float)Math.PI);
            GL11.glTranslatef((float)(-f8 * 0.4f), (float)(eo.a(eo.c(f6) * (float)Math.PI * 2.0f) * 0.2f), (float)(-f7 * 0.2f));
            GL11.glTranslatef((float)(0.7f * f5), (float)(-0.65f * f5 - (1.0f - f3) * 0.6f), (float)(-0.9f * f5));
            GL11.glRotatef((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glEnable((int)32826);
            f6 = bi2.d(f2);
            f7 = eo.a(f6 * f6 * (float)Math.PI);
            f8 = eo.a(eo.c(f6) * (float)Math.PI);
            GL11.glRotatef((float)(-f7 * 20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)(-f8 * 20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glRotatef((float)(-f8 * 80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            f6 = 0.4f;
            GL11.glScalef((float)f6, (float)f6, (float)f6);
            this.a(this.b);
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            float f9 = 0.8f;
            float f10 = bi2.d(f2);
            float f11 = eo.a(f10 * (float)Math.PI);
            float f12 = eo.a(eo.c(f10) * (float)Math.PI);
            GL11.glTranslatef((float)(-f12 * 0.3f), (float)(eo.a(eo.c(f10) * (float)Math.PI * 2.0f) * 0.4f), (float)(-f11 * 0.4f));
            GL11.glTranslatef((float)(0.8f * f9), (float)(-0.75f * f9 - (1.0f - f3) * 0.6f), (float)(-0.9f * f9));
            GL11.glRotatef((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glEnable((int)32826);
            f10 = bi2.d(f2);
            f11 = eo.a(f10 * f10 * (float)Math.PI);
            f12 = eo.a(eo.c(f10) * (float)Math.PI);
            GL11.glRotatef((float)(f12 * 70.0f), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)(-f11 * 20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glBindTexture((int)3553, (int)this.a.n.a(this.a.g.aY, this.a.g.x()));
            GL11.glTranslatef((float)-1.0f, (float)3.6f, (float)3.5f);
            GL11.glRotatef((float)120.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glRotatef((float)200.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glRotatef((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glTranslatef((float)5.6f, (float)0.0f, (float)0.0f);
            ak ak2 = kx.a.a(this.a.g);
            bu bu2 = (bu)ak2;
            f12 = 1.0f;
            GL11.glScalef((float)f12, (float)f12, (float)f12);
            bu2.b();
            GL11.glPopMatrix();
        }
        GL11.glDisable((int)32826);
        i.a();
    }

    public void b(float f2) {
        int n2;
        GL11.glDisable((int)3008);
        if (this.a.g.aT > 0) {
            n2 = this.a.n.a("/terrain.png");
            GL11.glBindTexture((int)3553, (int)n2);
            this.d(f2);
        }
        if (this.a.g.I()) {
            n2 = eo.b(this.a.g.ak);
            int n3 = eo.b(this.a.g.al);
            int n4 = eo.b(this.a.g.am);
            int n5 = this.a.n.a("/terrain.png");
            GL11.glBindTexture((int)3553, (int)n5);
            int n6 = this.a.e.a(n2, n3, n4);
            if (ly.n[n6] != null) {
                this.a(f2, ly.n[n6].a(2));
            }
        }
        if (this.a.g.a(gb.f)) {
            n2 = this.a.n.a("/water.png");
            GL11.glBindTexture((int)3553, (int)n2);
            this.c(f2);
        }
        GL11.glEnable((int)3008);
    }

    private void a(float f2, int n2) {
        ho ho2 = ho.a;
        float f3 = this.a.g.a(f2);
        f3 = 0.1f;
        GL11.glColor4f((float)f3, (float)f3, (float)f3, (float)0.5f);
        GL11.glPushMatrix();
        float f4 = -1.0f;
        float f5 = 1.0f;
        float f6 = -1.0f;
        float f7 = 1.0f;
        float f8 = -0.5f;
        float f9 = 0.0078125f;
        float f10 = (float)(n2 % 16) / 256.0f - f9;
        float f11 = ((float)(n2 % 16) + 15.99f) / 256.0f + f9;
        float f12 = (float)(n2 / 16) / 256.0f - f9;
        float f13 = ((float)(n2 / 16) + 15.99f) / 256.0f + f9;
        ho2.b();
        ho2.a(f4, f6, f8, f11, f13);
        ho2.a(f5, f6, f8, f10, f13);
        ho2.a(f5, f7, f8, f10, f12);
        ho2.a(f4, f7, f8, f11, f12);
        ho2.a();
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private void c(float f2) {
        ho ho2 = ho.a;
        float f3 = this.a.g.a(f2);
        GL11.glColor4f((float)f3, (float)f3, (float)f3, (float)0.5f);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glPushMatrix();
        float f4 = 4.0f;
        float f5 = -1.0f;
        float f6 = 1.0f;
        float f7 = -1.0f;
        float f8 = 1.0f;
        float f9 = -0.5f;
        float f10 = -this.a.g.aq / 64.0f;
        float f11 = this.a.g.ar / 64.0f;
        ho2.b();
        ho2.a(f5, f7, f9, f4 + f10, f4 + f11);
        ho2.a(f6, f7, f9, 0.0f + f10, f4 + f11);
        ho2.a(f6, f8, f9, 0.0f + f10, 0.0f + f11);
        ho2.a(f5, f8, f9, f4 + f10, 0.0f + f11);
        ho2.a();
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)3042);
    }

    private void d(float f2) {
        ho ho2 = ho.a;
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.9f);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        float f3 = 1.0f;
        for (int i2 = 0; i2 < 2; ++i2) {
            GL11.glPushMatrix();
            int n2 = ly.as.bb + i2 * 16;
            int n3 = (n2 & 0xF) << 4;
            int n4 = n2 & 0xF0;
            float f4 = (float)n3 / 256.0f;
            float f5 = ((float)n3 + 15.99f) / 256.0f;
            float f6 = (float)n4 / 256.0f;
            float f7 = ((float)n4 + 15.99f) / 256.0f;
            float f8 = (0.0f - f3) / 2.0f;
            float f9 = f8 + f3;
            float f10 = 0.0f - f3 / 2.0f;
            float f11 = f10 + f3;
            float f12 = -0.5f;
            GL11.glTranslatef((float)((float)(-(i2 * 2 - 1)) * 0.24f), (float)-0.3f, (float)0.0f);
            GL11.glRotatef((float)((float)(i2 * 2 - 1) * 10.0f), (float)0.0f, (float)1.0f, (float)0.0f);
            ho2.b();
            ho2.a(f8, f10, f12, f5, f7);
            ho2.a(f9, f10, f12, f4, f7);
            ho2.a(f9, f11, f12, f4, f6);
            ho2.a(f8, f11, f12, f5, f6);
            ho2.a();
            GL11.glPopMatrix();
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)3042);
    }

    public void a() {
        float f2;
        this.d = this.c;
        bi bi2 = this.a.g;
        ev ev2 = bi2.b.a();
        ev ev3 = ev2;
        float f3 = ev3 == this.b ? 1.0f : 0.0f;
        float f4 = f3 - this.c;
        if (f4 < -(f2 = 0.4f)) {
            f4 = -f2;
        }
        if (f4 > f2) {
            f4 = f2;
        }
        this.c += f4;
        if (this.c < 0.1f) {
            this.b = ev3;
        }
    }

    public void b() {
        this.c = 0.0f;
    }

    public void c() {
        this.c = 0.0f;
    }
}

