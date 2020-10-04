package fr.pederobien.minecrafthunter.entries;

import java.util.Optional;

import org.bukkit.entity.Player;

import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftgameplateform.entries.simple.OrientationEntry;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.HunterPlugin;
import fr.pederobien.minecrafthunter.impl.Hunters;
import fr.pederobien.minecrafthunter.interfaces.IHunter;
import fr.pederobien.minecrafthunter.interfaces.IObsHunter;

public class TargetEntry extends OrientationEntry implements IObsHunter {
	private IHunter hunter;
	private Optional<IHunter> optTarget;

	/**
	 * Create an entry that displays the orientation to follow to reach the target associated to the player's objective.
	 * 
	 * @param score The line number of this entry.
	 */
	public TargetEntry(int score) {
		super(score);
		hunter = Hunters.getInstance().getAsHunter(getPlayer());
		optTarget = hunter.getTarget();
		if (optTarget.isPresent())
			setBlock(optTarget.get().getPlayer().getLocation().getBlock());
		hunter.addObserver(this);
	}

	@Override
	protected IMinecraftMessageCode getBeforeAsCode(Player player) {
		return EHunterMessageCode.HUNTER__TARGET;
	}

	@Override
	public String getAfter() {
		return HunterPlugin.getCurrentHunter().isTargetNameDisplayed() ? optTarget.isPresent() ? "(" + optTarget.get().getPlayer().getName() + ")" : null : null;
	}

	@Override
	public void onTargetChanged(IHunter hunter, IHunter oldTarget, IHunter newTarget) {
		if (newTarget == null)
			getObjective().removeEntry(getScore());
		else {
			if (!isActivated())
				getObjective().addEntry(this);
			setBlock(newTarget.getPlayer().getLocation().getBlock());
		}
	}

	@Override
	public void onHunterAdded(IHunter hunter, IHunter addedHunter) {

	}

	@Override
	public void onHunterRemoved(IHunter hunter, IHunter removedHunter) {

	}
}
