package net.gizzmo.battlethrone.database.table;

import net.gizzmo.battlethrone.config.Settings;

public class DataTable {
    public static final String tableName = Settings.databaseTableNameThrone;
    public static final String columnId = "id_throne";
    public static final String columnName = "name_throne";
    public static final String columnRegion = "region_throne";
    public static final String columnSettings = "settings_throne";
    public static final String columnHologram = "hologram_throne";
    public static final String requestCreate = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + columnId + " INT NOT NULL AUTO_INCREMENT, " + columnName + " VARCHAR(36) NOT NULL, " + columnRegion + " TEXT NOT NULL, " + columnSettings + " TEXT NOT NULL, " + columnHologram + " TEXT NOT NULL, PRIMARY KEY (" + columnId + "));";
}
