package buffon.dialog;

import buffon.Main;
import buffon.dialog.options.AbstractDialogOption;
import buffon.dialog.options.IntOptionWrapper;
import buffon.dialog.options.OptionsProvider;
import buffon.dialog.options.SpinnerDialogOption;
import buffon.util.SpringUtilities;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class TestDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JList list;
	private JPanel cardPanel;
	private JPanel settingsPanel;
	private JPanel appereancePanel;

	private static Map<String, IntOptionWrapper> optionsOld = new HashMap<>();

	static {
		for (Map.Entry<String, IntOptionWrapper> entry : OptionsProvider.getOptions().entrySet()) {
			optionsOld.put(entry.getKey(), new IntOptionWrapper(entry.getValue().getValue()));
		}
	}

	public TestDialog() {
		initGUI();
	}

	private void initGUI() {
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(e -> onOK());

		buttonCancel.addActionListener(e -> onCancel());

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
	}

	private void initSettingsPanel() {
		IntOptionWrapper wrapper = OptionsProvider.getOption("noOfLines");
		SpinnerDialogOption noOfLines = new SpinnerDialogOption("Number of lines: ",
				createJSpinner(wrapper.getValue(), 3, 20, 1),
				wrapper
		);

		wrapper = OptionsProvider.getOption("lengthFactor");
		SpinnerDialogOption needleLength = new SpinnerDialogOption("Needle length factor: ",
				createJSpinner(wrapper.getValue(), 0, 100, 1),
				wrapper
		);

		wrapper = OptionsProvider.getOption("noOfDigits");
		SpinnerDialogOption noOfDigits = new SpinnerDialogOption("Number of π digits: ",
				createJSpinner(wrapper.getValue(), 2, 6, 1),
				wrapper
		);

		JPanel panel = createOptionsPanel(noOfLines, needleLength, noOfDigits);

		settingsPanel.setLayout(new BorderLayout());
		settingsPanel.add(panel, BorderLayout.NORTH);
	}

	private JPanel createOptionsPanel(AbstractDialogOption... options) {
		JPanel p = new JPanel();
		p.setLayout(new SpringLayout());
		for (AbstractDialogOption option : options) {
			JLabel l = new JLabel(option.getName(), JLabel.TRAILING);
			p.add(l);

			l.setLabelFor(option.getComponent());
			p.add(option.getComponent());
		}
		SpringUtilities.makeCompactGrid(p, options.length, 2, 6, 6, 6, 6);
		return p;
	}

	private JSpinner createJSpinner(int value, int min, int max, int stepSize) {
		SpinnerModel model = new SpinnerNumberModel(value, min, max, stepSize);
		return new JSpinner(model);
	}

	private void onOK() {
		Main.getCanvas().resetContext();
		dispose();
	}

	private void onCancel() {
		OptionsProvider.setOptions(optionsOld);
		dispose();
	}
}
