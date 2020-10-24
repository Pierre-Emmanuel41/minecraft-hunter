package fr.pederobien.minecrafthunter.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftgameplateform.dictionary.ECommonMessageCode;
import fr.pederobien.minecraftgameplateform.impl.editions.AbstractLabelEdition;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.exceptions.DecayValueMustBeGreaterThanOne;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class DecayHunter extends AbstractLabelEdition<IHunterConfiguration> {

	protected DecayHunter() {
		super(EHunterLabel.DECAY, EHunterMessageCode.HUNTER_DECAY__EXPLANATION);
	}

	@Override
	public boolean isAvailable() {
		return get() != null && !get().isOneHunterPerTarget();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		try {
			get().setDecay(getInt(args[0]));
			sendSynchro(sender, EHunterMessageCode.HUNTER_DECAY__VALUE_DEFINED, get().getDecay(), get().getName());
		} catch (IndexOutOfBoundsException e) {
			sendNotSynchro(sender, EHunterMessageCode.HUNTER_DECAY__VALUE_IS_MISSING);
			return false;
		} catch (NumberFormatException e) {
			sendNotSynchro(sender, ECommonMessageCode.COMMON_BAD_INTEGER_FORMAT);
			return false;
		} catch (DecayValueMustBeGreaterThanOne e) {
			sendNotSynchro(sender, EHunterMessageCode.HUNTER_DECAY__DECAY_VALUE_MUST_BE_GREATER_THAN_ONE);
			return false;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (args.length) {
		case 1:
			return asList(getMessage(sender, EHunterMessageCode.HUNTER_DECAY__ON_TAB_COMPLETE));
		default:
			return emptyList();
		}
	}
}
