package at.asdf00.avaplus;

import at.asdf00.avaplus.util.CustomConfig;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ModConfig {
    public static CustomConfig conf;

    public static long AMOGUS_RFTOREPLICATE = 40_000_000_000L;
    public static long AMOGUST2_RFTOREPLICATE = 60_000_000_000L;

    public static void init(File file) {
        conf = new CustomConfig(file);
        String category;

        category = "singularity_replicator";
        conf.addCustomCategoryComment(category, "Configure the replicator");
        AMOGUS_RFTOREPLICATE = conf.getLong("RF_TO_REPLICATE", category, 40_000_000_000L, 1, Long.MAX_VALUE, "Set the energy needed to replicate a singularity");

        category = "replicator_tier2";
        conf.addCustomCategoryComment(category, "Configure the replicator tier 2");
        AMOGUST2_RFTOREPLICATE = conf.getLong("RF_TO_REPLICATE_T2", category, 60_000_000_000L, 1, Long.MAX_VALUE, "Set the energy needed to replicate any item");
    }
    public static void registerConfig(FMLPreInitializationEvent e) {
        Main.configFile = new File(e.getModConfigurationDirectory() + "/" + References.MODID);
        Main.configFile.mkdirs();
        init(new File(Main.configFile.getPath(), References.MODID + ".cfg"));
    }
}