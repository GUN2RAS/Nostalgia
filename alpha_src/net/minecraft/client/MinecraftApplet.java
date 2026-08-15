/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import net.minecraft.client.Minecraft;

public class MinecraftApplet
extends Applet {
    private Canvas a;
    private Minecraft b;
    private Thread c = null;

    public void init() {
        this.a = new h(this);
        boolean bl2 = false;
        if (this.getParameter("fullscreen") != null) {
            bl2 = this.getParameter("fullscreen").equalsIgnoreCase("true");
        }
        this.b = new g(this, this, this.a, this, this.getWidth(), this.getHeight(), bl2);
        this.b.j = this.getDocumentBase().getHost();
        if (this.getDocumentBase().getPort() > 0) {
            this.b.j = this.b.j + ":" + this.getDocumentBase().getPort();
        }
        if (this.getParameter("username") != null && this.getParameter("sessionid") != null) {
            this.b.i = new dl(this.getParameter("username"), this.getParameter("sessionid"));
            System.out.println("Setting user: " + this.b.i.b + ", " + this.b.i.c);
            if (this.getParameter("mppass") != null) {
                this.b.i.d = this.getParameter("mppass");
            }
        } else {
            this.b.i = new dl("Player", "");
        }
        if (this.getParameter("loadmap_user") != null && this.getParameter("loadmap_id") != null) {
            this.b.s = this.getParameter("loadmap_user");
            this.b.t = Integer.parseInt(this.getParameter("loadmap_id"));
        } else if (this.getParameter("server") != null && this.getParameter("port") != null) {
            this.b.a(this.getParameter("server"), Integer.parseInt(this.getParameter("port")));
        }
        this.b.l = true;
        this.setLayout(new BorderLayout());
        this.add((Component)this.a, "Center");
        this.a.setFocusable(true);
        this.validate();
    }

    public void a() {
        if (this.c != null) {
            return;
        }
        this.c = new Thread((Runnable)this.b, "Minecraft main thread");
        this.c.start();
    }

    public void start() {
        if (this.b != null) {
            this.b.m = false;
        }
    }

    public void stop() {
        if (this.b != null) {
            this.b.m = true;
        }
    }

    public void destroy() {
        this.b();
    }

    public void b() {
        if (this.c == null) {
            return;
        }
        this.b.d();
        try {
            this.c.join(10000L);
        }
        catch (InterruptedException interruptedException) {
            try {
                this.b.c();
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        this.c = null;
    }

    public void c() {
        this.a = null;
        this.b = null;
        this.c = null;
        try {
            this.removeAll();
            this.validate();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

