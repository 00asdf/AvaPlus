package at.asdf00.avaplus.util.handlers;

import at.asdf00.avaplus.References;
import at.asdf00.avaplus.objects.blocks.amogus.ContainerAmogus;
import at.asdf00.avaplus.objects.blocks.amogus.GuiAmogus;
import at.asdf00.avaplus.objects.blocks.amogus.TileEntityAmogus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID) {
            case References.AMOGUI:
                return new ContainerAmogus(player.inventory, (TileEntityAmogus) world.getTileEntity(new BlockPos(x,y,z)), true);
            case References.AMOGUIT2:
                return new ContainerAmogus(player.inventory, (TileEntityAmogus) world.getTileEntity(new BlockPos(x,y,z)), false);
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID) {
            case References.AMOGUI:
                return new GuiAmogus(player.inventory, (TileEntityAmogus) world.getTileEntity(new BlockPos(x,y,z)), true);
            case References.AMOGUIT2:
                return new GuiAmogus(player.inventory, (TileEntityAmogus) world.getTileEntity(new BlockPos(x,y,z)), false);
            default:
                return null;
        }
    }
}
