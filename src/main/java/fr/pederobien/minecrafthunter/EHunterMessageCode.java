package fr.pederobien.minecrafthunter;

import fr.pederobien.minecraftdictionary.impl.Permission;
import fr.pederobien.minecraftdictionary.interfaces.IMinecraftMessageCode;

public enum EHunterMessageCode implements IMinecraftMessageCode {
	HUNTER_EXPLANATION,

	// Code for command new
	NEW_HUNTER__EXPLANATION, NEW_HUNTER__NAME_IS_MISSING, NEW_HUNTER__NAME_ALREADY_TAKEN, NEW_HUNTER__CONFIGURATION_CREATED,

	// Code for command rename
	RENAME_HUNTER__EXPLANATION, RENAME_HUNTER__NAME_IS_MISSING, RENAME_HUNTER__NAME_ALREADY_TAKEN, RENAME_HUNTER__CONFIGURATION_RENAMED,

	// Code for command save
	SAVE_HUNTER__EXPLANATION, SAVE_HUNTER__CONFIGURATION_SAVED,

	// Code for command list
	LIST_HUNTER__EXPLANATION, LIST_HUNTER__NO_REGISTERED_CONFIGURATION, LIST_HUNTER__ONE_REGISTERED_CONFIGURATION, LIST_HUNTER__SEVERAL_ELEMENTS,

	// Code for command delete
	DELETE_HUNTER__EXPLANATION, DELETE_HUNTER__NAME_IS_MISSING, DELETE_HUNTER__DID_NOT_DELETE, DELETE_HUNTER__CONFIGURATION_DELETED,

	// Code for command details
	DETAILS_HUNTER__EXPLANATION, DETAILS_HUNTER__ON_DETAILS,

	// Code for command load
	LOAD_HUNTER__EXPLANATION, LOAD_HUNTER__NAME_IS_MISSING, LOAD_HUNTER__CONFIGURATION_LOADED,

	// Code for command playerDontRevive
	PLAYER_DONT_REVIVE_TIME_HUNTER__EXPLANATION, PLAYER_DONT_REVIVE_TIME_HUNTER__TIME_IS_MISSING, PLAYER_DONT_REVIVE_TIME_HUNTER__FROM_THE_BEGINNING,
	PLAYER_DONT_REVIVE_TIME_HUNTER__TIME_DEFINED,

	// Code for command isUhc
	IS_UHC_HUNTER__EXPLANATION, IS_UHC_HUNTER__VALUE_IS_MISSING, IS_UHC_HUNTER__VALUE_DEFINED,

	// Code for command itemOnPlayerKills
	ITEM_ON_PLAYER_KILLS_HUNTER__EXPLANATION, ITEM_ON_PLAYER_KILLS_HUNTER__ITEM_IS_MISSING, ITEM_ON_PLAYER_KILLS_HUNTER__ITEM_NOT_FOUND,
	ITEM_ON_PLAYER_KILLS_HUNTER__ITEM_DEFINED,

	// Code for in game messages
	HUNTER_OVERWORLD_BORDER_IS_MISSING(Permission.SENDER), HUNTER_PVP_ENABLED(Permission.ALL), HUNTER_NO_RESURRECTION(Permission.ALL),
	HUNTER_NO_RESURRECTION_COUNT_DOWN(Permission.ALL);

	private Permission permission;

	private EHunterMessageCode() {
		this(Permission.OPERATORS);
	}

	private EHunterMessageCode(Permission permission) {
		this.permission = permission;
	}

	@Override
	public String value() {
		return toString();
	}

	@Override
	public Permission getPermission() {
		return permission;
	}

	@Override
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
}