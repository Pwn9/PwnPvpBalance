package com.pwn9.PwnPvpBalance;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
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
		if(PwnPvpBalance.pvpBalances.get(victim.getUniqueId()) != null)
		{
			
			//if(PwnPvpBalance.pvpBalances.get(victim.getUniqueId()).get(attacker.getUniqueId()) != null)
			if(Integer.valueOf(PwnPvpBalance.pvpBalances.get(victim.getUniqueId()).get(attacker.getUniqueId())) != null)	
			{
				
				// Is this PvP battle at the killstreak yet?
	        	Integer counts = PwnPvpBalance.pvpBalances.get(victim.getUniqueId()).get(attacker.getUniqueId());

				if(counts >= PwnPvpBalance.killstreak)
				{
					
					// Is this a straight up shield configuration or a damage reduction configuration?
					
					// Shield active - full protection enabled
					if (PwnPvpBalance.shield) 	
					{
						
						// attacker message
						if(PwnPvpBalance.lastMessage.containsKey(attacker.getUniqueId()))
			            {
			                Long lastTime = PwnPvpBalance.lastMessage.get(attacker.getUniqueId());
			                Long currTime = System.currentTimeMillis();
			                
			                if(currTime > lastTime) 
			                {
								attacker.sendMessage(ChatColor.RED + victim.getName() + " has n00b shield against you and cannot be harmed.");
								PwnPvpBalance.lastMessage.put(attacker.getUniqueId(), PwnPvpBalance.calcTimer((long) 10000));
			                }
			            }
						
						else 
						{
							attacker.sendMessage(ChatColor.RED + victim.getName() + " has n00b shield against you and cannot be harmed.");
							PwnPvpBalance.lastMessage.put(attacker.getUniqueId(), PwnPvpBalance.calcTimer((long) 10000));	
						}

						// victim message
						if(PwnPvpBalance.lastMessage.containsKey(victim.getUniqueId()))
			            {
							
			                Long lastTime = PwnPvpBalance.lastMessage.get(victim.getUniqueId());
			                Long currTime = System.currentTimeMillis();
			                
			                if(currTime > lastTime) 
			                {
								victim.sendMessage(ChatColor.RED + attacker.getName() + " triggered your n00b shield, time for payback!");
								PwnPvpBalance.lastMessage.put(victim.getUniqueId(), PwnPvpBalance.calcTimer((long) 10000));
			                }
			                
			            }
						
						else
						{
							victim.sendMessage(ChatColor.RED + attacker.getName() + " triggered your n00b shield, time for payback!");
							PwnPvpBalance.lastMessage.put(victim.getUniqueId(), PwnPvpBalance.calcTimer((long) 10000));	
						}
						
						// Shield event = this event should get cancelled and no damage done
						e.setCancelled(true);

					}
					
					// No shield, do damage reductions instead
					else 
					{
						// Get damage debuff amounts - potentially move to it's own super cool function
						
						// This setup basically knocks 10% off each hit for every kill over the killstreak total to a max of 90%, which seems a bit OP to me.
						
						// So this should probably be far more configurable.
						
						Double diffs = (double) ((counts + 1) - PwnPvpBalance.killstreak);
						Double perDiffs = (double) 1;
						
						if (PwnPvpBalance.maxDiffs >= diffs) 
						{
							perDiffs = 1 - (diffs / 10);
						}
						else
						{
							perDiffs = 1 - (PwnPvpBalance.maxDiffs / 10);							
						}
						
						Double damageModifier = e.getDamage() * perDiffs;
						
						// Logging?  Might be spammy - consider log levels in the future
			        	if (PwnPvpBalance.logEnabled)
			        	{
			        		PwnPvpBalance.logToFile(attacker.getName() + " V. " + victim.getName() + " : Init Damage: " + e.getDamage() + " Mod Damage: " + damageModifier);
			        	}						
						
						// Alter damage amount 
						e.setDamage(damageModifier);
						
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
	        
	        // Is the killer a player at all?
	        if (e.getEntity().getKiller() == null) return;
	        
	        // Get the killer
	        Player killer = (Player)e.getEntity().getKiller(); 
	           
	        // Here will go a routine to match up the killed with the killer and add to a balance mapping        
	        // Get any current killed -> killer -> counts, if none create new
	       
	        // Does current killed have a map up yet? If not start one and add the killer + 1 kill
	        if(PwnPvpBalance.pvpBalances.get(killed.getUniqueId()) == null) 
	        {
	        	
	        	PwnPvpBalance.pvpBalances.put(killed.getUniqueId(), new HashMap<UUID, Integer>());
	        	
	        	PwnPvpBalance.pvpBalances.get(killed.getUniqueId()).put(killer.getUniqueId(), 1);
	        	
	        	if (PwnPvpBalance.logEnabled)
	        	{
	        		PwnPvpBalance.logToFile(killed.getName() + " killed by " + killer.getName() + " 1 time");
	        	}
	        }	
	        
	        // Has current killed player been killed by this killer yet? If not add killer + 1 kill
	        else if(PwnPvpBalance.pvpBalances.get(killed.getUniqueId()).get(killer.getUniqueId()) == null)
	        {
	        	
	        	PwnPvpBalance.pvpBalances.get(killed.getUniqueId()).put(killer.getUniqueId(), 1);
	        	
	        	if (PwnPvpBalance.logEnabled)
	        	{
	        		PwnPvpBalance.logToFile(killed.getName() + " killed by " + killer.getName() + " 1 time");
	        	}
	        	
	        }
	        
	        // Current killed player has been killed by this killer, add another kill
	        else 
        	{
	        	
	        	Integer counts = PwnPvpBalance.pvpBalances.get(killed.getUniqueId()).get(killer.getUniqueId());
	        	counts = counts + 1;
	        	
	        	PwnPvpBalance.pvpBalances.get(killed.getUniqueId()).put(killer.getUniqueId(), counts);
	        	
	        	if (PwnPvpBalance.logEnabled)
	        	{
	        		PwnPvpBalance.logToFile(killed.getName() + " killed by " + killer.getName() + " " + counts +" times");
	        	}
	        	
        	}
	          
	        // Now the reverse, erase the killed player from the killers list or reduce by one depending on settings	
	        
	        // Does the killer have an entry?
	        if(PwnPvpBalance.pvpBalances.get(killer.getUniqueId()) != null)
	        {
	        	// Does the killer have an entry for the playery they just killed
		        if(PwnPvpBalance.pvpBalances.get(killer.getUniqueId()).get(killed.getUniqueId()) != null)
		        {
		        	
		        	// If scale down we have to just reduce the streak by 1
		        	if (PwnPvpBalance.scaleDown) 
		        	{
		        		
			        	Integer counts = PwnPvpBalance.pvpBalances.get(killer.getUniqueId()).get(killed.getUniqueId());
			        	
			        	// The count is still uneven
			        	if (counts > 0) 
			        	{
			        		counts = counts - 1;

			        		// Update the killstreak
				        	PwnPvpBalance.pvpBalances.get(killer.getUniqueId()).put(killed.getUniqueId(), counts);
				        	
				        	if (PwnPvpBalance.logEnabled)
				        	{
				        		PwnPvpBalance.logToFile(killed.getName() + " counts against " + killer.getName() + " scaled down to " + counts);
				        	}
				        	
			        	}
			        	// Score has been settled
			        	else 
			        	{
			        		
			        		// Remove player from the list
				        	PwnPvpBalance.pvpBalances.get(killer.getUniqueId()).remove(killed.getUniqueId());
				        	
				        	if (PwnPvpBalance.logEnabled)
				        	{
				        		PwnPvpBalance.logToFile(killer.getName() + " and " + killed.getName() + " have evened up the score");
				        	}	
				        	
			        	}
			        	
		        	}
		        	
		        	// If scale down is false, then when player kills someone on their list, that person is removed entirely.
		        	else 
		        	{
		        		
			        	PwnPvpBalance.pvpBalances.get(killer.getUniqueId()).remove(killed.getUniqueId());
			        	
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
	        if(PwnPvpBalance.pvpBalances.get(p.getUniqueId()) != null)
	        {
	        	
	        	// Remove player logging out from map
	        	PwnPvpBalance.pvpBalances.remove(p.getUniqueId());
	        	
	        	// Log it?
	        	if (PwnPvpBalance.logEnabled)
	        	{
	        		PwnPvpBalance.logToFile(p.getName() + " logged out and removed from balance list.");
	        	}
	        	
	        }
	        
        } 
    	
	}
	
}