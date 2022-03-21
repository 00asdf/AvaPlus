package at.asdf00.avaplus.objects.blocks;

import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.References;
import at.asdf00.avaplus.init.BlockInit;
import at.asdf00.avaplus.objects.blocks.amogus.TileEntityAmogus;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAmogus extends BlockBase {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockAmogus(String name) {
        super(name, Material.IRON);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
        setHardness(6);
    }

    public static void setState(Boolean active, EnumFacing facing, World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        worldIn.setBlockState(pos, BlockInit.AMOGUS.getDefaultState()
                .withProperty(FACING, facing == null ? state.getValue(FACING) : facing)
                .withProperty(ACTIVE, active == null ? state.getValue(ACTIVE) : active), 3);
        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            playerIn.openGui(Main.instance, References.AMOGUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote)
            setState(null, state.getValue(FACING), worldIn, pos);
    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean hasTileEntity() {
        return true;
    }
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return hasTileEntity();
    }
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityAmogus();
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
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityAmogus tileentity = (TileEntityAmogus)worldIn.getTileEntity(pos);
        worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.handlerIn.getStackInSlot(0)));
        worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.handlerOut.getStackInSlot(0)));
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
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE,FACING);
    }
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getFront((meta & 3) + 2);
        if(facing.getAxis() == EnumFacing.Axis.Y)
            facing = EnumFacing.NORTH;
        return getDefaultState().withProperty(FACING, facing).withProperty(ACTIVE, (meta & 4) == 4);
        // meta masks: direction &3, active &4
    }
    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(FACING).getIndex() - 2) + (state.getValue(ACTIVE) ? 4 : 0);
        // meta masks: direction &3, active &4
    }
}
