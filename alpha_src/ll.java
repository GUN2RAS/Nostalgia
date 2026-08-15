/*
 * Decompiled with CFR 0.152.
 */
public class ll {
    public ed[] a;
    public int b = 0;
    private boolean c = false;

    public ll(ed[] edArray) {
        this.a = edArray;
        this.b = edArray.length;
    }

    public ll(ed[] edArray, int n2, int n3, int n4, int n5) {
        this(edArray);
        float f2 = 0.0015625f;
        float f3 = 0.003125f;
        edArray[0] = edArray[0].a((float)n4 / 64.0f - f2, (float)n3 / 32.0f + f3);
        edArray[1] = edArray[1].a((float)n2 / 64.0f + f2, (float)n3 / 32.0f + f3);
        edArray[2] = edArray[2].a((float)n2 / 64.0f + f2, (float)n5 / 32.0f - f3);
        edArray[3] = edArray[3].a((float)n4 / 64.0f - f2, (float)n5 / 32.0f - f3);
    }

    public void a() {
        ed[] edArray = new ed[this.a.length];
        for (int i2 = 0; i2 < this.a.length; ++i2) {
            edArray[i2] = this.a[this.a.length - i2 - 1];
        }
        this.a = edArray;
    }

    public void a(ho ho2, float f2) {
        aj aj2 = this.a[1].a.a(this.a[0].a);
        aj aj3 = this.a[1].a.a(this.a[2].a);
        aj aj4 = aj3.b(aj2).b();
        ho2.b();
        if (this.c) {
            ho2.b(-((float)aj4.a), -((float)aj4.b), -((float)aj4.c));
        } else {
            ho2.b((float)aj4.a, (float)aj4.b, (float)aj4.c);
        }
        for (int i2 = 0; i2 < 4; ++i2) {
            ed ed2 = this.a[i2];
            ho2.a((float)ed2.a.a * f2, (float)ed2.a.b * f2, (float)ed2.a.c * f2, ed2.b, ed2.c);
        }
        ho2.a();
    }
}

