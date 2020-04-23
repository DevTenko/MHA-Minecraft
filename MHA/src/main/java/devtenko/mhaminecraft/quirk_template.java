package devtenko.mhaminecraft;


import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedList;

public class quirk_template {
    Player player, attacked_player;
    float health_boost,absorption_boost,damage_boost,speed_boost = 1.0f;
    boolean quirk_toggle = true;
    boolean quirk_running = false;
    MHAMinecraft plugin;

    protected void switch_toggle(){
        quirk_toggle = !quirk_toggle;
    }

    protected void switch_running(){
        quirk_running = !quirk_running;
    }
}
class Overhaul extends quirk_template{

}
class Compress extends quirk_template{
    LinkedList<Player> compressed_player = new LinkedList<Player>();

    protected void compress(){
        compressed_player.add(attacked_player);
        attacked_player.setGameMode(GameMode.SPECTATOR);
        attacked_player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,10000,1090000))
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                compressed_player.remove(attacked_player);
                attacked_player.teleport(player.getLocation());
                attacked_player.setGameMode(GameMode.SURVIVAL);
                attacked_player.removePotionEffect(PotionEffectType.BLINDNESS);
            }
        },300);
    }

    protected void release(){
        for(int i = 0; i < compressed_player.size(); i++){
            compressed_player.get(i).teleport(player);
            compressed_player.get(i).setGameMode(GameMode.SURVIVAL);
            compressed_player.get(i).removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }
}
class Gravity extends quirk_template{
    LinkedList<Player> floating_player = new LinkedList<Player>();
    protected void player_float(){
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000,5));
    }
    protected void enemy_float(){
        attacked_player.teleport(player.getLocation().add(0,50,0));
        attacked_player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100000000,1000000));
        floating_player.add(attacked_player);
    }
    protected void release(){
        for(int i = 0; i < floating_player.size(); i++){
            floating_player.get(i).removePotionEffect(PotionEffectType.SLOW_FALLING);
        }
    }
}
class StrongArm extends quirk_template{

}
class Eraser extends quirk_template{

}
class Permeation extends quirk_template{

}
class Warp extends quirk_template{

}
class Paralyze extends quirk_template{

}
class Half_Half extends quirk_template{

}
class Absorption extends quirk_template{

}
class Chronostasus extends quirk_template{

}