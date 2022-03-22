package at.asdf00.avaplus.objects.blocks.eyeofsauron;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotHandlerSauron extends SlotItemHandler {

    public SlotHandlerSauron(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return TileEntitySauron.isValidInput(stack) && super.isItemValid(stack);
    }
}
