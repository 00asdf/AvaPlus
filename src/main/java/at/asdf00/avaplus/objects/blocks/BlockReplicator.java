package at.asdf00.avaplus.objects.blocks;

import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.References;
import at.asdf00.avaplus.init.BlockInit;
import at.asdf00.avaplus.objects.blocks.tileentities.TileEntityReplicator;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockReplicator extends BlockBase implements ITileEntityProvider {

    // properties
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    // contructor
    public BlockReplicator(String name) {
        super(name, Material.IRON);
        setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE, false));
    }


    // just stuff
    /*
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(BlockInit.REPLICATOR);
    }
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(BlockInit.REPLICATOR);
    } */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            playerIn.openGui(Main.instance, References.GUI_REPLICATOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityReplicator();
    }
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState();
    }
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, this.getDefaultState(), 2);
    }
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {ACTIVE});
    }
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityReplicator tileentity = (TileEntityReplicator)worldIn.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(worldIn, pos, tileentity);
        super.breakBlock(worldIn, pos, state);
    }

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
