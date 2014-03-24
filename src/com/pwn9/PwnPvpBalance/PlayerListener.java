package com.pwn9.PwnPvpBalance;

import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
		
		// Get the event world
		World w = e.getDamager().getWorld();		
		
		// We should check to see if plugin is enabled first.
		if (!PwnPvpBalance.isEnabledIn(w.getName())) return; 
		
		// The attacker
		Player attacker = null;
		
		// The victim
		Player victim = null;
		
		// Determine who or what attacker is
		if(e.getDamager() instanceof Arrow) 
		{
			Arrow arrow = (Arrow)e.getDamager();
			
			if(arrow.getShooter() instanceof Player) 
			{
				attacker = (Player)arrow.getShooter();
			} 
			
		} 
		else if(e.getDamager() instanceof Player) 
		{
			attacker = (Player)e.getDamager();
		} 
		else 
		{
			// attacker is NOT a player - do we want to do anything with this knowledge?
			return;
		}
		
		// Check if damage is done TO a player
		if(e.getEntity() instanceof Player) 
		{
			victim = (Player)e.getEntity();
		}
		else 
		{
			// victim is NOT a player - do we want to do anything with this knowledge?
			return;
		}
		
		// We have our attacker and victim now so, so lets get the stats and do the math
		if(PwnPvpBalance.pvpBalances.get(victim.getName()) != null)
		{
			
			if(PwnPvpBalance.pvpBalances.get(victim.getName()).get(attacker.getName()) != null)
			{
				
				// Is this PvP battle at the killstreak yet?
	        	Integer counts = PwnPvpBalance.pvpBalances.get(victim.getName()).get(attacker.getName());

				if(counts >= PwnPvpBalance.killstreak)
				{
					
					// Is this a straight up shield configuration or a damage reduction configuration?
					
					// Shield active - full proection enabled
					if (PwnPvpBalance.shield) 	
					{
						
						// This message should be configurable, also it could be a bit spammy, maybe we should only send it once for a period of time
						attacker.sendMessage(victim.getName() + " has n00b shield against you and cannot be harmed.");

						// This message should be configurable, also it could be a bit spammy, maybe we should only send it once for a period of time
						victim.sendMessage(attacker.getName() + " triggered your n00b shield, time for payback!");
						
						// Shield event = this event should get cancelled and no damage done
						e.setCancelled(true);
						
						// Logging?  Might be spammy let's think about it and save for later
						
					}
					
					// No shield, do damage reductions instead
					else 
					{
						
						Integer diffs = (counts + 1) - PwnPvpBalance.killstreak;
						
						// Alter damage amount by dividing current damage by the diffs.
						e.setDamage(e.getDamage() / diffs);
						
						// Logging?  Might be spammy let's think about it and save for later
						
					}				
					
				}
				
			}
			
		}
		
	}
	
	// Handle adding and removing players from balance map on death event
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent e)
	{
		
	    if (e.getEntity() instanceof Player)
	    {
	    	
			// Get the event world
			World w = e.getEntity().getWorld();	
			
			// We should check to see if plugin is enabled first.
			if (!PwnPvpBalance.isEnabledIn(w.getName())) return; 
			
	    	// Killed player
	        Player killed = (Player)e.getEntity();
	        
	        // Killer player
	        Player killer = (Player)e.getEntity().getKiller(); 
	           
	        // Here will go a routine to match up the killed with the killer and add to a balance mapping        
	        // Get any current killed -> killer -> counts, if none create new
	       
	        // Does current killed have a map up yet? If not start one and add the killer + 1 kill
	        if(PwnPvpBalance.pvpBalances.get(killed.getName()) == null) 
	        {
	        	
	        	PwnPvpBalance.pvpBalances.put(killed.getName(), new HashMap<String, Integer>());
	        	
	        	PwnPvpBalance.pvpBalances.get(killed.getName()).put(killer.getName(), 1);
	        	
	        	if (PwnPvpBalance.logEnabled)
	        	{
	        		PwnPvpBalance.logToFile(killed.getName() + " killed by " + killer.getName() + " 1 time");
	        	}
	        }	
	        
	        // Has current killed player been killed by this killer yet? If not add killer + 1 kill
	        else if(PwnPvpBalance.pvpBalances.get(killed.getName()).get(killer.getName()) == null)
	        {
	        	
	        	PwnPvpBalance.pvpBalances.get(killed.getName()).put(killer.getName(), 1);
	        	
	        	if (PwnPvpBalance.logEnabled)
	        	{
	        		PwnPvpBalance.logToFile(killed.getName() + " killed by " + killer.getName() + " 1 time");
	        	}
	        	
	        }
	        
	        // Current killed player has been killed by this killer, add another kill
	        else 
        	{
	        	
	        	Integer counts = PwnPvpBalance.pvpBalances.get(killed.getName()).get(killer.getName());
	        	counts = counts + 1;
	        	
	        	PwnPvpBalance.pvpBalances.get(killed.getName()).put(killer.getName(), counts);
	        	
	        	if (PwnPvpBalance.logEnabled)
	        	{
	        		PwnPvpBalance.logToFile(killed.getName() + " killed by " + killer.getName() + " " + counts +" times");
	        	}
	        	
        	}
	          
	        // Now the reverse, erase the killed player from the killers list or reduce by one depending on settings	
	        if(PwnPvpBalance.pvpBalances.get(killer.getName()) != null)
	        {
	        	
		        if(PwnPvpBalance.pvpBalances.get(killer.getName()).get(killed.getName()) != null)
		        {
		        	
		        	if (PwnPvpBalance.scaleDown) 
		        	{
		        		
			        	Integer counts = PwnPvpBalance.pvpBalances.get(killer.getName()).get(killed.getName());
			        	
			        	if (counts > 0) 
			        	{
			        		counts = counts - 1;

				        	PwnPvpBalance.pvpBalances.get(killer.getName()).put(killed.getName(), counts);
				        	
				        	if (PwnPvpBalance.logEnabled)
				        	{
				        		PwnPvpBalance.logToFile(killed.getName() + " counts against " + killer.getName() + " scaled down to " + counts);
				        	}
				        	
			        	}
			        	
			        	else 
			        	{
			        		
				        	PwnPvpBalance.pvpBalances.get(killer.getName()).remove(killed.getName());
				        	
				        	if (PwnPvpBalance.logEnabled)
				        	{
				        		PwnPvpBalance.logToFile(killer.getName() + " and " + killed.getName() + " have evened up the score");
				        	}	
				        	
			        	}
			        	
		        	}
		        	
		        	// If scale down is false, then when player kills someone on their list, that person is removed entirely.
		        	else 
		        	{
		        		
			        	PwnPvpBalance.pvpBalances.get(killer.getName()).remove(killed.getName());
			        	
			        	if (PwnPvpBalance.logEnabled)
			        	{
			        		PwnPvpBalance.logToFile(killed.getName() + " removed from " + killer.getName() + " payback list");
			        	}
			        	
		        	}
		        	
		        }
		        
	        }  
	        
	    }
	    
	}
	
	// Remove player map if they logout - configurable
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent e)
	{	
		
		// Get the player
		Player p = e.getPlayer();
		
		// If configured to end on quit
    	if (PwnPvpBalance.endOnQuit)
    	{		
    		
	        // Check if player is in the list
	        if(PwnPvpBalance.pvpBalances.get(p.getName()) != null)
	        {
	        	
	        	// Remove player logging out from map
	        	PwnPvpBalance.pvpBalances.remove(p.getName());
	        	
	        	// Log it?
	        	if (PwnPvpBalance.logEnabled)
	        	{
	        		PwnPvpBalance.logToFile(p.getName() + " logged out and removed from balance list.");
	        	}
	        	
	        }
	        
        } 
    	
	}
	
}
