/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class nv
extends bh {
    protected String a = "Edit sign message:";
    private ob h;
    private int i;
    private int j = 0;

    public nv(ob ob2) {
        this.h = ob2;
    }

    public void a() {
        this.e.clear();
        Keyboard.enableRepeatEvents((boolean)true);
        this.e.add(new fk(0, this.c / 2 - 100, this.d / 4 + 120, "Done"));
    }

    public void h() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    public void g() {
        ++this.i;
    }

    protected void a(fk fk2) {
        if (!fk2.g) {
            return;
        }
        if (fk2.f == 0) {
            this.h.j_();
            this.b.a((bh)null);
        }
    }

    protected void a(char c2, int n2) {
        if (n2 == 200) {
            this.j = this.j - 1 & 3;
        }
        if (n2 == 208 || n2 == 28) {
            this.j = this.j + 1 & 3;
        }
        if (n2 == 14 && this.h.a[this.j].length() > 0) {
            this.h.a[this.j] = this.h.a[this.j].substring(0, this.h.a[this.j].length() - 1);
        }
        if (" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(c2) >= 0 && this.h.a[this.j].length() < 15) {
            int n3 = this.j;
            this.h.a[n3] = this.h.a[n3] + c2;
        }
    }

    public void a(int n2, int n3, float f2) {
        this.i();
        this.a(this.g, this.a, this.c / 2, 40, 0xFFFFFF);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(this.c / 2), (float)(this.d / 2), (float)50.0f);
        float f3 = 93.75f;
        GL11.glScalef((float)(-f3), (float)(-f3), (float)(-f3));
        GL11.glRotatef((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        ly ly2 = this.h.g();
        if (ly2 == ly.aE) {
            float f4 = (float)(this.h.f() * 360) / 16.0f;
            GL11.glRotatef((float)f4, (float)0.0f, (float)1.0f, (float)0.0f);
        } else {
            int n4 = this.h.f();
            float f5 = 0.0f;
            if (n4 == 2) {
                f5 = 180.0f;
            }
            if (n4 == 4) {
                f5 = 90.0f;
            }
            if (n4 == 5) {
                f5 = -90.0f;
            }
            GL11.glRotatef((float)f5, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glTranslatef((float)0.0f, (float)0.3125f, (float)0.0f);
        }
        if (this.i / 6 % 2 == 0) {
            this.h.b = this.j;
        }
        fz.a.a(this.h, -0.5, -0.75, -0.5, 0.0f);
        this.h.b = -1;
        GL11.glPopMatrix();
        super.a(n2, n3, f2);
    }
}

