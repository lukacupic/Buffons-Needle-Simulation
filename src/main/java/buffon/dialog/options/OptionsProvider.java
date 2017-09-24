package buffon.dialog.options;

import buffon.components.canvas.Canvas;

public class OptionsProvider {

	/**
	 * A wrapper of the number of lines on the canvas.
	 */
	private static IntOptionWrapper noOfLines = new IntOptionWrapper(5);

	/**
	 * A wrapper of the number of decimal places to round the π to.
	 */
	private static IntOptionWrapper noOfDigits = new IntOptionWrapper(4);

	/**
	 * A wrapper of the portion of the distance between the
	 * lines on the {@link Canvas}.
	 * This is a value between {@code 0.0} and {@code 1.0}
	 * (included); for example, a length factor of 0.5 means
	 * that the needle will be long {@code 0.5 * distance}.
	 */
	private static IntOptionWrapper lengthFactor = new IntOptionWrapper(50);

	public static IntOptionWrapper getNoOfLines() {
		return noOfLines;
	}

	public static IntOptionWrapper getNoOfDigits() {
		return noOfDigits;
	}

	public static IntOptionWrapper getLengthFactor() {
		return lengthFactor;
	}
}
