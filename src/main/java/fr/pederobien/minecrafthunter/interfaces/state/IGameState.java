package fr.pederobien.minecrafthunter.interfaces.state;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.minecraftgameplateform.interfaces.editions.IPlateformCodeSender;
import fr.pederobien.minecraftgameplateform.interfaces.element.IEventListener;
import fr.pederobien.minecraftgameplateform.interfaces.runtime.timeline.IObsTimeLine;

public interface IGameState extends IObsTimeLine, IPlateformCodeSender {

	/**
	 * @param sender  Source of the command
	 * @param command Command which was executed
	 * @param label   Alias of the command which was used
	 * @param args    Passed command arguments
	 * @return True if the game is successful initiated, false otherwise.
	 */
	boolean initiate(CommandSender sender, Command command, String label, String[] args);

	/**
	 * Method called to start the game.
	 */
	void start();

	/**
	 * Method called to pause the game.
	 */
	void pause();

	/**
	 * Method called to relaunch the game when it is in pause.
	 */
	void relaunch();

	/**
	 * Method called to stop a game.
	 */
	void stop();

	/**
	 * @return The listener that interact with minecraft events.
	 */
	IEventListener getListener();
}
