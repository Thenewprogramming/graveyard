package com.thenewprogramming.Bukkit.Vote4TempBan;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Vote4TempBanCommandExecutor implements CommandExecutor {
	
	private Vote4TempBan plugin;
	
	public Vote4TempBanCommandExecutor(Vote4TempBan plugin){
		this.plugin = plugin;
	}
	
	public boolean startvote(CommandSender sender, String[] args){
		if (!(plugin.getplayercooldown().contains(sender.getName()))){
			//if there's a voting going on, return true.
			if (!plugin.getvotingcurrently()){
				//player has to exist :P
				if (!(Bukkit.getServer().getPlayer(args[1]) == null)){
					if(args.length > 1){
						//i didn't want to make a big mess here, so i put it inside the main class.
						plugin.startvote(sender, args);
						return true;
					}
					else{
						return false;
					}
				}
				else{
					sender.sendMessage("§aThis player is not online");
					//there is no need to show the correct syntax if there's no error with that
					return true;
				}
			}
			else{
				sender.sendMessage("§aThere is already a voting going on");
				//there is no need to show the correct syntax if there's no error with that
				return true;
			}
		}
		else{
			sender.sendMessage("§aYou need to wait at least "+((plugin.getConfig().getLong("timesettings.votecooldown")/20)/60)+" min before starting a new vote");
			//there is no need to show the correct syntax if there's no error with that
			return true;
		}
	}
	
	public void voteyes(CommandSender sender){
		if (plugin.getvotingcurrently()){
			//check if the player has already voted
			if (plugin.getplayersvoted().contains(sender.getName())){
				sender.sendMessage("§aYou already voted");
			}
			else{
				ArrayList<String> newplayersvoted = plugin.getplayersvoted();
				newplayersvoted.add(sender.getName());
				plugin.setplayersvoted(newplayersvoted);
				plugin.setyesvotes(plugin.getyesvotes()+1);
				sender.sendMessage("§aYou voted yes");
			}
		
		}
		else{
			sender.sendMessage("§aThere is no voting going on");
		}
	}
	
	public void voteno(CommandSender sender){
		if (plugin.getvotingcurrently()){
			//check if the player has already voted
			if (plugin.getplayersvoted().contains(sender.getName())){
				sender.sendMessage("§aYou already voted");
			}
			else{
				ArrayList<String> newplayersvoted = plugin.getplayersvoted();
				newplayersvoted.add(sender.getName());
				plugin.setplayersvoted(newplayersvoted);
				plugin.setnovotes(plugin.getnovotes()+1);
				sender.sendMessage("§aYou voted no");
			}
		}
		else{
			sender.sendMessage("§aThere is no voting going on.");
		}
	}
	
	public void cancelvote(CommandSender sender){
		if(plugin.getvotingcurrently()){
			plugin.cancelvote(sender);
		}
		else{
			sender.sendMessage("§aThere is no voting going on.");
		}
	}
	
	public boolean unbanplayer(CommandSender sender, String[] args){
		if(args[1]!= null){
			if(plugin.getplayersbanned().contains(args[1])){
				plugin.ubanplayer(sender, args[1]);
                return true;
			}
			else{
                sender.sendMessage("§aThat player isn't banned.");
                return true;
            }
		}
        return false;
	}
	
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(cmd.getName().equalsIgnoreCase("vote")){
            if(args[0].equalsIgnoreCase("yes")){
                if (checkPermission("Vote4TempBan.vote", sender)){
                   voteyes(sender);
                }
                return true;
            }
            
            else if(args[0].equalsIgnoreCase("no")){
                if (checkPermission("Vote4TempBan.vote", sender)){
                   voteno(sender);
                }
                return true;
            }
            
            else if(args[0].equalsIgnoreCase("start")){
                if (checkPermission("Vote4TempBan.start", sender)){
                   return startvote(sender, args);
                }
                return true;
            }
            
            else if(args[0].equalsIgnoreCase("cancel")){
                if (checkPermission("Vote4TempBan.cancel", sender)){
                    cancelvote(sender);
                }
                return true;
            }
            
            else if(args[0].equalsIgnoreCase("unban")){
                if (checkPermission("Vote4TempBan.unban", sender)){
                    return unbanplayer(sender, args);
                }
                return true;
            }
            else{
            	return false;
            }
        }
    	return false; 
    }

    public boolean checkPermission(String permission, CommandSender sender){
        if (sender.hasPermission(permission)){
            return true;
        }
        else{
            sender.sendMessage("§aYou do not have permission to do that");
            return false;
        }
    }

}
