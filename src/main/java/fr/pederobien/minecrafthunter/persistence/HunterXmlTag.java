package fr.pederobien.minecrafthunter.persistence;

public enum HunterXmlTag {
	NAME("name"), BORDERS("borders"), BORDER("border"), TIMES("times"), PVP("pvp"), PLAYER_DONT_REVIVE("playerDontRevive"), IS_UHC("isUhc"),
	ITEM_ON_PLAYER_KILLS("itemOnPlayerKills"), IS_ONE_HUNTER_PER_TARGET("isOneHunterPerTarget"), IS_DISTANCE_FROM_HUNTER_DISPLAYED("isDistanceFromHunterDisplayed"),
	IS_TARGET_NAME_DISPLAYED("isTargetNameDisplayed"), TARGET_DIRECTION_REFRESH_PERIOD("targetDirectionRefreshPeriod"),
	HUNTER_DISTANCE_REFRESH_PERIOD("hunterDistanceRefreshPeriod");

	private String name;

	private HunterXmlTag(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
