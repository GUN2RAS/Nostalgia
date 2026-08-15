package net.nostalgia.client.gui.hologram3d;

public class HologramCamera {
  private float camX;
  private float camZ;
  private float zoom = 0.6F;
  private final float rotation = 0.785F;
  private final float tilt = 0.4F;
  private final int centerX;
  private final int centerZ;
  private final int maxRadius;

  public HologramCamera(int centerX, int centerZ, int maxRadius) {
    this.centerX = centerX;
    this.centerZ = centerZ;
    this.maxRadius = maxRadius;
    this.camX = centerX;
    this.camZ = centerZ;
  }

  public void pan(float screenDeltaX, float screenDeltaY) {
    float cos = (float)Math.cos(0.7850000262260437);
    float sin = (float)Math.sin(0.7850000262260437);
    float invZoom = 1.0F / this.zoom;
    float invTiltZoom = 1.0F / (this.zoom * 0.4F);
    float worldDx = cos * screenDeltaX * invZoom - sin * screenDeltaY * invTiltZoom;
    float worldDz = sin * screenDeltaX * invZoom + cos * screenDeltaY * invTiltZoom;
    this.camX -= worldDx;
    this.camZ -= worldDz;
    float dx = this.camX - this.centerX;
    float dz = this.camZ - this.centerZ;
    float dist = (float)Math.sqrt(dx * dx + dz * dz);
    float effectiveRadius = this.maxRadius;
    if (dist > effectiveRadius) {
      float scale = effectiveRadius / dist;
      this.camX = this.centerX + dx * scale;
      this.camZ = this.centerZ + dz * scale;
    }
  }

  public void adjustZoom(double scrollDelta, float cursorOffX, float cursorOffY) {
    float cos = (float)Math.cos(0.7850000262260437);
    float sin = (float)Math.sin(0.7850000262260437);
    float invZoom = 1.0F / this.zoom;
    float invTiltZoom = 1.0F / (this.zoom * 0.4F);
    float worldCurX = cos * cursorOffX * invZoom - sin * cursorOffY * invTiltZoom;
    float worldCurZ = sin * cursorOffX * invZoom + cos * cursorOffY * invTiltZoom;
    float oldZoom = this.zoom;
    this.zoom *= (float)(1.0 + scrollDelta * 0.15);
    if (this.zoom < 0.3F) {
      this.zoom = 0.3F;
    }

    if (this.zoom > 3.0F) {
      this.zoom = 3.0F;
    }

    float newInvZoom = 1.0F / this.zoom;
    float newInvTiltZoom = 1.0F / (this.zoom * 0.4F);
    float newWorldCurX = cos * cursorOffX * newInvZoom - sin * cursorOffY * newInvTiltZoom;
    float newWorldCurZ = sin * cursorOffX * newInvZoom + cos * cursorOffY * newInvTiltZoom;
    this.camX += worldCurX - newWorldCurX;
    this.camZ += worldCurZ - newWorldCurZ;
  }

  public void project(float worldX, float worldY, float worldZ, float[] out) {
    float relX = worldX - this.camX;
    float relZ = worldZ - this.camZ;
    float cos = (float)Math.cos(0.7850000262260437);
    float sin = (float)Math.sin(0.7850000262260437);
    float isoX = relX * cos + relZ * sin;
    float isoZ = -relX * sin + relZ * cos;
    out[0] = isoX * this.zoom;
    out[1] = (isoZ * 0.4F - worldY * 0.7F) * this.zoom;
  }

  public float getCamX() {
    return this.camX;
  }

  public float getCamZ() {
    return this.camZ;
  }

  public float getZoom() {
    return this.zoom;
  }

  public void setCamX(float x) {
    this.camX = x;
  }

  public void setCamZ(float z) {
    this.camZ = z;
  }

  public int[] unproject(float screenX, float screenY) {
    float invZoom = 1.0F / this.zoom;
    float px = screenX * invZoom;
    float py = screenY * invZoom / 0.4F;
    float cos = (float)Math.cos(0.7850000262260437);
    float sin = (float)Math.sin(0.7850000262260437);
    float worldX = px * cos - py * sin + this.camX;
    float worldZ = px * sin + py * cos + this.camZ;
    return new int[]{(int)worldX, (int)worldZ};
  }
}
