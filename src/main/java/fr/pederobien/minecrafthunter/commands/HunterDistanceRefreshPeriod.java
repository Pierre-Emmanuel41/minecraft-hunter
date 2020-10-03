package fr.pederobien.minecrafthunter.commands;

import java.time.format.DateTimeParseException;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftdevelopmenttoolkit.utils.DisplayHelper;
import fr.pederobien.minecraftgameplateform.dictionary.ECommonMessageCode;
import fr.pederobien.minecraftgameplateform.impl.editions.AbstractLabelEdition;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class HunterDistanceRefreshPeriod extends AbstractLabelEdition<IHunterConfiguration> {

	protected HunterDistanceRefreshPeriod() {
		super(EHunterLabel.HUNTER_DISTANCE_REFRESH_PERIOD, EHunterMessageCode.HUNTER_DISTANCE_REFRESH_PERIOD__EXPLANATION);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			get().setHunterDistanceRefreshPeriod(getTime(args[0]));
			sendSynchro(sender, EHunterMessageCode.HUNTER_DISTANCE_REFRESH_PERIOD__TIME_DEFINED, get().getName(),
					DisplayHelper.toString(get().getHunterDistanceRefreshPeriod(), true));
		} catch (IndexOutOfBoundsException e) {
			sendNotSynchro(sender, EHunterMessageCode.HUNTER_DISTANCE_REFRESH_PERIOD__TIME_IS_MISSING);
			return false;
		} catch (DateTimeParseException e) {
			sendNotSynchro(sender, ECommonMessageCode.COMMON_BAD_TIME_FORMAT);
			return false;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (args.length) {
		case 1:
			return asList(getMessage(sender, ECommonMessageCode.COMMON_TIME_TAB_COMPLETE));
		default:
			return emptyList();
		}
	}
}
