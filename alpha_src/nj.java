/*
 * Decompiled with CFR 0.152.
 */
import net.minecraft.client.Minecraft;

public class nj
extends hq {
    private int c = -1;
    private int d = -1;
    private int e = -1;
    private float f = 0.0f;
    private float g = 0.0f;
    private float h = 0.0f;
    private int i = 0;
    private boolean j = false;
    private gy k;
    private int l = 0;

    public nj(Minecraft minecraft, gy gy2) {
        super(minecraft);
        this.k = gy2;
    }

    public void a(dm dm2) {
        dm2.aq = -180.0f;
    }

    public boolean b(int n2, int n3, int n4, int n5) {
        this.k.a((fn)new fg(3, n2, n3, n4, n5));
        int n6 = this.a.e.a(n2, n3, n4);
        int n7 = this.a.e.e(n2, n3, n4);
        boolean bl2 = super.b(n2, n3, n4, n5);
        ev ev2 = this.a.g.t();
        if (ev2 != null) {
            ev2.a(n6, n2, n3, n4);
            if (ev2.a == 0) {
                ev2.a(this.a.g);
                this.a.g.u();
            }
        }
        if (bl2 && this.a.g.b(ly.n[n6])) {
            ly.n[n6].b_(this.a.e, n2, n3, n4, n7);
        }
        return bl2;
    }

    public void a(int n2, int n3, int n4, int n5) {
        this.j = true;
        this.k.a((fn)new fg(0, n2, n3, n4, n5));
        int n6 = this.a.e.a(n2, n3, n4);
        if (n6 > 0 && this.f == 0.0f) {
            ly.n[n6].b(this.a.e, n2, n3, n4, this.a.g);
        }
        if (n6 > 0 && ly.n[n6].a(this.a.g) >= 1.0f) {
            this.b(n2, n3, n4, n5);
        }
    }

    public void a() {
        if (!this.j) {
            return;
        }
        this.j = false;
        this.k.a((fn)new fg(2, 0, 0, 0, 0));
        this.f = 0.0f;
        this.i = 0;
    }

    public void c(int n2, int n3, int n4, int n5) {
        this.j = true;
        this.e();
        this.k.a((fn)new fg(1, n2, n3, n4, n5));
        if (this.i > 0) {
            --this.i;
            return;
        }
        if (n2 == this.c && n3 == this.d && n4 == this.e) {
            int n6 = this.a.e.a(n2, n3, n4);
            if (n6 == 0) {
                return;
            }
            ly ly2 = ly.n[n6];
            this.f += ly2.a(this.a.g);
            if (this.h % 4.0f == 0.0f && ly2 != null) {
                this.a.A.b(ly2.bl.d(), (float)n2 + 0.5f, (float)n3 + 0.5f, (float)n4 + 0.5f, (ly2.bl.b() + 1.0f) / 8.0f, ly2.bl.c() * 0.5f);
            }
            this.h += 1.0f;
            if (this.f >= 1.0f) {
                this.b(n2, n3, n4, n5);
                this.f = 0.0f;
                this.g = 0.0f;
                this.h = 0.0f;
                this.i = 5;
            }
        } else {
            this.f = 0.0f;
            this.g = 0.0f;
            this.h = 0.0f;
            this.c = n2;
            this.d = n3;
            this.e = n4;
        }
    }

    public void a(float f2) {
        if (this.f <= 0.0f) {
            this.a.u.b = 0.0f;
            this.a.f.i = 0.0f;
        } else {
            float f3;
            this.a.u.b = f3 = this.g + (this.f - this.g) * f2;
            this.a.f.i = f3;
        }
    }

    public float b() {
        return 4.0f;
    }

    public void a(cn cn2) {
        super.a(cn2);
    }

    public void c() {
        this.e();
        this.g = this.f;
    }

    private void e() {
        ev ev2 = this.a.g.b.a();
        int n2 = 0;
        if (ev2 != null) {
            n2 = ev2.c;
        }
        if (n2 != this.l) {
            this.l = n2;
            this.k.a((fn)new dz(0, this.l));
        }
    }

    public boolean a(dm dm2, cn cn2, ev ev2, int n2, int n3, int n4, int n5) {
        this.e();
        this.k.a((fn)new do(ev2 != null ? ev2.c : -1, n2, n3, n4, n5));
        return super.a(dm2, cn2, ev2, n2, n3, n4, n5);
    }

    public dm b(cn cn2) {
        return new la(this.a, cn2, this.a.i, this.k);
    }
}

