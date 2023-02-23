package net.gizzmo.battlethrone.command.admin;

import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.core.MSubCommand;
import net.gizzmo.battlethrone.throne.Throne;

public class CmdThroneAdminDelete extends MSubCommand {
    public CmdThroneAdminDelete() {
        this.aliases.add("delete");
        this.aliases.add("del");
        this.correctUsage = "/tadmin delete <name>";
        this.permission = "battlethrone.throneadmin.delete";
        this.senderMustBePlayer = false;
    }

    @Override
    public void execute() {
        if (this.args.length != 1) {
            this.sendCorrectUsage();
            return;
        }

        String displayName = args[0];

        if (!this.main.getThroneManager().isThrone(displayName)) {
            this.msg(Lang.MSG_THRONEADMIN_DELETE_NotExists.toMsg().replace("%name%", displayName));
            return;
        }

        Throne throne = this.main.getThroneManager().getThrone(displayName);
        this.main.getThroneManager().removeThrone(throne, var1 -> this.msg(Lang.MSG_THRONEADMIN_DELETE_Deleted.toMsg()));
    }
}
