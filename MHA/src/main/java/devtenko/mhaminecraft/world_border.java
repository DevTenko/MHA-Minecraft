package devtenko.mhaminecraft;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class world_border {
    MHAMinecraft plugin;
    public world_border(MHAMinecraft plugin){
        this.plugin = plugin;
        World normal =plugin.getServer().getWorld(plugin.getConfig().getString("World.Normal"));
        normal.getWorldBorder().setSize(2500);
        normal.getWorldBorder().setCenter(new Location(normal,0,0,0));
        normal.getWorldBorder().setDamageBuffer(2);
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                spawn_airdrop();
                if(normal.getWorldBorder().getSize() <= 125){
                    List<Player> playerList =plugin.getServer().getWorld(plugin.getConfig().getString("World.End")).getPlayers();
                    for(Player p : playerList){
                        p.teleport(new Location(normal,0, normal.getHighestBlockYAt(0,0),0));
                    }
                    playerList =plugin.getServer().getWorld(plugin.getConfig().getString("World.Nether")).getPlayers();
                    for(Player p : playerList){
                        p.teleport(new Location(normal,0, normal.getHighestBlockYAt(0,0),0));
                    }
                    plugin.getServer().unloadWorld(plugin.getConfig().getString("World.Nether"),true);
                    plugin.getServer().unloadWorld(plugin.getConfig().getString("World.End"),true);
                    plugin.getServer().broadcastMessage("Nether and The End has been Disabled");
                    normal.getWorldBorder().setSize(normal.getWorldBorder().getSize() - 10, 240);
                    plugin.getServer().broadcastMessage("Current World Border Size: " + normal.getWorldBorder().getSize());
                    plugin.getServer().broadcastMessage("Updated World Border Size: " + (normal.getWorldBorder().getSize() -10));
                    plugin.getServer().broadcastMessage("World Border is shrinking Head to 0,0");
                }
                else {
                    plugin.getServer().broadcastMessage("Current World Border Size: " + normal.getWorldBorder().getSize());
                    plugin.getServer().broadcastMessage("Updated World Border Size: " + normal.getWorldBorder().getSize() / 2);
                    plugin.getServer().broadcastMessage("World Border is shrinking Head to 0,0");
                    normal.getWorldBorder().setSize(normal.getWorldBorder().getSize() / 2, 240);
                }
            }
        },6000,6000);
    }

    private void spawn_airdrop(){
        int world_border_size = (int) plugin.getServer().getWorld(plugin.getConfig().getString("World.Normal")).getWorldBorder().getSize();
        if(world_border_size >= 225){
            return;
        }
        else{
            Random random = new Random();
            int x = random.nextInt(world_border_size + world_border_size) - world_border_size;
            int z = random.nextInt(world_border_size + world_border_size) - world_border_size;
            int y = plugin.getServer().getWorld(plugin.getConfig().getString("World.Normal")).getHighestBlockYAt(x,z) + 2;
            int random_item_amount = random.nextInt(7)+3;
            Block airdrop = plugin.getServer().getWorld(plugin.getConfig().getString("World.Normal")).getBlockAt(x,y,z);
            airdrop.setType(Material.CHEST);
            Chest airdrop_chest = (Chest) airdrop;
            for(int i =0; i< random_item_amount;i++) {
                int item = random.nextInt(5);
                if(item == 1){
                    airdrop_chest.getInventory().addItem(new ItemStack(Material.IRON_BLOCK,2));
                }
                else if(item == 2){
                    airdrop_chest.getInventory().addItem(new ItemStack(Material.ENDER_PEARL,3));
                }
                else if(item == 3){
                    airdrop_chest.getInventory().addItem(new ItemStack(Material.BOW, 1));
                }
                else if(item == 4){
                    airdrop_chest.getInventory().addItem(new ItemStack(Material.ARROW, 5));
                }
                else if(item == 5){
                    airdrop_chest.getInventory().addItem(new ItemStack(Material.DIAMOND_ORE, 3));
                }
            }
            plugin.getServer().broadcastMessage("Airdrop at: "+ airdrop.getLocation());
        }
    }
}
