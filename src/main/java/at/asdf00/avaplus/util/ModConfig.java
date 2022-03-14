package at.asdf00.avaplus.util;

import at.asdf00.avaplus.Main;
import at.asdf00.avaplus.References;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ModConfig {
    public static CustomConfig conf;

    public static long AMOGUS_RFTOREPLICATE = 40_000_000_000l;

    public static void init(File file) {
        conf = new CustomConfig(file);
        String category;

        category = "singularity_replicator";
        conf.addCustomCategoryComment(category, "Configure the replicator");
        AMOGUS_RFTOREPLICATE = conf.getLong("RF_TO_REPLICATE", category, 40_000_000_000l, 1, Long.MAX_VALUE, "Set the energy needed to replicate a singularity");
    }
    public static void registerConfig(FMLPreInitializationEvent e) {
        Main.configFile = new File(e.getModConfigurationDirectory() + "/" + References.MODID);
        Main.configFile.mkdirs();
        init(new File(Main.configFile.getPath(), References.MODID + ".cfg"));
    }
}
