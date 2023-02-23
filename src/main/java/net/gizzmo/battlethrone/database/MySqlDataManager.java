package net.gizzmo.battlethrone.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.gizzmo.battlethrone.BattleThrone;

public class MySqlDataManager extends DataManager {
    public MySqlDataManager(final BattleThrone main) {
        super(main);
    }

    @Override
    public void open(String mySqlHost, int mySqlPort, String mySqlDatabase, String mySqlUser, String mySqlPassword) {
        HikariConfig config = new HikariConfig();
        String url = "jdbc:mysql://" + mySqlHost + ":" + mySqlPort + "/" + mySqlDatabase + "?characterEncoding=utf8&autoReconnect=true&useSSL=false";
        config.setJdbcUrl(url);
        config.setUsername(mySqlUser);
        config.setPassword(mySqlPassword);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(10);
        this.hikariDataSource = new HikariDataSource(config);
    }

    @Override
    public void open() {
    }
}

