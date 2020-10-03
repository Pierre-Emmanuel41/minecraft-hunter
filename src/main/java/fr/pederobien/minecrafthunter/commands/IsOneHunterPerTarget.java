package fr.pederobien.minecrafthunter.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftdevelopmenttoolkit.exceptions.BooleanParseException;
import fr.pederobien.minecraftgameplateform.dictionary.ECommonMessageCode;
import fr.pederobien.minecraftgameplateform.impl.editions.AbstractLabelEdition;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class IsOneHunterPerTarget extends AbstractLabelEdition<IHunterConfiguration> {

	protected IsOneHunterPerTarget() {
		super(EHunterLabel.IS_ONE_HUNTER_PER_TARGET, EHunterMessageCode.IS_ONE_HUNTER_PER_TARGET__EXPLANATION);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			get().setIsOneHunterPerTarget(getBoolean(args[0]));
			sendSynchro(sender, EHunterMessageCode.IS_ONE_HUNTER_PER_TARGET__VALUE_DEFINED, get().getName(), get().isOneHunterPerTarget());
		} catch (IndexOutOfBoundsException e) {
			sendNotSynchro(sender, EHunterMessageCode.IS_ONE_HUNTER_PER_TARGET__VALUE_IS_MISSING);
			return false;
		} catch (BooleanParseException e) {
			sendNotSynchro(sender, ECommonMessageCode.COMMON_BAD_BOOLEAN_FORMAT);
			return false;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (args.length) {
		case 1:
			return filter(asList("true", "false").stream(), args);
		default:
			return emptyList();
		}
	}
}
