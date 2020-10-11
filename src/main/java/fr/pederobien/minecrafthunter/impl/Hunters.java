package fr.pederobien.minecrafthunter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

//import fr.pederobien.minecrafthunter.HunterPlugin;
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
	public IHunter getAsHunter(Player player) {
		return hunters.get(player);
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
			IHunter hunter = Hunter.of(h);
			targetList.removeIf(player -> player.equals(h));

			Player t = TeamManager.getRandom(targetList).get();
			IHunter target = Hunter.of(t);
			hunter.setTarget(target);

			targetList.clear();
			// Updating map and targetList
			for (Entry<Player, Integer> entry : map.entrySet()) {
				int occurence = entry.getValue();
				if (!entry.getKey().equals(t)) {
					occurence = entry.getValue() + 1; // Multiply here by decay instead of increasing by 1
					map.put(entry.getKey(), occurence);
				}

				for (int i = 0; i < occurence; i++)
					targetList.add(entry.getKey());
			}

			hunters.put(h, hunter);
		}

		// Départ : tous les joueurs sont mentionnés une seule fois (J1, J2, J3, J4)
		//
		// Paramètre decay (ex decay = 3) modifie la probabilité des autres joueurs pour être sélectionné en tant que cible.
		//
		// Tirage aléatoire du chasseur entre J1, J2, J3 et J4 -> J2 = hunter
		// Tirage aléatoire de la cible entre J1, J3 et J4 -> (1xJ1, 1xJ3, 1xJ4)
		// J3 choisi
		// Mise à jour de la liste : (3xJ1, 3xJ2, 1xJ3, 3xJ4)
		//
		//
		// Tirage aléatoire du chasseur entre J1, J3 et J4 -> J4 = hunter
		// Tirage aléatoire de la cible entre J1, J2 et J3 -> (3xJ1, 3xJ2, 1xJ3)
		// J1 choisi
		// Mise à jour de la liste : (3xJ1, 9xJ2, 3xJ3, 9xJ4)
		//
		// Tirage aléatoire du chasseur entre J1, J3 -> J3 = hunter
		// Tirage aléatoire de la cible entre J1, J2, J4 -> (3xJ1, 9xJ2, 9xJ4)
		// J1 choisi
		// Mise à jour de la liste : (3xJ1, 36xJ2, 9xJ3, 36xJ4)
		//
		// Tirage aléatoire du chasseur entre J1 -> J1 = hunter
		// Tirage aléatoire de la cible entre J2, J3, J4 -> (36xJ2, 9xJ3, 36xJ4)
		// J4 choisi
		// Mise à jour de la liste : (9xJ1, 108xJ2, 27xJ3, 36xJ4)
		//
		// Result : J2 -> J3, J4 -> J1, J3 -> J1, J1 -> J4
		//
		// modélisation : hunters : List<Player> targets : Map<Player, Integer> : Player = cible, Integer = nb occurence
	}

	private void associateHunterAndTarget(List<Player> players, int hunterIndex, int targetIndex) {
		IHunter hunter = Hunter.of(players.get(hunterIndex));
		IHunter target = Hunter.of(players.get(targetIndex));
		hunter.setTarget(target);
		hunters.put(hunter.getPlayer(), hunter);
	}
}
