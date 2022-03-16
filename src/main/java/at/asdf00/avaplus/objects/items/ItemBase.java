package at.asdf00.avaplus.objects.items;

import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.init.ItemInit;
import at.asdf00.avaplus.util.IHasModel;
import morph.avaritia.Avaritia;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemBase extends Item implements IHasModel {

    public ItemBase(String name, int maxStackSize) {
        setRegistryName(name);
        setUnlocalizedName(name);
        setMaxDamage(0);
        setCreativeTab(Avaritia.tab);
        setMaxStackSize(maxStackSize);

        ItemInit.ITEM_LIST.add(this);
    }
    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
