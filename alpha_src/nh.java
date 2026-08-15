/*
 * Decompiled with CFR 0.152.
 */
class nh
extends Thread {
    final /* synthetic */ ii a;

    nh(ii ii2) {
        this.a = ii2;
    }

    public void run() {
        try {
            Thread.sleep(5000L);
            if (ii.e(this.a).isAlive()) {
                try {
                    ii.e(this.a).stop();
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            if (ii.f(this.a).isAlive()) {
                try {
                    ii.f(this.a).stop();
                }
                catch (Throwable throwable) {}
            }
        }
        catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }
}

