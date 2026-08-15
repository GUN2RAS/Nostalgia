/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;

public abstract class ee
extends bh {
    private static ab j = new ab();
    protected int a = 176;
    protected int h = 166;
    protected List i = new ArrayList();

    public void a(int n2, int n3, float f2) {
        this.i();
        int n4 = (this.c - this.a) / 2;
        int n5 = (this.d - this.h) / 2;
        this.a(f2);
        GL11.glPushMatrix();
        GL11.glRotatef((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        i.b();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)n4, (float)n5, (float)0.0f);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)32826);
        for (int i2 = 0; i2 < this.i.size(); ++i2) {
            mm mm2 = (mm)this.i.get(i2);
            this.a(mm2);
            if (!mm2.a(n2, n3)) continue;
            GL11.glDisable((int)2896);
            GL11.glDisable((int)2929);
            int n6 = mm2.e;
            int n7 = mm2.f;
            this.a(n6, n7, n6 + 16, n7 + 16, -2130706433, -2130706433);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)2929);
        }
        eu eu2 = this.b.g.b;
        if (eu2.e != null) {
            GL11.glTranslatef((float)0.0f, (float)0.0f, (float)32.0f);
            j.a(this.g, this.b.n, eu2.e, n2 - n4 - 8, n3 - n5 - 8);
            j.b(this.g, this.b.n, eu2.e, n2 - n4 - 8, n3 - n5 - 8);
        }
        GL11.glDisable((int)32826);
        i.a();
        GL11.glDisable((int)2896);
        GL11.glDisable((int)2929);
        this.j();
        GL11.glEnable((int)2896);
        GL11.glEnable((int)2929);
        GL11.glPopMatrix();
    }

    protected void j() {
    }

    protected abstract void a(float var1);

    private void a(mm mm2) {
        int n2;
        gh gh2 = mm2.b;
        int n3 = mm2.a;
        int n4 = mm2.e;
        int n5 = mm2.f;
        ev ev2 = gh2.c(n3);
        if (ev2 == null && (n2 = mm2.c()) >= 0) {
            GL11.glDisable((int)2896);
            this.b.n.b(this.b.n.a("/gui/items.png"));
            this.b(n4, n5, n2 % 16 * 16, n2 / 16 * 16, 16, 16);
            GL11.glEnable((int)2896);
            return;
        }
        j.a(this.g, this.b.n, ev2, n4, n5);
        j.b(this.g, this.b.n, ev2, n4, n5);
    }

    private dk a(int n2, int n3) {
        for (int i2 = 0; i2 < this.i.size(); ++i2) {
            mm mm2 = (mm)this.i.get(i2);
            if (!mm2.a(n2, n3)) continue;
            return mm2;
        }
        return null;
    }

    protected void a(int n2, int n3, int n4) {
        if (n4 == 0 || n4 == 1) {
            dk dk2 = this.a(n2, n3);
            eu eu2 = this.b.g.b;
            if (dk2 != null) {
                ev ev2 = dk2.b();
                if (ev2 != null || eu2.e != null) {
                    if (ev2 != null && eu2.e == null) {
                        int n5 = n4 == 0 ? ev2.a : (ev2.a + 1) / 2;
                        eu2.e = dk2.b.a(dk2.a, n5);
                        if (ev2.a == 0) {
                            dk2.b(null);
                        }
                        dk2.a();
                    } else if (ev2 == null && eu2.e != null && dk2.a(eu2.e)) {
                        int n6;
                        int n7 = n6 = n4 == 0 ? eu2.e.a : 1;
                        if (n6 > dk2.b.e()) {
                            n6 = dk2.b.e();
                        }
                        dk2.b(eu2.e.a(n6));
                        if (eu2.e.a == 0) {
                            eu2.e = null;
                        }
                    } else if (ev2 != null && eu2.e != null) {
                        int n8;
                        if (dk2.a(eu2.e)) {
                            if (ev2.c != eu2.e.c) {
                                if (eu2.e.a <= dk2.b.e()) {
                                    ev ev3 = ev2;
                                    dk2.b(eu2.e);
                                    eu2.e = ev3;
                                }
                            } else if (ev2.c == eu2.e.c) {
                                if (n4 == 0) {
                                    int n9 = eu2.e.a;
                                    if (n9 > dk2.b.e() - ev2.a) {
                                        n9 = dk2.b.e() - ev2.a;
                                    }
                                    if (n9 > eu2.e.c() - ev2.a) {
                                        n9 = eu2.e.c() - ev2.a;
                                    }
                                    eu2.e.a(n9);
                                    if (eu2.e.a == 0) {
                                        eu2.e = null;
                                    }
                                    ev2.a += n9;
                                } else if (n4 == 1) {
                                    int n10 = 1;
                                    if (n10 > dk2.b.e() - ev2.a) {
                                        n10 = dk2.b.e() - ev2.a;
                                    }
                                    if (n10 > eu2.e.c() - ev2.a) {
                                        n10 = eu2.e.c() - ev2.a;
                                    }
                                    eu2.e.a(n10);
                                    if (eu2.e.a == 0) {
                                        eu2.e = null;
                                    }
                                    ev2.a += n10;
                                }
                            }
                        } else if (ev2.c == eu2.e.c && eu2.e.c() > 1 && (n8 = ev2.a) > 0 && n8 + eu2.e.a <= eu2.e.c()) {
                            eu2.e.a += n8;
                            ev2.a(n8);
                            if (ev2.a == 0) {
                                dk2.b(null);
                            }
                            dk2.a();
                        }
                    }
                }
                dk2.d();
            } else if (eu2.e != null) {
                int n11 = (this.c - this.a) / 2;
                int n12 = (this.d - this.h) / 2;
                if (n2 < n11 || n3 < n12 || n2 >= n11 + this.a || n3 >= n12 + this.a) {
                    bi bi2 = this.b.g;
                    if (n4 == 0) {
                        bi2.a(eu2.e);
                        eu2.e = null;
                    }
                    if (n4 == 1) {
                        bi2.a(eu2.e.a(1));
                        if (eu2.e.a == 0) {
                            eu2.e = null;
                        }
                    }
                }
            }
        }
    }

    protected void b(int n2, int n3, int n4) {
        if (n4 == 0) {
            // empty if block
        }
    }

    protected void a(char c2, int n2) {
        if (n2 == 1 || n2 == this.b.y.o.b) {
            this.b.a((bh)null);
        }
    }

    public void h() {
        eu eu2 = this.b.g.b;
        if (eu2.e != null) {
            this.b.g.a(eu2.e);
            eu2.e = null;
        }
    }

    public boolean b() {
        return false;
    }
}

