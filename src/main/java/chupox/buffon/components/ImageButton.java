package chupox.buffon.components;

import chupox.buffon.util.Util;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Represents a swing button with an easier way of adding an icon to
 * the button.
 * <p>
 * Each button can be either "flippable" or "non-flippable". The former
 * means that the button contains two states, such as a "Play/Pause"
 * button. The latter refers to a regular button with a single purpose.
 */
public class ImageButton extends JButton {

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

	private Runnable defaultAction;

	private Runnable selectedAction;

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
	public ImageButton(ImageIcon icon, String tooltip, Runnable defaultAction) {
		this(icon, null, tooltip, null, defaultAction, null);
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
	                   String selectedTooltip, Runnable defaultAction, Runnable selectedAction) {
		super();
		this.defaultIcon = defaultIcon;
		this.selectedIcon = selectedIcon;
		this.defaultTooltip = defaultTooltip;
		this.selectedTooltip = selectedTooltip;
		this.defaultAction = defaultAction;
		this.selectedAction = selectedAction;
		this.setFocusable(false);

		init();
	}

	/**
	 * Initializes the button settings and GUI.
	 */
	private void init() {
		setIcon(defaultIcon);
		setToolTipText(defaultTooltip);
		setBorder(BorderFactory.createEmptyBorder());
		setContentAreaFilled(false);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (isDefault) defaultAction.run();
				else selectedAction.run();

				if (!isFlippable) return;
				flip();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				BufferedImage im = (BufferedImage) ((ImageIcon) getIcon()).getImage();
				setIcon(new ImageIcon(Util.colorImage(im, Controls.DEFAULT_ICON_COLOR.darker())));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				BufferedImage im = (BufferedImage) ((ImageIcon) getIcon()).getImage();
				setIcon(new ImageIcon(Util.colorImage(im, Controls.DEFAULT_ICON_COLOR)));
			}
		});
	}

	/**
	 * Inverts the purpose of the button (but only if the button is
	 * declared as "flippable" as in {@link #isFlippable}. For example:
	 * The "Play/Pause" button has two states/purposes, hence that button
	 * will be declared as "flippable".
	 */
	public void flip() {
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