package at.asdf00.avaplus.objects.blocks.tileentities;

import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.objects.blocks.BlockAmogus;
import at.asdf00.avaplus.objects.blocks.energy.CustomEnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAmogus extends TileEntity implements ITickable {
    private CustomEnergyStorage storage = new CustomEnergyStorage(75000, 20, 0, 0);
    public ItemStackHandler handler = new ItemStackHandler(2);
    private String customName;

    private static final boolean _debugSelfFueling = true;
    public static final long rfToReplicate = Integer.MAX_VALUE;

    public int energy = storage.getEnergyStored();
    public long rfConsumed;
    public boolean active;

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
        if(capability == CapabilityEnergy.ENERGY) return true;
        return super.hasCapability(capability, facing);
    }
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) handler;
        if(capability == CapabilityEnergy.ENERGY) return (T)storage;
        return super.getCapability(capability, facing);
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("Inventory", handler.serializeNBT());
        compound.setLong("RfConsumed", rfConsumed);
        compound.setBoolean("Active", active);
        storage.writeToNBT(compound);
        compound.setString("Name", getDisplayName().toString());
        return compound;
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        handler.deserializeNBT(compound.getCompoundTag("Inventory"));
        storage.readFromNBT(compound);
        rfConsumed = compound.getLong("RfConsumed");
        active = compound.getBoolean("Active");
        if(compound.hasKey("Name")) this.customName = compound.getString("Name");
    }
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("container.amogus");
    }

    @Override
    public void update() {
        if (_debugSelfFueling && world.isBlockPowered(pos)) {
            energy += Math.min(energy + Integer.MAX_VALUE >> 8, Integer.MAX_VALUE);
        }
        if (energy > 0 &&
                handler.getStackInSlot(0).isItemEqual(new ItemStack(Item.getByNameOrId("avaritia:singularity"))) &&
                handler.getStackInSlot(1).getCount() < handler.getSlotLimit(1) - 1) {
            active = true;
            rfConsumed += energy;
            energy = 0;
        } else {
            active = false;
        }
        if (rfConsumed >= rfToReplicate) {
            if (ItemStack.areItemsEqual(handler.getStackInSlot(0), handler.getStackInSlot(1))) {
                handler.getStackInSlot(1).grow(2);
                handler.getStackInSlot(0).shrink(1);
            } else if (handler.getStackInSlot(1).isEmpty()) {
                handler.setStackInSlot(1, handler.getStackInSlot(0).copy());
                handler.getStackInSlot(1).grow(1);
                handler.getStackInSlot(0).shrink(1);
            }
        }

        if (active) {
            markDirty();
        }
    }



    public int getEnergyStored() {
        return storage.getEnergyStored();
    }
    public int getMaxEnergyStored() {
        return storage.getMaxEnergyStored();
    }
    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }
    public int getField(int id) {
        if (id == 0)
            return (int) (rfConsumed >> 32);
        return -1;
    }
    public void setField(int id, int value) {
        if (id == 0)
            rfConsumed = value;
    }
}
