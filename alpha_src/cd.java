/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public class cd
extends nq {
    private kh a;
    private kh o;
    private int p = 0;
    private int q = 0;
    private float r;

    public cd(cn cn2, kh kh2, kh kh3, float f2) {
        super(cn2, kh2.ak, kh2.al, kh2.am, kh2.an, kh2.ao, kh2.ap);
        this.a = kh2;
        this.o = kh3;
        this.q = 3;
        this.r = f2;
    }

    public void a(ho ho2, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8 = ((float)this.p + f2) / (float)this.q;
        f8 *= f8;
        double d2 = this.a.ak;
        double d3 = this.a.al;
        double d4 = this.a.am;
        double d5 = this.o.aI + (this.o.ak - this.o.aI) * (double)f2;
        double d6 = this.o.aJ + (this.o.al - this.o.aJ) * (double)f2 + (double)this.r;
        double d7 = this.o.aK + (this.o.am - this.o.aK) * (double)f2;
        double d8 = d2 + (d5 - d2) * (double)f8;
        double d9 = d3 + (d6 - d3) * (double)f8;
        double d10 = d4 + (d7 - d4) * (double)f8;
        int n2 = eo.b(d8);
        int n3 = eo.b(d9 + (double)(this.aB / 2.0f));
        int n4 = eo.b(d10);
        float f9 = this.ag.c(n2, n3, n4);
        GL11.glColor4f((float)f9, (float)f9, (float)f9, (float)1.0f);
        kx.a.a(this.a, (float)(d8 -= l), (float)(d9 -= m), (float)(d10 -= n), this.a.aq, f2);
    }

    public void e_() {
        ++this.p;
        if (this.p == this.q) {
            this.F();
        }
    }

    public int c() {
        return 3;
    }
}

