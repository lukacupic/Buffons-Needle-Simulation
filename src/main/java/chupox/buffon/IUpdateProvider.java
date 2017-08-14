package chupox.buffon;

import chupox.buffon.components.canvas.Canvas;

/**
 * A provider of simulator changes. Any class implementing
 * this interface must be of type {@link Canvas}, representing
 * a simulator.
 */
public interface IUpdateProvider {

	/**
	 * Adds the specified listener to the list.
	 *
	 * @param listener the listener to add
	 */
	void addUpdateListener(IUpdateListener listener);

	/**
	 * Removes the specified listener from the list.
	 *
	 * @param listener the listener to remove
	 */
	void removeUpdateListener(IUpdateListener listener);

	/**
	 * Notifies all active listeners that a change has been
	 * made to the simulator values.
	 */
	void notifyListeners();
}