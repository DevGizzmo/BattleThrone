package net.gizzmo.battlethrone.command.admin;

import net.gizzmo.battlethrone.api.tools.area.AreaTools;
import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.config.Settings;
import net.gizzmo.battlethrone.core.MSubCommand;
import net.gizzmo.battlethrone.hologram.MHologram;
import net.gizzmo.battlethrone.throne.settings.SettingsThrone;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;

public class CmdThroneAdminCreate extends MSubCommand {
    public CmdThroneAdminCreate() {
        this.aliases.add("create");
        this.aliases.add("cr");
        this.correctUsage = "/tadmin create <name> <montant_earning> <delay_time>";
        this.permission = "battlethrone.throneadmin.create";
        this.senderMustBePlayer = true;
    }

    @Override
    public void execute() {
        if (args.length != 3) {
            this.sendCorrectUsage();
            return;
        }

        try {
            String displayName = args[0];
            int moneyEarning = Integer.parseInt(args[1]);
            int cooldownPeriod = Integer.parseInt(args[2]);

            if (this.main.getThroneManager().isThrone(displayName)) {
                this.msg(Lang.MSG_THRONEADMIN_CREATE_AlreadyExists.toMsg().replace("%name%", displayName));
                return;
            }

            if (!this.main.getThroneManager().getPlayersLocations().containsKey(this.player)) {
                this.msg(Lang.MSG_THRONEADMIN_CREATE_AREA_NoPosition.toMsg());
                return;
            }

            Location pos1 = this.main.getThroneManager().getPlayersLocations().get(player).get("first");
            Location pos2 = this.main.getThroneManager().getPlayersLocations().get(player).get("second");

            if (pos1 == null) {
                this.msg(Lang.MSG_THRONEADMIN_CREATE_AREA_NoFirstPosition.toMsg());
                return;
            }

            if (pos2 == null) {
                this.msg(Lang.MSG_THRONEADMIN_CREATE_AREA_NoSecondPosition.toMsg());
                return;
            }

            AreaTools mainRegion = new AreaTools(pos1, pos2);
            SettingsThrone settings = new SettingsThrone(moneyEarning, cooldownPeriod, new HashMap<>());

            MHologram hologram = null;
            if (Settings.hologramEnable) {
                List<String> lineList = Settings.hologramLines;
                lineList.replaceAll(s -> s.replaceAll("<throne>", displayName));

                hologram = new MHologram(displayName, this.player.getLocation().add(0, 2, 0), lineList);
                hologram.init();
            }

            this.main.getThroneManager().createThrone(this.player, displayName, mainRegion, settings, hologram);
        } catch (NumberFormatException e){
            this.msg(Lang.MSG_THRONEADMIN_NumberFormat.toMsg());
        }
    }
}
