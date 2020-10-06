package fr.pederobien.minecrafthunter.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import fr.pederobien.minecrafthunter.interfaces.IHunter;
import fr.pederobien.minecrafthunter.interfaces.IObsHunter;

public class Hunter extends EventListener implements IHunter {
	private Player source;
	private Map<Player, IHunter> hunters;
	private IHunter target;
	private IObservable<IObsHunter> observers;
	private List<IHunter> quitPlayers;

	private Hunter(Player player) {
		this.source = player;

		hunters = new HashMap<Player, IHunter>();
		observers = new Observable<IObsHunter>();
		quitPlayers = new ArrayList<IHunter>();

		Plateform.getPlayerQuitOrJoinEventListener().addObserver(this);
		register(HunterPlugin.get());
	}

	public static IHunter of(Player player) {
		return new Hunter(player);
	}

	@Override
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		if (event.getPlayer().getName().equals(source.getName())) {
			quitPlayers.add(this);
			return;
		}
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
		observers.notifyObservers(obs -> obs.onHunterAdded(this, hunter));
		hunters.put(hunter.getPlayer(), hunter);
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
		IHunter oldTarget = this.target;
		observers.notifyObservers(obs -> obs.onTargetChanged(this, oldTarget, target));
		this.target = target;
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

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!event.getEntity().getName().equals(source.getName()))
			return;

		if (HunterPlugin.getCurrentHunter().isOneHunterPerTarget())
			reorganizeHunterAndTarget(event.getEntity().getKiller());
		else
			reorganizeHunterAndTargets(event.getEntity().getKiller());
	}

	private void reorganizeHunterAndTarget(Player killer) {
		for (IHunter hunter : getHunters())
			hunter.setTarget(target);
	}

	private void reorganizeHunterAndTargets(Player killer) {
		for (IHunter hunter : getHunters())
			hunter.setTarget(target);
	}
}
