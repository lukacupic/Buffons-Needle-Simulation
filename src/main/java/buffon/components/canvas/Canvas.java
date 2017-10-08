package buffon.components.canvas;

import buffon.components.canvas.update.IUpdateListener;
import buffon.components.canvas.update.IUpdateProvider;
import buffon.dialog.options.OptionsProvider;
import buffon.model.Needle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A canvas for running and displaying the animation.
 */
public class Canvas extends JComponent implements IUpdateProvider {

	/**
	 * The distance between the lines.
	 */
	private double distance;

	/**
	 * The coordinates to check against when a needle is thrown.
	 */
	private List<Integer> lineCoordinates = new ArrayList<>();

	/**
	 * Pseudo-random number generator object.
	 */
	private Random rand = new Random();

	/**
	 * Animation delay in milliseconds - the time in between
	 * each new needle is thrown onto the canvas.
	 */
	private static int delay;

	/**
	 * A flag indicating whether the animation is currently running.
	 * Is toggled through {@link #play()} and {@link #stop()} methods.
	 */
	private volatile boolean running = false;

	/**
	 * The image object holding the graphical context of the canvas.
	 */
	private BufferedImage image;

	/**
	 * The graphics object, representing the graphics of the {@link #image}.
	 */
	private Graphics2D g2d;

	/**
	 * The background color of the canvas.
	 */
	private Color backgroundColor = new Color(225, 225, 225);

	/**
	 * The number of needles which have been thrown on the canvas.
	 */
	private int thrownCount;

	/**
	 * The number of needles which have landed on one of the lines.
	 */
	private int hitCount;

	/**
	 * The approximate value of π.
	 */
	private double pi = -1;

	/**
	 * The length of the needles thrown onto this canvas.
	 */
	private int needleLength;

	/**
	 * The timer for running the animation.
	 */
	private Timer animator = new Timer(delay, e -> Canvas.this.repaint());

	/**
	 * A list of update listeners, waiting to be notified for changes of
	 * the simulator values.
	 */
	private List<IUpdateListener> listeners = new ArrayList<>();


	/**
	 * Creates a new drawing canvas.
	 */
	public Canvas() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	}

	/**
	 * Plays the animation. If the animation is already playing,
	 * invoking will have no effect; otherwise the animation will
	 * either continue playing if previously paused or start a
	 * new animation if previously stopped.
	 */
	public void play() {
		if (running) return;
		animator.start();
		running = true;
	}

	/**
	 * Pauses the animation, causing no more needles to be
	 * generated, until the {@link #play()} method is called
	 * upon.
	 */
	public void pause() {
		animator.stop();
		running = false;
	}

	/**
	 * Stops the animation, causing the deletion of any previous
	 * contents that were on the drawing canvas.
	 */
	public void stop() {
		pause();
		resetContext();
	}

	/**
	 * Clears the canvas and resets the simulator values.
	 */
	public void resetContext() {
		clearImage();
		resetSimulatorValues();
		repaint();
	}

	/**
	 * Resets the current simulator values to their default values.
	 */
	private void resetSimulatorValues() {
		thrownCount = 0;
		hitCount = 0;
		pi = -1;
	}

	/**
	 * Sets the animation speed.
	 * <p>
	 * The slowest speed is 1000 milliseconds (equivalent to speed value
	 * of 0.0) and the fastest speed (equivalent to 1.0) is the fastest
	 * possible speed of the animation, but since it's entirely hardware
	 * dependant, it cannot be exactly specified.
	 *
	 * @param speed the animation speed, from the interval [0.0, 1.0]
	 */
	public void setSpeed(double speed) {
		if (speed < 0 || speed > 1.0) {
			throw new IllegalArgumentException();
		}
		delay = (int) (1000 * Math.exp(-10 * speed));
		animator.setDelay(delay);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (image == null) init();

		if (running) {
			Needle needle = generateNeedle();
			updateValues(needle);

			Stroke temp = g2d.getStroke();
			g2d.setStroke(new BasicStroke(1.8f));
			g2d.setColor(needle.getColor());
			g2d.drawLine(needle.getX1(), needle.getY1(), needle.getX2(), needle.getY2());
			g2d.setStroke(temp);
		}
		g.drawImage(image, 0, 0, null);
		notifyListeners();
	}

	/**
	 * Returns a new randomly generated needle to be thrown
	 * onto the canvas.
	 *
	 * @return a new randomly generated needle
	 */
	private Needle generateNeedle() {
		int x = Math.abs(rand.nextInt() % this.getWidth());
		int y = Math.abs(rand.nextInt() % this.getHeight());

		double angle = Math.abs(rand.nextInt() % (2 * Math.PI));

		double xLen = needleLength / 2 * Math.cos(angle);
		double yLen = needleLength / 2 * Math.sin(angle);

		int x1 = x - (int) xLen;
		int y1 = y - (int) yLen;
		int x2 = x + (int) xLen;
		int y2 = y + (int) yLen;

		int r = Math.abs(rand.nextInt()) % 256;
		int g = Math.abs(rand.nextInt()) % 256;
		int b = Math.abs(rand.nextInt()) % 256;

		return new Needle(x1, y1, x2, y2, new Color(r, g, b));
	}

	/**
	 * Initializes the canvas and it's settings.
	 */
	private void init() {
		image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		// anti-aliasing
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		clearImage();
		calculateNeedleLength();
	}

	/**
	 * Clears the {@link #image} by removing all elements drawn
	 * upon it and by restoring the default background color.
	 */
	private void clearImage() {
		g2d.setBackground(backgroundColor);
		g2d.clearRect(0, 0, image.getWidth(), image.getHeight());

		g2d.setColor(Color.BLACK);
		drawLines((int) OptionsProvider.getOption(OptionsProvider.NUMBER_OF_STRIPS).getValue() + 1);
	}

	/**
	 * Draws a specified number of equally placed lines.
	 *
	 * @param n the number of lines to draw
	 */
	private void drawLines(int n) {
		if (n < 2) throw new IllegalArgumentException();

		distance = (double) this.getWidth() / (n - 1);

		lineCoordinates.clear(); // fixes the bug where needles would hit invisible lines

		double currentDistance = 0;
		for (int i = 0; i < n; i++) {
			lineCoordinates.add((int) currentDistance);

			g2d.drawLine((int) currentDistance, 0, (int) currentDistance, this.getHeight());
			currentDistance += distance;
		}
	}

	/**
	 * Calculates the length of the needle from the lengthFactor property.
	 */
	public void calculateNeedleLength() {
		double factor = (double) OptionsProvider.getOption(OptionsProvider.LENGTH_FACTOR).getValue();
		needleLength = ((Double) (factor * distance)).intValue();
	}

	/**
	 * Updates the simulation values: {@link #thrownCount}, {@link #hitCount}
	 * and {@link #pi}.
	 *
	 * @param needle the needle which was just thrown
	 */
	private void updateValues(Needle needle) {
		thrownCount++;
		if (landedOnLine(needle)) {
			hitCount++;
			pi = calculatePI();
		}
	}

	/**
	 * Checks whether the dropped needle has landed on a line.
	 *
	 * @param needle the needle to check
	 * @return true if the given needle has landed on a line;
	 * false otherwise
	 */
	private boolean landedOnLine(Needle needle) {
		int x1 = needle.getX1();
		int x2 = needle.getX2();

		for (int x : lineCoordinates) {
			// check if the x is in between x1 and x2 (while checking which of x1 or x2 is smaller)
			if (x1 < x2 ? x1 <= x && x <= x2 : x2 <= x && x <= x1) return true; // :)
		}
		return false;
	}

	/**
	 * Computes a new value of π and stores it into the
	 * variable {@link #pi}.
	 */
	private double calculatePI() {
		return 2 * (double) needleLength / distance * (double) thrownCount / hitCount;
	}

	/**
	 * Returns the total number of needles which have been
	 * thrown onto the canvas.
	 *
	 * @return the total number of needles thrown onto the
	 * canvas
	 */
	public int getThrownCount() {
		return thrownCount;
	}

	/**
	 * Returns the number of needles which have landed
	 * on one of the lines.
	 *
	 * @return the number of needles, landed on one of
	 * the lines in the current animation cycle
	 */
	public int getHitCount() {
		return hitCount;
	}


	/**
	 * Returns the calculated value of π, rounded to the number
	 * of decimal places wrapped inside OptionsProvider#getNoOfDigits.
	 *
	 * @return the value of π, rounded to a specific number of
	 * digits.
	 */
	public String getPI() {
		if (Math.abs(pi + 1) < 10E-8) return "-";
		return String.format("%." + OptionsProvider.getOption(OptionsProvider.NUMBER_OF_DIGITS).getValue() + "f", pi);
	}

	// IUpdateProvider methods

	@Override
	public void addUpdateListener(IUpdateListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeUpdateListener(IUpdateListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void notifyListeners() {
		for (IUpdateListener l : listeners) {
			l.update(this);
		}
	}
}
