package net.gizzmo.battlethrone.config;

import net.gizzmo.battlethrone.api.tools.StringTools;
import net.gizzmo.battlethrone.item.AxeItem;
import net.gizzmo.battlethrone.core.MConfig;
import net.gizzmo.battlethrone.database.DataManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Settings {

    private static FileConfiguration config;
    public static String databaseMySQLHost;
    public static String databaseMySQLDatabase;
    public static String databaseMySQLUser;
    public static String databaseMySQLPassword;
    public static int databaseMySQLPort;
    public static String databaseTableNameThrone;
    public static boolean dataSavePeriodically;
    public static long dataSaveInterval;
    public static DataManager.DatabaseType databaseType;
    public static boolean visualizerEnable;
    public static int visualizerIntervalSend;
    public static int visualizerViewDistance;
    public static float visualizerParticleSpeed;
    public static int visualizerParticleAmount;
    public static Material blockSwapFrom;
    public static Material blockSwapTo;
    public static AxeItem axeItem;
    public static boolean hologramEnable;
    public static double hologramRefreshingDelay;
    public static String hologramMessageContest;
    public static List<String> hologramLines;
    public static boolean rewardMoneyEnable;
    public static boolean rewardCommandEnable;
    public static List<String> rewardMessage;

    public Settings() {
    }

    public static void setConfig(MConfig mConfig) {
        config = mConfig.getConfig();
        load();
    }

    private static void load() {
        databaseMySQLHost = config.getString("database.mySQLHost", "localhost");
        databaseMySQLDatabase = config.getString("database.mySQLDatabase", "db");
        databaseMySQLUser = config.getString("database.mySQLUser", "root");
        databaseMySQLPassword = config.getString("database.mySQLPassword", "");
        databaseMySQLPort = config.getInt("database.mySQLPort", 3306);
        databaseTableNameThrone = config.getString("database.tableNames.throne", "t_faction_throne");
        dataSavePeriodically = config.getBoolean("dataSavePeriodically.dataSavePeriodically", true);
        dataSaveInterval = config.getLong("dataSavePeriodically.dataSaveInterval", 15L);

        try {
            databaseType = DataManager.DatabaseType.valueOf((config.getString("database.type", "sqlite").toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException var4) {
            databaseType = DataManager.DatabaseType.SQLITE;
        }

        visualizerEnable = config.getBoolean("protocol.visualizer.enable", false);
        visualizerIntervalSend = config.getInt("protocol.visualizer.intervalSend", 2);
        visualizerViewDistance = config.getInt("protocol.visualizer.viewDistance", 10);
        visualizerParticleSpeed = (float) config.getDouble("protocol.visualizer.particle.speed", 1.0);
        visualizerParticleAmount = config.getInt("protocol.visualizer.particle.amount", 10);

        axeItem = new AxeItem(config.getString("item.axe.name", "[Axe Throne] Hache de la création."), config.getStringList("item.axe.lore"));

        blockSwapFrom = Material.getMaterial(config.getString("blockSwap.from", "GOLD_BLOCK").toUpperCase());
        blockSwapTo = Material.getMaterial(config.getString("blockSwap.to", "COAL_BLOCK").toUpperCase());

        hologramEnable = config.getBoolean("hologram.enable", false);
        hologramRefreshingDelay = (config.getDouble("hologram.refreshingDelay") * 20);
        hologramMessageContest = config.getString("hologram.messageContest", "Contester");
        hologramLines = StringTools.fixColorsList(config.getStringList("hologram.text"));

        rewardMoneyEnable = config.getBoolean("reward.money.enable", true);
        rewardCommandEnable = config.getBoolean("reward.command.enable", true);
        rewardMessage = StringTools.fixColorsList(config.getStringList("reward.message"));
    }
}
