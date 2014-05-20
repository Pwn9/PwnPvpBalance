package com.pwn9.PwnPvpBalance;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerMoveListener implements Listener 
{

	public PlayerMoveListener(PwnPvpBalance plugin) 
	{
	    plugin.getServer().getPluginManager().registerEvents(this, plugin);    
	}	


	// Work in progress, armor balancing with speed effects... 
	
	// Should we modify the player on every move event, or should we set their walk speed on join, respawn and armor equip/unequip events?  
	
	// It would probably be more resource efficient to do the above ^.  Less code to do the onMove though..
	
	// Actually it would probably be more effective to just check all the time, NCP checks the move event all the time, so this shouldn't be too much of a problem
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMoveEvent(PlayerMoveEvent event)
	{

		// Get the event world
		World w = event.getPlayer().getWorld();		
		
		// We should check to see if plugin is enabled first.
		if (!PwnPvpBalance.isEnabledIn(w.getName())) return; 		
		
		// Is armor speed enabled?
		if (!PwnPvpBalance.armorSpeed) return;
		
		// Get the player
		Player player = event.getPlayer();
		
		// Lets get player's current armor
		int armorWeight = 0;
		
		// Setup some stuffs
		Float spdMod;
		Float walksp = player.getWalkSpeed();
		
		ItemStack boots = player.getInventory().getBoots();
		ItemStack chest = player.getInventory().getChestplate();
		ItemStack pants = player.getInventory().getLeggings();
		ItemStack helmet = player.getInventory().getHelmet();
		
		if (boots != null) {
		    if (boots.getType() == Material.LEATHER_BOOTS) {
		       armorWeight = armorWeight + 1; 
		    }
		    if (boots.getType() == Material.GOLD_BOOTS) {
		    	armorWeight = armorWeight + 2;  
		    }
		    if (boots.getType() == Material.IRON_BOOTS) {
		    	armorWeight = armorWeight + 3; 
		    }
		    if (boots.getType() == Material.DIAMOND_BOOTS) {
		    	armorWeight = armorWeight + 4; 
		    }		    
		}

		if (chest != null) {
		    if (chest.getType() == Material.LEATHER_CHESTPLATE) {
		    	armorWeight = armorWeight + 1; 
		    }
		    if (chest.getType() == Material.GOLD_CHESTPLATE) {
		    	armorWeight = armorWeight + 2; 
		    }
		    if (chest.getType() == Material.IRON_HELMET) {
		    	armorWeight = armorWeight + 3; 
		    }
		    if (chest.getType() == Material.DIAMOND_CHESTPLATE) {
		    	armorWeight = armorWeight + 4; 
		    }		    
		}

		if (pants != null) {
		    if (pants.getType() == Material.LEATHER_LEGGINGS) {
		    	armorWeight = armorWeight + 1; 
		    }
		    if (pants.getType() == Material.GOLD_LEGGINGS) {
		    	armorWeight = armorWeight + 2; 
		    }
		    if (pants.getType() == Material.IRON_LEGGINGS) {
		    	armorWeight = armorWeight + 3; 
		    }
		    if (pants.getType() == Material.DIAMOND_LEGGINGS) {
		    	armorWeight = armorWeight + 4; 
		    }		    
		}
		
		if (helmet != null) {
		    if (helmet.getType() == Material.LEATHER_HELMET) {
		    	armorWeight = armorWeight + 1; 
		    }
		    if (helmet.getType() == Material.GOLD_HELMET) {
		    	armorWeight = armorWeight + 3; 
		    }
		    if (helmet.getType() == Material.IRON_HELMET) {
		    	armorWeight = armorWeight + 3; 
		    }
		    if (helmet.getType() == Material.DIAMOND_HELMET) {
		    	armorWeight = armorWeight + 4; 
		    }		    
		}
		
		if (armorWeight == 0) {
			
			spdMod = (float) (0.28);
			
			if (spdMod.toString() != walksp.toString()) {
				
				if (PwnPvpBalance.logEnabled) {
					PwnPvpBalance.logToFile(player.getName() + " speed was: " + walksp.toString() + " is now: " + spdMod.toString() + " Armor weight: " + armorWeight );
				}
				
				player.setWalkSpeed(spdMod);
				
			}
			
		}
		
		else {
			
			spdMod = (float) (0.28 - (armorWeight / 100));
			
			if (spdMod.toString() != walksp.toString()) {
				
				if (PwnPvpBalance.logEnabled) {
					PwnPvpBalance.logToFile(player.getName() + " walk speed was: " + walksp.toString() + " is now: " + spdMod.toString() + " Armor weight: " + armorWeight );
				}
				
				player.setWalkSpeed(spdMod);
			}
			
		}
		
		// Default walkspeed is apparently 0.2f
		//player.setWalkSpeed(0.3f);
		
		/*** All this was for potion effect modification, probably wont use this method, but keeping it here for reference right now ***/
		
		//player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
		
		//Location playerLocation = event.getPlayer().getLocation();
		//double y = playerLocation.getBlockY();
		//double x = playerLocation.getBlockX();
		//double z = playerLocation.getBlockZ();

		//World currentWorld = event.getPlayer().getWorld();

		//Location blockBelow = new Location(currentWorld, x, y - 2.0D, z);

		/*if (player.isSprinting())
			{
				if (plugin.speedLand > 1)
				{
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 500, plugin.speedLand - 2));
				}
			}
		*/
		/*else if ((player.getAllowFlight()) && (blockBelow.getBlock().getType().equals(Material.AIR)))
		{
			if (plugin.speedFly > 1)
			{
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 500, plugin.speedFly - 2));
			}
		}
		else if (playerLocation.getBlock().getType().equals(Material.STATIONARY_WATER))
		{
			if (plugin.speedWater > 1D)
			{
				Vector dir = player.getLocation().getDirection().normalize().multiply(plugin.speedWater - 1.6D);
				Vector vec = new Vector(dir.getX(), dir.getY(), dir.getZ());
				player.setVelocity(vec);
			}

		} */
		
		/*else 
		{
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 0, 0), true);
		}
		*/
		
		
		
	}
	
}
