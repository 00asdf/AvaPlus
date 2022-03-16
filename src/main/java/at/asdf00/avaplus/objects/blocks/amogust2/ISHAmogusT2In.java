package at.asdf00.avaplus.objects.blocks.amogust2;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ISHAmogusT2In extends ItemStackHandler {
    public ISHAmogusT2In(int size) {
        super(size);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return TileEntityAmogusT2.staticIsValidInput(stack) ? super.insertItem(slot, stack, simulate) : stack;
    }
}
