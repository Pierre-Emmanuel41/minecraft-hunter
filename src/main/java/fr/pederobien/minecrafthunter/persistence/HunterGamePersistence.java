package fr.pederobien.minecrafthunter.persistence;

import org.bukkit.Material;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.pederobien.minecraftborder.interfaces.IBorderConfiguration;
import fr.pederobien.minecraftborder.persistence.BorderPersistence;
import fr.pederobien.minecraftgameplateform.impl.element.persistence.AbstractMinecraftPersistence;
import fr.pederobien.minecraftgameplateform.interfaces.element.persistence.IMinecraftPersistence;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecrafthunter.impl.HunterConfiguration;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;
import fr.pederobien.minecrafthunter.persistence.loaders.HunterLoaderV10;
import fr.pederobien.persistence.interfaces.IPersistence;

public class HunterGamePersistence extends AbstractMinecraftPersistence<IHunterConfiguration> {
	private static final String ROOT_XML_DOCUMENT = "hungergame";
	private IPersistence<IBorderConfiguration> borderPersistence;

	private HunterGamePersistence() {
		super(Plateform.ROOT.resolve("HunterGame"), "DefaultHunterGameConfiguration");
		borderPersistence = BorderPersistence.getInstance();
		register(new HunterLoaderV10(borderPersistence));
	}

	public static IMinecraftPersistence<IHunterConfiguration> getInstance() {
		return SingletonHolder.PERSISTENCE;
	}

	private static class SingletonHolder {
		public static final IMinecraftPersistence<IHunterConfiguration> PERSISTENCE = new HunterGamePersistence();
	}

	@Override
	public void saveDefault() {
		set(new HunterConfiguration(getDefault()));
		save();
		for (IBorderConfiguration border : get().getBorders()) {
			borderPersistence.set(border);
			borderPersistence.save();
		}
		borderPersistence.set(null);
	}

	@Override
	public boolean save() {
		if (get() == null)
			return false;
		Document doc = newDocument();
		doc.setXmlStandalone(true);

		Element root = createElement(doc, ROOT_XML_DOCUMENT);
		doc.appendChild(root);

		Element version = createElement(doc, VERSION);
		version.appendChild(doc.createTextNode(getVersion().toString()));
		root.appendChild(version);

		Element name = createElement(doc, HunterXmlTag.NAME);
		name.appendChild(doc.createTextNode(get().getName()));
		root.appendChild(name);

		Element borders = createElement(doc, HunterXmlTag.BORDERS);
		for (IBorderConfiguration configuration : get().getBorders()) {
			Element border = createElement(doc, HunterXmlTag.BORDER);
			setAttribute(border, HunterXmlTag.NAME, configuration.getName());
			borders.appendChild(border);
		}
		root.appendChild(borders);

		Element isUhc = createElement(doc, HunterXmlTag.IS_UHC);
		isUhc.appendChild(doc.createTextNode(get().isUhc().toString()));
		root.appendChild(isUhc);

		Element times = createElement(doc, HunterXmlTag.TIMES);
		setAttribute(times, HunterXmlTag.PVP, get().getPvpTime());
		setAttribute(times, HunterXmlTag.PLAYER_DONT_REVIVE, get().getPlayerDontReviveTime());
		root.appendChild(times);

		Element itemOnPlayerKills = createElement(doc, HunterXmlTag.ITEM_ON_PLAYER_KILLS);
		itemOnPlayerKills.appendChild(doc.createTextNode(normalizeMaterial(get().getItemOnPlayerKills().getType())));
		root.appendChild(itemOnPlayerKills);

		saveDocument(doc, get().getName());
		return true;
	}

	private String normalizeMaterial(Material material) {
		return material.name().toLowerCase().replace("_", " ");
	}
}