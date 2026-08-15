/*
 * Decompiled with CFR 0.152.
 */
import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class gs
extends cn {
    private LinkedList z = new LinkedList();
    private gy A;
    private lz B;
    private boolean C = false;
    private fi D = new fi();
    private Set E = new HashSet();
    private Set F = new HashSet();

    public gs(gy gy2) {
        super("MpServer");
        this.A = gy2;
        this.o = 8;
        this.p = 64;
        this.q = 8;
    }

    public void g() {
        Object object;
        int n2;
        ++this.c;
        int n3 = this.a(1.0f);
        if (n3 != this.e) {
            this.e = n3;
            for (n2 = 0; n2 < this.s.size(); ++n2) {
                ((im)this.s.get(n2)).e();
            }
        }
        for (n2 = 0; n2 < 10 && !this.F.isEmpty(); ++n2) {
            object = (kh)this.F.iterator().next();
            this.a((kh)object);
        }
        this.A.a();
        for (n2 = 0; n2 < this.z.size(); ++n2) {
            object = (lc)this.z.get(n2);
            if (--((lc)object).d != 0) continue;
            super.a(((lc)object).a, ((lc)object).b, ((lc)object).c, ((lc)object).e, ((lc)object).f);
            super.h(((lc)object).a, ((lc)object).b, ((lc)object).c);
            this.z.remove(n2--);
        }
    }

    public void c(int n2, int n3, int n4, int n5, int n6, int n7) {
        for (int i2 = 0; i2 < this.z.size(); ++i2) {
            lc lc2 = (lc)this.z.get(i2);
            if (lc2.a < n2 || lc2.b < n3 || lc2.c < n4 || lc2.a > n5 || lc2.b > n6 || lc2.c > n7) continue;
            this.z.remove(i2--);
        }
    }

    protected aw a(File file) {
        this.B = new lz(this);
        return this.B;
    }

    public void a() {
        this.o = 8;
        this.p = 64;
        this.q = 8;
    }

    protected void h() {
    }

    public void h(int n2, int n3, int n4, int n5) {
    }

    public boolean a(boolean bl2) {
        return false;
    }

    public void a(int n2, int n3, boolean bl2) {
        if (bl2) {
            this.B.d(n2, n3);
        } else {
            this.B.c(n2, n3);
        }
        if (!bl2) {
            this.b(n2 * 16, 0, n3 * 16, n2 * 16 + 15, 128, n3 * 16 + 15);
        }
    }

    public boolean a(kh kh2) {
        boolean bl2 = super.a(kh2);
        if (kh2 instanceof bi) {
            this.E.add(kh2);
        }
        return bl2;
    }

    public void d(kh kh2) {
        super.d(kh2);
        if (kh2 instanceof bi) {
            this.E.remove(kh2);
        }
    }

    protected void b(kh kh2) {
        super.b(kh2);
        if (this.F.contains(kh2)) {
            this.F.remove(kh2);
        }
    }

    protected void c(kh kh2) {
        super.c(kh2);
        if (this.E.contains(kh2)) {
            this.F.add(kh2);
        }
    }

    public void a(int n2, kh kh2) {
        this.E.add(kh2);
        if (!this.a(kh2)) {
            this.F.add(kh2);
        }
        this.D.a(n2, kh2);
    }

    public kh b(int n2) {
        return (kh)this.D.a(n2);
    }

    public kh c(int n2) {
        kh kh2 = (kh)this.D.b(n2);
        if (kh2 != null) {
            this.E.remove(kh2);
            this.d(kh2);
        }
        return kh2;
    }

    public boolean c(int n2, int n3, int n4, int n5) {
        int n6 = this.a(n2, n3, n4);
        int n7 = this.e(n2, n3, n4);
        if (super.c(n2, n3, n4, n5)) {
            this.z.add(new lc(this, n2, n3, n4, n6, n7));
            return true;
        }
        return false;
    }

    public boolean a(int n2, int n3, int n4, int n5, int n6) {
        int n7 = this.a(n2, n3, n4);
        int n8 = this.e(n2, n3, n4);
        if (super.a(n2, n3, n4, n5, n6)) {
            this.z.add(new lc(this, n2, n3, n4, n7, n8));
            return true;
        }
        return false;
    }

    public boolean a(int n2, int n3, int n4, int n5) {
        int n6 = this.a(n2, n3, n4);
        int n7 = this.e(n2, n3, n4);
        if (super.a(n2, n3, n4, n5)) {
            this.z.add(new lc(this, n2, n3, n4, n6, n7));
            return true;
        }
        return false;
    }

    public boolean c(int n2, int n3, int n4, int n5, int n6) {
        this.c(n2, n3, n4, n2, n3, n4);
        if (super.a(n2, n3, n4, n5, n6)) {
            this.e(n2, n3, n4, n5);
            return true;
        }
        return false;
    }

    public void b(int n2, int n3, int n4, ic ic2) {
        if (this.C) {
            return;
        }
        this.A.a((fn)new ny(n2, n3, n4, ic2));
    }

    public void k() {
        this.A.a((fn)new oh("Quitting"));
    }
}

