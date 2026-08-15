/*
 * Decompiled with CFR 0.152.
 */
class ni
extends Thread {
    final /* synthetic */ ii a;

    ni(ii ii2, String string) {
        this.a = ii2;
        super(string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void run() {
        Object object = ii.a;
        synchronized (object) {
            ++ii.b;
        }
        try {
            while (ii.a(this.a) && !ii.b(this.a)) {
                ii.c(this.a);
            }
        }
        finally {
            object = ii.a;
            synchronized (object) {
                --ii.b;
            }
        }
    }
}

