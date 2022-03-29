package at.asdf00.avaplus.objects.blocks.eyeofsauron;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;

public class ContainerSauron extends Container {
    protected final InventoryPlayer player;
    protected final TileEntitySauron tileentity;
    protected int matterStoredCheck = 0;
    protected int burnTimeCheck = 0;

    public ContainerSauron(InventoryPlayer player, TileEntitySauron tileentity) {
        this.player = player;
        this.tileentity = tileentity;
        addSlotToContainer(new SlotHandlerSauron(tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 0, 26, 27));
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
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener : listeners) {
            if (matterStoredCheck != tileentity.matterStored)
                listener.sendWindowProperty(this, 0, tileentity.matterStored);
            if (burnTimeCheck != tileentity.burnTime)
                listener.sendWindowProperty(this, 1, tileentity.burnTime);
        }
        matterStoredCheck = tileentity.matterStored;
        burnTimeCheck = tileentity.burnTime;
    }
    @Override
    public void updateProgressBar(int id, int data) {
        switch (id) {
            case 0:
                tileentity.matterStored = data;
                break;
            case 1:
                tileentity.burnTime = data;
                break;
        }
    }
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = getSlot(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack transferSt = slot.getStack();
            stack = transferSt.copy();
            if (index < 1) {    // transfer from generator to player
                if (!mergeItemStack(transferSt, 1, inventorySlots.size(), false))
                    return ItemStack.EMPTY;
                slot.onSlotChange(transferSt, stack);
            } else {    // transfer into generator
                if (TileEntitySauron.isValidInput(transferSt)) {
                    if (!mergeItemStack(transferSt, 0, 1, false)) {
                        if (index < 10 && !mergeItemStack(transferSt, 10, inventorySlots.size(), false))
                            return ItemStack.EMPTY;
                        else if (!mergeItemStack(transferSt, 1, 10, false))
                            return ItemStack.EMPTY;
                    }
                    slot.onSlotChange(transferSt, stack);
                } else {
                    if (!mergeItemStack(transferSt, 0, 1, false)) {
                        if (index < 10 && !mergeItemStack(transferSt, 10, inventorySlots.size(), false))
                            return ItemStack.EMPTY;
                        else if (!mergeItemStack(transferSt, 1, 10, false))
                            return ItemStack.EMPTY;
                    }
                    slot.onSlotChange(transferSt, stack);
                }
            }
        }
        return stack;
    }
}
