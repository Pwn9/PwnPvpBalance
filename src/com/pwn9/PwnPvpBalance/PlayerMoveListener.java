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
		
		if (player.hasPermission("pvpbal.nospeed")) {
			return;
		}
		
		// Lets get player's current armor
		double armorWeight = 0;
			
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
		    if (boots.getType() == Material.CHAINMAIL_BOOTS) {
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
		    if (chest.getType() == Material.CHAINMAIL_CHESTPLATE) {
		    	armorWeight = armorWeight + 2; 
		    }		    
		    if (chest.getType() == Material.IRON_CHESTPLATE) {
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
		    if (pants.getType() == Material.CHAINMAIL_LEGGINGS) {
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
		    	armorWeight = armorWeight + 2; 
		    }
		    if (helmet.getType() == Material.CHAINMAIL_HELMET) {
		    	armorWeight = armorWeight + 2; 
		    }
		    if (helmet.getType() == Material.IRON_HELMET) {
		    	armorWeight = armorWeight + 3; 
		    }
		    if (helmet.getType() == Material.DIAMOND_HELMET) {
		    	armorWeight = armorWeight + 4; 
		    }		    
		}
		
		double spdMod;
		if (armorWeight == 0) {	
			spdMod = (0.26f);			
		}
		else {
			spdMod = (0.26 - ((armorWeight / 2) / 100));
		}
		
		double walksp = player.getWalkSpeed();
		int spdModR = (int) Math.round(spdMod*1000);
		int walkspR = (int) Math.round(walksp*1000);
		
		if (spdModR != walkspR) {
			if (PwnPvpBalance.logEnabled) {
				PwnPvpBalance.logToFile(player.getName() + " speed was: " + walksp + " is now: " + spdMod + " Armor weight: " + armorWeight );
			}
			player.setWalkSpeed((float) spdMod);
		}
			
	}
	
}
