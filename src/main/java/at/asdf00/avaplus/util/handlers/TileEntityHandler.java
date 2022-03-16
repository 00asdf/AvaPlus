package at.asdf00.avaplus.util.handlers;

import at.asdf00.avaplus.References;
import at.asdf00.avaplus.objects.blocks.amogus.TileEntityAmogus;
import at.asdf00.avaplus.objects.blocks.amogust2.TileEntityAmogusT2;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {
    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityAmogus.class, new ResourceLocation(References.MODID + ":replicator"));
        GameRegistry.registerTileEntity(TileEntityAmogusT2.class, new ResourceLocation(References.MODID + ":replicator_t2"));
    }
}
