package at.asdf00.avaplus.objects.blocks.eyeofsauron;

import at.asdf00.avaplus.ModConfig;
import at.asdf00.avaplus.References;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSauron extends GuiContainer {
    protected static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/gui/amogui.png");
    protected final InventoryPlayer player;
    protected final TileEntitySauron tileentity;

    public GuiSauron(InventoryPlayer player, TileEntitySauron tileentity) {
        super(new ContainerSauron(player, tileentity));
        this.player = player;
        this.tileentity = tileentity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String tileName = tileentity.getDisplayName().getUnformattedText();
        fontRenderer.drawString(tileName, (xSize / 2 - fontRenderer.getStringWidth(tileName) / 2), 6, 0x404040);
        fontRenderer.drawString(player.getDisplayName().getUnformattedText(), 7, ySize - 96 + 2, 0x404040);
        String matter = "" + tileentity.matterStored;
        fontRenderer.drawString(matter, (xSize / 2 - fontRenderer.getStringWidth(matter) / 2), 14, 0x404040);
        String rfT = getSiRfT();
        fontRenderer.drawString(rfT, (xSize / 2 - fontRenderer.getStringWidth(rfT) / 2), 22, 0x404040);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(TEXTURES);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    private static final String[] si ={"", "k", "M", "G", "T", "P", "E"};
    private String getSiRfT() {
        int prod = tileentity.getRfProduced();
        int pre = 0;
        while (prod >= 1000) {
            prod /= 1000;
            pre++;
        }
        return getScaledAsString(tileentity.getRfProduced(), pre) + si[pre] + "RF/T";
    }
    private String getScaledAsString(long val, int pre) {
        if(pre > 0)
            val /= Math.round(Math.pow(1000, pre)) / 10;
        else
            val *= 10;
        return String.format("%d.%d ", val / 10, val % 10);
    }
}
