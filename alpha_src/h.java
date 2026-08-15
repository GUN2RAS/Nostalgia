/*
 * Decompiled with CFR 0.152.
 */
import java.awt.Canvas;
import net.minecraft.client.MinecraftApplet;

public class h
extends Canvas {
    final /* synthetic */ MinecraftApplet a;

    public h(MinecraftApplet minecraftApplet) {
        this.a = minecraftApplet;
    }

    public synchronized void addNotify() {
        super.addNotify();
        this.a.a();
    }

    public synchronized void removeNotify() {
        this.a.b();
        super.removeNotify();
    }
}

