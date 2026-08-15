/*
 * Decompiled with CFR 0.152.
 */
public class cb
extends cr {
    public void a(float f2, float f3, float f4, float f5, float f6, float f7) {
        super.a(f2, f3, f4, f5, f6, f7);
        float f8 = eo.a(this.k * (float)Math.PI);
        float f9 = eo.a((1.0f - (1.0f - this.k) * (1.0f - this.k)) * (float)Math.PI);
        this.d.f = 0.0f;
        this.e.f = 0.0f;
        this.d.e = -(0.1f - f8 * 0.6f);
        this.e.e = 0.1f - f8 * 0.6f;
        this.d.d = -1.5707964f;
        this.e.d = -1.5707964f;
        this.d.d -= f8 * 1.2f - f9 * 0.4f;
        this.e.d -= f8 * 1.2f - f9 * 0.4f;
        this.d.f += eo.b(f4 * 0.09f) * 0.05f + 0.05f;
        this.e.f -= eo.b(f4 * 0.09f) * 0.05f + 0.05f;
        this.d.d += eo.a(f4 * 0.067f) * 0.05f;
        this.e.d -= eo.a(f4 * 0.067f) * 0.05f;
    }
}

