/*
 * Decompiled with CFR 0.152.
 */
import java.util.ArrayList;
import java.util.List;

public class jc
extends kh {
    private int c = 0;
    public int a = 0;
    private int d;
    private int e;
    private int f;
    public er b;

    public jc(cn cn2) {
        super(cn2);
        this.aB = 0.0f;
        this.a(0.5f, 0.5f);
    }

    public jc(cn cn2, int n2, int n3, int n4, int n5) {
        this(cn2);
        this.d = n2;
        this.e = n3;
        this.f = n4;
        ArrayList<er> arrayList = new ArrayList<er>();
        er[] erArray = er.values();
        int n6 = erArray.length;
        for (int i2 = 0; i2 < n6; ++i2) {
            er er2;
            this.b = er2 = erArray[i2];
            this.b(n5);
            if (!this.i()) continue;
            arrayList.add(er2);
        }
        if (arrayList.size() > 0) {
            this.b = (er)((Object)arrayList.get(this.aQ.nextInt(arrayList.size())));
        }
        this.b(n5);
    }

    public void b(int n2) {
        this.a = n2;
        this.as = this.aq = (float)(n2 * 90);
        float f2 = this.b.z;
        float f3 = this.b.A;
        float f4 = this.b.z;
        if (n2 == 0 || n2 == 2) {
            f4 = 0.5f;
        } else {
            f2 = 0.5f;
        }
        f2 /= 32.0f;
        f3 /= 32.0f;
        f4 /= 32.0f;
        float f5 = (float)this.d + 0.5f;
        float f6 = (float)this.e + 0.5f;
        float f7 = (float)this.f + 0.5f;
        float f8 = 0.5625f;
        if (n2 == 0) {
            f7 -= f8;
        }
        if (n2 == 1) {
            f5 -= f8;
        }
        if (n2 == 2) {
            f7 += f8;
        }
        if (n2 == 3) {
            f5 += f8;
        }
        if (n2 == 0) {
            f5 -= this.c(this.b.z);
        }
        if (n2 == 1) {
            f7 += this.c(this.b.z);
        }
        if (n2 == 2) {
            f5 += this.c(this.b.z);
        }
        if (n2 == 3) {
            f7 -= this.c(this.b.z);
        }
        this.a((double)f5, (double)(f6 += this.c(this.b.A)), (double)f7);
        float f9 = -0.00625f;
        this.au.c(f5 - f2 - f9, f6 - f3 - f9, f7 - f4 - f9, f5 + f2 + f9, f6 + f3 + f9, f7 + f4 + f9);
    }

    private float c(int n2) {
        if (n2 == 32) {
            return 0.5f;
        }
        if (n2 == 64) {
            return 0.5f;
        }
        return 0.0f;
    }

    public void e_() {
        if (this.c++ == 100 && !this.i()) {
            this.c = 0;
            this.F();
            this.ag.a(new dx(this.ag, this.ak, this.al, this.am, new ev(di.aq)));
        }
    }

    public boolean i() {
        int n2;
        if (this.ag.a((kh)this, this.au).size() > 0) {
            return false;
        }
        int n3 = this.b.z / 16;
        int n4 = this.b.A / 16;
        int n5 = this.d;
        int n6 = this.e;
        int n7 = this.f;
        if (this.a == 0) {
            n5 = eo.b(this.ak - (double)((float)this.b.z / 32.0f));
        }
        if (this.a == 1) {
            n7 = eo.b(this.am - (double)((float)this.b.z / 32.0f));
        }
        if (this.a == 2) {
            n5 = eo.b(this.ak - (double)((float)this.b.z / 32.0f));
        }
        if (this.a == 3) {
            n7 = eo.b(this.am - (double)((float)this.b.z / 32.0f));
        }
        n6 = eo.b(this.al - (double)((float)this.b.A / 32.0f));
        for (int i2 = 0; i2 < n3; ++i2) {
            for (n2 = 0; n2 < n4; ++n2) {
                gb gb2 = this.a == 0 || this.a == 2 ? this.ag.f(n5 + i2, n6 + n2, this.f) : this.ag.f(this.d, n6 + n2, n7 + i2);
                if (gb2.a()) continue;
                return false;
            }
        }
        List list = this.ag.b(this, this.au);
        for (n2 = 0; n2 < list.size(); ++n2) {
            if (!(list.get(n2) instanceof jc)) continue;
            return false;
        }
        return true;
    }

    public boolean c_() {
        return true;
    }

    public boolean a(kh kh2, int n2) {
        this.F();
        this.ag.a(new dx(this.ag, this.ak, this.al, this.am, new ev(di.aq)));
        return true;
    }

    public void a(hm hm2) {
        hm2.a("Dir", (byte)this.a);
        hm2.a("Motive", this.b.y);
        hm2.a("TileX", this.d);
        hm2.a("TileY", this.e);
        hm2.a("TileZ", this.f);
    }

    public void b(hm hm2) {
        this.a = hm2.c("Dir");
        this.d = hm2.e("TileX");
        this.e = hm2.e("TileY");
        this.f = hm2.e("TileZ");
        String string = hm2.i("Motive");
        for (er er2 : er.values()) {
            if (!er2.y.equals(string)) continue;
            this.b = er2;
        }
        if (this.b == null) {
            this.b = er.a;
        }
        this.b(this.a);
    }
}

