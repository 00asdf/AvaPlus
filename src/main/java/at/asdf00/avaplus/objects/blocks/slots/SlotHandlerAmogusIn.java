package at.asdf00.avaplus.objects.blocks.slots;

import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.objects.blocks.tileentities.TileEntityAmogus;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotHandlerAmogusIn extends SlotItemHandler {
    public SlotHandlerAmogusIn(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return TileEntityAmogus.staticIsValidInput(stack) && super.isItemValid(stack);
    }
}
