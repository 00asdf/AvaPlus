package at.asdf00.avaplus;

import at.asdf00.avaplus.util.ModLogger;
import at.asdf00.avaplus.util.handlers.RegistryHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.SidedProxy;

import at.asdf00.avaplus.proxy.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, dependencies = References.DEPENDENCIES, useMetadata = true)
public class Main {

    @Instance
    public static Main instance;

    public static File configFile;

    @SidedProxy(clientSide = References.CLIENT, serverSide = References.SERVER)
    public static CommonProxy proxy;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent e) {
        ModLogger.setLogger(e.getModLog());
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
