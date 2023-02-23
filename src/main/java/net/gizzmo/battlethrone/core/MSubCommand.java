package net.gizzmo.battlethrone.core;

import net.gizzmo.battlethrone.BattleThrone;
import net.gizzmo.battlethrone.config.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class MSubCommand {
    public final BattleThrone main = BattleThrone.getInstance();
	public List<String> aliases = new ArrayList<>();
    public CommandSender sender;
    public String[] args;
    public Player player;
    public String correctUsage = "";
    public String permission = "";
    public boolean senderMustBePlayer;
    
    public MSubCommand() {
    }
    
    public void execute(final CommandSender sender, final String[] parts) {
    	if (sender instanceof Player) {
    		this.player = (Player) sender;
    	} else {
    		this.player = null;
    	}
    	
    	this.sender = sender;
        LinkedList<String> linkedList = new LinkedList<>(Arrays.asList(parts));
        linkedList.remove(0);
        this.args = linkedList.toArray(new String[0]);
        if (!sender.hasPermission(this.permission) && !sender.isOp()) {
            this.msg(Lang.MSG_NoAccess.toMsg());
        } else if (this.senderMustBePlayer && !this.isPlayer()) {
        	this.msg(Lang.MSG_PLAYER_Only.toMsg());
        } else {
            this.execute();
        }
    }
    
    public abstract void execute();

    public boolean isPlayer() {
        return this.player != null;
    }
    public String getSenderName() {
        return this.isPlayer() ? this.player.getName() : "Console";
    }
    public void msg(String message) {
        this.sender.sendMessage(message);
    }
    public void sendCorrectUsage() {
        this.msg(Lang.MSG_USAGE_SubCommand.toMsg().replace("%usage%", this.correctUsage));
    }
    
    public String buildStringFromArgs(final int startIndex, int endIndex) {
        if (endIndex < 0) {
            endIndex = 0;
        }

        String result;
        if (this.args.length > startIndex + 1) {
            StringBuilder stringBuilder = new StringBuilder(this.args[startIndex]);

            for(int index = startIndex + 1; index < endIndex + 1; ++index) {
                stringBuilder.append(" ").append(this.args[index]);
            }

            result = stringBuilder.toString();
        } else {
            result = this.args[startIndex];
        }

        return result;
    }
    public String objectToStr(final Object var1) {
        return String.valueOf(var1);
    }

}
