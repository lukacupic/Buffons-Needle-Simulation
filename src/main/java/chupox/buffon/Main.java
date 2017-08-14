package chupox.buffon;

import chupox.buffon.components.canvas.Canvas;
import chupox.buffon.components.canvas.update.IUpdateListener;
import chupox.buffon.components.canvas.update.IUpdateProvider;
import chupox.buffon.components.controls.Controls;

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
	 * The label for displaying the 'thrown count' value.
	 */
	private JLabel thrownCountLabel = new JLabel(thrownCountPrefix + "0");

	/**
	 * The label for displaying the 'hit count' value.
	 */
	private JLabel hitCountLabel = new JLabel(hitCountPrefix + "0");

	/**
	 * The label for displaying the 'pi' value.
	 */
	private JLabel piLabel = new JLabel(piPrefix + "-");

	/**
	 * The default constructor.
	 */
	public Main() {
		setSize(SIZE, SIZE);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		setTitle("Buffon's Needle Simulator v2.0");

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
		JPanel panel = new JPanel(new GridLayout(1, 3));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		panel.add(thrownCountLabel);
		panel.add(hitCountLabel);
		panel.add(piLabel);

		thrownCountLabel.setHorizontalAlignment(SwingConstants.LEFT);
		hitCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
		piLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		return panel;
	}

	// IUpdateListener methods

	@Override
	public void update(IUpdateProvider provider) {
		Canvas c = (Canvas) provider; // the provider is always the canvas

		thrownCountLabel.setText(thrownCountPrefix + c.getThrownCount());
		hitCountLabel.setText(hitCountPrefix + c.getHitCount());
		piLabel.setText(piPrefix + c.getPI());
	}
}