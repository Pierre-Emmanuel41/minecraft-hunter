package fr.pederobien.minecrafthunter.impl.state;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.pederobien.minecraftborder.interfaces.IBorderConfiguration;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecrafthunter.impl.Hunters;
import fr.pederobien.minecrafthunter.interfaces.IHunterGame;
import fr.pederobien.minecraftmanagers.PlayerManager;
import fr.pederobien.minecraftmanagers.PotionManager;
import fr.pederobien.minecraftmanagers.WorldManager;

public class StartState extends AbstractState {
	private Map<Player, Location> locations;

	public StartState(IHunterGame game) {
		super(game);
		locations = new HashMap<Player, Location>();
	}

	@Override
	public void start() {
		giveEffects();
		updatePlayers();
		updateOverWorld();
		teleport();
		Hunters.getInstance().dispatchHunters(getConfiguration());
		Plateform.getObjectiveUpdater().start();
		getGame().setCurrentState(getGame().getInGameState());
	}

	private void giveEffects() {
		PotionEffect resistance = PotionManager.createEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 1);
		PotionEffect regeneration = PotionManager.createEffect(PotionEffectType.REGENERATION, 600, 1);
		PotionEffect saturation = PotionManager.createEffect(PotionEffectType.SATURATION, 600, 1);
		PlayerManager.getPlayers().forEach(player -> PotionManager.giveEffects(player, resistance, regeneration, saturation));
	}

	private void updatePlayers() {
		PlayerManager.maxFoodForPlayers();
		PlayerManager.resetMaxHealthOfPlayers();
		PlayerManager.maxLifeToPlayers();
		PlayerManager.removeInventoryOfPlayers();
		PlayerManager.resetLevelOfPlayers();
		PlayerManager.setGameModeOfAllPlayers(GameMode.SURVIVAL);
	}

	private void updateOverWorld() {
		WorldManager.setTime(WorldManager.OVERWORLD, 0);
		WorldManager.setStorm(WorldManager.OVERWORLD, false);
		WorldManager.setThundering(WorldManager.OVERWORLD, false);
		WorldManager.setGameRule(WorldManager.OVERWORLD, GameRule.DO_DAYLIGHT_CYCLE, true);
	}

	private void teleport() {
		locations.clear();
		IBorderConfiguration overworld = getConfiguration().getBorder(WorldManager.OVERWORLD).get();

		PlayerManager.getPlayers().forEach(player -> {
			Location location = WorldManager.getRandomlyLocation(WorldManager.OVERWORLD, overworld.getBorderCenter(), overworld.getInitialBorderDiameter());
			if (!locations.isEmpty()) {
				boolean locationTooClose;
				do {
					locationTooClose = false;
					location = WorldManager.getRandomlyLocation(WorldManager.OVERWORLD, overworld.getBorderCenter(), overworld.getInitialBorderDiameter());
					for (Location loc : locations.values())
						locationTooClose |= WorldManager.getSquaredDistance2D(loc, location) < 150;
				} while (locationTooClose);
			}
			locations.put(player, location);
		});

		locations.entrySet().forEach(entry -> PlayerManager.teleporte(entry.getKey(), entry.getValue()));
	}
}
