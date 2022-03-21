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
        return super.insertItem(slot, stack, simulate);
    }
}
