package chupox.buffon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Util {

	/**
	 * Changes the color for the given image and returns a new image, the
	 * same as the old one, but with it's color changed to {@param c}.
	 *
	 * @param image the image to color
	 * @param c     the color
	 * @return the specified image but with the changed color
	 */
	private static BufferedImage colorImage(BufferedImage image, Color c) {
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
}
