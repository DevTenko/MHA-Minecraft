package devtenko.mhaminecraft;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitScheduler;
import java.util.HashMap;
public class event_handler implements Listener {
    MHAMinecraft plugin;
    scoreboard_manager scoreboard_manager_object;
    discord_handler discord_handler_object;
    BukkitScheduler scheduler;
    public HashMap<Player,quirk_object> player_quirks = new HashMap<Player, quirk_object>();
    public event_handler(MHAMinecraft plugin) {
        this.plugin = plugin;
        try {
            discord_handler_object = new discord_handler(plugin.getConfig().getString("Discord.Token"),plugin.getConfig().getLong("Discord.Text_Channel"));
            discord_handler_object.send_message("Game has Begun");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Bukkit.getPluginManager().registerEvents(this,plugin);
        scoreboard_manager_object = new scoreboard_manager();
        scheduler = plugin.getServer().getScheduler();
        for(Player p : plugin.getServer().getOnlinePlayers()){
            player_quirks.put(p,new quirk_object(plugin,this,p.getDisplayName(),plugin.getConfig().getString("Class."+p.getDisplayName()),0,plugin.getConfig().getInt("Team."+p.getDisplayName())));
        }
        scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for(Player p : plugin.getServer().getOnlinePlayers()){
                    if(player_quirks.get(p).time_till_next_quirk > 0) {
                        player_quirks.get(p).time_till_next_quirk = player_quirks.get(p).time_till_next_quirk - 1;
                        scoreboard_manager_object.default_quirk(p,player_quirks.get(p).time_till_next_quirk);
                    }
                }
            }
        },0,20);
    }
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player && e.getDamager() instanceof  Player){
            Player attacked_player = ((Player) e.getEntity()).getPlayer();
            Player attacker_player = ((Player) e.getDamager()).getPlayer();
            if(player_quirks.get(attacker_player).team_id == 0 || player_quirks.get(attacked_player).team_id != player_quirks.get(attacker_player).team_id) {
                player_quirks.get(attacker_player).attacked_player = attacked_player;
                player_quirks.get(attacker_player).action_event = null;
                player_quirks.get(attacker_player).player_hit_player = true;
                player_quirks.get(attacker_player).activate();
            }
            else{
                e.setCancelled(true);
            }
                System.out.println("7");
        }
        else if(e.getDamager() instanceof  Player && player_quirks.get(e.getDamager()).ability.equalsIgnoreCase("Compress")){
            System.out.println("1");
            Player attacker_player;
            if(e.getEntity() instanceof Player){
                System.out.println("2");
                attacker_player = ((Player) e.getDamager()).getPlayer();
                player_quirks.get(attacker_player).attacked_player = (Player) e.getEntity();
                player_quirks.get(attacker_player).player_hit_player = true;
            }
            else{
                System.out.println("3");
                attacker_player = ((Player) e.getDamager()).getPlayer();
                player_quirks.get(attacker_player).attacked_player = null;
            }
            player_quirks.get(attacker_player).activate();
        }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){

        if(player_quirks.get(e.getPlayer()).ability.equalsIgnoreCase("Compress")){
            return;
        }
        player_quirks.get(e.getPlayer()).action_event = e.getAction();
        player_quirks.get(e.getPlayer()).activate();
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        if(player_quirks.get(e.getPlayer()).ability.equalsIgnoreCase("Permeation")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        discord_handler_object.send_message(e.getEntity().getDisplayName() + " has died");
        e.getEntity().getServer().broadcastMessage(e.getEntity().getDisplayName() +" has died at " + e.getEntity().getLocation().getX() + "," +e.getEntity().getLocation().getY() + "," +e.getEntity().getLocation().getZ());
        e.getEntity().getPlayer().setGameMode(GameMode.SPECTATOR);
    }
}