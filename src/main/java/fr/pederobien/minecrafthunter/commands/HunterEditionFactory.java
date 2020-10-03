package fr.pederobien.minecrafthunter.commands;

import fr.pederobien.minecraftgameplateform.interfaces.editions.IMapPersistenceEdition;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class HunterEditionFactory {

	/**
	 * @return An edition to create a new hunger game configuration.
	 */
	public static IMapPersistenceEdition<IHunterConfiguration> newHunter() {
		return new NewHunter();
	}

	/**
	 * @return An edition to rename an hunger game configuration.
	 */
	public static IMapPersistenceEdition<IHunterConfiguration> renameHunter() {
		return new RenameHunter();
	}

	/**
	 * @return An edition to save an hunger game configuration.
	 */
	public static IMapPersistenceEdition<IHunterConfiguration> saveHunter() {
		return new SaveHunter();
	}

	/**
	 * @return An edition to display the name of all registered hunger game configurations.
	 */
	public static IMapPersistenceEdition<IHunterConfiguration> listHunter() {
		return new ListHunter();
	}

	/**
	 * @return An edition to delete the file of an hunger game configuration.
	 */
	public static IMapPersistenceEdition<IHunterConfiguration> deleteHunter() {
		return new DeleteHunter();
	}

	/**
	 * @return An edition to display the characteristics of the current hunger game configuration.
	 */
	public static IMapPersistenceEdition<IHunterConfiguration> detailsHunter() {
		return new DetailsHunter();
	}

	/**
	 * @return An edition to load an hunger game configuration.
	 */
	public static IMapPersistenceEdition<IHunterConfiguration> loadHunter() {
		return new LoadHunter();
	}

	/**
	 * @return An edition to set the time after which players respawn in spectator mode.
	 */
	public static IMapPersistenceEdition<IHunterConfiguration> playerDontReviveTime() {
		return new PlayerDontReviveTime();
	}

	/**
	 * @return An edition to set if the uhc mode is enabled or disabled.
	 */
	public static IMapPersistenceEdition<IHunterConfiguration> isUhc() {
		return new IsUhcHunter();
	}

	/**
	 * @return An edition to set the item to give to a player that killed another player.
	 */
	public static IMapPersistenceEdition<IHunterConfiguration> itemOnPlayerKills() {
		return new ItemOnPlayerKills();
	}

	/**
	 * @return An edition to set if a target can have several hunters.
	 */
	public static IMapPersistenceEdition<IHunterConfiguration> isOneHunterPerTarget() {
		return new IsOneHunterPerTarget();
	}

	/**
	 * @return An edition to set if the distance from the closer hunter should be displayed in to target's score board.
	 */
	public static IMapPersistenceEdition<IHunterConfiguration> isDistanceFromHunterDisplayed() {
		return new IsDistanceFromHunterDisplayed();
	}

	/**
	 * @return An edition to set if the target's name should be displayed in the hunter's score board.
	 */
	public static IMapPersistenceEdition<IHunterConfiguration> isTargetNameDisplayed() {
		return new IsTargetNameDisplayed();
	}
}
