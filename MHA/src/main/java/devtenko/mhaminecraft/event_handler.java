package devtenko.mhaminecraft;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class event_handler implements Listener {
    MHAMinecraft plugin;
    scoreboard_manager scoreboard_manager_object;
    private HashMap<Player,Long> last_quirk_use = new HashMap<Player, Long>();
    private LinkedList<String> disbled_quirk_player = new LinkedList<String>();
    private HashMap<Player,Integer> time_till_next_quirk = new HashMap<Player, Integer>();
    int erasure_timer = 20;
    public event_handler(MHAMinecraft plugin){
        for(int i = 0;i < plugin.getServer().getOnlinePlayers().size();i++){
            Player p = (Player) plugin.getServer().getOnlinePlayers().toArray()[i];
            last_quirk_use.put(p, (long) 0);
            time_till_next_quirk.put(p, 0);
        }
        Bukkit.getPluginManager().registerEvents(this,plugin);
        scoreboard_manager_object = new scoreboard_manager();
        this.plugin = plugin;
        new BukkitRunnable() {
            @Override
            public void run() {
                disbled_quirk_player = new LinkedList<String>();
                erasure_timer = 20;
            }
        }.runTaskTimer(plugin,0,400);

        new BukkitRunnable() {
            @Override
            public void run() {
                erasure_timer = erasure_timer - 1;
                Object[] online_players = plugin.getServer().getOnlinePlayers().toArray();
                for(int i = 0; i < online_players.length;i++){
                    Player p = (Player) online_players[i];
                        if(time_till_next_quirk.get(online_players[i]) <= 0){
                            return;
                        }
                        else if(time_till_next_quirk.containsKey(online_players[i]) && time_till_next_quirk.get(online_players[i]) >= 0){
                            time_till_next_quirk.replace((Player) online_players[i], time_till_next_quirk.get(online_players[i]) - 1);
                        }
                        else if(!(time_till_next_quirk.containsKey(online_players[i]))) {
                            time_till_next_quirk.put((Player) online_players[i], 7);
                        }

                    if(plugin.getConfig().getString("Class."+ p.getDisplayName()).equalsIgnoreCase( "Erasure")){
                        scoreboard_manager_object.erasure_scoreboard(p,erasure_timer);
                    }
                    else {
                        scoreboard_manager_object.defaul_quirk(p, time_till_next_quirk.get(p));
                    }
                }
            }
        }.runTaskTimer(plugin,0,20);
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        time_till_next_quirk.put(e.getPlayer(),7);
        if(!last_quirk_use.containsKey(player)){
            last_quirk_use.put(player, (long) 0);
        }
        Random random = new Random();
        int randomizer = random.nextInt(10);
        if(plugin.getConfig().get("Class."+player.getDisplayName()) != null){
            player.sendMessage("Your class is " +plugin.getConfig().get("Class."+player.getDisplayName()));
            return;
        }
        if(randomizer == 0){
            set_quirk(plugin,player,"Overhaul");
        }
        else if(randomizer == 1){
            set_quirk(plugin,player,"Decay");
        }
        else if(randomizer == 2){
            set_quirk(plugin,player,"Hardening");
        }
        else if(randomizer == 3){
            set_quirk(plugin,player,"Erasure");
        }
        else if(randomizer == 4){
            set_quirk(plugin,player,"Explosion");
        }
        else if(randomizer == 5){
            set_quirk(plugin,player,"Warp");
        }
        else if(randomizer == 6){
            set_quirk(plugin,player,"Paralyze");
        }
        else if(randomizer == 7){
            set_quirk(plugin,player,"Hell_Flame");
        }
        else if(randomizer == 8){
            set_quirk(plugin,player,"Gravity");
        }
        else if(randomizer == 9){
            set_quirk(plugin,player,"Compress");
        }
        else if(randomizer == 10){
            set_quirk(plugin,player,"Permeation");
        }
    }
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player){
            Entity attacked = e.getEntity();
            Player damager = (Player) e.getDamager();
            if(disbled_quirk_player.contains(damager.getDisplayName()) || last_quirk_use.get(damager) - System.currentTimeMillis() > -7000) return;
            ItemStack itemInHand = damager.getItemInHand();
            if(itemInHand.getType() == Material.STICK){
                String quirk = plugin.getConfig().getString("Class."+ damager.getDisplayName());
                if
                (quirk.equalsIgnoreCase( "Erasure")){
                    quirk_object damager_object = create_quirk(quirk,damager,attacked);
                    disbled_quirk_player = damager_object.player_deactivated_quirks;
                    last_quirk_use.replace(damager,System.currentTimeMillis());
                }
                else if
                (quirk.equalsIgnoreCase( "Warp")){
                    Random random_object = new Random();
                    Player teleport_player = (Player) e.getEntity();
                    int x = random_object.nextInt(100) -50;
                    int z = random_object.nextInt(50) -50;
                    int x_pos = (int) (damager.getLocation().getX() + x);
                    int z_pos = (int) (damager.getLocation().getZ() + z);
                    int y_pos = plugin.getServer().getWorld("world").getHighestBlockAt(x_pos,z_pos).getY() + 1;
                    quirk_object damager_object = new quirk_object(plugin, teleport_player.getDisplayName(),quirk);
                    damager_object.activate(x_pos,y_pos,z_pos);

                    last_quirk_use.replace(damager,System.currentTimeMillis());
                }
                else if(quirk.equalsIgnoreCase("Paralyze") || quirk.equalsIgnoreCase("Compress")){
                    create_quirk(quirk,damager,attacked);
                    last_quirk_use.replace(damager,System.currentTimeMillis());
                }
                check_delay(damager);
            }
        }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        String quirk = plugin.getConfig().getString("Class."+ e.getPlayer().getDisplayName());
        if(disbled_quirk_player.contains(e.getPlayer().getDisplayName()) || last_quirk_use.get(e.getPlayer()) - System.currentTimeMillis() > -7000)return;
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(e.getPlayer().getInventory().getItemInMainHand().getType() == (Material.STICK)){
                if(quirk.equalsIgnoreCase("Warp")) {
                    Random random_object = new Random();
                    Player teleport_player = e.getPlayer();
                    int x = random_object.nextInt(100) -50;
                    int z = random_object.nextInt(100) -50;
                    int x_pos = (int) (e.getPlayer().getLocation().getX() + x);
                    int z_pos = (int) (e.getPlayer().getLocation().getZ() + z);
                    int y_pos = plugin.getServer().getWorld("world").getHighestBlockAt(x_pos,z_pos).getY() + 1;
                    quirk_object damager_object = new quirk_object(plugin, teleport_player.getDisplayName(),quirk);
                    damager_object.activate(x_pos,y_pos,z_pos);
                    last_quirk_use.replace(e.getPlayer(), System.currentTimeMillis());
                    check_delay(e.getPlayer());
                }
                else if(quirk.equalsIgnoreCase("Permeation")){
                    e.getPlayer().getInventory().setHelmet(null);
                    e.getPlayer().getInventory().setChestplate(null);
                    e.getPlayer().getInventory().setLeggings(null);
                    e.getPlayer().getInventory().setBoots(null);
                        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,100000,10000000));
                        e.getPlayer().setGameMode(GameMode.SPECTATOR);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                e.getPlayer().setGameMode(GameMode.SURVIVAL);
                                e.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
                                last_quirk_use.replace(e.getPlayer(), System.currentTimeMillis());
                                check_delay(e.getPlayer());
                            }
                        },80);

                }
                else if(quirk.equalsIgnoreCase("Overhaul")){
                    Random random_object = new Random();
                    for(int i = 0; i < 40; i++){
                       int x = random_object.nextInt(10) -5;
                       int z = random_object.nextInt(10) -5;
                       int x_pos = (int) (e.getPlayer().getLocation().getX() + x);
                       int z_pos = (int) (e.getPlayer().getLocation().getZ() + z);
                       int y_pos = plugin.getServer().getWorld("world").getHighestBlockAt(x_pos,z_pos).getY() + 1;
                       e.getPlayer().getWorld().getBlockAt(new Location(e.getPlayer().getWorld(),x_pos,y_pos,z_pos)).setType(Material.STONE);
                   }
                    e.getPlayer().teleport(new Location(e.getPlayer().getWorld(),e.getPlayer().getLocation().getX(),e.getPlayer().getLocation().getY(),e.getPlayer().getLocation().getZ()));
                    last_quirk_use.replace(e.getPlayer(), System.currentTimeMillis());
                    check_delay(e.getPlayer());
                }
                else if(quirk.equalsIgnoreCase("Gravity")){
                    e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), e.getPlayer().getLocation().getX(),e.getPlayer().getLocation().getY() +75,e.getPlayer().getLocation().getZ()));
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 500,2));
                    last_quirk_use.replace(e.getPlayer(), System.currentTimeMillis());
                    check_delay(e.getPlayer());
                }
            }
        }
        else if(e.getAction().equals(Action.RIGHT_CLICK_AIR)){
            if(disbled_quirk_player.contains(e.getPlayer().getDisplayName()) || last_quirk_use.get(e.getPlayer()) - System.currentTimeMillis() > -7000)return;
            if(e.getPlayer().getInventory().getItemInMainHand().getType() == (Material.STICK)) {
                if (quirk.equalsIgnoreCase("Hell_Flame")) {
                    e.getPlayer().launchProjectile(Fireball.class);
                    e.getPlayer().launchProjectile(Fireball.class);
                    last_quirk_use.replace(e.getPlayer(), System.currentTimeMillis());
                }
                else if(quirk.equalsIgnoreCase("Permeation")){
                    e.getPlayer().getInventory().setHelmet(null);
                    e.getPlayer().getInventory().setChestplate(null);
                    e.getPlayer().getInventory().setLeggings(null);
                    e.getPlayer().getInventory().setBoots(null);
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,100000,10000000));
                    e.getPlayer().setGameMode(GameMode.SPECTATOR);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            e.getPlayer().setGameMode(GameMode.SURVIVAL);
                            e.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
                            last_quirk_use.replace(e.getPlayer(), System.currentTimeMillis());
                            check_delay(e.getPlayer());
                        }
                    },80);
                }
            }
        }
        else if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            if(disbled_quirk_player.contains(e.getPlayer().getDisplayName()) || last_quirk_use.get(e.getPlayer()) - System.currentTimeMillis() > -7000)return;
            if(quirk.equalsIgnoreCase("Explosion") || quirk.equalsIgnoreCase("Warp") || quirk.equalsIgnoreCase("Compress") || quirk.equalsIgnoreCase("Hardening"))return;
            if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK) {
                create_quirk(quirk, e.getPlayer(), e.getPlayer()); //Hardening, Explosion, Hell Flame, Gravity, Overhaul, Decay
                check_delay(e.getPlayer());
                last_quirk_use.replace(e.getPlayer(), System.currentTimeMillis());
            }
        }
        else if(e.getAction().equals(Action.LEFT_CLICK_AIR)){
            if(disbled_quirk_player.contains(e.getPlayer().getDisplayName()) || last_quirk_use.get(e.getPlayer()) - System.currentTimeMillis() > -7000)return;
            if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK){
                if(quirk.equalsIgnoreCase("Explosion")) {
                    create_quirk(quirk, e.getPlayer(), e.getPlayer());
                    check_delay(e.getPlayer());
                    last_quirk_use.replace(e.getPlayer(),System.currentTimeMillis());
                }
                else if(quirk.equalsIgnoreCase("Hardening")){
                    create_quirk(quirk, e.getPlayer(), e.getPlayer());
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            check_delay(e.getPlayer());
                            last_quirk_use.replace(e.getPlayer(),System.currentTimeMillis());
                        }
                    }, 140);
                }
            }
        }
    }
    private void set_quirk(MHAMinecraft plugin,Player player,String quirk){
        plugin.getConfig().set("Class."+player.getDisplayName(),quirk);
        player.sendMessage("You have gotten "+ quirk);
        plugin.getConfig().set("Class."+player.getDisplayName(), quirk);
        plugin.saveConfig();
    }

    private quirk_object create_quirk(String quirk, Player damager,Entity attacked){
        quirk_object damager_object = new quirk_object(plugin,damager.getDisplayName(),quirk);
        if(attacked instanceof Player) {
            damager_object.attacked_player = ((Player) attacked).getPlayer();
        }
        else if(!(attacked instanceof Player)){
            damager_object.attacked_player = null;
        }
        damager_object.activate(1,1,1);
        return damager_object;
    }
    private void check_delay(Player damager){
        if(!time_till_next_quirk.containsKey(damager)){
            time_till_next_quirk.put(damager,7);
        }
        else{
            time_till_next_quirk.replace(damager,7);
        }
    }
}
