package fr.pederobien.minecrafthunter.commands;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import fr.pederobien.minecraftgameplateform.impl.editions.AbstractLabelEdition;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class ItemOnPlayerKills extends AbstractLabelEdition<IHunterConfiguration> {

	protected ItemOnPlayerKills() {
		super(EHunterLabel.ITEM_ON_PLAYER_KILLS, EHunterMessageCode.ITEM_ON_PLAYER_KILLS_HUNTER__EXPLANATION);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			String materialKey = args[0];
			Material material = null;
			for (Material m : Material.values())
				if (m.getKey().getKey().equals(materialKey)) {
					material = m;
					break;
				}

			if (material == null) {
				sendNotSynchro(sender, EHunterMessageCode.ITEM_ON_PLAYER_KILLS_HUNTER__ITEM_NOT_FOUND, materialKey);
				return false;
			}

			get().setItemOnPlayerKills(new ItemStack(material));
			sendSynchro(sender, EHunterMessageCode.ITEM_ON_PLAYER_KILLS_HUNTER__ITEM_DEFINED, normalizeMaterial(get().getItemOnPlayerKills().getType()));
		} catch (IndexOutOfBoundsException e) {
			sendNotSynchro(sender, EHunterMessageCode.ITEM_ON_PLAYER_KILLS_HUNTER__ITEM_IS_MISSING);
			return false;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (args.length) {
		case 1:
			return filter(asList(Material.values()).stream().map(material -> material.getKey().getKey()), args);
		default:
			return emptyList();
		}
	}

	private String normalizeMaterial(Material material) {
		return material.name().toLowerCase().replace("_", " ");
	}
}
