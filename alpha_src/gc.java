/*
 * Decompiled with CFR 0.152.
 */
public class gc
extends bh {
    private bh a;
    private int h = 0;
    private String i = "";

    public gc(bh bh2) {
        this.a = bh2;
    }

    public void g() {
        ++this.h;
    }

    public void a() {
        this.e.clear();
        this.e.add(new fk(0, this.c / 2 - 100, this.d / 4 + 96 + 12, "Connect"));
        this.e.add(new fk(1, this.c / 2 - 100, this.d / 4 + 120 + 12, "Cancel"));
        ((fk)this.e.get((int)0)).g = false;
    }

    protected void a(fk fk2) {
        if (!fk2.g) {
            return;
        }
        if (fk2.f == 1) {
            this.b.a(this.a);
        } else if (fk2.f == 0) {
            String[] stringArray = this.i.split(":");
            this.b.a(new mn(this.b, stringArray[0], stringArray.length > 1 ? Integer.parseInt(stringArray[1]) : 25565));
        }
    }

    protected void a(char c2, int n2) {
        if (c2 == '\u0016') {
            int n3;
            String string = bh.c();
            if (string == null) {
                string = "";
            }
            if ((n3 = 32 - this.i.length()) > string.length()) {
                n3 = string.length();
            }
            if (n3 > 0) {
                this.i = this.i + string.substring(0, n3);
            }
        }
        if (c2 == '\r') {
            this.a((fk)this.e.get(0));
        }
        if (n2 == 14 && this.i.length() > 0) {
            this.i = this.i.substring(0, this.i.length() - 1);
        }
        if (" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(c2) >= 0 && this.i.length() < 32) {
            this.i = this.i + c2;
        }
        ((fk)this.e.get((int)0)).g = this.i.length() > 0;
    }

    public void a(int n2, int n3, float f2) {
        this.i();
        this.a(this.g, "Play Multiplayer", this.c / 2, this.d / 4 - 60 + 20, 0xFFFFFF);
        this.b(this.g, "Minecraft Multiplayer is currently not finished, but there", this.c / 2 - 140, this.d / 4 - 60 + 60 + 0, 0xA0A0A0);
        this.b(this.g, "is some buggy early testing going on.", this.c / 2 - 140, this.d / 4 - 60 + 60 + 9, 0xA0A0A0);
        this.b(this.g, "Enter the IP of a server to connect to it:", this.c / 2 - 140, this.d / 4 - 60 + 60 + 36, 0xA0A0A0);
        int n4 = this.c / 2 - 100;
        int n5 = this.d / 4 - 10 + 50 + 18;
        int n6 = 200;
        int n7 = 20;
        this.a(n4 - 1, n5 - 1, n4 + n6 + 1, n5 + n7 + 1, -6250336);
        this.a(n4, n5, n4 + n6, n5 + n7, -16777216);
        this.b(this.g, this.i + (this.h / 6 % 2 == 0 ? "_" : ""), n4 + 4, n5 + (n7 - 8) / 2, 0xE0E0E0);
        super.a(n2, n3, f2);
    }
}

