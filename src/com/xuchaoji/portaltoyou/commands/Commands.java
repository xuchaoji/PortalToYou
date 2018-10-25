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
					player.sendMessage("[PortalToYou] ������Ѱ��ң�������"+player2.getName());
					if(args[0].equals(player2.getName())) {
						flag = false;
						//��ȡĿ����ҵ�location
						setPlayerLocation(player2);
						setSourcePlayer(player, player2);
						//֪ͨ��Ҫ����py�����
						player.sendMessage(ChatColor.GREEN+"[portalToYou] �Ѿ���"+ChatColor.GREEN+player2+ChatColor.GREEN+"����py���󣬵ȴ��Է�����py");
						//֪ͨĿ�����
						player2.sendMessage(ChatColor.GREEN+"[portalToYou]"+ChatColor.YELLOW+player.getName()+ChatColor.GREEN+"��������py������"+
						ChatColor.GOLD+"py accept"+ChatColor.GREEN+"���ܣ�"+
						ChatColor.GOLD+"py deny "+ChatColor.GREEN+"�ܾ���");
					}
				}
				if(flag) {
					player.sendMessage(ChatColor.RED+"[portalToYou] ��Ҳ����ڻ�����");
				}
			}else if(args[0].equalsIgnoreCase(acceptPy)) {
				//Ŀ����ҽ���py
				if(plugin.getConfig().contains(player.getName())){
					//�������ļ�������location
					Location location=getLocFromConfig(player);
					player.teleport(location);
					//������Ҫpy����ҵ���Ӧlocation
					String player2=plugin.getConfig().getString(player.getName()+".Py");
					//������ң���ȡ��Ҫ���͵����
					Collection<? extends Player> onlinePlayer = plugin.getServer().getOnlinePlayers();
					for(Player p:onlinePlayer) {
						if(p.getName().equals(player2)) {
							p.teleport(location);
							p.sendMessage(ChatColor.GREEN+"[portalToYou] py�ɹ������ͻ���������ˣ�ɡ�");
							plugin.getConfig().set(player.getName(), null);
							plugin.saveConfig();
						}
					}
					
				}else {
					player.sendMessage(ChatColor.YELLOW+"[portalToYou] û��Ҫ����py��~");
				}
				
				
			}else {
				player.sendMessage(ChatColor.RED+"[portalToYou] ��������ȷ���������\n"
						+ ChatColor.GREEN+"���磺"+ChatColor.GOLD+"/py chaoji �����͵�chaoji��ߣ�����chaoji�Ĵ�������");
			}
		}else {
			sender.sendMessage(ChatColor.RED+"[portalToYou] ����Ҳ���py��");
		}
		return true;
	}

	/**
	 * �����λ�ñ��浽�����ļ�
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
	 * ���������������������浽Ŀ����Ҷ�Ӧ�������ļ���
	 * @param source ��������������
	 * @param destination Ŀ�����
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

