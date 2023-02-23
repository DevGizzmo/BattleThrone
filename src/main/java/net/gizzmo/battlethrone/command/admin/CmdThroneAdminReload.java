package net.gizzmo.battlethrone.command.admin;

import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.core.MSubCommand;

public class CmdThroneAdminReload extends MSubCommand {

    public CmdThroneAdminReload() {
        this.aliases.add("reload");
        this.aliases.add("rl");
        this.correctUsage  = "/tadmin reload";
        this.permission = "battlethrone.throneadmin.reload";
        this.senderMustBePlayer = false;
    }

    @Override
    public void execute() {
        if (this.args.length >= 1) {
            this.sendCorrectUsage();
            return;
        }

        this.main.reload();
        this.msg(Lang.MSG_THRONEADMIN_RELOAD_Reloaded.toMsg());
    }
}
