/*
 * Decompiled with CFR 0.152.
 */
import java.io.IOException;

public class ck
implements aw {
    private ga[] b = new ga[256];
    private cn c;
    private af d;
    byte[] a = new byte[32768];

    public ck(cn cn2, af af2) {
        this.c = cn2;
        this.d = af2;
    }

    public boolean a(int n2, int n3) {
        int n4 = n2 & 0xF | (n3 & 0xF) * 16;
        return this.b[n4] != null && this.b[n4].a(n2, n3);
    }

    public ga b(int n2, int n3) {
        int n4 = n2 & 0xF | (n3 & 0xF) * 16;
        try {
            if (!this.a(n2, n3)) {
                ga ga2 = this.c(n2, n3);
                if (ga2 == null) {
                    ga2 = new ga(this.c, this.a, n2, n3);
                    ga2.q = true;
                    ga2.p = true;
                }
                this.b[n4] = ga2;
            }
            return this.b[n4];
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private synchronized ga c(int n2, int n3) {
        try {
            return this.d.a(this.c, n2, n3);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return null;
        }
    }

    public void a(aw aw2, int n2, int n3) {
    }

    public boolean a(boolean bl2, nu nu2) {
        return true;
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return false;
    }
}

