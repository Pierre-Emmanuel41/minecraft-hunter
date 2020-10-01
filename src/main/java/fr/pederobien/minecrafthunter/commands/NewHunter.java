package fr.pederobien.minecrafthunter.commands;

import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftgameplateform.commands.common.CommonNew;
import fr.pederobien.minecraftgameplateform.commands.common.ECommonLabel;
import fr.pederobien.minecraftgameplateform.commands.configurations.EGameConfigurationLabel;
import fr.pederobien.minecraftgameplateform.interfaces.element.ILabel;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.impl.HunterConfiguration;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class NewHunter extends CommonNew<IHunterConfiguration> {

	protected NewHunter() {
		super(EHunterMessageCode.NEW_HUNTER__EXPLANATION);
	}

	@Override
	protected void onNameAlreadyTaken(CommandSender sender, String name) {
		sendSynchro(sender, EHunterMessageCode.NEW_HUNTER__NAME_ALREADY_TAKEN, name);
	}

	@Override
	protected void onNameIsMissing(CommandSender sender) {
		sendSynchro(sender, EHunterMessageCode.NEW_HUNTER__NAME_IS_MISSING);
	}

	@Override
	protected IHunterConfiguration create(String name) {
		return new HunterConfiguration(name);
	}

	@Override
	protected void onCreated(CommandSender sender, String name) {
		sendSynchro(sender, EHunterMessageCode.NEW_HUNTER__CONFIGURATION_CREATED, name);
		setAllAvailable();
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
