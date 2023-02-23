package net.gizzmo.battlethrone.throne.settings;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettingsThrone {
    private int moneyEarning;
    private int cooldownPeriod;
    private final Map<Integer, String> commands;
    private @Nullable Player owner;
    private int captureTime;
    private int counterTask;

    private final List<Player> playerInThrone;

    public SettingsThrone(int moneyEarning, int cooldownPeriod, Map<Integer, String> commands) {
        this.moneyEarning = moneyEarning;
        this.cooldownPeriod = cooldownPeriod;
        this.commands = commands;
        this.owner = null;
        this.playerInThrone = new ArrayList<>();
        this.captureTime = 0;
        this.counterTask = 0;
    }

    public int getMoneyEarning() {
        return this.moneyEarning;
    }
    public int getCooldownPeriod() {
        return this.cooldownPeriod;
    }
    public Map<Integer, String> getCommands() {
        return this.commands;
    }
    public OfflinePlayer getOwner() {
        return this.owner;
    }
    public List<Player> getPlayerInThrone() {
        return this.playerInThrone;
    }
    public int getCaptureTime() {
        return this.captureTime;
    }
    public int getCounterTask() {
        return this.counterTask;
    }
    public void setMoneyEarning(int moneyEarning) {
        this.moneyEarning = moneyEarning;
    }
    public void setCooldownPeriod(int cooldownPeriod) {
        this.cooldownPeriod = cooldownPeriod;
    }
    public void addCommand(int id, String command) {
        this.commands.put(id, command);
    }
    public void removeCommand(int id) {
        this.commands.remove(id);
    }
    public void setOwner(@Nullable Player owner) {
        this.owner = owner;
    }
    public void setCaptureTime(int captureTime) {
        this.captureTime = captureTime;
    }
    public void setCounterTask(int counterTask) {
        this.counterTask = counterTask;
    }
}
