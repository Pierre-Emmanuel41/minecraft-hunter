package fr.pederobien.minecrafthunter.impl;

import java.time.LocalTime;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftgameplateform.interfaces.element.IEventListener;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.HunterPlugin;
import fr.pederobien.minecrafthunter.impl.state.InGameState;
import fr.pederobien.minecrafthunter.impl.state.InitialState;
import fr.pederobien.minecrafthunter.impl.state.StartState;
import fr.pederobien.minecrafthunter.impl.state.StopState;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;
import fr.pederobien.minecrafthunter.interfaces.IHunterGame;
import fr.pederobien.minecrafthunter.interfaces.IHunterObjective;
import fr.pederobien.minecrafthunter.interfaces.state.IGameState;
import fr.pederobien.minecraftmanagers.EColor;
import fr.pederobien.minecraftmanagers.MessageManager.DisplayOption;
import fr.pederobien.minecraftmanagers.PlayerManager;
import fr.pederobien.minecraftmanagers.ScoreboardManager;

public class HunterGame implements IHunterGame {
	private IGameState initialState, startState, inGameState, stopState, current;
	private IHunterConfiguration configuration;

	public HunterGame(IHunterConfiguration configuration) {
		this.configuration = configuration;

		initialState = new InitialState(this);
		startState = new StartState(this);
		inGameState = new InGameState(this);
		stopState = new StopState(this);
		current = initialState;
	}

	@Override
	public boolean initiate(CommandSender sender, Command command, String label, String[] args) {
		return current.initiate(sender, command, label, args);
	}

	@Override
	public void start() {
		PlayerManager.getPlayers().forEach(player -> {
			IHunterObjective objective = new HunterObjective(HunterPlugin.get(), player, "Side bar", "Hunter Game", getConfiguration());
			objective.setScoreboard(ScoreboardManager.createScoreboard());
			Plateform.getObjectiveUpdater().register(objective);
		});
		current.start();
	}

	@Override
	public void stop() {
		current.stop();
	}

	@Override
	public void pause() {
		current.pause();
	}

	@Override
	public void relaunch() {
		current.relaunch();
	}

	@Override
	public IEventListener getListener() {
		return current.getListener();
	}

	@Override
	public void onPvpEnabled() {
		sendNotSynchro(EHunterMessageCode.HUNTER_PVP_ENABLED, DisplayOption.TITLE, EColor.DARK_RED);
	}

	@Override
	public boolean isRunning() {
		return current == inGameState;
	}

	@Override
	public IGameState getCurrentState() {
		return current;
	}

	@Override
	public IGameState setCurrentState(IGameState current) {
		this.current.getListener().setActivated(false);
		current.getListener().register(HunterPlugin.get());
		current.getListener().setActivated(true);
		return this.current = current;
	}

	@Override
	public IGameState getInitialState() {
		return initialState;
	}

	@Override
	public IGameState getStartState() {
		return startState;
	}

	@Override
	public IGameState getInGameState() {
		return inGameState;
	}

	@Override
	public IGameState getStopState() {
		return stopState;
	}

	@Override
	public int getCountDown() {
		return current.getCountDown();
	}

	@Override
	public int getCurrentCountDown() {
		return current.getCurrentCountDown();
	}

	@Override
	public void onTime(LocalTime time) {
		current.onTime(time);
	}

	@Override
	public void onCountDownTime(LocalTime currentTime) {
		current.onCountDownTime(currentTime);
	}

	@Override
	public LocalTime getNextNotifiedTime() {
		return current.getNextNotifiedTime();
	}

	@Override
	public IHunterConfiguration getConfiguration() {
		return configuration;
	}
}
