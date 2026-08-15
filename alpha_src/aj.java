/*
 * Decompiled with CFR 0.152.
 */
import java.util.ArrayList;
import java.util.List;

public class aj {
    private static List d = new ArrayList();
    private static int e = 0;
    public double a;
    public double b;
    public double c;

    public static aj a(double d2, double d3, double d4) {
        return new aj(d2, d3, d4);
    }

    public static void a() {
        e = 0;
    }

    public static aj b(double d2, double d3, double d4) {
        if (e >= d.size()) {
            d.add(aj.a(0.0, 0.0, 0.0));
        }
        return ((aj)d.get(e++)).e(d2, d3, d4);
    }

    private aj(double d2, double d3, double d4) {
        if (d2 == -0.0) {
            d2 = 0.0;
        }
        if (d3 == -0.0) {
            d3 = 0.0;
        }
        if (d4 == -0.0) {
            d4 = 0.0;
        }
        this.a = d2;
        this.b = d3;
        this.c = d4;
    }

    private aj e(double d2, double d3, double d4) {
        this.a = d2;
        this.b = d3;
        this.c = d4;
        return this;
    }

    public aj a(aj aj2) {
        return aj.b(aj2.a - this.a, aj2.b - this.b, aj2.c - this.c);
    }

    public aj b() {
        double d2 = eo.a(this.a * this.a + this.b * this.b + this.c * this.c);
        if (d2 < 1.0E-4) {
            return aj.b(0.0, 0.0, 0.0);
        }
        return aj.b(this.a / d2, this.b / d2, this.c / d2);
    }

    public aj b(aj aj2) {
        return aj.b(this.b * aj2.c - this.c * aj2.b, this.c * aj2.a - this.a * aj2.c, this.a * aj2.b - this.b * aj2.a);
    }

    public aj c(double d2, double d3, double d4) {
        return aj.b(this.a + d2, this.b + d3, this.c + d4);
    }

    public double c(aj aj2) {
        double d2 = aj2.a - this.a;
        double d3 = aj2.b - this.b;
        double d4 = aj2.c - this.c;
        return eo.a(d2 * d2 + d3 * d3 + d4 * d4);
    }

    public double d(aj aj2) {
        double d2 = aj2.a - this.a;
        double d3 = aj2.b - this.b;
        double d4 = aj2.c - this.c;
        return d2 * d2 + d3 * d3 + d4 * d4;
    }

    public double d(double d2, double d3, double d4) {
        double d5 = d2 - this.a;
        double d6 = d3 - this.b;
        double d7 = d4 - this.c;
        return d5 * d5 + d6 * d6 + d7 * d7;
    }

    public double c() {
        return eo.a(this.a * this.a + this.b * this.b + this.c * this.c);
    }

    public aj a(aj aj2, double d2) {
        double d3 = aj2.a - this.a;
        double d4 = aj2.b - this.b;
        double d5 = aj2.c - this.c;
        if (d3 * d3 < (double)1.0E-7f) {
            return null;
        }
        double d6 = (d2 - this.a) / d3;
        if (d6 < 0.0 || d6 > 1.0) {
            return null;
        }
        return aj.b(this.a + d3 * d6, this.b + d4 * d6, this.c + d5 * d6);
    }

    public aj b(aj aj2, double d2) {
        double d3 = aj2.a - this.a;
        double d4 = aj2.b - this.b;
        double d5 = aj2.c - this.c;
        if (d4 * d4 < (double)1.0E-7f) {
            return null;
        }
        double d6 = (d2 - this.b) / d4;
        if (d6 < 0.0 || d6 > 1.0) {
            return null;
        }
        return aj.b(this.a + d3 * d6, this.b + d4 * d6, this.c + d5 * d6);
    }

    public aj c(aj aj2, double d2) {
        double d3 = aj2.a - this.a;
        double d4 = aj2.b - this.b;
        double d5 = aj2.c - this.c;
        if (d5 * d5 < (double)1.0E-7f) {
            return null;
        }
        double d6 = (d2 - this.c) / d5;
        if (d6 < 0.0 || d6 > 1.0) {
            return null;
        }
        return aj.b(this.a + d3 * d6, this.b + d4 * d6, this.c + d5 * d6);
    }

    public String toString() {
        return "(" + this.a + ", " + this.b + ", " + this.c + ")";
    }

    public void a(float f2) {
        float f3 = eo.b(f2);
        float f4 = eo.a(f2);
        double d2 = this.a;
        double d3 = this.b * (double)f3 + this.c * (double)f4;
        double d4 = this.c * (double)f3 - this.b * (double)f4;
        this.a = d2;
        this.b = d3;
        this.c = d4;
    }

    public void b(float f2) {
        float f3 = eo.b(f2);
        float f4 = eo.a(f2);
        double d2 = this.a * (double)f3 + this.c * (double)f4;
        double d3 = this.b;
        double d4 = this.c * (double)f3 - this.a * (double)f4;
        this.a = d2;
        this.b = d3;
        this.c = d4;
    }
}

