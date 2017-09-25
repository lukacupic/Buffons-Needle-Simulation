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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JList list;
	private JPanel cardPanel;
	private JPanel settingsPanel;
	private JPanel aboutPanel;
	private JPanel warningPanel;

	private static Map<String, ObjectWrapper> optionsOld = new HashMap<>();

	static {
		for (Map.Entry<String, ObjectWrapper> entry : OptionsProvider.getOptions().entrySet()) {
			optionsOld.put(entry.getKey(), new ObjectWrapper(entry.getValue().getValue()));
		}
	}

	public TestDialog() {
		initGUI();
	}

	private void initGUI() {
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
		ObjectWrapper wrapper = OptionsProvider.getOption(OptionsProvider.NUMBER_OF_LINES);
		IntSpinnerOption noOfLines = new IntSpinnerOption("Number of lines: ",
				IntSpinnerOption.createJSpinner((int) wrapper.getValue(), 3, 20, 1),
				wrapper
		);

		wrapper = OptionsProvider.getOption(OptionsProvider.LENGTH_FACTOR);
		DoubleSpinnerOption needleLength = new DoubleSpinnerOption("Needle length factor: ",
				DoubleSpinnerOption.createJSpinner(((double) wrapper.getValue()), 0.00, 1.00, 0.01),
				wrapper
		);

		wrapper = OptionsProvider.getOption(OptionsProvider.NUMBER_OF_DIGITS);
		IntSpinnerOption noOfDigits = new IntSpinnerOption("Number of π digits: ",
				IntSpinnerOption.createJSpinner((int) wrapper.getValue(), 2, 6, 1),
				wrapper
		);

		JPanel panel = createOptionsPanel(noOfLines, needleLength, noOfDigits);

		settingsPanel.setLayout(new BorderLayout());
		settingsPanel.add(panel, BorderLayout.NORTH);
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

	private void initAboutPanel() {
		aboutPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		aboutPanel.add(new JLabel(UIManager.getIcon("OptionPane.warningIcon"), JLabel.CENTER), c);

		c.gridy = 1;
		c.insets = new Insets(10, 0, 0, 0);
		JLabel label = new JLabel("Buffon's Problem Simulator v2.0");
		label.setFont(new Font("default", Font.BOLD, 16));
		aboutPanel.add(label, c);

		c.gridy = 2;
		c.insets = new Insets(10, 0, 0, 0);
		aboutPanel.add(new JLabel("Coded by Luka Čupić"), c);

	}
}
