package devtenko.mhaminecraft;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class quirk_object {
    String name;
    String ability;
    Player attacked_player;
    Integer time_till_next_quirk = 0;
    boolean player_hit_player = false;
    Action action_event;
    MHAMinecraft plugin;
    event_handler eventHandler;
    Material attack_weapon;
    public quirk_object(MHAMinecraft plugin,event_handler event_handler,String name,String ability, int last_activation){
        this.name = name;
        this.plugin = plugin;
        this.ability = ability;
        this.time_till_next_quirk = last_activation;
        this.eventHandler = event_handler;
    }

    public void activate(){
        Player p = plugin.getServer().getPlayer(name);
        if(p.getInventory().getItemInMainHand().getType() != Material.STICK && p.getInventory().getItemInMainHand().getType() != Material.WOODEN_HOE)return;
        attack_weapon = p.getInventory().getItemInMainHand().getType();
        if(time_till_next_quirk > 0)return;
        if(ability.equalsIgnoreCase("Overhaul")){

            if(action_event.equals(Action.LEFT_CLICK_BLOCK) && attack_weapon.equals(Material.STICK)){
                int x_coordinate = (int) p.getLocation().getX();
                int y_coordinate = (int) p.getLocation().getY();
                int z_coordinate = (int) p.getLocation().getZ();
                for(int x = -5; x < 5; x++){
                    for(int z = -5; z <5;z++){
                        for(int y = -5; y < 5; y++){
                            if(!(x == 1 && z ==1)){
                                p.getWorld().getBlockAt(x_coordinate -x,y_coordinate- y,z_coordinate-z).breakNaturally();
                            }
                        }
                    }
                }
            }
            else if(action_event.equals(null) && attacked_player != null && attack_weapon.equals(Material.STICK)){
                attacked_player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1,3));
                player_hit_player = false;
            }
            time_till_next_quirk = 7;
        }
        else if(ability.equalsIgnoreCase("Compress")){
            System.out.println("4");
            if(player_hit_player && attacked_player != null && (plugin.getConfig().getString("Compress."+p.getDisplayName()) == null || plugin.getConfig().getString("Compress."+p.getDisplayName()) == "")&& attack_weapon.equals(Material.STICK)){
                System.out.println("5");
                attacked_player.teleport(new Location(p.getWorld(),1000,1000,1000));
                attacked_player.setGameMode(GameMode.SPECTATOR);
                plugin.getConfig().set("Compress."+p.getDisplayName(), attacked_player.getDisplayName());
                plugin.saveConfig();
                player_hit_player = false;
            }
            else if((plugin.getConfig().getString("Compress."+p.getDisplayName()) != null || plugin.getConfig().getString("Compress."+p.getDisplayName()) != "")&& attack_weapon.equals(Material.STICK)){
                System.out.println("6");
                plugin.getServer().getPlayer(plugin.getConfig().getString("Compress."+p.getDisplayName())).teleport(p.getLocation());
                plugin.getServer().getPlayer(plugin.getConfig().getString("Compress."+p.getDisplayName())).setGameMode(GameMode.SURVIVAL);
                plugin.getConfig().set("Compress."+p.getDisplayName(),"");
                plugin.saveConfig();
                player_hit_player = false;
            }
            time_till_next_quirk = 7;
        }
        else if(ability.equalsIgnoreCase("Gravity")){
            if((player_hit_player == true && attacked_player != null)&& attack_weapon.equals(Material.STICK)){
                attacked_player.teleport(p.getLocation().add(0,10,0));
                player_hit_player = false;
            }
            else if(((action_event.equals(Action.RIGHT_CLICK_AIR) || action_event.equals(Action.RIGHT_CLICK_BLOCK))&& attack_weapon.equals(Material.STICK))&& attack_weapon.equals(Material.STICK)){
                p.teleport(p.getLocation().add(0,100,0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,1,25));
            }
            time_till_next_quirk = 7;
        }
        else if(ability.equalsIgnoreCase("Permeation")){
            if((action_event.equals(Action.RIGHT_CLICK_AIR) || action_event.equals(Action.RIGHT_CLICK_BLOCK))&& attack_weapon.equals(Material.STICK)){
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,10000,10000));
                p.setGameMode(GameMode.SPECTATOR);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        p.setGameMode(GameMode.SURVIVAL);
                        p.removePotionEffect(PotionEffectType.BLINDNESS);
                        time_till_next_quirk = 7;
                    }
                },80);
            }
        }
        else if(ability.equalsIgnoreCase("Warp")){
            if((action_event.equals(Action.RIGHT_CLICK_AIR) || action_event.equals(Action.RIGHT_CLICK_BLOCK))&& attack_weapon.equals(Material.STICK)){
                Random random = new Random();
                int new_location = (random.nextInt(100) -50);
                int new_x = (int) (p.getLocation().getX()- new_location);
                int new_z = (int) (p.getLocation().getZ()- new_location);
                p.teleport(new Location(p.getWorld(), new_x, p.getWorld().getHighestBlockAt(new_x,new_z).getY(), new_z));
            }
            else if((action_event.equals(Action.RIGHT_CLICK_AIR) || action_event.equals(Action.RIGHT_CLICK_BLOCK))&& attack_weapon.equals(Material.WOODEN_HOE)){
                Random random = new Random();
                int y = random.nextInt(75);
                int x = random.nextInt(60) -30;
                int z = random.nextInt(60) -30;
                if(p.getWorld() == p.getServer().getWorld(plugin.getConfig().getString("World.Normal"))) {
                    if(plugin.getServer().getWorld(plugin.getConfig().getString("World.Nether")).getBlockAt(x, y,z).getType() == Material.AIR ){
                        p.teleport(new Location(plugin.getServer().getWorld(plugin.getConfig().getString("World.Nether")),x, y,z));
                        p.getWorld().getBlockAt(x,y-3,z).setType(Material.COBBLESTONE);
                        this.time_till_next_quirk = 7;
                    }
                    else{
                        this.time_till_next_quirk = 0;
                    }
                }
                else if(p.getWorld() == p.getServer().getWorld(plugin.getConfig().getString("World.Nether"))){
                    p.teleport(plugin.getServer().getWorld(plugin.getConfig().getString("World.End")).getHighestBlockAt(x, z).getLocation());
                    this.time_till_next_quirk = 7;
                }
                else if(p.getWorld() == p.getServer().getWorld(plugin.getConfig().getString("World.End"))){
                    p.teleport(plugin.getServer().getWorld(plugin.getConfig().getString("World.Normal")).getHighestBlockAt(x, z).getLocation());
                    this.time_till_next_quirk = 7;
                }
            }
            else if((player_hit_player == true && attacked_player != null)&& attack_weapon.equals(Material.STICK)){
                Random random = new Random();
                int new_location = (random.nextInt(100) -50);
                attacked_player.teleport(new Location(p.getWorld(), p.getLocation().getX()- new_location, p.getWorld().getHighestBlockAt(((int) p.getLocation().getX() - new_location), (int) (p.getLocation().getZ()- new_location)).getY(),p.getLocation().getZ()));
                player_hit_player = false;
            }
            time_till_next_quirk = 7;
        }
        else if(ability.equalsIgnoreCase("Eraser")){
            if(player_hit_player == true && attacked_player != null && attack_weapon.equals(Material.STICK)){
                eventHandler.player_quirks.get(attacked_player).time_till_next_quirk = eventHandler.player_quirks.get(attacked_player).time_till_next_quirk + 20;
                p.sendMessage("Quirk Erased");
                attacked_player.sendMessage("Your quirk has been erased");
                time_till_next_quirk = 7;
                player_hit_player = false;
            }
        }

        else if(ability.equalsIgnoreCase("Explosion")){
            if((action_event.equals(Action.RIGHT_CLICK_BLOCK) || action_event.equals(Action.RIGHT_CLICK_AIR))&& attack_weapon.equals(Material.STICK)){
                p.getWorld().spawn(p.getLocation(), TNTPrimed.class).setVelocity(p.getLocation().getDirection().multiply(2));
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        p.getWorld().spawn(p.getLocation(), TNTPrimed.class).setVelocity(p.getLocation().getDirection().multiply(2));
                        time_till_next_quirk = 7;
                    }
                },10);
            }
        }
    }
}