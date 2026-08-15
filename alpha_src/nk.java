/*
 * Decompiled with CFR 0.152.
 */
class nk
extends Thread {
    final /* synthetic */ ii a;

    nk(ii ii2, String string) {
        this.a = ii2;
        super(string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void run() {
        Object object = ii.a;
        synchronized (object) {
            ++ii.c;
        }
        try {
            while (ii.a(this.a)) {
                ii.d(this.a);
            }
        }
        finally {
            object = ii.a;
            synchronized (object) {
                --ii.c;
            }
        }
    }
}

