package at.asdf00.avaplus.objects.blocks.eyeofsauron;

import at.asdf00.avaplus.Main;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntitySauron extends TileEntity implements ITickable {

    public static boolean staticIsValidInput(ItemStack stack) {
        Item item = stack.getItem();
        if (item.getRegistryName().equals("avaritia:resource"))
            Main.logger.info("resource metadata = " + stack.getMetadata());
        return (item.getRegistryName().equals("avaritia:resource") || item.getRegistryName().equals("avaritia:block_resource"));
    }

    @Override
    public void update() {

    }
}
