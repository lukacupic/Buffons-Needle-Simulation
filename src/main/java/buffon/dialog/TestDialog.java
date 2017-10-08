package buffon.dialog;

import buffon.Main;
import buffon.components.canvas.Canvas;
import buffon.dialog.options.AbstractOption;
import buffon.dialog.options.DoubleSpinnerOption;
import buffon.dialog.options.IntSpinnerOption;
import buffon.dialog.options.ObjectWrapper;
import buffon.dialog.options.OptionsProvider;
import buffon.util.SpringUtilities;
import buffon.util.Util;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TestDialog extends JDialog {
	private static Map<String, ObjectWrapper> optionsOld = new HashMap<>();

	static {
		for (Map.Entry<String, ObjectWrapper> entry : OptionsProvider.getOptions().entrySet()) {
			optionsOld.put(entry.getKey(), new ObjectWrapper(entry.getValue().getValue()));
		}
	}

	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JList list;
	private JPanel cardPanel;
	private JPanel settingsPanel;
	private JPanel aboutPanel;
	private JPanel warningPanel;

	public TestDialog() {
		try {
			initGUI();
		} catch (IOException e) {
			Util.displayErrorDialog();
		}
	}

	private void initGUI() throws IOException {
		setResizable(false);
		setTitle("Preferences");
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(e -> onOK());

		buttonCancel.addActionListener(e -> onCancel());

		list.setSelectedIndex(0);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		list.addListSelectionListener(e -> {
			if (e.getValueIsAdjusting()) return;
			String selectedCard = (String) ((JList) e.getSource()).getSelectedValue();
			((CardLayout) cardPanel.getLayout()).show(cardPanel, selectedCard);
		});

		initSettingsPanel();
		initWarningPanel();
		initAboutPanel();
	}

	private void initSettingsPanel() {
		settingsPanel.setLayout(new BorderLayout());

		// init object wrappers
		ObjectWrapper wrapper = OptionsProvider.getOption(OptionsProvider.NUMBER_OF_LINES);
		IntSpinnerOption noOfLines = new IntSpinnerOption("Number of strips: ",
				IntSpinnerOption.createJSpinner((int) wrapper.getValue(), 2, 15, 1),
				wrapper
		);

		wrapper = OptionsProvider.getOption(OptionsProvider.LENGTH_FACTOR);
		DoubleSpinnerOption needleLength = new DoubleSpinnerOption("Needle length factor: ",
				DoubleSpinnerOption.createJSpinner(((double) wrapper.getValue()), 0.00, 1.00, 0.01),
				wrapper
		);

		wrapper = OptionsProvider.getOption(OptionsProvider.NUMBER_OF_DIGITS);
		IntSpinnerOption noOfDigits = new IntSpinnerOption("Number of π digits: ",
				IntSpinnerOption.createJSpinner((int) wrapper.getValue(), 0, 6, 1),
				wrapper
		);

		JPanel panel = createOptionsPanel(noOfLines, needleLength, noOfDigits);
		settingsPanel.add(panel, BorderLayout.NORTH);


		// TODO: Init reset button
		/*
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(l -> {
			try {
				OptionsProvider.getPrefs().clear();
			} catch (BackingStoreException ignorable) {
			}
		});

		JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		resetPanel.add(resetButton);

		settingsPanel.add(resetPanel, BorderLayout.SOUTH);
		*/
	}

	private JPanel createOptionsPanel(AbstractOption... options) {
		JPanel p = new JPanel();
		p.setLayout(new SpringLayout());
		for (AbstractOption option : options) {
			JLabel l = new JLabel(option.getName(), JLabel.TRAILING);
			p.add(l);

			l.setLabelFor(option.getComponent());
			p.add(option.getComponent());
		}
		SpringUtilities.makeCompactGrid(p, options.length, 2, 6, 6, 6, 6);
		return p;
	}

	private void onOK() {
		Canvas c = Main.getCanvas();
		c.resetContext();
		c.calculateNeedleLength();
		dispose();

		try {
			OptionsProvider.saveOptions();
		} catch (IOException e) {
			Util.displayErrorDialog();
		}
	}

	private void onCancel() {
		OptionsProvider.setDefaultOptions(optionsOld);
		dispose();
	}

	private void initWarningPanel() {
		warningPanel.setLayout(new BorderLayout());

		Icon icon = UIManager.getIcon("OptionPane.warningIcon");
		JLabel label = new JLabel("Changing the settings will cause the simulation to reset!", icon, JLabel.CENTER);

		warningPanel.add(label, BorderLayout.SOUTH);
	}

	private void initAboutPanel() throws IOException {
		aboutPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		aboutPanel.add(new JLabel(Util.getIcon("icons/logo.png", 50), JLabel.CENTER), c);

		c.gridy = 1;
		c.insets = new Insets(10, 0, 0, 0);
		JLabel label = new JLabel("Buffon's Needle Simulator v" + Main.version);
		label.setFont(new Font("default", Font.BOLD, 16));
		aboutPanel.add(label, c);

		JPanel author = new JPanel(new FlowLayout(FlowLayout.LEFT));
		author.add(new JLabel("Coded by"));

		String text = "<html><font color=\"#000000\"><u>Luka Čupić</u></font></html>";
		JButton authorButton = new JButton(text);
		authorButton.setBorder(BorderFactory.createEmptyBorder());
		authorButton.setContentAreaFilled(false);
		authorButton.addActionListener(e -> {
			try {
				Util.openWebpage(new URL("https://github.com/lukacupic"));
			} catch (MalformedURLException ex) {
				Util.displayErrorDialog();
			}
		});
		author.add(authorButton);

		c.gridy = 2;
		c.insets = new Insets(10, 0, 0, 0);
		aboutPanel.add(author, c);
	}
}
