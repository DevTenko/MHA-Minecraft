package devtenko.mhaminecraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class event_handler implements Listener {

    public event_handler(MHAMinecraft plugin){
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
    }

    @EventHandler
    public void onPlayerAttack(PlayerDeathEvent e){

    }
}
