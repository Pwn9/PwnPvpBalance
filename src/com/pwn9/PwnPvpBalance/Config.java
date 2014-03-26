package com.pwn9.PwnPvpBalance;

public class Config extends PwnPvpBalance
{

	// Load up any values from the config file
	public static void LoadConfig()
	{

		// Get enabled worlds
		PwnPvpBalance.enabledWorlds = instance.getConfig().getStringList("enabled_worlds");		
		
		// Get shield option
		PwnPvpBalance.shield = instance.getConfig().getBoolean("shield", false);		

		// Get killstreak
		PwnPvpBalance.killstreak = instance.getConfig().getInt("killstreak", 3);		

		// Get max diff / debuff value
		PwnPvpBalance.maxDiffs = instance.getConfig().getDouble("max_debuff", 9.0);	
			// Simple error checking on the config value here
			if (PwnPvpBalance.maxDiffs > 9.0) { PwnPvpBalance.maxDiffs = 9.0; }
			if (PwnPvpBalance.maxDiffs < 1.0) { PwnPvpBalance.maxDiffs = 1.0; }
		
		// Get scale down
		PwnPvpBalance.scaleDown = instance.getConfig().getBoolean("scale", true);
		
		// Get end on quit
		PwnPvpBalance.endOnQuit = instance.getConfig().getBoolean("end_on_quit", true);
		
		// Get logging enabled
		PwnPvpBalance.logEnabled = instance.getConfig().getBoolean("debug_log", false);
			
	}
	
}