package buffon.model;

import java.awt.Color;

/**
 * Represents a single needle, which will be thrown on
 * to the canvas.
 */
public class Needle {

	/**
	 * The x coordinate of the first point.
	 */
	private int x1;

	/**
	 * The y coordinate of the first point.
	 */
	private int y1;

	/**
	 * The x coordinate of the second point.
	 */
	private int x2;

	/**
	 * The y coordinate of the second point.
	 */
	private int y2;

	/**
	 * The color of the needle.
	 */
	private Color color;

	/**
	 * Creates an a new needle.
	 *
	 * @param x1    x-coordinate of the first point
	 * @param y1    y-coordinate of the first point
	 * @param x2    x-coordinate of the second point
	 * @param y2    y-coordinate of the second point
	 * @param color the color of the needle
	 */
	public Needle(int x1, int y1, int x2, int y2, Color color) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;
	}

	/**
	 * Gets the x coordinate of the first point.
	 *
	 * @return the x coordinate of the first point
	 */
	public int getX1() {
		return x1;
	}

	/**
	 * Gets the y coordinate of the first point.
	 *
	 * @return the y coordinate of the first point
	 */
	public int getY1() {
		return y1;
	}

	/**
	 * Gets the x coordinate of the second point.
	 *
	 * @return the x coordinate of the second point
	 */
	public int getX2() {
		return x2;
	}

	/**
	 * Gets the y coordinate of the second point.
	 *
	 * @return the y coordinate of the second point
	 */
	public int getY2() {
		return y2;
	}

	/**
	 * Gets the color of the needle.
	 *
	 * @return the needle's color
	 */
	public Color getColor() {
		return color;
	}
}