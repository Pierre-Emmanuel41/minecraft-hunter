package fr.pederobien.minecrafthunter.commands;

import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftgameplateform.commands.common.CommonLoad;
import fr.pederobien.minecraftgameplateform.commands.common.ECommonLabel;
import fr.pederobien.minecraftgameplateform.commands.configurations.EGameConfigurationLabel;
import fr.pederobien.minecraftgameplateform.interfaces.element.ILabel;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class LoadHunter extends CommonLoad<IHunterConfiguration> {

	protected LoadHunter() {
		super(EHunterMessageCode.LOAD_HUNTER__EXPLANATION);
	}

	@Override
	protected void onStyleLoaded(CommandSender sender, String name) {
		sendSynchro(sender, EHunterMessageCode.LOAD_HUNTER__CONFIGURATION_LOADED, name);
		setAllAvailable();
	}

	@Override
	protected void onNameIsMissing(CommandSender sender) {
		sendSynchro(sender, EHunterMessageCode.LOAD_HUNTER__NAME_IS_MISSING);
	}

	private void setAllAvailable() {
		for (ILabel label : ECommonLabel.values())
			setAvailableLabelEdition(label);
		for (ILabel label : EGameConfigurationLabel.values())
			// There is no team for an hunter configuration
			if (label.equals(EGameConfigurationLabel.TEAM))
				continue;
			else
				setAvailableLabelEdition(label);
		for (ILabel label : EHunterLabel.values())
			setAvailableLabelEdition(label);
	}
}
