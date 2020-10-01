package fr.pederobien.minecrafthunter;

import org.bukkit.plugin.java.JavaPlugin;

import fr.pederobien.minecraftgameplateform.commands.AbstractParentCommand;
import fr.pederobien.minecrafthunter.commands.HunterParent;
import fr.pederobien.minecrafthunter.interfaces.IHunterConfiguration;

public class HunterCommand extends AbstractParentCommand<IHunterConfiguration> {

	protected HunterCommand(JavaPlugin plugin) {
		super(plugin, new HunterParent(plugin));
	}
}
