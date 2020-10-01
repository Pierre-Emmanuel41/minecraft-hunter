package fr.pederobien.minecrafthunter.commands;

import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftgameplateform.commands.common.CommonDelete;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class DeleteHunter extends CommonDelete<IHunterConfiguration> {

	protected DeleteHunter() {
		super(EHunterMessageCode.DELETE_HUNTER__EXPLANATION);
	}

	@Override
	protected void onDidNotDelete(CommandSender sender, String name) {
		sendSynchro(sender, EHunterMessageCode.DELETE_HUNTER__DID_NOT_DELETE, name);
	}

	@Override
	protected void onDeleted(CommandSender sender, String name) {
		sendSynchro(sender, EHunterMessageCode.DELETE_HUNTER__CONFIGURATION_DELETED, name);
	}

	@Override
	protected void onNameIsMissing(CommandSender sender) {
		sendSynchro(sender, EHunterMessageCode.DELETE_HUNTER__NAME_IS_MISSING);
	}
}
