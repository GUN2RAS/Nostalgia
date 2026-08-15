/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
import org.lwjgl.input.Keyboard;

public class de
extends bh {
    private String a = "";
    private int h = 0;

    public void a() {
        Keyboard.enableRepeatEvents((boolean)true);
    }

    public void h() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    public void g() {
        ++this.h;
    }

    protected void a(char c2, int n2) {
        if (n2 == 1) {
            this.b.a((bh)null);
            return;
        }
        if (n2 == 28) {
            String string = this.a.trim();
            if (string.length() > 0) {
                this.b.g.a(this.a.trim());
            }
            this.b.a((bh)null);
            return;
        }
        if (n2 == 14 && this.a.length() > 0) {
            this.a = this.a.substring(0, this.a.length() - 1);
        }
        if (" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(c2) >= 0 && this.a.length() < 100) {
            this.a = this.a + c2;
        }
    }

    public void a(int n2, int n3, float f2) {
        this.a(2, this.d - 14, this.c - 2, this.d - 2, Integer.MIN_VALUE);
        this.b(this.g, "> " + this.a + (this.h / 6 % 2 == 0 ? "_" : ""), 4, this.d - 12, 0xE0E0E0);
    }

    protected void a(int n2, int n3, int n4) {
        if (n4 == 0 && this.b.u.a != null) {
            if (this.a.length() > 0 && !this.a.endsWith(" ")) {
                this.a = this.a + " ";
            }
            this.a = this.a + this.b.u.a;
            int n5 = 100;
            if (this.a.length() > n5) {
                this.a = this.a.substring(0, n5);
            }
        }
    }
}

