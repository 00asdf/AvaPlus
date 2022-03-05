package at.asdf00.avaplus.objects.blocks.tileentities;

import at.asdf00.avaplus.Main;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.Console;

public class TileEntityReplicator extends TileEntity implements IInventory, ITickable {

    // major vars
    private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
    private String custName;
    private long rfConsumed;
    // util vars
    private boolean activeLastTick = false;

    private static long rfToReplicate = 1000000000;


    @Override
    public void update() {
        // TODO: tick replicator
    }

    @Override
    public String getName() {
        return hasCustomName() ? custName : "container.replicator";
    }
    @Override
    public boolean hasCustomName() {
        return custName != null && !custName.isEmpty();
    }
    public void setCustomName(String customName) {
        custName = customName;
    }
    @Override
    public ITextComponent getDisplayName() {
        return hasCustomName() ? new TextComponentString(getName()) : new TextComponentTranslation(getName());
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }
    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory)
            if (!stack.isEmpty())
                return false;
        return true;
    }
    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.get(index);
    }
    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(inventory, index, count);
    }
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(inventory, index);
    }
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itStack = inventory.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itStack) && ItemStack.areItemStackTagsEqual(stack, itStack);
        inventory.set(index, stack);
        if (stack.getCount() > getInventoryStackLimit())
            stack.setCount(getInventoryStackLimit());

        // TODO: input only in slot 0 and max count 1
        // TODO: set rfConsumed

        markDirty();
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setLong("RfConsumed", rfConsumed);
        ItemStackHelper.saveAllItems(compound, inventory);
        if(hasCustomName())
            compound.setString("CustomName", custName);
        return compound;
    }
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        inventory = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, inventory);
        rfConsumed = compound.getLong("RfConsumed");
        if (compound.hasKey("CustomName", 8))
            setCustomName(compound.getString("CustomName"));
    }
    @SideOnly(Side.CLIENT)
    public static boolean isRunning() {
        // TODO: static(!) check if running
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;

    }
    @Override
    public void openInventory(EntityPlayer player) { }
    @Override
    public void closeInventory(EntityPlayer player) { }
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == 0 && stack.isItemEqual(new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("avaritia:singularity"))));
    }
    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return (int) (rfConsumed / Integer.MAX_VALUE);
            case 1:
                return activeLastTick ? 1 : -1;
            default:
                return 0;
        }
    }
    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                rfConsumed = (long)value * Integer.MAX_VALUE;
            case 1:
                activeLastTick = value == 1;
        }
    }
    @Override
    public int getFieldCount() {
        return 2;
    }
    @Override
    public void clear() {
        inventory.clear();
    }
}
