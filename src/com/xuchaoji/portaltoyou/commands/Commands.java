package com.xuchaoji.portaltoyou.commands;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.xuchaoji.portaltoyou.PortalToYou;

public class Commands implements CommandExecutor  {
	private Plugin plugin=PortalToYou.getPlugin(PortalToYou.class);
	public String pyMainCmd = "py";
	public String acceptPy = "accept";
	public String denyPy = "deny";
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			boolean flag = true;
			if(args.length==1 && !args[0].equalsIgnoreCase(acceptPy) && !args[0].equalsIgnoreCase(denyPy)) {
				Collection<? extends Player> onlinePlayer = plugin.getServer().getOnlinePlayers();
				for(Player player2:onlinePlayer) {
					player.sendMessage("[PortalToYou] 正在搜寻玩家，搜索到"+player2.getName());
					if(args[0].equals(player2.getName())) {
						flag = false;
						//获取目标玩家的location
						setPlayerLocation(player2);
						setSourcePlayer(player, player2);
						//通知想要发起py的玩家
						player.sendMessage(ChatColor.GREEN+"[portalToYou] 已经向"+ChatColor.GREEN+player2+ChatColor.GREEN+"发送py请求，等待对方接受py");
						//通知目标玩家
						player2.sendMessage(ChatColor.GREEN+"[portalToYou]"+ChatColor.YELLOW+player.getName()+ChatColor.GREEN+"尝试与你py，输入"+
						ChatColor.GOLD+"py accept"+ChatColor.GREEN+"接受，"+
						ChatColor.GOLD+"py deny "+ChatColor.GREEN+"拒绝。");
					}
				}
				if(flag) {
					player.sendMessage(ChatColor.RED+"[portalToYou] 玩家不存在或不在线");
				}
			}else if(args[0].equalsIgnoreCase(acceptPy)) {
				//目标玩家接受py
				if(plugin.getConfig().contains(player.getName())){
					//从配置文件中载入location
					Location location=getLocFromConfig(player);
					player.teleport(location);
					//传送想要py的玩家到对应location
					String player2=plugin.getConfig().getString(player.getName()+".Py");
					//遍历玩家，获取想要传送的玩家
					Collection<? extends Player> onlinePlayer = plugin.getServer().getOnlinePlayers();
					for(Player p:onlinePlayer) {
						if(p.getName().equals(player2)) {
							p.teleport(location);
							p.sendMessage(ChatColor.GREEN+"[portalToYou] py成功啦，和基友愉快的玩耍吧。");
							plugin.getConfig().set(player.getName(), null);
							plugin.saveConfig();
						}
					}
					
				}else {
					player.sendMessage(ChatColor.YELLOW+"[portalToYou] 没人要和你py啊~");
				}
				
				
			}else {
				player.sendMessage(ChatColor.RED+"[portalToYou] 请输入正确的玩家名。\n"
						+ ChatColor.GREEN+"例如："+ChatColor.GOLD+"/py chaoji 请求传送到chaoji身边（接受chaoji的传送请求）");
			}
		}else {
			sender.sendMessage(ChatColor.RED+"[portalToYou] 非玩家不能py。");
		}
		return true;
	}

	/**
	 * 将玩家位置保存到配置文件
	 * @param player
	 */
	public void setPlayerLocation(Player player) {
		Location location = player.getLocation();
		plugin.getConfig().set(player.getName()+".World", location.getWorld().getName());
		plugin.getConfig().set(player.getName()+".Yaw", location.getYaw());
		plugin.getConfig().set(player.getName()+".Pitch", location.getPitch());
		plugin.getConfig().set(player.getName()+".X", location.getX());
		plugin.getConfig().set(player.getName()+".Y", location.getY());
		plugin.getConfig().set(player.getName()+".Z", location.getZ());
		plugin.saveConfig();
	}
	/**
	 * 将发起传送请求的玩家名保存到目标玩家对应的配置文件中
	 * @param source 发起传送请求的玩家
	 * @param destination 目标玩家
	 */
	public void setSourcePlayer(Player source,Player destination) {
		plugin.getConfig().set(destination.getName()+".Py", source.getName());
		plugin.saveConfig();
	}
	public Location getLocFromConfig(Player player) {
		double x=plugin.getConfig().getDouble(player.getName()+".X");
		double y=plugin.getConfig().getDouble(player.getName()+".Y");
		double z=plugin.getConfig().getDouble(player.getName()+".Z");
		float yaw=(float)plugin.getConfig().getDouble(player.getName()+".Yaw");
		float pitch=(float)plugin.getConfig().getDouble(player.getName()+".Pitch");
		Location location=new Location(Bukkit.getWorld(plugin.getConfig().getString(player.getName()+".World")), x, y, z, yaw, pitch);
		return location;
	}
}

