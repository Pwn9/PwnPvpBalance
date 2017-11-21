package com.pwn9.PwnPvpBalance;

public class Config 
{

	// Load up any values from the config file
	public static void LoadConfig()
	{

		// Get enabled worlds
		PwnPvpBalance.enabledWorlds = PwnPvpBalance.instance.getConfig().getStringList("enabled_worlds");		
		
		// Get shield option
		PwnPvpBalance.shield = PwnPvpBalance.instance.getConfig().getBoolean("shield", false);		

		// Get killstreak
		PwnPvpBalance.killstreak = PwnPvpBalance.instance.getConfig().getInt("killstreak", 3);		

		// Get max diff / debuff value
		PwnPvpBalance.maxDiffs = PwnPvpBalance.instance.getConfig().getDouble("max_debuff", 9.0);	
			// Simple error checking on the config value here
			if (PwnPvpBalance.maxDiffs > 9.0) { PwnPvpBalance.maxDiffs = 9.0; }
			if (PwnPvpBalance.maxDiffs < 1.0) { PwnPvpBalance.maxDiffs = 1.0; }
		
		// Get scale down
		PwnPvpBalance.scaleDown = PwnPvpBalance.instance.getConfig().getBoolean("scale", true);
		
		// Get end on quit
		PwnPvpBalance.endOnQuit = PwnPvpBalance.instance.getConfig().getBoolean("end_on_quit", true);

		// Get end timer - multiply by 1000 to turn into seconds
		PwnPvpBalance.shieldEndTimer = PwnPvpBalance.instance.getConfig().getInt("shield_end_timer", 300) * 1000;
		
		// Get armorspeed
		PwnPvpBalance.armorSpeed = PwnPvpBalance.instance.getConfig().getBoolean("armor_speed", false);
		
		// Get logging enabled
		PwnPvpBalance.logEnabled = PwnPvpBalance.instance.getConfig().getBoolean("debug_log", false);
		
		// Get balances config
		PwnPvpBalance.balances.saveDefaultConfig();
		
		// Testing save
		//PwnPvpBalance.balances.getConfig().set("test", "test");
		//PwnPvpBalance.balances.getConfig().set("testing.test", "testing");
			
	}
	
}