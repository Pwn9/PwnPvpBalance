package com.pwn9.PwnPvpBalance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.pwn9.PwnPvpBalance.MetricsLite;

public class PwnPvpBalance extends JavaPlugin 
{
	// For convenience, a reference to the instance of this plugin
    public static PwnPvpBalance instance;	
	
    // Get the datafolder
	public static File dataFolder;
	
	/*** Init configurable values ***/
	
	// Get enabled worlds
	public static List<String> enabledWorlds;
	
	// Get shield option
	public static Boolean shield;
	
	// Killstreak integer
	public static Integer killstreak;

	// Max diffs / debuff 
	public static Double maxDiffs;
	
	// Get scale down
	public static Boolean scaleDown;
	
	// Get end on quit
	public static Boolean endOnQuit;

	// Get armor speed
	public static Boolean armorSpeed;	
	
	// Get logging enabled
	public static Boolean logEnabled;	
	
	/*** Other values ***/

 	// map<killed, map<killer, killedtimes>> 
	public static Map<UUID, Map<UUID, Integer>> pvpBalances = new HashMap<UUID, Map<UUID, Integer>>();

 	// Last message map, designed to reduce sendmessage spam - player, lastmessagetime
	public static Map<UUID, Long> lastMessage = new HashMap<UUID, Long>();
	
	// Things to do when the plugin starts
	public void onEnable() 
	{
		// Create an instance of this, for some reason, I forget why.
		instance = this;
		 
		// Get the config.yml stuffs
		this.saveDefaultConfig();
		
		// Start Metrics
		try 
		{
		    MetricsLite metricslite = new MetricsLite(this);
		    metricslite.start();
		} 
		catch (IOException e) 
		{
		    // Failed to submit the stats :-(
		}
	    
		// Get data folder
		PwnPvpBalance.dataFolder = getDataFolder();
		
		// Load Configurable Values
		Config.LoadConfig();
		
		// Setup default player listeners
		new PlayerListener(this);
		
		// For efficiency, only load move listener if armorspeed is enabled
		if (PwnPvpBalance.armorSpeed) {
			new PlayerMoveListener(this);
		}
		
		if (PwnPvpBalance.logEnabled) {
			PwnPvpBalance.logToFile("PwnPvpBalance Enabled");
		}

	}
	
	public void onDisable() 
	{
		if (PwnPvpBalance.logEnabled) {
			PwnPvpBalance.logToFile("PwnPvpBalance Disabled");
		}
	}
	
	/*** Utility Section - Stuff that does stuff ***/
	
	// Boolean to return if the plugin is enabled in a specific world
	public static boolean isEnabledIn(String world) 
	{
		return enabledWorlds.contains(world);
	}	
	
	// Generic function to return the calculated time
	public static long calcTimer(Long time) 
	{
		return System.currentTimeMillis() + time;
	}
	
	// Special logging function
    public static void logToFile(String message) 
    {   
    	try 
    	{
		    if(!dataFolder.exists()) 
		    {
		    	dataFolder.mkdir();
		    }
		     
		    File saveTo = new File(dataFolder, "PwnPvpBalance.log");
		    if (!saveTo.exists())  
		    {
		    	saveTo.createNewFile();
		    }
		    
		    FileWriter fw = new FileWriter(saveTo, true);
		    PrintWriter pw = new PrintWriter(fw);
		    pw.println(getDate() +" "+ message);
		    pw.flush();
		    pw.close();
	    } 
	    catch (IOException e) 
	    {
	    	e.printStackTrace();
	    }
    }	
    
    // Date for logging
    public static String getDate() 
    {
		  String s;
		  Format formatter;
		  Date date = new Date(); 
		  formatter = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]");
		  s = formatter.format(date);
		  return s;
    }
	
}