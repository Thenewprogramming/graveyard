package com.thenewprogramming.Bukkit.Vote4TempBan;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;

public class Vote4TempBanListener implements Listener {
	
	//linking to the main class LIKE A BAUS
	private Vote4TempBan plugin;
	
	public Vote4TempBanListener(Vote4TempBan plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		//if the joining player is in the ban list, kick him
		if(plugin.getplayersbanned().contains(event.getPlayer().getName())){
			Bukkit.getLogger().info("test");
			event.setJoinMessage("");
			event.getPlayer().kickPlayer("Can't you wait "+(plugin.getConfig().getLong("timesettings.bantime")/20)/60+" min to join again??");
		}
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event){
		if(plugin.getplayersbanned().contains(event.getPlayer().getName())){
			event.setLeaveMessage("");
		}
	}
}