package at.asdf00.avaplus.objects.blocks;

import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.References;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockReplicator extends BlockBase {

    public BlockReplicator(String name) {
        super(name, Material.IRON);
    }

/*    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(!worldIn.isRemote)
        {
            playerIn.openGui(Main.instance, References.GUI_REPLICATOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    } */

    @Override
    public boolean hasTileEntity()
    {
        return true;
    }
    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
}
