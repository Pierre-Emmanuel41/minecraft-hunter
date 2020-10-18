package fr.pederobien.minecrafthunter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import fr.pederobien.minecrafthunter.interfaces.IHunter;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;
import fr.pederobien.minecraftmanagers.PlayerManager;
import fr.pederobien.minecraftmanagers.TeamManager;

public class Hunters {
	private Map<Player, IHunter> hunters;

	private Hunters() {
		hunters = new HashMap<Player, IHunter>();
	}

	public static Hunters getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		private static final Hunters INSTANCE = new Hunters();
	}

	/**
	 * Dispatch all players currently logged into the server as hunters/targets according to the parameter of the given configuration.
	 * 
	 * @param configuration the hunter configuration used to specify how to dispatch players and targets.
	 */
	public void dispatchHunters(IHunterConfiguration configuration) {
		if (configuration.isOneHunterPerTarget())
			dispatchWithOneHunter();
		else
			dispatchWithSeveralHunters(configuration);
	}

	/**
	 * Get the hunter equivalent of the give player.
	 * 
	 * @param player The player that is returned as IHunter.
	 * 
	 * @return The given player as hunter.
	 * 
	 * @see IHunter
	 */
	public Optional<IHunter> getAsHunter(Player player) {
		return Optional.ofNullable(hunters.get(player));
	}

	/**
	 * @return A stream that contains all registered hunters.
	 */
	public Stream<IHunter> getHunters() {
		return hunters.values().stream();
	}

	/**
	 * @return A stream that contains hunter in survival mode.
	 */
	public Stream<IHunter> getNotDeadHunters() {
		return getHunters().filter(hunter -> hunter.getPlayer().getGameMode().equals(GameMode.SURVIVAL));
	}

	/**
	 * Unregister each IHunter for the PlayerDeathEvent and clear the map that contains all registered hunters.
	 */
	public void reset() {
		hunters.values().forEach(hunter -> hunter.setActivated(false));
		hunters.clear();
	}

	private void dispatchWithOneHunter() {
		List<Player> players = new ArrayList<Player>(PlayerManager.getPlayers().collect(Collectors.toList()));
		for (int i = 0; i < 10; i++)
			players = TeamManager.mix(players);

		for (int i = 0; i < players.size() - 1; i++)
			associateHunterAndTarget(players, i, i + 1);

		associateHunterAndTarget(players, players.size() - 1, 0);
	}

	private void dispatchWithSeveralHunters(IHunterConfiguration configuration) {
		List<Player> players = new ArrayList<Player>(PlayerManager.getPlayers().collect(Collectors.toList()));
		Map<Player, Integer> map = new HashMap<Player, Integer>();
		List<Player> targetList = new ArrayList<Player>(players);

		// Initializing map
		for (Player player : players)
			map.put(player, 1);

		// Dispatching hunters and target
		while (!players.isEmpty()) {
			Player h = TeamManager.getRandom(players).get();
			players.remove(h);
			IHunter hunter = getOrCreateHunter(h);
			targetList.removeIf(player -> player.equals(h));

			Player t = TeamManager.getRandom(targetList).get();
			IHunter target = getOrCreateHunter(t);
			hunter.setTarget(target);

			targetList.clear();
			// Updating map and targetList
			for (Entry<Player, Integer> entry : map.entrySet()) {
				int occurence = entry.getValue();
				if (!entry.getKey().equals(t)) {
					occurence = entry.getValue() * 2; // Multiply here by decay instead of increasing by 1
					map.put(entry.getKey(), occurence);
				}

				for (int i = 0; i < occurence; i++)
					targetList.add(entry.getKey());
			}

			hunters.put(h, hunter);
		}
	}

	private void associateHunterAndTarget(List<Player> players, int hunterIndex, int targetIndex) {
		IHunter hunter = getOrCreateHunter(players.get(hunterIndex));
		IHunter target = getOrCreateHunter(players.get(targetIndex));
		hunter.setTarget(target);
	}

	private IHunter getOrCreateHunter(Player player) {
		Optional<IHunter> optHunter = getAsHunter(player);
		if (optHunter.isPresent())
			return optHunter.get();

		IHunter hunter = optHunter.isPresent() ? optHunter.get() : Hunter.of(player);
		hunters.put(hunter.getPlayer(), hunter);
		return hunter;
	}
}
