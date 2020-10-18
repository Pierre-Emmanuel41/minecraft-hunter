package fr.pederobien.minecrafthunter.entries;

import java.time.LocalTime;
import java.util.Optional;

import org.bukkit.entity.Player;

import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftgameplateform.entries.simple.OrientationEntry;
import fr.pederobien.minecraftgameplateform.interfaces.runtime.timeline.IObsTimeLine;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.HunterPlugin;
import fr.pederobien.minecrafthunter.impl.Hunters;
import fr.pederobien.minecrafthunter.interfaces.IHunter;
import fr.pederobien.minecrafthunter.interfaces.IObsHunter;

public class TargetEntry extends OrientationEntry implements IObsHunter, IObsTimeLine {
	private IHunter hunter;
	private Optional<IHunter> optTarget;
	private LocalTime period, currentTime;

	/**
	 * Create an entry that displays the orientation to follow to reach the target associated to the player's objective.
	 * 
	 * @param score  The line number of this entry.
	 * @param period The time between two check of the target location.
	 */
	public TargetEntry(int score, LocalTime period) {
		super(score);
		this.period = period;

		currentTime = LocalTime.of(0, 0, 0);
	}

	@Override
	public void initialize() {
		super.initialize();
		hunter = Hunters.getInstance().getAsHunter(getPlayer()).get();
		optTarget = hunter.getTarget();

		// Target cannot be null at the beginning
		setBlock(optTarget.get().getPlayer().getLocation().getBlock());
		hunter.addObserver(this);

		Plateform.getTimeLine().addObserver(currentTime, this);
	}

	@Override
	protected IMinecraftMessageCode getBeforeAsCode(Player player) {
		return EHunterMessageCode.HUNTER__TARGET;
	}

	@Override
	public String getAfter() {
		return HunterPlugin.getCurrentHunter().isTargetNameDisplayed() && optTarget.isPresent() ? " " + optTarget.get().getPlayer().getName() : "";
	}

	@Override
	public void onTargetChanged(IHunter hunter, IHunter oldTarget, IHunter newTarget) {
		if (newTarget != null)
			setBlock(newTarget.getPlayer().getLocation().getBlock());
		// Player is dead
		else {
			getObjective().removeEntry(getScore() - 1);
			getObjective().removeEntry(getScore());
			Plateform.getTimeLine().removeObserver(currentTime, this);
		}
	}

	@Override
	public void onHunterAdded(IHunter hunter, IHunter addedHunter) {

	}

	@Override
	public void onHunterRemoved(IHunter hunter, IHunter removedHunter) {

	}

	@Override
	public int getCountDown() {
		return 0;
	}

	@Override
	public int getCurrentCountDown() {
		return 0;
	}

	@Override
	public void onTime(LocalTime currentTime) {
		setBlock(optTarget.get().getPlayer().getLocation().getBlock());
		this.currentTime = currentTime.plusSeconds(period.toSecondOfDay());
	}

	@Override
	public void onCountDownTime(LocalTime currentTime) {

	}

	@Override
	public LocalTime getNextNotifiedTime() {
		return currentTime;
	}
}
