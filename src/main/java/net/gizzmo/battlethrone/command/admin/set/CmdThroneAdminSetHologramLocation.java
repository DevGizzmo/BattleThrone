package net.gizzmo.battlethrone.command.admin.set;

import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.config.Settings;
import net.gizzmo.battlethrone.core.MSubCommand;
import net.gizzmo.battlethrone.hologram.MHologram;
import net.gizzmo.battlethrone.throne.Throne;
import org.bukkit.Location;

public class CmdThroneAdminSetHologramLocation extends MSubCommand {
    public CmdThroneAdminSetHologramLocation() {
        this.aliases.add("hologram_location");
        this.aliases.add("hl");
        this.correctUsage = "/tadmin set hologram_location <throne_name>";
        this.permission = "battlethrone.throneadmin.set";
        this.senderMustBePlayer = true;
    }

    @Override
    public void execute() {
        if (args.length <= 1) {
            this.sendCorrectUsage();
            return;
        }

        Throne throne = this.main.getThroneManager().getThrone(args[1]);
        MHologram hologram = throne.getMHologram();
        Location playerLocation = this.player.getLocation().add(0, 2, 0);

        if (!Settings.hologramEnable) {
            this.msg(Lang.MSG_THRONEADMIN_SET_HOLOGRAM_NotEnable.toMsg());
            return;
        }

        if (!this.main.hasHolographicDisplays()) {
            this.msg(Lang.MSG_THRONEADMIN_SET_HOLOGRAM_HolographicDisplayNotEnable.toMsg());
            return;
        }

        if (hologram == null) {
            this.msg(Lang.MSG_THRONEADMIN_SET_HOLOGRAM_NotFound.toMsg().replace("%name%", throne.getName()));
            return;
        }

        hologram.getHologram().setPosition(playerLocation);
        hologram.setLocation(playerLocation);
        throne.save();
        this.msg(Lang.MSG_THRONEADMIN_SET_HOLOGRAM_Moved.toMsg().replace("%pos%", locationToMsg(hologram.getLocation())));
    }

    private String locationToMsg(Location location) {
        return "(World: " + location.getWorld().getName() + ") (Position: " + (int) location.getX() + ", " + (int) location.getY() + ", " + (int) location.getZ() + ")";
    }
}
