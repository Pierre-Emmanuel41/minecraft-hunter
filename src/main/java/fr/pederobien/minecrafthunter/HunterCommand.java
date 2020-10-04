package fr.pederobien.minecrafthunter;

import org.bukkit.plugin.java.JavaPlugin;

import fr.pederobien.minecraftgameplateform.commands.AbstractParentCommand;
import fr.pederobien.minecraftgameplateform.interfaces.element.IGame;
import fr.pederobien.minecrafthunter.commands.HunterParent;
import fr.pederobien.minecrafthunter.impl.Hunters;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class HunterCommand extends AbstractParentCommand<IHunterConfiguration> {

	protected HunterCommand(JavaPlugin plugin) {
		super(plugin, new HunterParent(plugin));
	}

	@Override
	public <U extends IGame> void onGameIsStopped(U IGame) {
		Hunters.getInstance().reset();
	}
}
