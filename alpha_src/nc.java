/*
 * Decompiled with CFR 0.152.
 */
import java.net.ConnectException;
import java.net.UnknownHostException;
import net.minecraft.client.Minecraft;

class nc
extends Thread {
    final /* synthetic */ Minecraft a;
    final /* synthetic */ String b;
    final /* synthetic */ int c;
    final /* synthetic */ mn d;

    nc(mn mn2, Minecraft minecraft, String string, int n2) {
        this.d = mn2;
        this.a = minecraft;
        this.b = string;
        this.c = n2;
    }

    public void run() {
        try {
            mn.a(this.d, new gy(this.a, this.b, this.c));
            if (mn.a(this.d)) {
                return;
            }
            mn.b(this.d).a((fn)new gt(this.a.i.b));
        }
        catch (UnknownHostException unknownHostException) {
            if (mn.a(this.d)) {
                return;
            }
            this.a.a(new cj("Failed to connect to the server", "Unknown host '" + this.b + "'"));
        }
        catch (ConnectException connectException) {
            if (mn.a(this.d)) {
                return;
            }
            this.a.a(new cj("Failed to connect to the server", connectException.getMessage()));
        }
        catch (Exception exception) {
            if (mn.a(this.d)) {
                return;
            }
            exception.printStackTrace();
            this.a.a(new cj("Failed to connect to the server", exception.toString()));
        }
    }
}

