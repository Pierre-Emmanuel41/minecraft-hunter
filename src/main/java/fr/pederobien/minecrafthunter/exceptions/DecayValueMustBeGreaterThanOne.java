package fr.pederobien.minecrafthunter.exceptions;

import fr.pederobien.minecraftgameplateform.exceptions.SimpleMessageException;

public class DecayValueMustBeGreaterThanOne extends SimpleMessageException {
	private static final long serialVersionUID = 1L;
	private int decay;

	public DecayValueMustBeGreaterThanOne(int decay) {
		super("The parameter decay should be greater or equals to 1");
	}

	/**
	 * @return The value of decay that should be greater than one.
	 */
	public int getDecay() {
		return decay;
	}
}
