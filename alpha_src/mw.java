/*
 * Decompiled with CFR 0.152.
 */
public class mw
implements gh {
    private ev[] a = new ev[1];

    public int c() {
        return 1;
    }

    public ev c(int n2) {
        return this.a[n2];
    }

    public String d() {
        return "Result";
    }

    public ev a(int n2, int n3) {
        if (this.a[n2] != null) {
            ev ev2 = this.a[n2];
            this.a[n2] = null;
            return ev2;
        }
        return null;
    }

    public void a(int n2, ev ev2) {
        this.a[n2] = ev2;
    }

    public int e() {
        return 64;
    }

    public void j_() {
    }
}

