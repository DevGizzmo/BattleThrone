package net.gizzmo.battlethrone.command.admin.set;

import net.gizzmo.battlethrone.core.MSubCommand;
import net.gizzmo.battlethrone.throne.Throne;
import net.gizzmo.battlethrone.config.Lang;

public class CmdThroneAdminSetCooldownPeriod extends MSubCommand {
    public CmdThroneAdminSetCooldownPeriod() {
        this.aliases.add("cooldown_period");
        this.aliases.add("cp");
        this.correctUsage = "/tadmin set cooldown_period <throne_name> <new_value>";
        this.permission = "battlethrone.throneadmin.set";
        this.senderMustBePlayer = false;
    }

    @Override
    public void execute() {
        if (args.length <= 2) {
            this.sendCorrectUsage();
            return;
        }

        try {
            String displayName = args[1];
            int cooldownPeriod = Integer.parseInt(args[2]);

            Throne throne = this.main.getThroneManager().getThrone(displayName);
            throne.getSettings().setCooldownPeriod(cooldownPeriod);
            throne.save();
            this.msg(Lang.MSG_THRONEADMIN_SET_CooldownPeriod.toMsg().replace("%cooldown_period%", String.valueOf(throne.getSettings().getCooldownPeriod())));
        } catch (NumberFormatException exception) {
            this.msg(Lang.MSG_THRONEADMIN_NumberFormat.toMsg());
        }
    }
}
