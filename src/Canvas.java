import javax.swing.JComponent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Canvas extends JComponent {

	/**
	 * The number of lines on the canvas.
	 */
	public static int NUMBER_OF_LINES = 5;

	/**
	 * The distance between the lines.
	 */
	private int distance;

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
	private static final int delay = 10;

	/**
	 * A flag indicating whether the animation is currently running.
	 * Is toggled through {@link #start()} and {@link #stop()} methods.
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
	private int countNeedles;

	/**
	 * The number of needles which have landed on one of the lines.
	 */
	private int countLanded;

	/**
	 * The approximate value of the number {@code pi}.
	 */
	private double pi;

	/**
	 * The length of the needles thrown onto this canvas.
	 */
	private int needleLength;

	/**
	 * Creates a new drawing canvas.
	 */
	public Canvas() {
	}

	/**
	 * Starts the animation.
	 */
	public void start() {
		if (running) return;

		new Thread(() -> {
			while (running) {
				repaint();

				try {
					Thread.sleep(delay);
				} catch (InterruptedException ignorable) {
				}
			}
		}).start();

		running = true;
	}

	/**
	 * Pauses the animation.
	 */
	public void pause() {
		running = false;
	}

	/**
	 * Stops the animation.
	 */
	public void stop() {
		clearImage();
		running = false;
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (image == null) init();

		// randomly choose a point on the plane
		int x = Math.abs(rand.nextInt() % this.getWidth());
		int y = Math.abs(rand.nextInt() % this.getHeight());

		// randomly choose the angle of the needle
		double angle = Math.abs(rand.nextInt() % (2 * Math.PI));

		// calculate the x and y lengths to the points
		double xLen = needleLength / 2 * Math.cos(angle);
		double yLen = needleLength / 2 * Math.sin(angle);

		// get the points to form a needle
		int x1 = x - (int) xLen;
		int y1 = y - (int) yLen;
		int x2 = x + (int) xLen;
		int y2 = y + (int) yLen;

		int red = Math.abs(rand.nextInt()) % 256;
		int green = Math.abs(rand.nextInt()) % 256;
		int blue = Math.abs(rand.nextInt()) % 256;

		Needle needle = new Needle(x1, y1, x2, y2, new Color(red, green, blue));
		updateValues(needle);

		g2d.setStroke(new BasicStroke(1.8f));
		g2d.setColor(needle.getColor());
		g2d.drawLine(needle.getX1(), needle.getY1(), needle.getX2(), needle.getY2());

		g.drawImage(image, 0, 0, null);
	}

	/**
	 * Initializes the canvas and it's settings.
	 */
	private void init() {
		image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		clearImage();

		g2d.setColor(Color.BLACK);
		drawLines(NUMBER_OF_LINES);

		needleLength = (int) (Needle.lengthFactor * distance);
	}

	/**
	 * Clears the {@link #image} by removing all elements drawn
	 * upon it and by restoring the default background color.
	 */
	private void clearImage() {
		g2d = image.createGraphics();
		g2d.setBackground(backgroundColor);
		g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
	}

	/**
	 * Updates the simulation values: {@link #countNeedles}, {@link #countLanded}
	 * and {@link #pi}.
	 *
	 * @param needle the needle which was just thrown
	 */
	private void updateValues(Needle needle) {
		countNeedles++;
		if (landedOnLine(needle)) {
			countLanded++;
			pi = calculatePI();
		}
	}

	/**
	 * Draws a specified number of equally placed lines on the given
	 * graphics object.
	 *
	 * @param n the number of lines to draw
	 */
	private void drawLines(int n) {
		if (n < 2) throw new IllegalArgumentException();

		distance = this.getWidth() / (n - 1);

		int currentDistance = 0;
		for (int i = 0; i < n; i++) {
			lineCoordinates.add(currentDistance);

			g2d.drawLine(currentDistance, 0, currentDistance, this.getHeight());
			currentDistance += distance;
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
	 * Computes a new value of the number {@code pi} and stores
	 * it into the variable {@link #pi}.
	 */
	private double calculatePI() {
		return 2 * (double) needleLength / distance * (double) countNeedles / countLanded;
	}
}
