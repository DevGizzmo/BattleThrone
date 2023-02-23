package net.gizzmo.battlethrone.throne;

import net.gizzmo.battlethrone.BattleThrone;
import net.gizzmo.battlethrone.api.tools.area.AreaTools;
import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.config.Settings;
import net.gizzmo.battlethrone.database.Callback;
import net.gizzmo.battlethrone.event.ThroneCreateEvent;
import net.gizzmo.battlethrone.hologram.MHologram;
import net.gizzmo.battlethrone.throne.settings.SettingsThrone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class ThroneManager {
    private final BattleThrone main;
    private List<Throne> thrones;
    private final Map<Player, Map<String, Location>> playersLocations;

    public ThroneManager(final BattleThrone main) {
        this.main = main;
        this.thrones = new ArrayList<>();
        this.playersLocations = new HashMap<>();
    }

    public void setThrones(final List<Throne> thrones) {
        this.thrones = thrones;
    }
    public List<Throne> getAllThrones() {
        return this.thrones;
    }
    public Map<Player, Map<String, Location>> getPlayersLocations() {
        return this.playersLocations;
    }

    public Throne getThrone(final int id) {
        for (Throne throne : this.thrones) {
            if (throne.getId() == id) {
                return throne;
            }
        }
        return null;
    }
    public Throne getThrone(final String name) {
        for (Throne throne : this.thrones) {
            if (throne.getName().equalsIgnoreCase(name)) {
                return throne;
            }
        }
        return null;
    }
    public boolean isThrone(final String name) {
        return this.getThrone(name) != null;
    }
    public Throne getPlayersThrone(final Player player) {
        for (Throne throne : this.thrones) {
            if (throne.getSettings().getOwner() != null && throne.getSettings().getOwner().equals(player) || throne.getSettings().getPlayerInThrone().contains(player)) {
                return throne;
            }
        }
        return null;
    }
    public boolean hasOwner(final Throne throne) {
        return throne.getSettings().getOwner() != null;
    }
    public void swapingBlock(final Throne throne, Material matFrom, Material matTo) {
        AreaTools region = throne.getMainRegion();
        int minX = region.getMin().getBlockX();
        int minY = region.getMin().getBlockY();
        int minZ = region.getMin().getBlockZ();

        int maxX = region.getMax().getBlockX();
        int maxY = region.getMax().getBlockY();
        int maxZ = region.getMax().getBlockZ();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = region.getMin().getWorld().getBlockAt(x, y, z);
                    if (block.getType() == matFrom) {
                        block.setType(matTo);
                    }
                }
            }
        }
    }
    public void releaseThrone(final Throne throne) {
        throne.getSettings().setOwner(null);
        throne.getSettings().setCaptureTime(0);
    }

    public void loadAllThrones() {
        this.main.getDataManager().loadAllThrones(listThrone -> {
            if (!listThrone.isEmpty()) {
                for (Throne throne : listThrone) {
                    if (throne.getMHologram() != null && Settings.hologramEnable) {
                        throne.getMHologram().init();
                    }
                }
            }
        });
    }
    public void createThrone(final Player player, final String name, final AreaTools areaTools, final SettingsThrone settings, @Nullable MHologram hologram) {
        final Throne throne = new Throne(0, name, areaTools, settings, hologram);
        ThroneCreateEvent event = new ThroneCreateEvent(throne, player);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            this.main.getDataManager().registerThrone(throne, var1 -> {
                throne.setId(var1);
                this.getAllThrones().add(throne);
                player.sendMessage(Lang.MSG_THRONEADMIN_CREATE_Created.toMsg().replace("%id%", String.valueOf(throne.getId())).replace("%name%", throne.getName()));
            });
        }
    }
    public void removeThrone(final Throne throne, final Callback<Integer> callback) {
        this.main.getDataManager().removeThrone(throne, var1 -> {
            if (throne.getMHologram() != null) {
                throne.getMHologram().getHologram().delete();
            }

            this.thrones.remove(throne);
            callback.onSuccess(var1);
        });
    }
}
