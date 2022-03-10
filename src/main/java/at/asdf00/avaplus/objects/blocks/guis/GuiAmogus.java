package at.asdf00.avaplus.objects.blocks.guis;

import at.asdf00.avaplus.References;
import at.asdf00.avaplus.objects.blocks.ContainerAmogus;
import at.asdf00.avaplus.objects.blocks.tileentities.TileEntityAmogus;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiAmogus extends GuiContainer {
    private static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/gui/amogus.png");
    private final InventoryPlayer player;
    private final TileEntityAmogus tileentity;

    public GuiAmogus(InventoryPlayer player, TileEntityAmogus tileentity) {
        super(new ContainerAmogus(player, tileentity));
        this.player = player;
        this.tileentity = tileentity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String tileName = tileentity.getDisplayName().getUnformattedText();
        fontRenderer.drawString(tileName, (xSize / 2 - fontRenderer.getStringWidth(tileName) / 2) -5, 6, 4210752);
        fontRenderer.drawString(player.getDisplayName().getUnformattedText(), 7, ySize - 96 + 2, 4210752);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(TEXTURES);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        /*
        int l = (int) (getCookProgressScaled(24) >> 32);
        drawTexturedModalRect(guiLeft + 63, guiTop + 36, 176, 14, l + 1, 16);

        int k = getEnergyStoredScaled(75);
        drawTexturedModalRect(guiLeft + 152, guiTop + 7, 176, 32, 16, 76 - k);
         */
    }
    private long getCookProgressScaled(int pixels) {
        long i = tileentity.rfConsumed;
        return i != 0 ? i * pixels / 100 : 0;
    }
    private int getEnergyStoredScaled(int pixels) {
        int i = tileentity.getEnergyStored();
        int j = tileentity.getMaxEnergyStored();
        return i != 0 && j != 0 ? i * pixels / j : 0;
    }
}
