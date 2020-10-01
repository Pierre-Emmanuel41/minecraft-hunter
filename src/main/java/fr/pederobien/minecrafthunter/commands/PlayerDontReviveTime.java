package fr.pederobien.minecrafthunter.commands;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftdevelopmenttoolkit.utils.DisplayHelper;
import fr.pederobien.minecraftgameplateform.dictionary.ECommonMessageCode;
import fr.pederobien.minecraftgameplateform.impl.editions.AbstractLabelEdition;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class PlayerDontReviveTime extends AbstractLabelEdition<IHunterConfiguration> {

	protected PlayerDontReviveTime() {
		super(EHunterLabel.PLAYER_DONT_REVIVE_TIME, EHunterMessageCode.PLAYER_DONT_REVIVE_TIME_HUNTER__EXPLANATION);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			get().setPlayerDontReviveTime(LocalTime.parse(args[0]));
			if (get().getPlayerDontReviveTime().equals(LocalTime.of(0, 0, 0)))
				sendSynchro(sender, EHunterMessageCode.PLAYER_DONT_REVIVE_TIME_HUNTER__FROM_THE_BEGINNING);
			else
				sendSynchro(sender, EHunterMessageCode.PLAYER_DONT_REVIVE_TIME_HUNTER__TIME_DEFINED, DisplayHelper.toString(get().getPlayerDontReviveTime(), false));
			return true;
		} catch (IndexOutOfBoundsException e) {
			sendSynchro(sender, EHunterMessageCode.PLAYER_DONT_REVIVE_TIME_HUNTER__TIME_IS_MISSING);
			return false;
		} catch (DateTimeParseException e) {
			sendSynchro(sender, ECommonMessageCode.COMMON_BAD_TIME_FORMAT);
			return false;
		}
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

	@Override
	public boolean isAvailable() {
		return get() != null && !get().isUhc();
	}
}
