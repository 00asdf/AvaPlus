package at.asdf00.avaplus.objects.blocks.amogust2;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotHandlerAmogusT2In extends SlotItemHandler {
    public SlotHandlerAmogusT2In(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return TileEntityAmogusT2.staticIsValidInput(stack) && super.isItemValid(stack);
    }
}
