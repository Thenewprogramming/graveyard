package com.thenewprogramming.Bukkit.Vote4TempBan;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Vote4TempBan extends JavaPlugin{
	
	//begin defining variables (made like this so it does not get in the way of other plugins)
	int VotingTask;
	private int yesvotes = 0;
	private int novotes = 0;
	private boolean votingcurrently = false;
	private ArrayList<String> playersvoted = new ArrayList<String>();
	private ArrayList<String> playercooldown = new ArrayList<String>();
	private ArrayList<String> playersbanned = new ArrayList<String>();
	public int getyesvotes(){
		return yesvotes;
	}
	public void setyesvotes(int yesvotes){
		this.yesvotes = yesvotes;
	}
	public int getnovotes(){
		return novotes;
	}
	public void setnovotes(int novotes){
		this.novotes = novotes;
	}
	public boolean getvotingcurrently(){
		return votingcurrently;
	}
	public void setvotingcurrently(boolean votingcurrently){
		this.votingcurrently = votingcurrently;
	}
	public ArrayList<String> getplayersvoted(){
		return playersvoted;
	}
	public void setplayersvoted(ArrayList<String> playersvoted){
		this.playersvoted=playersvoted;
	}
	public ArrayList<String> getplayercooldown(){
		return playercooldown;
	}
	public void setplayercooldown(ArrayList<String> playercooldown){
		this.playercooldown=playercooldown;
	}
	public ArrayList<String> getplayersbanned(){
		return playersbanned;
	}
	public void setplayersbanned(ArrayList<String> playersbanned){
		this.playersbanned = playersbanned;
	}
	//end defining variables

	@Override
    public void onEnable(){
		//init the config and listeners
		this.saveDefaultConfig();
		getCommand("vote").setExecutor(new Vote4TempBanCommandExecutor(this));
		new Vote4TempBanListener(this);
		getLogger().info("Vote4TempBan has been Enabled!");
    }
 
    @Override
    public void onDisable() {
    	getLogger().info("Vote4TempBan has been Disabled!");
    }
    
    public void startvote(CommandSender sender, String[] args){
    	this.votingcurrently=true;
    	//better managing the cooldowns in another class
    	this.playercooldown(sender);
    	final Player target = Bukkit.getServer().getPlayer(args[1]);
    	String reason = args[2];
    	
    	//converting args[3] to a string and pasting it inside reason.
    	int loopcount = 3;
    	while(!(loopcount==args.length)){
    		reason += " " + args[loopcount];
    		loopcount += 1;
    	}
    	
    	Bukkit.getLogger().info(Vote4TempBan.this.getConfig().getStringList("messages.votestart").toString());
    	
    	//some boring server messages
    	Bukkit.getServer().broadcastMessage("§c"+sender.getName()+"§a has started a vote to temporarily ban:");
    	Bukkit.getServer().broadcastMessage("§aPlayer: §c"+target.getName()+"§a for reason: §c"+reason);
    	Bukkit.getServer().broadcastMessage("§aTo vote, use: /vote yes/no");
    	Bukkit.getServer().broadcastMessage("§aThe vote will end in "+(Vote4TempBan.this.getConfig().getLong("timesettings.votelength")/20)/60+" min, be fast!");
    	
    	VotingTask = this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				Bukkit.getServer().broadcastMessage("§aThe vote ended with:");
				Bukkit.getServer().broadcastMessage("§ayes votes: §c"+yesvotes+"§a no votes: §c"+novotes);
				//here i check if the player will be banned or not
				if (yesvotes>novotes){
					//i want at least 2 yes votes, otherwise it's not fair
					if (yesvotes>1){
						target.kickPlayer("Think about what you've done and come back in "+(Vote4TempBan.this.getConfig().getLong("timesettings.bantime")/20)/60+" min");
						//i need to manage who is banned or not, so i can unban them onDisable()
						playersbanned.add(target.getName());
						Bukkit.getServer().broadcastMessage("§c"+target.getName()+"§a has been banned for "+(Vote4TempBan.this.getConfig().getLong("timesettings.bantime")/20)/60+" min");
						//i wanted to have this piece of code a bit clean, so i put this somewhere else
						aftervote(target);
					}
					else{
						Bukkit.getServer().broadcastMessage("§c"+target.getName()+"§a has been spared...");
					}
				}
				else if (yesvotes<=novotes){
					Bukkit.getServer().broadcastMessage("§c"+target.getName()+"§a has been spared...");
				}
				//resetting variables
				votingcurrently=false;
				yesvotes=0;
				novotes=0;
				playersvoted.clear();
			}
    	}, Vote4TempBan.this.getConfig().getInt("timesettings.votelength"));
    }
    
    public void aftervote(final Player target){
    	//this part will make sure the player gets unbanned after the specified time.
    	this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				playersbanned.remove(target.getDisplayName());
				Bukkit.getServer().broadcastMessage("§c"+target.getName()+"§a has been unbanned... Watch out!");
			}
    	}, Vote4TempBan.this.getConfig().getInt("timesettings.bantime"));
    }
    public void playercooldown(final CommandSender sender){
    	//first of all, add the player to the cooled down players list
    	playercooldown.add(sender.getName());
    	this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				//after an hour, remove it from the list
				playercooldown.remove(sender.getName());
			}
    	}, Vote4TempBan.this.getConfig().getInt("timesettings.votecooldown"));
    }
	
	public void cancelvote(CommandSender sender){
		votingcurrently=false;
		yesvotes=0;
		novotes=0;
		playersvoted.clear();
		Bukkit.getServer().broadcastMessage("§c"+sender.getName()+"§a has canceled the voting.");
		this.getServer().getScheduler().cancelTask(VotingTask);
	}
	
	public void ubanplayer(CommandSender sender, String playername){
		playersbanned.remove(playername);
		Bukkit.getServer().broadcastMessage("§c"+sender.getName()+"§c has ubanned §c"+ playername);
		Bukkit.getServer().broadcastMessage("§c"+playername+"§a has been unbanned... Watch out!");
	}
	
}
