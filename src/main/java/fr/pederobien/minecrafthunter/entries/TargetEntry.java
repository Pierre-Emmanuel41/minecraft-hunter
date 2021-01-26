package fr.pederobien.minecrafthunter.entries;

import java.time.LocalTime;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;
import fr.pederobien.minecraftgameplateform.entries.simple.OrientationEntry;
import fr.pederobien.minecraftgameplateform.interfaces.runtime.timeline.IObsTimeLine;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.HunterPlugin;
import fr.pederobien.minecrafthunter.impl.Hunters;
import fr.pederobien.minecrafthunter.interfaces.IHunter;
import fr.pederobien.minecrafthunter.interfaces.IObsHunter;
import fr.pederobien.minecraftmanagers.EColor;
import fr.pederobien.minecraftmanagers.WorldManager;

public class TargetEntry extends OrientationEntry implements IObsHunter, IObsTimeLine, Listener {
	private IHunter hunter;
	private Optional<IHunter> optTarget;
	private LocalTime period, currentTime;
	private boolean isInDifferentWorld;

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
		getObjective().getPlugin().getServer().getPluginManager().registerEvents(this, getObjective().getPlugin());
		hunter = Hunters.getInstance().getAsHunter(getPlayer()).get();
		optTarget = hunter.getTarget();

		// Target cannot be null at the beginning
		setBlock(getTargetBlock());
		hunter.addObserver(this);

		Plateform.getTimeLine().addObserver(currentTime, this);
		isInDifferentWorld = false;
	}

	@Override
	protected String updateCurrentValue(Player player) {
		return isInDifferentWorld && getLocation(optTarget.get()).getWorld().equals(WorldManager.NETHER_WORLD) ? EColor.RED.getInColor(super.updateCurrentValue(player))
				: EColor.RESET.getInColor(super.updateCurrentValue(player));
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
		if (newTarget != null) {
			optTarget = Optional.of(newTarget);
			setBlock(getTargetBlock());
		} else {
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
		setBlock(getTargetBlock());
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

		// Send message to player
		// To remove ?
		if (event.getTo().getWorld().equals(WorldManager.END_WORLD))
			event.setCancelled(true);

		if (event.getPlayer().equals(hunter.getPlayer()) || event.getPlayer().equals(optTarget.get().getPlayer())) {
			isInDifferentWorld = getLocation(hunter).getWorld().equals(getLocation(optTarget.get()).getWorld());
			getObjective().update(this);
		}
	}

	private Block getTargetBlock() {
		if (!isInDifferentWorld || hunter.getPlayer().getLocation().getWorld().equals(WorldManager.NETHER_WORLD))
			return optTarget.get().getPlayer().getLocation().getBlock();

		return toOverworldBlock(optTarget.get().getPlayer().getLocation().getBlock());
	}

	private Block toOverworldBlock(Block origin) {
		return origin.getRelative(origin.getX() * 8, origin.getY(), origin.getZ() * 8);
	}

	private Location getLocation(IHunter hunter) {
		return hunter.getPlayer().getLocation();
	}
}
