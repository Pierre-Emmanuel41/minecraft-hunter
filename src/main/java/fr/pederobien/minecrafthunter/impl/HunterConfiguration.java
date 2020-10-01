package fr.pederobien.minecrafthunter.impl;

import java.time.LocalTime;
import java.util.StringJoiner;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.pederobien.minecraftborder.impl.AbstractGameBorderConfiguration;
import fr.pederobien.minecraftdevelopmenttoolkit.utils.DisplayHelper;
import fr.pederobien.minecraftgameplateform.interfaces.element.IGame;
import fr.pederobien.minecraftgameplateform.interfaces.element.ITeam;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;
import fr.pederobien.minecraftmanagers.WorldManager;

public class HunterConfiguration extends AbstractGameBorderConfiguration implements IHunterConfiguration {
	private static final LocalTime DEFAULT_PLAYER_DONT_REVIVE_TIME = LocalTime.of(0, 0, 0);
	private static final Boolean DEFAULT_UHC_MODE = false;
	private static final ItemStack DEFAULT_ITEM_ON_PLAYER_KILLS = new ItemStack(Material.GOLDEN_APPLE);

	// private IGame game;
	private LocalTime playerDontReviveTime, playerDontReviveTimeBefore;
	private Boolean isUhc;
	private ItemStack itemOnPlayerKills;

	protected HunterConfiguration(String name) {
		super(name);
	}

	@Override
	public IGame getGame() {
		return null;
	}

	@Override
	public LocalTime getPlayerDontReviveTime() {
		return playerDontReviveTime == null ? DEFAULT_PLAYER_DONT_REVIVE_TIME : playerDontReviveTime;
	}

	@Override
	public void setPlayerDontReviveTime(LocalTime playerDontReviveTime) {
		if (isUhc())
			return;
		this.playerDontReviveTime = playerDontReviveTime;
		playerDontReviveTimeBefore = getPlayerDontReviveTime();
	}

	@Override
	public Boolean isUhc() {
		return isUhc == null ? DEFAULT_UHC_MODE : isUhc;
	}

	@Override
	public void setIsUhc(boolean isUhc) {
		this.isUhc = isUhc;
		playerDontReviveTime = isUhc ? DEFAULT_PLAYER_DONT_REVIVE_TIME : playerDontReviveTimeBefore;
	}

	@Override
	public ItemStack getItemOnPlayerKills() {
		return itemOnPlayerKills == null ? DEFAULT_ITEM_ON_PLAYER_KILLS : itemOnPlayerKills;
	}

	@Override
	public void setItemOnPlayerKills(ItemStack itemOnPlayerKills) {
		this.itemOnPlayerKills = itemOnPlayerKills;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner("\n");
		joiner.add("Name : " + getName());
		joiner.add("Teams :" + (getTeams().isEmpty() ? " none" : ""));
		for (ITeam team : getTeams())
			joiner.add(team.toString());

		joiner.add("Borders :" + (getBorders().isEmpty() ? "none" : ""));
		if (!getBorders().isEmpty()) {
			joiner.add(getWorldBorders(WorldManager.OVERWORLD));
			joiner.add(getWorldBorders(WorldManager.NETHER_WORLD));
			joiner.add(getWorldBorders(WorldManager.END_WORLD));
		}
		joiner.add("IsUhc : " + display(isUhc, isUhc().toString()));
		joiner.add("Player don't revive time : " + display(playerDontReviveTime, DisplayHelper.toString(getPlayerDontReviveTime(), true)));
		joiner.add("Pvp time : " + DisplayHelper.toString(getPvpTime(), true));
		joiner.add("Item on player kills : " + display(itemOnPlayerKills, normalizeMaterial(getItemOnPlayerKills().getType())));
		return joiner.toString();
	}

	private String normalizeMaterial(Material material) {
		return material.toString().toLowerCase().replace("_", " ");
	}
}
