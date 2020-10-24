package fr.pederobien.minecrafthunter.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftgameplateform.dictionary.ECommonMessageCode;
import fr.pederobien.minecraftgameplateform.impl.editions.AbstractLabelEdition;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.exceptions.MinimumDistanceMustBePositive;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class MinimumDistance extends AbstractLabelEdition<IHunterConfiguration> {

	protected MinimumDistance() {
		super(EHunterLabel.MINIMUM_DISTANCE, EHunterMessageCode.HUNTER_MINIMUM_DISTANCE__EXPLANATION);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		try {
			get().setMinimumDistance(getInt(args[0]));
			sendSynchro(sender, EHunterMessageCode.HUNTER_MINIMUM_DISTANCE__DISTANCE_DEFINED, get().getMinimumDistance(), get().getName());
		} catch (IndexOutOfBoundsException e) {
			sendNotSynchro(sender, EHunterMessageCode.HUNTER_MINIMUM_DISTANCE__DISTANCE_IS_MISSING);
			return false;
		} catch (NumberFormatException e) {
			sendNotSynchro(sender, ECommonMessageCode.COMMON_BAD_INTEGER_FORMAT);
			return false;
		} catch (MinimumDistanceMustBePositive e) {
			sendNotSynchro(sender, EHunterMessageCode.HUNTER_MINIMUM_DISTANCE__DISTANCE_MUST_BE_POSITIVE);
			return false;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (args.length) {
		case 1:
			return asList(getMessage(sender, EHunterMessageCode.HUNTER_MINIMUM_DISTANCE__ON_TAB_COMPLETE));
		default:
			return emptyList();
		}
	}
}
