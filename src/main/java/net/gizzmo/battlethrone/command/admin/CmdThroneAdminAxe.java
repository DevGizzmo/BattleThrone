package net.gizzmo.battlethrone.command.admin;

import net.gizzmo.battlethrone.config.Lang;
import net.gizzmo.battlethrone.config.Settings;
import net.gizzmo.battlethrone.core.MSubCommand;

public class CmdThroneAdminAxe extends MSubCommand {

    public CmdThroneAdminAxe() {
        this.aliases.add("axe");
        this.correctUsage  = "/tadmin axe";
        this.permission = "battlethrone.throneadmin.axe";
        this.senderMustBePlayer = true;
    }

    @Override
    public void execute() {
        if (this.args.length >= 1) {
            this.sendCorrectUsage();
            return;
        }

        if (this.player.getInventory().firstEmpty() != -1) {
            this.player.getInventory().addItem(Settings.axeItem.getItemStack());
            this.msg(Lang.MSG_THRONEADMIN_AXE_GiveItem.toMsg());
        } else {
            this.msg(Lang.MSG_THRONEADMIN_AXE_InventoryFull.toMsg());
        }
    }
}
