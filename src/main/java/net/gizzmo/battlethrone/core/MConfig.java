package net.gizzmo.battlethrone.core;

import net.gizzmo.battlethrone.BattleThrone;
import net.gizzmo.battlethrone.api.tools.FileTools;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MConfig {
    private final BattleThrone main;
    private final String name;
    private FileConfiguration fileConfiguration;
    private File file;
    
    public MConfig(BattleThrone main, String name) {
    	this.main = main;
    	this.name = name;
    	this.load();
    }
    
    private void load() {
        File file = new File(this.main.getDataFolder(), this.name);
        if (!file.exists()) {
            FileTools.mkdir(file);
            FileTools.copy(this.main.getResource(this.name), file);
        }

        this.file = file;
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
        this.fileConfiguration.options().copyDefaults(true);
    }
    public void save() {
        try {
            this.fileConfiguration.options().copyDefaults(true);
            this.fileConfiguration.save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    
    public FileConfiguration getConfig() {
        return this.fileConfiguration;
    }
}
