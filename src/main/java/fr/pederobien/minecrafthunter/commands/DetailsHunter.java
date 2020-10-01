package fr.pederobien.minecrafthunter.commands;

import fr.pederobien.minecraftgameplateform.commands.common.CommonDetails;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class DetailsHunter extends CommonDetails<IHunterConfiguration> {

	protected DetailsHunter() {
		super(EHunterMessageCode.DETAILS_HUNTER__EXPLANATION, EHunterMessageCode.DETAILS_HUNTER__ON_DETAILS);
	}
}
