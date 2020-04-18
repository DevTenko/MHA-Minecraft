package devtenko.mhaminecraft;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class world_border {
    MHAMinecraft plugin;
    public world_border(MHAMinecraft plugin){
        this.plugin = plugin;
        World normal =plugin.getServer().getWorld(plugin.getConfig().getString("World.Normal"));
        normal.getWorldBorder().setSize(2500);
        normal.getWorldBorder().setCenter(new Location(normal,0,0,0));
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if(normal.getWorldBorder().getSize() <= 25){
                    List<Player> playerList =plugin.getServer().getWorld(plugin.getConfig().getString("World.End")).getPlayers();
                    for(Player p : playerList){
                        p.teleport(new Location(normal,0, normal.getHighestBlockYAt(0,0),0));
                    }
                    playerList =plugin.getServer().getWorld(plugin.getConfig().getString("World.Nether")).getPlayers();
                    for(Player p : playerList){
                        p.teleport(new Location(normal,0, normal.getHighestBlockYAt(0,0),0));
                    }
                    plugin.getServer().unloadWorld(plugin.getConfig().getString("World.Nether"),true);
                    plugin.getServer().unloadWorld(plugin.getConfig().getString("World.End"),true);
                    plugin.getServer().broadcastMessage("Nether and The End has been Disabled");
                }
                plugin.getServer().broadcastMessage("Current World Border Size: " + normal.getWorldBorder().getSize());
                plugin.getServer().broadcastMessage("Updated World Border Size: " + normal.getWorldBorder().getSize() / 2);
                plugin.getServer().broadcastMessage("World Border is shrinking Head to 0,0");
                normal.getWorldBorder().setSize(normal.getWorldBorder().getSize()/2,240);
            }
        },6000,6000);
    }
}
