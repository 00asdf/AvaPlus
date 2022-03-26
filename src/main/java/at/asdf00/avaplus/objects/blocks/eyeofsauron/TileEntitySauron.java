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
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.*;

public class TileEntitySauron extends TileEntity implements ITickable {
    public final ISHSauron handler;
    protected final EnergyStorage dummy;
    protected int matterStored;
    protected List<EnergyStorage> adjacentEnergy;

    protected int coyoteTime = 0;

    public TileEntitySauron() {
        handler = new ISHSauron(1);
        dummy = new EnergyStorage(0);
    }

    public static boolean isValidInput(ItemStack stack) {
        Item item = stack.getItem();
        if (item.getRegistryName().toString().equals("avaritia:resource"))
            return stack.getMetadata() == 5 || stack.getMetadata() == 6;
        return item.getRegistryName().toString().equals("avaritia:block_resource") && stack.getMetadata() == 1;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        if(capability == CapabilityEnergy.ENERGY)
            return true;
        return super.hasCapability(capability, facing);
    }
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) handler;
        if(capability == CapabilityEnergy.ENERGY)
            return (T) dummy;
        return super.getCapability(capability, facing);
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("Input", handler.serializeNBT());
        compound.setInteger("MatterStored", matterStored);
        return compound;
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        handler.deserializeNBT(compound.getCompoundTag("Input"));
        matterStored = compound.getInteger("MatterStored");
    }
    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("container.black_hole_generator.name");
    }
    @Override
    public void onLoad() {
        super.onLoad();
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
            int produced = getRfProduced();
            matterStored--;
            // push produced energy
            IEnergyStorage[] output = Arrays.stream(EnumFacing.values())
                    .map(f -> new Pair<>(f, world.getTileEntity(pos.offset(f))))
                    .filter(p -> p.getValue() != null)
                    .map(p -> p.getValue().getCapability(CapabilityEnergy.ENERGY, p.getKey().getOpposite()))
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(e -> e.receiveEnergy(Integer.MAX_VALUE, true)))
                    .toArray(IEnergyStorage[]::new);
            for (int i = 0; i < output.length; i++) {
                if (!world.isRemote)
                    Main.logger.info("pushed " + output[i].receiveEnergy(produced / (output.length - i), true) + "RF to adjacent block!");
                produced -= output[i].receiveEnergy(produced / (output.length - i), false);
            }
        } else {
            if (coyoteTime > 1)
                coyoteTime--;
            else if (coyoteTime == 1) {
                BlockSauron.setState(true, null, world, pos);
                coyoteTime = 0;
            }
        }
    }
    public long getMatterYield(ItemStack stack) {
        if (stack.getItem().getRegistryName().toString().equals("avaritia:resource")) {
            if (stack.getMetadata() == 5)
                return ModConfig.SAURON_YIELD_ICATALYST;
            else if (stack.getMetadata() == 6)
                return ModConfig.SAURON_YIELD_IINGOT;
        } else if (stack.getItem().getRegistryName().toString().equals("avaritia:block_resource") && stack.getMetadata() == 1)
            return ModConfig.SAURON_YIELD_IBLOCK;
        return 0;
    }
    public int getRfProduced() {
        return (int)(Integer.MAX_VALUE * ((float)matterStored / (float)ModConfig.SAURON_MATTER_CAPACITY));
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    // pair similar to javafx
    private class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }
        public V getValue() {
            return value;
        }
    }
}
