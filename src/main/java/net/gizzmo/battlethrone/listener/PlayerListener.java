package net.gizzmo.battlethrone.listener;

import com.comphenix.protocol.wrappers.EnumWrappers;
import net.gizzmo.battlethrone.BattleThrone;
import net.gizzmo.battlethrone.api.tools.protocollib.particle.ParticleTools;
import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.config.Settings;
import net.gizzmo.battlethrone.database.table.DataTable;
import net.gizzmo.battlethrone.event.PlayerCapturedThroneEvent;
import net.gizzmo.battlethrone.event.PlayerEnterThroneEvent;
import net.gizzmo.battlethrone.event.PlayerLeaveThroneEvent;
import net.gizzmo.battlethrone.item.AxeItem;
import net.gizzmo.battlethrone.throne.Throne;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class PlayerListener implements Listener {
    private final BattleThrone main;
    private final Map<Player, BukkitTask> playerVisualizer;

    public PlayerListener(BattleThrone main) {
        this.main = main;
        this.playerVisualizer = new HashMap<>();
    }

    @EventHandler
    public void onPlayerPrepocessCommdn(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (message.startsWith("/altertable")) {
            event.setCancelled(true);
            this.main.getDataManager().transmission("TRUNCATE TABLE " + DataTable.tableName + ";");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Throne throne = this.main.getThroneManager().getPlayersThrone(player);

        if (throne != null) {
            PlayerLeaveThroneEvent leaveEvent = new PlayerLeaveThroneEvent(throne, player);
            Bukkit.getServer().getPluginManager().callEvent(leaveEvent);
            if (!leaveEvent.isCancelled()) {
                throne.getSettings().getPlayerInThrone().remove(player);

                if (throne.getSettings().getOwner().equals(player)) {
                    throne.getSettings().setOwner(null);
                    throne.getSettings().setCaptureTime(0);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Throne throne = this.main.getThroneManager().getPlayersThrone(player);

        if (throne != null) {
            PlayerLeaveThroneEvent leaveEvent = new PlayerLeaveThroneEvent(throne, player);
            Bukkit.getServer().getPluginManager().callEvent(leaveEvent);
            if (!leaveEvent.isCancelled()) {
                throne.getSettings().getPlayerInThrone().remove(player);

                if (throne.getSettings().getOwner().equals(player)) {
                    throne.getSettings().setOwner(null);
                    throne.getSettings().setCaptureTime(0);
                }
            }

            player.sendMessage(Lang.MSG_EVENT_THRONE_ToPlayerLeave.toMsg());
        }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        AxeItem axeItem = Settings.axeItem;
        ItemMeta axeItemMeta = axeItem.getItemStack().getItemMeta();

        if (item == null || !item.hasItemMeta() || item.getItemMeta().getDisplayName() == null) {
            return;
        }

        if (!item.getItemMeta().getDisplayName().equalsIgnoreCase(axeItemMeta.getDisplayName())) {
            return;
        }

        if (!this.main.getThroneManager().getPlayersLocations().containsKey(player)) {
            this.main.getThroneManager().getPlayersLocations().put(player, new HashMap<>());
        }

        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Location firstPosition = e.getClickedBlock().getLocation();
            String firstPositionToString = firstPosition.getWorld().getName() + ", " + firstPosition.getX() + ", " + firstPosition.getY() + ", " + firstPosition.getZ();

            this.main.getThroneManager().getPlayersLocations().get(player).put("first", e.getClickedBlock().getLocation());
            player.sendMessage(Lang.MSG_EVENT_INTERACT_AXE_FirstPosition.toMsg().replace("%pos%", firstPositionToString));
        }

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Location secondPosition = e.getClickedBlock().getLocation();
            String secondPositionToString = secondPosition.getWorld().getName() + ", " + secondPosition.getX() + ", " + secondPosition.getY() + ", " + secondPosition.getZ();

            this.main.getThroneManager().getPlayersLocations().get(player).put("second", e.getClickedBlock().getLocation());
            player.sendMessage(Lang.MSG_EVENT_INTERACT_AXE_SecondPosition.toMsg().replace("%pos%", secondPositionToString));
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location from = e.getFrom();
        Location to = e.getTo();

        for (Throne throne : this.main.getThroneManager().getAllThrones()) {
            PlayerCapturedThroneEvent capturedEvent = new PlayerCapturedThroneEvent(throne, player);
            PlayerLeaveThroneEvent leaveEvent = new PlayerLeaveThroneEvent(throne, player);
            PlayerEnterThroneEvent enterEvent = new PlayerEnterThroneEvent(throne, player);

            //Vérifie si le joueur se déplace dans la même région.
            if (throne.getMainRegion().isInArea(from) && throne.getMainRegion().isInArea(to)){
                if (!this.main.getThroneManager().hasOwner(throne) && throne.getSettings().getPlayerInThrone().size() == 1) {
                    Bukkit.getServer().getPluginManager().callEvent(capturedEvent);
                    if (!capturedEvent.isCancelled()) {
                        throne.getSettings().setOwner(player);
                        player.sendMessage(Lang.MSG_EVENT_THRONE_ToPlayerCaptured.toMsg());
                        Bukkit.getServer().getOnlinePlayers().forEach(targetPlayer -> {
                            if (targetPlayer != player) {
                                targetPlayer.sendMessage(Lang.MSG_EVENT_THRONE_ToAllPlayerCaptured.toMsg().replace("%owner%", player.getName()));
                            }
                        });
                    }
                }
            }

            //Vérifie si le joueur est sorti d'une région.
             else if (throne.getMainRegion().isInArea(from) && !throne.getMainRegion().isInArea(to)) {
                if (this.main.getThroneManager().hasOwner(throne) && throne.getSettings().getOwner().equals(player)) {
                    this.main.getThroneManager().releaseThrone(throne);
                }

                Bukkit.getServer().getPluginManager().callEvent(leaveEvent);
                if (!leaveEvent.isCancelled()) {
                    throne.getSettings().getPlayerInThrone().remove(player);
                    player.sendMessage(Lang.MSG_EVENT_THRONE_ToPlayerLeave.toMsg());
                }
            }

             //Vérifie si le joueur entre dans une région.
            else if (throne.getMainRegion().isInArea(to)) {
                Bukkit.getServer().getPluginManager().callEvent(enterEvent);
                if (!enterEvent.isCancelled()) {
                    throne.getSettings().getPlayerInThrone().add(player);
                    player.sendMessage(Lang.MSG_EVENT_THRONE_ToPlayerEnter.toMsg());
                }

                if (!this.main.getThroneManager().hasOwner(throne)) {
                    Bukkit.getServer().getPluginManager().callEvent(capturedEvent);
                    if (!capturedEvent.isCancelled()) {
                        throne.getSettings().setOwner(player);
                        player.sendMessage(Lang.MSG_EVENT_THRONE_ToPlayerCaptured.toMsg());

                        Bukkit.getServer().getOnlinePlayers().forEach(targetPlayer -> {
                            if (targetPlayer != player) {
                                targetPlayer.sendMessage(Lang.MSG_EVENT_THRONE_ToAllPlayerCaptured.toMsg().replace("%owner%", player.getName()));
                            }
                        });
                    }
                }
            }
        }

        if (this.main.hasProtocolLib() && Settings.visualizerEnable) {
            ItemStack itemInHand = player.getItemInHand();
            AxeItem axeItem = Settings.axeItem;

            if (itemInHand != null && itemInHand.hasItemMeta() && itemInHand.equals(axeItem.getItemStack())) {
                if (this.playerVisualizer.containsKey(player)) {
                    return;
                }

                this.playerVisualizer.put(player, new BukkitRunnable() {
                    @Override
                    public void run() {
                        boolean throneInRange = false;
                        for (Throne throne : PlayerListener.this.main.getThroneManager().getAllThrones()) {
                            double distance1 = throne.getMainRegion().getMin().distance(player.getLocation());
                            double distance2 = throne.getMainRegion().getMax().distance(player.getLocation());

                            if (distance1 <= Settings.visualizerViewDistance || distance2 <= Settings.visualizerViewDistance) {
                                ParticleTools.playRegionParticles(player, throne.getMainRegion(), EnumWrappers.Particle.VILLAGER_HAPPY, Settings.visualizerParticleSpeed, Settings.visualizerParticleAmount);
                                throneInRange = true;
                            }
                        }

                        if (!throneInRange) {
                            PlayerListener.this.playerVisualizer.remove(player);
                            this.cancel();
                        }
                    }
                }.runTaskTimer(this.main, 0, ((long) Settings.visualizerIntervalSend * 20)));
            } else {
                if (this.playerVisualizer.containsKey(player)) {
                    this.playerVisualizer.get(player).cancel();
                    this.playerVisualizer.remove(player);
                }
            }
        }
    }
}
