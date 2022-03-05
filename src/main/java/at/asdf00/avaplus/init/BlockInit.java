package at.asdf00.avaplus.init;

import at.asdf00.avaplus.objects.blocks.BlockBase;
import at.asdf00.avaplus.objects.blocks.BlockReplicator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

public class BlockInit {
    public static final List<Block> BLOCK_LIST = new ArrayList<>();

    public static final Block BLOCK_TEST = new BlockBase("block_test", Material.IRON);
    //public static final Block REPLICATOR = new BlockReplicator("replicator");
}
