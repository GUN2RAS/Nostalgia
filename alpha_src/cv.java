/*
 * Decompiled with CFR 0.152.
 */
public class cv
extends ly {
    protected cv(int n2, int n3) {
        super(n2, n3, gb.c);
    }

    public int a(int n2) {
        return this.bb + (n2 == 1 ? 1 : 0);
    }

    public boolean a(cn cn2, int n2, int n3, int n4, dm dm2) {
        int n5 = cn2.e(n2, n3, n4);
        if (n5 > 0) {
            this.e(cn2, n2, n3, n4, n5);
            return true;
        }
        return false;
    }

    public void e(cn cn2, int n2, int n3, int n4, int n5) {
        cn2.a((String)null, n2, n3, n4);
        cn2.b(n2, n3, n4, 0);
        int n6 = di.aQ.aS + n5 - 1;
        float f2 = 0.7f;
        double d2 = (double)(cn2.n.nextFloat() * f2) + (double)(1.0f - f2) * 0.5;
        double d3 = (double)(cn2.n.nextFloat() * f2) + (double)(1.0f - f2) * 0.2 + 0.6;
        double d4 = (double)(cn2.n.nextFloat() * f2) + (double)(1.0f - f2) * 0.5;
        dx dx2 = new dx(cn2, (double)n2 + d2, (double)n3 + d3, (double)n4 + d4, new ev(n6));
        dx2.c = 10;
        cn2.a(dx2);
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5, float f2) {
        if (cn2.y) {
            return;
        }
        if (n5 > 0) {
            this.e(cn2, n2, n3, n4, n5);
        }
        super.a(cn2, n2, n3, n4, n5, f2);
    }
}

