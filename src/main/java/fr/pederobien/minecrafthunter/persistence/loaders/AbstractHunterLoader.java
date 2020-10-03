package fr.pederobien.minecrafthunter.persistence.loaders;

import java.io.FileNotFoundException;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.pederobien.minecraftborder.interfaces.IBorderConfiguration;
import fr.pederobien.minecrafthunter.impl.HunterConfiguration;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;
import fr.pederobien.minecrafthunter.persistence.HunterXmlTag;
import fr.pederobien.persistence.impl.xml.AbstractXmlPersistenceLoader;
import fr.pederobien.persistence.interfaces.IPersistence;

public abstract class AbstractHunterLoader extends AbstractXmlPersistenceLoader<IHunterConfiguration> {
	private IPersistence<IBorderConfiguration> borderPersistence;

	protected AbstractHunterLoader(Double version, IPersistence<IBorderConfiguration> borderPersistence) {
		super(version);
		this.borderPersistence = borderPersistence;
	}

	@Override
	protected IHunterConfiguration create() {
		return new HunterConfiguration("DefaultHunterConfiguration");
	}

	/**
	 * @return The persistence that load borders associated to an hunger game configuration.
	 */
	public IPersistence<IBorderConfiguration> getBorderPersistence() {
		return borderPersistence;
	}

	/**
	 * Set the configuration's name
	 * 
	 * @param root The xml root that contains all configuration's parameter
	 */
	protected void setName(Element root) {
		Node name = getElementsByTagName(root, HunterXmlTag.NAME).item(0);
		get().setName(name.getChildNodes().item(0).getNodeValue());
	}

	/**
	 * Set the configuration's borders
	 * 
	 * @param root The xml root that contains all configuration's parameter
	 */
	protected void setBorders(Element root) {
		get().clearBorders();
		NodeList borders = getElementsByTagName(root, HunterXmlTag.BORDER);
		for (int i = 0; i < borders.getLength(); i++) {
			try {
				getBorderPersistence().load(getStringAttribute((Element) borders.item(i), HunterXmlTag.NAME));
				get().add(getBorderPersistence().get());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Set the configuration's times : pvpTime, playerDontReviveTimes.
	 * 
	 * @param root The xml root that contains all configuration's parameter
	 */
	protected void setTimes(Element root) {
		Node times = getElementsByTagName(root, HunterXmlTag.TIMES).item(0);
		get().setPvpTime(getLocalTimeAttribute((Element) times, HunterXmlTag.PVP));
		get().setPlayerDontReviveTime(getLocalTimeAttribute((Element) times, HunterXmlTag.PLAYER_DONT_REVIVE));
	}

	/**
	 * Set the configuration's uhc mode.
	 * 
	 * @param root The xml root that contains all configuration's parameter
	 */
	protected void setIsUhc(Element root) {
		Node isUhc = getElementsByTagName(root, HunterXmlTag.IS_UHC).item(0);
		get().setIsUhc(getBooleanNodeValue(isUhc.getChildNodes().item(0)));
	}

	/**
	 * Set the configuration's item on player kills.
	 * 
	 * @param root The xml root that contains all configuration's parameter
	 */
	protected void setItemOnPlayerKills(Element root) {
		Node itemOnPlayerKills = getElementsByTagName(root, HunterXmlTag.ITEM_ON_PLAYER_KILLS).item(0);
		get().setItemOnPlayerKills(new ItemStack(getMaterial(itemOnPlayerKills.getChildNodes().item(0).getNodeValue())));
	}

	/**
	 * Set the configuration's is one hunter per target.
	 * 
	 * @param root The xml root that contains all configuration's parameter
	 */
	protected void setIsOneHunterPerTarget(Element root) {
		Node isOneHunterPerTarget = getElementsByTagName(root, HunterXmlTag.IS_ONE_HUNTER_PER_TARGET).item(0);
		get().setIsOneHunterPerTarget(getBooleanNodeValue(isOneHunterPerTarget.getChildNodes().item(0)));
	}

	/**
	 * Set the configuration's is distance from hunter displayed
	 * 
	 * @param root The xml root that contains all configuration's parameter
	 */
	protected void setIsDistanceFromHunterDisplayed(Element root) {
		Node isDistanceFromHunterDisplayed = getElementsByTagName(root, HunterXmlTag.IS_DISTANCE_FROM_HUNTER_DISPLAYED).item(0);
		get().setIsDistanceFromHunterDisplayed(getBooleanNodeValue(isDistanceFromHunterDisplayed.getChildNodes().item(0)));
	}

	/**
	 * Set the configuration's is target name displayed
	 * 
	 * @param root The xml root that contains all configuration's parameter
	 */
	protected void setIsTargetNameDisplayed(Element root) {
		Node isTargetNameDisplayed = getElementsByTagName(root, HunterXmlTag.IS_TARGET_NAME_DISPLAYED).item(0);
		get().setIsTargetNameDisplayed(getBooleanNodeValue(isTargetNameDisplayed.getChildNodes().item(0)));
	}

	/**
	 * Set the configuration's target direction refresh period.
	 * 
	 * @param root The xml root that contains all configuration's parameter
	 */
	protected void setTargetDirectionRefreshPeriod(Element root) {
		Node targetDirectionRefreshPeriod = getElementsByTagName(root, HunterXmlTag.TARGET_DIRECTION_REFRESH_PERIOD).item(0);
		get().setTargetDirectionRefreshPeriod(getLocalTimeNodeValue(targetDirectionRefreshPeriod.getChildNodes().item(0)));
	}

	/**
	 * Set the configuration's hunter distance refresh period.
	 * 
	 * @param root The xml root that contains all configuration's parameter
	 */
	protected void setHunterDistanceRefreshPeriod(Element root) {
		Node hunterDistanceRefreshPeriod = getElementsByTagName(root, HunterXmlTag.HUNTER_DISTANCE_REFRESH_PERIOD).item(0);
		get().setHunterDistanceRefreshPeriod(getLocalTimeNodeValue(hunterDistanceRefreshPeriod.getChildNodes().item(0)));
	}

	private Material getMaterial(String name) {
		String normalizedName = name.toUpperCase().replace(" ", "_");
		for (Material material : Material.values())
			if (material.name().equals(normalizedName))
				return material;
		return null;
	}
}
