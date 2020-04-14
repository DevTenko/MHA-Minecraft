package devtenko.mhaminecraft;

import java.util.LinkedList;

public class config_manager {
    MHAMinecraft plugin;
    public config_manager(MHAMinecraft plugin){
        this.plugin = plugin;
        try {
            plugin.getConfig();
        }
        catch (NullPointerException e){
            plugin.getConfig().set("Class.Hobo_Bleach", 0);
            plugin.saveDefaultConfig();
        }
    }

    public LinkedList getPlayerClass(){
        LinkedList list = (LinkedList) plugin.getConfig().get("Class");
        return list;
    }
}
