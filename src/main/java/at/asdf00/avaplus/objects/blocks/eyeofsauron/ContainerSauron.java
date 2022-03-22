package at.asdf00.avaplus.objects.blocks.eyeofsauron;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.CapabilityItemHandler;

public class ContainerSauron extends Container {
    protected final InventoryPlayer player;
    protected final TileEntitySauron tileentity;

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
}
