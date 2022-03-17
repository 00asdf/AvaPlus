package at.asdf00.avaplus.util.handlers;

import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.init.BlockInit;
import at.asdf00.avaplus.init.ItemInit;
import at.asdf00.avaplus.util.IHasModel;
import at.asdf00.avaplus.ModConfig;
import javafx.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@EventBusSubscriber
public class RegistryHandler {
    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> e) {
        e.getRegistry().registerAll(ItemInit.ITEM_LIST.toArray(new Item[0]));
    }
    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> e) {
        e.getRegistry().registerAll(BlockInit.BLOCK_LIST.toArray(new Block[0]));
        TileEntityHandler.registerTileEntities();
    }
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent e) {
        for (Item item : ItemInit.ITEM_LIST) {
            if (item instanceof IHasModel) {
                ((IHasModel)item).registerModels();
            }
        }
        for (Block block : BlockInit.BLOCK_LIST) {
            if (block instanceof IHasModel) {
                ((IHasModel)block).registerModels();
            }
        }
    }

    public static void preInitRegistries(FMLPreInitializationEvent event) {
        ModConfig.registerConfig(event);
    }
    public static void initRegistries(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
    }
}
