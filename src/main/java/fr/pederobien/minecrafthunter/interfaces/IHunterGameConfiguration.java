package fr.pederobien.minecrafthunter.interfaces;

import java.time.LocalTime;

import org.bukkit.inventory.ItemStack;

import fr.pederobien.minecraftborder.interfaces.IGameBorderConfiguration;

public interface IHunterGameConfiguration extends IGameBorderConfiguration {

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
}
