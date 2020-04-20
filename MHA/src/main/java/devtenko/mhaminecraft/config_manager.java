package devtenko.mhaminecraft;

import org.bukkit.entity.Player;

public class config_manager {
    MHAMinecraft plugin;
    public config_manager(MHAMinecraft plugin) {
        this.plugin = plugin;
        if (plugin.getConfig().contains("Class") && plugin.getConfig().contains("Timer") && plugin.getConfig().contains("World") && plugin.getConfig().contains("Discord") && plugin.getConfig().contains("Delay") && plugin.getConfig().contains("Gamemode") && plugin.getConfig().contains("Points")) {
            return;
        } else {
            plugin.getConfig().set("Class.Hobo_Bleach", "Eraser");
            plugin.getConfig().set("Timer",7);
            plugin.getConfig().set("World.Normal","world");
            plugin.getConfig().set("World.End","world_the_end");
            plugin.getConfig().set("World.Nether","world_nether");
            plugin.getConfig().set("Discord.Token","0");
            plugin.getConfig().set("Discord.Text_Channel",0);
            plugin.getConfig().set("Delay.Overhaul",7);
            plugin.getConfig().set("Delay.Gravity",7);
            plugin.getConfig().set("Delay.Compress",7);
            plugin.getConfig().set("Delay.Warp",7);
            plugin.getConfig().set("Delay.Explosion",7);
            plugin.getConfig().set("Delay.Permeation",7);
            plugin.getConfig().set("Delay.Eraser",7);
            plugin.getConfig().set("Points.Hobo_Bleach",0);
            plugin.getConfig().set("Gamemode","Solo");
            plugin.saveConfig();
        }
    }
}