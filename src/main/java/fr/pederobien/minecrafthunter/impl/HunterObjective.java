package fr.pederobien.minecrafthunter.impl;

import java.time.LocalTime;
import java.util.function.Function;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;

import fr.pederobien.minecraftborder.entries.WorldBorderSizeCountDownEntry;
import fr.pederobien.minecraftborder.interfaces.IBorderConfiguration;
import fr.pederobien.minecraftgameplateform.entries.simple.CenterEntry;
import fr.pederobien.minecraftgameplateform.entries.simple.LocationSynchronizedEntry;
import fr.pederobien.minecraftgameplateform.entries.simple.TeamPlayerOnModeEntry;
import fr.pederobien.minecraftgameplateform.entries.updaters.TimeTaskObserverEntryUpdater;
import fr.pederobien.minecraftgameplateform.impl.element.GameObjective;
import fr.pederobien.minecraftgameplateform.interfaces.element.ITeam;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecrafthunter.entries.HunterEntry;
import fr.pederobien.minecrafthunter.entries.TargetEntry;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;
import fr.pederobien.minecrafthunter.interfaces.IHunterObjective;
import fr.pederobien.minecraftmanagers.WorldManager;
import fr.pederobien.minecraftscoreboards.impl.updaters.UpdatersFactory;
import fr.pederobien.minecraftscoreboards.interfaces.IEntry;

public class HunterObjective extends GameObjective<IHunterConfiguration> implements IHunterObjective {

	/**
	 * Create an empty objective based on the given parameters.
	 * 
	 * @param plugin        The plugin used to update this objective.
	 * @param player        The player associated to this objective. This player is used to display its informations.
	 * @param name          The name of this objective.
	 * @param displayName   The name displayed on the given player score board.
	 * @param criteria      The criteria tracked by this objective.
	 * @param displaySlot   The slot where this objective is displayed on player screen.
	 * @param configuration The configuration associated to this objective.
	 */
	public HunterObjective(Plugin plugin, Player player, String name, String displayName, String criteria, DisplaySlot displaySlot, IHunterConfiguration configuration) {
		super(plugin, player, name, displayName, criteria, displaySlot, configuration);
	}

	/**
	 * Create an empty objective based on the given parameters.
	 * 
	 * @param plugin        The plugin used to update this objective.
	 * @param player        The player associated to this objective. This player is used to display its informations.
	 * @param name          The name of this objective.
	 * @param displayName   The name displayed on the given player score board.
	 * @param displaySlot   The slot where this objective is displayed on player screen.
	 * @param configuration The configuration associated to this objective.
	 */
	public HunterObjective(Plugin plugin, Player player, String name, String displayName, DisplaySlot displaySlot, IHunterConfiguration configuration) {
		this(plugin, player, name, displayName, "dummy", displaySlot, configuration);
	}

	/**
	 * Create an empty objective based on the given parameters.
	 * 
	 * @param plugin        The plugin used to update this objective.
	 * @param player        The player associated to this objective. This player is used to display its informations.
	 * @param name          The name of this objective.
	 * @param displayName   The name displayed on the given player score board.
	 * @param displaySlot   The slot where this objective is displayed on player screen.
	 * @param configuration The configuration associated to this objective.
	 */
	public HunterObjective(Plugin plugin, Player player, String name, String displayName, IHunterConfiguration configuration) {
		this(plugin, player, name, displayName, DisplaySlot.SIDEBAR, configuration);
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
		emptyEntry(-entries().size());
		for (ITeam team : getConfiguration().getTeams())
			if (!team.getPlayers().isEmpty())
				add(score -> new TeamPlayerOnModeEntry(score, team, GameMode.SURVIVAL, true).addUpdater(UpdatersFactory.playerGameModeChange()));
	}

	@Override
	public void onCountDownTime(LocalTime currentTime) {

	}

	@Override
	public LocalTime getNextNotifiedTime() {
		return LocalTime.of(0, 0, 0);
	}

	@Override
	public void initialize() {
		initiate();
		super.initialize();
	}

	@Override
	public void initiate() {
		IBorderConfiguration borderConf = getConfiguration().getBorder(WorldManager.OVERWORLD).get();
		add(score -> new LocationSynchronizedEntry(score).addUpdater(UpdatersFactory.playerMove().condition(e -> e.getPlayer().equals(getPlayer()))));
		add(score -> new CenterEntry(score, borderConf.getBorderCenter()).addUpdater(UpdatersFactory.playerMove().condition(e -> e.getPlayer().equals(getPlayer()))));

		emptyEntry(-entries().size());

		add(score -> new TargetEntry(score, getConfiguration().getTargetDirectionRefreshPeriod())
				.addUpdater(UpdatersFactory.playerMove().condition(e -> e.getPlayer().equals(getPlayer()))));

		if (getConfiguration().isDistanceFromHunterDisplayed())
			add(score -> new HunterEntry(score, getConfiguration().getHunterDistanceRefreshPeriod())
					.addUpdater(UpdatersFactory.playerMove().condition(e -> e.getPlayer().equals(getPlayer()))));

		emptyEntry(-entries().size());

		for (IBorderConfiguration border : getConfiguration().getBorders())
			add(score -> new WorldBorderSizeCountDownEntry(score, border, "#").setDisplayHalfSize(true).addUpdater(new TimeTaskObserverEntryUpdater()));

		Plateform.getTimeLine().addObserver(borderConf.getStartTime(), this);
	}

	private void add(Function<Integer, IEntry> constructor) {
		addEntry(constructor.apply(-entries().size()));
	}
}
