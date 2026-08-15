package net.nostalgia.client.gui;

import java.io.File;
import java.io.IOException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;

public class EpilepsyWarningScreen extends Screen {
  private static final Identifier FLASH_TEXTURE = Identifier.fromNamespaceAndPath("nostalgia", "textures/environment/flash.png");
  private final Screen parent;
  public static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("nostalgia_warning_accepted.txt").toFile();

  public EpilepsyWarningScreen(Screen parent) {
    super(Component.translatable("gui.nostalgia.warning.title"));
    this.parent = parent;
  }

  public static boolean hasAcceptedWarning() {
    return CONFIG_FILE.exists();
  }

  private void acceptWarning() {
    try {
      if (!CONFIG_FILE.getParentFile().exists()) {
        CONFIG_FILE.getParentFile().mkdirs();
      }

      CONFIG_FILE.createNewFile();
    } catch (IOException var2) {
      var2.printStackTrace();
    }
  }

  protected void init() {
    super.init();
  }

  public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
    double mouseX = event.x();
    double mouseY = event.y();
    int btnW = 130;
    int btnH = 30;
    int btnX = this.width / 2 - btnW / 2;
    int btnY = this.height / 2 + 45;
    if (mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH) {
      this.acceptWarning();
      if (this.minecraft != null) {
        this.minecraft.setScreen(this.parent);
      }

      return true;
    } else {
      return super.mouseClicked(event, doubleClick);
    }
  }

  public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
    int textureWidth = 256;
    int textureHeight = 256;
    int x = (this.width - textureWidth) / 2;
    int y = (this.height - textureHeight) / 2;
    graphics.blit(RenderPipelines.GUI_TEXTURED, FLASH_TEXTURE, x, y, 0.0F, 0.0F, textureWidth, textureHeight, textureWidth, textureHeight);
    Component boldTitle = this.title.copy().withStyle(Style.EMPTY.withBold(true));
    graphics.centeredText(this.font, boldTitle, this.width / 2, y + 25, -1);
    Component warningText = Component.translatable("gui.nostalgia.warning.text").copy().withStyle(Style.EMPTY.withBold(true));
    int textY = y + 108;
    graphics.textWithWordWrap(this.font, warningText, this.width / 2 - 85, textY, 170, -1, true);
    int btnW = 130;
    int btnH = 30;
    int btnX = this.width / 2 - btnW / 2;
    int btnY = this.height / 2 + 45;
    boolean hovered = mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH;
    int btnColor = hovered ? -11141121 : -1;
    Component continueText = Component.translatable("gui.nostalgia.warning.continue").copy().withStyle(Style.EMPTY.withBold(true));
    graphics.centeredText(this.font, continueText, this.width / 2, btnY + 11, btnColor);
    super.extractRenderState(graphics, mouseX, mouseY, partialTick);
  }

  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
    super.extractBackground(graphics, mouseX, mouseY, partialTick);
  }

  public boolean shouldCloseOnEsc() {
    return false;
  }
}
