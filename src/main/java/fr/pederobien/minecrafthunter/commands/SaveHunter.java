package fr.pederobien.minecrafthunter.commands;

import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftgameplateform.commands.common.CommonSave;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class SaveHunter extends CommonSave<IHunterConfiguration> {

	protected SaveHunter() {
		super(EHunterMessageCode.SAVE_HUNTER__EXPLANATION);
	}

	@Override
	protected void onSave(CommandSender sender, String name) {
		sendSynchro(sender, EHunterMessageCode.SAVE_HUNTER__CONFIGURATION_SAVED, name);
	}
}
