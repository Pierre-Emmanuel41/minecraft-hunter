package fr.pederobien.minecrafthunter.interfaces.state;

import fr.pederobien.minecraftgameplateform.interfaces.runtime.timeline.IObsTimeLine;

public interface IStateGame extends IObsTimeLine {

	IGameState getCurrentState();

	IGameState setCurrentState(IGameState current);

	IGameState getInitialState();

	IGameState getStartState();

	IGameState getInGameState();

	IGameState getStopState();
}
