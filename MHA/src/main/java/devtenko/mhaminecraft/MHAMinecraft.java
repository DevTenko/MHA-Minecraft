package devtenko.mhaminecraft;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;


public final class MHAMinecraft extends JavaPlugin {
    private boolean begin = false;
    private event_handler eventHandler;
    private config_manager configManager;
    private  world_border world_border_object;
    @Override
    public void onEnable() {
        configManager = new config_manager(this);
        if(begin == false) {
            this.getServer().getWorld("world").setPVP(false);
        }
        getCommand("switch").setTabCompleter(new tab_complete());
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
                for(Player p : getServer().getOnlinePlayers()){
                    p.getInventory().clear();
                    p.setHealth(20);
                    p.setFoodLevel(20);
                    p.teleport(new Location(p.getWorld(),0,p.getWorld().getHighestBlockYAt(0,0)+2,0));
                }
                world_border_object = new world_border(this);
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
            else if(sender instanceof  Player && command.getName().equalsIgnoreCase("discord_token")&& args[0] != null){
                getConfig().set("Discord.Token",args[0]);
                saveConfig();
                return true;
            }
            else if(sender instanceof  Player && command.getName().equalsIgnoreCase("discord_text_channel")&& args[0] != null){
                getConfig().set("Discord.Text_Channel",Long.parseLong(args[0]));
                saveConfig();
                return true;
            }
            else if(sender instanceof  Player && command.getName().equalsIgnoreCase("discord_guild_id")&& args[0] != null){
                getConfig().set("Discord.Guild_ID",Long.parseLong(args[0]));
                saveConfig();
                return true;
            }
            else if(command.getName().equalsIgnoreCase("switch") && begin == false && args[0] != null){
                Player p = (Player) sender;
                this.getConfig().set("Class."+p.getDisplayName(), (args[0]));
                this.saveConfig();
                p.sendMessage("Your Quirk has been switched");
                return true;
            }
            else if(command.getName().equalsIgnoreCase("team") && begin == false && args[0] != null){
                Player p = (Player) sender;
                this.getConfig().set("Team."+p.getDisplayName(), Integer.parseInt(args[0]));
                this.saveConfig();
                p.sendMessage("Your Team has been switched");
                return true;
            }
            return false;
        }
        return false;
    }
}
