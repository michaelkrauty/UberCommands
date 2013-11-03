package me.michaelkrauty.UberCommands;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
//import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class UberCommands extends JavaPlugin implements Listener{
	
	public static Set<String> hidingPlayers = new HashSet<String>();
	
	SettingsManager lang = SettingsManager.getInstance();
	
	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent event){
		if(!hidingPlayers.isEmpty()){
			for(String hiding : hidingPlayers){
				Player hidingPlayer = getServer().getPlayerExact(hiding);
				if(hidingPlayer != null){
					hidingPlayer.hidePlayer(event.getPlayer());
				}else{
					hidingPlayers.remove(hiding);
				}
			}
		}
	}	
	
	public UberCommands plugin;
	
	protected UpdateChecker updateChecker;
	
	public void checkPlayer(){
		
	}
	
	public void onEnable(){
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e){
		}
		
		SettingsManager.getInstance().setup(this);
		
		lang.saveLang();
		lang.reloadLang();
		
		getServer().getPluginManager().registerEvents(this, this);
		PluginDescriptionFile pdffile = this.getDescription();
		Server server = Bukkit.getServer();
		ConsoleCommandSender console = server.getConsoleSender();
		saveDefaultConfig();
		
		this.updateChecker = new UpdateChecker(this, "http://dev.bukkit.org/bukkit-plugins/ubercommands/files.rss");
		if(getConfig().getString("checkupdate") == "true"){
			if(this.updateChecker.updateNeeded()){
				console.sendMessage(ChatColor.GREEN + getConfig().getString("lang.on_plugin_enable.update_needed_line1").replace("<latestversion>", this.updateChecker.getVersion()).replace("<link>", this.updateChecker.getLink()));
				console.sendMessage(ChatColor.GREEN + getConfig().getString("lang.on_plugin_enable.update_needed_line2").replace("<latestversion>", this.updateChecker.getVersion()).replace("<link>", this.updateChecker.getLink()));
			}
			if(!this.updateChecker.updateNeeded()){
				console.sendMessage(ChatColor.GREEN + getConfig().getString("lang.on_plugin_enable.update_not_needed").replace("<currentversion>", pdffile.getVersion()));
			}
		}
		console.sendMessage(ChatColor.GREEN + "[UberCommands] Enabled UberCommands v" + pdffile.getVersion());
	}
	
	@SuppressWarnings({ "deprecation", "unused" })
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Server server = Bukkit.getServer();
		ConsoleCommandSender console = server.getConsoleSender();
		
		
		if(commandLabel.equalsIgnoreCase("uc") || commandLabel.equalsIgnoreCase("ubercommands") || commandLabel.equalsIgnoreCase("uber") || commandLabel.equalsIgnoreCase("u")){
			
			
			
			if(args.length == 0){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.no_arguments"));
					return true;
				}else{
					console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.no_arguments"));
					return true;
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("update")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(args.length == 1){
						if(sender.hasPermission("ubercommands.update")){
							if(this.updateChecker.updateNeeded()){
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.update.update_needed_line1").replace("<currentversion>", this.getDescription().getVersion()).replace("<latestversion>", this.updateChecker.getVersion()).replace("<link>", this.updateChecker.getLink()));
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.update.update_needed_line2").replace("<currentversion>", this.getDescription().getVersion()).replace("<latestversion>", this.updateChecker.getVersion()).replace("<link>", this.updateChecker.getLink()));
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.update.update_needed_line3").replace("<currentversion>", this.getDescription().getVersion()).replace("<latestversion>", this.updateChecker.getVersion()).replace("<link>", this.updateChecker.getLink()));
								return true;
							}
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
							return true;
						}
						if(!this.updateChecker.updateNeeded()){
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.update.update_not_needed").replace("<currentversion>", this.getDescription().getVersion()).replace("<latestversion>", this.updateChecker.getVersion()).replace("<link>", this.updateChecker.getLink()));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.update.incorrect_usage"));
						return true;
					}
				}else{
					if(args.length == 1){
						if(this.updateChecker.updateNeeded()){
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.update.update_needed_line1").replace("<currentversion>", this.getDescription().getVersion()).replace("<latestversion>", this.updateChecker.getVersion()).replace("<link>", this.updateChecker.getLink()));
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.update.update_needed_line2").replace("<currentversion>", this.getDescription().getVersion()).replace("<latestversion>", this.updateChecker.getVersion()).replace("<link>", this.updateChecker.getLink()));
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.update.update_needed_line3").replace("<currentversion>", this.getDescription().getVersion()).replace("<latestversion>", this.updateChecker.getVersion()).replace("<link>", this.updateChecker.getLink()));
							return true;
						}
						if(!this.updateChecker.updateNeeded()){
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.update.update_not_needed").replace("<currentversion>", this.getDescription().getVersion()).replace("<latestversion>", this.updateChecker.getVersion()).replace("<link>", this.updateChecker.getLink()));
							return true;
						}
					}else{
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.update.incorrect_usage"));
						return true;
					}
					return true;
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("help")){
				
				
				if(args.length == 1){
					sender.sendMessage(ChatColor.GOLD + "Hello!");
					sender.sendMessage(ChatColor.GOLD + "Formatting: <> is required. [] is optional. | specifies another alias for the command");
					sender.sendMessage(ChatColor.GOLD + "Commands:");
					sender.sendMessage(ChatColor.GOLD + "/uc help: Displays this help dialogue");
					sender.sendMessage(ChatColor.GOLD + "/uc vanish|hide <player>: Make a player disappear/reappear (Player only vanishes on your screen)");
					sender.sendMessage(ChatColor.GOLD + "/uc vanishall|hideall: Hide all players");
					sender.sendMessage(ChatColor.GOLD + "/uc whoami: remember who you are");
					sender.sendMessage(ChatColor.GOLD + "/uc tp <player>: Teleport to a player");
					sender.sendMessage(ChatColor.GOLD + "/uc tphere <player>: Teleport a player to you");
					sender.sendMessage(ChatColor.GOLD + "/uc heal [player]: Heal yourself");
					sender.sendMessage(ChatColor.GOLD + "Use /uc help 2 for the next page");
					return true;
				}
				if(args.length == 2 && args[1].equalsIgnoreCase("2")){
					sender.sendMessage(ChatColor.GOLD + "/uc feed [player]: Satisfy that tingling sensation");
					sender.sendMessage(ChatColor.GOLD + "/uc starve [player]: Burn a hole in your stomach");
					sender.sendMessage(ChatColor.GOLD + "/uc ip <player>: Get the IP of a player");
					sender.sendMessage(ChatColor.GOLD + "/uc clearinventory|clearinv|ci [player]: Clear your inventory");
					sender.sendMessage(ChatColor.GOLD + "/uc slap <player>: Smack 'em hard");
					sender.sendMessage(ChatColor.GOLD + "/uc creative|c [player]: Get creative up in here");
					sender.sendMessage(ChatColor.GOLD + "/uc survival|s [player]: Go back to survival");
					sender.sendMessage(ChatColor.GOLD + "/uc fly [player]: Fly!");
					sender.sendMessage(ChatColor.GOLD + "/uc kill <player>: Hurt them just a bit too much");
					sender.sendMessage(ChatColor.GOLD + "/uc trash: Dispose of the item you're holding");
					sender.sendMessage(ChatColor.GOLD + "Use /uc help 3 for the next page");
					return true;
				}
				if(args.length == 2 && args[1].equalsIgnoreCase("3")){
					sender.sendMessage(ChatColor.GOLD + "/uc arrow: Pew pew pew!");
					sender.sendMessage(ChatColor.GOLD + "/uc snowball: Snowball fight!");
					sender.sendMessage(ChatColor.GOLD + "/uc location|locate|loc <player>: Locate a player");
					sender.sendMessage(ChatColor.GOLD + "/uc position|pos: Find yourself");
					sender.sendMessage(ChatColor.GOLD + "/uc update: check for UberCommands updates");
					sender.sendMessage(ChatColor.GOLD + "/uc latestversion: Get the latest version of UberCommands");
					sender.sendMessage(ChatColor.GOLD + "/uc reload: Reload UberCommands config");
					return true;
				}else{
					sender.sendMessage(ChatColor.GOLD + "That page doesn't exist (yet)!");
					return true;
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("reload")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(args.length == 1){
						if(sender.hasPermission("ubercommands.reload") || sender.hasPermission("ubercommands.use")){
							reloadConfig();
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.reload.player_message_reloaded").replace("<currentversion>", this.getDescription().getVersion()));
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.reload.player_message_reloaded").replace("<currentversion>", this.getDescription().getVersion()));
							return true;
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied").replace("<currentversion>", this.getDescription().getVersion()));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.reload.incorrect_usage").replace("<currentversion>", this.getDescription().getVersion()));
						return true;
					}
				}else{
					if(args.length == 1){
						reloadConfig();
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.reload.player_message_reloaded").replace("<currentversion>", this.getDescription().getVersion()));
						return true;
					}else{
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.reload.incorrect_usage").replace("<currentversion>", this.getDescription().getVersion()));
						return true;
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("latestversion")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(args.length == 1){
						if(sender.hasPermission("ubercommands.latestversion") || sender.hasPermission("ubercommands.use")){
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.latestversion.latest_version").replace("<currentversion>", this.getDescription().getVersion()).replace("<latestversion>", this.updateChecker.getVersion()).replace("<link>", this.updateChecker.getLink()));
							return true;
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.latestversion.incorrect_usage"));
						return true;
					}
				}else{
					if(args.length == 1){
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.latestversion.latest_version").replace("<currentversion>", this.getDescription().getVersion()).replace("<latestversion>", this.updateChecker.getVersion()).replace("<link>", this.updateChecker.getLink()));
						return true;
					}else{
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.latestversion.incorrect_usage"));
						return true;
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("vanish") || args[0].equalsIgnoreCase("hide")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.vanish")){
						if(args.length == 2){
							if(player.getServer().getPlayer(args[1]) != null){
								Player target = sender.getServer().getPlayer(args[1]);
								String targetName = target.getName();
								if(!target.hasPermission("ubercommands.vanish.exempt")){
									if(player.canSee(target)){
										
										player.hidePlayer(target);
										player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.vanish.player_message_hidden").replace("<targetname>", targetName).replace("<playername>", playerName));
										return true;
									}
									if(!player.canSee(target)){
										player.showPlayer(target);
										player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.vanish.player_message_unhidden").replace("<targetname>", targetName).replace("<playername>", playerName));
										return true;
									}
								}else{
									sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
									return true;
								}
							}else{
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
								return true;
							}
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.vanish.incorrect_usage"));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
						return true;
					}
				}else{
					console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.vanish.executed_as_console_error"));
					return true;
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("friend")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.friend")){
						if(getServer().getPlayer(args[1]) instanceof Player){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							if(!player.canSee(target)){
								player.showPlayer(target);
								player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.friend.player_message_friended").replace("<targetname>", targetName).replace("<playername>", playerName));
								return true;
							}
							if(player.canSee(target)){
								player.hidePlayer(target);
								player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.friend.player_message_unfriended").replace("<targetname>", targetName).replace("<playername>", playerName));
								return true;
							}
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
							return true;
						}
					}else{
						player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
						return true;
					}
				}else{
					console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.friend.executed_as_console_error"));
					return true;
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("vanishall") || args[0].equalsIgnoreCase("hideall")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.vanishall")){
						if(!hidingPlayers.contains(player.getName())){
							hidingPlayers.add(player.getName());
							for (Player online : getServer().getOnlinePlayers()){
								player.hidePlayer(online);
							}
							player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.vanishall.players_have_been_hidden"));
						}else{
							hidingPlayers.remove(player.getName());
							for (Player online : getServer().getOnlinePlayers()){
								if (!player.canSee(online)){
									player.showPlayer(online);
								}
							}
							player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.vanishall.players_have_been_unhidden"));
						}
						return true;
					}
				}else{
					console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.vanishall.executed_as_console_error"));
					return true;
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("whoami")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.whoami")){
						if(args.length == 1){
							String playername = player.getName();
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.whoami.player_message_whoami_line1").replace("<playername>", playerName));
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.whoami.player_message_whoami_line2").replace("<playername>", playerName));
							return true;
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.whoami.incorrect_usage"));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
						return true;
					}
				}else{
					console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.whoami.executed_as_console_error"));
					return true;
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("tp")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.tp")){
						if(args.length == 2){
							if(player.getServer().getPlayer(args[1]) instanceof Player){
								Player target = player.getServer().getPlayer(args[1]);
								String targetName = target.getName();
								Location targetLocation = target.getLocation();
								
								player.teleport(targetLocation);
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.tp.player_message").replace("<targetname>", targetName).replace("<playername>", playerName));
								target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.tp.target_message").replace("<playername>", playerName).replace("<targetname>", targetName));
								return true;
							}else{
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
								return true;
							}
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
						return true;
					}
				}else{
					console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.tp.executed_as_console_error"));
					return true;
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("tphere")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.tphere")){
						if(args.length == 2){
							if(player.getServer().getPlayer(args[1]) != null){
								Player target = player.getServer().getPlayer(args[1]);
								String targetName = target.getName();
								Location playerlocation = player.getLocation();
								
								target.teleport(playerlocation);
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.tphere.player_message").replace("<playername>", playerName).replace("<targetname>", targetName));
								target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.tphere.target_message").replace("<playername>", playerName).replace("<targetname>", targetName));
								return true;
							}else{
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
								return true;
							}
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.tphere.incorrect_usage"));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
						return true;
					}
				}else{
					console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.tphere.executed_as_console_error"));
					return true;
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("heal")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.heal")){
						if(args.length == 1){
							player.setHealth(20);
							player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.heal.player_message_healed_self").replace("<playername>", playerName));
							return true;
						}
						if(args.length == 2){
							if(player.getServer().getPlayer(args[1]) instanceof Player){
								Player target = sender.getServer().getPlayer(args[1]);
								String targetName = target.getName();
								target.setHealth(20);
								player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.heal.player_message_healed_player").replace("<playername>", playerName).replace("<targetname>", targetName));
								target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.heal.target_message_healed").replace("<playername>", playerName).replace("<targetname>", targetName));
								return true;
							}else{
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
								return true;
							}
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.heal.incorrect_usage").replace("<playername>", playerName));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
						return true;
					}
				}else{
					if(args.length == 1){
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.heal.executed_as_console_error"));
						return true;
					}
					if(args.length == 2){
						if(getServer().getPlayer(args[1]) instanceof Player){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							target.setHealth(20);
							console.sendMessage(ChatColor.GOLD + targetName + " was healed!");
							target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.heal.target_message_healed_by_console").replace("<targetname>", targetName));
							return true;
						}else{
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
							return true;
						}
					}else{
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.heal.incorrect_usage"));
						return true;
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("feed")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.feed")){
						if(args.length == 1){
							player.setFoodLevel(20);
							player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.feed.player_message_fed_self").replace("<playername>", playerName));
							return true;
						}
						if(args.length == 2){
							if(player.getServer().getPlayer(args[1]) instanceof Player){
								Player target = sender.getServer().getPlayer(args[1]);
								String targetName = target.getName();
								target.setFoodLevel(20);
								target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.feed.target_message_fed").replace("<playername>", playerName).replace("<targetname>", targetName));
								player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.feed.player_message_fed_player").replace("<playername>", playerName).replace("<targetname>", targetName));
								return true;
							}else{
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found").replace("<playername>", playerName));
								return true;
							}
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.feed.incorrect_usage").replace("<playername>", playerName));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied").replace("<playername>", playerName));
						return true;
					}
				}else{
					if(args.length == 1){
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.feed.executed_as_console_error"));
						return true;
					}
					if(args.length == 2){
						if(getServer().getPlayer(args[1]) instanceof Player){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							target.setFoodLevel(20);
							target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.feed.target_message_fed_by_console").replace("<targetname>", targetName));
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.feed.player_message_fed_player").replace("<targetname>", targetName));
							return true;
						}else{
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
							return true;
						}
					}else{
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.feed.incorrect_usage"));
						return true;
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("starve")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.starve")){
						if(args.length == 1){
							player.setFoodLevel(0);
							player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.starve.player_message_starved_self").replace("<playername>", playerName));
							return true;
						}
						if(args.length == 2){
							if(player.getServer().getPlayer(args[1]) instanceof Player){
								Player target = sender.getServer().getPlayer(args[1]);
								String targetName = target.getName();
								target.setFoodLevel(0);
								player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.starve.player_message_starved_player").replace("<playername>", playerName).replace("<targetname>", targetName));
								target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.starve.target_message_starved").replace("<playername>", playerName).replace("<targetname>", targetName));
								return true;
							}else{
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found").replace("<playername>", playerName));
								return true;
							}
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied").replace("<playername>", playerName));
						return true;
					}
				}else{
					if(args.length == 1){
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.starve.executed_by_console_error"));
						return true;
					}
					if(args.length == 2){
						if(getServer().getPlayer(args[1]) instanceof Player){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							target.setFoodLevel(0);
							target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.starve.target_message_starved_by_console").replace("<targetname>", targetName));
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.starve.player_message_starved_player").replace("<targetname>", targetName));
							return true;
						}else{
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
							return true;
						}
					}else{
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.starve.incorrect_usage"));
						return true;
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("ip")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.ip")){
						InetSocketAddress playerAddress = player.getAddress();
						if(args.length == 1){
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.ip.player_message_ip_self").replace("<playername>", playerName).replace("<player_ip>", playerAddress.toString()));
							return true;
						}
						if(args.length == 2){
							if(sender.getServer().getPlayer(args[1]) instanceof Player){
								Player target = sender.getServer().getPlayer(args[1]);
								String targetName = target.getName();
								InetSocketAddress targetAddress = target.getAddress();
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.ip.player_message_ip_player").replace("<playername>", playerName).replace("<targetname>", targetName).replace("<player_ip>", playerAddress.toString()).replace("<target_ip>", targetAddress.toString()));
								return true;
							}else{
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found").replace("<playername>", playerName).replace("<player_ip>", playerAddress.toString()));
								return true;
							}
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.ip.incorrect_usage").replace("<playername>", playerName).replace("<player_ip>", playerAddress.toString()));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied").replace("<playername>", playerName));
						return true;
					}
				}else{
					if(args.length == 1){
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.ip.executed_as_console_error"));
						return true;
					}
					if(args.length == 2){
						if(getServer().getPlayer(args[1]) instanceof Player){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							InetSocketAddress targetAddress = target.getAddress();
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.ip.player_message_ip_player").replace("<targetname>", targetName).replace("<target_ip>", targetAddress.toString()));
							return true;
						}else{
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
							return true;
						}
					}else{
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.ip.incorrect_usage_console"));
						return true;
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("clearinventory") || args[0].equalsIgnoreCase("clearinv") || args[0].equalsIgnoreCase("ci")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.ci")){
						if(args.length == 1){
							player.getInventory().clear();
							player.getInventory().setArmorContents(null);
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.clearinventory.player_message_clearinventory_self").replace("<playername>", playerName));
							return true;
						}
						if(args.length == 2){
							if(sender.getServer().getPlayer(args[1]) instanceof Player){
								Player target = player.getServer().getPlayer(args[1]);
								String targetName = target.getName();
								target.getInventory().clear();
								target.getInventory().setArmorContents(null);
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.clearinventory.player_message_clearinventory_player").replace("<playername>", playerName).replace("<targetname>", targetName));
								target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.clearinventory.target_message_clearinventory").replace("<playername>", playerName).replace("<targetname>", targetName));
								return true;
							}else{
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found").replace("<playername>", playerName));
								return true;
							}
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.clearinventory.incorrect_usage").replace("<playername>", playerName));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied").replace("<playername>", playerName));
						return true;
					}
				}else{
					if(args.length == 1){
					console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.clearinventory.executed_as_console_error"));
					return true;
				}
					if(args.length == 2){
						if(getServer().getPlayer(args[1]) instanceof Player){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							target.getInventory().clear();
							target.getInventory().setArmorContents(null);
							target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.clearinventory.target_message_clearinventory").replace("<targetname>", targetName));
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_message_clearinventory_player").replace("<targetname>", targetName));
							return true;
						}else{
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
							return true;
						}
					}else{
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.clearinventory.incorrect_usage_console"));
						return true;
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("slap")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.slap")){
						ItemStack fish = new ItemStack(Material.RAW_FISH, 1);
						if(args.length == 2){
							if(sender.getServer().getPlayer(args[1]) instanceof Player){
								Player target = sender.getServer().getPlayer(args[1]);
								String targetName = target.getName();
								target.damage(1);
								player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.slap.player_message_slap_player").replace("<playername>", playerName).replace("<targetname>", targetName));
								target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.slap.target_message_slap").replace("<playername>", playerName).replace("<targetname>", targetName));
								if(getConfig().getString("wetfish") == "true"){
									target.getInventory().addItem(fish);
									return true;
								}
							}else{
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found").replace("<playername>", playerName));
								return true;
							}
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.slap.incorrect_usage").replace("<playername>", playerName));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied").replace("<playername>", playerName));
						return true;
					}
				}else{
					ItemStack fish = new ItemStack(Material.RAW_FISH, 1);
					if(args.length == 2){
						if(getServer().getPlayer(args[1]) instanceof Player){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							target.damage(1);
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.slap.player_message_slap_player").replace("<targetname>", targetName));
							target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.slap.target_message_slapped_by_console").replace("<targetname>", targetName));
							if(getConfig().getString("wetfish") == "true"){
								target.getInventory().addItem(fish);
								return true;
							}
						}else{
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
							return true;
						}
					}else{
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.slap.incorrect_usage"));
						return true;
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("creative")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.creative")){
						if(args.length == 1){
							if(player.getGameMode().equals(GameMode.CREATIVE)){
								player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.creative.player_message_already_in_creative").replace("<playername>", playerName));
								return true;
							}
							player.setGameMode(GameMode.CREATIVE);
							player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.creative.player_message_gamemode_creative").replace("<playername>", playerName));
							return true;
						}
						if(sender.hasPermission("ubercommands.creative.others")){
							if(args.length == 2){
								if(sender.getServer().getPlayer(args[1]) instanceof Player){
									Player target = sender.getServer().getPlayer(args[1]);
									String targetName = target.getName();
									if(target.getGameMode().equals(GameMode.CREATIVE)){
										player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.creative.player_message_player_already_in_creative").replace("<playername>", playerName).replace("<targetname>", targetName));
										return true;
									}
									target.setGameMode(GameMode.CREATIVE);
									player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.creative.player_message_gamemode_creative_player").replace("<playername>", playerName).replace("<targetname>", targetName));
									target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.creative.target_message_gamemode_creative").replace("<playername>", playerName).replace("<targetname>", targetName));
									return true;
								}else{
									sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found").replace("<playername>", playerName));
									return true;
								}
							}else{
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.creative.incorrect_usage").replace("<playername>", playerName));
								return true;
							}
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied").replace("<playername>", playerName));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied").replace("<playername>", playerName));
						return true;
					}
				}else{
					if(args.length == 1){
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.executed_as_console_error"));
						return true;
					}
					if(args.length == 2){
						if(getServer().getPlayer(args[1]) instanceof Player){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							if(target.getGameMode().equals(GameMode.CREATIVE)){
								console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.creative.player_message_player_already_in_creative").replace("<targetname>", targetName));
								return true;
							}
							target.setGameMode(GameMode.CREATIVE);
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.creative.player_message_gamemode_creative_player").replace("<targetname>", targetName));
							target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.creative.target_message_gamemode_creative").replace("<targetname>", targetName));
							return true;
						}else{
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
							return true;
						}
					}else{
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.creative.incorrect_usage_console"));
						return true;
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("survival")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.survival")){
						if(args.length == 1){
							if(player.getGameMode().equals(GameMode.SURVIVAL)){
								player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.survival.player_message_already_in_survival").replace("<playername>", playerName));
								return true;
							}
							player.setGameMode(GameMode.SURVIVAL);
							player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.survival.player_message_gamemode_survival").replace("<playername>", playerName));
							return true;
						}
						if(sender.hasPermission("ubercommands.survival.others")){
							if(args.length == 2){
								if(sender.getServer().getPlayer(args[1]) instanceof Player){
									Player target = sender.getServer().getPlayer(args[1]);
									String targetName = target.getName();
									if(target.getGameMode().equals(GameMode.SURVIVAL)){
										player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.survival.player_message_player_already_in_survival").replace("<playername>", playerName).replace("<targetname>", targetName));
										return true;
									}
									target.setGameMode(GameMode.SURVIVAL);
									player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.survival.player_message_survival_player").replace("<playername>", playerName).replace("<targetname>", targetName));
									target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.survival.target_message_gamemode_survival").replace("<playername>", playerName).replace("<targetname>", targetName));
									return true;
								}else{	
									player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found").replace("<playername>", playerName));
									return true;
								}
							}else{
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.incorrect_usage").replace("<playername>", playerName));
								return true;
							}
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied").replace("<playername>", playerName));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied").replace("<playername>", playerName));
						return true;
					}
				}else{
					if(args.length == 1){
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.execute_as_console_error"));
						return true;
					}
					if(args.length == 2){
						if(getServer().getPlayer(args[1]) instanceof Player){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							if(target.getGameMode().equals(GameMode.SURVIVAL)){
								console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.survival.player_message_player_already_in_survival").replace("<targetname>", targetName));
								return true;
							}
							target.setGameMode(GameMode.SURVIVAL);
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.survival.player_message_gamemode_survival").replace("<targetname>", targetName));
							target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.survival.target_message_gamemode_survival").replace("<targetname>", targetName));
							return true;
						}else{
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
							return true;
						}
					}else{
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.survival.incorrect_usage_console"));
						return true;
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("fly")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.fly")){
						if(args.length == 1){
							if(player.getAllowFlight() == false){
								player.setAllowFlight(true);
								player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.fly.player_message_fly").replace("<playername>", playerName));
								return true;
							}
							if(player.getAllowFlight() == true){
								player.setAllowFlight(false);
								player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.fly.player_message_fly_disabled").replace("<playername>", playerName));
								return true;
							}
						}
						if(player.hasPermission("ubercommands.fly.others")){
							if(args.length == 2){
								if(sender.getServer().getPlayer(args[1]) instanceof Player){
									Player target = player.getServer().getPlayer(args[1]);
									String targetName = target.getName();
									if(target.getAllowFlight() == false){
										target.setAllowFlight(true);
										player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.fly.player_message_fly_player").replace("<playername>", playerName).replace("<targetname>", targetName));
										target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.fly.target_message_fly").replace("<playername>", playerName).replace("<targetname>", targetName));
										return true;
									}
									if(target.getAllowFlight() == true){
										target.setAllowFlight(false);
										player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.fly.player_message_fly_player_disabled").replace("<playername>", playerName).replace("<targetname>", targetName));
										target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.fly.target_message_fly_disabled").replace("<playername>", playerName).replace("<targetname>", targetName));
										return true;
									}
								}else{
									sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found").replace("<playername>", playerName));
									return true;
								}
							}else{
								player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.fly.incorrect_usage").replace("<playername>", playerName));
								return true;
							}
						}else{
							player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied").replace("<playername>", playerName));
							return true;
						}
					}else{
						player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied").replace("<playername>", playerName));
						return true;
					}
				}else{
					if(args.length == 1){
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.fly.executed_as_console_error"));
						return true;
					}
					if(args.length == 2){
						if(getServer().getPlayer(args[1]) instanceof Player){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							if(target.getAllowFlight() == false){
								target.setAllowFlight(true);
								console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.fly.player_message_fly_player").replace("<targetname>", targetName));
								target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.target_message_fly").replace("<targetname>", targetName));
								return true;
							}
							target.setAllowFlight(false);
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.fly.player_message_fly_player_disabled").replace("<targetname>", targetName));
							target.sendMessage(ChatColor.GOLD + getConfig().getString("lang.fly.target_message_fly_disabled").replace("<targetname>", targetName));
							return true;
						}else{
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
							return true;
						}
					}else{
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.incorrect_usage_console"));
						return true;
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("kill")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.kill")){
						if(args.length == 1){
							player.setHealth(0);
							player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.kill.player_message_kill").replace("<playername>", playerName));
							return true;
						}
						if(args.length == 2){
							if(getServer().getPlayer(args[1]) instanceof Player){
								Player target = player.getServer().getPlayer(args[1]);
								String targetName = target.getName();
								target.setHealth(0);
								player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.kill.player_message_kill_player").replace("<playername>", playerName).replace("<targetname>", targetName));
								return true;
							}else{
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found").replace("<playername>", playerName));
								return true;
							}
						}else{
							player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.kill.incorrect_usage").replace("<playername>", playerName));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied").replace("<playername>", playerName));
						return true;
					}
				}else{
					if(args.length == 1){
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.kill.player_message_kill_console"));
						return true;
					}
					if(args.length == 2){
						if(getServer().getPlayer(args[1]) instanceof Player){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							if(target.getGameMode().equals(GameMode.SURVIVAL)){
								target.setHealth(0);
								console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.kill.player_message_kill_player").replace("<targetname>", targetName));
								return true;
							}
						}else{
							console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.kill.player_not_found"));
							return true;
						}
					}else{
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.kill.incorrect_usage_console"));
						return true;
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("trash")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.trash")){
						if(args.length == 1){
							player.setItemInHand(null);
							player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.trash.player_message_arrow").replace("<playername>", playerName));
							return true;
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.trash.incorrect_usage").replace("<playername>", playerName));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
						return true;
					}
				}else{
					console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.executed_as_Console_error"));
					return true;
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("arrow")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.arrow")){
						if(args.length == 1){
							player.shootArrow();
							player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.arrow.player_message_snowball").replace("<playername>", playerName));
							return true;
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.arrow.incorrect_usage").replace("<playername>", playerName));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
						return true;
					}
				}else{
					console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.arrow.executed_as_console_error"));
					return true;
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("snowball")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.snowball")){
						if(args.length == 1){
							player.throwSnowball();
							player.sendMessage(ChatColor.GOLD + getConfig().getString("lang.snowball.player_message_snowball").replace("<playername>", playerName));
							return true;
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.snowball.incorrect_message").replace("<playername>", playerName));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
						return true;
					}
				}else{
					console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.snowball.executed_as_console_error"));
					return true;
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("location") || args[0].equalsIgnoreCase("locate") || args[0].equalsIgnoreCase("loc")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.locate")){
						if(sender.getServer().getPlayer(args[1]) instanceof Player){
							if(args.length == 2){
								Player target = sender.getServer().getPlayer(args[1]);
								String targetName = target.getName();
								if(!target.hasPermission("ubercommands.locate.exempt")){
									int x = target.getLocation().getBlockX();
									int y = target.getLocation().getBlockY();
									int z = target.getLocation().getBlockZ();
									String world = target.getWorld().getName();
									
									String blockx = String.valueOf(x);
									String blocky = String.valueOf(y);
									String blockz = String.valueOf(z);
									
									sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.location.player_message_location_line1").replace("<targetname>", targetName).replace("<playername>", playerName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
									sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.location.player_message_location_line2").replace("<targetname>", targetName).replace("<playername>", playerName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
									sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.location.player_message_location_line3").replace("<targetname>", targetName).replace("<playername>", playerName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
									sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.location.player_message_location_line4").replace("<targetname>", targetName).replace("<playername>", playerName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
									sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.location.player_message_location_line5").replace("<targetname>", targetName).replace("<playername>", playerName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
									return true;
								}else{
									sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
									return true;
								}
							}else{
								sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.location.incorrect_usage"));
								return true;
							}
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.player_not_found"));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
						return true;
					}
				}else{
					if(args.length == 2){
						Player target = getServer().getPlayer(args[1]);
						String targetName = target.getName();
						int x = target.getLocation().getBlockX();
						int y = target.getLocation().getBlockY();
						int z = target.getLocation().getBlockZ();
						String world = target.getWorld().getName();
						
						String blockx = String.valueOf(x);
						String blocky = String.valueOf(y);
						String blockz = String.valueOf(z);
						
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.location.player_message_location_line1").replace("<targetname>", targetName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.location.player_message_location_line2").replace("<targetname>", targetName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.location.player_message_location_line3").replace("<targetname>", targetName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.location.player_message_location_line4").replace("<targetname>", targetName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.location.player_message_location_line5").replace("<targetname>", targetName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
						return true;
					}else{
						console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.location.incorrect_usage"));
						return true;
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("position") || args[0].equalsIgnoreCase("pos")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.position")){
						if(args.length == 1){
							int x = player.getLocation().getBlockX();
							int y = player.getLocation().getBlockY();
							int z = player.getLocation().getBlockZ();
							String world = player.getWorld().getName();

							String blockx = String.valueOf(x);
							String blocky = String.valueOf(y);
							String blockz = String.valueOf(z);
							
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.position.player_message_position_line1").replace("<targetname>", playerName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.position.player_message_position_line2").replace("<targetname>", playerName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.position.player_message_position_line3").replace("<targetname>", playerName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.position.player_message_position_line4").replace("<targetname>", playerName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.position.player_message_position_line5").replace("<targetname>", playerName).replace("<x>", blockx).replace("<y>", blocky).replace("<z>", blockz).replace("<world>", world));
							return true;
						}else{
							sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.position.incorrect_usage"));
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
						return true;
					}
				}else{
					console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.position.executed_as_console_error"));
					return true;
				}
			}
			
			
			
/*			if(args[0].equalsIgnoreCase("ban")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					String playerDisplayName = player.getDisplayName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.ban")){
						if(args.length == 2){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							String targetDisplayName = target.getDisplayName();
							if(getServer().getPlayer(args[1]) instanceof Player){
								target.kickPlayer("The ban hammer has spoken!");
								target.setBanned(true);
								getServer().broadcastMessage(ChatColor.GOLD + "Player " + playerDisplayName + ChatColor.GOLD + " has banned " + targetDisplayName + ChatColor.GOLD + " from the server!");
							}
						}
					}
				}else{
					if(args.length == 2){
						Player target = getServer().getPlayer(args[1]);
						String targetName = target.getName();
						String targetDisplayName = target.getDisplayName();
						if(getServer().getPlayer(args[1]) instanceof Player){
							target.kickPlayer("The Ban Hammer has spoken!");
							target.setBanned(true);
						}
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("unban")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					String playerDisplayName = player.getDisplayName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.unban")){
						if(args.length == 2){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							String targetDisplayName = target.getDisplayName();
							if(getServer().getPlayer(args[1]) instanceof Player){
								target.setBanned(false);
								getServer().broadcastMessage(ChatColor.GOLD + "Player " + playerDisplayName + ChatColor.GOLD + " has unbanned " + targetDisplayName + ChatColor.GOLD + "!");
							}
						}
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("kick")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.kick")){
						if(getServer().getPlayer(args[1]) instanceof Player){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							target.kickPlayer("Kicked.");
							return true;
						}
					}
					
					
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("fakeban")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.fakeban")){
						if(args.length == 2){
							if(getServer().getPlayer(args[1]) instanceof Player){
								String playerDisplayName = player.getDisplayName();
								Player target = getServer().getPlayer(args[1]);
								String targetName = target.getName();
								String targetDisplayName = target.getDisplayName();
								
								getServer().broadcastMessage(ChatColor.GOLD + "Player " + playerDisplayName + ChatColor.GOLD + " banned " + targetDisplayName + ChatColor.GOLD + " for The Ban Hammer has spoken!.");
								target.kickPlayer("The Ban Hammer has spoken!");
								return true;
							}else{
								sender.sendMessage("Couldn't find that player!");
								return true;
							}
						}else{
							sender.sendMessage("Usage: /uc fakeban <player>");
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.permission_denied"));
						return true;
					}
				}else{
					if(args.length == 2){
						if(getServer().getPlayer(args[1]) instanceof Player){
							Player target = getServer().getPlayer(args[1]);
							String targetName = target.getName();
							String targetDisplayName = target.getDisplayName();
							getServer().broadcastMessage(ChatColor.GOLD + "Player " + ChatColor.RED + "Console " + ChatColor.GOLD + "banned " + targetDisplayName + ChatColor.GOLD + " for The Ban Hammer has spoken!.");
							target.kickPlayer("The Ban Hammer has spoken!");
						}else{
							console.sendMessage("Couldn't find that player!");
							return true;
						}
					}else{
						console.sendMessage("Usage: /uc fakeban <player");
						return true;
					}
				}
			}
			
			
			
			if(args[0].equalsIgnoreCase("messwith")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					String playerName = player.getName();
					
					
					if(sender.hasPermission("ubercommands.use") || sender.hasPermission("ubercommands.messwith")){
						if(args.length == 2){
							if(getServer().getPlayer(args[1]) instanceof Player){
								Player target = getServer().getPlayer(args[1]);
								String targetName = target.getName();
								
								Location targetLocation = target.getLocation();
								player.getWorld().spawnCreature(targetLocation, EntityType.CHICKEN);
								return true;
							}
						}
					}
				}
			}*/
			
			
			
			
		}
		
		
		
		if(sender instanceof Player){
			Player player = (Player) sender;
			String playerName = player.getName();
			
			
			sender.sendMessage(ChatColor.GOLD + getConfig().getString("lang.unknown_command"));
			return true;
		}else{
			console.sendMessage(ChatColor.GOLD + getConfig().getString("lang.unknown_command"));
			return true;
		}
	}
}