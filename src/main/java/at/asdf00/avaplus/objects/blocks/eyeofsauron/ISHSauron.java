package at.asdf00.avaplus.objects.blocks.eyeofsauron;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ISHSauron extends ItemStackHandler {
    public ISHSauron(int size) {
        super(size);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return TileEntitySauron.isValidInput(stack) ? super.insertItem(slot, stack, simulate) : stack;
    }
    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return TileEntitySauron.isValidInput(stack) && super.isItemValid(slot, stack);
    }
}
