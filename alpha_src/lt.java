/*
 * Decompiled with CFR 0.152.
 */
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class lt {
    private float[] a = new float[768];
    private int[] b = new int[5120];
    private int[] c = new int[5120];
    private int[] d = new int[5120];
    private int[] e = new int[5120];
    private int[] f = new int[34];
    private int[] g = new int[768];

    public lt() {
        try {
            BufferedImage bufferedImage = ImageIO.read(lt.class.getResource("/terrain.png"));
            int[] nArray = new int[65536];
            bufferedImage.getRGB(0, 0, 256, 256, nArray, 0, 256);
            for (int i2 = 0; i2 < 256; ++i2) {
                int n2 = 0;
                int n3 = 0;
                int n4 = 0;
                int n5 = i2 % 16 * 16;
                int n6 = i2 / 16 * 16;
                int n7 = 0;
                for (int i3 = 0; i3 < 16; ++i3) {
                    for (int i4 = 0; i4 < 16; ++i4) {
                        int n8 = nArray[i4 + n5 + (i3 + n6) * 256];
                        int n9 = n8 >> 24 & 0xFF;
                        if (n9 <= 128) continue;
                        n2 += n8 >> 16 & 0xFF;
                        n3 += n8 >> 8 & 0xFF;
                        n4 += n8 & 0xFF;
                        ++n7;
                    }
                    if (n7 == 0) {
                        ++n7;
                    }
                    this.a[i2 * 3 + 0] = n2 / n7;
                    this.a[i2 * 3 + 1] = n3 / n7;
                    this.a[i2 * 3 + 2] = n4 / n7;
                }
            }
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        for (int i5 = 0; i5 < 256; ++i5) {
            if (ly.n[i5] == null) continue;
            this.g[i5 * 3 + 0] = ly.n[i5].a(1);
            this.g[i5 * 3 + 1] = ly.n[i5].a(2);
            this.g[i5 * 3 + 2] = ly.n[i5].a(3);
        }
    }

    public void a(jj jj2) {
        cn cn2 = jj2.b;
        if (cn2 == null) {
            jj2.f = true;
            jj2.e = true;
            return;
        }
        int n2 = jj2.c * 16;
        int n3 = jj2.d * 16;
        int n4 = n2 + 16;
        int n5 = n3 + 16;
        ga ga2 = cn2.b(jj2.c, jj2.d);
        if (ga2.q) {
            jj2.f = true;
            jj2.e = true;
            return;
        }
        jj2.f = false;
        Arrays.fill(this.c, 0);
        Arrays.fill(this.d, 0);
        Arrays.fill(this.f, 160);
        for (int i2 = n5 - 1; i2 >= n3; --i2) {
            for (int i3 = n4 - 1; i3 >= n2; --i3) {
                int n6 = i3 - n2;
                int n7 = i2 - n3;
                int n8 = n6 + n7;
                boolean bl2 = true;
                for (int i4 = 0; i4 < 128; ++i4) {
                    float f2;
                    float f3;
                    int n9;
                    int n10 = n7 - n6 - i4 + 160 - 16;
                    if (n10 >= this.f[n8] && n10 >= this.f[n8 + 1]) continue;
                    ly ly2 = ly.n[cn2.a(i3, i4, i2)];
                    if (ly2 == null) {
                        bl2 = false;
                        continue;
                    }
                    if (ly2.bn == gb.f) {
                        int n11 = cn2.a(i3, i4 + 1, i2);
                        if (n11 != 0 && ly.n[n11].bn == gb.f) continue;
                        float f4 = (float)i4 / 127.0f * 0.6f + 0.4f;
                        float f5 = cn2.c(i3, i4 + 1, i2) * f4;
                        if (n10 < 0 || n10 >= 160) continue;
                        int n12 = n8 + n10 * 32;
                        if (n8 >= 0 && n8 <= 32 && this.d[n12] <= i4) {
                            this.d[n12] = i4;
                            this.e[n12] = (int)(f5 * 127.0f);
                        }
                        if (n8 >= -1 && n8 <= 31 && this.d[n12 + 1] <= i4) {
                            this.d[n12 + 1] = i4;
                            this.e[n12 + 1] = (int)(f5 * 127.0f);
                        }
                        bl2 = false;
                        continue;
                    }
                    if (bl2) {
                        if (n10 < this.f[n8]) {
                            this.f[n8] = n10;
                        }
                        if (n10 < this.f[n8 + 1]) {
                            this.f[n8 + 1] = n10;
                        }
                    }
                    float f6 = (float)i4 / 127.0f * 0.6f + 0.4f;
                    if (n10 >= 0 && n10 < 160) {
                        int n13 = n8 + n10 * 32;
                        int n14 = this.g[ly2.bc * 3 + 0];
                        float f7 = (cn2.c(i3, i4 + 1, i2) * 0.8f + 0.2f) * f6;
                        n9 = n14;
                        if (n8 >= 0) {
                            f3 = f7;
                            if (this.c[n13] <= i4) {
                                this.c[n13] = i4;
                                this.b[n13] = 0xFF000000 | (int)(this.a[n9 * 3 + 0] * f3) << 16 | (int)(this.a[n9 * 3 + 1] * f3) << 8 | (int)(this.a[n9 * 3 + 2] * f3);
                            }
                        }
                        if (n8 < 31) {
                            f3 = f7 * 0.9f;
                            if (this.c[n13 + 1] <= i4) {
                                this.c[n13 + 1] = i4;
                                this.b[n13 + 1] = 0xFF000000 | (int)(this.a[n9 * 3 + 0] * f3) << 16 | (int)(this.a[n9 * 3 + 1] * f3) << 8 | (int)(this.a[n9 * 3 + 2] * f3);
                            }
                        }
                    }
                    if (n10 < -1 || n10 >= 159) continue;
                    int n15 = n8 + (n10 + 1) * 32;
                    int n16 = this.g[ly2.bc * 3 + 1];
                    float f8 = cn2.c(i3 - 1, i4, i2) * 0.8f + 0.2f;
                    n9 = this.g[ly2.bc * 3 + 2];
                    f3 = cn2.c(i3, i4, i2 + 1) * 0.8f + 0.2f;
                    if (n8 >= 0) {
                        f2 = f8 * f6 * 0.6f;
                        if (this.c[n15] <= i4 - 1) {
                            this.c[n15] = i4 - 1;
                            this.b[n15] = 0xFF000000 | (int)(this.a[n16 * 3 + 0] * f2) << 16 | (int)(this.a[n16 * 3 + 1] * f2) << 8 | (int)(this.a[n16 * 3 + 2] * f2);
                        }
                    }
                    if (n8 >= 31) continue;
                    f2 = f3 * 0.9f * f6 * 0.4f;
                    if (this.c[n15 + 1] > i4 - 1) continue;
                    this.c[n15 + 1] = i4 - 1;
                    this.b[n15 + 1] = 0xFF000000 | (int)(this.a[n9 * 3 + 0] * f2) << 16 | (int)(this.a[n9 * 3 + 1] * f2) << 8 | (int)(this.a[n9 * 3 + 2] * f2);
                }
            }
        }
        this.a();
        if (jj2.a == null) {
            jj2.a = new BufferedImage(32, 160, 2);
        }
        jj2.a.setRGB(0, 0, 32, 160, this.b, 0, 32);
        jj2.e = true;
    }

    private void a() {
        for (int i2 = 0; i2 < 32; ++i2) {
            for (int i3 = 0; i3 < 160; ++i3) {
                int n2 = i2 + i3 * 32;
                if (this.c[n2] == 0) {
                    this.b[n2] = 0;
                }
                if (this.d[n2] <= this.c[n2]) continue;
                int n3 = this.b[n2] >> 24 & 0xFF;
                this.b[n2] = ((this.b[n2] & 0xFEFEFE) >> 1) + this.e[n2];
                if (n3 < 128) {
                    this.b[n2] = Integer.MIN_VALUE + this.e[n2] * 2;
                    continue;
                }
                int n4 = n2;
                this.b[n4] = this.b[n4] | 0xFF000000;
            }
        }
    }
}

