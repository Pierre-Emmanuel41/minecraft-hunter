package fr.pederobien.minecrafthunter.entries;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftgameplateform.entries.PlateformEntry;
import fr.pederobien.minecraftgameplateform.interfaces.runtime.timeline.IObsTimeLine;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.impl.Hunters;
import fr.pederobien.minecrafthunter.interfaces.IHunter;
import fr.pederobien.minecrafthunter.interfaces.IObsHunter;
import fr.pederobien.minecraftmanagers.WorldManager;

public class HunterEntry extends PlateformEntry implements IObsHunter, IObsTimeLine {
	private IHunter target;
	private LocalTime period, currentTime;
	private Integer distance;

	/**
	 * Constructs an entry that displays the distance to the closed hunter for this target.
	 * 
	 * @param score  The line number of this entry.
	 * @param period The time between two check of the closest hunter location.
	 */
	public HunterEntry(int score, LocalTime period) {
		super(score);
		this.period = period;

		currentTime = LocalTime.of(0, 0, 0);
	}

	@Override
	public void initialize() {
		super.initialize();
		target = Hunters.getInstance().getAsHunter(getPlayer()).get();
		target.addObserver(this);
		distance = getHunterDistance();
		Plateform.getTimeLine().addObserver(currentTime, this);
	}

	@Override
	protected String updateCurrentValue(Player player) {
		return distance == null ? "?" : distance.toString();
	}

	@Override
	protected IMinecraftMessageCode getBeforeAsCode(Player player) {
		return EHunterMessageCode.HUNTER__HUNTER;
	}

	@Override
	public void onTargetChanged(IHunter hunter, IHunter oldTarget, IHunter newTarget) {
		if (newTarget != null)
			return;

		getObjective().removeEntry(getScore());
		Plateform.getTimeLine().removeObserver(currentTime, this);
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
		distance = getHunterDistance();
		this.currentTime = currentTime.plusSeconds(period.toSecondOfDay());
	}

	@Override
	public void onCountDownTime(LocalTime currentTime) {

	}

	@Override
	public LocalTime getNextNotifiedTime() {
		return currentTime;
	}

	private Integer getHunterDistance() {
		if (target.getHunters().isEmpty())
			return null;

		int min = Integer.MAX_VALUE;
		List<IHunter> hunters = new ArrayList<IHunter>(target.getHunters());
		for (IHunter hunter : hunters)
			min = Math.min(min, (int) WorldManager.getDistance2D(target.getPlayer().getLocation(), hunter.getPlayer().getLocation()));
		return min;
	}
}
