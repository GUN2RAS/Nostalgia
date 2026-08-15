/*
 * Decompiled with CFR 0.152.
 */
import java.util.ArrayList;
import java.util.List;

public class cf {
    private static List g = new ArrayList();
    private static int h = 0;
    public double a;
    public double b;
    public double c;
    public double d;
    public double e;
    public double f;

    public static cf a(double d2, double d3, double d4, double d5, double d6, double d7) {
        return new cf(d2, d3, d4, d5, d6, d7);
    }

    public static void a() {
        h = 0;
    }

    public static cf b(double d2, double d3, double d4, double d5, double d6, double d7) {
        if (h >= g.size()) {
            g.add(cf.a(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        }
        return ((cf)g.get(h++)).c(d2, d3, d4, d5, d6, d7);
    }

    private cf(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.a = d2;
        this.b = d3;
        this.c = d4;
        this.d = d5;
        this.e = d6;
        this.f = d7;
    }

    public cf c(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.a = d2;
        this.b = d3;
        this.c = d4;
        this.d = d5;
        this.e = d6;
        this.f = d7;
        return this;
    }

    public cf a(double d2, double d3, double d4) {
        double d5 = this.a;
        double d6 = this.b;
        double d7 = this.c;
        double d8 = this.d;
        double d9 = this.e;
        double d10 = this.f;
        if (d2 < 0.0) {
            d5 += d2;
        }
        if (d2 > 0.0) {
            d8 += d2;
        }
        if (d3 < 0.0) {
            d6 += d3;
        }
        if (d3 > 0.0) {
            d9 += d3;
        }
        if (d4 < 0.0) {
            d7 += d4;
        }
        if (d4 > 0.0) {
            d10 += d4;
        }
        return cf.b(d5, d6, d7, d8, d9, d10);
    }

    public cf b(double d2, double d3, double d4) {
        double d5 = this.a - d2;
        double d6 = this.b - d3;
        double d7 = this.c - d4;
        double d8 = this.d + d2;
        double d9 = this.e + d3;
        double d10 = this.f + d4;
        return cf.b(d5, d6, d7, d8, d9, d10);
    }

    public cf c(double d2, double d3, double d4) {
        return cf.b(this.a + d2, this.b + d3, this.c + d4, this.d + d2, this.e + d3, this.f + d4);
    }

    public double a(cf cf2, double d2) {
        double d3;
        if (cf2.e <= this.b || cf2.b >= this.e) {
            return d2;
        }
        if (cf2.f <= this.c || cf2.c >= this.f) {
            return d2;
        }
        if (d2 > 0.0 && cf2.d <= this.a && (d3 = this.a - cf2.d) < d2) {
            d2 = d3;
        }
        if (d2 < 0.0 && cf2.a >= this.d && (d3 = this.d - cf2.a) > d2) {
            d2 = d3;
        }
        return d2;
    }

    public double b(cf cf2, double d2) {
        double d3;
        if (cf2.d <= this.a || cf2.a >= this.d) {
            return d2;
        }
        if (cf2.f <= this.c || cf2.c >= this.f) {
            return d2;
        }
        if (d2 > 0.0 && cf2.e <= this.b && (d3 = this.b - cf2.e) < d2) {
            d2 = d3;
        }
        if (d2 < 0.0 && cf2.b >= this.e && (d3 = this.e - cf2.b) > d2) {
            d2 = d3;
        }
        return d2;
    }

    public double c(cf cf2, double d2) {
        double d3;
        if (cf2.d <= this.a || cf2.a >= this.d) {
            return d2;
        }
        if (cf2.e <= this.b || cf2.b >= this.e) {
            return d2;
        }
        if (d2 > 0.0 && cf2.f <= this.c && (d3 = this.c - cf2.f) < d2) {
            d2 = d3;
        }
        if (d2 < 0.0 && cf2.c >= this.f && (d3 = this.f - cf2.c) > d2) {
            d2 = d3;
        }
        return d2;
    }

    public boolean a(cf cf2) {
        if (cf2.d <= this.a || cf2.a >= this.d) {
            return false;
        }
        if (cf2.e <= this.b || cf2.b >= this.e) {
            return false;
        }
        return !(cf2.f <= this.c) && !(cf2.c >= this.f);
    }

    public cf d(double d2, double d3, double d4) {
        this.a += d2;
        this.b += d3;
        this.c += d4;
        this.d += d2;
        this.e += d3;
        this.f += d4;
        return this;
    }

    public double b() {
        double d2 = this.d - this.a;
        double d3 = this.e - this.b;
        double d4 = this.f - this.c;
        return (d2 + d3 + d4) / 3.0;
    }

    public cf c() {
        return cf.b(this.a, this.b, this.c, this.d, this.e, this.f);
    }

    public mf a(aj aj2, aj aj3) {
        aj aj4 = aj2.a(aj3, this.a);
        aj aj5 = aj2.a(aj3, this.d);
        aj aj6 = aj2.b(aj3, this.b);
        aj aj7 = aj2.b(aj3, this.e);
        aj aj8 = aj2.c(aj3, this.c);
        aj aj9 = aj2.c(aj3, this.f);
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
        if (aj4 != null && (aj10 == null || aj2.d(aj4) < aj2.d(aj10))) {
            aj10 = aj4;
        }
        if (aj5 != null && (aj10 == null || aj2.d(aj5) < aj2.d(aj10))) {
            aj10 = aj5;
        }
        if (aj6 != null && (aj10 == null || aj2.d(aj6) < aj2.d(aj10))) {
            aj10 = aj6;
        }
        if (aj7 != null && (aj10 == null || aj2.d(aj7) < aj2.d(aj10))) {
            aj10 = aj7;
        }
        if (aj8 != null && (aj10 == null || aj2.d(aj8) < aj2.d(aj10))) {
            aj10 = aj8;
        }
        if (aj9 != null && (aj10 == null || aj2.d(aj9) < aj2.d(aj10))) {
            aj10 = aj9;
        }
        if (aj10 == null) {
            return null;
        }
        int n2 = -1;
        if (aj10 == aj4) {
            n2 = 4;
        }
        if (aj10 == aj5) {
            n2 = 5;
        }
        if (aj10 == aj6) {
            n2 = 0;
        }
        if (aj10 == aj7) {
            n2 = 1;
        }
        if (aj10 == aj8) {
            n2 = 2;
        }
        if (aj10 == aj9) {
            n2 = 3;
        }
        return new mf(0, 0, 0, n2, aj10);
    }

    private boolean a(aj aj2) {
        if (aj2 == null) {
            return false;
        }
        return aj2.b >= this.b && aj2.b <= this.e && aj2.c >= this.c && aj2.c <= this.f;
    }

    private boolean b(aj aj2) {
        if (aj2 == null) {
            return false;
        }
        return aj2.a >= this.a && aj2.a <= this.d && aj2.c >= this.c && aj2.c <= this.f;
    }

    private boolean c(aj aj2) {
        if (aj2 == null) {
            return false;
        }
        return aj2.a >= this.a && aj2.a <= this.d && aj2.b >= this.b && aj2.b <= this.e;
    }

    public void b(cf cf2) {
        this.a = cf2.a;
        this.b = cf2.b;
        this.c = cf2.c;
        this.d = cf2.d;
        this.e = cf2.e;
        this.f = cf2.f;
    }
}

