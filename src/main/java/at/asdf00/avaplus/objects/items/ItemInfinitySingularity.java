package at.asdf00.avaplus.objects.items;

import morph.avaritia.api.IHaloRenderItem;
import morph.avaritia.init.AvaritiaTextures;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemInfinitySingularity extends ItemBase implements IHaloRenderItem {

    public ItemInfinitySingularity(String name) {
        super(name, 64);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldDrawHalo(ItemStack stack) {
        return true;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getHaloTexture(ItemStack stack) {
        return AvaritiaTextures.HALO;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public int getHaloColour(ItemStack stack) {
        return 0xff000000;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public int getHaloSize(ItemStack stack) {
        return 5;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldDrawPulse(ItemStack stack) {
        return false;
    }

    @Override
    public void registerModels() {
        super.registerModels();

    }
}
