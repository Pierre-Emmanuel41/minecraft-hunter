package fr.pederobien.minecrafthunter.interfaces;

import java.time.LocalTime;

import org.bukkit.inventory.ItemStack;

import fr.pederobien.minecraftborder.interfaces.IGameBorderConfiguration;

public interface IHunterConfiguration extends IGameBorderConfiguration {

	/**
	 * While the game time is less than this time, if a player dies, it will respawn in game mode survival and keep its inventory.
	 * Once this time is exceed, a player will respawn in spectator mode and drop its inventory if it dies.
	 * 
	 * @return The time after which a player respawn in spectator mode.
	 */
	LocalTime getPlayerDontReviveTime();

	/**
	 * While the game time is less than this time, if a player dies, it will respawn in game mode survival and keep its inventory.
	 * Once this time is exceed, a player will respawn in spectator mode and drop its inventory if it dies.
	 * 
	 * @param playerDontReviveTime The time after which a player respawn in spectator mode.
	 */
	void setPlayerDontReviveTime(LocalTime playerDontReviveTime);

	/**
	 * Get the uhc mode of this configuration. If true, the game rule natural regeneration is set to false and the
	 * playerDontReviveTime is set to 0h 0m 0s.
	 * 
	 * @return True if the uhc mode is activated, false otherwise.
	 */
	Boolean isUhc();

	/**
	 * Set the uhc mode of this configuration. If true, the game rule natural regeneration is set to false and the
	 * playerDontReviveTime is set to 0h 0m 0s.
	 * 
	 * @param isUhc True if the uhc mode is activated, false otherwise.
	 */
	void setIsUhc(boolean isUhc);

	/**
	 * @return The item stack given to a player when it kills another player.
	 */
	ItemStack getItemOnPlayerKills();

	/**
	 * Set the item to give to a player that killed another player.
	 * 
	 * @param itemOnPlayerKills The item to give.
	 */
	void setItemOnPlayerKills(ItemStack itemOnPlayerKills);

	/**
	 * @return True if a target can only one hunter, false otherwise.
	 */
	Boolean isOneHunterPerTarget();

	/**
	 * Set if a target can only one hunter.
	 * 
	 * @param isOneHunterPerTarget True if a target can only one hunter, false otherwise.
	 */
	void setIsOneHunterPerTarget(boolean isOneHunterPerTarget);

	/**
	 * @return True if the distance from the target's hunter should be displayed on target's score board, false otherwise.
	 */
	Boolean isDistanceFromHunterDisplayed();

	/**
	 * Set if the distance between the hunter and its target should be displayed in the target's score board.
	 * 
	 * @param isDistanceFromHunterDisplayed True if the distance from the target's hunter should be displayed in the target's score
	 *                                      board, false otherwise.
	 */
	void setIsDistanceFromHunterDisplayed(boolean isDistanceFromHunterDisplayed);

	/**
	 * @return True if the target's name should be displayed in the hunter's score board, false otherwise.
	 */
	Boolean isTargetNameDisplayed();

	/**
	 * Set if the target's name should be displayed in the hunter's score board.
	 * 
	 * @param isTargetNameDisplayed True if the target's name should be displayed in the hunter's score board, false otherwise.
	 */
	void setIsTargetNameDisplayed(boolean isTargetNameDisplayed);

	/**
	 * @return The period between two refresh of the target direction in the hunter score board.
	 */
	LocalTime getTargetDirectionRefreshPeriod();

	/**
	 * Set the time between two refresh of the target direction in the hunter score board.
	 * 
	 * @param targetDirectionRefreshPeriod The period between two refresh of the target location in the hunter score board.
	 */
	void setTargetDirectionRefreshPeriod(LocalTime targetDirectionRefreshPeriod);

	/**
	 * @return The period between two refresh of the hunter distance in the target score board.
	 */
	LocalTime getHunterDistanceRefreshPeriod();

	/**
	 * Set the time between two refresh of the hunter distance in the target score board.
	 * 
	 * @param hunterDistanceRefreshPeriod The period between two refresh of the hunter distance in the target score board.
	 */
	void setHunterDistanceRefreshPeriod(LocalTime hunterDistanceRefreshPeriod);
}
