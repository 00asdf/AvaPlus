package at.asdf00.avaplus.objects.blocks;

import at.asdf00.avaplus.init.BlockInit;
import at.asdf00.avaplus.objects.blocks.eyeofsauron.TileEntitySauron;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSauron extends BlockBase {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool EMPTY = PropertyBool.create("empty");

    public BlockSauron(String name) {
        super(name, Material.IRON);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(EMPTY, true));
        setHardness(10);
    }

    public static void setState(Boolean empty, EnumFacing facing, World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity te = worldIn.getTileEntity(pos);
        worldIn.setBlockState(pos, BlockInit.EYEOFSAURON.getDefaultState()
                .withProperty(FACING, facing == null ? state.getValue(FACING) : facing)
                .withProperty(EMPTY, empty == null ? state.getValue(EMPTY) : empty), 3);
        if (te != null) {
            te.validate();
            worldIn.setTileEntity(pos, te);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, EMPTY);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(FACING).getIndex() - 2) + (state.getValue(EMPTY) ? 4 : 0);
        // meta masks: direction &3, empty &4
    }
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getFront((meta & 3) + 2);
        if (facing.getAxis() == EnumFacing.Axis.Y)
            facing = EnumFacing.NORTH;
        return getDefaultState().withProperty(FACING, facing).withProperty(EMPTY, (meta & 4) == 4);
        // meta masks: direction &3, empty &4
    }
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return hasTileEntity();
    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean hasTileEntity() {
        return true;
    }
    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntitySauron();
    }
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote)
            setState(null, state.getValue(FACING), worldIn, pos);
    }
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntitySauron te = (TileEntitySauron)worldIn.getTileEntity(pos);
        worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.handler.getStackInSlot(0)));
        super.breakBlock(worldIn, pos, state);
    }
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }
}
