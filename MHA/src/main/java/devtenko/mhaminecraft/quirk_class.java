package devtenko.mhaminecraft;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

public class quirk_class {
    Player player;
    String quirk_name;
    int quirk_time;
    MHAMinecraft plugin;
    Player attacked_player = null;
    Action player_action;
    Random random;
    boolean paralyze = false;
    int team_id;
    public quirk_class(MHAMinecraft plugin, Player p, String quirk_name,int team_id){
        this.player = p;
        this.quirk_name = quirk_name;
        this.quirk_time = 0;
        this.plugin = plugin;
        this.team_id = team_id;
        this.random = new Random();
    }

    public void activate(){
        if((player.getItemInHand().getType().equals(Material.STICK) || player.getItemInHand().equals(Material.WOODEN_HOE))) {
            if (quirk_time > 0) return;
            if (quirk_name == null) return;
            if (player == null) return;
            if (quirk_name.equalsIgnoreCase("Overhaul")) {
                if (player.getItemInHand().getType() == Material.STICK) {
                    if (player_action.equals(Action.LEFT_CLICK_BLOCK)) {
                        int x_coordinate = (int) player.getLocation().getX();
                        int y_coordinate = (int) player.getLocation().getY();
                        int z_coordinate = (int) player.getLocation().getZ();
                        for (int x = -5; x < 5; x++) {
                            for (int z = -5; z < 5; z++) {
                                for (int y = -5; y < 5; y++) {
                                    if (!(x == 0 && z == 0)) {
                                        player.getWorld().getBlockAt(x_coordinate - x, y_coordinate - y, z_coordinate - z).breakNaturally();
                                    }
                                }
                            }
                        }
                        quirk_time = plugin.getConfig().getInt("Delay.Overhaul");
                    } else if (attacked_player != null) {
                        attacked_player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1));
                        quirk_time = plugin.getConfig().getInt("Delay.Overhaul");
                    }
                }
            } else if (quirk_name.equalsIgnoreCase("compress")) {
                if (player.getItemInHand().getType() == Material.STICK) {
                    if (attacked_player != null) {
                        if (plugin.getConfig().getString("Compress." + player.getDisplayName()) != null && plugin.getConfig().getString("Compress." + player.getDisplayName()) != "") {
                            Player trapped_player = plugin.getServer().getPlayer(plugin.getConfig().getString("Compress." + player.getDisplayName()));
                            trapped_player.teleport(player.getLocation());
                            trapped_player.setGameMode(GameMode.SURVIVAL);
                            plugin.getConfig().set("Compress." + player.getDisplayName(), "");
                        } else {
                            attacked_player.teleport(new Location(player.getWorld(), 1000, 1000, 1000));
                            attacked_player.setGameMode(GameMode.SPECTATOR);
                            plugin.getConfig().set("Compress." + player.getDisplayName(), attacked_player.getDisplayName());
                        }
                        plugin.saveConfig();
                        quirk_time = plugin.getConfig().getInt("Delay.Compress");
                    } else {
                        if (plugin.getConfig().getString("Compress." + player.getDisplayName()) != null && plugin.getConfig().getString("Compress." + player.getDisplayName()) != "") {
                            Player trapped_player = plugin.getServer().getPlayer(plugin.getConfig().getString("Compress." + player.getDisplayName()));
                            trapped_player.teleport(player.getLocation());
                            trapped_player.setGameMode(GameMode.SURVIVAL);
                            plugin.getConfig().set("Compress." + player.getDisplayName(), "");
                            plugin.saveConfig();
                            quirk_time = plugin.getConfig().getInt("Delay.Compress");
                        }
                    }
                }
            } else if (quirk_name.equalsIgnoreCase("Gravity")) {
                if (player.getItemInHand().getType() == Material.STICK) {
                    if (attacked_player != null) {
                        attacked_player.teleport(player.getLocation().add(0, 10, 0));
                        quirk_time = plugin.getConfig().getInt("Delay.Gravity");
                    } else if (player_action.equals(Action.RIGHT_CLICK_BLOCK) || player_action.equals(Action.RIGHT_CLICK_AIR)) {
                        player.teleport(player.getLocation().add(0, 50, 0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 600, 2));
                        quirk_time = plugin.getConfig().getInt("Delay.Gravity");
                    }
                }
            } else if (quirk_name.equalsIgnoreCase("Eraser")) {
                if (player.getItemInHand().getType() == Material.STICK) {
                    if (attacked_player != null) {
                        plugin.player_quirks.get(attacked_player).quirk_time = plugin.player_quirks.get(attacked_player).quirk_time + 20;
                        quirk_time = plugin.getConfig().getInt("Delay.Eraser");
                    }
                }
            } else if (quirk_name.equalsIgnoreCase("Permeation")) {
                if (player.getItemInHand().getType() == Material.STICK) {
                    if (player_action.equals(Action.RIGHT_CLICK_AIR) || player_action.equals(Action.RIGHT_CLICK_BLOCK)) {
                        player.setFlySpeed(0.02f);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10000, 10000));
                        player.setGameMode(GameMode.SPECTATOR);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                player.setGameMode(GameMode.SURVIVAL);
                                player.removePotionEffect(PotionEffectType.BLINDNESS);
                                player.setFlySpeed(1f);
                                quirk_time = plugin.getConfig().getInt("Delay.Permeation");
                            }
                        }, 80);
                    }
                }
            } else if (quirk_name.equalsIgnoreCase("Warp")) {
                if (player.getItemInHand().getType() == Material.STICK) {
                    if (player_action.equals(Action.RIGHT_CLICK_AIR) || player_action.equals(Action.RIGHT_CLICK_BLOCK)) {
                        List<Entity> e=  player.getNearbyEntities(150,150,150);
                        for(Entity e1 : e){
                            if(e1 instanceof Player){
                                if(((Player) e1).getPlayer().getGameMode() == GameMode.SURVIVAL){
                                    player.teleport(e1.getLocation());
                                    quirk_time = plugin.getConfig().getInt("Delay.Warp");
                                    return;
                                }
                            }

                        }
                    } else if (attacked_player != null) {
                        Location new_location = player.getLocation().add((random.nextInt(100) - 50), (random.nextInt(100) - 50), (random.nextInt(100) - 50));
                        attacked_player.teleport(new Location(new_location.getWorld(), new_location.getX(), new_location.getWorld().getHighestBlockYAt(new_location), new_location.getZ()));
                        quirk_time = plugin.getConfig().getInt("Delay.Warp");
                    }
                } else if (player.getItemInHand().getType() == Material.WOODEN_HOE) {
                    if (player_action.equals(Action.RIGHT_CLICK_AIR) || player_action.equals(Action.RIGHT_CLICK_BLOCK)) {
                        World nether = plugin.getServer().getWorld(plugin.getConfig().getString("World.Nether"));
                        World overworld = plugin.getServer().getWorld(plugin.getConfig().getString("World.Normal"));
                        World end = plugin.getServer().getWorld(plugin.getConfig().getString("World.End"));
                        int new_x = random.nextInt(100) - 50;
                        int new_z = random.nextInt(100) - 50;
                        if (player.getWorld().equals(nether)) {
                            int new_y = end.getHighestBlockYAt(new_x, new_z);
                            end.getBlockAt(new_x, new_y - 2, new_z).setType(Material.STONE);
                            player.teleport(new Location(end, new_x, new_y, new_z));
                            quirk_time = plugin.getConfig().getInt("Delay.Warp");
                        } else if (player.getWorld().equals(overworld)) {
                            int new_y = nether.getHighestBlockYAt(new_x, new_z);
                            nether.getBlockAt(new_x, new_y - 2, new_z).setType(Material.STONE);
                            player.teleport(new Location(nether, new_x, new_y, new_z));
                            quirk_time = plugin.getConfig().getInt("Delay.Warp");
                        } else if (player.getWorld().equals(end)) {
                            int new_y = overworld.getHighestBlockYAt(new_x, new_z);
                            overworld.getBlockAt(new_x, new_y - 2, new_z).setType(Material.STONE);
                            player.teleport(new Location(overworld, new_x, new_y, new_z));
                            quirk_time = plugin.getConfig().getInt("Delay.Warp");
                        }
                    }
                }
                }
            else if (quirk_name.equalsIgnoreCase("Explosion")) {
                if (player_action.equals(Action.LEFT_CLICK_BLOCK) || player_action.equals(Action.LEFT_CLICK_AIR)) {
                    player.getWorld().spawn(player.getLocation(), TNTPrimed.class).setVelocity(player.getLocation().getDirection().multiply(2));
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            player.getWorld().spawn(player.getLocation(), TNTPrimed.class).setVelocity(player.getLocation().getDirection().multiply(2));
                            quirk_time = plugin.getConfig().getInt("Delay.Explosion");
                        }
                    }, 10);
                }
            }
            else if(quirk_name.equalsIgnoreCase("Paralyze")){
                if(attacked_player != null){
                    plugin.player_quirks.get(attacked_player).paralyze = true;
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            plugin.player_quirks.get(attacked_player).paralyze = false;
                            quirk_time = plugin.getConfig().getInt("Delay.Paralyze");
                        }
                    },100);
                }
            }
        }
    }
}
