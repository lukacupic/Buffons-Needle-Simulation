package chupox.buffon.components;

import chupox.buffon.util.Util;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;

public class Controls extends JPanel {

	/**
	 * The default size for the icons.
	 */
	private static final int DEFAULT_ICON_SIZE = 45;

	/**
	 * The default color for the icons.
	 */
	private static final Color DEFAULT_ICON_COLOR = new Color(40, 77, 135);

	/**
	 * The class loader for loading the image resources.
	 */
	private ClassLoader classloader = Thread.currentThread().getContextClassLoader();

	/**
	 * The drawing canvas.
	 */
	private Canvas canvas;

	/**
	 * The play/pause button.
	 */
	private ImageButton playButton;

	/**
	 * The stop button.
	 */
	private ImageButton stopButton;

	/**
	 * The settings button.
	 */
	private ImageButton settingsButton;

	/**
	 * The constraints object for the {@link GridBagLayout}.
	 */
	private GridBagConstraints c = new GridBagConstraints();

	/**
	 * Creates a new instance of this class.
	 *
	 * @throws RuntimeException if an error occurs while instantiating
	 *                          the object
	 */
	public Controls() throws RuntimeException {
		try {
			initGUI();
		} catch (IOException ex) {
			throw new RuntimeException();
		}
	}

	/**
	 * Initializes the GUI.
	 *
	 * @throws IOException if an I/O error occurs
	 */
	private void initGUI() throws IOException {
		setLayout(new GridBagLayout());

		JPanel controlsPanel = createControlsPanel();
		changeConstraints(0, 0, new Insets(10, 10, 10, 10));
		add(controlsPanel, c);

		JPanel speedPanel = createSpeedPanel();
		changeConstraints(0, 1, new Insets(10, 0, 10, 0));
		add(speedPanel, c);

		JPanel settingsPanel = createSettingsPanel();
		changeConstraints(0, 2, new Insets(10, 10, 10, 10));
		add(settingsPanel, c);
	}

	/**
	 * Creates the panel holding the animation control buttons:
	 * {@link #playButton} and {@link #stopButton}.
	 *
	 * @return the panel holding the animation control buttons
	 * @throws IOException if an I/O error occurs
	 */
	private JPanel createControlsPanel() throws IOException {
		JPanel controls = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();

		playButton = createPlayButton();
		changeConstraints(0, 0, new Insets(10, 10, 10, 10));
		controls.add(playButton, c);

		stopButton = createStopButton();
		changeConstraints(0, 1, new Insets(10, 10, 10, 10));
		controls.add(stopButton, c);

		return controls;
	}

	/**
	 * Creates the {@link #playButton}.
	 *
	 * @return the {@link #playButton}
	 * @throws IOException if an I/O error occurs
	 */
	private ImageButton createPlayButton() throws IOException {
		ImageIcon playIcon = getIcon("icons/play.png");
		ImageIcon pauseIcon = getIcon("icons/pause.png");

		ImageButton playButton = new ImageButton(playIcon, pauseIcon, "Play", "Pause");
		playButton.addActionListener(e -> {
			if (!playButton.isDefault()) canvas.play();
			else canvas.pause();
		});
		return playButton;
	}

	/**
	 * Creates the {@link #stopButton}.
	 *
	 * @return the {@link #stopButton}
	 * @throws IOException if an I/O error occurs
	 */
	private ImageButton createStopButton() throws IOException {
		ImageIcon stopIcon = getIcon("icons/stop.png");

		ImageButton stopButton = new ImageButton(stopIcon, "Stop");
		stopButton.addActionListener(e -> {
			canvas.stop();
			if (!playButton.isDefault()) {
				playButton.flip();
			}
		});
		return stopButton;
	}

	/**
	 * Creates the panel holding the speed control slider.
	 *
	 * @return the panel holding the speed control slider
	 * @throws IOException if an I/O error occurs
	 */
	private JPanel createSpeedPanel() {
		JPanel speedPanel = new JPanel();
		c = new GridBagConstraints();

		JSlider slider = createSlider();
		changeConstraints(0, 2, new Insets(10, 0, 10, 0));
		c.weighty = 1;
		speedPanel.add(slider, c);

		return speedPanel;
	}

	/**
	 * Creates the slider for controlling the animation speed.
	 *
	 * @return the slider which controls the animation speed
	 */
	private JSlider createSlider() {
		int defValue = 50;
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, defValue);
		slider.addChangeListener(e -> canvas.setSpeed((double) slider.getValue() / 100));

		slider.setValue(defValue); // set the value to trigger the canvas' setSpeed() method
		slider.setBorder(new TitledBorder("Speed"));

		Hashtable<Integer, Component> labelTable = new Hashtable<>();
		labelTable.put(slider.getMinimum(), new JLabel("Slow"));
		labelTable.put(slider.getMaximum(), new JLabel("Fast"));
		slider.setLabelTable(labelTable);

		slider.setPreferredSize(new Dimension(100, 30));

		return slider;
	}

	/**
	 * Creates the panel holding the settings button.
	 *
	 * @return the panel holding the settings button
	 * @throws IOException if an I/O error occurs
	 */
	private JPanel createSettingsPanel() throws IOException {
		JPanel settingsPanel = new JPanel();
		c = new GridBagConstraints();

		settingsButton = createSettingsButton();
		settingsPanel.add(settingsButton);

		return settingsPanel;
	}


	/**
	 * Creates the {@link #settingsButton}.
	 *
	 * @return the {@link #settingsButton}
	 * @throws IOException if an I/O error occurs
	 */
	private ImageButton createSettingsButton() throws IOException {
		ImageIcon settingsIcon = getIcon("icons/settings.png");

		settingsButton = new ImageButton(settingsIcon, "Settings");
		stopButton.addActionListener(e -> {
			// Show settings window
		});
		return settingsButton;
	}

	/**
	 * Changes the specified attributes of the {@link #c} object.
	 *
	 * @param gridx  the new column of the {@link GridBagLayout}
	 * @param gridy  the new row of the {@link GridBagLayout}
	 * @param insets the new external padding
	 * @see GridBagConstraints
	 */
	private void changeConstraints(int gridx, int gridy, Insets insets) {
		c.gridx = gridx;
		c.gridy = gridy;
		c.insets = insets;
	}

	/**
	 * Returns the icon, created from an image with the specified name.
	 * The size of the icon is specified with {@link #DEFAULT_ICON_SIZE},
	 * and the color with {@link #DEFAULT_ICON_COLOR}.
	 *
	 * @param filename the name of the image
	 * @return an {@link ImageIcon} of the image with the specified name
	 * @throws IOException if an I/O error occurs
	 */
	private ImageIcon getIcon(String filename) throws IOException {
		return getIcon(filename, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
	}

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
	private ImageIcon getIcon(String filename, int size, Color color) throws IOException {
		BufferedImage im = loadImage(filename);
		if (im == null) return null;

		im = Util.colorImage(im, color);
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
	private BufferedImage loadImage(String filename) throws IOException {
		URL url = classloader.getResource(filename);
		return url != null ? ImageIO.read(url) : null;
	}

	/**
	 * Sets the given canvas as the currently active canvas.
	 *
	 * @param canvas the canvas
	 */
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
}
