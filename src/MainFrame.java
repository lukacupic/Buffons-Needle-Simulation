import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple simulator of the Buffon's needle problem.
 *
 * @author Luka Čupić
 */
public class MainFrame extends JFrame {

	/**
	 * Width and height of the main window.
	 */
	private static final int SIZE = 600;

	/**
	 * The length of the needle. If set to -1, the length
	 * will be initialized to the distance between the lines.
	 */
	private static int LENGTH = -1;

	/**
	 * The default number of lines. If set to -1, a default value
	 * of 5 will be set.
	 */
	private static int LINES_NUMBER = 10;

	/**
	 * Random number generator.
	 */
	private Random rand = new Random();

	/**
	 * A list of needles.
	 */
	private List<Needle> list = new ArrayList<>();

	/**
	 * A needle thrownCounter.
	 */
	private int countNeedles = 0;

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
	 * The distance between the lines.
	 */
	private int distance;

	/**
	 * The drawing canvas.
	 */
	private JPanel canvas;

	/**
	 * The boolean initializer for the canvas.
	 */
	private boolean initCanvas = true;

	/**
	 * The coordinates to check against when a needle is thrown.
	 */
	private List<Integer> lineCoordinates;

	/**
	 * The approximation of PI.
	 */
	private JLabel piLabel = new JLabel("PI: -");

	/**
	 * The default constructor.
	 */
	public MainFrame() {
		setSize(SIZE, SIZE);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		setTitle("Buffon's Needle Simulator v1.0");

		initGUI();
	}

	/**
	 * The main method.
	 *
	 * @param args command line arguments; not used in this program
	 */
	public static void main(String args[]) {
		SwingUtilities.invokeLater(MainFrame::new);
	}

	/**
	 * Initializes the graphical interface of this frame.
	 */
	private void initGUI() {
		setLayout(new BorderLayout());

		// create the drawing canvas
		canvas = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paintComponent(g);
				paintCanvas(g);
			}
		};

		// create the drawing timer
		Timer timer = new Timer(18, e -> canvas.repaint());

		// create the animation button
		JButton button = new JButton("Start");
		button.addActionListener(l -> {
			if (!timer.isRunning()) {
				timer.start();
				button.setText("Pause");
			} else {
				timer.stop();
				button.setText("Start");
			}
		});

		// create the toolbar
		JPanel toolbar = new JPanel();
		toolbar.setLayout(new GridLayout());

		// add button and counting label to the toolbar
		toolbar.add(button);
		toolbar.add(thrownCounter);
		toolbar.add(landedCounter);
		toolbar.add(piLabel);

		thrownCounter.setHorizontalAlignment(SwingConstants.CENTER);
		landedCounter.setHorizontalAlignment(SwingConstants.CENTER);
		piLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// add toolbar and canvas to the frame
		add(toolbar, BorderLayout.PAGE_START);
		add(canvas, BorderLayout.CENTER);
	}

	/**
	 * Paints the dropped needles on to the drawing canvas.
	 *
	 * @param g the graphics object to paint upon
	 */
	private void paintCanvas(Graphics g) {

		// set-up the canvas
		setupCanvas(g);

		// if this is the first call, don't draw needles yet
		if (initCanvas) {
			initCanvas = false;
			return;
		}

		// randomly choose a point on the plane
		int x = Math.abs(rand.nextInt() % canvas.getWidth());
		int y = Math.abs(rand.nextInt() % canvas.getHeight());

		// randomly choose the angle of the needle
		double angle = Math.abs(rand.nextInt() % (2 * Math.PI));

		// calculate the x and y lengths to the points
		double xLen = LENGTH / 2 * Math.cos(angle);
		double yLen = LENGTH / 2 * Math.sin(angle);

		// get the points to form a needle
		int x1 = x - (int) xLen;
		int y1 = y - (int) yLen;
		int x2 = x + (int) xLen;
		int y2 = y + (int) yLen;

		int red = Math.abs(rand.nextInt()) % 256;
		int green = Math.abs(rand.nextInt()) % 256;
		int blue = Math.abs(rand.nextInt()) % 256;

		Needle needle = new Needle(x1, y1, x2, y2, new Color(red, green, blue));
		list.add(needle);

		// did it land on one of the lines ??
		if (landedOnLine(needle)) {
			landedCounter.setText(String.format("Hit count: %d", ++countLanded));
			updatePI();
		}

		// draw the lines
		for (Needle n : list) {
			((Graphics2D) g).setStroke(new BasicStroke(1.8f));
			g.setColor(n.color);
			g.drawLine(n.x1, n.y1, n.x2, n.y2);
		}

		thrownCounter.setText(String.format("Thrown count: %d", ++countNeedles));
	}

	/**
	 * Updates the approximated value of PI.
	 */
	private void updatePI() {
		double pi = 2 * (double) LENGTH / distance * (double) countNeedles / countLanded;

		piLabel.setText(String.format("PI: %f", pi));
	}

	/**
	 * Checks whether the dropped needle has landed on a line.
	 *
	 * @param n the needle to check
	 * @return true if the given needle has landed on a line;
	 * false otherwise
	 */
	private boolean landedOnLine(Needle n) {
		int x1 = n.x1;
		int x2 = n.x2;

		for (int x : lineCoordinates) {
			// whether x1 or x2 is smaller, check if the x is in between x1 and x2
			boolean landedOnCurrentLine = x1 < x2 ? x1 <= x && x <= x2 : x2 <= x && x <= x1; // :)

			if (landedOnCurrentLine) return true;
		}
		return false;
	}

	/**
	 * Sets-up the canvas, by drawing the lines on top
	 * of it
	 *
	 * @param g the graphics object to paint upon
	 */
	private void setupCanvas(Graphics g) {
		if (lineCoordinates == null) {
			lineCoordinates = new ArrayList<>();
		}


		if (LINES_NUMBER < 0) {
			LINES_NUMBER = 5;
		}

		g.setColor(Color.BLACK);

		drawLines(g, LINES_NUMBER);
	}

	/**
	 * Draws a given number of lines on the given graphics object.
	 *
	 * @param g the graphics object to paint upon
	 * @param n the number of lines to draw
	 */
	private void drawLines(Graphics g, int n) {
		if (n < 2) throw new IllegalArgumentException();

		distance = canvas.getWidth() / (n - 1);

		if (LENGTH < 0) LENGTH = (int) (0.5 * distance);

		int currentDistance = 0;
		for (int i = 0; i < n; i++) {
			// add the x-coordinate of the line to the list
			lineCoordinates.add(currentDistance);

			g.drawLine(currentDistance, 0, currentDistance, canvas.getHeight());
			currentDistance += distance;
		}
	}

	/**
	 * Represents a single needle object, which will be thrown on
	 * to the canvas.
	 *
	 * @author Luka Čupić
	 */
	private class Needle {
		int x1;
		int y1;
		int x2;
		int y2;
		Color color;

		/**
		 * Creates an a new needle instance.
		 *
		 * @param x1    x-coordinate of the first point
		 * @param y1    y-coordinate of the first point
		 * @param x2    x-coordinate of the second point
		 * @param y2    y-coordinate of the second point
		 * @param color the color of the needle
		 */
		Needle(int x1, int y1, int x2, int y2, Color color) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.color = color;
		}
	}
}