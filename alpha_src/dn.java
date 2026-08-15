/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class dn
extends ak {
    protected fo d;
    protected fo e;

    public dn(fo fo2, float f2) {
        this.d = fo2;
        this.b = f2;
    }

    public void a(fo fo2) {
        this.e = fo2;
    }

    public void a(ge ge2, double d2, double d3, double d4, float f2, float f3) {
        GL11.glPushMatrix();
        GL11.glDisable((int)2884);
        this.d.k = this.c(ge2, f3);
        boolean bl2 = this.d.l = ge2.af != null;
        if (this.e != null) {
            this.e.l = this.d.l;
        }
        try {
            float f4;
            float f5 = ge2.o + (ge2.n - ge2.o) * f3;
            float f6 = ge2.as + (ge2.aq - ge2.as) * f3;
            float f7 = ge2.at + (ge2.ar - ge2.at) * f3;
            GL11.glTranslatef((float)((float)d2), (float)((float)d3), (float)((float)d4));
            float f8 = this.d(ge2, f3);
            GL11.glRotatef((float)(180.0f - f5), (float)0.0f, (float)1.0f, (float)0.0f);
            if (ge2.J > 0) {
                f4 = ((float)ge2.J + f3 - 1.0f) / 20.0f * 1.6f;
                if ((f4 = eo.c(f4)) > 1.0f) {
                    f4 = 1.0f;
                }
                GL11.glRotatef((float)(f4 * this.a(ge2)), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            f4 = 0.0625f;
            GL11.glEnable((int)32826);
            GL11.glScalef((float)-1.0f, (float)-1.0f, (float)1.0f);
            this.a(ge2, f3);
            GL11.glTranslatef((float)0.0f, (float)(-24.0f * f4 - 0.0078125f), (float)0.0f);
            float f9 = ge2.Q + (ge2.R - ge2.Q) * f3;
            float f10 = ge2.S - ge2.R * (1.0f - f3);
            if (f9 > 1.0f) {
                f9 = 1.0f;
            }
            this.a(ge2.aY, ge2.x());
            GL11.glEnable((int)3008);
            this.d.b(f10, f9, f8, f6 - f5, f7, f4);
            for (int i2 = 0; i2 < 4; ++i2) {
                if (!this.a(ge2, i2)) continue;
                this.e.b(f10, f9, f8, f6 - f5, f7, f4);
                GL11.glDisable((int)3042);
                GL11.glEnable((int)3008);
            }
            this.b(ge2, f3);
            float f11 = ge2.a(f3);
            int n2 = this.a(ge2, f11, f3);
            if ((n2 >> 24 & 0xFF) > 0 || ge2.G > 0 || ge2.J > 0) {
                GL11.glDisable((int)3553);
                GL11.glDisable((int)3008);
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glDepthFunc((int)514);
                if (ge2.G > 0 || ge2.J > 0) {
                    GL11.glColor4f((float)f11, (float)0.0f, (float)0.0f, (float)0.4f);
                    this.d.b(f10, f9, f8, f6 - f5, f7, f4);
                    for (int i3 = 0; i3 < 4; ++i3) {
                        if (!this.a(ge2, i3)) continue;
                        GL11.glColor4f((float)f11, (float)0.0f, (float)0.0f, (float)0.4f);
                        this.e.b(f10, f9, f8, f6 - f5, f7, f4);
                    }
                }
                if ((n2 >> 24 & 0xFF) > 0) {
                    float f12 = (float)(n2 >> 16 & 0xFF) / 255.0f;
                    float f13 = (float)(n2 >> 8 & 0xFF) / 255.0f;
                    float f14 = (float)(n2 & 0xFF) / 255.0f;
                    float f15 = (float)(n2 >> 24 & 0xFF) / 255.0f;
                    GL11.glColor4f((float)f12, (float)f13, (float)f14, (float)f15);
                    this.d.b(f10, f9, f8, f6 - f5, f7, f4);
                    for (int i4 = 0; i4 < 4; ++i4) {
                        if (!this.a(ge2, i4)) continue;
                        GL11.glColor4f((float)f12, (float)f13, (float)f14, (float)f15);
                        this.e.b(f10, f9, f8, f6 - f5, f7, f4);
                    }
                }
                GL11.glDepthFunc((int)515);
                GL11.glDisable((int)3042);
                GL11.glEnable((int)3008);
                GL11.glEnable((int)3553);
            }
            GL11.glDisable((int)32826);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        GL11.glEnable((int)2884);
        GL11.glPopMatrix();
    }

    protected float c(ge ge2, float f2) {
        return ge2.d(f2);
    }

    protected float d(ge ge2, float f2) {
        return (float)ge2.aR + f2;
    }

    protected void b(ge ge2, float f2) {
    }

    protected boolean a(ge ge2, int n2) {
        return false;
    }

    protected float a(ge ge2) {
        return 90.0f;
    }

    protected int a(ge ge2, float f2, float f3) {
        return 0;
    }

    protected void a(ge ge2, float f2) {
    }
}

