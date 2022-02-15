package at.asdf00.avaplus.objects.items;

import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.init.ItemInit;
import at.asdf00.avaplus.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

    public ItemBase(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.REDSTONE);

        ItemInit.ITEM_LIST.add(this);
    }
    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
