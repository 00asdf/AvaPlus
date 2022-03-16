package at.asdf00.avaplus.objects.blocks.guis;

import at.asdf00.avaplus.References;
import at.asdf00.avaplus.objects.blocks.ContainerAmogus;
import at.asdf00.avaplus.objects.blocks.tileentities.TileEntityAmogus;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiAmogus extends GuiContainer {
    protected static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/gui/amogui.png");
    protected final InventoryPlayer player;
    protected final TileEntityAmogus tileentity;
    protected boolean singularityOnly;

    public GuiAmogus(InventoryPlayer player, TileEntityAmogus tileentity, boolean tier1) {
        super(new ContainerAmogus(player, tileentity, tier1));
        this.player = player;
        this.tileentity = tileentity;
        this.singularityOnly = tier1;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String tileName = tileentity.getDisplayName().getUnformattedText();
        fontRenderer.drawString(tileName, (xSize / 2 - fontRenderer.getStringWidth(tileName) / 2), 6, 0x404040);
        fontRenderer.drawString(player.getDisplayName().getUnformattedText(), 7, ySize - 96 + 2, 0x404040);
        String progress = String.format("%.1f%%", tileentity.getProgress() * 100.0);
        fontRenderer.drawString(progress, (xSize / 2 - fontRenderer.getStringWidth(progress) / 2), 46, 0x404040);
        String prStr = progressToString();
        fontRenderer.drawString(prStr, (xSize / 2 - fontRenderer.getStringWidth(prStr) / 2), 56, 0x404040);
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
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    private static final String[] si ={"", "k", "M", "G", "T", "P", "E"};
    private String progressToString() {
        long cur = tileentity.getRfToReplicate();
        int pre = 0;
        while (cur >= 1000) {
            cur /= 1000;
            pre++;
        }
        return getScaledAsString(tileentity.getRfConsumed(), pre) + "/ " + getScaledAsString(tileentity.getRfToReplicate(), pre) + si[pre] + "RF";
    }
    private String getScaledAsString(long val, int pre) {
        val /= Math.round(Math.pow(1000, pre)) / 10;
        return String.format("%d.%d ", val / 10, val % 10);
    }
}
