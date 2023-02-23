package net.gizzmo.battlethrone.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.gizzmo.battlethrone.BattleThrone;
import net.gizzmo.battlethrone.api.tools.FileTools;

import java.io.File;

public class SqliteDataManager extends DataManager {
    public SqliteDataManager(final BattleThrone main) {
        super(main);
    }

    @Override
    public void open(String mySqlHost, int mySqlPort, String mySqlDatabase, String mySqlUser, String mySqlPassword) {
    }

    @Override
    public void open() {
        File dbFile = new File(this.main.getDataFolder().getAbsolutePath() + "/database/" + this.fileBackendName + ".db");
        if (!dbFile.exists()) {
            FileTools.mkdir(dbFile);
        }

        HikariConfig config = new HikariConfig();
        String url = "jdbc:sqlite:" + dbFile.getPath();
        config.setJdbcUrl(url);
        config.setUsername("");
        config.setPassword("");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(10);
        this.hikariDataSource = new HikariDataSource(config);
    }
}
