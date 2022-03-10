package at.asdf00.avaplus.objects.blocks;


import at.asdf00.avaplus.Main;
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
    private int rfConsumed;

    public ContainerAmogus(InventoryPlayer player, TileEntityAmogus tileentity) {
        this.tileentity = tileentity;
        IItemHandler handler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // replicator slots
        addSlotToContainer(new SlotHandlerAmogusIn(handler, 0, 44, 21));
        addSlotToContainer(new SlotHandlerAmogusOut(handler, 1, 97, 36));

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
        return this.tileentity.isUsableByPlayer(playerIn);
    }

    @Override
    public void updateProgressBar(int id, int data) {
        this.tileentity.setField(id, data);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for(int i = 0; i < listeners.size(); ++i) {
            IContainerListener listener = (IContainerListener)this.listeners.get(i);
            if(rfConsumed != tileentity.getField(0)) listener.sendWindowProperty(this, 0, tileentity.getField(0));
        }
        rfConsumed = tileentity.getField(0);
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
                    if (!mergeItemStack(transferSt, 0, 1, false))
                        return ItemStack.EMPTY;
                    slot.onSlotChange(transferSt, stack);
                } else {
                    return ItemStack.EMPTY;
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