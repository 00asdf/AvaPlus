package at.asdf00.avaplus;

import at.asdf00.avaplus.util.handlers.RegistryHandler;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.SidedProxy;

import at.asdf00.avaplus.proxy.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, dependencies = References.DEPENDENCIES, useMetadata = true)
public class Main {

    // will be private later
    public static Logger logger;


    @Instance
    public static Main instance;

    public static File configFile;

    @SidedProxy(clientSide = References.CLIENT, serverSide = References.SERVER)
    public static CommonProxy proxy;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
        RegistryHandler.preInitRegistries(e);
    }
    @EventHandler
    public static void init(FMLInitializationEvent e) {
        RegistryHandler.initRegistries(e);
    }
    @EventHandler
    public static void postInit(FMLPostInitializationEvent e) {

    }
}
