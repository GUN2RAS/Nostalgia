/*
 * Decompiled with CFR 0.152.
 */
import net.minecraft.client.Minecraft;

public class fl
extends Thread {
    final /* synthetic */ Minecraft a;

    public fl(Minecraft minecraft, String string) {
        this.a = minecraft;
        super(string);
        this.setDaemon(true);
        this.start();
    }

    public void run() {
        while (this.a.F) {
            try {
                Thread.sleep(Integer.MAX_VALUE);
            }
            catch (InterruptedException interruptedException) {}
        }
    }
}

