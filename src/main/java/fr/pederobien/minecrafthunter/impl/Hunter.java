package fr.pederobien.minecrafthunter.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.pederobien.minecraftgameplateform.impl.element.EventListener;
import fr.pederobien.minecraftgameplateform.impl.observer.Observable;
import fr.pederobien.minecraftgameplateform.interfaces.observer.IObservable;
import fr.pederobien.minecraftgameplateform.utils.Plateform;
import fr.pederobien.minecrafthunter.HunterPlugin;
import fr.pederobien.minecrafthunter.exceptions.TargetAndHunterAreEqualsException;
import fr.pederobien.minecrafthunter.interfaces.IHunter;
import fr.pederobien.minecrafthunter.interfaces.IObsHunter;
import fr.pederobien.minecraftmanagers.TeamManager;

public class Hunter extends EventListener implements IHunter {
	private Player source;
	private Map<Player, IHunter> hunters;
	private IHunter target;
	private IObservable<IObsHunter> observers;

	private Hunter(Player player) {
		this.source = player;

		hunters = new HashMap<Player, IHunter>();
		observers = new Observable<IObsHunter>();

		Plateform.getPlayerQuitOrJoinEventListener().addObserver(this);
		register(HunterPlugin.get());
	}

	public static IHunter of(Player player) {
		return new Hunter(player);
	}

	@Override
	public void onPlayerQuitEvent(PlayerQuitEvent event) {

	}

	@Override
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (event.getPlayer().getName().equals(source.getName()))
			setPlayer(event.getPlayer());
	}

	@Override
	public Player getPlayer() {
		return source;
	}

	@Override
	public void setPlayer(Player player) {
		this.source = player;
	}

	@Override
	public IHunter addHunter(IHunter hunter) {
		hunters.put(hunter.getPlayer(), hunter);
		observers.notifyObservers(obs -> obs.onHunterAdded(this, hunter));
		return this;
	}

	@Override
	public IHunter removeHunter(IHunter hunter) {
		if (hunters.remove(hunter.getPlayer()) != null)
			observers.notifyObservers(obs -> obs.onHunterRemoved(this, hunter));
		return this;
	}

	@Override
	public IHunter setTarget(IHunter target) {
		if (equals(target))
			throw new TargetAndHunterAreEqualsException(this);

		IHunter oldTarget = this.target;
		observers.notifyObservers(obs -> obs.onTargetChanged(this, oldTarget, target));
		this.target = target;
		if (target != null)
			target.addHunter(this);
		return this;
	}

	@Override
	public Optional<IHunter> getTarget() {
		return Optional.ofNullable(target);
	}

	@Override
	public List<IHunter> getHunters() {
		return Collections.unmodifiableList(new ArrayList<IHunter>(hunters.values()));
	}

	@Override
	public void addObserver(IObsHunter obs) {
		observers.addObserver(obs);
	}

	@Override
	public void removeObserver(IObsHunter obs) {
		observers.removeObserver(obs);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof IHunter))
			return false;

		IHunter other = (IHunter) obj;
		return getPlayer().equals(other.getPlayer());
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		for (IHunter hunter : getHunters())
			joiner.add(hunter.getPlayer().getName());
		return String.format("{%s,target=%s,hunters=%s}", getPlayer().getName(), (getTarget().isPresent() ? getTarget().get().getPlayer().getName() : null), joiner);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!event.getEntity().getName().equals(source.getName()) || event.getEntity().getGameMode().equals(GameMode.SURVIVAL))
			return;

		target.removeHunter(this);
		if (HunterPlugin.getCurrentHunter().isOneHunterPerTarget())
			reorganizeHunterAndTarget();
		else
			reorganizeHunterAndTargets();
		setTarget(null);

		List<IHunter> hunters = new ArrayList<IHunter>(this.hunters.values());
		hunters.forEach(hunter -> removeHunter(hunter));
	}

	private void reorganizeHunterAndTarget() {
		for (IHunter hunter : getHunters())
			try {
				hunter.setTarget(target);
			} catch (TargetAndHunterAreEqualsException e) {
				findNewTarget(e.getHunter());
			}
	}

	private void reorganizeHunterAndTargets() {
		for (IHunter hunter : getHunters())
			try {
				hunter.setTarget(target);
			} catch (TargetAndHunterAreEqualsException e) {
				findNewTarget(e.getHunter());
			}
	}

	private void findNewTarget(IHunter hunter) {
		List<IHunter> players = Hunters.getInstance().getNotDeadHunters().filter(h -> !h.getPlayer().equals(hunter.getPlayer())).collect(Collectors.toList());
		Optional<IHunter> optPlayer = TeamManager.getRandom(players);
		hunter.setTarget(optPlayer.isPresent() ? optPlayer.get() : null);
	}
}
