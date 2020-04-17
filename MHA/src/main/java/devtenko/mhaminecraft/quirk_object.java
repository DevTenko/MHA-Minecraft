package devtenko.mhaminecraft;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class quirk_object {
    String name;
    String ability;
    Entity attacked_player;
    MHAMinecraft plugin;
    public LinkedList<String> player_deactivated_quirks = new LinkedList<String>();
    public quirk_object(MHAMinecraft plugin,String name,String ability){
        this.name = name;
        this.plugin = plugin;
        this.ability = ability;
    }

    public void activate(int x, int y,int z){
        Player p = plugin.getServer().getPlayer(name);
            if (ability.equalsIgnoreCase("Overhaul")) {
                Overhaul(p);
            } else if (ability.equalsIgnoreCase("Hardening")) {
                Hardening(p);
            } else if (attacked_player != null && ability.equalsIgnoreCase("Erasure")) {
                player_deactivated_quirks =  Erasure(p,attacked_player);
            } else if (ability.equalsIgnoreCase("Decay")) {
                Decay(p);
            } else if (ability.equalsIgnoreCase("Explosion")) {
                Explosion(p);
            } else if (ability.equalsIgnoreCase("Warp")) {
                Warp(p, x, y, z);
            }
            else if (ability.equalsIgnoreCase("Hell_Flame")) {
                hell_flame(p);
            }
            else if (attacked_player != null && ability.equalsIgnoreCase("Paralyze")) {
                Player c_player = (Player) attacked_player;
                Paralyze(c_player);
                attacked_player = null;
            }
            else if (ability.equalsIgnoreCase("Gravity")) {
                gravity(p);
            }
            else if (ability.equalsIgnoreCase("Compress")) {
                compress(p,attacked_player);
            }
            else {
                return;
            }
    }
    private void Overhaul(Player p){
        LinkedList<Integer> coords = get_player_coordinates(p);
        for(int z = -5; z < 5; z++){
            for(int x = -5; x < 5; x++){
                for(int y = -5; y < 5;y++){
                    p.getWorld().getBlockAt(coords.get(0) - x,coords.get(1) - y,coords.get(2)-z).breakNaturally();
                }
            }
        }
        p.teleport(new Location(p.getWorld(),coords.get(0),1+p.getWorld().getHighestBlockYAt(coords.get(0),coords.get(2)),coords.get(2)));
    }

    private void Hardening(Player p) {
        ItemStack diamond_helmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack diamond_chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack diamond_leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack diamond_boots = new ItemStack(Material.DIAMOND_BOOTS);
        diamond_helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1);
        diamond_chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1);
        diamond_leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1);
        diamond_boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1);
        p.getInventory().setBoots(diamond_boots);
        p.getInventory().setChestplate(diamond_chestplate);
        p.getInventory().setHelmet(diamond_helmet);
        p.getInventory().setLeggings(diamond_leggings);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.getInventory().setBoots(null);
                p.getInventory().setChestplate(null);
                p.getInventory().setHelmet(null);
                p.getInventory().setLeggings(null);
            }
        }, 140);
    }

    private LinkedList<String> Erasure(Player p, Entity target){
       LinkedList<String> player_list = new LinkedList<String>();
           if (target instanceof Player) {
              Player temp_player = ((Player) target).getPlayer();
              player_list.add(temp_player.getDisplayName());
       }
       return player_list;
    }

    private void Decay(Player p){
        LinkedList<Integer> coords =get_player_coordinates(p);
        for(int z = -10; z < 10; z++){
            for(int x = -10; x < 10; x++){
                for(int y = -10; y < 10;y++){
                    p.getWorld().getBlockAt(coords.get(0)-x,coords.get(1)-y,coords.get(2)-z).setType(Material.AIR);
                }
            }
        }
        p.teleport(new Location(p.getWorld(),coords.get(0),1+p.getWorld().getHighestBlockYAt(coords.get(0),coords.get(2)),coords.get(2)));
    }

    private void Explosion(Player p) {
        Location current_position = p.getLocation();
        current_position.getWorld().spawn(current_position.add(p.getLocation().getDirection()), TNTPrimed.class).setVelocity(p.getLocation().getDirection().normalize().multiply(2));
        get_scheduler(plugin).scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                current_position.getWorld().spawn(current_position.add(p.getLocation().getDirection()), TNTPrimed.class).setVelocity(p.getLocation().getDirection().normalize().multiply(2));
            }
        },10);
    }

    private void Warp(Player p, int x,int y, int z){
        p.teleport(new Location(p.getWorld(),x,y,z));
    }

    private void Paralyze(Player hit){
        if(hit == null) return;
        Random random = new Random();
        int slow_time = (random.nextInt(20) - 10) * 20;
        hit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,slow_time,5000));
    }

    private void hell_flame(Player p){
        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,1000));
        LinkedList<Integer> coordinates = get_player_coordinates(p);
        for(int x = -5; x < 5;x++){
            for(int z = -5; z< 5;z++){
                p.getWorld().getBlockAt(coordinates.get(0) - x,p.getWorld().getHighestBlockYAt(coordinates.get(0) -x,coordinates.get(2)-z),coordinates.get(2) - z).setType(Material.FIRE);
            }
        }
    }

    private void gravity(Player p){
        List<Entity> entity = p.getNearbyEntities(5,5,5);
        for(int i =0; i < entity.size(); i++){
            entity.get(i).teleport(new Location(p.getWorld(),entity.get(i).getLocation().getX(),entity.get(i).getLocation().getY() + 10,entity.get(i).getLocation().getZ()));
        }
    }

    private void compress(Player p, Entity attacked){
        if(plugin.getConfig().getString("Compress."+p.getDisplayName()) == null || plugin.getConfig().getString("Compress."+p.getDisplayName()) == ""){
            if(!(attacked instanceof Player)){
                return;
            }
            ((Player) attacked).getPlayer().teleport(new Location(p.getWorld(),1000,1000,1000));
            ((Player) attacked).getPlayer().setGameMode(GameMode.SPECTATOR);
            plugin.getConfig().set("Compress."+p.getDisplayName(),((Player) attacked).getPlayer().getDisplayName());
            plugin.saveConfig();
        }
        else if(plugin.getConfig().getString("Compress."+p.getDisplayName()) != null || plugin.getConfig().getString("Compress."+p.getDisplayName()) != ""){
            plugin.getServer().getPlayer(plugin.getConfig().getString("Compress."+p.getDisplayName())).teleport(new Location(p.getWorld(),p.getLocation().getX(),p.getLocation().getY(),p.getLocation().getZ()));
            plugin.getServer().getPlayer(plugin.getConfig().getString("Compress."+p.getDisplayName())).setGameMode(GameMode.SURVIVAL);
            plugin.getConfig().set("Compress."+p.getDisplayName(),"");
            plugin.saveConfig();
        }
        else{
            return;
        }
    }

    private LinkedList<Integer> get_player_coordinates(Player p){
        LinkedList<Integer> coordinates_list = new LinkedList<Integer>();
        coordinates_list.add((int) p.getLocation().getX());
        coordinates_list.add((int) p.getLocation().getY());
        coordinates_list.add((int) p.getLocation().getZ());
        return coordinates_list;
    }
    private BukkitScheduler get_scheduler(MHAMinecraft plugin){
        return(plugin.getServer().getScheduler());
    }
}