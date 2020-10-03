package fr.pederobien.minecrafthunter.commands;

import org.bukkit.plugin.Plugin;

import fr.pederobien.minecraftborder.editions.AbstractGameBorderConfigurationParent;
import fr.pederobien.minecraftgameplateform.interfaces.editions.IMapPersistenceEdition;
import fr.pederobien.minecrafthunter.EHunterMessageCode;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;
import fr.pederobien.minecrafthunter.persistence.HunterPersistence;

public class HunterParent extends AbstractGameBorderConfigurationParent<IHunterConfiguration> {

	public HunterParent(Plugin plugin) {
		super("hunter", EHunterMessageCode.HUNTER_EXPLANATION, plugin, HunterPersistence.getInstance());
		addEdition(HunterEditionFactory.playerDontReviveTime());
		addEdition(HunterEditionFactory.isUhc());
		addEdition(HunterEditionFactory.itemOnPlayerKills());
		addEdition(HunterEditionFactory.isOneHunterPerTarget());
		addEdition(HunterEditionFactory.isDistanceFromHunterDisplayed());
	}

	@Override
	protected IMapPersistenceEdition<IHunterConfiguration> getNewEdition() {
		return HunterEditionFactory.newHunter();
	}

	@Override
	protected IMapPersistenceEdition<IHunterConfiguration> getRenameEdition() {
		return HunterEditionFactory.renameHunter();
	}

	@Override
	protected IMapPersistenceEdition<IHunterConfiguration> getSaveEdition() {
		return HunterEditionFactory.saveHunter();
	}

	@Override
	protected IMapPersistenceEdition<IHunterConfiguration> getListEdition() {
		return HunterEditionFactory.listHunter();
	}

	@Override
	protected IMapPersistenceEdition<IHunterConfiguration> getDeleteEdition() {
		return HunterEditionFactory.deleteHunter();
	}

	@Override
	protected IMapPersistenceEdition<IHunterConfiguration> getDetailsEdition() {
		return HunterEditionFactory.detailsHunter();
	}

	@Override
	protected IMapPersistenceEdition<IHunterConfiguration> getLoadEdition() {
		return HunterEditionFactory.loadHunter();
	}
}
