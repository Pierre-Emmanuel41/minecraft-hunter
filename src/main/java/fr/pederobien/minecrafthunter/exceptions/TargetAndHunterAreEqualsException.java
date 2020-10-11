package fr.pederobien.minecrafthunter.exceptions;

import fr.pederobien.minecraftgameplateform.exceptions.SimpleMessageException;
import fr.pederobien.minecrafthunter.interfaces.IHunter;

public class TargetAndHunterAreEqualsException extends SimpleMessageException {
	private static final long serialVersionUID = 1L;
	private IHunter hunter;

	public TargetAndHunterAreEqualsException(IHunter hunter) {
		super("The hunter " + hunter.getPlayer().getName() + " cannot be its target");
		this.hunter = hunter;
	}

	/**
	 * @return The hunter that hunts itself.
	 */
	public IHunter getHunter() {
		return hunter;
	}
}
