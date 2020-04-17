package devtenko.mhaminecraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;


public final class MHAMinecraft extends JavaPlugin {
    private boolean begin = false;
    private event_handler eventHandler;
    private config_manager configManager;
    @Override
    public void onEnable() {
        configManager = new config_manager(this);
        if(begin == false) {
            this.getServer().getWorld("world").setPVP(false);
        }
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("start") && sender.isOp()) {
                eventHandler = new event_handler(this);
                begin = true;

                int time_till_pvp = this.getConfig().getInt("Timer") * 20 * 60;
                BukkitScheduler scheduler = this.getServer().getScheduler();
                getServer().broadcastMessage("PvP Starts in " + this.getConfig().getInt("Timer") + " Minutes");
                scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                    @Override
                    public void run() {
                        getServer().broadcastMessage("PvP Starts in 3");
                    }
                },time_till_pvp-60);
                scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                    @Override
                    public void run() {
                        getServer().broadcastMessage("PvP Starts in 2");
                    }
                },time_till_pvp-40);
                scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                    @Override
                    public void run() {
                        getServer().broadcastMessage("PvP Starts in 1");
                        sender.getServer().getWorld("world").setPVP(true);
                    }
                },time_till_pvp-20);
                return true;
            }
            else if(command.getName().equalsIgnoreCase("switch") && begin == false && args[0] != null){
                Player p = (Player) sender;
                this.getConfig().set("Class."+p.getDisplayName(), args[0]);
                this.saveConfig();
                return true;
            }
            return false;
        }
        return false;
    }
}
