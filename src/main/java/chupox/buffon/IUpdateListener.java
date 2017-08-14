package chupox.buffon;

/**
 * A listener for changes of the simulator values.
 */
public interface IUpdateListener {

	/**
	 * Called to indicate that an update of simulator
	 * values has occurred.
	 *
	 * @param provider the simulator whose values has
	 *                 been changed
	 */
	void update(IUpdateProvider provider);
}