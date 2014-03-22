package com.pwn9.PwnPvpBalance;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerListener implements Listener 
{

	public PlayerListener(PwnPvpBalance plugin) 
	{
	    plugin.getServer().getPluginManager().registerEvents(this, plugin);    
	}	

	
	// Handle damage events, ignore cancelled for efficiency
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDamage(EntityDamageByEntityEvent e) 
	{
		// Check is player is shielded 
		//if (Shield.isShielded(e)) e.setCancelled(true); 
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent e)
	{
	    if (e.getEntity() instanceof Player)
	    {
	    	// Killed player
	        Player killed = (Player)e.getEntity();
	        // Killer player
	        Player killer = (Player)e.getEntity().getKiller();
	        
	        
	    }
	}	
	
}
