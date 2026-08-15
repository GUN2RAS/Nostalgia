/*
 * Decompiled with CFR 0.152.
 */
import java.util.ArrayList;
import java.util.Random;

public class ly {
    public static final bb e = new bb("stone", 1.0f, 1.0f);
    public static final bb f = new bb("wood", 1.0f, 1.0f);
    public static final bb g = new bb("gravel", 1.0f, 1.0f);
    public static final bb h = new bb("grass", 1.0f, 1.0f);
    public static final bb i = new bb("stone", 1.0f, 1.0f);
    public static final bb j = new bb("stone", 1.0f, 1.5f);
    public static final bb k = new u("stone", 1.0f, 1.0f);
    public static final bb l = new bb("cloth", 1.0f, 1.0f);
    public static final bb m = new t("sand", 1.0f, 1.0f);
    public static final ly[] n = new ly[256];
    public static final boolean[] o = new boolean[256];
    public static final boolean[] p = new boolean[256];
    public static final boolean[] q = new boolean[256];
    public static final int[] r = new int[256];
    public static final boolean[] s = new boolean[256];
    public static final int[] t = new int[256];
    public static final ly u = new ce(1, 1).c(1.5f).b(10.0f).a(i);
    public static final my v = (my)new my(2).c(0.6f).a(h);
    public static final ly w = new hz(3, 2).c(0.5f).a(g);
    public static final ly x = new ly(4, 16, gb.d).c(2.0f).b(10.0f).a(i);
    public static final ly y = new ly(5, 4, gb.c).c(2.0f).b(5.0f).a(f);
    public static final ly z = new dt(6, 15).c(0.0f).a(h);
    public static final ly A = new ly(7, 17, gb.d).c(-1.0f).b(6000000.0f).a(i);
    public static final ly B = new hv(8, gb.f).c(100.0f).d(3);
    public static final ly C = new hn(9, gb.f).c(100.0f).d(3);
    public static final ly D = new hv(10, gb.g).c(0.0f).a(1.0f).d(255);
    public static final ly E = new hn(11, gb.g).c(100.0f).a(1.0f).d(255);
    public static final ly F = new dh(12, 18).c(0.5f).a(m);
    public static final ly G = new gz(13, 19).c(0.6f).a(g);
    public static final ly H = new gw(14, 32).c(3.0f).b(5.0f).a(i);
    public static final ly I = new gw(15, 33).c(3.0f).b(5.0f).a(i);
    public static final ly J = new gw(16, 34).c(3.0f).b(5.0f).a(i);
    public static final ly K = new mg(17).c(2.0f).a(f);
    public static final iz L = (iz)new iz(18, 52).c(0.2f).d(1).a(h);
    public static final ly M = new ng(19).c(0.6f).a(h);
    public static final ly N = new ct(20, 49, gb.o, false).c(0.3f).a(k);
    public static final ly O = null;
    public static final ly P = null;
    public static final ly Q = null;
    public static final ly R = null;
    public static final ly S = null;
    public static final ly T = null;
    public static final ly U = null;
    public static final ly V = null;
    public static final ly W = null;
    public static final ly X = null;
    public static final ly Y = null;
    public static final ly Z = null;
    public static final ly aa = null;
    public static final ly ab = null;
    public static final ly ac = new ly(35, 64, gb.k).c(0.8f).a(l);
    public static final ly ad = null;
    public static final mq ae = (mq)new mq(37, 13).c(0.0f).a(h);
    public static final mq af = (mq)new mq(38, 12).c(0.0f).a(h);
    public static final mq ag = (mq)new ky(39, 29).c(0.0f).a(h).a(0.125f);
    public static final mq ah = (mq)new ky(40, 28).c(0.0f).a(h);
    public static final ly ai = new c(41, 39).c(3.0f).b(10.0f).a(j);
    public static final ly aj = new c(42, 38).c(5.0f).b(10.0f).a(j);
    public static final ly ak = new oi(43, true).c(2.0f).b(10.0f).a(i);
    public static final ly al = new oi(44, false).c(2.0f).b(10.0f).a(i);
    public static final ly am = new ly(45, 7, gb.d).c(2.0f).b(10.0f).a(i);
    public static final ly an = new q(46, 8).c(0.0f).a(h);
    public static final ly ao = new ds(47, 35).c(1.5f).a(f);
    public static final ly ap = new ly(48, 36, gb.d).c(2.0f).b(10.0f).a(i);
    public static final ly aq = new cm(49, 37).c(10.0f).b(2000.0f).a(i);
    public static final ly ar = new mj(50, 80).c(0.0f).a(0.9375f).a(f);
    public static final og as = (og)new og(51, 31).c(0.0f).a(1.0f).a(f);
    public static final ly at = new bj(52, 65).c(5.0f).a(j);
    public static final ly au = new km(53, y);
    public static final ly av = new b(54).c(2.5f).a(f);
    public static final ly aw = new kf(55, 84).c(0.0f).a(e);
    public static final ly ax = new gw(56, 50).c(3.0f).b(5.0f).a(i);
    public static final ly ay = new c(57, 40).c(5.0f).b(10.0f).a(j);
    public static final ly az = new cs(58).c(2.5f).a(f);
    public static final ly aA = new hd(59, 88).c(0.0f).a(h);
    public static final ly aB = new mi(60).c(0.6f).a(g);
    public static final ly aC = new ku(61, false).c(3.5f).a(i);
    public static final ly aD = new ku(62, true).c(3.5f).a(i).a(0.875f);
    public static final ly aE = new lr(63, ob.class, true).c(1.0f).a(f);
    public static final ly aF = new fw(64, gb.c).c(3.0f).a(f);
    public static final ly aG = new br(65, 83).c(0.4f).a(f);
    public static final ly aH = new if(66, 128).c(0.7f).a(j);
    public static final ly aI = new km(67, x);
    public static final ly aJ = new lr(68, ob.class, false).c(1.0f).a(f);
    public static final ly aK = new no(69, 96).c(0.5f).a(f);
    public static final ly aL = new al(70, ly.u.bb, js.b).c(0.5f).a(i);
    public static final ly aM = new fw(71, gb.e).c(5.0f).a(j);
    public static final ly aN = new al(72, ly.y.bb, js.a).c(0.5f).a(f);
    public static final ly aO = new ai(73, 51, false).c(3.0f).b(5.0f).a(i);
    public static final ly aP = new ai(74, 51, true).a(0.625f).c(3.0f).b(5.0f).a(i);
    public static final ly aQ = new bg(75, 115, false).c(0.0f).a(f);
    public static final ly aR = new bg(76, 99, true).c(0.0f).a(0.5f).a(f);
    public static final ly aS = new hu(77, ly.u.bb).c(0.5f).a(i);
    public static final ly aT = new fd(78, 66).c(0.1f).a(l);
    public static final ly aU = new he(79, 67).c(0.5f).d(3).a(k);
    public static final ly aV = new p(80, 66).c(0.2f).a(l);
    public static final ly aW = new hy(81, 70).c(0.4f).a(l);
    public static final ly aX = new jv(82, 72).c(0.6f).a(g);
    public static final ly aY = new jm(83, 73).c(0.0f).a(h);
    public static final ly aZ = new cv(84, 74).c(2.0f).b(10.0f).a(i);
    public static final ly ba = new fh(85, 4).c(2.0f).b(5.0f).a(f);
    public int bb;
    public final int bc;
    protected float bd;
    protected float be;
    public double bf;
    public double bg;
    public double bh;
    public double bi;
    public double bj;
    public double bk;
    public bb bl = e;
    public float bm = 1.0f;
    public final gb bn;
    public float bo = 0.6f;

    protected ly(int n2, gb gb2) {
        if (n[n2] != null) {
            throw new IllegalArgumentException("Slot " + n2 + " is already occupied by " + n[n2] + " when adding " + this);
        }
        this.bn = gb2;
        ly.n[n2] = this;
        this.bc = n2;
        this.a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        ly.p[n2] = this.b();
        ly.r[n2] = this.b() ? 255 : 0;
        ly.s[n2] = this.i();
        ly.q[n2] = false;
    }

    protected ly(int n2, int n3, gb gb2) {
        this(n2, gb2);
        this.bb = n3;
    }

    protected ly a(bb bb2) {
        this.bl = bb2;
        return this;
    }

    protected ly d(int n2) {
        ly.r[this.bc] = n2;
        return this;
    }

    protected ly a(float f2) {
        ly.t[this.bc] = (int)(15.0f * f2);
        return this;
    }

    protected ly b(float f2) {
        this.be = f2 * 3.0f;
        return this;
    }

    private boolean i() {
        return false;
    }

    public boolean c() {
        return true;
    }

    public int f() {
        return 0;
    }

    protected ly c(float f2) {
        this.bd = f2;
        if (this.be < f2 * 5.0f) {
            this.be = f2 * 5.0f;
        }
        return this;
    }

    protected void b(boolean bl2) {
        ly.o[this.bc] = bl2;
    }

    public void a(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.bf = f2;
        this.bg = f3;
        this.bh = f4;
        this.bi = f5;
        this.bj = f6;
        this.bk = f7;
    }

    public float c(nm nm2, int n2, int n3, int n4) {
        return nm2.c(n2, n3, n4);
    }

    public boolean c(nm nm2, int n2, int n3, int n4, int n5) {
        if (n5 == 0 && this.bg > 0.0) {
            return true;
        }
        if (n5 == 1 && this.bj < 1.0) {
            return true;
        }
        if (n5 == 2 && this.bh > 0.0) {
            return true;
        }
        if (n5 == 3 && this.bk < 1.0) {
            return true;
        }
        if (n5 == 4 && this.bf > 0.0) {
            return true;
        }
        if (n5 == 5 && this.bi < 1.0) {
            return true;
        }
        return !nm2.g(n2, n3, n4);
    }

    public int a(nm nm2, int n2, int n3, int n4, int n5) {
        return this.a(n5, nm2.e(n2, n3, n4));
    }

    public int a(int n2, int n3) {
        return this.a(n2);
    }

    public int a(int n2) {
        return this.bb;
    }

    public cf f(cn cn2, int n2, int n3, int n4) {
        return cf.b((double)n2 + this.bf, (double)n3 + this.bg, (double)n4 + this.bh, (double)n2 + this.bi, (double)n3 + this.bj, (double)n4 + this.bk);
    }

    public void a(cn cn2, int n2, int n3, int n4, cf cf2, ArrayList arrayList) {
        cf cf3 = this.d(cn2, n2, n3, n4);
        if (cf3 != null && cf2.a(cf3)) {
            arrayList.add(cf3);
        }
    }

    public cf d(cn cn2, int n2, int n3, int n4) {
        return cf.b((double)n2 + this.bf, (double)n3 + this.bg, (double)n4 + this.bh, (double)n2 + this.bi, (double)n3 + this.bj, (double)n4 + this.bk);
    }

    public boolean b() {
        return true;
    }

    public boolean a(int n2, boolean bl2) {
        return this.h();
    }

    public boolean h() {
        return true;
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
    }

    public void b(cn cn2, int n2, int n3, int n4, Random random) {
    }

    public void b(cn cn2, int n2, int n3, int n4, int n5) {
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
    }

    public int a() {
        return 10;
    }

    public void e(cn cn2, int n2, int n3, int n4) {
    }

    public void b(cn cn2, int n2, int n3, int n4) {
    }

    public int a(Random random) {
        return 1;
    }

    public int a(int n2, Random random) {
        return this.bc;
    }

    public float a(dm dm2) {
        if (this.bd < 0.0f) {
            return 0.0f;
        }
        if (!dm2.b(this)) {
            return 1.0f / this.bd / 100.0f;
        }
        return dm2.a(this) / this.bd / 30.0f;
    }

    public void b_(cn cn2, int n2, int n3, int n4, int n5) {
        this.a(cn2, n2, n3, n4, n5, 1.0f);
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5, float f2) {
        if (cn2.y) {
            return;
        }
        int n6 = this.a(cn2.n);
        for (int i2 = 0; i2 < n6; ++i2) {
            int n7;
            if (cn2.n.nextFloat() > f2 || (n7 = this.a(n5, cn2.n)) <= 0) continue;
            float f3 = 0.7f;
            double d2 = (double)(cn2.n.nextFloat() * f3) + (double)(1.0f - f3) * 0.5;
            double d3 = (double)(cn2.n.nextFloat() * f3) + (double)(1.0f - f3) * 0.5;
            double d4 = (double)(cn2.n.nextFloat() * f3) + (double)(1.0f - f3) * 0.5;
            dx dx2 = new dx(cn2, (double)n2 + d2, (double)n3 + d3, (double)n4 + d4, new ev(n7));
            dx2.c = 10;
            cn2.a(dx2);
        }
    }

    public float a(kh kh2) {
        return this.be / 5.0f;
    }

    public mf a(cn cn2, int n2, int n3, int n4, aj aj2, aj aj3) {
        this.a((nm)cn2, n2, n3, n4);
        aj2 = aj2.c(-n2, -n3, -n4);
        aj3 = aj3.c(-n2, -n3, -n4);
        aj aj4 = aj2.a(aj3, this.bf);
        aj aj5 = aj2.a(aj3, this.bi);
        aj aj6 = aj2.b(aj3, this.bg);
        aj aj7 = aj2.b(aj3, this.bj);
        aj aj8 = aj2.c(aj3, this.bh);
        aj aj9 = aj2.c(aj3, this.bk);
        if (!this.a(aj4)) {
            aj4 = null;
        }
        if (!this.a(aj5)) {
            aj5 = null;
        }
        if (!this.b(aj6)) {
            aj6 = null;
        }
        if (!this.b(aj7)) {
            aj7 = null;
        }
        if (!this.c(aj8)) {
            aj8 = null;
        }
        if (!this.c(aj9)) {
            aj9 = null;
        }
        aj aj10 = null;
        if (aj4 != null && (aj10 == null || aj2.c(aj4) < aj2.c(aj10))) {
            aj10 = aj4;
        }
        if (aj5 != null && (aj10 == null || aj2.c(aj5) < aj2.c(aj10))) {
            aj10 = aj5;
        }
        if (aj6 != null && (aj10 == null || aj2.c(aj6) < aj2.c(aj10))) {
            aj10 = aj6;
        }
        if (aj7 != null && (aj10 == null || aj2.c(aj7) < aj2.c(aj10))) {
            aj10 = aj7;
        }
        if (aj8 != null && (aj10 == null || aj2.c(aj8) < aj2.c(aj10))) {
            aj10 = aj8;
        }
        if (aj9 != null && (aj10 == null || aj2.c(aj9) < aj2.c(aj10))) {
            aj10 = aj9;
        }
        if (aj10 == null) {
            return null;
        }
        int n5 = -1;
        if (aj10 == aj4) {
            n5 = 4;
        }
        if (aj10 == aj5) {
            n5 = 5;
        }
        if (aj10 == aj6) {
            n5 = 0;
        }
        if (aj10 == aj7) {
            n5 = 1;
        }
        if (aj10 == aj8) {
            n5 = 2;
        }
        if (aj10 == aj9) {
            n5 = 3;
        }
        return new mf(n2, n3, n4, n5, aj10.c(n2, n3, n4));
    }

    private boolean a(aj aj2) {
        if (aj2 == null) {
            return false;
        }
        return aj2.b >= this.bg && aj2.b <= this.bj && aj2.c >= this.bh && aj2.c <= this.bk;
    }

    private boolean b(aj aj2) {
        if (aj2 == null) {
            return false;
        }
        return aj2.a >= this.bf && aj2.a <= this.bi && aj2.c >= this.bh && aj2.c <= this.bk;
    }

    private boolean c(aj aj2) {
        if (aj2 == null) {
            return false;
        }
        return aj2.a >= this.bf && aj2.a <= this.bi && aj2.b >= this.bg && aj2.b <= this.bj;
    }

    public void c(cn cn2, int n2, int n3, int n4) {
    }

    public int g() {
        return 0;
    }

    public boolean a(cn cn2, int n2, int n3, int n4) {
        int n5 = cn2.a(n2, n3, n4);
        return n5 == 0 || ly.n[n5].bn.d();
    }

    public boolean a(cn cn2, int n2, int n3, int n4, dm dm2) {
        return false;
    }

    public void a(cn cn2, int n2, int n3, int n4, kh kh2) {
    }

    public void d(cn cn2, int n2, int n3, int n4, int n5) {
    }

    public void b(cn cn2, int n2, int n3, int n4, dm dm2) {
    }

    public void a(cn cn2, int n2, int n3, int n4, kh kh2, aj aj2) {
    }

    public void a(nm nm2, int n2, int n3, int n4) {
    }

    public int d(nm nm2, int n2, int n3, int n4) {
        return 0xFFFFFF;
    }

    public boolean b(nm nm2, int n2, int n3, int n4, int n5) {
        return false;
    }

    public boolean d() {
        return false;
    }

    public void b(cn cn2, int n2, int n3, int n4, kh kh2) {
    }

    public boolean c(cn cn2, int n2, int n3, int n4, int n5) {
        return false;
    }

    public void e() {
    }

    public void a_(cn cn2, int n2, int n3, int n4, int n5) {
        this.b_(cn2, n2, n3, n4, n5);
    }

    public boolean g(cn cn2, int n2, int n3, int n4) {
        return true;
    }

    static {
        for (int i2 = 0; i2 < 256; ++i2) {
            if (n[i2] == null) continue;
            di.c[i2] = new av(i2 - 256);
        }
    }
}

