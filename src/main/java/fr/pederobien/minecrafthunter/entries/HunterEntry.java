package fr.pederobien.minecrafthunter.entries;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftgameplateform.entries.PlateformEntry;
import fr.pederobien.minecraftgameplateform.interfaces.runtime.timeline.IObsTimeLine;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.impl.Hunters;
import fr.pederobien.minecrafthunter.interfaces.IHunter;
import fr.pederobien.minecrafthunter.interfaces.IObsHunter;
import fr.pederobien.minecraftmanagers.EColor;
import fr.pederobien.minecraftmanagers.WorldManager;

public class HunterEntry extends PlateformEntry implements IObsHunter, IObsTimeLine, Listener {
	private IHunter target;
	private LocalTime period, currentTime;
	private Integer distance;
	private boolean isInDifferentWorld;
	private IHunter closestHunter;

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
		getObjective().getPlugin().getServer().getPluginManager().registerEvents(this, getObjective().getPlugin());
		target = Hunters.getInstance().getAsHunter(getPlayer()).get();
		target.addObserver(this);
		getClosestHunter();
		distance = getDistance();
		Plateform.getTimeLine().addObserver(currentTime, this);
	}

	@Override
	protected String updateCurrentValue(Player player) {
		String showedDistance = distance == null ? "?" : distance.toString();
		return isInDifferentWorld && getLocation(closestHunter).getWorld().equals(WorldManager.NETHER_WORLD) ? EColor.RED.getInColor(showedDistance)
				: EColor.RESET.getInColor(showedDistance);
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
		getClosestHunter();
		distance = getDistance();
		this.currentTime = currentTime.plusSeconds(period.toSecondOfDay());
	}

	@Override
	public void onCountDownTime(LocalTime currentTime) {

	}

	@Override
	public LocalTime getNextNotifiedTime() {
		return currentTime;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerPortalEvent(PlayerPortalEvent event) {
		if (!isActivated())
			return;

		getClosestHunter();

		if (event.getPlayer().equals(target.getPlayer()) || event.getPlayer().equals(closestHunter.getPlayer())) {
			isInDifferentWorld = getLocation(target).getWorld().equals(getLocation(closestHunter).getWorld());
			getObjective().update(this);
		}
	}

	private void getClosestHunter() {
		if (target.getHunters().isEmpty()) {
			closestHunter = null;
			return;
		}

		int min = Integer.MAX_VALUE;
		List<IHunter> hunters = new ArrayList<IHunter>(target.getHunters());
		Location overworldTargetBlock = toOverworldBlock(target, getLocation(target).getBlock());
		for (IHunter hunter : hunters) {
			int oldMin = min;
			min = Math.min(min, (int) WorldManager.getDistance2D(overworldTargetBlock, toOverworldBlock(hunter, getLocation(hunter).getBlock())));
			if (oldMin != min)
				closestHunter = hunter;
		}
	}

	private Location toOverworldBlock(IHunter hunter, Block origin) {
		if (getLocation(hunter).getWorld().equals(WorldManager.OVERWORLD))
			return origin.getLocation();
		return new Location(WorldManager.OVERWORLD, origin.getX() * 8, origin.getY(), origin.getZ() * 8);
	}

	private Integer getDistance() {
		if (closestHunter == null)
			return null;

		if (!isInDifferentWorld)
			return (int) WorldManager.getDistance2D(getLocation(target), getLocation(closestHunter));

		if (target.getPlayer().getLocation().getWorld().equals(WorldManager.OVERWORLD))
			return (int) WorldManager.getDistance2D(getLocation(target), toOverworldBlock(closestHunter, getLocation(closestHunter).getBlock()));

		return (int) WorldManager.getDistance2D(toOverworldBlock(target, getLocation(target).getBlock()), getLocation(closestHunter));
	}

	private Location getLocation(IHunter hunter) {
		return hunter.getPlayer().getLocation();
	}
}
