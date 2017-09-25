package buffon.dialog.options;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class IntSpinnerOption extends AbstractOption<Integer> {

	private JSpinner spinner;

	private ObjectWrapper optionWrapper;

	public IntSpinnerOption(String name, JSpinner spinner, ObjectWrapper optionWrapper) {
		this.name = name;
		this.spinner = spinner;
		this.optionWrapper = optionWrapper;

		spinner.addChangeListener(e -> optionWrapper.setValue(getValue()));
	}

	public static JSpinner createJSpinner(int value, int min, int max, int stepSize) {
		SpinnerModel model = new SpinnerNumberModel(value, min, max, stepSize);
		return new JSpinner(model);
	}

	@Override
	public Integer getValue() {
		return (int) spinner.getValue();
	}

	@Override
	public JSpinner getComponent() {
		return spinner;
	}
}
