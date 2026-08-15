/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class bu
extends dn {
    private cr f;
    private cr g;
    private cr h;
    private static final String[] i = new String[]{"cloth", "chain", "iron", "diamond", "gold"};

    public bu() {
        super(new cr(0.0f), 0.5f);
        this.f = (cr)this.d;
        this.g = new cr(1.0f);
        this.h = new cr(0.5f);
    }

    protected boolean a(dm dm2, int n2) {
        di di2;
        ev ev2 = dm2.b.d(3 - n2);
        if (ev2 != null && (di2 = ev2.a()) instanceof mr) {
            mr mr2 = (mr)di2;
            this.a("/armor/" + i[mr2.aZ] + "_" + (n2 == 2 ? 2 : 1) + ".png");
            cr cr2 = n2 == 2 ? this.h : this.g;
            cr2.a.h = n2 == 0;
            cr2.b.h = n2 == 0;
            cr2.c.h = n2 == 1 || n2 == 2;
            cr2.d.h = n2 == 1;
            cr2.e.h = n2 == 1;
            cr2.f.h = n2 == 2 || n2 == 3;
            cr2.g.h = n2 == 2 || n2 == 3;
            this.a(cr2);
            return true;
        }
        return false;
    }

    public void a(dm dm2, double d2, double d3, double d4, float f2, float f3) {
        ev ev2 = dm2.b.a();
        this.f.i = ev2 != null;
        this.h.i = this.f.i;
        this.g.i = this.f.i;
        this.h.j = this.f.j = dm2.o();
        this.g.j = this.f.j;
        super.a(dm2, d2, d3 - (double)dm2.aB, d4, f2, f3);
        this.f.j = false;
        this.h.j = false;
        this.g.j = false;
        this.f.i = false;
        this.h.i = false;
        this.g.i = false;
        kd kd2 = this.a();
        float f4 = 1.6f;
        float f5 = 0.016666668f * f4;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)d2 + 0.0f), (float)((float)d3 + 2.3f), (float)((float)d4));
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-this.a.i), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)this.a.j, (float)1.0f, (float)0.0f, (float)0.0f);
        float f6 = dm2.d(this.a.h);
        f5 = (float)((double)f5 * (Math.sqrt(f6) / 2.0));
        GL11.glScalef((float)(-f5), (float)(-f5), (float)f5);
        String string = dm2.i;
        GL11.glDisable((int)2896);
        GL11.glDepthMask((boolean)false);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        ho ho2 = ho.a;
        GL11.glDisable((int)3553);
        ho2.b();
        int n2 = kd2.a(string) / 2;
        ho2.a(0.0f, 0.0f, 0.0f, 0.25f);
        ho2.a((double)(-n2 - 1), -1.0, 0.0);
        ho2.a((double)(-n2 - 1), 8.0, 0.0);
        ho2.a((double)(n2 + 1), 8.0, 0.0);
        ho2.a((double)(n2 + 1), -1.0, 0.0);
        ho2.a();
        GL11.glEnable((int)3553);
        kd2.b(string, -kd2.a(string) / 2, 0, 0x20FFFFFF);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        kd2.b(string, -kd2.a(string) / 2, 0, -1);
        GL11.glEnable((int)2896);
        GL11.glDisable((int)3042);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    protected void a(dm dm2, float f2) {
        ev ev2 = dm2.b.a();
        if (ev2 != null) {
            GL11.glPushMatrix();
            this.f.d.b(0.0625f);
            GL11.glTranslatef((float)-0.0625f, (float)0.4375f, (float)0.0625f);
            if (ev2.c < 256 && bc.a(ly.n[ev2.c].f())) {
                float f3 = 0.5f;
                GL11.glTranslatef((float)0.0f, (float)0.1875f, (float)-0.3125f);
                GL11.glRotatef((float)20.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glRotatef((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glScalef((float)(f3 *= 0.75f), (float)(-f3), (float)f3);
            } else if (di.c[ev2.c].a()) {
                float f4 = 0.625f;
                GL11.glTranslatef((float)0.0f, (float)0.1875f, (float)0.0f);
                GL11.glScalef((float)f4, (float)(-f4), (float)f4);
                GL11.glRotatef((float)-100.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glRotatef((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            } else {
                float f5 = 0.375f;
                GL11.glTranslatef((float)0.25f, (float)0.1875f, (float)-0.1875f);
                GL11.glScalef((float)f5, (float)f5, (float)f5);
                GL11.glRotatef((float)60.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glRotatef((float)20.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            this.a.f.a(ev2);
            GL11.glPopMatrix();
        }
    }

    protected void b(dm dm2, float f2) {
        float f3 = 0.9375f;
        GL11.glScalef((float)f3, (float)f3, (float)f3);
    }

    public void b() {
        this.f.k = 0.0f;
        this.f.a(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
        this.f.d.a(0.0625f);
    }
}

