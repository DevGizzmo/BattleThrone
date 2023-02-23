package net.gizzmo.battlethrone.command.admin;

import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.core.MSubCommand;
import net.gizzmo.battlethrone.throne.Throne;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class CmdThroneAdminList extends MSubCommand {
    public CmdThroneAdminList() {
        this.aliases.add("list");
        this.correctUsage  = "/tadmin list <page>";
        this.permission = "battlethrone.throneadmin.list";
        this.senderMustBePlayer = false;
    }

    @Override
    public void execute() {
        if (this.args.length != 1) {
            this.sendCorrectUsage();
            return;
        }

        try {
            int page = Integer.parseInt(args[0]);
            if (page == 0) {
                page = 1;
            }

            if (page > this.paginateThrones().size()) {
                page = this.paginateThrones().size();
            }

            this.msg(Lang.MSG_THRONEADMIN_LIST_Header.toMsg().replace("%page%", String.valueOf(page)).replace("%totalpages%", String.valueOf(this.paginateThrones().size())));
            if (this.paginateThrones().isEmpty()) {
                this.msg(Lang.MSG_THRONEADMIN_LIST_Empty.toMsg());
                return;
            }

            for (Throne throne : this.paginateThrones().get(page - 1)) {
                this.msg(Lang.MSG_THRONEADMIN_LIST_Throne.toMsg().replace("%id%", String.valueOf(throne.getId())).replace("%name%", throne.getName()).replace("%location%", this.locationToMsg(throne)));
            }
        } catch (NumberFormatException e) {
            this.msg(Lang.MSG_THRONEADMIN_NumberFormat.toMsg());
        }
    }

    private List<List<Throne>> paginateThrones() {
        List<List<Throne>> paginatedThrones = new ArrayList<>();
        int currentPage = 0;
        int pageSize = 5;
        while (currentPage * pageSize < this.main.getThroneManager().getAllThrones().size()) {
            int startIndex = currentPage * pageSize;
            int endIndex = Math.min(startIndex + pageSize, this.main.getThroneManager().getAllThrones().size());
            List<Throne> page = this.main.getThroneManager().getAllThrones().subList(startIndex, endIndex);
            paginatedThrones.add(page);
            currentPage++;
        }
        return paginatedThrones;
    }

    private String locationToMsg(Throne throne) {
        Location corner1 = throne.getMainRegion().getMax();
        Location corner2 = throne.getMainRegion().getMin();

        return "(World: " + corner1.getWorld().getName() + ") (Pos1: " + (int) corner1.getX() + ", " + (int) corner1.getY() + ", " + (int) corner1.getZ() + ") (Pos2: " + (int) corner2.getX() + ", " + (int) corner2.getY() + ", " + (int) corner2.getZ() + ")";
    }
}
