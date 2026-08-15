/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.lwjgl.opengl.GL11;

public class bq {
    protected cn a;
    private List[] b = new List[4];
    private ey c;
    private Random d = new Random();

    public bq(cn cn2, ey ey2) {
        if (cn2 != null) {
            this.a = cn2;
        }
        this.c = ey2;
        for (int i2 = 0; i2 < 4; ++i2) {
            this.b[i2] = new ArrayList();
        }
    }

    public void a(nq nq2) {
        int n2 = nq2.c();
        this.b[n2].add(nq2);
    }

    public void a() {
        for (int i2 = 0; i2 < 4; ++i2) {
            for (int i3 = 0; i3 < this.b[i2].size(); ++i3) {
                nq nq2 = (nq)this.b[i2].get(i3);
                nq2.e_();
                if (!nq2.aA) continue;
                this.b[i2].remove(i3--);
            }
        }
    }

    public void a(kh kh2, float f2) {
        float f3 = eo.b(kh2.aq * (float)Math.PI / 180.0f);
        float f4 = eo.a(kh2.aq * (float)Math.PI / 180.0f);
        float f5 = -f4 * eo.a(kh2.ar * (float)Math.PI / 180.0f);
        float f6 = f3 * eo.a(kh2.ar * (float)Math.PI / 180.0f);
        float f7 = eo.b(kh2.ar * (float)Math.PI / 180.0f);
        nq.l = kh2.aI + (kh2.ak - kh2.aI) * (double)f2;
        nq.m = kh2.aJ + (kh2.al - kh2.aJ) * (double)f2;
        nq.n = kh2.aK + (kh2.am - kh2.aK) * (double)f2;
        for (int i2 = 0; i2 < 3; ++i2) {
            if (this.b[i2].size() == 0) continue;
            int n2 = 0;
            if (i2 == 0) {
                n2 = this.c.a("/particles.png");
            }
            if (i2 == 1) {
                n2 = this.c.a("/terrain.png");
            }
            if (i2 == 2) {
                n2 = this.c.a("/gui/items.png");
            }
            GL11.glBindTexture((int)3553, (int)n2);
            ho ho2 = ho.a;
            ho2.b();
            for (int i3 = 0; i3 < this.b[i2].size(); ++i3) {
                nq nq2 = (nq)this.b[i2].get(i3);
                nq2.a(ho2, f2, f3, f7, f4, f5, f6);
            }
            ho2.a();
        }
    }

    public void b(kh kh2, float f2) {
        int n2 = 3;
        if (this.b[n2].size() == 0) {
            return;
        }
        ho ho2 = ho.a;
        for (int i2 = 0; i2 < this.b[n2].size(); ++i2) {
            nq nq2 = (nq)this.b[n2].get(i2);
            nq2.a(ho2, f2, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        }
    }

    public void a(cn cn2) {
        this.a = cn2;
        for (int i2 = 0; i2 < 4; ++i2) {
            this.b[i2].clear();
        }
    }

    public void a(int n2, int n3, int n4) {
        int n5 = this.a.a(n2, n3, n4);
        if (n5 == 0) {
            return;
        }
        ly ly2 = ly.n[n5];
        int n6 = 4;
        for (int i2 = 0; i2 < n6; ++i2) {
            for (int i3 = 0; i3 < n6; ++i3) {
                for (int i4 = 0; i4 < n6; ++i4) {
                    double d2 = (double)n2 + ((double)i2 + 0.5) / (double)n6;
                    double d3 = (double)n3 + ((double)i3 + 0.5) / (double)n6;
                    double d4 = (double)n4 + ((double)i4 + 0.5) / (double)n6;
                    this.a(new iw(this.a, d2, d3, d4, d2 - (double)n2 - 0.5, d3 - (double)n3 - 0.5, d4 - (double)n4 - 0.5, ly2));
                }
            }
        }
    }

    public void a(int n2, int n3, int n4, int n5) {
        int n6 = this.a.a(n2, n3, n4);
        if (n6 == 0) {
            return;
        }
        ly ly2 = ly.n[n6];
        float f2 = 0.1f;
        double d2 = (double)n2 + this.d.nextDouble() * (ly2.bi - ly2.bf - (double)(f2 * 2.0f)) + (double)f2 + ly2.bf;
        double d3 = (double)n3 + this.d.nextDouble() * (ly2.bj - ly2.bg - (double)(f2 * 2.0f)) + (double)f2 + ly2.bg;
        double d4 = (double)n4 + this.d.nextDouble() * (ly2.bk - ly2.bh - (double)(f2 * 2.0f)) + (double)f2 + ly2.bh;
        if (n5 == 0) {
            d3 = (double)n3 + ly2.bg - (double)f2;
        }
        if (n5 == 1) {
            d3 = (double)n3 + ly2.bj + (double)f2;
        }
        if (n5 == 2) {
            d4 = (double)n4 + ly2.bh - (double)f2;
        }
        if (n5 == 3) {
            d4 = (double)n4 + ly2.bk + (double)f2;
        }
        if (n5 == 4) {
            d2 = (double)n2 + ly2.bf - (double)f2;
        }
        if (n5 == 5) {
            d2 = (double)n2 + ly2.bi + (double)f2;
        }
        this.a(new iw(this.a, d2, d3, d4, 0.0, 0.0, 0.0, ly2).b(0.2f).d(0.6f));
    }

    public String b() {
        return "" + (this.b[0].size() + this.b[1].size() + this.b[2].size());
    }
}

