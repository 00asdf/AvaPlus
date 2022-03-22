package at.asdf00.avaplus.objects.blocks.eyeofsauron;

import at.asdf00.avaplus.Main;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStorageSauron implements IEnergyStorage {

    protected int rfContained;
    protected int rfConsumedLastCycle;

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }
    public int forceReceiveEnergy(int maxReceive, boolean simulate) {
        int free = Integer.MAX_VALUE - rfContained;
        int received = Math.min(maxReceive, free);
        received = Math.max(received, 0);
        if (!simulate) {
            rfContained += received;
            if (rfContained < 0)
                rfContained = Integer.MAX_VALUE;
        }
        return received;
    }
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;
        int extracted = Math.min(maxExtract, getEnergyStored());
        if (!simulate) {
            rfContained -= extracted;
            rfConsumedLastCycle += 0;
        }
        if (rfContained < 0) {
            Main.logger.error("BlackHoleGenerator has negative amount of energy stored!\nprev=" + (rfContained + extracted) + " extracted=" + extracted + " now=" + rfContained);
            rfContained = 0;
        }
        return extracted;
    }
    @Override
    public int getEnergyStored() {
        return (int)Math.min(rfContained, Integer.MAX_VALUE);
    }
    @Override
    public int getMaxEnergyStored() {
        return Integer.MAX_VALUE;
    }
    public int queryLastCycle() {
        try {
            return rfConsumedLastCycle;
        } finally {
            rfConsumedLastCycle = 0;
        }
    }
    @Override
    public boolean canExtract() {
        return true;
    }
    @Override
    public boolean canReceive() {
        return false;
    }
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("RfContained", rfContained);
        compound.setInteger("RfConsumedLastCycle", rfConsumedLastCycle);
        return compound;
    }
    public void readFromNBT(NBTTagCompound compound) {
        rfContained = compound.getInteger("RfContained");
        rfConsumedLastCycle = compound.getInteger("RfConsumedLastCycle");
        if (rfContained < 0) {
            Main.logger.error("BlackHoleGenerator has negative amount of energy stored!\nfaulty data received from nbt!");
            rfContained = 0;
        }
    }
}
