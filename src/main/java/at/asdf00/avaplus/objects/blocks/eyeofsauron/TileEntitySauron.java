package at.asdf00.avaplus.objects.blocks.eyeofsauron;

import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.ModConfig;
import at.asdf00.avaplus.objects.blocks.BlockSauron;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;

public class TileEntitySauron extends TileEntity implements ITickable {
    public final ISHSauron handler;
    protected final EnergyStorageSauron storage;
    protected int matterStored;

    protected int coyoteTime = 0;

    public TileEntitySauron() {
        handler = new ISHSauron(1);
        storage = new EnergyStorageSauron();
    }

    public static boolean isValidInput(ItemStack stack) {
        Item item = stack.getItem();
        if (item.getRegistryName().equals("avaritia:resource"))
            Main.logger.info("resource metadata = " + stack.getMetadata());
        return (item.getRegistryName().equals("avaritia:resource") || item.getRegistryName().equals("avaritia:block_resource"));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        if(capability == CapabilityEnergy.ENERGY)
            return true;
        return super.hasCapability(capability, facing);
    }
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) handler;
        if(capability == CapabilityEnergy.ENERGY)
            return (T) storage;
        return super.getCapability(capability, facing);
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("Input", handler.serializeNBT());
        compound.setInteger("MatterStored", matterStored);
        storage.writeToNBT(compound);
        return compound;
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        handler.deserializeNBT(compound.getCompoundTag("Input"));
        matterStored = compound.getInteger("MatterStored");
        storage.readFromNBT(compound);
    }
    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("container.black_hole_generator.name");
    }

    @Override
    public void update() {
        // consume items
        ItemStack stack = handler.getStackInSlot(0);
        if (!stack.isEmpty() &&
                matterStored < ModConfig.SAURON_MATTER_CAPACITY &&
                isValidInput(stack)) {
            matterStored += getMatterYield(stack);
            stack.shrink(1);
        }
        // generate energy
        if (matterStored > 0) {
            if (coyoteTime == 0)
                BlockSauron.setState(false, null, world, pos);
            coyoteTime = 15;

            int produced = (int)(Integer.MAX_VALUE * ((float)matterStored / (float)ModConfig.SAURON_MATTER_CAPACITY));
            storage.forceReceiveEnergy(produced, false);
            matterStored--;
        } else {
            if (coyoteTime > 1)
                coyoteTime--;
            else if (coyoteTime == 1) {
                BlockSauron.setState(true, null, world, pos);
                coyoteTime = 0;
            }
        }
    }
    protected long getMatterYield(ItemStack stack) {
        if (stack.getItem().getRegistryName().equals("avaritia:resource")) {
            // sort by meta data
            return ModConfig.SAURON_YIELD_IINGOT;
        } else if (stack.getItem().getRegistryName().equals("avartia:block_resource") /* && meta filter */)
            return ModConfig.SAURON_YIELD_IBLOCK;
        return 0;
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }
}