package at.asdf00.avaplus.objects.blocks.eyeofsauron;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStorageSauron implements IEnergyStorage {

    protected long rfContained;

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }
    public long forceReceiveEnergy(long maxReceive, boolean simulate) {
        long free = Math.max(Long.MAX_VALUE - rfContained, 0);
        long received = Math.min(maxReceive, free);
        received = Math.max(received, 0);
        if (!simulate)
            rfContained += received;
        return received;
    }
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;
        int extracted = Math.min(maxExtract, getEnergyStored());
        if (!simulate)
            rfContained -= extracted;
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
    @Override
    public boolean canExtract() {
        return true;
    }
    @Override
    public boolean canReceive() {
        return false;
    }
}
