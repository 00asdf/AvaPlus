package at.asdf00.avaplus;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.SidedProxy;

import at.asdf00.avaplus.proxy.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, dependencies = References.DEPENDENCIES, useMetadata = true)
public class Main {

    private static Logger logger;


    @Instance
    public static Main instance;

    @SidedProxy(clientSide = References.CLIENT, serverSide = References.SERVER)
    public static CommonProxy proxy;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
    }
    @EventHandler
    public static void init(FMLInitializationEvent e) {

    }
    @EventHandler
    public static void postInit(FMLPostInitializationEvent e) {

    }
}
