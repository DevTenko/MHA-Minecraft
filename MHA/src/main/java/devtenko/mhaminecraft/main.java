package devtenko.mhaminecraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin{
	protected world_border worldBorder;
	protected timer timer;
	@Override
	public void onEnable() {
		this.worldBorder = new world_border(this);
		this.timer = new timer(this,worldBorder);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.isOp()) {
			
		}
		else {
			if(label.equalsIgnoreCase("start")) {
				this.worldBorder.setCenter();
				this.getServer().broadcastMessage("World Border Center has been set to 0,0");
				this.timer.start();
				this.getServer().broadcastMessage("World Border will shrink in 10 minutes");
			}
		}
		return false;
	}
}
