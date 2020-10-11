package fr.pederobien.minecrafthunter.interfaces;

import java.util.List;
import java.util.Optional;

import org.bukkit.entity.Player;

import fr.pederobien.minecraftgameplateform.interfaces.element.IEventListener;
import fr.pederobien.minecraftgameplateform.interfaces.observer.IObsPlayerQuitOrJoinEventListener;
import fr.pederobien.minecrafthunter.exceptions.TargetAndHunterAreEqualsException;

public interface IHunter extends IObsPlayerQuitOrJoinEventListener, IEventListener {

	/**
	 * @return The player source for this hunter.
	 */
	Player getPlayer();

	/**
	 * Set the player source for this hunter.
	 * 
	 * @param player The player source for this hunter.
	 */
	void setPlayer(Player player);

	/**
	 * Adds the given hunter to the list of hunters of this hunter.
	 * 
	 * @param hunter The hunter to add.
	 * 
	 * @return This hunter.
	 */
	IHunter addHunter(IHunter hunter);

	/**
	 * Removes the given hunter from the list of hunters of this hunter.
	 * 
	 * @param hunter The hunter to remove.
	 * 
	 * @return This hunter.
	 */
	IHunter removeHunter(IHunter hunter);

	/**
	 * Set the target of this hunter.
	 * 
	 * @param target The target that is hunted by this hunter.
	 * 
	 * @return This hunter.
	 * 
	 * @throws TargetAndHunterAreEqualsException If the given target equals this hunter.
	 */
	IHunter setTarget(IHunter target);

	/**
	 * @return An optional that contains the target of this hunter if it exists, an empty optional otherwise.
	 */
	Optional<IHunter> getTarget();

	/**
	 * @return The list of hunters that hunts this hunter. So this hunter is also a target.
	 */
	List<IHunter> getHunters();

	/**
	 * Appends an observer for this hunter.
	 * 
	 * @param obs The observer to add.
	 */
	void addObserver(IObsHunter obs);

	/**
	 * Removes the given observer from the list of observer for this hunter.
	 * 
	 * @param obs The observer to remove.
	 */
	void removeObserver(IObsHunter obs);
}
