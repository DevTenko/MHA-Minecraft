package devtenko.mhaminecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import java.util.HashMap;


public final class MHAMinecraft extends JavaPlugin {
    private boolean begin = false;
    private event_handler eventHandler;
    private config_manager configManager;
    private world_border world_border_object;
    protected HashMap<Player,Object> player_quirks = new HashMap<Player, Object>();

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
        String text = "";
        for(Player p:getServer().getOnlinePlayers()) {
             text = p.getDisplayName() + " has "+ getConfig().getInt("Points.Team_"+player_quirks.get(p).team_id)+ " points ";
            eventHandler.discord_handler_object.send_message(text);
        }
        super.onDisable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(sender.isOp()){
                if(args.length <= 0){
                    if(command.getName().equalsIgnoreCase("start")){
                        return start_game(sender);
                    }
                    else if(command.getName().equalsIgnoreCase("points")){
                        for(Player pl: getServer().getOnlinePlayers()){
                            sender.sendMessage(pl.getDisplayName() + " Has " + getConfig().getInt("Points.Team_"+player_quirks.get(pl).team_id) + " Points ");
                        }
                        return true;
                    }
                }
                else{
                    if(command.getName().equalsIgnoreCase("discord_token")){
                        return update_token(args);
                    }
                    else if(command.getName().equalsIgnoreCase("discord_text_channel")){
                        return update_text_channel(args);
                    }
                    if(command.getName().equalsIgnoreCase("switch")){
                        return update_quirk(sender,args);
                    }
                    else if(command.getName().equalsIgnoreCase("team")){
                        return update_team(sender,args);
                    }
                }
            }
            else{
                if(args.length > 0){
                    if(command.getName().equalsIgnoreCase("switch")){
                        return update_quirk(sender,args);
                    }
                    else if(command.getName().equalsIgnoreCase("team")){
                        return update_team(sender,args);
                    }
                }
                else if(args.length <= 0){
                    if(command.getName().equalsIgnoreCase("points")){
                        for(Player pl: getServer().getOnlinePlayers()){
                            sender.sendMessage(pl.getDisplayName() + " Has " + getConfig().getInt("Points.Team_"+player_quirks.get(pl).team_id) + " Points ");
                        }
                        return true;
                    }
                }
            }
        }
        else{
            if(sender.isOp()){
                if(args.length <= 0){
                    if(command.getName().equalsIgnoreCase("start")){
                        return start_game(sender);
                    }
                }
                else if(args.length >= 1){
                    if(command.getName().equalsIgnoreCase("discord_token")){
                        return update_token(args);
                    }
                    else if(command.getName().equalsIgnoreCase("discord_text_channel")){
                        return update_text_channel(args);
                    }
                }
            }
        }
        return false;
    }
    private boolean start_game(CommandSender sender){
        world_border_object = new world_border(this);

        begin = true;
        for(Player p : getServer().getOnlinePlayers()) {
            player_quirks.put(p,new quirk_class(this,p,getConfig().getString("Class."+p.getDisplayName()),getConfig().getInt("Team."+p.getDisplayName())));
            p.getInventory().clear();
            p.setHealth(20);
            p.setFoodLevel(20);
            p.teleport(new Location(p.getWorld(), 0, p.getWorld().getHighestBlockYAt(0, 0) + 2, 0));
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
            update_scoreboard(p,player_quirks.get(p).quirk_time);
        }
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for(Player p: getServer().getOnlinePlayers()){
                    if(player_quirks.get(p).quirk_time >= 0){
                        update_scoreboard(p,player_quirks.get(p).quirk_time);
                        player_quirks.get(p).quirk_time = player_quirks.get(p).quirk_time - 1;
                    }
                }
            }
        },0,20);
        eventHandler = new event_handler(this);
        return true;
    }
    private boolean update_token(String[] args){
        getConfig().set("Discord.Token",args[0]);
        saveConfig();
        return true;
    }
    private boolean update_text_channel(String[] args){
        getConfig().set("Discord.Text_Channel",Long.parseLong(args[0]));
        saveConfig();
        return true;
    }
    private boolean update_quirk(CommandSender sender, String[] args){
        Player p = (Player) sender;
        this.getConfig().set("Class."+p.getDisplayName(), (args[0]));
        this.saveConfig();
        p.sendMessage("Your Quirk has been switched");
        return true;
    }
    private boolean update_team(CommandSender sender, String[] args){
        Player p = (Player) sender;
        this.getConfig().set("Team."+p.getDisplayName(), Integer.parseInt(args[0]));
        this.saveConfig();
        p.sendMessage("Your Team has been switched");
        return true;
    }
    private void update_scoreboard(Player p, int time_till_next_quirk){
            String time_left = String.valueOf(time_till_next_quirk);
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective =scoreboard.registerNewObjective("test", "dummy", ChatColor.RED + "Time Till Quirk");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            Score score = objective.getScore(ChatColor.RED + "Time Till Quirk: " + time_left);
            score.setScore(1);
            p.setScoreboard(scoreboard);
    }
}
