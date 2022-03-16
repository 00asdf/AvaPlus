package at.asdf00.avaplus.objects.blocks;

import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.References;
import at.asdf00.avaplus.init.BlockInit;
import at.asdf00.avaplus.objects.blocks.amogust2.TileEntityAmogusT2;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockAmogusT2 extends BlockAmogus {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockAmogusT2(String name) {
        super(name);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
        setHardness(6);
    }

    public static void setState(Boolean active, EnumFacing facing, World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        worldIn.setBlockState(pos, BlockInit.AMOGUST2.getDefaultState()
                .withProperty(FACING, facing == null ? state.getValue(FACING) : facing)
                .withProperty(ACTIVE, active == null ? state.getValue(ACTIVE) : active), 3);
        if(tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            playerIn.openGui(Main.instance, References.AMOGUIT2, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            EnumFacing face = state.getValue(FACING);
            setState(null, face, worldIn, pos);
        }
    }
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityAmogusT2();
    }
}
