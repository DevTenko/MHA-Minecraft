package devtenko.mhaminecraft;

public class config_manager {
    MHAMinecraft plugin;
    public config_manager(MHAMinecraft plugin) {
        this.plugin = plugin;
        if (plugin.getConfig().contains("Class") && plugin.getConfig().contains("Timer") && plugin.getConfig().contains("World")) {
            return;
        } else {
            plugin.getConfig().set("Class.Hobo_Bleach", "Erasure");
            plugin.getConfig().set("Timer",7);
            plugin.getConfig().set("World.Normal","world");
            plugin.getConfig().set("World.End","world_the_end");
            plugin.getConfig().set("World.Nether","world_nether");
            plugin.saveConfig();
        }
    }
}
