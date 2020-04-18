package devtenko.mhaminecraft;

import org.bukkit.entity.Player;

public class config_manager {
    MHAMinecraft plugin;
    public config_manager(MHAMinecraft plugin) {
        this.plugin = plugin;
        if (plugin.getConfig().contains("Class") && plugin.getConfig().contains("Timer") && plugin.getConfig().contains("World") && plugin.getConfig().contains("Discord")) {
            return;
        } else {
            plugin.getConfig().set("Class.Hobo_Bleach", "Erasure");
            plugin.getConfig().set("Timer",7);
            plugin.getConfig().set("World.Normal","world");
            plugin.getConfig().set("World.End","world_the_end");
            plugin.getConfig().set("World.Nether","world_nether");
            plugin.getConfig().set("Discord.Token","0");
            plugin.getConfig().set("Discord.Text_Channel",0);
            plugin.getConfig().set("Discord.Guild_ID",0);
            plugin.saveConfig();
        }
    }
}
