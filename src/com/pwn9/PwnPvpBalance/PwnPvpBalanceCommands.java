package com.pwn9.PwnPvpBalance;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class PwnPvpBalanceCommands implements CommandExecutor
{
	private PwnPvpBalance plugin;
	
	public PwnPvpBalanceCommands(PwnPvpBalance plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{		
		if (!(sender instanceof Player) && !(sender instanceof ConsoleCommandSender))
		{
			sender.sendMessage("§cThis command can only be run by a player.");
		}
		else
		{
			if (cmd.getName().equalsIgnoreCase("ppb"))
			{
				ppb(sender, cmd, label, args);
				return true;
			}
		}
		return false;
	}	
   	
	public void ppb(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length > 0)
		{
			if(args[0].equalsIgnoreCase("reload"))
			{
				plugin.reloadConfig();
				Config.LoadConfig();
				sender.sendMessage("§cPwnPvpBalance: Settings reloaded!");
				
				if (PwnPvpBalance.logEnabled) {
					PwnPvpBalance.logToFile(sender.getName() + " reloaded the settings.");
				}
			}
			else if(args[0].equalsIgnoreCase("save"))
			{
				plugin.saveConfig();
				Config.LoadConfig();
				sender.sendMessage("§cPwnPvpBalance: Settings saved!");
				
				if (PwnPvpBalance.logEnabled) {
					PwnPvpBalance.logToFile(sender.getName() + " saved the settings.");
				}
			}
           else 
           {
        	   sender.sendMessage("§cPwnPvpBalance Usage: /ppb <reload|save>"); 
           }			
		}
		else 
		{
    	   sender.sendMessage("§cPwnPvpBalance Usage: ppb <reload|save>");     
	    }		
	}
}