/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

public class ey {
    public static boolean a = false;
    private HashMap b = new HashMap();
    private HashMap c = new HashMap();
    private IntBuffer d = df.c(1);
    private ByteBuffer e = df.b(0x100000);
    private List f = new ArrayList();
    private Map g = new HashMap();
    private fr h;
    private boolean i = false;

    public ey(fr fr2) {
        this.h = fr2;
    }

    public int a(String string) {
        Integer n2 = (Integer)this.b.get(string);
        if (n2 != null) {
            return n2;
        }
        try {
            this.d.clear();
            df.a(this.d);
            int n3 = this.d.get(0);
            if (string.startsWith("##")) {
                this.a(this.b(ImageIO.read(ey.class.getResourceAsStream(string.substring(2)))), n3);
            } else if (string.startsWith("%%")) {
                this.i = true;
                this.a(ImageIO.read(ey.class.getResourceAsStream(string.substring(2))), n3);
                this.i = false;
            } else {
                this.a(ImageIO.read(ey.class.getResourceAsStream(string)), n3);
            }
            this.b.put(string, n3);
            return n3;
        }
        catch (IOException iOException) {
            throw new RuntimeException("!!");
        }
    }

    private BufferedImage b(BufferedImage bufferedImage) {
        int n2 = bufferedImage.getWidth() / 16;
        BufferedImage bufferedImage2 = new BufferedImage(16, bufferedImage.getHeight() * n2, 2);
        Graphics graphics = bufferedImage2.getGraphics();
        for (int i2 = 0; i2 < n2; ++i2) {
            graphics.drawImage(bufferedImage, -i2 * 16, i2 * bufferedImage.getHeight(), null);
        }
        graphics.dispose();
        return bufferedImage2;
    }

    public int a(BufferedImage bufferedImage) {
        this.d.clear();
        df.a(this.d);
        int n2 = this.d.get(0);
        this.a(bufferedImage, n2);
        this.c.put(n2, bufferedImage);
        return n2;
    }

    public void a(BufferedImage bufferedImage, int n2) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        GL11.glBindTexture((int)3553, (int)n2);
        if (a) {
            GL11.glTexParameteri((int)3553, (int)10241, (int)9987);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        } else {
            GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        }
        if (this.i) {
            GL11.glTexParameteri((int)3553, (int)10242, (int)10496);
            GL11.glTexParameteri((int)3553, (int)10243, (int)10496);
        } else {
            GL11.glTexParameteri((int)3553, (int)10242, (int)10497);
            GL11.glTexParameteri((int)3553, (int)10243, (int)10497);
        }
        int n11 = bufferedImage.getWidth();
        int n12 = bufferedImage.getHeight();
        int[] nArray = new int[n11 * n12];
        byte[] byArray = new byte[n11 * n12 * 4];
        bufferedImage.getRGB(0, 0, n11, n12, nArray, 0, n11);
        for (n10 = 0; n10 < nArray.length; ++n10) {
            n9 = nArray[n10] >> 24 & 0xFF;
            n8 = nArray[n10] >> 16 & 0xFF;
            n7 = nArray[n10] >> 8 & 0xFF;
            n6 = nArray[n10] & 0xFF;
            if (this.h != null && this.h.g) {
                n5 = (n8 * 30 + n7 * 59 + n6 * 11) / 100;
                n4 = (n8 * 30 + n7 * 70) / 100;
                n3 = (n8 * 30 + n6 * 70) / 100;
                n8 = n5;
                n7 = n4;
                n6 = n3;
            }
            byArray[n10 * 4 + 0] = (byte)n8;
            byArray[n10 * 4 + 1] = (byte)n7;
            byArray[n10 * 4 + 2] = (byte)n6;
            byArray[n10 * 4 + 3] = (byte)n9;
        }
        this.e.clear();
        this.e.put(byArray);
        this.e.position(0).limit(byArray.length);
        GL11.glTexImage2D((int)3553, (int)0, (int)6408, (int)n11, (int)n12, (int)0, (int)6408, (int)5121, (ByteBuffer)this.e);
        if (a) {
            for (n10 = 1; n10 <= 4; ++n10) {
                n9 = n11 >> n10 - 1;
                n8 = n11 >> n10;
                n7 = n12 >> n10;
                for (n6 = 0; n6 < n8; ++n6) {
                    for (n5 = 0; n5 < n7; ++n5) {
                        n4 = this.e.getInt((n6 * 2 + 0 + (n5 * 2 + 0) * n9) * 4);
                        n3 = this.e.getInt((n6 * 2 + 1 + (n5 * 2 + 0) * n9) * 4);
                        int n13 = this.e.getInt((n6 * 2 + 1 + (n5 * 2 + 1) * n9) * 4);
                        int n14 = this.e.getInt((n6 * 2 + 0 + (n5 * 2 + 1) * n9) * 4);
                        int n15 = this.b(this.b(n4, n3), this.b(n13, n14));
                        this.e.putInt((n6 + n5 * n8) * 4, n15);
                    }
                }
                GL11.glTexImage2D((int)3553, (int)n10, (int)6408, (int)n8, (int)n7, (int)0, (int)6408, (int)5121, (ByteBuffer)this.e);
            }
        }
    }

    public void a(int n2) {
        this.c.remove(n2);
        this.d.clear();
        this.d.put(n2);
        this.d.flip();
        GL11.glDeleteTextures((IntBuffer)this.d);
    }

    public int a(String string, String string2) {
        cc cc2 = (cc)this.g.get(string);
        if (cc2 != null && cc2.a != null && !cc2.d) {
            if (cc2.c < 0) {
                cc2.c = this.a(cc2.a);
            } else {
                this.a(cc2.a, cc2.c);
            }
            cc2.d = true;
        }
        if (cc2 == null || cc2.c < 0) {
            return this.a(string2);
        }
        return cc2.c;
    }

    public cc a(String string, hb hb2) {
        cc cc2 = (cc)this.g.get(string);
        if (cc2 == null) {
            this.g.put(string, new cc(string, hb2));
        } else {
            ++cc2.b;
        }
        return cc2;
    }

    public void b(String string) {
        cc cc2 = (cc)this.g.get(string);
        if (cc2 != null) {
            --cc2.b;
            if (cc2.b == 0) {
                if (cc2.c >= 0) {
                    this.a(cc2.c);
                }
                this.g.remove(string);
            }
        }
    }

    public void a(z z2) {
        this.f.add(z2);
        z2.a();
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
        z z2;
        int n12;
        for (n12 = 0; n12 < this.f.size(); ++n12) {
            z2 = (z)this.f.get(n12);
            z2.c = this.h.g;
            z2.a();
            this.e.clear();
            this.e.put(z2.a);
            this.e.position(0).limit(z2.a.length);
            z2.a(this);
            for (n11 = 0; n11 < z2.e; ++n11) {
                for (n10 = 0; n10 < z2.e; ++n10) {
                    GL11.glTexSubImage2D((int)3553, (int)0, (int)(z2.b % 16 * 16 + n11 * 16), (int)(z2.b / 16 * 16 + n10 * 16), (int)16, (int)16, (int)6408, (int)5121, (ByteBuffer)this.e);
                    if (!a) continue;
                    for (n9 = 1; n9 <= 4; ++n9) {
                        n8 = 16 >> n9 - 1;
                        n7 = 16 >> n9;
                        for (n6 = 0; n6 < n7; ++n6) {
                            for (n5 = 0; n5 < n7; ++n5) {
                                n4 = this.e.getInt((n6 * 2 + 0 + (n5 * 2 + 0) * n8) * 4);
                                n3 = this.e.getInt((n6 * 2 + 1 + (n5 * 2 + 0) * n8) * 4);
                                n2 = this.e.getInt((n6 * 2 + 1 + (n5 * 2 + 1) * n8) * 4);
                                int n13 = this.e.getInt((n6 * 2 + 0 + (n5 * 2 + 1) * n8) * 4);
                                int n14 = this.a(this.a(n4, n3), this.a(n2, n13));
                                this.e.putInt((n6 + n5 * n7) * 4, n14);
                            }
                        }
                        GL11.glTexSubImage2D((int)3553, (int)n9, (int)(z2.b % 16 * n7), (int)(z2.b / 16 * n7), (int)n7, (int)n7, (int)6408, (int)5121, (ByteBuffer)this.e);
                    }
                }
            }
        }
        for (n12 = 0; n12 < this.f.size(); ++n12) {
            z2 = (z)this.f.get(n12);
            if (z2.d <= 0) continue;
            this.e.clear();
            this.e.put(z2.a);
            this.e.position(0).limit(z2.a.length);
            GL11.glBindTexture((int)3553, (int)z2.d);
            GL11.glTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)16, (int)16, (int)6408, (int)5121, (ByteBuffer)this.e);
            if (!a) continue;
            for (n11 = 1; n11 <= 4; ++n11) {
                n10 = 16 >> n11 - 1;
                n9 = 16 >> n11;
                for (n8 = 0; n8 < n9; ++n8) {
                    for (n7 = 0; n7 < n9; ++n7) {
                        n6 = this.e.getInt((n8 * 2 + 0 + (n7 * 2 + 0) * n10) * 4);
                        n5 = this.e.getInt((n8 * 2 + 1 + (n7 * 2 + 0) * n10) * 4);
                        n4 = this.e.getInt((n8 * 2 + 1 + (n7 * 2 + 1) * n10) * 4);
                        n3 = this.e.getInt((n8 * 2 + 0 + (n7 * 2 + 1) * n10) * 4);
                        n2 = this.a(this.a(n6, n5), this.a(n4, n3));
                        this.e.putInt((n8 + n7 * n9) * 4, n2);
                    }
                }
                GL11.glTexSubImage2D((int)3553, (int)n11, (int)0, (int)0, (int)n9, (int)n9, (int)6408, (int)5121, (ByteBuffer)this.e);
            }
        }
    }

    private int a(int n2, int n3) {
        int n4 = (n2 & 0xFF000000) >> 24 & 0xFF;
        int n5 = (n3 & 0xFF000000) >> 24 & 0xFF;
        return (n4 + n5 >> 1 << 24) + ((n2 & 0xFEFEFE) + (n3 & 0xFEFEFE) >> 1);
    }

    private int b(int n2, int n3) {
        int n4 = (n2 & 0xFF000000) >> 24 & 0xFF;
        int n5 = (n3 & 0xFF000000) >> 24 & 0xFF;
        int n6 = 255;
        if (n4 + n5 == 0) {
            n4 = 1;
            n5 = 1;
            n6 = 0;
        }
        int n7 = (n2 >> 16 & 0xFF) * n4;
        int n8 = (n2 >> 8 & 0xFF) * n4;
        int n9 = (n2 & 0xFF) * n4;
        int n10 = (n3 >> 16 & 0xFF) * n5;
        int n11 = (n3 >> 8 & 0xFF) * n5;
        int n12 = (n3 & 0xFF) * n5;
        int n13 = (n7 + n10) / (n4 + n5);
        int n14 = (n8 + n11) / (n4 + n5);
        int n15 = (n9 + n12) / (n4 + n5);
        return n6 << 24 | n13 << 16 | n14 << 8 | n15;
    }

    public void b() {
        BufferedImage bufferedImage;
        Iterator<Object> iterator = this.c.keySet().iterator();
        while (iterator.hasNext()) {
            int n2 = (Integer)iterator.next();
            bufferedImage = (BufferedImage)this.c.get(n2);
            this.a(bufferedImage, n2);
        }
        for (cc cc2 : this.g.values()) {
            cc2.d = false;
        }
        for (String string : this.b.keySet()) {
            try {
                if (string.startsWith("##")) {
                    bufferedImage = this.b(ImageIO.read(ey.class.getResourceAsStream(string.substring(2))));
                } else if (string.startsWith("%%")) {
                    this.i = true;
                    bufferedImage = ImageIO.read(ey.class.getResourceAsStream(string.substring(2)));
                    this.i = false;
                } else {
                    bufferedImage = ImageIO.read(ey.class.getResourceAsStream(string));
                }
                int n3 = (Integer)this.b.get(string);
                this.a(bufferedImage, n3);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }

    public void b(int n2) {
        if (n2 < 0) {
            return;
        }
        GL11.glBindTexture((int)3553, (int)n2);
    }
}

