package net.gizzmo.battlethrone.command.admin.set;

import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.core.MSubCommand;
import net.gizzmo.battlethrone.throne.Throne;

public class CmdThroneAdminSetName extends MSubCommand {
    public CmdThroneAdminSetName() {
        this.aliases.add("name");
        this.correctUsage = "/tadmin set name <throne_name> <new_name>";
        this.permission = "battlethrone.throneadmin.set";
        this.senderMustBePlayer = true;
    }

    @Override
    public void execute() {
        if (args.length <= 2) {
            this.sendCorrectUsage();
            return;
        }

        String displayName = args[1];
        String newName = args[2];

        this.main.getThroneManager().getThrone(displayName).setName(newName);

        Throne throne = this.main.getThroneManager().getThrone(newName);
        if (throne.getMHologram() != null) {
            throne.getMHologram().setThroneName(newName);
            throne.getMHologram().init();
        }
        throne.save();
        this.msg(Lang.MSG_THRONEADMIN_SET_Name.toMsg().replace("%name%", throne.getName()));

    }
}
