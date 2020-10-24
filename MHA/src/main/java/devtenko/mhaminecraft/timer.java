package devtenko.mhaminecraft;

import org.bukkit.scheduler.BukkitRunnable;

public class timer {
	protected main plugin;
	protected world_border worldBorder;
	protected int clockKeeper = 0;
	protected timer(main plugin, world_border wb) {
		this.plugin = plugin;
		this.worldBorder = wb;
	}
	
	protected void start() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				clockKeeper = clockKeeper + 2;
				if(clockKeeper - 6000 == 0) {
					
				}
			}
		}.runTaskTimer(plugin, 0, 40);
	}
}
