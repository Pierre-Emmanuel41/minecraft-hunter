package fr.pederobien.minecrafthunter.commands;

import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftgameplateform.commands.common.CommonList;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class ListHunter extends CommonList<IHunterConfiguration> {

	protected ListHunter() {
		super(EHunterMessageCode.LIST_HUNTER__EXPLANATION);
	}

	@Override
	protected void onNoElement(CommandSender sender) {
		sendSynchro(sender, EHunterMessageCode.LIST_HUNTER__NO_REGISTERED_CONFIGURATION);
	}

	@Override
	protected void onOneElement(CommandSender sender, String name) {
		sendSynchro(sender, EHunterMessageCode.LIST_HUNTER__ONE_REGISTERED_CONFIGURATION, name);
	}

	@Override
	protected void onSeveralElement(CommandSender sender, String names) {
		sendSynchro(sender, EHunterMessageCode.LIST_HUNTER__SEVERAL_ELEMENTS, names);
	}
}
