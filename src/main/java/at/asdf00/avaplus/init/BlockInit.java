package at.asdf00.avaplus.init;

import at.asdf00.avaplus.objects.blocks.BlockAmogus;
import at.asdf00.avaplus.objects.blocks.BlockAmogusT2;
import at.asdf00.avaplus.objects.blocks.BlockSauron;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockInit {
    public static final List<Block> BLOCK_LIST = new ArrayList<>();

    // public static final Block BLOCK_TEST = new BlockBase("block_test", Material.IRON);
    public static final Block AMOGUS = new BlockAmogus("replicator");
    public static final Block EYEOFSAURON = new BlockSauron("black_hole_generator");

    public static final Block AMOGUST2 = new BlockAmogusT2("replicator_t2");
}
