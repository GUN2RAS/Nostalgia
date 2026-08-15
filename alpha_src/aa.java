/*
 * Decompiled with CFR 0.152.
 */
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;

public class aa
extends z {
    protected float[] g = new float[320];
    protected float[] h = new float[320];
    private Minecraft i;
    private int[] j = new int[256];
    private double k;
    private double l;

    public aa(Minecraft minecraft) {
        super(di.aO.a((ev)null));
        this.i = minecraft;
        this.f = 1;
        try {
            BufferedImage bufferedImage = ImageIO.read(Minecraft.class.getResource("/gui/items.png"));
            int n2 = this.b % 16 * 16;
            int n3 = this.b / 16 * 16;
            bufferedImage.getRGB(n2, n3, 16, 16, this.j, 0, 16);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void a() {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12;
        double d2;
        for (int i2 = 0; i2 < 256; ++i2) {
            int n13 = this.j[i2] >> 24 & 0xFF;
            int n14 = this.j[i2] >> 16 & 0xFF;
            int n15 = this.j[i2] >> 8 & 0xFF;
            int n16 = this.j[i2] >> 0 & 0xFF;
            if (this.c) {
                int n17 = (n14 * 30 + n15 * 59 + n16 * 11) / 100;
                int n18 = (n14 * 30 + n15 * 70) / 100;
                int n19 = (n14 * 30 + n16 * 70) / 100;
                n14 = n17;
                n15 = n18;
                n16 = n19;
            }
            this.a[i2 * 4 + 0] = (byte)n14;
            this.a[i2 * 4 + 1] = (byte)n15;
            this.a[i2 * 4 + 2] = (byte)n16;
            this.a[i2 * 4 + 3] = (byte)n13;
        }
        double d3 = 0.0;
        if (this.i.e != null && this.i.g != null) {
            double d4 = (double)this.i.e.o - this.i.g.ak;
            double d5 = (double)this.i.e.q - this.i.g.am;
            d3 = (double)(this.i.g.aq - 90.0f) * Math.PI / 180.0 - Math.atan2(d5, d4);
        }
        for (d2 = d3 - this.k; d2 < -Math.PI; d2 += Math.PI * 2) {
        }
        while (d2 >= Math.PI) {
            d2 -= Math.PI * 2;
        }
        if (d2 < -1.0) {
            d2 = -1.0;
        }
        if (d2 > 1.0) {
            d2 = 1.0;
        }
        this.l += d2 * 0.1;
        this.l *= 0.8;
        this.k += this.l;
        double d6 = Math.sin(this.k);
        double d7 = Math.cos(this.k);
        for (n12 = -4; n12 <= 4; ++n12) {
            n11 = (int)(8.5 + d7 * (double)n12 * 0.3);
            n10 = (int)(7.5 - d6 * (double)n12 * 0.3 * 0.5);
            n9 = n10 * 16 + n11;
            n8 = 100;
            n7 = 100;
            n6 = 100;
            n5 = 255;
            if (this.c) {
                n4 = (n8 * 30 + n7 * 59 + n6 * 11) / 100;
                n3 = (n8 * 30 + n7 * 70) / 100;
                n2 = (n8 * 30 + n6 * 70) / 100;
                n8 = n4;
                n7 = n3;
                n6 = n2;
            }
            this.a[n9 * 4 + 0] = (byte)n8;
            this.a[n9 * 4 + 1] = (byte)n7;
            this.a[n9 * 4 + 2] = (byte)n6;
            this.a[n9 * 4 + 3] = (byte)n5;
        }
        for (n12 = -8; n12 <= 16; ++n12) {
            n11 = (int)(8.5 + d6 * (double)n12 * 0.3);
            n10 = (int)(7.5 + d7 * (double)n12 * 0.3 * 0.5);
            n9 = n10 * 16 + n11;
            n8 = n12 >= 0 ? 255 : 100;
            n7 = n12 >= 0 ? 20 : 100;
            n6 = n12 >= 0 ? 20 : 100;
            n5 = 255;
            if (this.c) {
                n4 = (n8 * 30 + n7 * 59 + n6 * 11) / 100;
                n3 = (n8 * 30 + n7 * 70) / 100;
                n2 = (n8 * 30 + n6 * 70) / 100;
                n8 = n4;
                n7 = n3;
                n6 = n2;
            }
            this.a[n9 * 4 + 0] = (byte)n8;
            this.a[n9 * 4 + 1] = (byte)n7;
            this.a[n9 * 4 + 2] = (byte)n6;
            this.a[n9 * 4 + 3] = (byte)n5;
        }
    }
}

