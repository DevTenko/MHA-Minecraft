package devtenko.mhaminecraft;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

public class event_handler implements Listener {
    MHAMinecraft plugin;
    discord_handler discord_handler_object;
    BukkitScheduler scheduler;
    HashMap<Player, Integer> team_id_list = new HashMap<Player, Integer>();

    public event_handler(MHAMinecraft plugin) {
        this.plugin = plugin;
        try {
            discord_handler_object = new discord_handler(plugin.getConfig().getString("Discord.Token"), plugin.getConfig().getLong("Discord.Text_Channel"));
            discord_handler_object.send_message("Game has Begun");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bukkit.getPluginManager().registerEvents(this, plugin);
        scheduler = plugin.getServer().getScheduler();
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (!team_id_list.containsKey(p)) {
                team_id_list.put(p, plugin.player_quirks.get(p).team_id);
            }
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (e.getEntity() instanceof Player) {
                if(plugin.player_quirks.get(((Player) e.getEntity()).getPlayer()).quirk_time > 0)return;
                if (plugin.player_quirks.get(((Player) e.getDamager()).getPlayer()).team_id == plugin.player_quirks.get(((Player) e.getEntity()).getPlayer()).team_id) {
                    e.setCancelled(true);
                    return;
                }
                plugin.player_quirks.get(((Player) e.getDamager()).getPlayer()).attacked_player = ((Player) e.getEntity()).getPlayer();
            } else {
                if(plugin.player_quirks.get(((Player) e.getEntity()).getPlayer()).quirk_time > 0)return;
                plugin.player_quirks.get(((Player) e.getDamager()).getPlayer()).attacked_player = null;
            }
            plugin.player_quirks.get(((Player) e.getDamager()).getPlayer()).activate();
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (plugin.player_quirks.get(e.getPlayer()).quirk_name.equalsIgnoreCase("Permeation")) {
            e.setTo(e.getPlayer().getLocation());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(plugin.player_quirks.get(e.getPlayer()).quirk_time <=0) {
            plugin.player_quirks.get(e.getPlayer()).player_action = e.getAction();
            plugin.player_quirks.get(e.getPlayer()).activate();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        team_id_list.remove(e.getEntity().getPlayer());
        if(e.getEntity().getKiller() instanceof Player){
            Player killer = e.getEntity().getKiller();
            plugin.getConfig().set("Points.Team_"+plugin.player_quirks.get(killer).team_id,plugin.getConfig().getInt("Points.Team_"+plugin.player_quirks.get(killer).team_id)  + 1);
            killer.setHealth(killer.getHealth() + 10);
        }
        if (!team_id_list.containsValue(e.getEntity().getPlayer())) {
            if (team_id_list.values().size() == 2) {
                plugin.getConfig().set("Points.Team_" + plugin.player_quirks.get(e.getEntity().getPlayer()).team_id, plugin.getConfig().getInt("Points.Team_" + plugin.player_quirks.get(e.getEntity().getPlayer()).team_id) + 2);
            } else if (team_id_list.values().size() == 1) {
                plugin.getConfig().set("Points.Team_" + plugin.player_quirks.get(e.getEntity().getPlayer()).team_id, plugin.getConfig().getInt("Points.Team_" + plugin.player_quirks.get(e.getEntity().getPlayer()).team_id) + 3);
            } else if (team_id_list.values().size() == 0) {
                plugin.getConfig().set("Points.Team_" + plugin.player_quirks.get(e.getEntity().getPlayer()).team_id, plugin.getConfig().getInt("Points.Team_" + plugin.player_quirks.get(e.getEntity().getPlayer()).team_id) + 5);
            }
            plugin.saveConfig();
        }
        e.getEntity().getPlayer().setGameMode(GameMode.SPECTATOR);
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if(plugin.player_quirks.get(e.getPlayer()).paralyze == true){
            e.setCancelled(true);
        }
    }
}