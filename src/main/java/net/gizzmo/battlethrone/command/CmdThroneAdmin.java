package net.gizzmo.battlethrone.command;

import net.gizzmo.battlethrone.BattleThrone;
import net.gizzmo.battlethrone.command.admin.*;
import net.gizzmo.battlethrone.command.admin.set.*;
import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.core.MCommand;
import net.gizzmo.battlethrone.core.MSubCommand;
import net.maliimaloo.battlethrone.command.admin.*;
import net.maliimaloo.battlethrone.command.admin.set.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CmdThroneAdmin extends MCommand implements CommandExecutor {
    private final List<MSubCommand> setSubCommands;
    public CmdThroneAdmin(BattleThrone main) {
        super(main);
        this.subCommands.add(new CmdThroneAdminReload());
        this.subCommands.add(new CmdThroneAdminAxe());
        this.subCommands.add(new CmdThroneAdminList());
        this.subCommands.add(new CmdThroneAdminCreate());
        this.subCommands.add(new CmdThroneAdminDelete());

        this.setSubCommands = new ArrayList<>();
        this.setSubCommands.add(new CmdThroneAdminSetName());
        this.setSubCommands.add(new CmdThroneAdminSetMoneyEarning());
        this.setSubCommands.add(new CmdThroneAdminSetCooldownPeriod());
        this.setSubCommands.add(new CmdThroneAdminSetLocation());
        this.setSubCommands.add(new CmdThroneAdminSetHologramLocation());
        this.setSubCommands.add(new CmdThroneAdminSetCommand());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Lang.MSG_USAGE_ThroneAdmin.toMsg());
            return true;
        }

        if (args[0].equals("set")) {
            if (args.length == 1) {
                sender.sendMessage(Lang.MSG_USAGE_ThroneAdminSet.toMsg());
                return true;
            }

            if (args.length >= 3) {
                String displayName = args[2];

                if (!this.main.getThroneManager().isThrone(displayName)) {
                    sender.sendMessage(Lang.MSG_THRONEADMIN_SET_ThroneNotExists.toMsg().replace("%name%", displayName));
                    return true;
                }
            }

            this.findSubCommand(sender, this.setSubCommands, args[1], args, Lang.MSG_USAGE_ThroneAdminSet);
        } else {
            this.findSubCommand(sender, this.subCommands, args[0], args, Lang.MSG_USAGE_ThroneAdmin);
        }

        return true;
    }

    private void findSubCommand(CommandSender sender, List<MSubCommand> subCommands, String argsCheck, String[] args, Lang msgUsage) {
        Iterator<MSubCommand> iterator = subCommands.iterator();

        MSubCommand subCommand;
        do {
            if (!iterator.hasNext()) {
                sender.sendMessage(msgUsage.toMsg());
                return;
            }

            subCommand = iterator.next();
        } while (!subCommand.aliases.contains(argsCheck));

        subCommand.execute(sender, args);
    }
}
