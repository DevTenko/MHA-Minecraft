package devtenko.mhaminecraft;

public class config_manager {
    MHAMinecraft plugin;
    public config_manager(MHAMinecraft plugin) {
        this.plugin = plugin;
        if (plugin.getConfig().contains("Class") && plugin.getConfig().contains("Timer")) {
            return;
        } else {
            plugin.getConfig().set("Class.Hobo_Bleach", "Erasure");
            plugin.getConfig().set("Timer",7);
            plugin.saveConfig();
        }
    }
}
