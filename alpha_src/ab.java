/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import java.util.Random;
import org.lwjgl.opengl.GL11;

public class ab
extends ak {
    private bc d = new bc();
    private Random e = new Random();

    public ab() {
        this.b = 0.15f;
        this.c = 0.75f;
    }

    public void a(dx dx2, double d2, double d3, double d4, float f2, float f3) {
        this.e.setSeed(187L);
        ev ev2 = dx2.a;
        GL11.glPushMatrix();
        float f4 = eo.a(((float)dx2.b + f3) / 10.0f + dx2.d) * 0.1f + 0.1f;
        float f5 = (((float)dx2.b + f3) / 20.0f + dx2.d) * 57.295776f;
        int n2 = 1;
        if (dx2.a.a > 1) {
            n2 = 2;
        }
        if (dx2.a.a > 5) {
            n2 = 3;
        }
        if (dx2.a.a > 20) {
            n2 = 4;
        }
        GL11.glTranslatef((float)((float)d2), (float)((float)d3 + f4), (float)((float)d4));
        GL11.glEnable((int)32826);
        if (ev2.c < 256 && bc.a(ly.n[ev2.c].f())) {
            GL11.glRotatef((float)f5, (float)0.0f, (float)1.0f, (float)0.0f);
            this.a("/terrain.png");
            float f6 = 0.25f;
            if (!ly.n[ev2.c].c() && ev2.c != ly.al.bc) {
                f6 = 0.5f;
            }
            GL11.glScalef((float)f6, (float)f6, (float)f6);
            for (int i2 = 0; i2 < n2; ++i2) {
                GL11.glPushMatrix();
                if (i2 > 0) {
                    float f7 = (this.e.nextFloat() * 2.0f - 1.0f) * 0.2f / f6;
                    float f8 = (this.e.nextFloat() * 2.0f - 1.0f) * 0.2f / f6;
                    float f9 = (this.e.nextFloat() * 2.0f - 1.0f) * 0.2f / f6;
                    GL11.glTranslatef((float)f7, (float)f8, (float)f9);
                }
                this.d.a(ly.n[ev2.c]);
                GL11.glPopMatrix();
            }
        } else {
            GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
            int n3 = ev2.b();
            if (ev2.c < 256) {
                this.a("/terrain.png");
            } else {
                this.a("/gui/items.png");
            }
            ho ho2 = ho.a;
            float f10 = (float)(n3 % 16 * 16 + 0) / 256.0f;
            float f11 = (float)(n3 % 16 * 16 + 16) / 256.0f;
            float f12 = (float)(n3 / 16 * 16 + 0) / 256.0f;
            float f13 = (float)(n3 / 16 * 16 + 16) / 256.0f;
            float f14 = 1.0f;
            float f15 = 0.5f;
            float f16 = 0.25f;
            for (int i3 = 0; i3 < n2; ++i3) {
                GL11.glPushMatrix();
                if (i3 > 0) {
                    float f17 = (this.e.nextFloat() * 2.0f - 1.0f) * 0.3f;
                    float f18 = (this.e.nextFloat() * 2.0f - 1.0f) * 0.3f;
                    float f19 = (this.e.nextFloat() * 2.0f - 1.0f) * 0.3f;
                    GL11.glTranslatef((float)f17, (float)f18, (float)f19);
                }
                GL11.glRotatef((float)(180.0f - this.a.i), (float)0.0f, (float)1.0f, (float)0.0f);
                ho2.b();
                ho2.b(0.0f, 1.0f, 0.0f);
                ho2.a(0.0f - f15, 0.0f - f16, 0.0, f10, f13);
                ho2.a(f14 - f15, 0.0f - f16, 0.0, f11, f13);
                ho2.a(f14 - f15, 1.0f - f16, 0.0, f11, f12);
                ho2.a(0.0f - f15, 1.0f - f16, 0.0, f10, f12);
                ho2.a();
                GL11.glPopMatrix();
            }
        }
        GL11.glDisable((int)32826);
        GL11.glPopMatrix();
    }

    public void a(kd kd2, ey ey2, ev ev2, int n2, int n3) {
        if (ev2 == null) {
            return;
        }
        if (ev2.c < 256 && bc.a(ly.n[ev2.c].f())) {
            int n4 = ev2.c;
            ey2.b(ey2.a("/terrain.png"));
            ly ly2 = ly.n[n4];
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(n2 - 2), (float)(n3 + 3), (float)0.0f);
            GL11.glScalef((float)10.0f, (float)10.0f, (float)10.0f);
            GL11.glTranslatef((float)1.0f, (float)0.5f, (float)8.0f);
            GL11.glRotatef((float)210.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glRotatef((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            this.d.a(ly2);
            GL11.glPopMatrix();
        } else if (ev2.b() >= 0) {
            GL11.glDisable((int)2896);
            if (ev2.c < 256) {
                ey2.b(ey2.a("/terrain.png"));
            } else {
                ey2.b(ey2.a("/gui/items.png"));
            }
            this.a(n2, n3, ev2.b() % 16 * 16, ev2.b() / 16 * 16, 16, 16);
            GL11.glEnable((int)2896);
        }
        GL11.glEnable((int)2884);
    }

    public void b(kd kd2, ey ey2, ev ev2, int n2, int n3) {
        if (ev2 == null) {
            return;
        }
        if (ev2.a > 1) {
            String string = "" + ev2.a;
            GL11.glDisable((int)2896);
            GL11.glDisable((int)2929);
            kd2.a(string, n2 + 19 - 2 - kd2.a(string), n3 + 6 + 3, 0xFFFFFF);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)2929);
        }
        if (ev2.d > 0) {
            int n4 = 13 - ev2.d * 13 / ev2.d();
            int n5 = 255 - ev2.d * 255 / ev2.d();
            GL11.glDisable((int)2896);
            GL11.glDisable((int)2929);
            GL11.glDisable((int)3553);
            ho ho2 = ho.a;
            int n6 = 255 - n5 << 16 | n5 << 8;
            int n7 = (255 - n5) / 4 << 16 | 0x3F00;
            this.a(ho2, n2 + 2, n3 + 13, 13, 2, 0);
            this.a(ho2, n2 + 2, n3 + 13, 12, 1, n7);
            this.a(ho2, n2 + 2, n3 + 13, n4, 1, n6);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)2929);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    private void a(ho ho2, int n2, int n3, int n4, int n5, int n6) {
        ho2.b();
        ho2.b(n6);
        ho2.a((double)(n2 + 0), (double)(n3 + 0), 0.0);
        ho2.a((double)(n2 + 0), (double)(n3 + n5), 0.0);
        ho2.a((double)(n2 + n4), (double)(n3 + n5), 0.0);
        ho2.a((double)(n2 + n4), (double)(n3 + 0), 0.0);
        ho2.a();
    }

    public void a(int n2, int n3, int n4, int n5, int n6, int n7) {
        float f2 = 0.0f;
        float f3 = 0.00390625f;
        float f4 = 0.00390625f;
        ho ho2 = ho.a;
        ho2.b();
        ho2.a(n2 + 0, n3 + n7, f2, (float)(n4 + 0) * f3, (float)(n5 + n7) * f4);
        ho2.a(n2 + n6, n3 + n7, f2, (float)(n4 + n6) * f3, (float)(n5 + n7) * f4);
        ho2.a(n2 + n6, n3 + 0, f2, (float)(n4 + n6) * f3, (float)(n5 + 0) * f4);
        ho2.a(n2 + 0, n3 + 0, f2, (float)(n4 + 0) * f3, (float)(n5 + 0) * f4);
        ho2.a();
    }
}

