package net.gizzmo.battlethrone.throne;

import net.gizzmo.battlethrone.task.ThroneTask;
import net.gizzmo.battlethrone.BattleThrone;
import net.gizzmo.battlethrone.api.tools.area.AreaTools;
import net.gizzmo.battlethrone.hologram.MHologram;
import net.gizzmo.battlethrone.throne.settings.SettingsThrone;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;

public class Throne {
    private int id;
    private String name;
    private AreaTools mainRegion;
    private SettingsThrone settings;
    private @Nullable MHologram hologram;
    private BukkitTask task;

    public Throne(int id, String name, AreaTools mainRegion, SettingsThrone settings, @Nullable MHologram hologram) {
        this.id = id;
        this.name = name;
        this.mainRegion = mainRegion;
        this.settings = settings;
        this.hologram = hologram;
        this.task = Bukkit.getScheduler().runTaskTimer(BattleThrone.getInstance(), new ThroneTask(this), 0, 20L);
    }

    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public AreaTools getMainRegion() {
        return this.mainRegion;
    }
    public SettingsThrone getSettings() {
        return this.settings;
    }
    public MHologram getMHologram() {
        return this.hologram;
    }
    public BukkitTask getTask() {
        return this.task;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setMainRegion(AreaTools mainRegion) {
        this.mainRegion = mainRegion;
    }
    public void setSettings(SettingsThrone settings) {
        this.settings = settings;
    }
    public void setMHologram(MHologram hologram) {
        this.hologram = hologram;
    }
    public void setTask(BukkitTask task) {
        this.task = task;
    }
    public void save() {
        BattleThrone.getInstance().getDataManager().updateThrone(this);
    }
}
