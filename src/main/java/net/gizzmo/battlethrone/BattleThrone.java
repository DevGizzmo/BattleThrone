package net.gizzmo.battlethrone;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import net.gizzmo.battlethrone.hologram.MHologramPlaceholder;
import net.gizzmo.battlethrone.throne.Throne;
import net.gizzmo.battlethrone.throne.ThroneManager;
import net.gizzmo.battlethrone.command.CmdThroneAdmin;
import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.config.Settings;
import net.gizzmo.battlethrone.core.MConfig;
import net.gizzmo.battlethrone.database.DataManager;
import net.gizzmo.battlethrone.database.MySqlDataManager;
import net.gizzmo.battlethrone.database.SqliteDataManager;
import net.gizzmo.battlethrone.dependency.PlaceholderApiHandler;
import net.gizzmo.battlethrone.listener.PlayerListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleThrone extends JavaPlugin {
    private static BattleThrone instance;
    private MConfig configMain;
    private MConfig configLang;
    private Economy economy;
    private DataManager dataManager;
    private ThroneManager throneManager;

    public BattleThrone() {
    }

    public static BattleThrone getInstance() {
        return instance;
    }

    public DataManager getDataManager() {
        return this.dataManager;
    }

    public ThroneManager getThroneManager() {
        return this.throneManager;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public void log(String message) {
        instance.getLogger().info("§9" + message);
    }

    @Override
    public void onEnable() {
        instance = this;

        this.log("#================[THRONE By Maliimaloo]=============#");
        this.log("# §bBattleThrone §aest en train de charger. Veuillez §9#");
        this.log("# §alire attentivement toutes les sorties qui en     §9#");
        this.log("# §adécoulent.                                       §9#");
        this.log("#===================================================#");

        if (!this.setupEconomy()) {
            this.getLogger().severe("Plugin désactivé car aucune dépendance Vault/économie n'a été trouvée !");
            this.getServer().getPluginManager().disablePlugin(this);
        } else {
            this.log("Chargement main configuration...");
            this.saveDefaultConfig();

            this.configLang = new MConfig(this, "lang.yml");
            this.configMain = new MConfig(this, "config.yml");

            this.throneManager = new ThroneManager(this);

            this.log("Chargement du fichier de §bconfig§9...");
            Settings.setConfig(configMain);
            this.log("§aChargement réussi");

            this.log("Chargement du fichier de §blang§9...");
            Lang.setConfig(configLang);
            this.log("§aChargement réussi");

            if (this.setupClipPlaceholders()) {
                this.log("§aClip's PlaceholderAPI support activée.");
            }

            if (this.setupHolographicDisplays()) {
                this.log("§aClip's HolographicDisplayAPI support activée.");
            } else {
                this.log("§cHolographicDisplayAPI n'a pas été trouvée, Certaines fonctionnalités ne seront pas activée.");
            }

            if (this.hasProtocolLib()) {
                this.log("§aProtocolLib support activée.");
            } else {
                this.log("§cProtocolLib n'a pas été trouvée, Certaines fonctionnalités ne seront pas activée.");
            }

            switch (Settings.databaseType) {
                case MYSQL:
                    this.dataManager = new MySqlDataManager(this);
                    this.dataManager.open(Settings.databaseMySQLHost, Settings.databaseMySQLPort, Settings.databaseMySQLDatabase, Settings.databaseMySQLUser, Settings.databaseMySQLPassword);
                    break;
                case SQLITE:
                    this.dataManager = new SqliteDataManager(this);
                    this.dataManager.open();
            }

            this.dataManager.setup();
            this.registerCommand();
            this.registerListeners();
        }
    }

    @Override
    public void onDisable() {
        if (this.dataManager != null) {
            this.dataManager.hikariClose();
        }

        for (Throne throne : this.getThroneManager().getAllThrones()) {
            if (throne.getMHologram() != null) {
                throne.getMHologram().delete();
            }
        }
    }

    public void reload() {
        this.configMain = new MConfig(this, "config.yml");
        this.configLang = new MConfig(this, "lang.yml");
        Settings.setConfig(configMain);
        Lang.setConfig(configLang);

        for (Throne throne : this.getThroneManager().getAllThrones()) {
            if (throne.getMHologram() != null) {
                throne.getMHologram().delete();
                if (Settings.hologramEnable) {
                    throne.getMHologram().init();
                }
            }
        }
    }

    public void load() {
        this.throneManager.loadAllThrones();
    }

    private void registerCommand() {
        this.getCommand("throneadmin").setExecutor(new CmdThroneAdmin(this));
    }

    private void registerListeners() {
        PluginManager pm = this.getServer().getPluginManager();

        pm.registerEvents(new PlayerListener(this), this);
    }

    private boolean setupClipPlaceholders() {
        return this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null && (new PlaceholderApiHandler(this)).register();
    }
    private boolean setupHolographicDisplays() {
        if (this.getServer().getPluginManager().isPluginEnabled("HolographicDisplays")) {
            HolographicDisplaysAPI. get(this).registerGlobalPlaceholder("battlethrone", (int) Settings.hologramRefreshingDelay, new MHologramPlaceholder(this));
            return true;
        }
        return false;
    }

    private boolean setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        } else {
            this.economy = rsp.getProvider();
            return this.economy != null;
        }
    }

    public boolean hasHolographicDisplays() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("HolographicDisplays");
    }

    public boolean hasProtocolLib() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("ProtocolLib");
    }
}
