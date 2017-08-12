package chupox.buffon.components;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
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

		settingsButton = new ImageButton(settingsIcon, settingsIcon, "Settings", "Settings");
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
