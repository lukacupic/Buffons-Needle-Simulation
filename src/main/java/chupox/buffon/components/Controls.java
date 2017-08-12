package chupox.buffon.components;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Controls extends JPanel {

	private ClassLoader classloader = Thread.currentThread().getContextClassLoader();

	private Canvas canvas;

	private ImageButton playButton;

	private ImageButton stopButton;

	public Controls() {
		try {
			initGUI();
		} catch (IOException ex) {
			System.out.println("FATAL ERROR!");
			System.exit(1);
		}
	}

	private void initGUI() throws IOException {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		playButton = createPlayButton();
		stopButton = createStopButton();

		add(playButton);
		add(stopButton);
	}

	private ImageButton createPlayButton() throws IOException {
		ImageIcon playIcon = new ImageIcon(loadImage("icons/play.png"));
		ImageIcon pauseIcon = new ImageIcon(loadImage("icons/pause.png"));

		ImageButton playButton = new ImageButton(playIcon, pauseIcon, "Play", "Pause");
		playButton.addActionListener(e -> {
			if (!playButton.isDefault()) canvas.play();
			else canvas.pause();
		});
		playButton.setBorder(BorderFactory.createCompoundBorder());

		return playButton;
	}

	private ImageButton createStopButton() throws IOException {
		ImageIcon stopIcon = new ImageIcon(loadImage("icons/stop.png"));

		ImageButton stopButton = new ImageButton(stopIcon, stopIcon, "Stop", "Stop");
		stopButton.addActionListener(e -> {
			canvas.stop();
			if (!playButton.isDefault()) {
				playButton.flip();
			}
		});

		return stopButton;
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
	private BufferedImage loadImage(String filename) throws IOException {
		URL url = classloader.getResource(filename);
		return url != null ? ImageIO.read(url) : null;
	}

	private class ImageButton extends JButton {

		private ImageIcon defaultIcon;

		private ImageIcon selectedIcon;

		private String defaultTooltip;

		private String selectedTooltip;

		private boolean isDefault = true;

		public ImageButton(ImageIcon defaultIcon, ImageIcon selectedIcon, String defaultTooltip,
		                   String selectedTooltip) throws IOException {
			super();
			this.defaultIcon = defaultIcon;
			this.selectedIcon = selectedIcon;
			this.defaultTooltip = defaultTooltip;
			this.selectedTooltip = selectedTooltip;

			setIcon(defaultIcon);
			setToolTipText(defaultTooltip);
			setBorder(BorderFactory.createEmptyBorder());
			setContentAreaFilled(false);

			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					flip();
				}
			});
		}

		private void flip() {
			ImageButton ib = ImageButton.this;
			ib.setToolTipText(isDefault() ? ib.selectedTooltip : ib.defaultTooltip);
			ib.setIcon(isDefault() ? ib.selectedIcon : ib.defaultIcon);
			isDefault = !isDefault;
		}

		public boolean isDefault() {
			return isDefault;
		}
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
}
