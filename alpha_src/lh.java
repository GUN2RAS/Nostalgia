/*
 * Decompiled with CFR 0.152.
 */
class lh
extends Thread {
    final /* synthetic */ ad a;

    lh(ad ad2) {
        this.a = ad2;
    }

    public void run() {
        while (ad.a(this.a)) {
            this.a.d();
            try {
                Thread.sleep(1L);
            }
            catch (Exception exception) {}
        }
    }
}

