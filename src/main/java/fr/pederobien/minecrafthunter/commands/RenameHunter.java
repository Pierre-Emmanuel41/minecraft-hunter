package fr.pederobien.minecrafthunter.commands;

import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftgameplateform.commands.common.CommonRename;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class RenameHunter extends CommonRename<IHunterConfiguration> {

	protected RenameHunter() {
		super(EHunterMessageCode.RENAME_HUNTER__EXPLANATION);
	}

	@Override
	protected void onNameAlreadyTaken(CommandSender sender, String currentName, String newName) {
		sendSynchro(sender, EHunterMessageCode.RENAME_HUNTER__NAME_ALREADY_TAKEN, currentName, newName);
	}

	@Override
	protected void onNameIsMissing(CommandSender sender, String oldName) {
		sendSynchro(sender, EHunterMessageCode.RENAME_HUNTER__NAME_IS_MISSING, oldName);
	}

	@Override
	protected void onRenamed(CommandSender sender, String oldName, String newName) {
		sendSynchro(sender, EHunterMessageCode.RENAME_HUNTER__CONFIGURATION_RENAMED, oldName, newName);
	}
}
