package me.michaelkrauty.UberCommands;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class SettingsManager extends JavaPlugin{
	
	private SettingsManager() { }
	
	static SettingsManager instance = new SettingsManager();
	
	public static SettingsManager getInstance(){
		return instance;
	}
	
	Plugin p;
	FileConfiguration lang;
	File langfile;
	
	public void setup(Plugin p){
		lang = p.getConfig();
		lang.options().copyDefaults(true);
		langfile = new File(p.getDataFolder(), "lang.yml");
	}
	
	public FileConfiguration getLang(){
		return lang;
	}
	
	public void saveLang(){
		try {
			lang.save(langfile);
		}catch (IOException e){
			getServer().getLogger().severe("Could not save lang.yml!");
		}
	}
	
	public PluginDescriptionFile getDesc(){
		return p.getDescription();
	}
	
	public void reloadLang(){
		lang = YamlConfiguration.loadConfiguration(langfile);
	}
}
