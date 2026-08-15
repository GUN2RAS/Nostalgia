/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class cn
implements nm {
    private List z = new ArrayList();
    public List a = new ArrayList();
    private List A = new ArrayList();
    private TreeSet B = new TreeSet();
    private Set C = new HashSet();
    public List b = new ArrayList();
    public long c = 0L;
    public boolean d = false;
    private long D = 0x88BBFFL;
    private long E = 12638463L;
    private long F = 0xFFFFFFL;
    public int e = 0;
    protected int f = new Random().nextInt();
    protected int g = 1013904223;
    public boolean h = false;
    public static float[] i = new float[16];
    private final long G = System.currentTimeMillis();
    protected int j = 40;
    public List k = new ArrayList();
    public int l;
    public Object m;
    public Random n = new Random();
    public int o;
    public int p;
    public int q;
    public boolean r = false;
    protected List s = new ArrayList();
    private aw H;
    public File t;
    public long u = 0L;
    private hm I;
    public long v = 0L;
    public final String w;
    public boolean x;
    private ArrayList J = new ArrayList();
    private Set K = new HashSet();
    private int L = this.n.nextInt(12000);
    private List M = new ArrayList();
    public boolean y = false;

    public static hm a(File file, String string) {
        File file2 = new File(file, "saves");
        File file3 = new File(file2, string);
        if (!file3.exists()) {
            return null;
        }
        File file4 = new File(file3, "level.dat");
        if (file4.exists()) {
            try {
                hm hm2 = x.a(new FileInputStream(file4));
                hm hm3 = hm2.k("Data");
                return hm3;
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static void b(File file, String string) {
        File file2 = new File(file, "saves");
        File file3 = new File(file2, string);
        if (!file3.exists()) {
            return;
        }
        cn.a(file3.listFiles());
        file3.delete();
    }

    private static void a(File[] fileArray) {
        for (int i2 = 0; i2 < fileArray.length; ++i2) {
            if (fileArray[i2].isDirectory()) {
                cn.a(fileArray[i2].listFiles());
            }
            fileArray[i2].delete();
        }
    }

    public cn(File file, String string) {
        this(file, string, new Random().nextLong());
    }

    public cn(String string) {
        this.w = string;
        this.H = this.a(this.t);
        this.f();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public cn(File file, String string, long l2) {
        Object object;
        File file2;
        this.w = string;
        file.mkdirs();
        this.t = new File(file, string);
        this.t.mkdirs();
        try {
            file2 = new File(this.t, "session.lock");
            object = new DataOutputStream(new FileOutputStream(file2));
            try {
                ((DataOutputStream)object).writeLong(this.G);
            }
            finally {
                ((FilterOutputStream)object).close();
            }
        }
        catch (IOException iOException) {
            throw new RuntimeException("Failed to check session lock, aborting");
        }
        file2 = new File(this.t, "level.dat");
        boolean bl2 = this.r = !file2.exists();
        if (file2.exists()) {
            try {
                object = x.a(new FileInputStream(file2));
                hm hm2 = ((hm)object).k("Data");
                this.u = hm2.f("RandomSeed");
                this.o = hm2.e("SpawnX");
                this.p = hm2.e("SpawnY");
                this.q = hm2.e("SpawnZ");
                this.c = hm2.f("Time");
                this.v = hm2.f("SizeOnDisk");
                this.d = hm2.m("SnowCovered");
                if (hm2.b("Player")) {
                    this.I = hm2.k("Player");
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            this.d = this.n.nextInt(4) == 0;
        }
        boolean bl3 = false;
        if (this.u == 0L) {
            this.u = l2;
            bl3 = true;
        }
        this.H = this.a(this.t);
        if (bl3) {
            this.x = true;
            this.o = 0;
            this.p = 64;
            this.q = 0;
            while (!this.f(this.o, this.q)) {
                this.o += this.n.nextInt(64) - this.n.nextInt(64);
                this.q += this.n.nextInt(64) - this.n.nextInt(64);
            }
            this.x = false;
        }
        this.f();
    }

    protected aw a(File file) {
        return new ft(this, new le(file, true), new nw(this, this.u));
    }

    public void a() {
        if (this.p <= 0) {
            this.p = 64;
        }
        while (this.g(this.o, this.q) == 0) {
            this.o += this.n.nextInt(8) - this.n.nextInt(8);
            this.q += this.n.nextInt(8) - this.n.nextInt(8);
        }
    }

    private boolean f(int n2, int n3) {
        int n4 = this.g(n2, n3);
        return n4 == ly.F.bc;
    }

    private int g(int n2, int n3) {
        int n4 = 63;
        while (this.a(n2, n4 + 1, n3) != 0) {
            ++n4;
        }
        return this.a(n2, n4, n3);
    }

    public void a(dm dm2) {
        try {
            if (this.I != null) {
                dm2.e(this.I);
                this.I = null;
            }
            this.a((kh)dm2);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a(boolean bl2, nu nu2) {
        if (!this.H.b()) {
            return;
        }
        if (nu2 != null) {
            nu2.b("Saving level");
        }
        this.m();
        if (nu2 != null) {
            nu2.d("Saving chunks");
        }
        this.H.a(bl2, nu2);
    }

    private void m() {
        hm hm2;
        this.l();
        hm hm3 = new hm();
        hm3.a("RandomSeed", this.u);
        hm3.a("SpawnX", this.o);
        hm3.a("SpawnY", this.p);
        hm3.a("SpawnZ", this.q);
        hm3.a("Time", this.c);
        hm3.a("SizeOnDisk", this.v);
        hm3.a("SnowCovered", this.d);
        hm3.a("LastPlayed", System.currentTimeMillis());
        dm dm2 = null;
        if (this.k.size() > 0) {
            dm2 = (dm)this.k.get(0);
        }
        if (dm2 != null) {
            hm2 = new hm();
            dm2.d(hm2);
            hm3.a("Player", hm2);
        }
        hm2 = new hm();
        hm2.a("Data", (el)hm3);
        try {
            File file = new File(this.t, "level.dat_new");
            File file2 = new File(this.t, "level.dat_old");
            File file3 = new File(this.t, "level.dat");
            x.a(hm2, new FileOutputStream(file));
            if (file2.exists()) {
                file2.delete();
            }
            file3.renameTo(file2);
            if (file3.exists()) {
                file3.delete();
            }
            file.renameTo(file3);
            if (file.exists()) {
                file.delete();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public boolean a(int n2) {
        if (!this.H.b()) {
            return true;
        }
        if (n2 == 0) {
            this.m();
        }
        return this.H.a(false, null);
    }

    public int a(int n2, int n3, int n4) {
        if (n2 < -32000000 || n4 < -32000000 || n2 >= 32000000 || n4 > 32000000) {
            return 0;
        }
        if (n3 < 0) {
            return 0;
        }
        if (n3 >= 128) {
            return 0;
        }
        return this.b(n2 >> 4, n4 >> 4).a(n2 & 0xF, n3, n4 & 0xF);
    }

    public boolean d(int n2, int n3, int n4) {
        if (n3 < 0 || n3 >= 128) {
            return false;
        }
        return this.h(n2 >> 4, n4 >> 4);
    }

    public boolean a(int n2, int n3, int n4, int n5, int n6, int n7) {
        if (n6 < 0 || n3 >= 128) {
            return false;
        }
        n2 >>= 4;
        n3 >>= 4;
        n4 >>= 4;
        n5 >>= 4;
        n6 >>= 4;
        n7 >>= 4;
        for (int i2 = n2; i2 <= n5; ++i2) {
            for (int i3 = n4; i3 <= n7; ++i3) {
                if (this.h(i2, i3)) continue;
                return false;
            }
        }
        return true;
    }

    private boolean h(int n2, int n3) {
        return this.H.a(n2, n3);
    }

    public ga a(int n2, int n3) {
        return this.b(n2 >> 4, n3 >> 4);
    }

    public ga b(int n2, int n3) {
        return this.H.b(n2, n3);
    }

    public boolean a(int n2, int n3, int n4, int n5, int n6) {
        if (n2 < -32000000 || n4 < -32000000 || n2 >= 32000000 || n4 > 32000000) {
            return false;
        }
        if (n3 < 0) {
            return false;
        }
        if (n3 >= 128) {
            return false;
        }
        ga ga2 = this.b(n2 >> 4, n4 >> 4);
        return ga2.a(n2 & 0xF, n3, n4 & 0xF, n5, n6);
    }

    public boolean a(int n2, int n3, int n4, int n5) {
        if (n2 < -32000000 || n4 < -32000000 || n2 >= 32000000 || n4 > 32000000) {
            return false;
        }
        if (n3 < 0) {
            return false;
        }
        if (n3 >= 128) {
            return false;
        }
        ga ga2 = this.b(n2 >> 4, n4 >> 4);
        return ga2.a(n2 & 0xF, n3, n4 & 0xF, n5);
    }

    public gb f(int n2, int n3, int n4) {
        int n5 = this.a(n2, n3, n4);
        if (n5 == 0) {
            return gb.a;
        }
        return ly.n[n5].bn;
    }

    public int e(int n2, int n3, int n4) {
        if (n2 < -32000000 || n4 < -32000000 || n2 >= 32000000 || n4 > 32000000) {
            return 0;
        }
        if (n3 < 0) {
            return 0;
        }
        if (n3 >= 128) {
            return 0;
        }
        ga ga2 = this.b(n2 >> 4, n4 >> 4);
        return ga2.b(n2 &= 0xF, n3, n4 &= 0xF);
    }

    public void b(int n2, int n3, int n4, int n5) {
        this.c(n2, n3, n4, n5);
    }

    public boolean c(int n2, int n3, int n4, int n5) {
        if (n2 < -32000000 || n4 < -32000000 || n2 >= 32000000 || n4 > 32000000) {
            return false;
        }
        if (n3 < 0) {
            return false;
        }
        if (n3 >= 128) {
            return false;
        }
        ga ga2 = this.b(n2 >> 4, n4 >> 4);
        ga2.b(n2 &= 0xF, n3, n4 &= 0xF, n5);
        return true;
    }

    public boolean d(int n2, int n3, int n4, int n5) {
        if (this.a(n2, n3, n4, n5)) {
            this.e(n2, n3, n4, n5);
            return true;
        }
        return false;
    }

    public boolean b(int n2, int n3, int n4, int n5, int n6) {
        if (this.a(n2, n3, n4, n5, n6)) {
            this.e(n2, n3, n4, n5);
            return true;
        }
        return false;
    }

    public void h(int n2, int n3, int n4) {
        for (int i2 = 0; i2 < this.s.size(); ++i2) {
            ((im)this.s.get(i2)).a(n2, n3, n4);
        }
    }

    protected void e(int n2, int n3, int n4, int n5) {
        this.h(n2, n3, n4);
        this.g(n2, n3, n4, n5);
    }

    public void f(int n2, int n3, int n4, int n5) {
        if (n4 > n5) {
            int n6 = n5;
            n5 = n4;
            n4 = n6;
        }
        this.b(n2, n4, n3, n2, n5, n3);
    }

    public void b(int n2, int n3, int n4, int n5, int n6, int n7) {
        for (int i2 = 0; i2 < this.s.size(); ++i2) {
            ((im)this.s.get(i2)).b(n2, n3, n4, n5, n6, n7);
        }
    }

    public void g(int n2, int n3, int n4, int n5) {
        this.l(n2 - 1, n3, n4, n5);
        this.l(n2 + 1, n3, n4, n5);
        this.l(n2, n3 - 1, n4, n5);
        this.l(n2, n3 + 1, n4, n5);
        this.l(n2, n3, n4 - 1, n5);
        this.l(n2, n3, n4 + 1, n5);
    }

    private void l(int n2, int n3, int n4, int n5) {
        if (this.h || this.y) {
            return;
        }
        ly ly2 = ly.n[this.a(n2, n3, n4)];
        if (ly2 != null) {
            ly2.a(this, n2, n3, n4, n5);
        }
    }

    public boolean i(int n2, int n3, int n4) {
        return this.b(n2 >> 4, n4 >> 4).c(n2 & 0xF, n3, n4 & 0xF);
    }

    public int j(int n2, int n3, int n4) {
        return this.a(n2, n3, n4, true);
    }

    public int a(int n2, int n3, int n4, boolean bl2) {
        int n5;
        if (n2 < -32000000 || n4 < -32000000 || n2 >= 32000000 || n4 > 32000000) {
            return 15;
        }
        if (bl2 && ((n5 = this.a(n2, n3, n4)) == ly.al.bc || n5 == ly.aB.bc)) {
            int n6 = this.a(n2, n3 + 1, n4, false);
            int n7 = this.a(n2 + 1, n3, n4, false);
            int n8 = this.a(n2 - 1, n3, n4, false);
            int n9 = this.a(n2, n3, n4 + 1, false);
            int n10 = this.a(n2, n3, n4 - 1, false);
            if (n7 > n6) {
                n6 = n7;
            }
            if (n8 > n6) {
                n6 = n8;
            }
            if (n9 > n6) {
                n6 = n9;
            }
            if (n10 > n6) {
                n6 = n10;
            }
            return n6;
        }
        if (n3 < 0) {
            return 0;
        }
        if (n3 >= 128) {
            n5 = 15 - this.e;
            if (n5 < 0) {
                n5 = 0;
            }
            return n5;
        }
        ga ga2 = this.b(n2 >> 4, n4 >> 4);
        return ga2.c(n2 &= 0xF, n3, n4 &= 0xF, this.e);
    }

    public boolean k(int n2, int n3, int n4) {
        if (n2 < -32000000 || n4 < -32000000 || n2 >= 32000000 || n4 > 32000000) {
            return false;
        }
        if (n3 < 0) {
            return false;
        }
        if (n3 >= 128) {
            return true;
        }
        if (!this.h(n2 >> 4, n4 >> 4)) {
            return false;
        }
        ga ga2 = this.b(n2 >> 4, n4 >> 4);
        return ga2.c(n2 &= 0xF, n3, n4 &= 0xF);
    }

    public int c(int n2, int n3) {
        if (n2 < -32000000 || n3 < -32000000 || n2 >= 32000000 || n3 > 32000000) {
            return 0;
        }
        if (!this.h(n2 >> 4, n3 >> 4)) {
            return 0;
        }
        ga ga2 = this.b(n2 >> 4, n3 >> 4);
        return ga2.b(n2 & 0xF, n3 & 0xF);
    }

    public void a(by by2, int n2, int n3, int n4, int n5) {
        int n6;
        if (!this.d(n2, n3, n4)) {
            return;
        }
        if (by2 == by.a) {
            if (this.k(n2, n3, n4)) {
                n5 = 15;
            }
        } else if (by2 == by.b && ly.t[n6 = this.a(n2, n3, n4)] > n5) {
            n5 = ly.t[n6];
        }
        if (this.a(by2, n2, n3, n4) != n5) {
            this.a(by2, n2, n3, n4, n2, n3, n4);
        }
    }

    public int a(by by2, int n2, int n3, int n4) {
        if (n3 < 0 || n3 >= 128 || n2 < -32000000 || n4 < -32000000 || n2 >= 32000000 || n4 > 32000000) {
            return by2.c;
        }
        int n5 = n2 >> 4;
        int n6 = n4 >> 4;
        if (!this.h(n5, n6)) {
            return 0;
        }
        ga ga2 = this.b(n5, n6);
        return ga2.a(by2, n2 & 0xF, n3, n4 & 0xF);
    }

    public void b(by by2, int n2, int n3, int n4, int n5) {
        if (n2 < -32000000 || n4 < -32000000 || n2 >= 32000000 || n4 > 32000000) {
            return;
        }
        if (n3 < 0) {
            return;
        }
        if (n3 >= 128) {
            return;
        }
        if (!this.h(n2 >> 4, n4 >> 4)) {
            return;
        }
        ga ga2 = this.b(n2 >> 4, n4 >> 4);
        ga2.a(by2, n2 & 0xF, n3, n4 & 0xF, n5);
        for (int i2 = 0; i2 < this.s.size(); ++i2) {
            ((im)this.s.get(i2)).a(n2, n3, n4);
        }
    }

    public float c(int n2, int n3, int n4) {
        return i[this.j(n2, n3, n4)];
    }

    public boolean b() {
        return this.e < 4;
    }

    public mf a(aj aj2, aj aj3) {
        return this.a(aj2, aj3, false);
    }

    public mf a(aj aj2, aj aj3, boolean bl2) {
        if (Double.isNaN(aj2.a) || Double.isNaN(aj2.b) || Double.isNaN(aj2.c)) {
            return null;
        }
        if (Double.isNaN(aj3.a) || Double.isNaN(aj3.b) || Double.isNaN(aj3.c)) {
            return null;
        }
        int n2 = eo.b(aj3.a);
        int n3 = eo.b(aj3.b);
        int n4 = eo.b(aj3.c);
        int n5 = eo.b(aj2.a);
        int n6 = eo.b(aj2.b);
        int n7 = eo.b(aj2.c);
        int n8 = 20;
        while (n8-- >= 0) {
            mf mf2;
            if (Double.isNaN(aj2.a) || Double.isNaN(aj2.b) || Double.isNaN(aj2.c)) {
                return null;
            }
            if (n5 == n2 && n6 == n3 && n7 == n4) {
                return null;
            }
            double d2 = 999.0;
            double d3 = 999.0;
            double d4 = 999.0;
            if (n2 > n5) {
                d2 = (double)n5 + 1.0;
            }
            if (n2 < n5) {
                d2 = (double)n5 + 0.0;
            }
            if (n3 > n6) {
                d3 = (double)n6 + 1.0;
            }
            if (n3 < n6) {
                d3 = (double)n6 + 0.0;
            }
            if (n4 > n7) {
                d4 = (double)n7 + 1.0;
            }
            if (n4 < n7) {
                d4 = (double)n7 + 0.0;
            }
            double d5 = 999.0;
            double d6 = 999.0;
            double d7 = 999.0;
            double d8 = aj3.a - aj2.a;
            double d9 = aj3.b - aj2.b;
            double d10 = aj3.c - aj2.c;
            if (d2 != 999.0) {
                d5 = (d2 - aj2.a) / d8;
            }
            if (d3 != 999.0) {
                d6 = (d3 - aj2.b) / d9;
            }
            if (d4 != 999.0) {
                d7 = (d4 - aj2.c) / d10;
            }
            int n9 = 0;
            if (d5 < d6 && d5 < d7) {
                n9 = n2 > n5 ? 4 : 5;
                aj2.a = d2;
                aj2.b += d9 * d5;
                aj2.c += d10 * d5;
            } else if (d6 < d7) {
                n9 = n3 > n6 ? 0 : 1;
                aj2.a += d8 * d6;
                aj2.b = d3;
                aj2.c += d10 * d6;
            } else {
                n9 = n4 > n7 ? 2 : 3;
                aj2.a += d8 * d7;
                aj2.b += d9 * d7;
                aj2.c = d4;
            }
            aj aj4 = aj.b(aj2.a, aj2.b, aj2.c);
            aj4.a = eo.b(aj2.a);
            n5 = (int)aj4.a;
            if (n9 == 5) {
                --n5;
                aj4.a += 1.0;
            }
            aj4.b = eo.b(aj2.b);
            n6 = (int)aj4.b;
            if (n9 == 1) {
                --n6;
                aj4.b += 1.0;
            }
            aj4.c = eo.b(aj2.c);
            n7 = (int)aj4.c;
            if (n9 == 3) {
                --n7;
                aj4.c += 1.0;
            }
            int n10 = this.a(n5, n6, n7);
            int n11 = this.e(n5, n6, n7);
            ly ly2 = ly.n[n10];
            if (n10 <= 0 || !ly2.a(n11, bl2) || (mf2 = ly2.a(this, n5, n6, n7, aj2, aj3)) == null) continue;
            return mf2;
        }
        return null;
    }

    public void a(kh kh2, String string, float f2, float f3) {
        for (int i2 = 0; i2 < this.s.size(); ++i2) {
            ((im)this.s.get(i2)).a(string, kh2.ak, kh2.al - (double)kh2.aB, kh2.am, f2, f3);
        }
    }

    public void a(double d2, double d3, double d4, String string, float f2, float f3) {
        for (int i2 = 0; i2 < this.s.size(); ++i2) {
            ((im)this.s.get(i2)).a(string, d2, d3, d4, f2, f3);
        }
    }

    public void a(String string, int n2, int n3, int n4) {
        for (int i2 = 0; i2 < this.s.size(); ++i2) {
            ((im)this.s.get(i2)).a(string, n2, n3, n4);
        }
    }

    public void a(String string, double d2, double d3, double d4, double d5, double d6, double d7) {
        for (int i2 = 0; i2 < this.s.size(); ++i2) {
            ((im)this.s.get(i2)).a(string, d2, d3, d4, d5, d6, d7);
        }
    }

    public boolean a(kh kh2) {
        int n2 = eo.b(kh2.ak / 16.0);
        int n3 = eo.b(kh2.am / 16.0);
        boolean bl2 = false;
        if (kh2 instanceof dm) {
            bl2 = true;
        }
        if (bl2 || this.h(n2, n3)) {
            if (kh2 instanceof dm) {
                this.k.add((dm)kh2);
                System.out.println("Player count: " + this.k.size());
            }
            this.b(n2, n3).a(kh2);
            this.a.add(kh2);
            this.b(kh2);
            return true;
        }
        return false;
    }

    protected void b(kh kh2) {
        for (int i2 = 0; i2 < this.s.size(); ++i2) {
            ((im)this.s.get(i2)).a(kh2);
        }
    }

    protected void c(kh kh2) {
        for (int i2 = 0; i2 < this.s.size(); ++i2) {
            ((im)this.s.get(i2)).b(kh2);
        }
    }

    public void d(kh kh2) {
        kh2.F();
        if (kh2 instanceof dm) {
            this.k.remove((dm)kh2);
            System.out.println("Player count: " + this.k.size());
        }
    }

    public void a(im im2) {
        this.s.add(im2);
    }

    public void b(im im2) {
        this.s.remove(im2);
    }

    public List a(kh kh2, cf cf2) {
        this.J.clear();
        int n2 = eo.b(cf2.a);
        int n3 = eo.b(cf2.d + 1.0);
        int n4 = eo.b(cf2.b);
        int n5 = eo.b(cf2.e + 1.0);
        int n6 = eo.b(cf2.c);
        int n7 = eo.b(cf2.f + 1.0);
        for (int i2 = n2; i2 < n3; ++i2) {
            for (int i3 = n6; i3 < n7; ++i3) {
                if (!this.d(i2, 64, i3)) continue;
                for (int i4 = n4 - 1; i4 < n5; ++i4) {
                    ly ly2 = ly.n[this.a(i2, i4, i3)];
                    if (ly2 == null) continue;
                    ly2.a(this, i2, i4, i3, cf2, this.J);
                }
            }
        }
        double d2 = 0.25;
        List list = this.b(kh2, cf2.b(d2, d2, d2));
        for (int i5 = 0; i5 < list.size(); ++i5) {
            cf cf3 = ((kh)list.get(i5)).f_();
            if (cf3 != null && cf3.a(cf2)) {
                this.J.add(cf3);
            }
            if ((cf3 = kh2.b_((kh)list.get(i5))) == null || !cf3.a(cf2)) continue;
            this.J.add(cf3);
        }
        return this.J;
    }

    public int a(float f2) {
        float f3 = this.c(f2);
        float f4 = 1.0f - (eo.b(f3 * (float)Math.PI * 2.0f) * 2.0f + 0.5f);
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        return (int)(f4 * 11.0f);
    }

    public aj b(float f2) {
        float f3 = this.c(f2);
        float f4 = eo.b(f3 * (float)Math.PI * 2.0f) * 2.0f + 0.5f;
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        float f5 = (float)(this.D >> 16 & 0xFFL) / 255.0f;
        float f6 = (float)(this.D >> 8 & 0xFFL) / 255.0f;
        float f7 = (float)(this.D & 0xFFL) / 255.0f;
        return aj.b(f5 *= f4, f6 *= f4, f7 *= f4);
    }

    public float c(float f2) {
        int n2 = (int)(this.c % 24000L);
        float f3 = ((float)n2 + f2) / 24000.0f - 0.25f;
        if (f3 < 0.0f) {
            f3 += 1.0f;
        }
        if (f3 > 1.0f) {
            f3 -= 1.0f;
        }
        float f4 = f3;
        f3 = 1.0f - (float)((Math.cos((double)f3 * Math.PI) + 1.0) / 2.0);
        f3 = f4 + (f3 - f4) / 3.0f;
        return f3;
    }

    public aj d(float f2) {
        float f3 = this.c(f2);
        float f4 = eo.b(f3 * (float)Math.PI * 2.0f) * 2.0f + 0.5f;
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        float f5 = (float)(this.F >> 16 & 0xFFL) / 255.0f;
        float f6 = (float)(this.F >> 8 & 0xFFL) / 255.0f;
        float f7 = (float)(this.F & 0xFFL) / 255.0f;
        return aj.b(f5 *= f4 * 0.9f + 0.1f, f6 *= f4 * 0.9f + 0.1f, f7 *= f4 * 0.85f + 0.15f);
    }

    public aj e(float f2) {
        float f3 = this.c(f2);
        float f4 = eo.b(f3 * (float)Math.PI * 2.0f) * 2.0f + 0.5f;
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        float f5 = (float)(this.E >> 16 & 0xFFL) / 255.0f;
        float f6 = (float)(this.E >> 8 & 0xFFL) / 255.0f;
        float f7 = (float)(this.E & 0xFFL) / 255.0f;
        return aj.b(f5 *= f4 * 0.94f + 0.06f, f6 *= f4 * 0.94f + 0.06f, f7 *= f4 * 0.91f + 0.09f);
    }

    public int d(int n2, int n3) {
        ga ga2 = this.a(n2, n3);
        n2 &= 0xF;
        n3 &= 0xF;
        for (int i2 = 127; i2 > 0; --i2) {
            int n4 = ga2.a(n2, i2, n3);
            if (n4 == 0 || !ly.n[n4].bn.c() && !ly.n[n4].bn.d()) {
                continue;
            }
            return i2 + 1;
        }
        return -1;
    }

    public int e(int n2, int n3) {
        return this.a(n2, n3).b(n2 & 0xF, n3 & 0xF);
    }

    public float f(float f2) {
        float f3 = this.c(f2);
        float f4 = 1.0f - (eo.b(f3 * (float)Math.PI * 2.0f) * 2.0f + 0.75f);
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        return f4 * f4 * 0.5f;
    }

    public void h(int n2, int n3, int n4, int n5) {
        jf jf2 = new jf(n2, n3, n4, n5);
        int n6 = 8;
        if (this.a(n2 - n6, n3 - n6, n4 - n6, n2 + n6, n3 + n6, n4 + n6)) {
            if (n5 > 0) {
                jf2.a((long)ly.n[n5].a() + this.c);
            }
            if (!this.C.contains(jf2)) {
                this.C.add(jf2);
                this.B.add(jf2);
            }
        }
    }

    public void c() {
        int n2;
        int n3;
        Object object;
        int n4;
        this.a.removeAll(this.A);
        for (n4 = 0; n4 < this.A.size(); ++n4) {
            object = (kh)this.A.get(n4);
            n3 = ((kh)object).ba;
            n2 = ((kh)object).bc;
            if (!((kh)object).aZ || !this.h(n3, n2)) continue;
            this.b(n3, n2).b((kh)object);
        }
        for (n4 = 0; n4 < this.A.size(); ++n4) {
            this.c((kh)this.A.get(n4));
        }
        this.A.clear();
        for (n4 = 0; n4 < this.a.size(); ++n4) {
            object = (kh)this.a.get(n4);
            if (((kh)object).af != null) {
                if (!((kh)object).af.aA && ((kh)object).af.ae == object) continue;
                ((kh)object).af.ae = null;
                ((kh)object).af = null;
            }
            if (!((kh)object).aA) {
                this.e((kh)object);
            }
            if (!((kh)object).aA) continue;
            n3 = ((kh)object).ba;
            n2 = ((kh)object).bc;
            if (((kh)object).aZ && this.h(n3, n2)) {
                this.b(n3, n2).b((kh)object);
            }
            this.a.remove(n4--);
            this.c((kh)object);
        }
        for (n4 = 0; n4 < this.b.size(); ++n4) {
            object = (ic)this.b.get(n4);
            ((ic)object).b();
        }
    }

    protected void e(kh kh2) {
        int n2;
        int n3;
        int n4 = eo.b(kh2.ak);
        if (!this.a(n4 - (n3 = 16), 0, (n2 = eo.b(kh2.am)) - n3, n4 + n3, 128, n2 + n3)) {
            return;
        }
        kh2.aI = kh2.ak;
        kh2.aJ = kh2.al;
        kh2.aK = kh2.am;
        kh2.as = kh2.aq;
        kh2.at = kh2.ar;
        if (kh2.af != null) {
            kh2.p();
        } else {
            kh2.e_();
        }
        int n5 = eo.b(kh2.ak / 16.0);
        int n6 = eo.b(kh2.al / 16.0);
        int n7 = eo.b(kh2.am / 16.0);
        if (!kh2.aZ || kh2.ba != n5 || kh2.bb != n6 || kh2.bc != n7) {
            if (kh2.aZ && this.h(kh2.ba, kh2.bc)) {
                this.b(kh2.ba, kh2.bc).a(kh2, kh2.bb);
            }
            if (this.h(n5, n7)) {
                this.b(n5, n7).a(kh2);
            } else {
                kh2.aZ = false;
                System.out.println("Removing entity because it's not in a chunk!!");
                kh2.F();
            }
        }
        if (kh2.ae != null) {
            if (kh2.ae.aA || kh2.ae.af != kh2) {
                kh2.ae.af = null;
                kh2.ae = null;
            } else {
                this.e(kh2.ae);
            }
        }
        if (Double.isNaN(kh2.ak) || Double.isInfinite(kh2.ak)) {
            kh2.ak = kh2.aI;
        }
        if (Double.isNaN(kh2.al) || Double.isInfinite(kh2.al)) {
            kh2.al = kh2.aJ;
        }
        if (Double.isNaN(kh2.am) || Double.isInfinite(kh2.am)) {
            kh2.am = kh2.aK;
        }
        if (Double.isNaN(kh2.ar) || Double.isInfinite(kh2.ar)) {
            kh2.ar = kh2.at;
        }
        if (Double.isNaN(kh2.aq) || Double.isInfinite(kh2.aq)) {
            kh2.aq = kh2.as;
        }
    }

    public boolean a(cf cf2) {
        List list = this.b(null, cf2);
        for (int i2 = 0; i2 < list.size(); ++i2) {
            kh kh2 = (kh)list.get(i2);
            if (kh2.aA || !kh2.ad) continue;
            return false;
        }
        return true;
    }

    public boolean b(cf cf2) {
        int n2 = eo.b(cf2.a);
        int n3 = eo.b(cf2.d + 1.0);
        int n4 = eo.b(cf2.b);
        int n5 = eo.b(cf2.e + 1.0);
        int n6 = eo.b(cf2.c);
        int n7 = eo.b(cf2.f + 1.0);
        if (cf2.a < 0.0) {
            --n2;
        }
        if (cf2.b < 0.0) {
            --n4;
        }
        if (cf2.c < 0.0) {
            --n6;
        }
        for (int i2 = n2; i2 < n3; ++i2) {
            for (int i3 = n4; i3 < n5; ++i3) {
                for (int i4 = n6; i4 < n7; ++i4) {
                    ly ly2 = ly.n[this.a(i2, i3, i4)];
                    if (ly2 == null || !ly2.bn.d()) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean c(cf cf2) {
        int n2 = eo.b(cf2.a);
        int n3 = eo.b(cf2.d + 1.0);
        int n4 = eo.b(cf2.b);
        int n5 = eo.b(cf2.e + 1.0);
        int n6 = eo.b(cf2.c);
        int n7 = eo.b(cf2.f + 1.0);
        for (int i2 = n2; i2 < n3; ++i2) {
            for (int i3 = n4; i3 < n5; ++i3) {
                for (int i4 = n6; i4 < n7; ++i4) {
                    int n8 = this.a(i2, i3, i4);
                    if (n8 != ly.as.bc && n8 != ly.D.bc && n8 != ly.E.bc) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean a(cf cf2, gb gb2, kh kh2) {
        int n2 = eo.b(cf2.a);
        int n3 = eo.b(cf2.d + 1.0);
        int n4 = eo.b(cf2.b);
        int n5 = eo.b(cf2.e + 1.0);
        int n6 = eo.b(cf2.c);
        int n7 = eo.b(cf2.f + 1.0);
        boolean bl2 = false;
        aj aj2 = aj.b(0.0, 0.0, 0.0);
        for (int i2 = n2; i2 < n3; ++i2) {
            for (int i3 = n4; i3 < n5; ++i3) {
                for (int i4 = n6; i4 < n7; ++i4) {
                    double d2;
                    ly ly2 = ly.n[this.a(i2, i3, i4)];
                    if (ly2 == null || ly2.bn != gb2 || !((double)n5 >= (d2 = (double)((float)(i3 + 1) - jp.b(this.e(i2, i3, i4)))))) continue;
                    bl2 = true;
                    ly2.a(this, i2, i3, i4, kh2, aj2);
                }
            }
        }
        if (aj2.c() > 0.0) {
            aj2 = aj2.b();
            double d3 = 0.004;
            kh2.an += aj2.a * d3;
            kh2.ao += aj2.b * d3;
            kh2.ap += aj2.c * d3;
        }
        return bl2;
    }

    public boolean a(cf cf2, gb gb2) {
        int n2 = eo.b(cf2.a);
        int n3 = eo.b(cf2.d + 1.0);
        int n4 = eo.b(cf2.b);
        int n5 = eo.b(cf2.e + 1.0);
        int n6 = eo.b(cf2.c);
        int n7 = eo.b(cf2.f + 1.0);
        for (int i2 = n2; i2 < n3; ++i2) {
            for (int i3 = n4; i3 < n5; ++i3) {
                for (int i4 = n6; i4 < n7; ++i4) {
                    ly ly2 = ly.n[this.a(i2, i3, i4)];
                    if (ly2 == null || ly2.bn != gb2) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean b(cf cf2, gb gb2) {
        int n2 = eo.b(cf2.a);
        int n3 = eo.b(cf2.d + 1.0);
        int n4 = eo.b(cf2.b);
        int n5 = eo.b(cf2.e + 1.0);
        int n6 = eo.b(cf2.c);
        int n7 = eo.b(cf2.f + 1.0);
        for (int i2 = n2; i2 < n3; ++i2) {
            for (int i3 = n4; i3 < n5; ++i3) {
                for (int i4 = n6; i4 < n7; ++i4) {
                    ly ly2 = ly.n[this.a(i2, i3, i4)];
                    if (ly2 == null || ly2.bn != gb2) continue;
                    int n8 = this.e(i2, i3, i4);
                    double d2 = i3 + 1;
                    if (n8 < 8) {
                        d2 = (double)(i3 + 1) - (double)n8 / 8.0;
                    }
                    if (!(d2 >= cf2.b)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public void a(kh kh2, double d2, double d3, double d4, float f2) {
        new je().a(this, kh2, d2, d3, d4, f2);
    }

    public float a(aj aj2, cf cf2) {
        double d2 = 1.0 / ((cf2.d - cf2.a) * 2.0 + 1.0);
        double d3 = 1.0 / ((cf2.e - cf2.b) * 2.0 + 1.0);
        double d4 = 1.0 / ((cf2.f - cf2.c) * 2.0 + 1.0);
        int n2 = 0;
        int n3 = 0;
        float f2 = 0.0f;
        while (f2 <= 1.0f) {
            float f3 = 0.0f;
            while (f3 <= 1.0f) {
                float f4 = 0.0f;
                while (f4 <= 1.0f) {
                    double d5 = cf2.a + (cf2.d - cf2.a) * (double)f2;
                    double d6 = cf2.b + (cf2.e - cf2.b) * (double)f3;
                    double d7 = cf2.c + (cf2.f - cf2.c) * (double)f4;
                    if (this.a(aj.b(d5, d6, d7), aj2) == null) {
                        ++n2;
                    }
                    ++n3;
                    f4 = (float)((double)f4 + d4);
                }
                f3 = (float)((double)f3 + d3);
            }
            f2 = (float)((double)f2 + d2);
        }
        return (float)n2 / (float)n3;
    }

    public void i(int n2, int n3, int n4, int n5) {
        if (n5 == 0) {
            --n3;
        }
        if (n5 == 1) {
            ++n3;
        }
        if (n5 == 2) {
            --n4;
        }
        if (n5 == 3) {
            ++n4;
        }
        if (n5 == 4) {
            --n2;
        }
        if (n5 == 5) {
            ++n2;
        }
        if (this.a(n2, n3, n4) == ly.as.bc) {
            this.a((float)n2 + 0.5f, (float)n3 + 0.5f, (float)n4 + 0.5f, "random.fizz", 0.5f, 2.6f + (this.n.nextFloat() - this.n.nextFloat()) * 0.8f);
            this.d(n2, n3, n4, 0);
        }
    }

    public kh a(Class clazz) {
        return null;
    }

    public String d() {
        return "All: " + this.a.size();
    }

    public ic b(int n2, int n3, int n4) {
        ga ga2 = this.b(n2 >> 4, n4 >> 4);
        if (ga2 != null) {
            return ga2.d(n2 & 0xF, n3, n4 & 0xF);
        }
        return null;
    }

    public void a(int n2, int n3, int n4, ic ic2) {
        ga ga2 = this.b(n2 >> 4, n4 >> 4);
        if (ga2 != null) {
            ga2.a(n2 & 0xF, n3, n4 & 0xF, ic2);
        }
    }

    public void l(int n2, int n3, int n4) {
        ga ga2 = this.b(n2 >> 4, n4 >> 4);
        if (ga2 != null) {
            ga2.e(n2 & 0xF, n3, n4 & 0xF);
        }
    }

    public boolean g(int n2, int n3, int n4) {
        ly ly2 = ly.n[this.a(n2, n3, n4)];
        if (ly2 == null) {
            return false;
        }
        return ly2.b();
    }

    public void a(nu nu2) {
        this.a(true, nu2);
    }

    public boolean e() {
        int n2 = 1000;
        while (this.z.size() > 0) {
            if (--n2 <= 0) {
                return true;
            }
            ((kn)this.z.remove(this.z.size() - 1)).a(this);
        }
        return false;
    }

    public void a(by by2, int n2, int n3, int n4, int n5, int n6, int n7) {
        this.a(by2, n2, n3, n4, n5, n6, n7, true);
    }

    public void a(by by2, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl2) {
        int n8 = (n5 + n2) / 2;
        int n9 = (n7 + n4) / 2;
        if (!this.d(n8, 64, n9)) {
            return;
        }
        int n10 = this.z.size();
        if (bl2) {
            int n11 = 4;
            if (n11 > n10) {
                n11 = n10;
            }
            for (int i2 = 0; i2 < n11; ++i2) {
                kn kn2 = (kn)this.z.get(this.z.size() - i2 - 1);
                if (kn2.a != by2 || !kn2.a(n2, n3, n4, n5, n6, n7)) continue;
                return;
            }
        }
        this.z.add(new kn(by2, n2, n3, n4, n5, n6, n7));
        if (this.z.size() > 100000) {
            while (this.z.size() > 50000) {
                this.e();
            }
        }
    }

    public void f() {
        int n2 = this.a(1.0f);
        if (n2 != this.e) {
            this.e = n2;
        }
    }

    public void g() {
        this.H.a();
        int n2 = this.a(1.0f);
        if (n2 != this.e) {
            this.e = n2;
            for (int i2 = 0; i2 < this.s.size(); ++i2) {
                ((im)this.s.get(i2)).e();
            }
        }
        ++this.c;
        if (this.c % (long)this.j == 0L) {
            this.a(false, null);
        }
        this.a(false);
        this.h();
    }

    protected void h() {
        int n2;
        int n3;
        int n4;
        int n5;
        this.K.clear();
        for (int i2 = 0; i2 < this.k.size(); ++i2) {
            Object object = (dm)this.k.get(i2);
            n5 = eo.b(((dm)object).ak / 16.0);
            n4 = eo.b(((dm)object).am / 16.0);
            int n6 = 9;
            for (n3 = -n6; n3 <= n6; ++n3) {
                for (n2 = -n6; n2 <= n6; ++n2) {
                    this.K.add(new ol(n3 + n5, n2 + n4));
                }
            }
        }
        if (this.L > 0) {
            --this.L;
        }
        for (Object object : this.K) {
            int n7;
            int n8;
            int n9;
            n5 = ((ol)object).a * 16;
            n4 = ((ol)object).b * 16;
            ga ga2 = this.b(((ol)object).a, ((ol)object).b);
            if (this.L == 0) {
                dm dm2;
                this.f = this.f * 3 + this.g;
                n3 = this.f >> 2;
                n2 = n3 & 0xF;
                n9 = n3 >> 8 & 0xF;
                n8 = n3 >> 16 & 0x7F;
                n7 = ga2.a(n2, n8, n9);
                if (n7 == 0 && this.j(n2 += n5, n8, n9 += n4) <= this.n.nextInt(8) && this.a(by.a, n2, n8, n9) <= 0 && (dm2 = this.a((double)n2 + 0.5, (double)n8 + 0.5, (double)n9 + 0.5, 8.0)) != null && dm2.d((double)n2 + 0.5, (double)n8 + 0.5, (double)n9 + 0.5) > 4.0) {
                    this.a((double)n2 + 0.5, (double)n8 + 0.5, (double)n9 + 0.5, "ambient.cave.cave", 0.7f, 0.8f + this.n.nextFloat() * 0.2f);
                    this.L = this.n.nextInt(12000) + 6000;
                }
            }
            if (this.d && this.n.nextInt(4) == 0) {
                this.f = this.f * 3 + this.g;
                n3 = this.f >> 2;
                n2 = n3 & 0xF;
                n9 = n3 >> 8 & 0xF;
                n8 = this.d(n2 + n5, n9 + n4);
                if (n8 >= 0 && n8 < 128 && ga2.a(by.b, n2, n8, n9) < 10) {
                    n7 = ga2.a(n2, n8 - 1, n9);
                    if (ga2.a(n2, n8, n9) == 0 && ly.aT.a(this, n2 + n5, n8, n9 + n4)) {
                        this.d(n2 + n5, n8, n9 + n4, ly.aT.bc);
                    }
                    if (n7 == ly.C.bc && ga2.b(n2, n8 - 1, n9) == 0) {
                        this.d(n2 + n5, n8 - 1, n9 + n4, ly.aU.bc);
                    }
                }
            }
            for (n3 = 0; n3 < 80; ++n3) {
                this.f = this.f * 3 + this.g;
                n2 = this.f >> 2;
                n9 = n2 & 0xF;
                n8 = n2 >> 8 & 0xF;
                n7 = n2 >> 16 & 0x7F;
                byte by2 = ga2.b[n9 << 11 | n8 << 7 | n7];
                if (!ly.o[by2]) continue;
                ly.n[by2].a(this, n9 + n5, n7, n8 + n4, this.n);
            }
        }
    }

    public boolean a(boolean bl2) {
        int n2 = this.B.size();
        if (n2 != this.C.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        }
        if (n2 > 1000) {
            n2 = 1000;
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3;
            jf jf2 = (jf)this.B.first();
            if (!bl2 && jf2.e > this.c) break;
            this.B.remove(jf2);
            this.C.remove(jf2);
            int n4 = 8;
            if (!this.a(jf2.a - n4, jf2.b - n4, jf2.c - n4, jf2.a + n4, jf2.b + n4, jf2.c + n4) || (n3 = this.a(jf2.a, jf2.b, jf2.c)) != jf2.d || n3 <= 0) continue;
            ly.n[n3].a(this, jf2.a, jf2.b, jf2.c, this.n);
        }
        return this.B.size() != 0;
    }

    public void m(int n2, int n3, int n4) {
        int n5 = 16;
        Random random = new Random();
        for (int i2 = 0; i2 < 1000; ++i2) {
            int n6;
            int n7;
            int n8 = n2 + this.n.nextInt(n5) - this.n.nextInt(n5);
            int n9 = this.a(n8, n7 = n3 + this.n.nextInt(n5) - this.n.nextInt(n5), n6 = n4 + this.n.nextInt(n5) - this.n.nextInt(n5));
            if (n9 <= 0) continue;
            ly.n[n9].b(this, n8, n7, n6, random);
        }
    }

    public List b(kh kh2, cf cf2) {
        this.M.clear();
        int n2 = eo.b((cf2.a - 2.0) / 16.0);
        int n3 = eo.b((cf2.d + 2.0) / 16.0);
        int n4 = eo.b((cf2.c - 2.0) / 16.0);
        int n5 = eo.b((cf2.f + 2.0) / 16.0);
        for (int i2 = n2; i2 <= n3; ++i2) {
            for (int i3 = n4; i3 <= n5; ++i3) {
                if (!this.h(i2, i3)) continue;
                this.b(i2, i3).a(kh2, cf2, this.M);
            }
        }
        return this.M;
    }

    public List a(Class clazz, cf cf2) {
        int n2 = eo.b((cf2.a - 2.0) / 16.0);
        int n3 = eo.b((cf2.d + 2.0) / 16.0);
        int n4 = eo.b((cf2.c - 2.0) / 16.0);
        int n5 = eo.b((cf2.f + 2.0) / 16.0);
        ArrayList arrayList = new ArrayList();
        for (int i2 = n2; i2 <= n3; ++i2) {
            for (int i3 = n4; i3 <= n5; ++i3) {
                if (!this.h(i2, i3)) continue;
                this.b(i2, i3).a(clazz, cf2, arrayList);
            }
        }
        return arrayList;
    }

    public List i() {
        return this.a;
    }

    public void b(int n2, int n3, int n4, ic ic2) {
        if (this.d(n2, n3, n4)) {
            this.a(n2, n4).f();
        }
        for (int i2 = 0; i2 < this.s.size(); ++i2) {
            ((im)this.s.get(i2)).a(n2, n3, n4, ic2);
        }
    }

    public int b(Class clazz) {
        int n2 = 0;
        for (int i2 = 0; i2 < this.a.size(); ++i2) {
            kh kh2 = (kh)this.a.get(i2);
            if (!clazz.isAssignableFrom(kh2.getClass())) continue;
            ++n2;
        }
        return n2;
    }

    public void a(List list) {
        this.a.addAll(list);
        for (int i2 = 0; i2 < list.size(); ++i2) {
            this.b((kh)list.get(i2));
        }
    }

    public void b(List list) {
        this.A.addAll(list);
    }

    public void j() {
        while (this.H.a()) {
        }
    }

    public boolean a(int n2, int n3, int n4, int n5, boolean bl2) {
        int n6 = this.a(n3, n4, n5);
        ly ly2 = ly.n[n6];
        ly ly3 = ly.n[n2];
        cf cf2 = ly3.d(this, n3, n4, n5);
        if (bl2) {
            cf2 = null;
        }
        if (cf2 != null && !this.a(cf2)) {
            return false;
        }
        if (ly2 == ly.B || ly2 == ly.C || ly2 == ly.D || ly2 == ly.E || ly2 == ly.as || ly2 == ly.aT) {
            return true;
        }
        return n2 > 0 && ly2 == null && ly3.a(this, n3, n4, n5);
    }

    public bl a(kh kh2, kh kh3, float f2) {
        int n2 = eo.b(kh2.ak);
        int n3 = eo.b(kh2.al);
        int n4 = eo.b(kh2.am);
        int n5 = (int)(f2 + 16.0f);
        int n6 = n2 - n5;
        int n7 = n3 - n5;
        int n8 = n4 - n5;
        int n9 = n2 + n5;
        int n10 = n3 + n5;
        int n11 = n4 + n5;
        ci ci2 = new ci(this, n6, n7, n8, n9, n10, n11);
        return new cz(ci2).a(kh2, kh3, f2);
    }

    public bl a(kh kh2, int n2, int n3, int n4, float f2) {
        int n5 = eo.b(kh2.ak);
        int n6 = eo.b(kh2.al);
        int n7 = eo.b(kh2.am);
        int n8 = (int)(f2 + 8.0f);
        int n9 = n5 - n8;
        int n10 = n6 - n8;
        int n11 = n7 - n8;
        int n12 = n5 + n8;
        int n13 = n6 + n8;
        int n14 = n7 + n8;
        ci ci2 = new ci(this, n9, n10, n11, n12, n13, n14);
        return new cz(ci2).a(kh2, n2, n3, n4, f2);
    }

    public boolean j(int n2, int n3, int n4, int n5) {
        int n6 = this.a(n2, n3, n4);
        if (n6 == 0) {
            return false;
        }
        return ly.n[n6].c(this, n2, n3, n4, n5);
    }

    public boolean n(int n2, int n3, int n4) {
        if (this.j(n2, n3 - 1, n4, 0)) {
            return true;
        }
        if (this.j(n2, n3 + 1, n4, 1)) {
            return true;
        }
        if (this.j(n2, n3, n4 - 1, 2)) {
            return true;
        }
        if (this.j(n2, n3, n4 + 1, 3)) {
            return true;
        }
        if (this.j(n2 - 1, n3, n4, 4)) {
            return true;
        }
        return this.j(n2 + 1, n3, n4, 5);
    }

    public boolean k(int n2, int n3, int n4, int n5) {
        if (this.g(n2, n3, n4)) {
            return this.n(n2, n3, n4);
        }
        int n6 = this.a(n2, n3, n4);
        if (n6 == 0) {
            return false;
        }
        return ly.n[n6].b((nm)this, n2, n3, n4, n5);
    }

    public boolean o(int n2, int n3, int n4) {
        if (this.k(n2, n3 - 1, n4, 0)) {
            return true;
        }
        if (this.k(n2, n3 + 1, n4, 1)) {
            return true;
        }
        if (this.k(n2, n3, n4 - 1, 2)) {
            return true;
        }
        if (this.k(n2, n3, n4 + 1, 3)) {
            return true;
        }
        if (this.k(n2 - 1, n3, n4, 4)) {
            return true;
        }
        return this.k(n2 + 1, n3, n4, 5);
    }

    public dm a(kh kh2, double d2) {
        return this.a(kh2.ak, kh2.al, kh2.am, d2);
    }

    public dm a(double d2, double d3, double d4, double d5) {
        double d6 = -1.0;
        dm dm2 = null;
        for (int i2 = 0; i2 < this.k.size(); ++i2) {
            dm dm3 = (dm)this.k.get(i2);
            double d7 = dm3.d(d2, d3, d4);
            if (!(d5 < 0.0) && !(d7 < d5 * d5) || d6 != -1.0 && !(d7 < d6)) continue;
            d6 = d7;
            dm2 = dm3;
        }
        return dm2;
    }

    public void a(int n2, int n3, int n4, int n5, int n6, int n7, byte[] byArray) {
        int n8 = n2 >> 4;
        int n9 = n4 >> 4;
        int n10 = n2 + n5 - 1 >> 4;
        int n11 = n4 + n7 - 1 >> 4;
        int n12 = 0;
        int n13 = n3;
        int n14 = n3 + n6;
        if (n13 < 0) {
            n13 = 0;
        }
        if (n14 > 128) {
            n14 = 128;
        }
        for (int i2 = n8; i2 <= n10; ++i2) {
            int n15 = n2 - i2 * 16;
            int n16 = n2 + n5 - i2 * 16;
            if (n15 < 0) {
                n15 = 0;
            }
            if (n16 > 16) {
                n16 = 16;
            }
            for (int i3 = n9; i3 <= n11; ++i3) {
                int n17 = n4 - i3 * 16;
                int n18 = n4 + n7 - i3 * 16;
                if (n17 < 0) {
                    n17 = 0;
                }
                if (n18 > 16) {
                    n18 = 16;
                }
                n12 = this.b(i2, i3).a(byArray, n15, n13, n17, n16, n14, n18, n12);
                this.b(i2 * 16 + n15, n13, i3 * 16 + n17, i2 * 16 + n16, n14, i3 * 16 + n18);
            }
        }
    }

    public void k() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void l() {
        try {
            File file = new File(this.t, "session.lock");
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
            try {
                if (dataInputStream.readLong() != this.G) {
                    throw new lx("The save is being accessed from another location, aborting");
                }
            }
            finally {
                dataInputStream.close();
            }
        }
        catch (IOException iOException) {
            throw new lx("Failed to check session lock, aborting");
        }
    }

    public void a(long l2) {
        this.c = l2;
    }

    public void f(kh kh2) {
        int n2 = eo.b(kh2.ak / 16.0);
        int n3 = eo.b(kh2.am / 16.0);
        int n4 = 2;
        for (int i2 = n2 - n4; i2 <= n2 + n4; ++i2) {
            for (int i3 = n3 - n4; i3 <= n3 + n4; ++i3) {
                this.b(i2, i3);
            }
        }
        if (!this.a.contains(kh2)) {
            System.out.println("REINSERTING PLAYER!");
            this.a.add(kh2);
        }
    }

    static {
        float f2 = 0.05f;
        for (int i2 = 0; i2 <= 15; ++i2) {
            float f3 = 1.0f - (float)i2 / 15.0f;
            cn.i[i2] = (1.0f - f3) / (f3 * 3.0f + 1.0f) * (1.0f - f2) + f2;
        }
    }
}

