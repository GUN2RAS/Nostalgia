/*
 * Decompiled with CFR 0.152.
 */
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import net.minecraft.client.Minecraft;

public final class fp
extends WindowAdapter {
    final /* synthetic */ Minecraft a;
    final /* synthetic */ Thread b;

    public fp(Minecraft minecraft, Thread thread) {
        this.a = minecraft;
        this.b = thread;
    }

    public void windowClosing(WindowEvent windowEvent) {
        this.a.d();
        try {
            this.b.join();
        }
        catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        System.exit(0);
    }
}

