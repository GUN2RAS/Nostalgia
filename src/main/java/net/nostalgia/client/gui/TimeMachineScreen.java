package net.nostalgia.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.nostalgia.inventory.TimeMachineMenu;
import net.nostalgia.network.C2STravelRequestPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class TimeMachineScreen extends AbstractContainerScreen<TimeMachineMenu> {
    private static final Identifier CRT_SCANLINES = Identifier.fromNamespaceAndPath("nostalgia", "textures/gui/crt_scanlines.png");
    private static final Identifier ALPHA_NODE = Identifier.fromNamespaceAndPath("nostalgia", "textures/gui/nodes/dimension_alpha.png");
    private static final Identifier RD_NODE = Identifier.fromNamespaceAndPath("nostalgia", "textures/gui/nodes/dimension_rd.png");

    private static final Identifier OVERWORLD_NODE = Identifier.fromNamespaceAndPath("minecraft", "textures/block/grass_block_side.png");

    public static boolean nextScreenIsError = false;
    private boolean isUnstableError = false;

    private int overloadFrames = 0;
    private boolean isOverloading = false;
    private int selectedTargetIndex = 0;

    private String[] currentTargets;
    private String[] currentTargetNames;
    private Identifier[] currentTargetIcons;

    public TimeMachineScreen(TimeMachineMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.inventoryLabelY = 1000;
        this.titleLabelX = 8;
        this.titleLabelY = 5;
    }

    @Override
    protected void init() {
        super.init();
        if (nextScreenIsError) {
            this.isUnstableError = true;
            nextScreenIsError = false;
        }
        if (this.currentTargets == null) {
            java.util.List<String> tgts = new java.util.ArrayList<>();
            java.util.List<String> names = new java.util.ArrayList<>();
            java.util.List<Identifier> icons = new java.util.ArrayList<>();

            net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> currentKey = this.minecraft.level.dimension();

            if (currentKey != net.minecraft.world.level.Level.OVERWORLD) {
                tgts.add("overworld");
                names.add("OVERWORLD");
                icons.add(Identifier.tryParse("minecraft:textures/block/grass_block_side.png")); 
            }
            if (currentKey != net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
                tgts.add("alpha");
                names.add("ALPHA 1.1.2_01");
                icons.add(ALPHA_NODE);
            }
            if (currentKey != net.nostalgia.world.dimension.ModDimensions.RD_132211_LEVEL_KEY) {
                tgts.add("rd");
                names.add("RD-132211");
                icons.add(RD_NODE);
            }

            if (tgts.isEmpty()) {
                tgts.add("alpha"); names.add("ALPHA 1.1.2_01"); icons.add(ALPHA_NODE);
            }

            this.currentTargets = tgts.toArray(new String[0]);
            this.currentTargetNames = names.toArray(new String[0]);
            this.currentTargetIcons = icons.toArray(new Identifier[0]);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (this.isOverloading) {
            this.overloadFrames++;
            
            if (this.minecraft != null && this.minecraft.player != null) {
                
                if (this.overloadFrames == 1) {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.BEACON_ACTIVATE, 1.0f, 0.5f);
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.CONDUIT_ACTIVATE, 1.0f, 0.1f);
                }
                if (this.overloadFrames == 15) {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_RESONATE, 1.0f, 0.5f);
                }
                if (this.overloadFrames == 25) {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.RESPAWN_ANCHOR_CHARGE, 1.0f, 0.6f);
                }
                if (this.overloadFrames > 5 && Math.random() < 0.2) {
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.GLASS_BREAK, 0.2f, (float)(1.2 + Math.random()));
                }
                
                if (this.overloadFrames > 40) {
                    
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.WARDEN_SONIC_BOOM, 2.0f, 0.5f);
                    this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.PORTAL_TRAVEL, 0.4f, 1.2f);
                    
                    ClientPlayNetworking.send(new C2STravelRequestPayload(this.currentTargets[this.selectedTargetIndex]));
                    this.minecraft.player.closeContainer();
                }
            }
        }
    }

    @Override
    public void extractRenderState(net.minecraft.client.gui.GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        if (this.isUnstableError) {
            this.extractBackground(graphics, mouseX, mouseY, partialTick);
            this.extractLabels(graphics, mouseX, mouseY);
            return;
        }
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        this.extractTooltip(graphics, mouseX, mouseY);
    }

    private int getRandomCyberColor() {
        int r = (int) (Math.random() * 255);
        int g = (int) (Math.random() * 255);
        int b = (int) (Math.random() * 255);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    private int blendColor(int color1, int color2, float ratio) {
        int a1 = (color1 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int a2 = (color2 >> 24) & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int a = (int) (a1 + (a2 - a1) * ratio);
        int r = (int) (r1 + (r2 - r1) * ratio);
        int g = (int) (g1 + (g2 - g1) * ratio);
        int b = (int) (b1 + (b2 - b1) * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    @Override
    public void extractBackground(net.minecraft.client.gui.GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if (this.isUnstableError) {
            graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xFF330000);
            graphics.outline(x - 1, y - 1, this.imageWidth + 2, this.imageHeight + 2, 0xFFFF0000); 
            graphics.outline(x, y, this.imageWidth, this.imageHeight, 0xFFAA0000);
            long time = System.currentTimeMillis();
            int scrollY = (int) ((time / 5) % 4); 
            graphics.blit(RenderPipelines.GUI_TEXTURED, CRT_SCANLINES, x, y, 0.0F, scrollY, this.imageWidth, this.imageHeight, 4, 4);
            if (Math.random() < 0.1) {
                graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, getRandomCyberColor() & 0x66FFFFFF);
            }
            return;
        }

        graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xFF141414);
        graphics.outline(x - 1, y - 1, this.imageWidth + 2, this.imageHeight + 2, 0xFFFFFFFF); 
        graphics.outline(x, y, this.imageWidth, this.imageHeight, 0xFF335577); 
        
        graphics.fill(x + 1, y + 1, x + this.imageWidth - 1, y + 16, 0xFF001122); 
        graphics.horizontalLine(x + 1, x + this.imageWidth - 2, y + 16, 0xFFFFFFFF);

        int vX = x + 8;
        int vY = y + 22;
        int vW = 160;
        int vH = 70;
        
        graphics.fill(vX - 2, vY - 2, vX + vW + 2, vY + vH + 2, 0xFF000000);

        int shakeX = this.isOverloading ? (int)((Math.random() - 0.5) * 6) : 0;
        int shakeY = this.isOverloading ? (int)((Math.random() - 0.5) * 6) : 0;
        int ovX = vX + shakeX;
        int ovY = vY + shakeY;

        graphics.outline(vX - 2, vY - 2, vW + 4, vH + 4, 0xFF4477AA);

        graphics.enableScissor(vX, vY, vX + vW, vY + vH);
        long time = System.currentTimeMillis();
        float panX = (float) Math.sin(time / 4000.0) * 10;
        
        Identifier currentIcon = this.currentTargetIcons[this.selectedTargetIndex];
        graphics.blit(RenderPipelines.GUI_TEXTURED, currentIcon, (int)(ovX - 10 + panX), ovY - 10, 0.0F, 0.0F, 180, 90, 180, 90);
        
        int scrollY = (int) ((time / 20) % 4);
        if (this.isOverloading) scrollY = (int) ((time / 5) % 4); 
        graphics.blit(RenderPipelines.GUI_TEXTURED, CRT_SCANLINES, ovX, ovY, 0.0F, scrollY, vW, vH, 4, 4);
        
        if (this.isOverloading) {
            
            graphics.fill(vX, vY, vX + vW, vY + vH, getRandomCyberColor() & 0x66FFFFFF);
        }
        graphics.disableScissor();

        float waveSpeed = this.isOverloading ? 50.0f : 300.0f;
        float w1 = (float) (Math.sin(time / waveSpeed) * 0.5 + 0.5);
        float w2 = (float) (Math.sin(time / waveSpeed + 1.0) * 0.5 + 0.5);
        float w3 = (float) (Math.sin(time / waveSpeed + 2.0) * 0.5 + 0.5);

        int baseC1 = 0xFF116688; int activeC1 = 0xFF00FFFF;
        int baseC2 = 0xFF003355; int activeC2 = 0xFF00AAFF;
        int baseC3 = 0xFF002233; int activeC3 = 0xFF0066AA;

        int wireC1 = blendColor(baseC1, activeC1, w1);
        int wireC2 = blendColor(baseC2, activeC2, w2);
        int wireC3 = blendColor(baseC3, activeC3, w3);

        if (this.isOverloading && Math.random() < 0.1) wireC1 = getRandomCyberColor();

        graphics.fill(x + 88, y + 110, x + 90, y + 118, wireC1);
        graphics.fill(x + 50, y + 110, x + 90, y + 112, wireC1);
        graphics.fill(x + 50, y + 92, x + 52, y + 112, wireC1);

        graphics.fill(x + 80, y + 114, x + 82, y + 118, wireC2);
        graphics.fill(x + 80, y + 114, x + 120, y + 116, wireC2);
        graphics.fill(x + 120, y + 92, x + 122, y + 116, wireC2);

        graphics.fill(x + 84, y + 104, x + 86, y + 118, wireC3);

        int btnTgtX = x + 38;
        int btnTgtY = y + 95;
        boolean hoverTgt = isHoveringArea(btnTgtX, btnTgtY, 100, 12, mouseX, mouseY) && !this.isOverloading;
        graphics.fill(btnTgtX, btnTgtY, btnTgtX + 100, btnTgtY + 12, hoverTgt ? 0xFF113355 : 0xFF001122);
        graphics.outline(btnTgtX, btnTgtY, 100, 12, hoverTgt ? 0xFFFFFFFF : 0xFF5588BB); 
        graphics.centeredText(this.font, this.currentTargetNames[this.selectedTargetIndex] + " \u25BC", btnTgtX + 50, btnTgtY + 2, 0xFFFFFFFF);

        int cx = x + 80 - 1;
        int cy = y + 118 - 1;
        graphics.fill(cx, cy, cx + 18, cy + 18, 0xFF001122); 
        graphics.outline(cx, cy, 18, 18, this.isOverloading ? getRandomCyberColor() : 0xFF33CCFF); 

        graphics.fill(x + 7, y + 141, x + 169, y + 160, 0xFF0C0C0C);
        for(int c = 0; c < 9; ++c) {
            int px = x + 8 + c * 18 - 1;
            int py = y + 142 - 1;
            graphics.outline(px, py, 18, 18, 0xFF555555);
        }

        int btnGoX = x + 108;
        int btnGoY = y + 115;
        boolean hoverGo = isHoveringArea(btnGoX, btnGoY, 32, 21, mouseX, mouseY) && !this.isOverloading;
        
        graphics.fill(btnGoX, btnGoY, btnGoX + 32, btnGoY + 21, hoverGo ? 0xFF000000 : 0xFF112233);
        graphics.outline(btnGoX, btnGoY, 32, 21, hoverGo ? 0xFF00FFFF : 0xFF3388CC);
        graphics.centeredText(this.font, Component.translatable("gui.nostalgia.time_machine.launch").getString(), btnGoX + 16, btnGoY + 7, hoverGo ? 0xFFFFFFFF : 0xFFAADDFF);

        if (hoverGo) {
            graphics.setTooltipForNextFrame(this.font, Component.translatable("gui.nostalgia.time_machine.launch_tooltip"), mouseX, mouseY);
        }
    }

    @Override
    protected void extractLabels(net.minecraft.client.gui.GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.isUnstableError) {
            int x = (this.width - this.imageWidth) / 2;
            int y = (this.height - this.imageHeight) / 2;
            String key = "nostalgia.ritual.unstable_zone";
            String errorMsg = net.minecraft.client.resources.language.I18n.exists(key) 
                ? net.minecraft.client.resources.language.I18n.get(key) 
                : "§cUnstable zone: move at least 10 chunks away to start a new source.";
            graphics.centeredText(this.font, "CRITICAL ERROR", x + this.imageWidth / 2, y + 30, 0xFFFF0000);
            graphics.textWithWordWrap(this.font, net.minecraft.network.chat.Component.literal(errorMsg), x + 10, y + 50, this.imageWidth - 20, 0xFFFF5555);
            return;
        }
        graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFFFFFFF, false);
        if (!this.isOverloading) {
            graphics.text(this.font, Component.translatable("gui.nostalgia.time_machine.charge").getString(), 45, 122, 0x33CCFF, false);
        } else {
            graphics.text(this.font, "OVERLOAD", 30, 122, getRandomCyberColor(), true);
        }
    }

    private boolean isHoveringArea(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean isDoubleClick) {
        if (this.isUnstableError) return false;
        if (this.isOverloading) return false;

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        int btnTgtX = x + 38;
        int btnTgtY = y + 95;
        if (isHoveringArea(btnTgtX, btnTgtY, 100, 12, event.x(), event.y()) && event.button() == 0) {
            this.selectedTargetIndex = (this.selectedTargetIndex + 1) % this.currentTargets.length;
            
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1.0f);
            }
            return true;
        }

        int btnGoX = x + 108;
        int btnGoY = y + 115;
        if (isHoveringArea(btnGoX, btnGoY, 32, 21, event.x(), event.y()) && event.button() == 0) {
            this.isOverloading = true;
            this.overloadFrames = 0;
            net.nostalgia.client.ritual.RitualVisualManager.suppressZoneAudioUntil = System.currentTimeMillis() + 5000L;
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.playSound(net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK.value(), 1.0f, 1.0f);
            }
            return true;
        }

        return super.mouseClicked(event, isDoubleClick);
    }
}
