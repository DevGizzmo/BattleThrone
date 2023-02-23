package net.gizzmo.battlethrone.command.admin.set;

import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.core.MSubCommand;
import net.gizzmo.battlethrone.throne.Throne;

import java.text.DecimalFormat;

public class CmdThroneAdminSetMoneyEarning extends MSubCommand {
    public CmdThroneAdminSetMoneyEarning() {
        this.aliases.add("money_earning");
        this.aliases.add("me");
        this.correctUsage = "/tadmin set money_earning <throne_name> <new_value>";
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
            int moneyEarning = Integer.parseInt(args[2]);

            Throne throne = this.main.getThroneManager().getThrone(displayName);
            throne.getSettings().setMoneyEarning(moneyEarning);
            throne.save();
            this.msg(Lang.MSG_THRONEADMIN_SET_MoneyEarning.toMsg().replace("%money_earning%", new DecimalFormat("#,###").format(throne.getSettings().getMoneyEarning())));
        } catch (NumberFormatException exception) {
            this.msg(Lang.MSG_THRONEADMIN_NumberFormat.toMsg());
        }
    }
}
