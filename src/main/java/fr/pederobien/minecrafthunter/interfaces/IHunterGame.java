package fr.pederobien.minecrafthunter.interfaces;

import fr.pederobien.minecraftgameplateform.interfaces.element.IGame;
import fr.pederobien.minecrafthunter.interfaces.state.IStateGame;

public interface IHunterGame extends IGame, IStateGame {

	/**
	 * @return The configuration associated to this game.
	 */
	IHunterConfiguration getConfiguration();
}
