package at.asdf00.avaplus.objects.blocks.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.io.Console;

public class TileEntityReplicator extends TileEntity implements ITickable {

    @Override
    public void update() {
        System.out.println("hihi, im ticking :DDD");
    }
}
