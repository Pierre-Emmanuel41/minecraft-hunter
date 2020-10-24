package fr.pederobien.minecrafthunter.commands;

import fr.pederobien.minecraftgameplateform.interfaces.element.ILabel;

public enum EHunterLabel implements ILabel {
	PLAYER_DONT_REVIVE_TIME("playerDontReviveTime"), IS_UHC("isUhc"), ITEM_ON_PLAYER_KILLS("itemOnPlayerKills"), IS_ONE_HUNTER_PER_TARGET("isOneHunterPerTarget"),
	IS_HUNTER_DISTANCE_DISPLAYED("isHunterDistanceDisplayed"), IS_TARGET_NAME_DISPLAYED("isTargetNameDisplayed"),
	TARGET_DIRECTION_REFRESH_PERIOD("targetDirectionRefreshPeriod"), HUNTER_DISTANCE_REFRESH_PERIOD("hunterDistanceRefreshPeriod"), MINIMUM_DISTANCE("minimumDistance"),
	DECAY("decay");

	private String label;

	private EHunterLabel(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}
}
