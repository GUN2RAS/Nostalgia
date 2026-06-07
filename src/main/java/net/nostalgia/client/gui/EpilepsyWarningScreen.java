package net.nostalgia.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.io.File;
import java.io.IOException;

public class EpilepsyWarningScreen extends Screen {

    private static final Identifier FLASH_TEXTURE = Identifier.fromNamespaceAndPath("nostalgia", "textures/environment/flash.png");
    private final Screen parent;
    public static final File CONFIG_FILE = net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().resolve("nostalgia_warning_accepted.txt").toFile();

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();
        
        int btnW = 130;
        int btnH = 30;
        int btnX = this.width / 2 - btnW / 2;
        int btnY = this.height / 2 + 45; 
        
        if (mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH) {
            acceptWarning();
            if (this.minecraft != null) {
                this.minecraft.setScreen(this.parent);
            }
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public void extractRenderState(net.minecraft.client.gui.GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int textureWidth = 256;
        int textureHeight = 256;
        int x = (this.width - textureWidth) / 2;
        int y = (this.height - textureHeight) / 2;

        graphics.blit(
            net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, 
            FLASH_TEXTURE, 
            x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight
        );

        Component boldTitle = this.title.copy().withStyle(net.minecraft.network.chat.Style.EMPTY.withBold(true));
        graphics.centeredText(this.font, boldTitle, this.width / 2, y + 25, 0xFFFFFFFF); 
        
        Component warningText = Component.translatable("gui.nostalgia.warning.text").copy().withStyle(net.minecraft.network.chat.Style.EMPTY.withBold(true));
        int textY = y + 108; 
        graphics.textWithWordWrap(this.font, warningText, this.width / 2 - 85, textY, 170, 0xFFFFFFFF, true);  

        int btnW = 130;
        int btnH = 30;
        int btnX = this.width / 2 - btnW / 2;
        int btnY = this.height / 2 + 45; 
        
        boolean hovered = mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH;
        int btnColor = hovered ? 0xFF55FFFF : 0xFFFFFFFF;
        
        Component continueText = Component.translatable("gui.nostalgia.warning.continue").copy().withStyle(net.minecraft.network.chat.Style.EMPTY.withBold(true));
        graphics.centeredText(this.font, continueText, this.width / 2, btnY + 11, btnColor);
        
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }
    
    @Override
    public void extractBackground(net.minecraft.client.gui.GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
