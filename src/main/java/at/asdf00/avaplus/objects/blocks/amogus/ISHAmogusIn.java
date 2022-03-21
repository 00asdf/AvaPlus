package at.asdf00.avaplus.objects.blocks.amogus;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ISHAmogusIn extends ItemStackHandler {
    public ISHAmogusIn(int size) {
        super(size);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return TileEntityAmogus.staticIsValidInput(stack) ? super.insertItem(slot, stack, simulate) : stack;
    }
    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return TileEntityAmogus.staticIsValidInput(stack) && super.isItemValid(slot, stack);
    }
}
