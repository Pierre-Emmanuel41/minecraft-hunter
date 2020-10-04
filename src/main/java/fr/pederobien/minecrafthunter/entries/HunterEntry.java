package fr.pederobien.minecrafthunter.entries;

import org.bukkit.entity.Player;

import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftgameplateform.entries.PlateformEntry;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.impl.Hunters;
import fr.pederobien.minecrafthunter.interfaces.IHunter;
import fr.pederobien.minecraftmanagers.WorldManager;

public class HunterEntry extends PlateformEntry {
	private IHunter target;

	/**
	 * Constructs an entry that displays the distance to the closed hunter for this target.
	 * 
	 * @param score The line number of this entry.
	 */
	public HunterEntry(int score) {
		super(score);
		target = Hunters.getInstance().getAsHunter(getPlayer());
	}

	@Override
	protected String updateCurrentValue(Player player) {
		return getHunterDistance().toString();
	}

	@Override
	protected IMinecraftMessageCode getBeforeAsCode(Player player) {
		return EHunterMessageCode.HUNTER__HUNTER;
	}

	private Integer getHunterDistance() {
		int min = Integer.MAX_VALUE;
		for (IHunter hunter : target.getHunters())
			min = Math.min(min, (int) WorldManager.getSquaredDistance2D(target.getPlayer().getLocation(), hunter.getPlayer().getLocation()));
		return min;
	}
}
