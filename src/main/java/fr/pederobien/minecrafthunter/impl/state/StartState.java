package fr.pederobien.minecrafthunter.impl.state;

import java.util.Optional;

import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.pederobien.minecraftborder.interfaces.IBorderConfiguration;
import fr.pederobien.minecraftgameplateform.helpers.TeamHelper;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecrafthunter.interfaces.IHunterGame;
import fr.pederobien.minecraftmanagers.PlayerManager;
import fr.pederobien.minecraftmanagers.PotionManager;
import fr.pederobien.minecraftmanagers.WorldManager;

public class StartState extends AbstractState {

	public StartState(IHunterGame game) {
		super(game);
	}

	@Override
	public void start() {
		giveEffects();
		updatePlayers();
		updateOverWorld();
		teleport();
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
		TeamHelper.createTeamsOnServer(getConfiguration().getTeams());
		Optional<IBorderConfiguration> optConf = getConfiguration().getBorder(WorldManager.OVERWORLD);
		if (!optConf.isPresent())
			return;

		getConfigurationHelper().teleportTeamsRandomly(WorldManager.OVERWORLD, optConf.get().getBorderCenter(), optConf.get().getInitialBorderDiameter());
	}
}
