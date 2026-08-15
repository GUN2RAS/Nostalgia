/*
 * Decompiled with CFR 0.152.
 */
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

class kz
extends Thread {
    final /* synthetic */ String a;
    final /* synthetic */ hb b;
    final /* synthetic */ cc c;

    kz(cc cc2, String string, hb hb2) {
        this.c = cc2;
        this.a = string;
        this.b = hb2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void run() {
        HttpURLConnection httpURLConnection = null;
        try {
            URL uRL = new URL(this.a);
            httpURLConnection = (HttpURLConnection)uRL.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 404) {
                return;
            }
            this.c.a = this.b == null ? ImageIO.read(httpURLConnection.getInputStream()) : this.b.a(ImageIO.read(httpURLConnection.getInputStream()));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            httpURLConnection.disconnect();
        }
    }
}

