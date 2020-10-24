package fr.pederobien.minecrafthunter.exceptions;

import fr.pederobien.minecraftgameplateform.exceptions.SimpleMessageException;

public class MinimumDistanceMustBePositive extends SimpleMessageException {
	private static final long serialVersionUID = 1L;
	private int minimumDistance;

	public MinimumDistanceMustBePositive(int minimumDistance) {
		super("The minimum distance between players must be strictly positive");
		this.minimumDistance = minimumDistance;
	}

	/**
	 * @return The minimum distance that should be positive.
	 */
	public int getMinimumDistance() {
		return minimumDistance;
	}
}
