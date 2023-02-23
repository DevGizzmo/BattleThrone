package net.gizzmo.battlethrone.command.admin.set;

import net.gizzmo.battlethrone.api.tools.area.AreaTools;
import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.core.MSubCommand;
import net.gizzmo.battlethrone.throne.Throne;
import org.bukkit.Location;

public class CmdThroneAdminSetLocation extends MSubCommand {
    public CmdThroneAdminSetLocation() {
        this.aliases.add("location");
        this.aliases.add("loc");
        this.correctUsage = "/tadmin set location <throne_name>";
        this.permission = "battlethrone.throneadmin.set";
        this.senderMustBePlayer = true;
    }

    @Override
    public void execute() {
        if (args.length <= 1) {
            this.sendCorrectUsage();
            return;
        }

        String displayName = args[1];

        if (!this.main.getThroneManager().getPlayersLocations().containsKey(this.player)) {
            this.msg(Lang.MSG_THRONEADMIN_SET_POSITION_NoPosition.toMsg());
            return;
        }

        Location pos1 = this.main.getThroneManager().getPlayersLocations().get(player).get("first");
        Location pos2 = this.main.getThroneManager().getPlayersLocations().get(player).get("second");

        if (pos1 == null) {
            this.msg(Lang.MSG_THRONEADMIN_SET_POSITION_NoFirstPosition.toMsg());
            return;
        }

        if (pos2 == null) {
            this.msg(Lang.MSG_THRONEADMIN_SET_POSITION_NoSecondPosition.toMsg());
            return;
        }

        Throne throne = this.main.getThroneManager().getThrone(displayName);
        AreaTools newMainRegion = new AreaTools(pos1, pos2);
        throne.setMainRegion(newMainRegion);
        throne.save();
        this.msg(Lang.MSG_THRONEADMIN_SET_POSITION_Modified.toMsg().replace("%pos%", this.locationToMsg(throne)));

    }

    private String locationToMsg(Throne throne) {
        Location corner1 = throne.getMainRegion().getMax();
        Location corner2 = throne.getMainRegion().getMin();

        return "(World: " + corner1.getWorld().getName() + ") (Pos1: " + (int) corner1.getX() + ", " + (int) corner1.getY() + ", " + (int) corner1.getZ() + ") (Pos2: " + (int) corner2.getX() + ", " + (int) corner2.getY() + ", " + (int) corner2.getZ() + ")";
    }
}
