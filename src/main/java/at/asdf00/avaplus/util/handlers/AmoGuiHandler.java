package at.asdf00.avaplus.util.handlers;

import at.asdf00.avaplus.References;
import at.asdf00.avaplus.objects.blocks.ContainerAmogus;
import at.asdf00.avaplus.objects.blocks.guis.GuiAmogus;
import at.asdf00.avaplus.objects.blocks.tileentities.TileEntityAmogus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class AmoGuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if(ID == References.AMOGUI)
            return new ContainerAmogus(player.inventory, (TileEntityAmogus) world.getTileEntity(new BlockPos(x,y,z)));
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if(ID == References.AMOGUI)
            return new GuiAmogus(player.inventory, (TileEntityAmogus) world.getTileEntity(new BlockPos(x,y,z)));
        return null;
    }
}
