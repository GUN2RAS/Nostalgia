/*
 * Decompiled with CFR 0.152.
 */
import net.minecraft.client.Minecraft;

public class la
extends bi {
    private gy bg;
    private int bh = 0;
    private double bi;
    private double bj;
    private double bk;
    private double bl;
    private float bm;
    private float bn;
    private eu bo = new eu(null);

    public la(Minecraft minecraft, cn cn2, dl dl2, gy gy2) {
        super(minecraft, cn2, dl2);
        this.bg = gy2;
    }

    public void e_() {
        super.e_();
        this.J();
    }

    public void n() {
        this.J();
    }

    public void J() {
        boolean bl2;
        if (this.bh++ == 20) {
            if (!this.b.a(this.bo)) {
                this.bg.a((fn)new m(-1, this.b.a));
                this.bg.a((fn)new m(-2, this.b.c));
                this.bg.a((fn)new m(-3, this.b.b));
                this.bo = this.b.i();
            }
            this.bh = 0;
        }
        double d2 = this.ak - this.bi;
        double d3 = this.au.b - this.bj;
        double d4 = this.al - this.bk;
        double d5 = this.am - this.bl;
        double d6 = this.aq - this.bm;
        double d7 = this.ar - this.bn;
        boolean bl3 = d3 != 0.0 || d4 != 0.0 || d2 != 0.0 || d5 != 0.0;
        boolean bl4 = bl2 = d6 != 0.0 || d7 != 0.0;
        if (bl3 && bl2) {
            this.bg.a((fn)new ch(this.ak, this.au.b, this.al, this.am, this.aq, this.ar, this.av));
        } else if (bl3) {
            this.bg.a((fn)new s(this.ak, this.au.b, this.al, this.am, this.av));
        } else if (bl2) {
            this.bg.a((fn)new mh(this.aq, this.ar, this.av));
        } else {
            this.bg.a((fn)new eh(this.av));
        }
        if (bl3) {
            this.bi = this.ak;
            this.bj = this.au.b;
            this.bk = this.al;
            this.bl = this.am;
        }
        if (bl2) {
            this.bm = this.aq;
            this.bn = this.ar;
        }
    }

    protected void a(dx dx2) {
        System.out.println("Dropping?");
        ha ha2 = new ha(dx2);
        this.bg.a((fn)ha2);
        dx2.ak = (double)ha2.b / 32.0;
        dx2.al = (double)ha2.c / 32.0;
        dx2.am = (double)ha2.d / 32.0;
        dx2.an = (double)ha2.e / 128.0;
        dx2.ao = (double)ha2.f / 128.0;
        dx2.ap = (double)ha2.g / 128.0;
    }

    public void a(String string) {
        this.bg.a((fn)new ij(string));
    }

    public void w() {
        super.w();
        this.bg.a((fn)new hf(this, 1));
    }
}

