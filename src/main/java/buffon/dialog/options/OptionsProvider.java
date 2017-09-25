package buffon.dialog.options;

import java.util.HashMap;
import java.util.Map;

public class OptionsProvider {

	private static Map<String, IntOptionWrapper> options = new HashMap<>();

	static {
		resetOptions();
	}

	public static void resetOptions() {
		// The number of lines on the canvas
		resetOption("noOfLines", 5);

		// The number of decimal places to round the π to
		resetOption("noOfDigits", 2);

		// The portion of the distance between the
		// lines on the Canvas. This is a value between 0.0 and 1.0
		// (included); for example, a length factor of 0.5 means
		// that the needle will be long {@code 0.5 * distance}.
		resetOption("lengthFactor", 50);
	}

	private static void resetOption(String name, int value) {
		IntOptionWrapper wrapper = options.get(name);
		if (wrapper == null) {
			wrapper = new IntOptionWrapper(value);
			options.put(name, wrapper);
		} else {
			wrapper.setValue(value);
		}
	}

	public static Map<String, IntOptionWrapper> getOptions() {
		return options;
	}

	public static void setOptions(Map<String, IntOptionWrapper> options) {
		OptionsProvider.options = options;
	}

	public static IntOptionWrapper getOption(String name) {
		return options.get(name);
	}
}
