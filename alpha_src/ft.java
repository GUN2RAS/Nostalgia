/*
 * Decompiled with CFR 0.152.
 */
import java.io.IOException;

public class ft
implements aw {
    private ga c;
    private aw d;
    private af e;
    private ga[] f = new ga[1024];
    private cn g;
    int a = -999999999;
    int b = -999999999;
    private ga h;

    public ft(cn cn2, af af2, aw aw2) {
        this.c = new ga(cn2, new byte[32768], 0, 0);
        this.c.q = true;
        this.c.p = true;
        this.g = cn2;
        this.e = af2;
        this.d = aw2;
    }

    public boolean a(int n2, int n3) {
        if (n2 == this.a && n3 == this.b && this.h != null) {
            return true;
        }
        int n4 = n2 & 0x1F;
        int n5 = n3 & 0x1F;
        int n6 = n4 + n5 * 32;
        return this.f[n6] != null && (this.f[n6] == this.c || this.f[n6].a(n2, n3));
    }

    public ga b(int n2, int n3) {
        if (n2 == this.a && n3 == this.b && this.h != null) {
            return this.h;
        }
        int n4 = n2 & 0x1F;
        int n5 = n3 & 0x1F;
        int n6 = n4 + n5 * 32;
        if (!this.a(n2, n3)) {
            ga ga2;
            if (this.f[n6] != null) {
                this.f[n6].e();
                this.b(this.f[n6]);
                this.a(this.f[n6]);
            }
            if ((ga2 = this.c(n2, n3)) == null) {
                ga2 = this.d == null ? this.c : this.d.b(n2, n3);
            }
            this.f[n6] = ga2;
            if (this.f[n6] != null) {
                this.f[n6].d();
            }
            if (!this.f[n6].n && this.a(n2 + 1, n3 + 1) && this.a(n2, n3 + 1) && this.a(n2 + 1, n3)) {
                this.a(this, n2, n3);
            }
            if (this.a(n2 - 1, n3) && !this.b((int)(n2 - 1), (int)n3).n && this.a(n2 - 1, n3 + 1) && this.a(n2, n3 + 1) && this.a(n2 - 1, n3)) {
                this.a(this, n2 - 1, n3);
            }
            if (this.a(n2, n3 - 1) && !this.b((int)n2, (int)(n3 - 1)).n && this.a(n2 + 1, n3 - 1) && this.a(n2, n3 - 1) && this.a(n2 + 1, n3)) {
                this.a(this, n2, n3 - 1);
            }
            if (this.a(n2 - 1, n3 - 1) && !this.b((int)(n2 - 1), (int)(n3 - 1)).n && this.a(n2 - 1, n3 - 1) && this.a(n2, n3 - 1) && this.a(n2 - 1, n3)) {
                this.a(this, n2 - 1, n3 - 1);
            }
        }
        this.a = n2;
        this.b = n3;
        this.h = this.f[n6];
        return this.f[n6];
    }

    private ga c(int n2, int n3) {
        if (this.e == null) {
            return null;
        }
        try {
            ga ga2 = this.e.a(this.g, n2, n3);
            if (ga2 != null) {
                ga2.s = this.g.c;
            }
            return ga2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private void a(ga ga2) {
        if (this.e == null) {
            return;
        }
        try {
            this.e.b(this.g, ga2);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void b(ga ga2) {
        if (this.e == null) {
            return;
        }
        try {
            ga2.s = this.g.c;
            this.e.a(this.g, ga2);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void a(aw aw2, int n2, int n3) {
        ga ga2 = this.b(n2, n3);
        if (!ga2.n) {
            ga2.n = true;
            if (this.d != null) {
                this.d.a(aw2, n2, n3);
                ga2.f();
            }
        }
    }

    public boolean a(boolean bl2, nu nu2) {
        int n2;
        int n3 = 0;
        int n4 = 0;
        if (nu2 != null) {
            for (n2 = 0; n2 < this.f.length; ++n2) {
                if (this.f[n2] == null || !this.f[n2].a(bl2)) continue;
                ++n4;
            }
        }
        n2 = 0;
        for (int i2 = 0; i2 < this.f.length; ++i2) {
            if (this.f[i2] == null) continue;
            if (bl2 && !this.f[i2].p) {
                this.a(this.f[i2]);
            }
            if (!this.f[i2].a(bl2)) continue;
            this.b(this.f[i2]);
            this.f[i2].o = false;
            if (++n3 == 2 && !bl2) {
                return false;
            }
            if (nu2 == null || ++n2 % 10 != 0) continue;
            nu2.a(n2 * 100 / n4);
        }
        if (bl2) {
            if (this.e == null) {
                return true;
            }
            this.e.b();
        }
        return true;
    }

    public boolean a() {
        if (this.e != null) {
            this.e.a();
        }
        return this.d.a();
    }

    public boolean b() {
        return true;
    }
}

