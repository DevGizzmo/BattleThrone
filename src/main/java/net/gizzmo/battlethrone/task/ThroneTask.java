package net.gizzmo.battlethrone.task;

import net.gizzmo.battlethrone.BattleThrone;
import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.config.Settings;
import net.gizzmo.battlethrone.event.PlayerCapturedThroneEvent;
import net.gizzmo.battlethrone.event.ThroneRewardEvent;
import net.gizzmo.battlethrone.throne.Throne;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class ThroneTask implements Runnable {
    private final BattleThrone main = BattleThrone.getInstance();
    private final Throne throne;

    public ThroneTask(final Throne throne) {
        this.throne = throne;
    }

    public void run() {
        List<Player> playersInThrone = this.throne.getSettings().getPlayerInThrone();

        if (this.main.getThroneManager().hasOwner(this.throne)) {
            OfflinePlayer owner = this.throne.getSettings().getOwner();

            this.throne.getSettings().setCounterTask(this.throne.getSettings().getCounterTask() + 1);
            this.throne.getSettings().setCaptureTime(this.throne.getSettings().getCaptureTime() + 1);

            if (playersInThrone.size() > 1) {
                this.main.getThroneManager().swapingBlock(this.throne, Settings.blockSwapFrom, Settings.blockSwapTo);
                this.throne.getSettings().setCaptureTime(0);
            }

            if (playersInThrone.size() == 1) {
                this.main.getThroneManager().swapingBlock(this.throne, Settings.blockSwapTo, Settings.blockSwapFrom);
            }

            if (this.throne.getSettings().getCounterTask() >= this.throne.getSettings().getCooldownPeriod() && playersInThrone.size() == 1) {
                ThroneRewardEvent rewardEvent = new ThroneRewardEvent(this.throne, this.throne.getSettings().getOwner().getPlayer());
                Bukkit.getServer().getPluginManager().callEvent(rewardEvent);
                if (!rewardEvent.isCancelled()) {
                    if (Settings.rewardMoneyEnable) {
                        this.main.getEconomy().depositPlayer(owner, this.throne.getSettings().getMoneyEarning());
                    }

                    if (Settings.rewardCommandEnable) {
                        for (String command : this.throne.getSettings().getCommands().values()) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replace("{player}", owner.getPlayer().getName()).replace("/", ""));
                        }
                    }

                    for (String rewardMessage : Settings.rewardMessage) {
                        owner.getPlayer().sendMessage(rewardMessage.replace("%money_earning%", String.valueOf(this.throne.getSettings().getMoneyEarning())));
                    }
                }

                this.throne.getSettings().setCounterTask(0);
            }
        } else if (playersInThrone.size() == 1){
            Player player = playersInThrone.get(0);

            PlayerCapturedThroneEvent capturedEvent = new PlayerCapturedThroneEvent(this.throne, player);
            Bukkit.getServer().getPluginManager().callEvent(capturedEvent);
            if (!capturedEvent.isCancelled()) {
                this.throne.getSettings().setOwner(player);
                player.sendMessage(Lang.MSG_EVENT_THRONE_ToPlayerCaptured.toMsg());
                Bukkit.getServer().getOnlinePlayers().forEach(targetPlayer -> targetPlayer.sendMessage(Lang.MSG_EVENT_THRONE_ToAllPlayerCaptured.toMsg().replace("%owner%", player.getName())));
            }
        }
    }
}
