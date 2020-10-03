package fr.pederobien.minecrafthunter.commands;

import fr.pederobien.minecraftgameplateform.interfaces.element.ILabel;

public enum EHunterLabel implements ILabel {
	PLAYER_DONT_REVIVE_TIME("playerDontReviveTime"), IS_UHC("isUhc"), ITEM_ON_PLAYER_KILLS("itemOnPlayerKills"), IS_ONE_HUNTER_PER_TARGET("isOneHunterPerTarget"),
	IS_DISTANCE_FROM_HUNTER_DISPLAYED("isDistanceFromHunterDisplayed"), IS_TARGET_NAME_DISPLAYED("isTargetNameDisplayed");

	private String label;

	private EHunterLabel(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}
}
