package at.asdf00.avaplus.objects.blocks;


import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.objects.blocks.tileentities.TileEntityAmogus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAmogus extends Container {
    private final TileEntityAmogus tileentity;
    private int rfConsumed, energy;

    public ContainerAmogus(InventoryPlayer player, TileEntityAmogus tileentity)
    {
        this.tileentity = tileentity;
        IItemHandler handler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        this.addSlotToContainer(new SlotItemHandler(handler, 0, 44, 21));
        this.addSlotToContainer(new SlotItemHandler(handler, 1, 97, 36));

        for(int y = 0; y < 3; y++)
        {
            for(int x = 0; x < 9; x++)
            {
                this.addSlotToContainer(new Slot(player, x + y*9 + 9, 8 + x*18, 84 + y*18));
            }
        }

        for(int x = 0; x < 9; x++)
        {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileentity.isUsableByPlayer(playerIn);
    }

    @Override
    public void updateProgressBar(int id, int data)
    {
        this.tileentity.setField(id, data);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for(int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener listener = (IContainerListener)this.listeners.get(i);

            if(rfConsumed != this.tileentity.getField(0)) listener.sendWindowProperty(this, 0, this.tileentity.getField(0));
            //if(energy != this.tileentity.getField(1)) listener.sendWindowProperty(this, 1, this.tileentity.getField(1));
        }

        this.rfConsumed = this.tileentity.getField(0);
        //this.energy = this.tileentity.getField(1);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            Main.logger.info("trying slot transfer");
            // TODO: transfer item to input slot and reject attempted transfers to output slot
        }
        /*
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack())
        {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if(index == 2)
            {
                if(!this.mergeItemStack(stack1, 4, 40, true)) return ItemStack.EMPTY;
                slot.onSlotChange(stack1, stack);
            }
            else if(index != 2 && index != 1 && index != 0)
            {
                Slot slot1 = (Slot)this.inventorySlots.get(index + 1);

                if(!SinteringFurnaceRecipes.getInstance().getSinteringResult(stack1, slot1.getStack()).isEmpty())
                {
                    if(!this.mergeItemStack(stack1, 0, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                    else if(index >= 4 && index < 31)
                    {
                        if(!this.mergeItemStack(stack1, 31, 40, false)) return ItemStack.EMPTY;
                    }
                    else if(index >= 31 && index < 40 && !this.mergeItemStack(stack1, 4, 31, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if(!this.mergeItemStack(stack1, 4, 40, false))
            {
                return ItemStack.EMPTY;
            }
            if(stack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();

            }
            if(stack1.getCount() == stack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, stack1);
        }
        return stack;
        */
        return ItemStack.EMPTY;
    }
}
