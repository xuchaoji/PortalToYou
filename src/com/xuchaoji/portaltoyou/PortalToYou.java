package com.xuchaoji.portaltoyou;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import com.xuchaoji.portaltoyou.commands.Commands;

public class PortalToYou extends JavaPlugin {
	private FileConfiguration cfg= getConfig();
	@Override
	public void onEnable() {
		this.getCommand("py").setExecutor(new Commands());
		System.out.println("[PortalToYou] 加载成功~");
		cfg.addDefault("portalStatus.chaoji", false);
		cfg.options().configuration();
		saveConfig();
	}
	
	@Override 
	public void onDisable() {
		System.out.println("[PortalToYou] 已停止~");
	}
}
