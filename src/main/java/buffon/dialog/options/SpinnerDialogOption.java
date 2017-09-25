package buffon.dialog.options;

import javax.swing.JSpinner;

public class SpinnerDialogOption extends AbstractDialogOption<Integer> {

	private JSpinner spinner;

	private IntOptionWrapper optionWrapper;

	public SpinnerDialogOption(String name, JSpinner spinner, IntOptionWrapper optionWrapper) {
		this.name = name;
		this.spinner = spinner;
		this.optionWrapper = optionWrapper;

		spinner.addChangeListener(e -> optionWrapper.setValue(getValue()));
	}

	@Override
	public Integer getValue() {
		return (int) (spinner.getValue());
	}

	@Override
	public JSpinner getComponent() {
		return spinner;
	}
}
