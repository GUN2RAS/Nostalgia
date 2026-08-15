/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class cx
extends bh {
    private static final Random h = new Random();
    String[] a = new String[]{" *   * * *   * *** *** *** *** *** ***", " ** ** * **  * *   *   * * * * *    * ", " * * * * * * * **  *   **  *** **   * ", " *   * * *  ** *   *   * * * * *    * ", " *   * * *   * *** *** * * * * *    * "};
    private kc[][] i;
    private float j = 0.0f;
    private String l = "missingno";

    public cx() {
        try {
            ArrayList<String> arrayList = new ArrayList<String>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cx.class.getResourceAsStream("/title/splashes.txt")));
            String string = "";
            while ((string = bufferedReader.readLine()) != null) {
                if ((string = string.trim()).length() <= 0) continue;
                arrayList.add(string);
            }
            this.l = (String)arrayList.get(h.nextInt(arrayList.size()));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void g() {
        this.j += 1.0f;
        if (this.i != null) {
            for (int i2 = 0; i2 < this.i.length; ++i2) {
                for (int i3 = 0; i3 < this.i[i2].length; ++i3) {
                    this.i[i2][i3].a();
                }
            }
        }
    }

    protected void a(char c2, int n2) {
    }

    public void a() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.get(2) + 1 == 11 && calendar.get(5) == 9) {
            this.l = "Happy birthday, ez!";
        } else if (calendar.get(2) + 1 == 6 && calendar.get(5) == 1) {
            this.l = "Happy birthday, Notch!";
        } else if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
            this.l = "Merry X-mas!";
        } else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
            this.l = "Happy new year!";
        }
        this.e.clear();
        this.e.add(new fk(1, this.c / 2 - 100, this.d / 4 + 48, "Singleplayer"));
        this.e.add(new fk(2, this.c / 2 - 100, this.d / 4 + 72, "Multiplayer"));
        this.e.add(new fk(3, this.c / 2 - 100, this.d / 4 + 96, "Play tutorial level"));
        this.e.add(new fk(0, this.c / 2 - 100, this.d / 4 + 120 + 12, "Options..."));
        ((fk)this.e.get((int)2)).g = false;
        if (this.b.i == null) {
            ((fk)this.e.get((int)1)).g = false;
        }
    }

    protected void a(fk fk2) {
        if (fk2.f == 0) {
            this.b.a(new ay(this, this.b.y));
        }
        if (fk2.f == 1) {
            this.b.a(new jq(this));
        }
        if (fk2.f == 2) {
            this.b.a(new gc(this));
        }
    }

    public void a(int n2, int n3, float f2) {
        this.i();
        ho ho2 = ho.a;
        this.a(f2);
        GL11.glBindTexture((int)3553, (int)this.b.n.a("/gui/logo.png"));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        ho2.b(0xFFFFFF);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(this.c / 2 + 90), (float)70.0f, (float)0.0f);
        GL11.glRotatef((float)-20.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        float f3 = 1.8f - eo.e(eo.a((float)(System.currentTimeMillis() % 1000L) / 1000.0f * (float)Math.PI * 2.0f) * 0.1f);
        f3 = f3 * 100.0f / (float)(this.g.a(this.l) + 32);
        GL11.glScalef((float)f3, (float)f3, (float)f3);
        this.a(this.g, this.l, 0, -8, 0xFFFF00);
        GL11.glPopMatrix();
        String string = "Copyright Mojang Specifications. Do not distribute.";
        this.b(this.g, string, this.c - this.g.a(string) - 2, this.d - 10, 0xFFFFFF);
        long l2 = Runtime.getRuntime().maxMemory();
        long l3 = Runtime.getRuntime().totalMemory();
        long l4 = Runtime.getRuntime().freeMemory();
        long l5 = l2 - l4;
        string = "Free memory: " + l5 * 100L / l2 + "% of " + l2 / 1024L / 1024L + "MB";
        this.b(this.g, string, this.c - this.g.a(string) - 2, 2, 0x808080);
        string = "Allocated memory: " + l3 * 100L / l2 + "% (" + l3 / 1024L / 1024L + "MB)";
        this.b(this.g, string, this.c - this.g.a(string) - 2, 12, 0x808080);
        super.a(n2, n3, f2);
    }

    private void a(float f2) {
        int n2;
        if (this.i == null) {
            this.i = new kc[this.a[0].length()][this.a.length];
            for (int i2 = 0; i2 < this.i.length; ++i2) {
                for (n2 = 0; n2 < this.i[i2].length; ++n2) {
                    this.i[i2][n2] = new kc(this, i2, n2);
                }
            }
        }
        GL11.glMatrixMode((int)5889);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        iy iy2 = new iy(this.b.c, this.b.d);
        n2 = 120 * iy2.a;
        GLU.gluPerspective((float)70.0f, (float)((float)this.b.c / (float)n2), (float)0.05f, (float)100.0f);
        GL11.glViewport((int)0, (int)(this.b.d - n2), (int)this.b.c, (int)n2);
        GL11.glMatrixMode((int)5888);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glDisable((int)2884);
        GL11.glCullFace((int)1029);
        GL11.glDepthMask((boolean)true);
        for (int i3 = 0; i3 < 3; ++i3) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)0.4f, (float)0.6f, (float)-12.0f);
            if (i3 == 0) {
                GL11.glClear((int)256);
                GL11.glTranslatef((float)0.0f, (float)-0.4f, (float)0.0f);
                GL11.glScalef((float)0.98f, (float)1.0f, (float)1.0f);
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
            }
            if (i3 == 1) {
                GL11.glDisable((int)3042);
                GL11.glClear((int)256);
            }
            if (i3 == 2) {
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)768, (int)1);
            }
            GL11.glScalef((float)1.0f, (float)-1.0f, (float)1.0f);
            GL11.glRotatef((float)15.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glScalef((float)0.89f, (float)1.0f, (float)0.4f);
            GL11.glTranslatef((float)((float)(-this.a[0].length()) * 0.5f), (float)((float)(-this.a.length) * 0.5f), (float)0.0f);
            GL11.glBindTexture((int)3553, (int)this.b.n.a("/terrain.png"));
            if (i3 == 0) {
                GL11.glBindTexture((int)3553, (int)this.b.n.a("/title/black.png"));
            }
            bc bc2 = new bc();
            for (int i4 = 0; i4 < this.a.length; ++i4) {
                for (int i5 = 0; i5 < this.a[i4].length(); ++i5) {
                    char c2 = this.a[i4].charAt(i5);
                    if (c2 == ' ') continue;
                    GL11.glPushMatrix();
                    kc kc2 = this.i[i5][i4];
                    float f3 = (float)(kc2.b + (kc2.a - kc2.b) * (double)f2);
                    float f4 = 1.0f;
                    float f5 = 1.0f;
                    float f6 = 0.0f;
                    if (i3 == 0) {
                        f4 = f3 * 0.04f + 1.0f;
                        f5 = 1.0f / f4;
                        f3 = 0.0f;
                    }
                    GL11.glTranslatef((float)i5, (float)i4, (float)f3);
                    GL11.glScalef((float)f4, (float)f4, (float)f4);
                    GL11.glRotatef((float)f6, (float)0.0f, (float)1.0f, (float)0.0f);
                    bc2.a(ly.u, f5);
                    GL11.glPopMatrix();
                }
            }
            GL11.glPopMatrix();
        }
        GL11.glDisable((int)3042);
        GL11.glMatrixMode((int)5889);
        GL11.glPopMatrix();
        GL11.glMatrixMode((int)5888);
        GL11.glPopMatrix();
        GL11.glViewport((int)0, (int)0, (int)this.b.c, (int)this.b.d);
        GL11.glEnable((int)2884);
    }

    static /* synthetic */ Random j() {
        return h;
    }
}

