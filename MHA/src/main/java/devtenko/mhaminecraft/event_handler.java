package devtenko.mhaminecraft;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class event_handler {
	private main plugin;
	private timer timer;
	private world_border worldBorder;
	protected event_handler(main plugin, timer t, world_border wb) {
		this.plugin = plugin;
		this.timer = t;
		this.worldBorder = wb;
	}
	
	protected void onEntityDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			
		}
		return;
	}
	
	protected void onPlayerMove(PlayerMoveEvent e) {
		
	}
}
