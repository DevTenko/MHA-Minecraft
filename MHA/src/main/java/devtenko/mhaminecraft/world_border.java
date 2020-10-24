package devtenko.mhaminecraft;

import org.bukkit.Location;
import org.bukkit.World;

public class world_border {
	private main plugin;
	private World world;
	protected world_border(main plugin) {
		this.plugin = plugin;
		try {
			this.world = plugin.getServer().getWorld(plugin.getConfig().getString("active_world"));
		}
		catch (Exception e) {
			System.out.print("!!!!World not Found!!!!");
		}
	}
	
	protected void shrink() {
		this.world.getWorldBorder().setSize((this.world.getWorldBorder().getSize() / 2) - 50);
	}
	protected void setCenter() {
		this.world.getWorldBorder().setCenter(new Location(world, 0,0, 0));
	}
}
