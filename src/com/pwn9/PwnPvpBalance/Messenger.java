package com.pwn9.PwnPvpBalance;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

// class for doing all player messages - eventually
public class Messenger extends PwnPvpBalance
{
	// handle sending shield messages to players
	public static void shieldMessage(Player p, Player q, String message) 
	{
		if(PwnPvpBalance.lastMessage.containsKey(p.getUniqueId()))
        {
            Long lastTime = PwnPvpBalance.lastMessage.get(p.getUniqueId());
            Long currTime = System.currentTimeMillis();
            
            if(currTime > lastTime) 
            {
				p.sendMessage(ChatColor.RED + q.getName() + message);
				PwnPvpBalance.lastMessage.put(p.getUniqueId(), PwnPvpBalance.calcTimer((long) 10000));
            }
        }
		else 
		{
			p.sendMessage(ChatColor.RED + q.getName() + message);
			PwnPvpBalance.lastMessage.put(p.getUniqueId(), PwnPvpBalance.calcTimer((long) 10000));	
		}		
	}	
}