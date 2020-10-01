package fr.pederobien.minecrafthunter.impl.state;

import java.time.LocalTime;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftgameplateform.exceptions.StateException;
import fr.pederobien.minecraftgameplateform.impl.element.EventListener;
import fr.pederobien.minecraftgameplateform.interfaces.element.IEventListener;
import fr.pederobien.minecraftgameplateform.interfaces.helpers.IGameConfigurationHelper;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;
import fr.pederobien.minecrafthunter.interfaces.IHunterGame;
import fr.pederobien.minecrafthunter.interfaces.state.IGameState;
import fr.pederobien.minecraftmanagers.EColor;
import fr.pederobien.minecraftmanagers.MessageManager.DisplayOption;

public abstract class AbstractState implements IGameState {
	private IHunterGame game;
	private IGameConfigurationHelper helper;
	private int currentCountDown;

	protected AbstractState(IHunterGame game) {
		this.game = game;
		helper = Plateform.getOrCreateConfigurationHelper(getConfiguration());
		currentCountDown = getCountDown();
	}

	@Override
	public int getCountDown() {
		return 5;
	}

	@Override
	public int getCurrentCountDown() {
		return currentCountDown;
	}

	@Override
	public void onTime(LocalTime time) {
		sendNotSynchro(EHunterMessageCode.HUNTER_NO_RESURRECTION, DisplayOption.TITLE, EColor.DARK_RED);
		onPlayerDontRevive();
		currentCountDown = getCountDown();
	}

	@Override
	public void onCountDownTime(LocalTime currentTime) {
		sendNotSynchro(EHunterMessageCode.HUNTER_NO_RESURRECTION_COUNT_DOWN, DisplayOption.TITLE, EColor.GOLD, currentCountDown);
		currentCountDown--;
	}

	@Override
	public LocalTime getNextNotifiedTime() {
		return LocalTime.of(0, 0, 0);
	}

	@Override
	public boolean initiate(CommandSender sender, Command command, String label, String[] args) {
		throw new StateException(this);
	}

	@Override
	public void start() {
		throw new StateException(this);
	}

	@Override
	public void pause() {
		throw new StateException(this);
	}

	@Override
	public void relaunch() {
		throw new StateException(this);
	}

	@Override
	public void stop() {
		throw new StateException(this);
	}

	@Override
	public IEventListener getListener() {
		return new EventListener();
	}

	/**
	 * @return The game managed by this state.
	 */
	protected IHunterGame getGame() {
		return game;
	}

	/**
	 * @return The configuration associated to this game.
	 */
	protected IHunterConfiguration getConfiguration() {
		return getGame().getConfiguration();
	}

	/**
	 * @return A game configuration helper associated to the configuration returned by {@link #getConfiguration()}. The helper is
	 *         defined in the constructor of this state. This means that if the configuration changed, the helper could have bad
	 *         result.
	 */
	protected IGameConfigurationHelper getConfigurationHelper() {
		return helper;
	}

	/**
	 * Method called when the time returned by {@link IHunterConfiguration#getPlayerDontReviveTime()} is exceeded. Do nothing if not
	 * overrided.
	 */
	protected void onPlayerDontRevive() {
	}
}
