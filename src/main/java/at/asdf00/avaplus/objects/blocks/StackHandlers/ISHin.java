package at.asdf00.avaplus.objects.blocks.StackHandlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ISHin extends ItemStackHandler {
    public ISHin(int size) {
        super(size);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return stack.getItem().getRegistryName().toString().equals("avaritia:singularity") ? super.insertItem(slot, stack, simulate) : stack;
    }
}
