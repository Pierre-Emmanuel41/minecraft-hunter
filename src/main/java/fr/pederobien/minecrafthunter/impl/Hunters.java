package fr.pederobien.minecrafthunter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import fr.pederobien.minecrafthunter.HunterPlugin;
import fr.pederobien.minecrafthunter.interfaces.IHunter;
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
	 * Dispatch all players currently logged into the server as hunter/target.
	 */
	public void dispatchHunters() {
		if (HunterPlugin.getCurrentHunter().isOneHunterPerTarget())
			dispatchWithOneHunter();
		else
			dispatchWithSeveralHunters();
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

	private void dispatchWithSeveralHunters() {

	}

	private void associateHunterAndTarget(List<Player> players, int hunterIndex, int targetIndex) {
		IHunter hunter = Hunter.of(players.get(hunterIndex));
		IHunter target = Hunter.of(players.get(targetIndex));
		hunter.setTarget(target);
		hunters.put(hunter.getPlayer(), hunter);
	}
}
