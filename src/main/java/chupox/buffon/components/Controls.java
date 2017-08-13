package chupox.buffon.components;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;

public class Controls extends JPanel {

	private ClassLoader classloader = Thread.currentThread().getContextClassLoader();

	private Canvas canvas;

	private ImageButton playButton;

	private ImageButton stopButton;

	private ImageButton settingsButton;

	private GridBagConstraints c = new GridBagConstraints();

	public Controls() {
		try {
			initGUI();
		} catch (IOException ex) {
			System.out.println("FATAL ERROR!");
			System.exit(1);
		}
	}

	private void initGUI() throws IOException {
		setLayout(new GridBagLayout());

		JPanel controlsPanel = createControlsPanel();
		changeConstraints(0, 0, new Insets(0, 0, 0, 0));
		add(controlsPanel, c);

		JPanel speedPanel = createSpeedPanel();
		changeConstraints(0, 1, new Insets(0, 0, 0, 0));
		add(speedPanel, c);

		JPanel settingsPanel = createSettingsPanel();
		changeConstraints(0, 2, new Insets(0, 0, 0, 0));
		add(settingsPanel, c);
	}

	private JPanel createSettingsPanel() throws IOException {
		JPanel settingsPanel = new JPanel();
		c = new GridBagConstraints();

		settingsButton = createSettingsButton();
		settingsPanel.add(settingsButton);

		return settingsPanel;
	}

	private ImageButton createSettingsButton() throws IOException {
		ImageIcon settingsIcon = new ImageIcon(loadImage("icons/settings.png"));

		settingsButton = new ImageButton(settingsIcon, "Settings");
		stopButton.addActionListener(e -> {
			// Show settings window
		});
		return settingsButton;
	}

	private JPanel createSpeedPanel() {
		JPanel speedPanel = new JPanel();
		c = new GridBagConstraints();

		JSlider slider = createSlider();
		changeConstraints(0, 2, new Insets(10, 0, 10, 0));
		c.weighty = 1;
		speedPanel.add(slider, c);

		return speedPanel;
	}

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

	private JSlider createSlider() {
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		slider.addChangeListener(e -> canvas.setSpeed((double) slider.getValue() / 100));

		slider.setValue(50); // set the value to trigger the canvas' setSpeed() method
		slider.setBorder(new TitledBorder("Speed"));

		Hashtable<Integer, Component> labelTable = new Hashtable<>();
		labelTable.put(slider.getMinimum(), new JLabel("Slow"));
		labelTable.put(slider.getMaximum(), new JLabel("Fast"));
		slider.setLabelTable(labelTable);

		slider.setPreferredSize(new Dimension(100, 30));

		return slider;
	}

	private void changeConstraints(int gridx, int gridy, Insets insets) {
		c.gridx = gridx;
		c.gridy = gridy;
		c.insets = insets;
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
	 * Represents a swing button with an easier way of adding an icon to
	 * the button.
	 * <p>
	 * Each button can be either "flippable" or "non-flippable". The former
	 * means that the button contains two states, such as a "Play/Pause"
	 * button. The latter refers to a regular button with a single purpose.
	 */
	private class ImageButton extends JButton {

		/**
		 * The icon for a default button.
		 */
		private ImageIcon defaultIcon;

		/**
		 * The icon for a selected button.
		 */
		private ImageIcon selectedIcon;

		/**
		 * The tooltip text for a default button.
		 */
		private String defaultTooltip;

		/**
		 * The tooltip text for a selected button.
		 */
		private String selectedTooltip;

		/**
		 * The default, empty border for each button.
		 */
		private Border border = BorderFactory.createEmptyBorder();

		/**
		 * The border for a hovered button.
		 */
		private Border hoveredBorder = BorderFactory.createEtchedBorder();

		/**
		 * A flag which tells the current state of the button. This field
		 * is always true for a non-flippable button.
		 */
		private boolean isDefault = true;

		/**
		 * A flag which tells whether or not the button is "flippable", i.e.
		 * the number of states the button has.
		 */
		private boolean isFlippable = true;

		/**
		 * Creates a new {@link ImageButton} with the given icon and tooltip
		 * text.
		 *
		 * @param icon    the icon
		 * @param tooltip the tooltip text
		 */
		public ImageButton(ImageIcon icon, String tooltip) {
			this(icon, null, tooltip, null);
			isFlippable = false;
		}

		/**
		 * Creates a new "flippable" button. The initial looks of the button will
		 * be created with the given {@param defaultIcon} and {@param defaultTooltip}.
		 * When the button is clicked, the button "flips" into the "selected" state
		 * where new looks will be given to the button - the {@param selectedIcon} and
		 * {@param selectedTooltip}.
		 * <p>
		 * The action for the states are manually set; using the {@link #isDefault} can
		 * be helpful for knowing which state the button is currently at.
		 *
		 * @param defaultIcon     default icon
		 * @param selectedIcon    selected icon
		 * @param defaultTooltip  default tooltip text
		 * @param selectedTooltip selected tooltip text
		 */
		public ImageButton(ImageIcon defaultIcon, ImageIcon selectedIcon, String defaultTooltip,
		                   String selectedTooltip) {
			super();
			this.defaultIcon = defaultIcon;
			this.selectedIcon = selectedIcon;
			this.defaultTooltip = defaultTooltip;
			this.selectedTooltip = selectedTooltip;

			init();
		}

		/**
		 * Initializes the button settings and GUI.
		 */
		private void init() {
			setIcon(defaultIcon);
			setToolTipText(defaultTooltip);
			setBorder(border);
			setContentAreaFilled(false);

			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (!isFlippable) return;
					flip();
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					setBorder(hoveredBorder);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					setBorder(border);
				}
			});
		}

		/**
		 * Inverts the purpose of the button (but only if the button is
		 * declared as "flippable" as in {@link #isFlippable}. For example:
		 * The "Play/Pause" button has two states/purposes, hence that button
		 * will be declared as "flippable".
		 */
		private void flip() {
			setToolTipText(isDefault ? selectedTooltip : defaultTooltip);
			setIcon(isDefault ? selectedIcon : defaultIcon);
			isDefault = !isDefault;
		}

		/**
		 * Checks if the button is in it's default (initial) mode.
		 * The method always returns true for non-flippable buttons.
		 *
		 * @return true if the button is in it's default mode; false
		 * otherwise
		 */
		public boolean isDefault() {
			return isDefault;
		}
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
