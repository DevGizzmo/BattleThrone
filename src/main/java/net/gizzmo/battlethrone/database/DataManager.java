package net.gizzmo.battlethrone.database;

import com.google.gson.reflect.TypeToken;
import com.zaxxer.hikari.HikariDataSource;
import net.gizzmo.battlethrone.BattleThrone;
import net.gizzmo.battlethrone.api.tools.area.AreaGsonAdapter;
import net.gizzmo.battlethrone.api.tools.area.AreaTools;
import net.gizzmo.battlethrone.database.table.DataTable;
import net.gizzmo.battlethrone.hologram.MHologram;
import net.gizzmo.battlethrone.hologram.MHologramGsonAdapter;
import net.gizzmo.battlethrone.throne.Throne;
import net.gizzmo.battlethrone.throne.settings.SettingsGsonAdapter;
import net.gizzmo.battlethrone.throne.settings.SettingsThrone;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DataManager {
    protected BattleThrone main;
    protected HikariDataSource hikariDataSource = null;
    protected String fileBackendName = "database";

    public DataManager(final BattleThrone main) {
        this.main = main;
    }

    public abstract void open(String mySqlHost, int mySqlPort, String mySqlDatabase, String mySqlUser, String mySqlPassword);
    public abstract void open();
    public void setup() {
        this.transmission(DataTable.requestCreate);
        this.main.load();
    }
    public void hikariClose() {
        if (this.hikariDataSource == null || this.hikariDataSource.isClosed()) {
            return;
        }

        this.hikariDataSource.close();
    }

    protected Throne getThroneFromResult(final MResultSet resultSet) {
        int id = resultSet.getInt(DataTable.columnId);
        String name = resultSet.getString(DataTable.columnName);
        AreaTools mainRegion = AreaGsonAdapter.getGson().fromJson(resultSet.getString(DataTable.columnRegion), (new TypeToken<AreaTools>() {}.getType()));
        SettingsThrone settings = SettingsGsonAdapter.getGson().fromJson(resultSet.getString(DataTable.columnSettings), (new TypeToken<SettingsThrone>() {}.getType()));

        MHologram hologram = MHologramGsonAdapter.getGson().fromJson(resultSet.getString(DataTable.columnHologram), (new TypeToken<MHologram>() {}.getType()));
        return new Throne(id, name, mainRegion, settings, hologram);
    }

    public void loadAllThrones(Callback<List<Throne>> callback) {
        final long startTime = System.currentTimeMillis();
        final String selectRequest = "SELECT * FROM " + DataTable.tableName + ";";

        this.asyncRequest(selectRequest, new Callback<MResultSet>() {
            final List<Throne> loadedThrones = new ArrayList<>();

            @Override
            public void onSuccess(MResultSet resultSet) {
                if (resultSet != null) {
                    while (resultSet.next()) {
                        Throne throne = getThroneFromResult(resultSet);
                        this.loadedThrones.add(throne);
                    }
                }

                callback.onSuccess(loadedThrones);
                DataManager.this.main.getThroneManager().setThrones(loadedThrones);
                DataManager.this.main.log("§aRécupération réussie de §b" + loadedThrones.size() + " thrones§a.");
                DataManager.this.main.log("§aExécution de la requête §b[load_thrones] §aen §b" + (System.currentTimeMillis() - startTime) + "ms§a.");
            }
        });
    }
    public void registerThrone(final Throne throne, Callback<Integer> callback) {
        final long startTime = System.currentTimeMillis();
        final String insertRequest = "INSERT INTO " + DataTable.tableName + "(" + DataTable.columnName + ", " + DataTable.columnRegion + ", " + DataTable.columnSettings +  ", " + DataTable.columnHologram + ") VALUES (?, ?, ?, ?);";
        final Object[] objects = new Object[] { throne.getName(), AreaGsonAdapter.getGson().toJson(throne.getMainRegion()), SettingsGsonAdapter.getGson().toJson(throne.getSettings()), (throne.getMHologram() == null ? "" : MHologramGsonAdapter.getGson().toJson(throne.getMHologram())) };

        Bukkit.getScheduler().runTaskAsynchronously(this.main, () -> {
            try {
                Connection connection = this.hikariDataSource.getConnection();
                Throwable throwable = null;

                try {
                    PreparedStatement req_sql = connection.prepareStatement(insertRequest, Statement.RETURN_GENERATED_KEYS);

                    int index = 1;
                    for (Object obj : objects) {
                        req_sql.setObject(index, obj.toString());
                        ++index;
                    }

                    req_sql.executeUpdate();
                    ResultSet resultSet = req_sql.getGeneratedKeys();

                    if (resultSet.next()) {
                        callback.onSuccess(resultSet.getInt(1));
                    } else {
                        callback.onSuccess(-1);
                    }

                    this.main.log("§aEnregistrement réussie du throne §b" + throne.getName() + "§a.");
                    this.main.log("§aExécution de la requête §b[register_throne] §aen §b" + (System.currentTimeMillis() - startTime) + "ms§a.");

                    req_sql.close();
                    resultSet.close();
                } catch (Throwable var2) {
                    throwable = var2;
                    throw var2;
                } finally {
                    if (connection != null) {
                        if (throwable != null) {
                            try {
                                connection.close();
                            } catch (Throwable var3) {
                                throwable.addSuppressed(var3);
                            }
                        } else {
                            connection.close();
                        }
                    }

                }
            } catch (SQLException var4) {
                var4.printStackTrace();
                if (callback != null) {
                    Bukkit.getScheduler().runTask(this.main, () -> callback.onFailure(var4));
                }
            }

        });
    }
    public void removeThrone(final Throne throne, final Callback<Integer> callback) {
        final long startTime = System.currentTimeMillis();
        final String deleteRequest = "DELETE FROM " + DataTable.tableName + " WHERE " + DataTable.columnId + " = ?;";

        this.asyncTransmission(deleteRequest, new Callback<Integer>() {
            @Override
            public void onSuccess(Integer var1) {
                callback.onSuccess(var1);
                DataManager.this.main.log("§aSuppression réussie pour le throne §bid: " + throne.getId() + ", name: " + throne.getName() + "§a.");
                DataManager.this.main.log("§aExécution de la requête §b[remove_throne] §aen §b" + (System.currentTimeMillis() - startTime) + "ms§a.");
            }

            @Override
            public void onFailure(Throwable cause) {
                Callback.super.onFailure(cause);
            }
        }, throne.getId());
    }
    public void updateThrone(final Throne throne) {
        final long startTime = System.currentTimeMillis();
        final String updateRequest = "UPDATE " + DataTable.tableName + " SET " + DataTable.columnName + " = ?, " + DataTable.columnRegion + " = ?, " + DataTable.columnSettings + " = ?, " + DataTable.columnHologram + " = ? WHERE " + DataTable.columnId + "= ?;";

        this.asyncTransmission(updateRequest, new Callback<Integer>() {
            @Override
            public void onSuccess(Integer var1) {
                DataManager.this.main.log("§aUpdate réussie pour le throne §bid: " + throne.getId() + ", name: " + throne.getName() + "§a.");
                DataManager.this.main.log("§aExécution de la requête §b[update_throne] §aen §b" + (System.currentTimeMillis() - startTime) + "ms§a.");
            }

            @Override
            public void onFailure(Throwable cause) {
                Callback.super.onFailure(cause);
            }
        }, throne.getName(), AreaGsonAdapter.getGson().toJson(throne.getMainRegion()), SettingsGsonAdapter.getGson().toJson(throne.getSettings()), (throne.getMHologram() == null ? "" : MHologramGsonAdapter.getGson().toJson(throne.getMHologram())), throne.getId());
    }

    public MResultSet request(final String query, final Object... objects) {
        PreparedStatement req_sql = null;
        ResultSet req = null;

        try {
            Connection connection = this.hikariDataSource.getConnection();
            Throwable var1 = null;

            try {
                req_sql = connection.prepareStatement(query);

                int index = 1;
                for (Object obj : objects) {
                    req_sql.setObject(index, obj.toString());
                    index++;
                }

                req = req_sql.executeQuery();
                MResultSet result_req = new MResultSet(req);

                req.close();
                req_sql.close();

                return result_req;
            } catch (Throwable var2) {
                var1 = var2;
                throw var2;
            } finally {
                if (connection != null) {
                    if (var1 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var3) {
                            var1.addSuppressed(var3);
                        }
                    } else {
                        connection.close();
                    }
                }

            }
        } catch (SQLException var4) {
            var4.printStackTrace();
        } finally {
            try {
                if (req != null) {
                    req.close();
                }

                if (req_sql != null) {
                    req_sql.close();
                }
            } catch (SQLException var5) {
                var5.printStackTrace();
            }

        }

        return null;
    }
    public boolean transmission(final String query, final Object... objects) {
        try {
            Connection connection = this.hikariDataSource.getConnection();
            Throwable var1 = null;

            try {
                PreparedStatement req_sql = connection.prepareStatement(query);

                int index = 1;
                for (Object obj : objects) {
                    req_sql.setObject(index, obj.toString());
                    ++index;
                }

                req_sql.executeUpdate();
                req_sql.close();
                return true;
            } catch (Throwable var2) {
                var1 = var2;
                throw var1;
            } finally {
                if (connection != null) {
                    if (var1 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var3) {
                            var1.addSuppressed(var3);
                        }
                    } else {
                        connection.close();
                    }
                }
            }
        } catch (Throwable var4) {
            var4.printStackTrace();
            return false;
        }
    }
    public void asyncRequest(final String query, final Object... objects) {
        this.asyncRequest(query, null, objects);
    }
    public void asyncRequest(final String query, final  Callback<MResultSet> callback, final Object... objects) {
        Bukkit.getScheduler().runTaskAsynchronously(this.main, () -> {
            PreparedStatement req_sql = null;
            ResultSet req = null;

            try {
                Connection connection = this.hikariDataSource.getConnection();
                Throwable var1 = null;

                try {
                    req_sql = connection.prepareStatement(query);

                    int index = 1;
                    for (Object obj : objects) {
                        req_sql.setObject(index, obj.toString());
                        ++index;
                    }

                    req = req_sql.executeQuery();
                    MResultSet result_req = new MResultSet(req);

                    req.close();
                    req_sql.close();

                    if (callback != null) {
                        Bukkit.getScheduler().runTask(this.main, () -> callback.onSuccess(result_req));
                    }
                } catch (Throwable var2) {
                    var1 = var2;
                    throw var2;
                } finally {
                    if (connection != null) {
                        if (var1 != null) {
                            try {
                                connection.close();
                            } catch (Throwable var3) {
                                var1.addSuppressed(var3);
                            }
                        } else {
                            connection.close();
                        }
                    }

                }
            } catch (SQLException var4) {
                var4.printStackTrace();
                if (callback != null) {
                    Bukkit.getScheduler().runTask(this.main, () -> callback.onFailure(var4));
                }
            } finally {
                try {
                    if (req != null) {
                        req.close();
                    }

                    if (req_sql != null) {
                        req_sql.close();
                    }
                } catch (SQLException var5) {
                    var5.printStackTrace();
                }
            }
        });
    }
    public void asyncTransmission(final String query, final Object... objects) {
        this.asyncTransmission(query, null, objects);
    }
    public void asyncTransmission(final String query, final Callback<Integer> callback, final Object... objects) {
        Bukkit.getScheduler().runTaskAsynchronously(this.main, () -> {
            try {
                Connection connection = this.hikariDataSource.getConnection();
                Throwable var1 = null;

                try {
                    PreparedStatement req_sql = connection.prepareStatement(query);

                    int index = 1;
                    for (Object obj : objects) {
                        req_sql.setObject(index, obj.toString());
                        ++index;
                    }

                    int result = req_sql.executeUpdate();
                    req_sql.close();
                    if (callback != null) {
                        Bukkit.getScheduler().runTask(this.main, () -> callback.onSuccess(result));
                    }
                } catch (Throwable var2) {
                    var1 = var2;
                    throw var2;
                } finally {
                    if (connection != null) {
                        if (var1 != null) {
                            try {
                                connection.close();
                            } catch (Throwable var3) {
                                var1.addSuppressed(var3);
                            }
                        } else {
                            connection.close();
                        }
                    }

                }
            } catch (SQLException var4) {
                var4.printStackTrace();
                if (callback != null) {
                    Bukkit.getScheduler().runTask(this.main, () -> callback.onFailure(var4));
                }
            }

        });
    }

    public enum DatabaseType {
        MYSQL,
        SQLITE;

        DatabaseType() {
        }
    }
}

