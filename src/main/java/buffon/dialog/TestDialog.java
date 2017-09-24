package buffon.dialog;

import buffon.dialog.options.AbstractDialogOption;
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
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TestDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JList list;
	private JPanel cardPanel;
	private JPanel settingsPanel;
	private JPanel appereancePanel;

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
		SpinnerDialogOption option1 = new SpinnerDialogOption("Number of lines: ",
				createJSpinner(5, 3, 20, 1));

		SpinnerDialogOption option2 = new SpinnerDialogOption("Needle length: ",
				createJSpinner(50, 0, 100, 1));

		SpinnerDialogOption option3 = new SpinnerDialogOption("Number of π digits: ",
				createJSpinner(2, 2, 6, 1));

		JPanel panel = createOptionsPanel(option1, option2, option3);

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
		// add your code here
		dispose();
	}

	private void onCancel() {
		// add your code here if necessary
		dispose();
	}

	public static void main(String[] args) {
		TestDialog dialog = new TestDialog();

		try {
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (Exception ignorable) {
		}

		dialog.pack();
		dialog.setVisible(true);
		System.exit(0);
	}
}
