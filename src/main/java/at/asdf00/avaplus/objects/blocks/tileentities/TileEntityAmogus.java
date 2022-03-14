package at.asdf00.avaplus.objects.blocks.tileentities;

import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.References;
import at.asdf00.avaplus.objects.blocks.BlockAmogus;
import at.asdf00.avaplus.objects.blocks.StackHandlers.ISHout;
import at.asdf00.avaplus.objects.blocks.energy.EnergyStorageAmogus;
import at.asdf00.avaplus.util.ModConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class TileEntityAmogus extends TileEntity implements ITickable {
    private final EnergyStorageAmogus storage = new EnergyStorageAmogus(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    public ItemStackHandler handlerIn = new ItemStackHandler(1);
    public ItemStackHandler handlerOut = new ISHout(1);
    private String customName;

    private static final boolean _debugSelfFueling = References._DEBUGMODE;
    //public static final long rfToReplicate = (long)Integer.MAX_VALUE << 2;

    public long rfConsumed;
    public boolean active;

    private int lastActive = 0;

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
        if(capability == CapabilityEnergy.ENERGY) return true;
        return super.hasCapability(capability, facing);
    }
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        // top: in/out input slot
        // other sides: out output slot
        // player: both slots combined
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == null)
                return (T) new CombinedInvWrapper(handlerIn, handlerOut);
            else if (facing == EnumFacing.UP)
                return (T) handlerIn;
            else
                return (T) handlerOut;
        }
        if(capability == CapabilityEnergy.ENERGY)
            return (T)storage;
        return super.getCapability(capability, facing);
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("Input", handlerIn.serializeNBT());
        compound.setTag("Output", handlerOut.serializeNBT());
        compound.setLong("RfConsumed", rfConsumed);
        compound.setBoolean("Active", active);
        storage.writeToNBT(compound);
        compound.setString("Name", getDisplayName().toString());
        return compound;
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        handlerIn.deserializeNBT(compound.getCompoundTag("Input"));
        handlerOut.deserializeNBT(compound.getCompoundTag("Output"));
        rfConsumed = compound.getLong("RfConsumed");
        active = compound.getBoolean("Active");
        storage.readFromNBT(compound);
        customName = compound.getString("Name");
    }
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("container.amogus");
    }

    @Override
    public void update() {
        if (_debugSelfFueling) {
            if (world.isBlockPowered(pos))
                storage.receiveEnergy(Integer.MAX_VALUE >> 6, false);
            //Main.logger.info("energy stored: " + storage.getEnergyStored());
            //Main.logger.info("energy consumed: " + rfConsumed);
        }

        // reset progress if empty (singularity can be swapped mid process
        if (handlerIn.getStackInSlot(0).isEmpty()) {
            rfConsumed = 0;
        }
        // only process singularity if there is space in output slot
        if (storage.forceExtractEnergy((int)Math.min(Integer.MAX_VALUE, ModConfig.AMOGUS_RFTOREPLICATE - rfConsumed), true) > 0 &&
                handlerIn.getStackInSlot(0).getItem().getRegistryName().toString().equals("avaritia:singularity") &&
                handlerOut.getStackInSlot(0).getCount() < handlerOut.getStackInSlot(0).getMaxStackSize() - 1 &&
                (handlerOut.getStackInSlot(0).isEmpty() || handlerIn.getStackInSlot(0).isItemEqual(handlerOut.getStackInSlot(0)))) {
            active = true;
            // setting active blockstate
            if (lastActive == 0)
                BlockAmogus.setState(true, null, world, pos);
            lastActive = 15;
            rfConsumed += storage.forceExtractEnergy((int)Math.min(Integer.MAX_VALUE, ModConfig.AMOGUS_RFTOREPLICATE - rfConsumed), false);
        } else {
            active = false;
            // setting active blockstate (15 ticks delay when deactivating to reduce lag in case of fluctuating power)
            if (lastActive == 1) {
                BlockAmogus.setState(false, null, world, pos);
                lastActive = 0;
            } else if (lastActive > 1) {
                lastActive--;
            }
        }
        // replicate item
        if (rfConsumed >= ModConfig.AMOGUS_RFTOREPLICATE && handlerOut.getStackInSlot(0).getCount() < handlerOut.getStackInSlot(0).getMaxStackSize() - 1) {
            if (ItemStack.areItemsEqual(handlerIn.getStackInSlot(0), handlerOut.getStackInSlot(0))) {
                handlerOut.getStackInSlot(0).grow(2);
                handlerIn.getStackInSlot(0).shrink(1);
                rfConsumed = 0;
            } else if (handlerOut.getStackInSlot(0).isEmpty()) {
                handlerOut.setStackInSlot(0, handlerIn.getStackInSlot(0).copy());
                handlerOut.getStackInSlot(0).setCount(2);
                handlerIn.getStackInSlot(0).shrink(1);
                rfConsumed = 0;
            }
        }

        if (active) {
            markDirty();
        }
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }
    public double getProgress() {
        return (double)rfConsumed / (double)ModConfig.AMOGUS_RFTOREPLICATE;
    }
    public long getRfConsumed() {
        return rfConsumed;
    }
    public int getRfConsumedScaled() {
        if (ModConfig.AMOGUS_RFTOREPLICATE <= Integer.MAX_VALUE)
            return (int)rfConsumed;
        return (int)(getProgress() * Integer.MAX_VALUE);
    }
    public void setRfConsumedScaled(int value) {
        if (ModConfig.AMOGUS_RFTOREPLICATE <= Integer.MAX_VALUE)
            rfConsumed = value;
        else
            rfConsumed = (long)(((double)value / (double)Integer.MAX_VALUE) * ModConfig.AMOGUS_RFTOREPLICATE);
    }
}
