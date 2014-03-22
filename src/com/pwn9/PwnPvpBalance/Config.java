package com.pwn9.PwnPvpBalance;

public class Config extends PwnPvpBalance
{

	// Load up any values from the config file
	public static void LoadConfig()
	{

		// Get enabled worlds
		PwnPvpBalance.enabledWorlds = instance.getConfig().getStringList("enabled_worlds");		
		
		// Get logging enabled
		PwnPvpBalance.logEnabled = instance.getConfig().getBoolean("debug_log");
			
	}
	
}