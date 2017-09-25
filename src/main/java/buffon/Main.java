package buffon;

import buffon.components.canvas.Canvas;
import buffon.components.canvas.update.IUpdateListener;
import buffon.components.canvas.update.IUpdateProvider;
import buffon.components.controls.Controls;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

/**
 * A simple simulator of the Buffon's needle problem.
 */
public class Main extends JFrame implements IUpdateListener {

	/**
	 * Width and height of the main window.
	 */
	private static final int SIZE = 600;

	/**
	 * The drawing canvas.
	 */
	private static Canvas canvas = new Canvas();

	/**
	 * The controls panel.
	 */
	private static Controls controls = new Controls();

	private static JFrame mainFrame;

	/**
	 * The prefix text for the 'thrown count' value.
	 */
	private String thrownCountPrefix = "Thrown: ";

	/**
	 * The prefix text for the 'hit count' value.
	 */
	private String hitCountPrefix = "Hit: ";

	/**
	 * The prefix text for the 'pi' value.
	 */
	private String piPrefix = "π ≈ ";

	/**
	 * The text for displaying the 'thrown count' value.
	 */
	private String thrownCount = thrownCountPrefix + "0";

	/**
	 * The text for displaying the 'hit count' value.
	 */
	private String hitCount = hitCountPrefix + "0";

	/**
	 * The text for displaying the 'pi' value.
	 */
	private String pi = piPrefix + "-";

	private JLabel valuesLabel = new JLabel(thrownCount + " | " + hitCount);

	private JLabel piLabel = new JLabel(pi);

	public static final double version = 2.0;

	/**
	 * The default constructor.
	 */
	public Main() {
		mainFrame = this;
		setSize(SIZE, SIZE);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		setTitle("Buffon's Needle Simulator v" + version);

		initGUI();
	}

	/**
	 * The main method.
	 *
	 * @param args command line arguments; not used in this program
	 */
	public static void main(String args[]) {
		SwingUtilities.invokeLater(Main::new);
	}

	/**
	 * Initializes the graphical interface of this frame.
	 */
	private void initGUI() {
		setLayout(new BorderLayout());

		canvas.addUpdateListener(this);
		controls.setCanvas(canvas);

		add(createLabelsPanel(), BorderLayout.PAGE_START);
		add(canvas, BorderLayout.CENTER);

		add(controls, BorderLayout.LINE_END);
	}

	/**
	 * Creates a panel which holds the simulator values.
	 *
	 * @return a panel holding the simulator values
	 */
	private JPanel createLabelsPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		panel.add(valuesLabel);
		panel.add(piLabel);

		valuesLabel.setHorizontalAlignment(SwingConstants.LEFT);
		piLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		return panel;
	}

	// IUpdateListener methods

	@Override
	public void update(IUpdateProvider provider) {
		Canvas c = (Canvas) provider; // the provider is always the canvas

		String thrownCountText = thrownCountPrefix + c.getThrownCount();
		String hitCountText = hitCountPrefix + c.getHitCount();
		valuesLabel.setText(thrownCountText + " | " + hitCountText);
		piLabel.setText(piPrefix + c.getPI());
	}

	// Getters


	public static Canvas getCanvas() {
		return canvas;
	}

	public static JFrame getMainFrame() {
		return mainFrame;
	}
}