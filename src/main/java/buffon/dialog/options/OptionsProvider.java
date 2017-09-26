package buffon.dialog.options;

import buffon.util.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class OptionsProvider {

	private static Map<String, ObjectWrapper> options = new HashMap<>();

	public static final String NUMBER_OF_LINES = "noOfLines";
	public static final String NUMBER_OF_DIGITS = "noOfDigits";
	public static final String LENGTH_FACTOR = "lengthFactor";

	//private static final String OPTIONS_FILENAME = "src/main/resources/options.properties";

	private static Preferences prefs = Preferences.userNodeForPackage(OptionsProvider.class);

	static {
		try {
			initOptions();
		} catch (IOException e) {
			Util.displayErrorDialog();
		}
	}

	private static void initOptions() throws IOException {
		boolean setDefault = true;
		try {
			loadOptions();
			setDefault = options.size() == 0;
		} catch (IOException ignorable) {
		}

		if (setDefault) {
			setDefaultOptions();
			saveOptions();
		}
	}

	private static void loadOptions() throws IOException {
		try {
			options.put(NUMBER_OF_LINES, new ObjectWrapper(Integer.parseInt(prefs.get(NUMBER_OF_LINES, ""))));
			options.put(NUMBER_OF_DIGITS, new ObjectWrapper(Integer.parseInt(prefs.get(NUMBER_OF_DIGITS, ""))));
			options.put(LENGTH_FACTOR, new ObjectWrapper(Double.parseDouble(prefs.get(LENGTH_FACTOR, ""))));
		} catch (NumberFormatException ignorable) {
		}
	}

	public static void saveOptions() throws IOException {
		for (Map.Entry<String, ObjectWrapper> entry : options.entrySet()) {
			prefs.put(entry.getKey(), entry.getValue().getValue().toString());
		}
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			throw new IOException(e);
		}
	}

	public static void setDefaultOptions() {
		// The number of lines on the canvas
		setOption(NUMBER_OF_LINES, 5);

		// The number of decimal places to round the π to
		setOption(NUMBER_OF_DIGITS, 2);

		// The portion of the distance between the
		// lines on the Canvas. This is a value between 0.0 and 1.0
		// (included); for example, a length factor of 0.5 means
		// that the needle will be long {@code 0.5 * distance}.
		setOption(LENGTH_FACTOR, 0.50);
	}

	private static void setOption(String name, Object value) {
		ObjectWrapper wrapper = options.get(name);
		if (wrapper == null) {
			wrapper = new ObjectWrapper(value);
			options.put(name, wrapper);
		} else {
			wrapper.setValue(value);
		}
	}

	public static Map<String, ObjectWrapper> getOptions() {
		return options;
	}

	public static void setDefaultOptions(Map<String, ObjectWrapper> options) {
		OptionsProvider.options = options;
	}

	public static ObjectWrapper getOption(String name) {
		return options.get(name);
	}
}
