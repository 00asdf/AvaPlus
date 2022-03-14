package at.asdf00.avaplus.objects.blocks;


import at.asdf00.avaplus.objects.blocks.slots.SlotHandlerAmogusIn;
import at.asdf00.avaplus.objects.blocks.slots.SlotHandlerAmogusOut;
import at.asdf00.avaplus.objects.blocks.tileentities.TileEntityAmogus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerAmogus extends Container {
    private final TileEntityAmogus tileentity;
    private int scaledRfConsumed;

    public ContainerAmogus(InventoryPlayer player, TileEntityAmogus tileentity) {
        this.tileentity = tileentity;
        IItemHandler handler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        // replicator slots
        addSlotToContainer(new SlotHandlerAmogusIn(handler, 0, 26, 27));
        addSlotToContainer(new SlotHandlerAmogusOut(handler, 1, 134, 27));
        // hotbar slots
        for(int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 142));
        }
        // player inventory slots
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(player, x + y*9 + 9, 8 + x*18, 84 + y*18));
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileentity.isUsableByPlayer(playerIn);
    }
    @Override
    public void updateProgressBar(int id, int data) {
        if (id == 0)
            tileentity.setRfConsumedScaled(data);
    }
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for(int i = 0; i < listeners.size(); ++i) {
            IContainerListener listener = listeners.get(i);
            if(scaledRfConsumed != tileentity.getRfConsumedScaled())
                listener.sendWindowProperty(this, 0, tileentity.getRfConsumedScaled());
        }
        scaledRfConsumed = tileentity.getRfConsumedScaled();
    }
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack transferSt = slot.getStack();
            stack = transferSt.copy();
            if (index < 2) {    // transfer from replicator to player
                if (!mergeItemStack(transferSt, 2, inventorySlots.size(), false))
                    return ItemStack.EMPTY;
                slot.onSlotChange(transferSt, stack);
            } else {    // transfer into replicator
                if (transferSt.getItem().getRegistryName().toString().equals("avaritia:singularity")) {
                    if (!mergeItemStack(transferSt, 0, 1, false)) {
                        if (index < 11 && !mergeItemStack(transferSt, 11, inventorySlots.size(), false))
                            return ItemStack.EMPTY;
                        else if (!mergeItemStack(transferSt, 2, 11, false))
                            return ItemStack.EMPTY;
                    }
                    slot.onSlotChange(transferSt, stack);
                } else {
                    if (!mergeItemStack(transferSt, 0, 1, false)) {
                        if (index < 11 && !mergeItemStack(transferSt, 11, inventorySlots.size(), false))
                            return ItemStack.EMPTY;
                        else if (!mergeItemStack(transferSt, 2, 11, false))
                            return ItemStack.EMPTY;
                    }
                }
            }
            if(transferSt.isEmpty())
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
            if(transferSt.getCount() == stack.getCount())
                return ItemStack.EMPTY;
            slot.onTake(playerIn, transferSt);
        }
        return stack;
    }
}
