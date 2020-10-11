package fr.pederobien.minecrafthunter.impl.state;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.pederobien.minecraftgameplateform.impl.element.EventListener;
import fr.pederobien.minecraftgameplateform.interfaces.element.IEventListener;
import fr.pederobien.minecrafthunter.HunterPlugin;
import fr.pederobien.minecrafthunter.interfaces.IHunterGame;
import fr.pederobien.minecraftmanagers.MessageManager;
import fr.pederobien.minecraftmanagers.PlayerManager;

public class InGameState extends AbstractState {
	private IEventListener inGameListener, pauseGameListener;
	private boolean playerRevive;
	private boolean isRegistered;

	public InGameState(IHunterGame game) {
		super(game);
		inGameListener = new InGameListener();
		pauseGameListener = new PauseGameListener();
		playerRevive = true;
		isRegistered = false;
	}

	@Override
	public void pause() {
		if (!isRegistered) {
			pauseGameListener.register(HunterPlugin.get());
			isRegistered = true;
		}
		pauseGameListener.setActivated(true);
	}

	@Override
	public void relaunch() {
		pauseGameListener.setActivated(false);
	}

	@Override
	public void stop() {
		if (pauseGameListener.isActivated())
			pauseGameListener.setActivated(false);
		getGame().setCurrentState(getGame().getStopState()).stop();
	}

	@Override
	public IEventListener getListener() {
		return inGameListener;
	}

	@Override
	protected void onPlayerDontRevive() {
		playerRevive = false;
	}

	private class InGameListener extends EventListener {

		@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
		public void onPlayerDie(PlayerDeathEvent event) {
			if (!isActivated())
				return;

			if (!playerRevive || event.getEntity().getKiller() instanceof Player) {
				event.setKeepInventory(false);
				PlayerManager.setGameModeOfPlayer(event.getEntity(), GameMode.SPECTATOR);
				Player killer = event.getEntity().getKiller();
				if (killer.getInventory().firstEmpty() == -1)
					killer.getWorld().dropItem(killer.getLocation(), getConfiguration().getItemOnPlayerKills());
				else
					killer.getInventory().addItem(getConfiguration().getItemOnPlayerKills());
			} else {
				event.setKeepInventory(true);
				PlayerManager.setGameModeOfPlayer(event.getEntity(), GameMode.SURVIVAL);
			}
		}
	}

	private class PauseGameListener extends EventListener {
		private List<Player> players;

		@Override
		public void setActivated(boolean isActivated) {
			super.setActivated(isActivated);
			if (isActivated()) {
				players = PlayerManager.getPlayersOnMode(GameMode.SURVIVAL).collect(Collectors.toList());
				PlayerManager.setGameModeOfPlayersOnMode(GameMode.SURVIVAL, GameMode.SPECTATOR);
				getConfiguration().getBorders().forEach(border -> border.pause());
			} else {
				players.forEach(player -> PlayerManager.setGameModeOfPlayer(player, GameMode.SURVIVAL));
				getConfiguration().getBorders().forEach(border -> border.relaunched());
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
		public void onPlayerMoveEvent(PlayerMoveEvent event) {
			if (!isActivated())
				return;

			event.setCancelled(true);
			MessageManager.sendMessage(event.getPlayer(), ChatColor.RED + "Game paused, you can't move");
		}
	}
}
