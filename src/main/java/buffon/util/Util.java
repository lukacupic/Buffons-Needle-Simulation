package buffon.util;

import buffon.Main;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Holds several utility methods for performing some common tasks.
 */
public class Util {

	/**
	 * The class loader for loading resources.
	 */
	private static ClassLoader classloader = Thread.currentThread().getContextClassLoader();

	/**
	 * Returns the icon, created from an image with the specified name,
	 * and of specified size and color.
	 *
	 * @param filename the name of the icon
	 * @param size     the size of the icon
	 * @param color    the color of the icon
	 * @return an {@link ImageIcon} of the image with the specified name
	 * @throws IOException if an I/O error occurs
	 */
	public static ImageIcon getIcon(String filename, int size, Color color) throws IOException {
		BufferedImage im = loadImage(filename);
		if (im == null) return null;

		im = Util.colorImage(im, color);
		return new ImageIcon(Scalr.resize(im, size));
	}

	/**
	 * Returns the icon, created from an image with the specified name
	 * and of specified size.
	 *
	 * @param filename the name of the icon
	 * @param size     the size of the icon
	 * @return an {@link ImageIcon} of the image with the specified name
	 * @throws IOException if an I/O error occurs
	 */
	public static ImageIcon getIcon(String filename, int size) throws IOException {
		BufferedImage im = loadImage(filename);
		if (im == null) return null;

		return new ImageIcon(Scalr.resize(im, size));
	}

	/**
	 * Reads the image from the provided path and returns it as a
	 * buffered image.
	 *
	 * @param filename a path to the image file
	 * @return an image, or {@code null} if the image could not have
	 * been read
	 * @throws IOException if an I/O error occurs
	 */
	public static BufferedImage loadImage(String filename) throws IOException {
		URL url = classloader.getResource(filename);
		return url != null ? ImageIO.read(url) : null;
	}

	/**
	 * Changes the color for the given image and returns a new image, the
	 * same as the old one, but with it's color changed to {@param c}.
	 *
	 * @param image the image to color
	 * @param c     the color
	 * @return the specified image but with the changed color
	 */
	public static BufferedImage colorImage(BufferedImage image, Color c) {
		int width = image.getWidth();
		int height = image.getHeight();
		WritableRaster raster = image.getRaster();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int[] pixels = raster.getPixel(i, j, (int[]) null);
				pixels[0] = c.getRed();
				pixels[1] = c.getGreen();
				pixels[2] = c.getBlue();
				raster.setPixel(i, j, pixels);
			}
		}
		return image;
	}

	/**
	 * Returns the URL of the resource specified by the given filename.
	 *
	 * @param filename the filename
	 * @return the URL of the given resource
	 */
	public static URL getResourceURL(String filename) {
		return classloader.getResource(filename);
	}

	/**
	 * Displays a generic error message and terminates the application.
	 */
	public static void displayErrorDialog() {
		displayErrorDialog(
				"Sadly, an error has occurred. Please restart the application.",
				"Sorry :("
		);
		System.exit(1);
	}

	/**
	 * Displays a specified error message.
	 */
	public static void displayErrorDialog(String message, String title) {
		JOptionPane.showMessageDialog(Main.getMainFrame(), title, message,
				JOptionPane.ERROR_MESSAGE
		);
	}

	/**
	 * Opens the webpage denoted by the specified URL in the default
	 * web browser.
	 *
	 * @param url the URL of the web page do open
	 */
	public static void openWebpage(URL url) {
		try {
			openWebpage(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens the webpage denoted by the specified URI in the default
	 * web browser.
	 *
	 * @param uri the URI of the web page do open
	 */
	private static void openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
