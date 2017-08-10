import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.util.Scanner;

/**
 * A simple simulator of the Buffon's needle problem.
 */
public class MainFrame extends JFrame {

	/**
	 * Width and height of the main window.
	 */
	private static final int SIZE = 600;

	/**
	 * The drawing canvas.
	 */
	private static Canvas canvas;

	/**
	 * A landed needle thrownCounter.
	 */
	private int countLanded = 0;

	/**
	 * The total number of needles thrown.
	 */
	private JLabel thrownCounter = new JLabel("Thrown count: 0");

	/**
	 * The number of needles landed on a line.
	 */
	private JLabel landedCounter = new JLabel("Hit count: 0");

	/**
	 * The default constructor.
	 */
	public MainFrame() {
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
		SwingUtilities.invokeLater(MainFrame::new);

		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			String str = sc.nextLine();

			if ("pause".equals(str.toLowerCase())) {
				canvas.pause();
			} else if ("stop".equals(str.toLowerCase())) {
				canvas.stop();
			} else if ("start".equals(str.toLowerCase())) {
				canvas.start();
			}
		}
	}

	/**
	 * Initializes the graphical interface of this frame.
	 */
	private void initGUI() {
		setLayout(new BorderLayout());

		canvas = new Canvas();
		add(canvas, BorderLayout.CENTER);

		canvas.start();
	}
}