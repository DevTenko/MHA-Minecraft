package devtenko.mhaminecraft;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class MHAMinecraft extends JavaPlugin {
    private event_handler eventHandler;
    private HashMap<String,Integer> player_class;
    private config_manager configManager;
    @Override
    public void onEnable() {
        eventHandler = new event_handler(this);
        configManager = new config_manager(this);
        for(int i =0; i < configManager.getPlayerClass().size(); i++){
            player_class.entrySet(configManager.getPlayerClass().get(i)[0],);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
