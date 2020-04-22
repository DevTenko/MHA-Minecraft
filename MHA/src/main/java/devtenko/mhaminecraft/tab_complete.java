package devtenko.mhaminecraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class tab_complete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1){
            List<String> valid_classes = new ArrayList<String>();
            valid_classes.add("Overhaul");
            valid_classes.add("Compress");
            valid_classes.add("Gravity");
            valid_classes.add("Warp");
            valid_classes.add("Eraser");
            valid_classes.add("Permeation");
            valid_classes.add("Explosion");
            valid_classes.add("Paralyze");
            valid_classes.add("Half_Half");
            valid_classes.add("FatGum");
            valid_classes.add("Rappa");
            return valid_classes;
        }
        return null;
    }
}
