package at.asdf00.avaplus.init;

import at.asdf00.avaplus.objects.items.ItemBase;
import javafx.util.Pair;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

import java.util.LinkedList;
import java.util.List;

public class ItemInit {
    public static final List<Item> ITEM_LIST = new LinkedList<>();

    public static final Item INFINITY_SINGULARITY = new ItemBase("infinity_singularity", 64);
}
