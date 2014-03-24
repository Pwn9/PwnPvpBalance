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
	
	// Get logging enabled
	public static Boolean logEnabled;
	
	// Killstreak integer
	public static Integer killstreak;
	
	// Setup the player map - or array list?
	// map<killedname, map<killername, killedtimes>>
	public static Map<String, Map<String, Integer>> pvpBalances = new HashMap<String, Map<String,Integer>>();
		
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
	    
		// Setup listeners
		new PlayerListener(this);
			
		// Get data folder
		PwnPvpBalance.dataFolder = getDataFolder();
		
		// Load Configurable Values
		Config.LoadConfig();

	}
	
	public void onDisable() 
	{
		
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