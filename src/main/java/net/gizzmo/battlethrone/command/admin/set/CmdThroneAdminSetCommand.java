package net.gizzmo.battlethrone.command.admin.set;

import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.core.MSubCommand;
import net.gizzmo.battlethrone.throne.Throne;

import java.util.Arrays;
import java.util.Map;

public class CmdThroneAdminSetCommand extends MSubCommand {
    public CmdThroneAdminSetCommand() {
        this.aliases.add("command");
        this.aliases.add("cmd");
        this.correctUsage = "/tadmin set command <throne_name> <add|remove|list>";
        this.permission = "battlethrone.throneadmin.set";
        this.senderMustBePlayer = false;
    }

    @Override
    public void execute() {
        if (args.length <= 2) {
            this.sendCorrectUsage();
            return;
        }


        Throne throne = this.main.getThroneManager().getThrone(args[1]);
        String key = args[2];

        if (!key.equalsIgnoreCase("add") && !key.equalsIgnoreCase("remove") && !key.equalsIgnoreCase("list")) {
            this.sendCorrectUsage();
            return;
        }

        if (args.length == 3 && key.equalsIgnoreCase("add")) {
            this.msg(Lang.MSG_THRONEADMIN_SET_COMMAND_ADD_Usage.toMsg());
            return;
        }

        if (args.length == 3 && key.equalsIgnoreCase("remove")) {
            this.msg(Lang.MSG_THRONEADMIN_SET_COMMAND_REMOVE_Usage.toMsg());
            return;
        }

        if (key.equalsIgnoreCase("list")) {
            this.msg(Lang.MSG_THRONEADMIN_SET_COMMAND_LIST_Header.toMsg().replace("%throne_name%", throne.getName()));
            if (throne.getSettings().getCommands().isEmpty()) {
                this.msg(Lang.MSG_THRONEADMIN_SET_COMMAND_LIST_Empty.toMsg());
                return;
            }

            for (Map.Entry<Integer, String> entry : throne.getSettings().getCommands().entrySet()) {
                this.msg(Lang.MSG_THRONEADMIN_SET_COMMAND_LIST_Info.toMsg().replace("%commande_id%", String.valueOf(entry.getKey())).replace("%commande%", entry.getValue()));
            }
            return;
        }

        String[] commandsSplit = String.join(" ", Arrays.copyOfRange(args, 3, args.length)).split(",");
        boolean update = false;

        for (String command : commandsSplit) {
            if (key.equalsIgnoreCase("add")) {
                if (!command.startsWith("/")) {
                    this.msg(Lang.MSG_THRONEADMIN_SET_COMMAND_ADD_CommandInvalid.toMsg().replace("%commande%", command));
                    continue;
                }

                int id = 1;
                while (throne.getSettings().getCommands().containsKey(id)) {
                    id++;
                }
                throne.getSettings().addCommand(id, command);
                this.msg(Lang.MSG_THRONEADMIN_SET_COMMAND_ADD_Added.toMsg().replace("%commande%", command));
                update = true;
            }

            if (key.equalsIgnoreCase("remove")) {
                try {
                    int id = Integer.parseInt(command);
                    if (throne.getSettings().getCommands().containsKey(id)) {
                        this.msg(Lang.MSG_THRONEADMIN_SET_COMMAND_REMOVE_Removed.toMsg().replace("%commande%", throne.getSettings().getCommands().get(id)));
                        throne.getSettings().removeCommand(id);
                        update = true;
                    } else {
                        this.msg(Lang.MSG_THRONEADMIN_SET_COMMAND_REMOVE_IdNotFound.toMsg().replace("%commande_id%", String.valueOf(id)));
                    }
                } catch (NumberFormatException exception) {
                    this.msg(Lang.MSG_THRONEADMIN_SET_COMMAND_REMOVE_IdInvalid.toMsg().replace("%commande_id%", command));
                }
            }
        }

        if (update) {
            throne.save();
        }
    }
}
