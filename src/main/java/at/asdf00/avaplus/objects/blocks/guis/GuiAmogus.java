package at.asdf00.avaplus.objects.blocks.guis;

import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.References;
import at.asdf00.avaplus.objects.blocks.ContainerAmogus;
import at.asdf00.avaplus.objects.blocks.tileentities.TileEntityAmogus;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiAmogus extends GuiContainer {
    private static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/gui/amogui.png");
    private final InventoryPlayer player;
    private final TileEntityAmogus tileentity;
    private final boolean singularityOnly;

    public GuiAmogus(InventoryPlayer player, TileEntityAmogus tileentity, boolean singularityOnly) {
        super(new ContainerAmogus(player, tileentity));
        this.player = player;
        this.tileentity = tileentity;
        this.singularityOnly = singularityOnly;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String tileName = tileentity.getDisplayName().getUnformattedText();
        fontRenderer.drawString(tileName, (xSize / 2 - fontRenderer.getStringWidth(tileName) / 2), 6, 0x404040);
        fontRenderer.drawString(player.getDisplayName().getUnformattedText(), 7, ySize - 96 + 2, 0x404040);
        String progress = String.format("%.1f%%", tileentity.getProgress() * 100.0);
        fontRenderer.drawString(progress, (xSize / 2 - fontRenderer.getStringWidth(progress) / 2), 46, 0x404040);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(TEXTURES);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        drawTexturedModalRect(guiLeft + 45, guiTop + 27, 2, 169, Math.round((float)(tileentity.getProgress() * 86)), 17);
        if (singularityOnly)
            drawTexturedModalRect(guiLeft + 26, guiTop + 27, 0, 188, 16, 16);
    }
}
