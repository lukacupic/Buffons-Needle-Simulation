package buffon.dialog.options;

import buffon.util.Util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OptionsProvider {

	private static Map<String, ObjectWrapper> options = new HashMap<>();

	public static final String NUMBER_OF_LINES = "noOfLines";
	public static final String NUMBER_OF_DIGITS = "noOfDigits";
	public static final String LENGTH_FACTOR = "lengthFactor";

	private static final String OPTIONS_FILENAME = "src/main/resources/options.properties";

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
		InputStream in = null;
		try {
			in = new FileInputStream(OPTIONS_FILENAME);
		} catch (FileNotFoundException ignorable) {
		}

		if (in == null) return;

		Properties p = new Properties();
		p.load(in);

		options.put(NUMBER_OF_LINES, new ObjectWrapper(Integer.parseInt(p.getProperty(NUMBER_OF_LINES))));
		options.put(NUMBER_OF_DIGITS, new ObjectWrapper(Integer.parseInt(p.getProperty(NUMBER_OF_DIGITS))));
		options.put(LENGTH_FACTOR, new ObjectWrapper(Double.parseDouble(p.getProperty(LENGTH_FACTOR))));
	}

	public static void saveOptions() throws IOException {
		Properties p = new Properties();
		for (Map.Entry<String, ObjectWrapper> entry : options.entrySet()) {
			p.setProperty(entry.getKey(), entry.getValue().getValue().toString());
		}
		FileOutputStream os = new FileOutputStream(OPTIONS_FILENAME);
		p.store(os, null);
		os.flush();
		os.close();
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
