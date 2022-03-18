package at.asdf00.avaplus.objects.blocks.tileentities;

import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.References;
import at.asdf00.avaplus.objects.blocks.BlockAmogus;
import at.asdf00.avaplus.objects.blocks.StackHandlers.ISHAmogusIn;
import at.asdf00.avaplus.objects.blocks.StackHandlers.ISHAmogusOut;
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
    protected final EnergyStorageAmogus storage;
    public final ItemStackHandler handlerIn;
    public final ItemStackHandler handlerOut;
    protected String customName;

    public TileEntityAmogus() {
        storage = new EnergyStorageAmogus(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        handlerIn = new ISHAmogusIn(1);
        handlerOut = new ISHAmogusOut(1);
    }

    protected static final boolean _debugSelfFueling = References._DEBUGMODE;
    protected static final int _debugSeflFuelingAmount = Integer.MAX_VALUE >> 2;

    public long rfConsumed;
    public boolean active;

    protected int lastActive = 0;
    protected boolean thrownError = false;

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
        return new TextComponentTranslation("container.replicator");
    }

    @Override
    public void update() {
        if (_debugSelfFueling) {
            if (world.isBlockPowered(pos))
                storage.receiveEnergy(_debugSeflFuelingAmount, false);
        }

        // reset progress if empty (singularity can be swapped mid process
        if (handlerIn.getStackInSlot(0).isEmpty())
            rfConsumed = 0;

        // throw error if there is an invalid item in input slot
        // this should in theory never happen due to filters put in place
        if ((isValidInput(handlerIn.getStackInSlot(0)) || handlerIn.getStackInSlot(0).isEmpty()) && thrownError)
            thrownError = false;
        else if (!(isValidInput(handlerIn.getStackInSlot(0))  || handlerIn.getStackInSlot(0).isEmpty()) && !thrownError) {
            Main.logger.error("Replicator at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " has invalid item " + handlerIn.getStackInSlot(0).getItem().getRegistryName() + " in its input slot!");
            thrownError = true;
        }

        // only process singularity if there is space in output slot
        if (storage.forceExtractEnergy((int)Math.min(Integer.MAX_VALUE, getRfToReplicate() - rfConsumed), true) > 0 &&
                !handlerIn.getStackInSlot(0).isEmpty() &&
                isValidInput(handlerIn.getStackInSlot(0)) &&
                handlerOut.getStackInSlot(0).getCount() < handlerOut.getStackInSlot(0).getMaxStackSize() - 1 &&
                (handlerOut.getStackInSlot(0).isEmpty() || handlerIn.getStackInSlot(0).isItemEqual(handlerOut.getStackInSlot(0)))) {
            active = true;
            // setting active blockstate
            if (lastActive == 0)
                BlockAmogus.setState(true, null, world, pos);
            lastActive = 15;
            rfConsumed += storage.forceExtractEnergy((int)Math.min(Integer.MAX_VALUE, getRfToReplicate() - rfConsumed), false);
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
        if (rfConsumed >= getRfToReplicate() && handlerOut.getStackInSlot(0).getCount() < handlerOut.getStackInSlot(0).getMaxStackSize() - 1) {
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

        if (active)
            markDirty();
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }
    public double getProgress() {
        return (double)rfConsumed / (double)getRfToReplicate();
    }
    public long getRfConsumed() {
        return rfConsumed;
    }
    public short getRfConsumedScaled() {
        if (getRfToReplicate() <= Short.MAX_VALUE)
            return (short)rfConsumed;
        return (short)(getProgress() * (double)Short.MAX_VALUE);
    }
    public void setRfConsumedScaled(short value) {
        if (getRfToReplicate() <= Short.MAX_VALUE)
            rfConsumed = value;
        else
            rfConsumed = (long)(((double)value / (double)Short.MAX_VALUE) * getRfToReplicate());
    }

    // methods for ease of use in child classes
    public boolean isValidInput(ItemStack stack) {
        return staticIsValidInput(stack);
    }
    public static boolean staticIsValidInput(ItemStack stack) {
        return stack.getItem().getRegistryName().toString().equals("avaritia:singularity");
    }
    public long getRfToReplicate() {
        return ModConfig.AMOGUS_RFTOREPLICATE;
    }
}
