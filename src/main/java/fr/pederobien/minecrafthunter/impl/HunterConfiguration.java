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
	private static final LocalTime DEFAULT_TARGET_DIRECTION_REFRESH_PERIOD = LocalTime.of(0, 0, 30);
	private static final LocalTime DEFAULT_HUNTER_DISTANCE_REFRESH_PERIOD = LocalTime.of(0, 0, 30);
	private static final Boolean DEFAULT_UHC_MODE = false;
	private static final Boolean DEFAULT_IS_ONE_HUNTER_PER_TARGET = true;
	private static final Boolean DEFAULT_IS_DISTANCE_FROM_HUNTER_DISPLAYED = true;
	private static final Boolean DEFAULT_IS_TARGET_NAME_DISPLAYED = false;
	private static final ItemStack DEFAULT_ITEM_ON_PLAYER_KILLS = new ItemStack(Material.GOLDEN_APPLE);

	// private IGame game;
	private LocalTime playerDontReviveTime, playerDontReviveTimeBefore, targetDirectionRefreshPeriod, hunterDistanceRefreshPeriod;
	private Boolean isUhc, isOneHunterPerTarget, isDistanceFromHunterDisplayed, isTargetNameDisplayed;
	private ItemStack itemOnPlayerKills;

	public HunterConfiguration(String name) {
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
	public Boolean isOneHunterPerTarget() {
		return isOneHunterPerTarget == null ? DEFAULT_IS_ONE_HUNTER_PER_TARGET : isOneHunterPerTarget;
	}

	@Override
	public void setOneHunterPerTarget(boolean isOneHunterPerTarget) {
		this.isOneHunterPerTarget = isOneHunterPerTarget;
	}

	@Override
	public Boolean isDistanceFromHunterDisplayed() {
		return isOneHunterPerTarget ? isDistanceFromHunterDisplayed == null ? DEFAULT_IS_DISTANCE_FROM_HUNTER_DISPLAYED : isDistanceFromHunterDisplayed : false;
	}

	@Override
	public void setIsDistanceFromHunterDisplayed(boolean isDistanceFromHunterDisplayed) {
		this.isDistanceFromHunterDisplayed = isDistanceFromHunterDisplayed;
	}

	@Override
	public Boolean isTargetNameDisplayed() {
		return isTargetNameDisplayed == null ? DEFAULT_IS_TARGET_NAME_DISPLAYED : isTargetNameDisplayed;
	}

	@Override
	public void setIsTargetNameDisplayed(boolean isTargetNameDisplayed) {
		this.isTargetNameDisplayed = isTargetNameDisplayed;
	}

	@Override
	public LocalTime getTargetDirectionRefreshPeriod() {
		return targetDirectionRefreshPeriod == null ? DEFAULT_TARGET_DIRECTION_REFRESH_PERIOD : targetDirectionRefreshPeriod;
	}

	@Override
	public void setTargetDirectionRefreshPeriod(LocalTime targetDirectionRefreshPeriod) {
		this.targetDirectionRefreshPeriod = targetDirectionRefreshPeriod;
	}

	@Override
	public LocalTime getHunterDistanceRefreshPeriod() {
		return hunterDistanceRefreshPeriod == null ? DEFAULT_HUNTER_DISTANCE_REFRESH_PERIOD : hunterDistanceRefreshPeriod;
	}

	@Override
	public void setHunterDistanceRefreshPeriod(LocalTime hunterDistanceRefreshPeriod) {
		this.hunterDistanceRefreshPeriod = hunterDistanceRefreshPeriod;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner("\n");
		joiner.add("Name : " + getName());
		joiner.add("Teams : " + (getTeams().isEmpty() ? "none" : ""));
		for (ITeam team : getTeams())
			joiner.add(team.toString());

		joiner.add("Borders : " + (getBorders().isEmpty() ? "none" : ""));
		if (!getBorders().isEmpty()) {
			joiner.add(getWorldBorders(WorldManager.OVERWORLD));
			joiner.add(getWorldBorders(WorldManager.NETHER_WORLD));
			joiner.add(getWorldBorders(WorldManager.END_WORLD));
		}
		joiner.add("IsUhc : " + display(isUhc, isUhc().toString()));
		joiner.add("Player don't revive time : " + display(playerDontReviveTime, DisplayHelper.toString(getPlayerDontReviveTime(), true)));
		joiner.add("Pvp time : " + DisplayHelper.toString(getPvpTime(), true));
		joiner.add("Item on player kills : " + display(itemOnPlayerKills, normalizeMaterial(getItemOnPlayerKills().getType())));
		joiner.add("One hunter per target : " + display(isOneHunterPerTarget, isOneHunterPerTarget().toString()));
		joiner.add("Distance from hunter displayed : " + display(isDistanceFromHunterDisplayed, isDistanceFromHunterDisplayed().toString()));
		joiner.add("Target name displayed : " + display(isTargetNameDisplayed, isTargetNameDisplayed().toString()));
		joiner.add("Target direction refresh period : " + display(targetDirectionRefreshPeriod, DisplayHelper.toString(getTargetDirectionRefreshPeriod(), true)));
		joiner.add("Hunter distance refresh period : " + display(hunterDistanceRefreshPeriod, DisplayHelper.toString(getHunterDistanceRefreshPeriod(), true)));
		return joiner.toString();
	}

	private String normalizeMaterial(Material material) {
		return material.toString().toLowerCase().replace("_", " ");
	}
}
