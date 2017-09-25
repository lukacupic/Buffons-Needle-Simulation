package buffon.dialog.options;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.text.DecimalFormat;

public class DoubleSpinnerOption extends AbstractOption<Double> {

	private JSpinner spinner;

	private ObjectWrapper optionWrapper;

	public DoubleSpinnerOption(String name, JSpinner spinner, ObjectWrapper optionWrapper) {
		this.name = name;
		this.spinner = spinner;
		this.optionWrapper = optionWrapper;

		spinner.addChangeListener(e -> optionWrapper.setValue(getValue()));
	}

	public static JSpinner createJSpinner(double value, double min, double max, double stepSize) {
		SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, stepSize);
		JSpinner spinner = new JSpinner(model);
		JSpinner.NumberEditor editor = (JSpinner.NumberEditor) spinner.getEditor();
		DecimalFormat format = editor.getFormat();
		format.setMinimumFractionDigits(2);
		return spinner;
	}

	@Override
	public Double getValue() {
		return (double) spinner.getValue();
	}

	@Override
	public JSpinner getComponent() {
		return spinner;
	}
}
