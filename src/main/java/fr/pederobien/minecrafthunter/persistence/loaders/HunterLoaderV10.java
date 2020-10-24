package fr.pederobien.minecrafthunter.persistence.loaders;

import org.w3c.dom.Element;

import fr.pederobien.minecraftborder.interfaces.IBorderConfiguration;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;
import fr.pederobien.persistence.interfaces.IPersistence;
import fr.pederobien.persistence.interfaces.xml.IXmlPersistenceLoader;

public class HunterLoaderV10 extends AbstractHunterLoader {

	public HunterLoaderV10(IPersistence<IBorderConfiguration> borderPersistence) {
		super(1.0, borderPersistence);
	}

	@Override
	public IXmlPersistenceLoader<IHunterConfiguration> load(Element root) {
		createNewElement();

		// Getting configuration name
		setName(root);

		// Getting configuration borders
		setBorders(root);

		// Getting configuration uhc mode
		setIsUhc(root);

		// Getting configuration times
		setTimes(root);

		// Getting configuration itemOnPlayerKills
		setItemOnPlayerKills(root);

		// Getting configuration is one hunter per target
		setIsOneHunterPerTarget(root);

		// Getting configuration is distance from hunter displayed
		setIsDistanceFromHunterDisplayed(root);

		// Getting configuration is target name displayed
		setIsTargetNameDisplayed(root);

		// Getting configuration minimum distance
		setMinimumDistance(root);
		return this;
	}
}
