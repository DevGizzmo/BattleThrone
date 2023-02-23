package net.gizzmo.battlethrone.core;

import net.gizzmo.battlethrone.BattleThrone;

import java.util.ArrayList;
import java.util.List;

public class MCommand {
	public BattleThrone main;
	public List<MSubCommand> subCommands;
	
	public MCommand(final BattleThrone main) {
		this.main = main;
		this.subCommands = new ArrayList<>();
	}
}
