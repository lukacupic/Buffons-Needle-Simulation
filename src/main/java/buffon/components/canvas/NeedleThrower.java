package buffon.components.canvas;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class NeedleThrower {

	/**
	 * The canvas to draw upon.
	 */
	private Canvas canvas;

	/**
	 * The number of needles to throw.
	 */
	private int n;

	/**
	 * A counter for the number of needles thrown so far.
	 */
	private int counter;

	/**
	 * A flag denoting whether the NeedleThrower is currently
	 * active, i.e. if the needles are currently being thrown
	 * onto the canvas.
	 */
	private boolean throwing;

	/**
	 * The timer used to throw the needles. When triggered, it
	 * will complete the task (of throwing the needles) {@link #n}
	 * number of times, after which it will stop (until triggered
	 * again).
	 */
	private Timer thrower = new Timer(0, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			canvas.repaint();
			if (++counter > n) {
				throwing = false;
				((Timer) e.getSource()).stop();
			}
		}
	});

	/**
	 * Creates a new {@link NeedleThrower} object.
	 *
	 * @param canvas the canvas to throw the needles onto
	 * @param n      the number of needles to throw
	 */
	public NeedleThrower(Canvas canvas, int n) {
		this.canvas = canvas;
		this.n = n;
	}

	/**
	 * Creates a new {@link NeedleThrower} object.
	 *
	 * @param canvas the canvas to throw the needles onto
	 */
	public NeedleThrower(Canvas canvas) {
		this.canvas = canvas;
	}

	/**
	 * Starts throwing the needles. This method can be called
	 * multiple times.
	 */
	public void start() {
		this.counter = 0;
		this.throwing = true;
		thrower.start();
	}

	/**
	 * Sets the new number of needles to throw.
	 *
	 * @param n the number of needles to throw
	 */
	public void setN(int n) {
		this.n = n;
	}

	/**
	 * Checks if the needles are currently being thrown.
	 *
	 * @return true if and only if the needles are currently
	 * being thrown
	 */
	public boolean isThrowing() {
		return throwing;
	}
}
