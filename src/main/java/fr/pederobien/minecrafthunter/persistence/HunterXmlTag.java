package fr.pederobien.minecrafthunter.persistence;

public enum HunterXmlTag {
	NAME("name"), BORDERS("borders"), BORDER("border"), TIMES("times"), PVP("pvp"), PLAYER_DONT_REVIVE("playerDontRevive"), IS_UHC("isUhc"),
	ITEM_ON_PLAYER_KILLS("itemOnPlayerKills");

	private String name;

	private HunterXmlTag(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
