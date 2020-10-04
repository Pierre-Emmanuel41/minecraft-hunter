package fr.pederobien.minecrafthunter.interfaces;

public interface IObsHunter {

	/**
	 * Notify this observer that the target of the given <code>hunter</code> has changed.
	 * 
	 * @param hunter    The hunter whose target has changed.
	 * @param oldTarget The old hunter target.
	 * @param newTarget The new hunter target.
	 */
	void onTargetChanged(IHunter hunter, IHunter oldTarget, IHunter newTarget);

	/**
	 * Notify this observer that an hunter has been added to the list of the given <code>hunter</code>
	 * 
	 * @param hunter      The hunter whose hunters list has changed.
	 * @param addedHunter The added hunter.
	 */
	void onHunterAdded(IHunter hunter, IHunter addedHunter);

	/**
	 * Notify this observer that an hunter has been removed from the list of the given <code>hunter</code>
	 * 
	 * @param hunter        The hunter whose hunters list has changed.
	 * @param removedHunter The removed hunter.
	 */
	void onHunterRemoved(IHunter hunter, IHunter removedHunter);
}
