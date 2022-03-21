package at.asdf00.avaplus.objects.blocks.amogust2;

import at.asdf00.avaplus.objects.blocks.BlockAmogusT2;
import at.asdf00.avaplus.objects.blocks.amogus.ISHAmogusOut;
import at.asdf00.avaplus.objects.blocks.amogus.TileEntityAmogus;
import at.asdf00.avaplus.objects.blocks.amogus.EnergyStorageAmogus;
import at.asdf00.avaplus.ModConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TileEntityAmogusT2 extends TileEntityAmogus {
    public TileEntityAmogusT2 () {
        storage = new EnergyStorageAmogus(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        handlerIn = new ISHAmogusT2In(1);
        handlerOut = new ISHAmogusOut(1);
    }

    public static boolean staticIsValidInput(ItemStack stack) {
        return true;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("container.replicator_t2.name");
    }
    @Override
    public boolean isValidInput(ItemStack stack) {
        return staticIsValidInput(stack);
    }
    @Override
    public long getRfToReplicate() {
        return ModConfig.AMOGUST2_RFTOREPLICATE;
    }
    @Override
    public void setBlockState(Boolean active, EnumFacing facing, World worldIn, BlockPos pos) {
        BlockAmogusT2.setState(active, facing, worldIn, pos);
    }
}
