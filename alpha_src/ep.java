/*
 * Decompiled with CFR 0.152.
 */
import java.io.InputStream;
import paulscode.sound.codecs.CodecJOrbis;

public class ep
extends CodecJOrbis {
    protected InputStream openInputStream() {
        return new hk(this, this.url, this.urlConnection.getInputStream());
    }
}

